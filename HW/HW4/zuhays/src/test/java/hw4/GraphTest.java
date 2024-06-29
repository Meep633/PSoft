package hw4;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

class GraphTest {
	Map<String, Set<Edge<String, String>>> validEdges1; //regular nodes + edges
	Map<String, Set<Edge<String, String>>> validEdges2; //duplicate nodes + edges
	String constNode1 = "1";
	String constNode2 = "2";
	
	@BeforeEach
	public void setup() {
		validEdges1 = new HashMap<String, Set<Edge<String, String>>>();
		for (int i = 1; i < 5; i++) {
			Set<Edge<String, String>> edgeSet = new HashSet<Edge<String, String>>();
			for (int j = 1; j < 5; j++) {
				edgeSet.add(new Edge<String, String>(Integer.toString(j), Integer.toString(i)));
			}
			validEdges1.put(Integer.toString(i), edgeSet);
		}
		
		validEdges2 = new HashMap<String, Set<Edge<String, String>>>();
		for (int i = 1; i < 5; i++) {
			Set<Edge<String, String>> edgeSet = new HashSet<Edge<String, String>>();
			for (int j = 1; j < 5; j++) {
				edgeSet.add(new Edge<String, String>("dupe", "dupe"));
			}
			validEdges2.put("dupe", edgeSet);
		}
	}

	@Test
	void testNoArgConstructor() {
		new Graph<String, String>();
		new Graph<Integer, Integer>();
		new Graph<String, Integer>();
	}
	
