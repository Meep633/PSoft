package hw6;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import hw5.CSVParser;
import hw4.Graph;
import hw4.Edge;

class LegoPathsTest {
	LegoPaths lp;
	Map<String, Set<Edge<String, Double>>> expectedSmallValidLegoEdges = new HashMap<String, Set<Edge<String, Double>>>();
	Map<String, Set<Edge<String, Double>>> expectedDuplicatesLegoEdges = new HashMap<String, Set<Edge<String, Double>>>();
	
	@BeforeEach
	void smallValidLegoEdges() {
		// Set A - A,B ; Set B - B,C,D ; Set C - A,B,D ; Set D - B,D ; Set E - A,C,E
		// A-A = 0.0, A-B = 1/2, A-C = 1.0, A-D = 1.0, A-E = 1.0
		// B-A = 1/2, B-B = 0.0, B-C = 1.0, B-D = 1/3, B-E = n/a
		// C-A = 1.0, C-B = 1.0, C-C = 0.0, C-D = 1.0, C-E = 1.0
		// D-A = 1.0, D-B = 1/3, D-C = 1.0, D-D = 0.0, D-E = n/a
		// E-A = 1.0, E-B = n/a, E-C = 1.0, E-D = n/a, E-E = 0.0
		Set<Edge<String, Double>> setA = new HashSet<Edge<String, Double>>();
		setA.add(new Edge<String, Double>("A", 0.0));
		setA.add(new Edge<String, Double>("B", 1.0/2));
		setA.add(new Edge<String, Double>("C", 1.0));
		setA.add(new Edge<String, Double>("D", 1.0));
		setA.add(new Edge<String, Double>("E", 1.0));
		expectedSmallValidLegoEdges.put("A", setA);
		
		Set<Edge<String, Double>> setB = new HashSet<Edge<String, Double>>();
		setB.add(new Edge<String, Double>("A", 1.0/2));
		setB.add(new Edge<String, Double>("B", 0.0));
		setB.add(new Edge<String, Double>("C", 1.0));
		setB.add(new Edge<String, Double>("D", 1.0/3));
		expectedSmallValidLegoEdges.put("B", setB);
		
		Set<Edge<String, Double>> setC = new HashSet<Edge<String, Double>>();
		setC.add(new Edge<String, Double>("A", 1.0));
		setC.add(new Edge<String, Double>("B", 1.0));
		setC.add(new Edge<String, Double>("C", 0.0));
		setC.add(new Edge<String, Double>("D", 1.0));
		setC.add(new Edge<String, Double>("E", 1.0));
		expectedSmallValidLegoEdges.put("C", setC);
		
		Set<Edge<String, Double>> setD = new HashSet<Edge<String, Double>>();
		setD.add(new Edge<String, Double>("A", 1.0));
		setD.add(new Edge<String, Double>("B", 1.0/3));
		setD.add(new Edge<String, Double>("C", 1.0));
		setD.add(new Edge<String, Double>("D", 0.0));
		expectedSmallValidLegoEdges.put("D", setD);
		
		Set<Edge<String, Double>> setE = new HashSet<Edge<String, Double>>();
		setE.add(new Edge<String, Double>("A", 1.0));
		setE.add(new Edge<String, Double>("C", 1.0));
		setE.add(new Edge<String, Double>("E", 0.0));
		expectedSmallValidLegoEdges.put("E", setE);
		
		Set<Edge<String, Double>> setF = new HashSet<Edge<String, Double>>();
		setF.add(new Edge<String, Double>("F", 0.0));
		expectedSmallValidLegoEdges.put("F", setF);
	}
	
	@BeforeEach
	void duplicatesLegoEdges() {
		// Set A - A,B ; Set B - A,B ; Set C - B,C
		// A-A = 0.0, A-B = 1.0/2, A-C = n/a
		// B-A = 1.0/2, B-B = 0.0, B-C = 1.0
		// C-A = n/a, C-B = 1.0, C-C = 0.0
		Set<Edge<String, Double>> setA = new HashSet<Edge<String, Double>>();
		setA.add(new Edge<String, Double>("A", 0.0));
		setA.add(new Edge<String, Double>("B", 1.0/2));
		expectedDuplicatesLegoEdges.put("A", setA);
		
		Set<Edge<String, Double>> setB = new HashSet<Edge<String, Double>>();
		setB.add(new Edge<String, Double>("A", 1.0/2));
		setB.add(new Edge<String, Double>("B", 0.0));
		setB.add(new Edge<String, Double>("C", 1.0));
		expectedDuplicatesLegoEdges.put("B", setB);
		
		Set<Edge<String, Double>> setC = new HashSet<Edge<String, Double>>();
		setC.add(new Edge<String, Double>("B", 1.0));
		setC.add(new Edge<String, Double>("C", 0.0));
		expectedDuplicatesLegoEdges.put("C", setC);
	}
	
