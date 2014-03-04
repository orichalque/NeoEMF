package fr.inria.atlanmod.neo4emf.impl.tests;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.RelationshipType;

import fr.inria.atlanmod.neo4emf.INeo4emfResource;
import fr.inria.atlanmod.neo4emf.INeo4emfResourceFactory;
import fr.inria.atlanmod.neo4emf.Point;
import fr.inria.atlanmod.neo4emf.change.impl.Entry;
import fr.inria.atlanmod.neo4emf.drivers.NESession;
import fr.inria.atlanmod.neo4emf.impl.Neo4emfObject;
import fr.inria.atlanmod.neo4emf.testdata.Container;
import fr.inria.atlanmod.neo4emf.testdata.TestFactory;
import fr.inria.atlanmod.neo4emf.testdata.TestPackage;
import fr.inria.atlanmod.neo4emf.testdata.Vertex;
import fr.inria.atlanmod.neo4emf.testdata.reltypes.ReltypesMappings;

public class Neo4emfResourceTest {
	
	private static final File DB_FOLDER = new File("/tmp/Neo4emfResourceTest");
	private static final URI uri = URI.createURI("neo4emf:"+DB_FOLDER.getAbsolutePath());
	private static NESession session;
	
	
	private INeo4emfResource resource;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FileUtils.forceMkdir(DB_FOLDER);
		
		INeo4emfResourceFactory.eINSTANCE.setRelationshipsMap(fr.inria.atlanmod.neo4emf.testdata.reltypes.ReltypesMappings.getInstance().getMap());
		session = new NESession(TestPackage.eINSTANCE);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FileUtils.forceDelete(DB_FOLDER);
	}

	@Before
	public void setUp() throws Exception {
		resource = session.createResource(uri);
	}

	@After
	public void tearDown() throws Exception {
		resource.shutdown();
	}

	@Test
	public void testCreateAndSave() throws Exception {
		int times = 10000;
		TestPackage pack = TestPackage.eINSTANCE;
		TestFactory factory = pack.getTestFactory();
		
		Container container = factory.createContainer();
		container.setName("My Container");
		
		for (int i = 1; i < times ; i++) {
			Vertex vertex = factory.createVertex();
			vertex.setName("Vertex name ["+i+"]");
			vertex.setContainer(container);
		}

		resource.attached(container);
		System.out.println("Changelog size: " + resource.getChangeLog().size());
		
		resource.save(null);
		//resource.shutdown();


		//INeo4emfResource loaded = (INeo4emfResource) rSet.createResource(uri);
		//loaded.load(null);
		//EList<EObject> contents = loaded.getContents();
		
		//assert contents.contains(container);
		//loaded.shutdown();
		
	}

	@Test
	public void testNeo4emfURItoString() throws Exception {
		String str = "neo4emf:////a/b/c";
		URI uri = URI.createURI(str);
		
		//String result = Neo4emfResource.neo4emfURItoString(uri);
		//assert "/a/b/c/".equals(result);
	}
	
	@Test
	public void testReferences() throws Exception {
		int times = 10;
		TestPackage pack = TestPackage.eINSTANCE;
		TestFactory factory = pack.getTestFactory();
		
		Container container = factory.createContainer();
		container.setName("My Container");
		
		for (int i = 1; i < times ; i++) {
			Vertex vertex = factory.createVertex();
			vertex.setName("Vertex name ["+i+"]");
			vertex.setContainer(container);
		}

		resource.attached(container);
		System.out.println("Changelog size: " + resource.getChangeLog().size());
		for (Entry each : resource.getChangeLog().changes()) {
			System.out.println(each);
		}
		
		resource.save(null);
		//resource.shutdown();
	}
	
	
	//@Test
	public void testAttached() {
		EObject obj = new Neo4emfObject();
		//resource.attached(obj);
		
	}

	@Test
	public void testDetached() {
		EObject obj = EcoreFactory.eINSTANCE.createEObject();
		//resource.detached(obj);
	}
	
}
