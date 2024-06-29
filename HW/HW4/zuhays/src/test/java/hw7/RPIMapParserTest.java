package hw7;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;
import java.io.IOException;

class RPIMapParserTest {
	Map<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>> nodes;
	Map<Integer, Set<Integer>> edges;
	
	@BeforeEach
	void setup() {
		nodes = new HashMap<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>>();
		edges = new HashMap<Integer, Set<Integer>>();
	}
	
	@Test
	void testReadNodeData() {
		Map<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>> expectedValues = 
				new HashMap<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>>();
		
		SimpleEntry<String, SimpleEntry<Integer, Integer>> nameEntry;
		SimpleEntry<Integer, Integer> coords;
		
		coords = new SimpleEntry<Integer, Integer>(1,2);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("name?", coords);
		expectedValues.put(0, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(2,2);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("name", coords);
		expectedValues.put(1, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(4,2);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("another name", coords);
		expectedValues.put(2, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(10,15);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("yet another name", coords);
		expectedValues.put(3, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(3,6);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>(":3", coords);
		expectedValues.put(4, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(100,100);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("kenny wu is a pretty cool guy", coords);
		expectedValues.put(5, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(40,60);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("thanks psoft mentors", coords);
		expectedValues.put(6, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(40,40);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("", coords);
		expectedValues.put(7, nameEntry);
		
		coords = new SimpleEntry<Integer, Integer>(4,4);
		nameEntry = new SimpleEntry<String, SimpleEntry<Integer, Integer>>("", coords);
		expectedValues.put(8, nameEntry);
		
		try {
			RPIMapParser.readNodeData("data/rpi_map_nodes_test.csv", nodes);
			assertTrue(nodes.size() == expectedValues.size(), "Unexpected number of nodes");
			expectedValues.forEach((id, data) -> {
				assertTrue(nodes.containsKey(id), "Nodes doesn't contain expected id");
				assertEquals(nodes.get(id).getKey(), expectedValues.get(id).getKey(),
						"Node doesn't contain expected name");
				assertEquals(nodes.get(id).getValue().getKey(), expectedValues.get(id).getValue().getKey(),
						"Node doesn't contain expected x-coord");
				assertEquals(nodes.get(id).getValue().getValue(), expectedValues.get(id).getValue().getValue(),
						"Node doesn't contain expected y-coord");
			});
			
			assertThrows(NullPointerException.class, () -> RPIMapParser.readNodeData(null, nodes));
			assertThrows(NullPointerException.class, () -> RPIMapParser.readNodeData("data/rpi_map_nodes_test.csv", null));
			assertThrows(NullPointerException.class, () -> RPIMapParser.readNodeData(null, null));
			assertThrows(IOException.class, () -> RPIMapParser.readNodeData("does not exist", nodes));
			assertThrows(IOException.class, () -> RPIMapParser.readNodeData("data/bad_format.csv", nodes));
			assertThrows(IOException.class, () -> RPIMapParser.readNodeData("data/rpi_map_nodes_bad_format.csv", nodes));
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
	}

	@Test
	void testReadEdgeData() {
		Map<Integer, Set<Integer>> expectedValues = new HashMap<Integer, Set<Integer>>();
		for (int i = 0; i < 9; i++) {
			expectedValues.put(i, new HashSet<Integer>());			
		}
		expectedValues.get(0).add(1);
		expectedValues.get(0).add(2);
		expectedValues.get(0).add(4);
		expectedValues.get(1).add(0);
		expectedValues.get(1).add(2);
		expectedValues.get(1).add(3);
		expectedValues.get(2).add(0);
		expectedValues.get(2).add(1);
		expectedValues.get(2).add(3);
		expectedValues.get(2).add(8);
		expectedValues.get(3).add(1);
		expectedValues.get(3).add(2);
		expectedValues.get(3).add(5);
		expectedValues.get(4).add(0);
		expectedValues.get(5).add(3);
		expectedValues.get(6).add(7);
		expectedValues.get(7).add(6);
		expectedValues.get(8).add(2);
		
		try {
			RPIMapParser.readEdgeData("data/rpi_map_edges_test.csv", edges);
			assertTrue(edges.keySet().size() == 9, "Unexpected number of keys in edges");
			expectedValues.forEach((key, value) -> {
				assertTrue(edges.containsKey(key), "Keys doesn't contain expected int");
				assertTrue(edges.get(key).size() == expectedValues.get(key).size(), 
						"Set doesn't have expected number of ints");
				for (int i : value) {
					assertTrue(edges.get(key).contains(i), "Set doesn't contain expected int");
					assertTrue(edges.containsKey(i), "Int found that isn't in keys");
				}
			});
			
			assertThrows(NullPointerException.class, () -> RPIMapParser.readEdgeData(null, edges));
			assertThrows(NullPointerException.class, () -> RPIMapParser.readEdgeData("data/rpi_map_edges_test.csv", null));
			assertThrows(NullPointerException.class, () -> RPIMapParser.readEdgeData(null, null));
			assertThrows(IOException.class, () -> RPIMapParser.readEdgeData("does not exist", edges));
			assertThrows(IOException.class, () -> RPIMapParser.readEdgeData("data/bad_format.csv", edges));
			assertThrows(IOException.class, () -> RPIMapParser.readEdgeData("data/rpi_map_edges_bad_format.csv", edges));
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
	}

	@Test
	void testReadRealNodeData() {
		try {
			RPIMapParser.readNodeData("data/RPI_map_data_Nodes.csv", nodes);
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
	}
	
	@Test
	void testReadRealEdgeData() {
		try {
			RPIMapParser.readEdgeData("data/RPI_map_data_Edges.csv", edges);
		} catch (Exception e) {
			fail("Exception thrown:\n" + e.getMessage());
		}
	}
}
