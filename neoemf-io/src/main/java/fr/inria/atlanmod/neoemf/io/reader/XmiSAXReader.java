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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.Attribute;

import static java.util.Objects.nonNull;

/**
 * A {@link Reader} that uses streams for reading and parsing XMI files.
 */
public class XmiSAXReader extends AbstractXmiReader {

    /**
     * Constructs a new {@code XmiSAXReader} with the given {@code handler}.
     *
     * @param handler the handler to notify
     */
    public XmiSAXReader(Handler handler) {
        super(handler);
    }

    @Override
    public void run(InputStream stream) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);

        factory.newSAXParser().parse(stream, new SAXHandler());
    }

    /**
     * The real implementation of the XMI parser.
     */
    private class SAXHandler extends DefaultHandler {

        /**
         * The factory to create the {@link Attribute}s.
         */
        private final XMLEventFactory factory = XMLEventFactory.newFactory();

        @Override
        public void startDocument() throws SAXException {
            readStartDocument();
        }

        @Override
        public void endDocument() throws SAXException {
            readEndDocument();
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            readNamespace(prefix, uri);
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes attributesRaw) throws SAXException {
            readStartElement(uri, name);

            int attributeCount = attributesRaw.getLength();
            for (int i = 0; i < attributeCount; i++) {
                readAttribute(
                        getPrefix(attributesRaw.getQName(i)),
                        attributesRaw.getLocalName(i),
                        attributesRaw.getValue(i));
            }

            flushStartElement();
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            readEndElement();
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String characters = String.valueOf(ch, start, length).trim();
            if (!characters.isEmpty()) {
                readCharacters(characters);
            }
        }

        /**
         * Returns the prefix of the {@code prefixedValue}, or {@code null} if there is no prefix.
         *
         * @param prefixedValue the value from which to extract the prefix
         *
         * @return the prefix of the {@code prefixedValue}, or {@code null} if there is no prefix
         */
        private String getPrefix(String prefixedValue) {
            String prefix = null;

            if (nonNull(prefixedValue)) {
                List<String> splittedName = Splitter.on(":").omitEmptyStrings().trimResults().splitToList(prefixedValue);
                if (splittedName.size() > 1) {
                    prefix = splittedName.get(0);
                }
            }

            return prefix;
        }
    }
}