	@BeforeEach
	void setup() {
		lp = new LegoPaths();
	}
	
	@Test
	void testNoArgConstructor() {
		new LegoPaths();
	}
	
	@Test
	void testCSVParser() {
		try {
			CSVParser.readData("data/lego1960.csv", new HashMap<String, Set<String>>(), new HashSet<String>());			
		} catch (Exception e) {
			fail("CSVParser read failed: " + e.getMessage());
		}
		assertThrows(IOException.class, () -> CSVParser.readData("does not exist", new HashMap<String, Set<String>>(), new HashSet<String>()));
		assertThrows(IOException.class, () -> CSVParser.readData("data/bad_format.csv", new HashMap<String, Set<String>>(), new HashSet<String>()));
	}
	
	@Test
	void testCreateNewGraph() {
		lp.createNewGraph("data/small_valid_lego.csv");
		lp.createNewGraph("data/empty_lego.csv");
		lp.createNewGraph("data/duplicates_lego.csv");
		assertThrows(NullPointerException.class, () -> lp.createNewGraph(null));
	}
	
	@Test
	void testCreateNewGraphBigData() {
		lp.createNewGraph("data/lego1960.csv");
		lp.createNewGraph("data/lego1970.csv");
		lp.createNewGraph("data/lego1980.csv");
//		lp.createNewGraph("data/lego1990.csv");
//		lp.createNewGraph("data/lego2000.csv");
//		lp.createNewGraph("data/lego2010.csv");
	}
	
	@Test
	void testUnderlyingGraph() {
		lp.createNewGraph("data/small_valid_lego.csv");
		Graph<String, Double> g = lp.underlyingGraph();
		Set<String> nodes = g.nodes();
		Map<String, Set<Edge<String, Double>>> edges = g.edges();
		
		//check for exact nodes
		assertTrue(nodes.size() == expectedSmallValidLegoEdges.keySet().size(), "Graph contains more than expected number of nodes");
		for (String node : expectedSmallValidLegoEdges.keySet()) {
			assertTrue(nodes.contains(node), "Graph doesn't contain expected node");
		}
		
		//check for exact edges
		for (String parent : nodes) {
			assertTrue(edges.get(parent).size() == expectedSmallValidLegoEdges.get(parent).size());
			for (Edge<String, Double> edge : edges.get(parent)) {
				assertTrue(expectedSmallValidLegoEdges.get(parent).contains(new Edge<String, Double>(edge.child(), edge.label())));
			}
		}
		
		//check for rep exposure
		g.addNode("New node");
		assertTrue(g.nodes().size() != lp.underlyingGraph().nodes().size(), "Adding new node to graph changed LegoPaths");
		g.addEdge("A", "B", 505.0);
		assertFalse(lp.underlyingGraph().containsEdge("A", "B", 505.0), "Adding new edge to graph changed LegoPaths");
		g.removeNode("A");
		assertTrue(lp.underlyingGraph().containsNode("A"), "Removing node from graph changed LegoPaths");
		g.removeEdge("B", "B", 0.0);
		assertTrue(lp.underlyingGraph().containsEdge("B", "B", 0.0), "Removing edge from graph changed LegoPaths");
		
		//test duplicates graph
		lp.createNewGraph("data/duplicates_lego.csv");
		g = lp.underlyingGraph();
		nodes = g.nodes();
		edges = g.edges();
		
		assertTrue(nodes.size() == expectedDuplicatesLegoEdges.keySet().size(), "Graph contains more than expected number of nodes");
		for (String node : expectedDuplicatesLegoEdges.keySet()) {
			assertTrue(nodes.contains(node), "Graph doesn't contain expected node");
		}
		for (String parent : nodes) {
			assertTrue(edges.get(parent).size() == expectedDuplicatesLegoEdges.get(parent).size());
			for (Edge<String, Double> edge : edges.get(parent)) {
				assertTrue(expectedDuplicatesLegoEdges.get(parent).contains(new Edge<String, Double>(edge.child(), edge.label())));
			}
		}
		
		//test empty graph
		lp.createNewGraph("data/empty_lego.csv");
		g = lp.underlyingGraph();
		assertTrue(g.nodes().size() == 0, "Empty graph has nodes");
	}
	