	@Test
	void testCopyConstructor() {
		Graph<String, String> g1 = new Graph<String, String>();
		Graph<String, String> g2 = new Graph<String, String>();
		Graph<String, String> g3 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g2.addNode(s);
			g3.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g3.addEdge(s, e.child(), e.label());
			}
		}
		
		new Graph<String, String>(g1);
		new Graph<String, String>(g2);
		new Graph<String, String>(g3);
		
		assertThrows(NullPointerException.class, () -> new Graph<String, String>(null));
	}
	
	@Test
	void testAddNode() {
		Graph<String, String> g1 = new Graph<String, String>();
		assertTrue(g1.addNode(":3"), "Adding new node returned false");
		assertTrue(g1.addNode(">:3"), "Adding new node returned false");
		assertTrue(g1.addNode(""), "Adding new node returned false");
		assertFalse(g1.addNode(":3"), "Adding duplicate node returned true");
		assertThrows(NullPointerException.class, () -> g1.addNode(null));
	}
	
	@Test
	void testRemoveNode() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g1.addEdge(s, e.child(), e.label());
			}
		}
		
		for (String s : validEdges1.keySet()) {
			assertTrue(g1.removeNode(s), "Removing node in graph returns false");
			assertFalse(g1.removeNode(s), "Removing removed node returns true");
		}
		assertFalse(g1.removeNode("Not in graph"), "Removing node not in graph returns false");
		assertThrows(NullPointerException.class, () -> g1.removeNode(null));
	}
	
	@Test
	void testContainsNode() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		
		for (String s : validEdges1.keySet()) {
			assertTrue(g1.containsNode(s), "Node in graph not found in graph");
		}
		assertFalse(g1.containsNode("Not in graph"), "Node not in graph found in graph");
		assertThrows(NullPointerException.class, () -> g1.containsNode(null));
	}
	
	@Test
	void testNodes() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		
		//check that g1.nodes() only has nodes from validEdges1
		Set<String> nodes = g1.nodes();
		for (String s : nodes) {
			assertTrue(g1.containsNode(s), "Node in nodes set not found in graph");
			assertTrue(validEdges1.containsKey(s), "Node in nodes set not found in valid edges map");
		}
		for (String s : validEdges1.keySet()) {
			assertTrue(nodes.contains(s), "Node in valid edges map not found in nodes set");
		}
		
		//check that g1.nodes() doesn't change after adding new node
		g1.addNode("New node");
		assertFalse(nodes.contains("New node"), "Nodes set changed after new node was added to graph");
		
		//check that graph doesn't change after adding new node to g1.nodes()
		nodes.add("Another new node");
		assertFalse(g1.containsNode("Another new node"), "Graph contains node added only to nodes set");
		
		//check that graph with duplicate nodes only has nodes().size() == 1
		Graph<String, String> g2 = new Graph<String, String>();
		for (String s : validEdges2.keySet()) {
			g2.addNode(s);
		}
		assertTrue(g2.nodes().size() == 1, "Graph with duplicate nodes only doesn't have nodes set size = 1");

		//check that graph with no nodes has nodes().size() == 0
		Graph<String, String> g3 = new Graph<String, String>();
		assertTrue(g3.nodes().size() == 0, "Graph with no nodes doesn't have nodes set size = 0");
	}
	
	@Test
	void testAddEdge() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				assertTrue(g1.addEdge(s, e.child(), e.label()), "Adding new edge returned false");
				assertFalse(g1.addEdge(s, e.child(), e.label()), "Adding duplicate edge returned true");
			}
		}
		assertFalse(g1.addEdge("Not in graph", "1", "label"), "Adding edge with parent not in graph returned true");
		assertFalse(g1.addEdge("1", "Not in graph", "label"), "Adding edge with child not in graph returned true");
		assertFalse(g1.addEdge("Not in graph", "Not in graph", "label"), "Adding edge with neither node in graph returned true");
		assertThrows(NullPointerException.class, () -> g1.addEdge(null, "", ""));
		assertThrows(NullPointerException.class, () -> g1.addEdge(null, null, ""));
		assertThrows(NullPointerException.class, () -> g1.addEdge(null, "", null));
		assertThrows(NullPointerException.class, () -> g1.addEdge(null, null, null));
		assertThrows(NullPointerException.class, () -> g1.addEdge("", null, ""));
		assertThrows(NullPointerException.class, () -> g1.addEdge("", null, null));
		assertThrows(NullPointerException.class, () -> g1.addEdge("", "", null));
	}
	
	@Test
	void testRemoveEdge() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g1.addEdge(s, e.child(), e.label());
			}
		}
		
		assertFalse(g1.removeEdge("Not in graph", "1", "label"), "Removing edge with parent not in graph returned true");
		assertFalse(g1.removeEdge("1", "Not in graph", "label"), "Removing edge with child not in graph returned true");
		assertFalse(g1.removeEdge("Not in graph", "Not in graph", "label"), "Removing edge with neither node in graph returned true");
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				assertTrue(g1.removeEdge(s, e.child(), e.label()), "Removing edge in graph returns false");
				assertFalse(g1.removeEdge(s, e.child(), e.label()), "Removing removed edge returns true");
			}
		}
		assertThrows(NullPointerException.class, () -> g1.removeEdge(null, "", ""));
		assertThrows(NullPointerException.class, () -> g1.removeEdge(null, null, ""));
		assertThrows(NullPointerException.class, () -> g1.removeEdge(null, "", null));
		assertThrows(NullPointerException.class, () -> g1.removeEdge(null, null, null));
		assertThrows(NullPointerException.class, () -> g1.removeEdge("", null, ""));
		assertThrows(NullPointerException.class, () -> g1.removeEdge("", null, null));
		assertThrows(NullPointerException.class, () -> g1.removeEdge("", "", null));
	}
	
	@Test
	void testContainsEdge() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g1.addEdge(s, e.child(), e.label());
			}
		}
		
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				assertTrue(g1.containsEdge(s, e.child(), e.label()), "Edge in graph not found in graph");
			}
		}
		assertFalse(g1.containsEdge("Not in graph", "1", "label"), "Edge with parent not in graph found in graph");
		assertFalse(g1.containsEdge("1", "Not in graph", "label"), "Edge with child not in graph found in graph");
		assertFalse(g1.containsEdge("Not in graph", "Not in graph", "label"), "Edge with neither node in graph found in graph");
		assertThrows(NullPointerException.class, () -> g1.containsEdge(null, "", ""));
		assertThrows(NullPointerException.class, () -> g1.containsEdge(null, null, ""));
		assertThrows(NullPointerException.class, () -> g1.containsEdge(null, "", null));
		assertThrows(NullPointerException.class, () -> g1.containsEdge(null, null, null));
		assertThrows(NullPointerException.class, () -> g1.containsEdge("", null, ""));
		assertThrows(NullPointerException.class, () -> g1.containsEdge("", null, null));
		assertThrows(NullPointerException.class, () -> g1.containsEdge("", "", null));
	}
	
	@Test
	void testEdges() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g1.addEdge(s, e.child(), e.label());
			}
		}
		
		//check that g1.edges() only has nodes and edges from validEdges1
		Map<String, Set<Edge<String, String>>> edges = g1.edges();
		for (String s : edges.keySet()) {
			assertTrue(g1.containsNode(s), "Graph doesn't contain node in edges map");
			assertTrue(validEdges1.containsKey(s), "Edges map contains node not in valid edges map");
			for (Edge<String, String> e : edges.get(s)) {
				assertTrue(g1.containsNode(e.child()), "Graph doesn't contain node in edges map");
				assertTrue(g1.containsEdge(s, e.child(), e.label()), "Graph doesn't contain edge in edges map");
				assertTrue(validEdges1.get(s).contains(e), "Edges map contains edge not in valid edges map");
			}
		}
		
		//check that adding new nodes and edges to graph doesn't change g1.edges()
		g1.addNode("New node 1");
		g1.addNode("New node 2");
		assertFalse(edges.containsKey("New node 1"), "Edges map changed after new node was added to graph");
		Edge<String, String> e1 = new Edge<String, String>("New node 2", "Label");
		g1.addEdge("New node 1", "New node 2", "Label");
		assertFalse(validEdges1.containsKey("New node 1") && 
				validEdges1.get("New node 1").contains(e1), "Edges map changed after new edge was added to graph");
		
		//check that adding new nodes and edges to g1.edges() doesn't change graph
		Edge<String, String> e2 = new Edge<String, String>("New node 4", "Label");
		Set<Edge<String, String>> set = new HashSet<Edge<String, String>>();
		set.add(e2);
		edges.put("New node 3", set);
		assertFalse(g1.containsNode("New node 3"), "Adding new node to edges map changed graph");
		assertFalse(g1.containsNode("New node 4"), "Adding new node to edges map changed graph");
		assertFalse(g1.containsEdge("New node 3", "New node 4", "Label"), "Adding new edge to edges map changed graph");
		
		//check that graph with duplicate nodes and edges only has 1 key and 0 values in edges()
		Graph<String, String> g2 = new Graph<String, String>();
		for (String s : validEdges2.keySet()) {
			g2.addNode(s);
		}
		assertTrue(g2.edges().keySet().size() == 1, "Edges map doesn't have 1 key only for graph with only duplicate nodes");
		for (String key : g2.edges().keySet()) {
			assertTrue(g2.edges().get(key).size() == 0, "Edges map doesn't have 0 values for graph with only duplicate nodes");			
		}
		
		//check that graph with duplicate nodes and edges only has 1 key and value in edges()
		for (String s : validEdges2.keySet()) {
			for (Edge<String, String> e : validEdges2.get(s)) {
				g2.addEdge(s, e.child(), e.label());
			}
		}
		assertTrue(g2.edges().keySet().size() == 1, "Edges map doesn't have 1 key only for graph with only duplicate nodes and edges");
		for (String key : g2.edges().keySet()) {
			assertTrue(g2.edges().get(key).size() == 1, "Edges map doesn't have 1 value only for graph with only duplicate nodes and edges");			
		}
		
		//check that graph with no nodes and edges has no keys and values in edges()
		Graph<String, String> g3 = new Graph<String, String>();
		assertTrue(g3.edges().keySet().size() == 0, "Edges map doesn't have 0 keys for empty graph");
	}
	
	@Test
	void testEdgesBetweenNodes() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g1.addEdge(s, e.child(), e.label());
			}
		}
		
		//check that edges() has same exact nodes and edges as validEdges1
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				Set<Edge<String, String>> edges = g1.edges(s, e.child());
				assertTrue(edges.size() == 1, "Edges set has more than one edge");
				assertTrue(edges.contains(e), "Edges set doesn't contained expected edge");
			}
		}
		
		//check that adding to edges() doesn't change graph and vice versa
		Set<Edge<String, String>> edges = g1.edges(constNode1, constNode2);
		Edge<String, String> edge = new Edge<String, String>("New node 1", "Label");
		edges.add(edge);
		assertFalse(g1.containsNode("New node 1"), "Adding node to edges set changed graph");
		assertFalse(g1.containsEdge(constNode1, "New node 2", "Label"), "Adding edge to edges set changed graph");
		
		g1.addNode("New node 2");
		g1.addEdge(constNode1, "New node 2", "Label");
		assertFalse(edges.contains(new Edge<String, String>("New node 2", "Label")), 
				"Edges set changed after graph added node");
		
		//check for duplicates graph
		Graph<String, String> g2 = new Graph<String, String>();
		for (String s : validEdges2.keySet()) {
			g2.addNode(s);
		}
		for (String s : validEdges2.keySet()) {
			assertTrue(g2.edges(s, s).size() == 0, "Edges set doesn't have 0 values for graph with no edges");
		}
		for (String s : validEdges2.keySet()) {
			for (Edge<String, String> e : validEdges2.get(s)) {
				g2.addEdge(s, e.child(), e.label());
			}
		}
		for (String s : validEdges2.keySet()) {
			for (Edge<String, String> e : validEdges2.get(s)) {
				assertTrue(g2.edges(s, s).size() == 1 && g2.edges(s, s).contains(e), 
						"Edges set doesn't have expected edge for graph with duplicate nodes and edges");
			}
		}
		
		//check for node not found
		assertTrue(g1.edges("Not in graph", constNode1).size() == 0, "Edges set has a key when given parent node not in graph");
		assertTrue(g1.edges(constNode1, "Not in graph").size() == 0, "Edges set has a value given child node not in graph");
		
		//check for null node
		assertThrows(NullPointerException.class, () -> g1.edges(null, constNode1));
		assertThrows(NullPointerException.class, () -> g1.edges(constNode1, null));
		assertThrows(NullPointerException.class, () -> g1.edges(null, null));
	}
	
	@Test
	void testOutgoingEdges() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g1.addEdge(s, e.child(), e.label());
			}
		}
		
		//check that outgoingEdges() has same exact nodes and edges as validEdges1
		for (String s : validEdges1.keySet()) {
			Set<Edge<String, String>> edges = g1.outgoingEdges(s);
			for (Edge<String, String> e : edges) {
				assertTrue(validEdges1.get(s).contains(e), "Outgoing edges set contains value not in valid edges map");
			}
			for (Edge<String, String> e : validEdges1.get(s)) {
				assertTrue(edges.contains(e), "Outgoing edges set doesn't contains value in valid edges map");
			}
		}
		
		//check that outgoingEdges map doesn't change when adding to graph and vice versa
		Set<Edge<String, String>> edges = g1.outgoingEdges(constNode1);
		Edge<String, String> edge = new Edge<String, String>("New node 1", "Label");
		edges.add(edge);
		assertFalse(g1.containsNode("New node 1"), "Adding new node to edges set changed graph");
		assertFalse(g1.containsEdge(constNode1, "New node 1", "Label"), "Adding new edge to edges set changed graph");
		
		g1.addNode("New node 2");
		g1.addEdge(constNode1, "New node 2", "Label");
		assertFalse(edges.contains(new Edge<String, String>("New node 2", "Label")),
				"Adding new edge to graph changed edges set");
		
		//check for duplicates graph
		Graph<String, String> g2 = new Graph<String, String>();
		for (String s : validEdges2.keySet()) {
			g2.addNode(s);
		}
		for (String s : validEdges2.keySet()) {
			assertTrue(g2.outgoingEdges(s).size() == 0, "Edges set doesn't have 0 values for graph with no edges");
		}
		for (String s : validEdges2.keySet()) {
			for (Edge<String, String> e : validEdges2.get(s)) {
				g2.addEdge(s, e.child(), e.label());
			}
		}
		for (String s : validEdges2.keySet()) {
			for (Edge<String, String> e : validEdges2.get(s)) {
				assertTrue(g2.outgoingEdges(s).size() == 1 && g2.outgoingEdges(s).contains(e), 
						"Edges set doesn't have expected edge for graph with duplicate nodes and edges");
			}
		}
		
		//check for node not found
		assertTrue(g1.outgoingEdges("Not in graph").size() == 0, "Edges set has a key when given parent node not in graph");
		
		//check for null node
		assertThrows(NullPointerException.class, () -> g1.outgoingEdges(null));
	}
	
	@Test
	void testIncomingEdges() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g1.addEdge(s, e.child(), e.label());
			}
		}
		
		//check that incomingEdges() has same exact nodes and edges as validEdges1
		for (String s : validEdges1.keySet()) {
			Map<String, Set<Edge<String, String>>> edges = g1.incomingEdges(s);
			assertTrue(edges.containsKey(s), "Incoming edges map doesn't contain expected key");
			assertTrue(edges.keySet().size() == validEdges1.keySet().size(), "Incoming edges map doesn't have same number of keys as valid edges map");
			for (String key : edges.keySet()) {
				assertTrue(validEdges1.containsKey(key), "Valid edges map doesn't contain key from incoming edges map");
				for (Edge<String, String> e : edges.get(key)) {
					assertTrue(validEdges1.get(key).contains(e), "Valid edges map doesn't contain value from incoming edges map");
				}
			}
		}
		
		//check that outgoingEdges map doesn't change when adding to graph and vice versa
		Map<String, Set<Edge<String, String>>> edges = g1.incomingEdges(constNode1);
		Edge<String, String> edge = new Edge<String, String>("New node 2", "Label");
		Set<Edge<String, String>> set = new HashSet<Edge<String, String>>();
		set.add(edge);
		edges.put("New node 1", set);
		assertFalse(g1.containsNode("New node 1"), "Adding new node to edges map changed graph");
		assertFalse(g1.containsNode("New node 2"), "Adding new node to edges map changed graph");
		assertFalse(g1.containsEdge("New node 1", "New node 2", "label"), "Adding new edge to edges map changed graph");
		
		g1.addNode("New node 3");
		g1.addEdge("New node 1", "New node 3", "Label");
		assertFalse(edges.get("New node 1").contains(new Edge<String, String>("New node 3", "Label")),
				"Adding new edge to graph changed edges map");
		
		//check for duplicates graph
		Graph<String, String> g2 = new Graph<String, String>();
		for (String s : validEdges2.keySet()) {
			g2.addNode(s);
		}
		for (String s : validEdges2.keySet()) {
			assertTrue(g2.incomingEdges(s).containsKey(s) && g2.incomingEdges(s).get(s).size() == 0, 
					"Edges map doesn't have 0 values for graph with no edges");
		}
		for (String s : validEdges2.keySet()) {
			for (Edge<String, String> e : validEdges2.get(s)) {
				g2.addEdge(s, e.child(), e.label());
			}
		}
		for (String s : validEdges2.keySet()) {
			for (Edge<String, String> e : validEdges2.get(s)) {
				assertTrue(g2.incomingEdges(s).containsKey(s) && g2.incomingEdges(s).get(s).size() == 1
						&& g2.incomingEdges(s).get(s).contains(e), 
						"Edges map doesn't have expected edge for graph with duplicate nodes and edges");
			}
		}
		
		//check for node not found
		assertTrue(g1.incomingEdges("Not in graph").keySet().size() == 0, "Edges map has a key when given child node not in graph");
		
		//check for null node
		assertThrows(NullPointerException.class, () -> g1.incomingEdges(null));
	}
	
	@Test
	void testOutDegree() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g1.addEdge(s, e.child(), e.label());
			}
		}
		
		//check for correct out-degree
		for (String s : validEdges1.keySet()) {
			assertTrue(g1.outDegree(s) == validEdges1.get(s).size(), "Node has wrong out-degree");
		}
		
		//check for node not found
		assertTrue(g1.outDegree("Not in graph") == -1, "Node not in graph has wrong out-degree");
		
		//check for null
		assertThrows(NullPointerException.class, () -> g1.outDegree(null));
	}
	
	@Test
	void testInDegree() {
		Graph<String, String> g1 = new Graph<String, String>();
		for (String s : validEdges1.keySet()) {
			g1.addNode(s);
		}
		for (String s : validEdges1.keySet()) {
			for (Edge<String, String> e : validEdges1.get(s)) {
				g1.addEdge(s, e.child(), e.label());
			}
		}
		
		//check for correct in-degree
		for (String s : validEdges1.keySet()) {
			assertTrue(g1.inDegree(s) == g1.incomingEdges(s).size(), "Node has wrong out-degree");
		}
		
		//check for node not found
		assertTrue(g1.inDegree("Not in graph") == -1, "Node not in graph has wrong in-degree");
		
		//check for null
		assertThrows(NullPointerException.class, () -> g1.inDegree(null));
	}
}
