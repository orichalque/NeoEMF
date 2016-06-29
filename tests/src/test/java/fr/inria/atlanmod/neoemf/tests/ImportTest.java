/*
 * Copyright (c) 2013 Atlanmod INRIA LINA Mines Nantes.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 */

package fr.inria.atlanmod.neoemf.tests;

import fr.inria.atlanmod.neoemf.graph.blueprints.datastore.BlueprintsPersistenceBackend;
import fr.inria.atlanmod.neoemf.graph.blueprints.datastore.BlueprintsPersistenceBackendFactory;
import fr.inria.atlanmod.neoemf.graph.blueprints.io.input.BlueprintsPersistenceHandlerNoConflict;
import fr.inria.atlanmod.neoemf.graph.blueprints.neo4j.resources.BlueprintsNeo4jResourceOptions;
import fr.inria.atlanmod.neoemf.graph.blueprints.resources.BlueprintsResourceOptions;
import fr.inria.atlanmod.neoemf.io.AllIOTest;
import fr.inria.atlanmod.neoemf.io.IOFactory;
import fr.inria.atlanmod.neoemf.io.PersistenceHandler;
import fr.inria.atlanmod.neoemf.io.impl.CounterDelegatedPersistenceHandler;
import fr.inria.atlanmod.neoemf.logger.NeoLogger;
import fr.inria.atlanmod.neoemf.resources.PersistentResourceOptions;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportTest extends AllIOTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File neo4jFile;

    @Before
    public void setUp() throws Exception {
        String timestamp = String.valueOf(new Date().getTime());
        neo4jFile = temporaryFolder.getRoot().toPath().resolve("import-Neo4j" + timestamp).toFile();
    }

    @Test
    public void testImportNeo4jSet1() throws Exception {
        testImportWithSax(getSet1(), getNeo4jHandler());
    }

    @Test
    public void testImportNeo4jSet2() throws Exception {
        testImportWithSax(getSet2(), getNeo4jHandler());
    }

    @Test
    public void testImportNeo4jSet3() throws Exception {
        testImportWithSax(getSet3(), getNeo4jHandler());
    }

    @Test
    @Ignore("XMI file not present in commit")
    public void testImportNeo4jSet4() throws Exception {
        testImportWithSax(getSet4(), getNeo4jHandler());
    }

    @Test
    @Ignore("XMI file not present in commit")
    public void testImportNeo4jSet5() throws Exception {
        testImportWithSax(getSet5(), getNeo4jHandler());
    }

    private PersistenceHandler getNeo4jHandler() throws Exception {
        Map<String, Object> options = new HashMap<>();

        List<PersistentResourceOptions.StoreOption> storeOptions = new ArrayList<>();
        storeOptions.add(BlueprintsResourceOptions.EStoreGraphOption.DIRECT_WRITE);

        options.put(BlueprintsResourceOptions.OPTIONS_BLUEPRINTS_GRAPH_TYPE,
                BlueprintsNeo4jResourceOptions.OPTIONS_BLUEPRINTS_TYPE_NEO4J);

        options.put(PersistentResourceOptions.STORE_OPTIONS, storeOptions);

        options.put(BlueprintsNeo4jResourceOptions.OPTIONS_BLUEPRINTS_NEO4J_CACHE_TYPE,
                BlueprintsNeo4jResourceOptions.CacheType.NONE);

        BlueprintsPersistenceBackend neo4jBackend = (BlueprintsPersistenceBackend)
                BlueprintsPersistenceBackendFactory.getInstance().createPersistentBackend(neo4jFile, options);

        return new CounterDelegatedPersistenceHandler(new BlueprintsPersistenceHandlerNoConflict(neo4jBackend), "blueprints1");
    }

    private void testImportWithSax(File file, PersistenceHandler persistenceHandler) throws Exception {
        registerJavaEPackage();

        try {
            IOFactory.importXmi(file, persistenceHandler);
        } catch (Exception e) {
            NeoLogger.error(e);
            throw e;
        }
    }
}