	@Test
	void testFindPath() {
		//regular graph
		lp.createNewGraph("data/small_valid_lego.csv");
		String msg = lp.findPath("A", "B");
		assertEquals(msg, "path from A to B:\nA to B with weight 0.500\ntotal cost: 0.500\n", 
				"Incorrect path outputted:\n" + msg);
		msg = lp.findPath("A", "D");
		assertEquals(msg, "path from A to D:\nA to B with weight 0.500\nB to D with weight 0.333\ntotal cost: 0.833\n", 
				"Incorrect path outputted:\n" + msg);
		msg = lp.findPath("B", "A");
		assertEquals(msg, "path from B to A:\nB to A with weight 0.500\ntotal cost: 0.500\n",
				"Incorrect path outputted:\n" + msg);
		msg = lp.findPath("A", "F");
		assertEquals(msg, "path from A to F:\nno path found\n",
				"Incorrect path outputted:\n" + msg);
		
		//graph with duplicates
		lp.createNewGraph("data/duplicates_lego.csv");
		msg = lp.findPath("A", "B");
		assertEquals(msg, "path from A to B:\nA to B with weight 0.500\ntotal cost: 0.500\n",
				"Incorrect path outputted:\n" + msg);
		msg = lp.findPath("B", "C");
		assertEquals(msg, "path from B to C:\nB to C with weight 1.000\ntotal cost: 1.000\n",
				"Incorrect path outputted:\n" + msg);
		msg = lp.findPath("A", "C");
		assertEquals(msg, "path from A to C:\nA to B with weight 0.500\nB to C with weight 1.000\ntotal cost: 1.500\n", 
				"Incorrect path outputted:\n" + msg);
		
		//empty graph
		lp.createNewGraph("data/empty_lego.csv");
		msg = lp.findPath("A", "B");
		assertEquals(msg, "unknown part A\nunknown part B\n", 
				"Incorrect path outputted:\n" + msg);
	}
	
	@Test
	void testFindPathEdgeCases() {
		lp.createNewGraph("data/small_valid_lego.csv");
		//PART1 = PART2 and both known
		String msg = lp.findPath("A", "A");
		assertEquals(msg, "path from A to A:\ntotal cost: 0.000\n", "Incorrect path outputted:\n" + msg);
		
		//PART1 unknown only
		msg = lp.findPath(":3", "A");
		assertEquals(msg, "unknown part :3\n", "Incorrect path outputted:\n" + msg);
		
		//PART2 unknown only
		msg = lp.findPath("A", ":3");
		assertEquals(msg, "unknown part :3\n", "Incorrect path outputted:\n" + msg);
		
		//PART1 != PART2 and both unknown
		msg = lp.findPath(">:3", ":3");
		assertEquals(msg, "unknown part >:3\nunknown part :3\n", "Incorrect path outputted:\n" + msg);
		
		//PART1 = PART2 and both unknown
		msg = lp.findPath(":3", ":3");
		assertEquals(msg, "unknown part :3\n", "Incorrect path outputted:\n" + msg);
		
		//null node given
		assertThrows(NullPointerException.class, () -> lp.findPath(null, "A"));
		assertThrows(NullPointerException.class, () -> lp.findPath("A", null));
		assertThrows(NullPointerException.class, () -> lp.findPath(":3", null));
		assertThrows(NullPointerException.class, () -> lp.findPath(null, null));
	}
	
	@Test
	void testBigDataFindPath() {
		lp.createNewGraph("data/lego1980.csv");
		lp.findPath("003381 [No Color/Any Color] Sticker Sheet for Set 663-1", "250 Blue Duplo Brick 2 x 6 x 2 Double Concave");
		lp.findPath("250 Blue Duplo Brick 2 x 6 x 2 Double Concave", "250 Blue Duplo Brick 2 x 6 x 2 Double Concave");
		lp.findPath("250 Blue Duplo Brick 2 x 6 x 2 Double Concave", "251pr0001 Red HO Scale Bedford ESSO Barrel Truck - Front Indicators\"");		
	}
	
