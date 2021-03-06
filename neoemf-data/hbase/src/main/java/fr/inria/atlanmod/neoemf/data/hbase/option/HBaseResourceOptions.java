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

package fr.inria.atlanmod.neoemf.data.hbase.option;

import fr.inria.atlanmod.neoemf.data.hbase.store.ReadOnlyHBaseStore;
import fr.inria.atlanmod.neoemf.option.PersistentResourceOptions;

/**
 * {@link PersistentResourceOptions} that hold HBase related resource-level features.
 */
public interface HBaseResourceOptions extends PersistentResourceOptions {

    /**
     * Only allows read operations on the underlying database.
     *
     * @see ReadOnlyHBaseStore
     */
    String READ_ONLY = "hbase.readOnly";
}