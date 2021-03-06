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

package fr.inria.atlanmod.neoemf.data;

import fr.inria.atlanmod.neoemf.AbstractTest;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PersistenceBackendFactoryRegistryTest extends AbstractTest {

    private static final String MOCK_1 = "mock1";
    private static final String MOCK_2 = "mock2";

    private final PersistenceBackendFactory persistenceBackendFactory1 = mock(PersistenceBackendFactory.class);
    private final PersistenceBackendFactory persistenceBackendFactory2 = mock(PersistenceBackendFactory.class);

    @Before
    public void unregisterFactories() {
        PersistenceBackendFactoryRegistry.unregisterAll();
    }

    @Test
    public void testSingleAdd() {
        PersistenceBackendFactoryRegistry.register(MOCK_1, persistenceBackendFactory1);
        assertThat(PersistenceBackendFactoryRegistry.getFactories()).hasSize(1);

        PersistenceBackendFactory registeredFactory = PersistenceBackendFactoryRegistry.getFactoryProvider(MOCK_1);
        assertThat(registeredFactory).isNotNull();
        assertThat(registeredFactory).isSameAs(persistenceBackendFactory1);
    }

    @Test
    public void testMultipleAdd() {
        PersistenceBackendFactoryRegistry.register(MOCK_1, persistenceBackendFactory1);
        PersistenceBackendFactoryRegistry.register(MOCK_2, persistenceBackendFactory2);
        assertThat(PersistenceBackendFactoryRegistry.getFactories()).hasSize(2);

        PersistenceBackendFactory registeredFactory1 = PersistenceBackendFactoryRegistry.getFactoryProvider(MOCK_1);
        assertThat(registeredFactory1).isNotNull();
        assertThat(registeredFactory1).isSameAs(persistenceBackendFactory1);

        PersistenceBackendFactory registeredFactory2 = PersistenceBackendFactoryRegistry.getFactoryProvider(MOCK_2);
        assertThat(registeredFactory2).isNotNull();
        assertThat(registeredFactory2).isSameAs(persistenceBackendFactory2);
    }
}