	@Test
	void testBigDataFindPathEdgeCases() {
		lp.createNewGraph("data/lego1980.csv");
		String part = "003381 [No Color/Any Color] Sticker Sheet for Set 663-1";
		
		//PART1 = PART2 and both known
		String msg = lp.findPath(part, part);
		assertEquals(msg, "path from " + part + " to " + part + ":\ntotal cost: 0.000\n", "Incorrect path outputted:\n" + msg);
		
		//PART1 unknown only
		msg = lp.findPath(":3", part);
		assertEquals(msg, "unknown part :3\n", "Incorrect path outputted:\n" + msg);
		
		//PART2 unknown only
		msg = lp.findPath(part, ":3");
		assertEquals(msg, "unknown part :3\n", "Incorrect path outputted:\n" + msg);
		
		//PART1 != PART2 and both unknown
		msg = lp.findPath(">:3", ":3");
		assertEquals(msg, "unknown part >:3\nunknown part :3\n", "Incorrect path outputted:\n" + msg);
		
		//PART1 = PART2 and both unknown
		msg = lp.findPath(":3", ":3");
		assertEquals(msg, "unknown part :3\n", "Incorrect path outputted:\n" + msg);
		
		//null node given
		assertThrows(NullPointerException.class, () -> lp.findPath(null, part));
		assertThrows(NullPointerException.class, () -> lp.findPath(part, null));
		assertThrows(NullPointerException.class, () -> lp.findPath(":3", null));
		assertThrows(NullPointerException.class, () -> lp.findPath(null, null));;
	}
	
//	@Test
//	void testBigDataFindPath() {
//		lp.createNewGraph("data/lego2010.csv");
//		lp.findPath("10000 Violet Duplo Animal Brick 2 x 2 Elephant Head", "10111pr0004 Dark Blue Duplo Figure with Headset and Cap Yellow, with Dark Blue Arms, and Legs, Yellow Knee Pads Print, and Yellow Vest with ID Badge and Radio, Open Mouth Print");
//		lp.findPath("1011Apr0030 Modulex White Modulex Tile 1 x 1 with Black '0' Print with No Lining", "1011Apr0024 Modulex White Modulex Tile 1 x 1 with Black 'Y' Print with No Lining");
//		lp.findPath("12825 Black Tile Special 1 x 1 with Clip with Rounded Tips", "14210 White String with End Studs and Minifig Grips 21L");
//	}
//	
//	@Test
//	void testBigDataFindPathEdgeCases() {
//		lp.createNewGraph("data/lego2010.csv");
//		String part = "10000 Violet Duplo Animal Brick 2 x 2 Elephant Head";
//		
//		//PART1 = PART2 and both known
//		String msg = lp.findPath(part, part);
//		assertEquals(msg, "path from " + part + " to " + part + ":\ntotal cost: 0.000\n", "Incorrect path outputted:\n" + msg);
//		
//		//PART1 unknown only
//		msg = lp.findPath(":3", part);
//		assertEquals(msg, "unknown part :3\n", "Incorrect path outputted:\n" + msg);
//		
//		//PART2 unknown only
//		msg = lp.findPath(part, ":3");
//		assertEquals(msg, "unknown part :3\n", "Incorrect path outputted:\n" + msg);
//		
//		//PART1 != PART2 and both unknown
//		msg = lp.findPath(">:3", ":3");
//		assertEquals(msg, "unknown part >:3\nunknown part :3\n", "Incorrect path outputted:\n" + msg);
//		
//		//PART1 = PART2 and both unknown
//		msg = lp.findPath(":3", ":3");
//		assertEquals(msg, "unknown part :3\n", "Incorrect path outputted:\n" + msg);
//		
//		//null node given
//		assertThrows(NullPointerException.class, () -> lp.findPath(null, part));
//		assertThrows(NullPointerException.class, () -> lp.findPath(part, null));
//		assertThrows(NullPointerException.class, () -> lp.findPath(":3", null));
//		assertThrows(NullPointerException.class, () -> lp.findPath(null, null));
//	}
}