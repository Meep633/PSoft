package hw4;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;

class GraphWrapperTest {
	GraphWrapper graph;
	
	@BeforeEach
	void setup() {
		graph = new GraphWrapper();
	}
	
	void addNodes() {
		graph.addNode("a");
		graph.addNode("b");
		graph.addNode("yippee");
		graph.addNode("d");
		graph.addNode("c");
	}
	void addEdges() {
		graph.addEdge("a", "b", "1");
		graph.addEdge("a", "b", "2");
		graph.addEdge("a", "a", "1");
		graph.addEdge("a", "c", "3");
		graph.addEdge("a", "yippee", "hi");
	}
	
	@Test
	void testInitialNodes() {
		int length = 0;
		Iterator<String> nodes = graph.listNodes();
		while (nodes.hasNext()) {
			length++;
			nodes.next();
		}
		assertTrue(length == 0, "Empty graph has nodes");
	}
	
	@Test
	void testAddNode() {
		addNodes();
	}
	
	@Test
	void testAddEdge() {
		addNodes();
		addEdges();
	}
	
	@Test
	void testListNodes() {
		addNodes();
		
		Iterator<String> nodes = graph.listNodes();
		assertTrue(nodes.hasNext(), "Graph has incorrect number of nodes");
		
		assertTrue(nodes.next().equals("a"), "Incorrect node");
		assertTrue(nodes.hasNext(), "Graph has incorrect number of nodes");
		
		assertTrue(nodes.next().equals("b"), "Incorrect node");
		assertTrue(nodes.hasNext(), "Graph has incorrect number of nodes");
		
		assertTrue(nodes.next().equals("c"), "Incorrect node");
		assertTrue(nodes.hasNext(), "Graph has incorrect number of nodes");
		
		assertTrue(nodes.next().equals("d"), "Incorrect node");
		assertTrue(nodes.hasNext(), "Graph has incorrect number of nodes");
		
		assertTrue(nodes.next().equals("yippee"), "Incorrect node");
		assertFalse(nodes.hasNext(), "Graph has incorrect number of nodes");
	}
	
	@Test
	void testListChildren() {
		addNodes();
		addEdges();
		
		Iterator<String> children = graph.listChildren("a");
		assertTrue(children.hasNext(), "Graph has incorrect number of edges");
		
		assertTrue(children.next().equals("a(1)"), "Incorrect edge");
		assertTrue(children.hasNext(), "Graph has incorrect number of edges");
		
		assertTrue(children.next().equals("b(1)"), "Incorrect edge");
		assertTrue(children.hasNext(), "Graph has incorrect number of edges");
		
		assertTrue(children.next().equals("b(2)"), "Incorrect edge");
		assertTrue(children.hasNext(), "Graph has incorrect number of edges");
		
		assertTrue(children.next().equals("c(3)"), "Incorrect edge");
		assertTrue(children.hasNext(), "Graph has incorrect number of edges");
		
		assertTrue(children.next().equals("yippee(hi)"), "Incorrect edge");
		assertFalse(children.hasNext(), "Graph has incorrect number of edges");
	}
}