/*
 * Copyright (c) 2013-2017 Atlanmod INRIA LINA Mines Nantes.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 */

package fr.inria.atlanmod.neoemf.io.reader;

import com.google.common.base.Splitter;

import fr.inria.atlanmod.neoemf.io.Handler;
import fr.inria.atlanmod.neoemf.io.structure.Attribute;
import fr.inria.atlanmod.neoemf.io.structure.Element;
import fr.inria.atlanmod.neoemf.io.structure.Feature;
import fr.inria.atlanmod.neoemf.io.structure.Identifier;
import fr.inria.atlanmod.neoemf.io.structure.MetaClass;
import fr.inria.atlanmod.neoemf.io.structure.Namespace;
import fr.inria.atlanmod.neoemf.io.structure.Reference;
import fr.inria.atlanmod.neoemf.io.util.XmiConstants;
import fr.inria.atlanmod.neoemf.util.logging.NeoLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import static java.util.Objects.nonNull;

/**
 * An abstract {@link Reader} that processes the raw structure of XMI files.
 */
public abstract class AbstractXmiReader extends AbstractReader {

    /**
     * A regex pattern of an attribute containing one or several references (XPath reference). Multiple references must
     * be separated by a space.
     * <p>
     * Example of recognized strings : {@code "//@&lt;name1&gt;.&lt;index1&gt;/@&lt;name2&gt;"}
     */
    private static final Pattern PATTERN_WELL_FORMED_REF =
            Pattern.compile("(/{1,2}@\\w+(\\.\\d+)?[ ]?)+", Pattern.UNICODE_CASE);

    /**
     * A regex pattern of a prefixed value.
     * <p>
     * Example of recognized strings : {@code "&lt;prefix&gt;:&lt;name&gt;"}
     */
    private static final Pattern PATTERN_PREFIXED_VALUE =
            Pattern.compile("(\\w+):(\\w+)");

    /**
     * Whether the current element has to be ignored.
     * <p>
     * Used when a special or unmanaged feature is encountered.
     */
    private boolean ignoreElement = false;

    /**
     * The current element.
     */
    private Element currentElement;

    /**
     * A collection that holds all features of the {@link #currentElement}.
     */
    private Collection<Feature> currentFeatures;

    /**
     * Constructs a new {@code AbstractXmiReader} with the given {@code handler}.
     *
     * @param handler the handler to notify
     */
    public AbstractXmiReader(Handler handler) {
        super(handler);
    }

    /**
     * Starts a new element.
     * <p>
     * <b>Note:</b> An element must be flushed with the {@link #flushStartElement()} method after analysing all its
     * attributes.
     *
     * @param uri  the URI of the element
     * @param name the name of the element
     */
    protected void readStartElement(String uri, String name) {
        currentElement = new Element(Namespace.Registry.getInstance().getFromUri(uri), name);
        currentFeatures = new ArrayList<>();
    }

    /**
     * Reads a new attribute of the current element.
     *
     * @param prefix the prefix of the attribute
     * @param name   the name of the attribute
     * @param value  the value of the attribute
     */
    protected void readAttribute(String prefix, String name, String value) {
        if (!ignoreElement) {
            Collection<Feature> localFeatures = getFeatures(prefix, name, value);

            if (ignoreElement) {
                // No need to go further
                return;
            }

            if (!localFeatures.isEmpty()) {
                currentFeatures.addAll(localFeatures);
            }
        }
    }

    /**
     * Finalizes the current element and sends notifications to handlers.
     */
    protected void flushStartElement() {
        if (!ignoreElement) {
            // Notifies the current element
            notifyStartElement(currentElement);

            // Notifies the features
            for (Feature feature : currentFeatures) {
                if (feature.isAttribute()) {
                    notifyAttribute((Attribute) feature);
                }
                else {
                    notifyReference((Reference) feature);
                }
            }

            // Reset the current element/features
            currentElement = null;
            currentFeatures = null;
        }
    }

    /**
     * Processes the end of an element.
     */
    protected void readEndElement() {
        if (!ignoreElement) {
            notifyEndElement();
        }
        else {
            ignoreElement = false;
        }
    }

    /**
     * Processes a feature, which can be an attribute or a reference.
     *
     * @param prefix the prefix of the feature
     * @param name   the name of the feature
     * @param value  the value of the feature
     *
     * @return a list of {@link Feature} that can be empty.
     *
     * @see #getAttribute(String, String)
     * @see #getReferences(String, Iterable)
     */
    @Nonnull
    private Collection<Feature> getFeatures(String prefix, String name, String value) {
        Collection<Feature> features;

        if (!processSpecialFeature(prefix, name, value)) {
            Collection<String> references = parseReference(value);

            if (!references.isEmpty()) {
                features = getReferences(name, references);
            }
            else {
                features = getAttribute(name, value);
            }
        }
        else {
            features = Collections.emptyList();
        }

        return features;
    }

