package hw5;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessorPathsTest {
	ProfessorPaths rpiProfs;
	String constNode = "003381 [No Color/Any Color] Sticker Sheet for Set 663-1";
	
	@BeforeEach
	void setup() {
		rpiProfs = new ProfessorPaths();
	}
	
	@Test
	void testCSVParser() {
		assertThrows(IOException.class, () -> CSVParser.readData("does not exist", new HashMap<String, Set<String>>(), new HashSet<String>()));
		assertThrows(IOException.class, () -> CSVParser.readData("data/bad_format.csv", new HashMap<String, Set<String>>(), new HashSet<String>()));
	}
	
	@Test
	void testNoArgConstructor() {
		new ProfessorPaths();
	}
	
	@Test
	void testCreateNewGraph() {
		rpiProfs.createNewGraph("data/courses.csv");
		rpiProfs.createNewGraph("data/small_valid.csv");			
		rpiProfs.createNewGraph("data/duplicates.csv");		
		assertThrows(NullPointerException.class, () -> rpiProfs.createNewGraph(null));
	}
	
	@Test
	void testGetProfs() {
		String[] filenames = {"data/courses.csv", "data/small_valid.csv", "data/duplicates.csv"};
		for (String filename : filenames) {
			rpiProfs.createNewGraph(filename);
			
			Set<String> profs = new HashSet<String>();
			Map<String, Set<String>> profsTeaching = new HashMap<String, Set<String>>();
			try {
				CSVParser.readData(filename, profsTeaching, profs);			
			} catch (Exception e) {
				fail("ProfessorParser error when reading data from " + filename);
			}
			
			List<String> ppProfs = rpiProfs.getProfs();
			for (String prof : ppProfs) {
				assertTrue(profs.contains(prof), "ProfessorPaths contains professor not in original set from " + filename);
			}
			profs.iterator().forEachRemaining((prof) -> {
				assertTrue(ppProfs.contains(prof), "ProfessorPaths doesn't contain expected professor from " + filename);
			});
		}
	}
	
	@Test
	void testGetCourses() {
		String[] filenames = {"data/courses.csv", "data/small_valid.csv", "data/duplicates.csv"};
		for (String filename : filenames) {
			rpiProfs.createNewGraph(filename);						
			Set<String> profs = new HashSet<String>();
			Map<String, Set<String>> profsTeaching = new HashMap<String, Set<String>>();
			try {
				CSVParser.readData(filename, profsTeaching, profs);			
			} catch (Exception e) {
				fail("ProfessorParser error when reading data from " + filename);
			}
			Set<String> courses = profsTeaching.keySet();
			
			List<String> ppCourses = rpiProfs.getCourses();
			for (String course : ppCourses) {
				assertTrue(courses.contains(course), "ProfessorPaths contains course not in original set from " + filename);
			}
			courses.iterator().forEachRemaining((course) -> {
				assertTrue(ppCourses.contains(course), "ProfessorPaths doesn't contain expected course from " + filename);
			});
		}
	}
	
	/* Cases to test:
	 * - node1 == node2
	 * - node1 and/or node2 not found
	 * - node1 and/or node2 is null
	 * - node1 has one edge to node2
	 * - node1 has multiple edges to node2
	 * - node1 has a short path to node2
	 * - node1 has a long path to node2
	 */
	
	@Test
	void testFindPath() {
		rpiProfs.createNewGraph("data/small_valid.csv");
		
		//no path between node1 and node2
		assertEquals(rpiProfs.findPath("A", "D"), "path from A to D:\nno path found\n", 
				"Incorrect output when no path between node1 and node2 exists");
		
		//edge between node1 and node2
		assertEquals(rpiProfs.findPath("A", "B"), "path from A to B:\nA to B via a-course\n",
				"Incorrect output when node1 and node2 are directly connected by one edge");
		
		//shortest path has < 10 edges between node1 and node2
		assertEquals(rpiProfs.findPath("A", "F"), "path from A to F:\nA to B via a-course\nB to C via c-course\nC to E via d-course\nE to F via f-course\n",
				"Incorrect output for typical shorter path between node1 and node2");
		
		//shortest path has >= 10 edges between node1 and node2
		assertEquals(rpiProfs.findPath("A", "Z"), "path from A to Z:\nA to B via a-course\nB to C via c-course\nC to E via d-course\nE to F via f-course\nF to G via g-course\nG to H via h-course\nH to I via blehhh\nI to J via :3\nJ to L via hi\nL to Z via waaa\n", 
				"Incorrect output for typical longer path between node1 and node2");
	}
	
	@Test
	void testFindPathEdgeCases() {
		rpiProfs.createNewGraph("data/courses.csv");
		
		//node1 == node2 and they're in the graph
		assertTrue(rpiProfs.findPath("A Bruce Carlson", "A Bruce Carlson").equals("path from A Bruce Carlson to A Bruce Carlson:\n"),
				"Incorrect output when node1 == node2");
		
		//node1 and/or node2 not in graph
		assertTrue(rpiProfs.findPath(":3", "A Bruce Carlson").equals("unknown professor :3\n"),
				"Incorrect output when node1 isn't in graph");
		assertTrue(rpiProfs.findPath("A Bruce Carlson", ":3").equals("unknown professor :3\n"),
				"Incorrect output when node2 isn't in graph");
		assertTrue(rpiProfs.findPath(">:3", ":3").equals("unknown professor >:3\nunknown professor :3\n"),
				"Incorrect output when node1 and node2 isn't in graph and node1 != node2");
		assertTrue(rpiProfs.findPath(":3", ":3").equals("unknown professor :3\n"),
				"Incorrect output when node1 and node2 isn't in graph and node1 == node2");
		
		//node1 and/or node2 is null
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath(null, "A Bruce Carlson"));
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath("A Bruce Carlson", null));
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath(null, ":3"));
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath(":3", null));
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath(null, null));
	}
	
	@Test
	void testBigData() {
		// i have no idea how to actually find the correct path in this dataset so im just 
		// assuming findPath works on this and seeing how many seconds it takes for the test to run
		rpiProfs.createNewGraph("data/lego2000.csv");
		rpiProfs.findPath(constNode, constNode);
	}
	
	@Test
	void testBigDataEdgeCases() {
		rpiProfs.createNewGraph("data/lego2000.csv");
		
		//node1 == node2 and they're in the graph
		assertTrue(rpiProfs.findPath(constNode, constNode)
				.equals("path from " + constNode + " to " + constNode + ":\n"),
				"Incorrect output when node1 == node2");
		
		//node1 and/or node2 not in graph
		assertTrue(rpiProfs.findPath(":3", constNode).equals("unknown professor :3\n"),
				"Incorrect output when node1 isn't in graph");
		assertTrue(rpiProfs.findPath(constNode, ":3").equals("unknown professor :3\n"),
				"Incorrect output when node2 isn't in graph");
		assertTrue(rpiProfs.findPath(">:3", ":3").equals("unknown professor >:3\nunknown professor :3\n"),
				"Incorrect output when node1 and node2 isn't in graph and node1 != node2");
		assertTrue(rpiProfs.findPath(":3", ":3").equals("unknown professor :3\n"),
				"Incorrect output when node1 and node2 isn't in graph and node1 == node2");
		
		//node1 and/or node2 is null
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath(null, constNode));
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath(constNode, null));
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath(null, ":3"));
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath(":3", null));
		assertThrows(NullPointerException.class, () -> rpiProfs.findPath(null, null));
	}
}