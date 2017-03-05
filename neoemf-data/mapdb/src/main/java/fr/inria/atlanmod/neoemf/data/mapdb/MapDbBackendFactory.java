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

package fr.inria.atlanmod.neoemf.data.mapdb;

import fr.inria.atlanmod.neoemf.data.AbstractPersistenceBackendFactory;
import fr.inria.atlanmod.neoemf.data.InvalidDataStoreException;
import fr.inria.atlanmod.neoemf.data.PersistenceBackend;
import fr.inria.atlanmod.neoemf.data.PersistenceBackendFactory;
import fr.inria.atlanmod.neoemf.data.mapdb.option.MapDbOptionsBuilder;
import fr.inria.atlanmod.neoemf.data.mapdb.util.MapDbURI;
import fr.inria.atlanmod.neoemf.resource.PersistentResource;
import fr.inria.atlanmod.neoemf.util.logging.Log;

import org.eclipse.emf.common.util.URI;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A factory that creates instances of {@link MapDbBackendIndices}.
 * <p>
 * As other implementations of {@link PersistenceBackendFactory}, this class can create transient and persistent
 * databases. Persistent back-end creation can be configured using {@link PersistentResource#save(Map)} and {@link
 * PersistentResource#load(Map)} option maps.
 * <p>
 * The factory handles transient back-ends by creating in-memory {@link Map} instances.
 *
 * @see PersistentResource
 * @see MapDbBackendIndices
 * @see MapDbOptionsBuilder
 */
@ParametersAreNonnullByDefault
public class MapDbBackendFactory extends AbstractPersistenceBackendFactory {

    /**
     * The literal description of the factory.
     */
    public static final String NAME = MapDbBackend.NAME;

    /**
     * Constructs a new {@code MapDbBackendFactory}.
     */
    protected MapDbBackendFactory() {
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    @Nonnull
    public static PersistenceBackendFactory getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Nonnull
    @Override
    public PersistenceBackend createTransientBackend() {
        DB db = DBMaker.memoryDB().make();
        return new MapDbBackendIndices(db);
    }

    @Nonnull
    @Override
    public PersistenceBackend createPersistentBackend(URI uri, Map<?, ?> options) throws InvalidDataStoreException {
        MapDbBackend backend;

        checkArgument(uri.isFile(), "NeoEMF/MapDB only supports file URIs");
        File file = new File(uri.toFileString());

        File dbFile = new File(MapDbURI.createURI(uri.appendSegment("neoemf.mapdb")).toFileString());
        if (!dbFile.getParentFile().exists()) {
            try {
                Files.createDirectories(dbFile.getParentFile().toPath());
            }
            catch (IOException e) {
                Log.error(e);
            }
        }

        DB db = DBMaker.fileDB(dbFile).fileMmapEnableIfSupported().make();
        backend = new MapDbBackendIndices(db);
        processGlobalConfiguration(file);

        return backend;
    }

    /**
     * The initialization-on-demand holder of the singleton of this class.
     */
    private static class Holder {

        /**
         * The instance of the outer class.
         */
        private static final PersistenceBackendFactory INSTANCE = new MapDbBackendFactory();
    }
}
