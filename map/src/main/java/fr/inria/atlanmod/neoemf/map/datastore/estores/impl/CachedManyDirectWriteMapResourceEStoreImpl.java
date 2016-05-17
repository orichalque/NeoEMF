/*******************************************************************************
 * Copyright (c) 2013 Atlanmod INRIA LINA Mines Nantes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Atlanmod INRIA LINA Mines Nantes - initial API and implementation
 *******************************************************************************/

package fr.inria.atlanmod.neoemf.map.datastore.estores.impl;

import fr.inria.atlanmod.neoemf.core.Id;
import fr.inria.atlanmod.neoemf.core.PersistentEObject;
import fr.inria.atlanmod.neoemf.logger.NeoLogger;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource.Internal;
import org.jboss.util.collection.SoftValueHashMap;
import org.mapdb.DB;
import org.mapdb.Fun.Tuple2;

import java.util.Map;

public class CachedManyDirectWriteMapResourceEStoreImpl extends DirectWriteMapResourceEStoreImpl {

	protected Map<Tuple2<Id, String>, Object> cachedArray;
	protected long founds;
	protected long notFounds;
	protected InfoThread thread;
	
	@SuppressWarnings("unchecked")
	public CachedManyDirectWriteMapResourceEStoreImpl(Internal resource, DB db) {
		super(resource, db);
		cachedArray = new SoftValueHashMap();
		thread = new InfoThread();
		thread.setDaemon(true);
		thread.start();
	}
	
	@Override
	protected Object getFromMap(PersistentEObject object, EStructuralFeature feature) {
		Tuple2<Id, String> key = new Tuple2<>(object.id(), feature.getName());
		Object returnValue = cachedArray.get(key);
		if (returnValue == null) {
			returnValue = super.getFromMap(object, feature);
			cachedArray.put(key, returnValue);
			notFounds++;
		} else {
			founds++;
		}
		return returnValue;
	}

	protected class InfoThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					sleep(20000);
				} catch (InterruptedException e) {
				}
				NeoLogger.log(NeoLogger.SEVERITY_INFO, "Founds: " + founds + " / Not Founds: " + notFounds);
			}
		}
	}
}