    /**
     * Processes a special feature as 'xsi:type', 'xmi:id' or 'xmi:idref'.
     *
     * @param prefix the prefix of the feature
     * @param name   the name of the feature
     * @param value  the value of the feature
     *
     * @return {@code true} if the given feature is a special feature
     */
    private boolean processSpecialFeature(String prefix, String name, String value) {
        boolean isSpecialFeature = false;

        // A special feature always has a prefix
        if (nonNull(prefix) && !prefix.isEmpty()) {
            final String prefixedValue = prefix + ':' + name;

            if (prefixedValue.matches(XmiConstants.XMI_XSI_TYPE)) { // xsi:type or xsi:type
                processMetaClass(value);
                isSpecialFeature = true;
            }
            else if (Objects.equals(XmiConstants.XMI_ID, prefixedValue)) { // xmi:id
                currentElement.id(Identifier.original(value));
                isSpecialFeature = true;
            }
            else if (Objects.equals(XmiConstants.XMI_IDREF, prefixedValue)) { // xmi:idref
                // It's not a feature of the current element, but a reference of the previous
                Reference reference = new Reference(currentElement.name());
                reference.idReference(Identifier.original(value));
                notifyReference(reference);
                ignoreElement = true;
                isSpecialFeature = true;
            }
            else if (Objects.equals(XmiConstants.XMI_VERSION_ATTR, prefixedValue)) { // xmi:version
                NeoLogger.debug("XMI version : " + value);
                isSpecialFeature = true;
            }
        }
        else if (Objects.equals(XmiConstants.HREF, name)) {
            NeoLogger.warn(
                    "{0} is an external reference to {1}. This feature is not supported yet.",
                    currentElement.name(), value);
            ignoreElement = true;
        }
        else if (Objects.equals(XmiConstants.NAME, name)) {
            currentElement.className(value);
            isSpecialFeature = true;
        }

        return isSpecialFeature;
    }

    /**
     * Returns a list of {@link String} representing XPath references, or {@code null} if the {@code attribute} does not
     * match with {@link #PATTERN_WELL_FORMED_REF}.
     *
     * @param attribute the attribute to parse
     *
     * @return a list of {@link String} representing XPath references, or {@code null} if the {@code attribute} does not
     * match with {@link #PATTERN_WELL_FORMED_REF}
     *
     * @see #PATTERN_WELL_FORMED_REF
     */
    @Nonnull
    private Collection<String> parseReference(String attribute) {
        List<String> references;

        if (!attribute.trim().isEmpty()) {
            references = Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(attribute);

            boolean isReference = true;
            for (int i = 0, referenceCount = references.size(); i < referenceCount && isReference; i++) {
                String ref = references.get(i);
                isReference = PATTERN_WELL_FORMED_REF.matcher(ref).matches();
            }

            if (!isReference) {
                references = Collections.emptyList();
            }
        }
        else {
            references = Collections.emptyList();
        }

        return references;
    }

    /**
     * Processes an attribute.
     *
     * @param name  the name of the attribute
     * @param value the value of the attribute
     *
     * @return a singleton list of {@link Feature} containing the processed attribute.
     */
    @Nonnull
    private Collection<Feature> getAttribute(String name, String value) {
        Attribute attribute = new Attribute(name);
        attribute.index(0);
        attribute.value(value);

        return Collections.singleton(attribute);
    }

    /**
     * Processes a list of {@code references} and returns a list of {@link Reference}.
     *
     * @param name       the name of the reference
     * @param references the list that holds the identifier of referenced elements
     *
     * @return a list of {@link Reference} from the given {@code references}
     */
    @Nonnull
    private Collection<Feature> getReferences(String name, Iterable<String> references) {
        Collection<Feature> features = new ArrayList<>();

        int index = 0;
        for (String rawReference : references) {
            Reference ref = new Reference(name);
            ref.index(index);
            ref.idReference(Identifier.generated(rawReference));
            features.add(ref);
            index++;
        }

        return features;
    }

    /**
     * Processes a metaclass attribute from the {@code prefixedValue}, and defines is as the metaclass of the given
     * {@code element}.
     *
     * @param prefixedValue the value representing the metaclass
     *
     * @see #PATTERN_PREFIXED_VALUE
     */
    private void processMetaClass(String prefixedValue) {
        Matcher m = PATTERN_PREFIXED_VALUE.matcher(prefixedValue);
        if (m.find()) {
            Namespace ns = Namespace.Registry.getInstance().getFromPrefix(m.group(1));
            String name = m.group(2);

            MetaClass metaClass = new MetaClass(ns, name);
            currentElement.metaClass(metaClass);
        }
        else {
            throw new IllegalArgumentException("Malformed metaclass " + prefixedValue);
        }
    }
}
