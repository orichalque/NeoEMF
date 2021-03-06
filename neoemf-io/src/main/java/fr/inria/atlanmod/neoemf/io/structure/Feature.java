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

package fr.inria.atlanmod.neoemf.io.structure;

import fr.inria.atlanmod.neoemf.data.store.PersistentStore;

/**
 * A simple representation of a structural feature, which can be either a reference or an attribute.
 */
public abstract class Feature extends NamedElement {

    /**
     * The identifier of the feature.
     */
    private Identifier id;

    /**
     * The index of the feature.
     */
    private int index;

    /**
     * Whether this feature is multi-valued.
     */
    private boolean many;

    /**
     * Constructs a new {@code Feature} with the given {@code name}. Its index is initialized to
     * {@link PersistentStore#NO_INDEX}.
     *
     * @param name the name of this feature
     */
    public Feature(String name) {
        super(name);
        this.index = PersistentStore.NO_INDEX;
    }

    /**
     * Returns the identifier of this feature.
     *
     * @return the identifier
     */
    public Identifier id() {
        return id;
    }

    /**
     * Defines the identifier of this feature.
     *
     * @param id the identifier
     */
    public void id(Identifier id) {
        this.id = id;
    }

    /**
     * Returns the index of this feature.
     *
     * @return the index
     */
    public int index() {
        return index;
    }

    /**
     * Defines the index of this feature.
     *
     * @param index the index
     */
    public void index(int index) {
        this.index = index;
    }

    /**
     * Sets whether this feature is multi-valued, or not.
     *
     * @return {@code true} if this feature is multi-valued
     */
    public boolean many() {
        return many;
    }

    /**
     * Defines whether this feature is multi-valued.
     *
     * @param many {@code true} if this feature is multi-valued
     */
    public void many(boolean many) {
        this.many = many;
    }

    /**
     * Defines whether this feature is a {@link Reference}.
     *
     * @return {@code true} if this feature is a reference.
     */
    public boolean isReference() {
        return false;
    }

    /**
     * Defines whether this feature is an {@link Attribute}.
     *
     * @return {@code true} if this feature is an attribute.
     */
    public boolean isAttribute() {
        return false;
    }
}
