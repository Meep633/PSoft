package hw7;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hw4.Graph;
import hw4.Edge;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.io.IOException;

class RPIMapTest {
	RPIMap map;
	
	@BeforeEach
	void setup() {
		map = new RPIMap();
	}
	
	void createGraph() throws IOException {
		map.createNewGraph("data/rpi_map_nodes_test.csv", "data/rpi_map_edges_test.csv");			
	}
	
	@Test
	void testEmptyConstructor() {
		new RPIMap();
	}

	@Test
	void testCreateNewGraph() {
		try {
			map.createNewGraph("data/rpi_map_nodes_test.csv", "data/rpi_map_edges_test.csv");
			map.createNewGraph("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");
			map.createNewGraph("data/rpi_map_simple_nodes.csv", "data/rpi_map_simple_edges.csv");
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		assertThrows(NullPointerException.class, () -> map.createNewGraph(null, "data/rpi_map_simple_edges.csv"));
		assertThrows(NullPointerException.class, () -> map.createNewGraph("data/rpi_map_simple_nodes.csv", null));
		assertThrows(NullPointerException.class, () -> map.createNewGraph(null, null));
		
		assertThrows(IOException.class, () -> map.createNewGraph("dne", "data/rpi_map_simple_edges.csv"));
		assertThrows(IOException.class, () -> map.createNewGraph("data/rpi_map_simple_nodes.csv", "dne"));
		assertThrows(IOException.class, () -> map.createNewGraph("dne", "dne"));
		assertThrows(IOException.class, () -> map.createNewGraph("data/rpi_map_simple_edges.csv", "data/rpi_map_simple_nodes.csv"));
		
		assertThrows(IllegalArgumentException.class, () -> map.createNewGraph("data/rpi_map_simple_nodes.csv", "data/rpi_map_simple_edges_err1.csv"));
		assertThrows(IllegalArgumentException.class, () -> map.createNewGraph("data/rpi_map_simple_nodes.csv", "data/rpi_map_simple_edges_err2.csv"));
	}
	
	@Test
	void testUnderlyingGraph() {
		try {
			createGraph();			
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		Graph<RPIMapNode, Double> expectedGraph = new Graph<RPIMapNode, Double>();
		RPIMapNode n0 = new RPIMapNode("name?", 0, 1, 2);
		RPIMapNode n1 = new RPIMapNode("name", 1, 2, 2);
		RPIMapNode n2 = new RPIMapNode("another name", 2, 4, 2);
		RPIMapNode n3 = new RPIMapNode("yet another name", 3, 10, 15);
		RPIMapNode n4 = new RPIMapNode(":3", 4, 3, 6);
		RPIMapNode n5 = new RPIMapNode("kenny wu is a pretty cool guy", 5, 100, 100);
		RPIMapNode n6 = new RPIMapNode("thanks psoft mentors", 6, 40, 60);
		RPIMapNode n7 = new RPIMapNode("", 7, 40, 40);
		RPIMapNode n8 = new RPIMapNode("", 8, 4, 4);
		expectedGraph.addNode(n0);
		expectedGraph.addNode(n1);
		expectedGraph.addNode(n2);
		expectedGraph.addNode(n3);
		expectedGraph.addNode(n4);
		expectedGraph.addNode(n5);
		expectedGraph.addNode(n6);
		expectedGraph.addNode(n7);
		expectedGraph.addNode(n8);
		expectedGraph.addEdge(n0, n1, Formulas.euclideanDistance(n0.getX(), n0.getY(), n1.getX(), n1.getY()));
		expectedGraph.addEdge(n0, n2, Formulas.euclideanDistance(n0.getX(), n0.getY(), n2.getX(), n2.getY()));
		expectedGraph.addEdge(n0, n4, Formulas.euclideanDistance(n0.getX(), n0.getY(), n4.getX(), n4.getY()));
		expectedGraph.addEdge(n1, n0, Formulas.euclideanDistance(n1.getX(), n1.getY(), n0.getX(), n0.getY()));
		expectedGraph.addEdge(n1, n2, Formulas.euclideanDistance(n1.getX(), n1.getY(), n2.getX(), n2.getY()));
		expectedGraph.addEdge(n1, n3, Formulas.euclideanDistance(n1.getX(), n1.getY(), n3.getX(), n3.getY()));
		expectedGraph.addEdge(n2, n0, Formulas.euclideanDistance(n2.getX(), n2.getY(), n0.getX(), n0.getY()));
		expectedGraph.addEdge(n2, n1, Formulas.euclideanDistance(n2.getX(), n2.getY(), n1.getX(), n1.getY()));
		expectedGraph.addEdge(n2, n3, Formulas.euclideanDistance(n2.getX(), n2.getY(), n3.getX(), n3.getY()));
		expectedGraph.addEdge(n2, n8, Formulas.euclideanDistance(n2.getX(), n2.getY(), n8.getX(), n8.getY()));
		expectedGraph.addEdge(n3, n1, Formulas.euclideanDistance(n3.getX(), n3.getY(), n1.getX(), n1.getY()));
		expectedGraph.addEdge(n3, n2, Formulas.euclideanDistance(n3.getX(), n3.getY(), n2.getX(), n2.getY()));
		expectedGraph.addEdge(n3, n5, Formulas.euclideanDistance(n3.getX(), n3.getY(), n5.getX(), n5.getY()));
		expectedGraph.addEdge(n4, n0, Formulas.euclideanDistance(n4.getX(), n4.getY(), n0.getX(), n0.getY()));
		expectedGraph.addEdge(n5, n3, Formulas.euclideanDistance(n5.getX(), n5.getY(), n3.getX(), n3.getY()));
		expectedGraph.addEdge(n6, n7, Formulas.euclideanDistance(n6.getX(), n6.getY(), n7.getX(), n7.getY()));
		expectedGraph.addEdge(n7, n6, Formulas.euclideanDistance(n7.getX(), n7.getY(), n6.getX(), n6.getY()));
		expectedGraph.addEdge(n8, n2, Formulas.euclideanDistance(n8.getX(), n8.getY(), n2.getX(), n2.getY()));
		
		Graph<RPIMapNode, Double> g = map.underlyingGraph();
		
		Set<RPIMapNode> nodes = g.nodes();
		assertTrue(nodes.size() == expectedGraph.nodes().size(), "Unexpected number of nodes");
		for (RPIMapNode node : nodes) {
			assertTrue(expectedGraph.containsNode(node), "Unexpected node in graph");
		}
		
		Map<RPIMapNode, Set<Edge<RPIMapNode, Double>>> edges = g.edges();
		Map<RPIMapNode, Set<Edge<RPIMapNode, Double>>> expectedEdges = expectedGraph.edges();
		assertTrue(edges.keySet().size() == expectedEdges.keySet().size(), "Unexpected number of keys");
		for (RPIMapNode parent : edges.keySet()) {
			assertTrue(edges.get(parent).size() == expectedEdges.get(parent).size(), "Unexpected number of edges");
			for (Edge<RPIMapNode, Double> edge : edges.get(parent)) {
				assertTrue(expectedGraph.containsEdge(parent, edge.child(), edge.label()), "Unexpected edge in graph");
			}
		}
	}

	@Test
	void testGetBuildings() {
		try {
			createGraph();			
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		Map<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>> expectedBuildings = 
				new HashMap<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>>();
		SimpleEntry<Integer, Integer> coords = new SimpleEntry<Integer, Integer>(1,2);
		SimpleEntry<String, SimpleEntry<Integer, Integer>> nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("name?", coords);
		expectedBuildings.put(0, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(2,2);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("name", coords);
		expectedBuildings.put(1, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(4,2);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("another name", coords);
		expectedBuildings.put(2, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(10,15);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("yet another name", coords);
		expectedBuildings.put(3, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(3,6);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>(":3", coords);
		expectedBuildings.put(4, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(100,100);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("kenny wu is a pretty cool guy", coords);
		expectedBuildings.put(5, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(40,60);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("thanks psoft mentors", coords);
		expectedBuildings.put(6, nameEntry);
		
		Map<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>> buildings = map.getBuildings();
		
		assertTrue(buildings.keySet().size() == expectedBuildings.keySet().size(), "Unexpected number of buildings");
		for (Integer id : buildings.keySet()) {
			assertTrue(expectedBuildings.containsKey(id), "Unexpected building ID");
			assertTrue(buildings.get(id).equals(expectedBuildings.get(id)), "Unexpected building information");
		}
	}
	
	@Test
	void testGetId() {
		try {
			createGraph();
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		String[] names = {"name?", "name", "another name", "yet another name", ":3",
				"kenny wu is a pretty cool guy", "thanks psoft mentors"};
		for (int i = 0; i < 7; i++) {
			Integer id = map.getId(names[i]);
			assertTrue(id != null, "Incorrect building ID");
			assertEquals(id, i, "Incorrect building ID");
		}
		assertTrue(map.getId("huh") == null, "Building ID found for name not in map");
		assertThrows(IllegalArgumentException.class, () -> map.getId(""));
		assertThrows(NullPointerException.class, () -> map.getId(null));
	}
	
	@Test
	void testGetBuildingName() {
		try {
			createGraph();
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		String[] names = {"name?", "name", "another name", "yet another name", ":3",
				"kenny wu is a pretty cool guy", "thanks psoft mentors"};
		for (int i = 0; i < 7; i++) {
			String name = map.getName(i);
			assertTrue(name != null && name.equals(names[i]), "Incorrect building name");
		}
		assertTrue(map.getName(-1) == null, "Building name found for ID not in map");
		assertThrows(IllegalArgumentException.class, () -> map.getName(7));
	}
	
	@Test
	void testGetBuildingCoords() {
		try {
			createGraph();
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		Integer[][] coords = {{1,2}, {2,2}, {4,2}, {10,15}, {3,6}, {100,100}, {40,60}, {40,40}, {4,4}};
		for (int i = 0; i < 9; i++) {
			SimpleEntry<Integer, Integer> coord = map.getCoords(i);
			assertTrue(coord != null && coord.getKey().equals(coords[i][0]) && coord.getValue().equals(coords[i][1]),
					"Incorrect coords");
		}
		assertTrue(map.getCoords(-1) == null, "Building coords found for ID not in map");
	}
	
	@Test
	void testContainsBuilding() {
		try {
			map.createNewGraph("data/rpi_map_simple_nodes.csv", "data/rpi_map_simple_edges.csv");
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		int[] ids = {0,1,3,4,6,7,8,9};
		for (int i = 0; i < 8; i++) {
			assertTrue(map.containsBuilding(ids[i]), "Map doesn't contain expected building ID");			
		}
		assertFalse(map.containsBuilding(2), "Map contains unexpected building ID");
		assertFalse(map.containsBuilding(5), "Map contains unexpected building ID");
		assertFalse(map.containsBuilding(-1), "Map contains unexpected building ID");
	}
	
	@Test
	void testGetIntersections() {
		try {
			createGraph();			
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		Map<Integer, SimpleEntry<Integer, Integer>> expectedIntersections = 
				new HashMap<Integer, SimpleEntry<Integer, Integer>>();
		expectedIntersections.put(7, new SimpleEntry<Integer, Integer>(40,40));
		expectedIntersections.put(8, new SimpleEntry<Integer, Integer>(4,4));
		
		Map<Integer, SimpleEntry<Integer, Integer>> intersections = map.getIntersections();
		
		assertTrue(intersections.keySet().size() == expectedIntersections.keySet().size(), 
				"Unexpected number of intersections");
		for (Integer id : expectedIntersections.keySet()) {
			assertTrue(intersections.containsKey(id), "Intersections doesn't contain expected ID");
			assertTrue(intersections.get(id).equals(expectedIntersections.get(id)), 
					"Intersections doesn't contain expected coords");
		}
	}
	
	@Test
	void testContainsIntersection() {
		try {
			map.createNewGraph("data/rpi_map_simple_nodes.csv", "data/rpi_map_simple_edges.csv");
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		for (int i = 0; i < 10; i++) {
			if (i == 2 || i == 5) {
				assertTrue(map.containsIntersection(i), "Map doesn't contain expected intersection ID");
			} else {
				assertFalse(map.containsIntersection(i), "Map contains unexpected intersection ID");
			}
		}
		assertFalse(map.containsIntersection(-1), "Map contains unexpected intersection ID");
	}
	
	@Test
	void testShortestPath() {
		try {
			map.createNewGraph("data/rpi_map_simple_nodes.csv", "data/rpi_map_simple_edges.csv");
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
		
		//normal path
		List<SimpleEntry<Integer, Double>> path = map.shortestPath(0,1);
		assertTrue(path != null, "No path found");
		assertTrue(path.size() == 1, "Incorrect number of edges in path");
		assertTrue(path.get(0).getKey() == 1, "Incorrect building/intersection ID");
		assertTrue(path.get(0).getValue().equals(1.0), "Incorrect edge weight");
		
		path = map.shortestPath(0,3);
		assertTrue(path != null, "No path found");
		assertTrue(path.size() == 3, "Incorrect number of edges in path");
		assertTrue(path.get(0).getKey() == 1, "Incorrect building/intersection ID");
		assertTrue(path.get(0).getValue().equals(1.0), "Incorrect edge weight");
		assertTrue(path.get(1).getKey() == 2, "Incorrect building/intersection ID");
		assertTrue(path.get(1).getValue().equals(1.0), "Incorrect edge weight");
		assertTrue(path.get(2).getKey() == 3, "Incorrect building/intersection ID");
		assertTrue(path.get(2).getValue().equals(1.0), "Incorrect edge weight");
		
		path = map.shortestPath(0,7);
		assertTrue(path != null, "No path found");
		assertTrue(path.size() == 4, "Incorrect number of edges in path");
		assertTrue(path.get(0).getKey() == 4, "Incorrect building/intersection ID");
		assertTrue(path.get(0).getValue().equals(1.0), "Incorrect edge weight");
		assertTrue(path.get(1).getKey() == 5, "Incorrect building/intersection ID");
		assertTrue(path.get(1).getValue().equals(1.0), "Incorrect edge weight");
		assertTrue(path.get(2).getKey() == 6, "Incorrect building/intersection ID");
		assertTrue(path.get(2).getValue().equals(1.0), "Incorrect edge weight");
		assertTrue(path.get(3).getKey() == 7, "Incorrect building/intersection ID");
		assertTrue(path.get(3).getValue().equals(1.0), "Incorrect edge weight");
		
		//path to self
		path = map.shortestPath(0,0);
		assertTrue(path != null, "No path found");
		assertTrue(path.size() == 0, "Incorrect number of edges in path");
		
		//no path
		path = map.shortestPath(0,8);
		assertTrue(path == null, "Path found for disconnected buildings");
		
		//id not in map (including intersection id)
		assertThrows(IllegalArgumentException.class, () -> map.shortestPath(-1,0));
		assertThrows(IllegalArgumentException.class, () -> map.shortestPath(0,-1));
		assertThrows(IllegalArgumentException.class, () -> map.shortestPath(-1,-1));
		assertThrows(IllegalArgumentException.class, () -> map.shortestPath(0,2));
		assertThrows(IllegalArgumentException.class, () -> map.shortestPath(2,0));
		assertThrows(IllegalArgumentException.class, () -> map.shortestPath(5,2));
	}
}
