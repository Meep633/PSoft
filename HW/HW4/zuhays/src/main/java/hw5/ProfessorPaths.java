package hw5;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;

import hw4.Edge;
import hw4.Graph;
import hw4.PathAlgorithms;

/**
 * ProfessorPaths is a graph representing the relationship between professors by common courses
 * that they've taught. Nodes are professors and edges between professors are classes that they've
 * both taught.
 */
public class ProfessorPaths {
	private Graph<String, String> graph;
	
	//ProfessorPaths is not an ADT
	
	/**
	 * Initialize graph to an empty graph
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @returns Empty ProfessorPaths graph
	 */
	public ProfessorPaths() {
		graph = new Graph<String, String>();
	}
	
	/**
	 * Resets graph and fills out with professors (nodes) and courses (edges) according
	 * to the CSV file
	 * @param filename Path to CSV file containing data about professors and courses
	 * @requires none
	 * @modifies this
	 * @effects Resets graph and fills it out with professors and courses according to the given file.
	 *          If filename isn't a CSV file that follows the format "prof","course", the error stack 
	 *          trace of an IOException is printed and the method exits early.
	 * @returns none
	 * @throws NullPointerException if filename == null
	 */
	public void createNewGraph(String filename) {
		if (filename == null) {
			throw new NullPointerException("Given filename is null");
		}
		
		Map<String, Set<String>> profsTeaching = new HashMap<String, Set<String>>();
		Set<String> profs = new HashSet<String>();
		try {			
			CSVParser.readData(filename, profsTeaching, profs);
		} catch (Exception e) {
			System.out.println("Error parsing given file");
			e.printStackTrace();
			return;
		}
		
		graph = new Graph<String, String>();
		
		profsTeaching.forEach((course, professors) -> {
			professors.forEach((prof1) -> {
				graph.addNode(prof1);
			});
			
			//all nodes must be added before making edges between them
			professors.forEach((prof1) -> {
				professors.forEach((prof2) -> {
					graph.addEdge(prof1, prof2, course);
				});
			});
		});
	}
	
	/**
	 * Get a list of all the professors in the ProfessorPaths graph
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @returns List of professors (nodes) in ProfessorPaths graph
	 */
	public List<String> getProfs() {
		return new ArrayList<String>(graph.nodes());
	}
	
	/**
	 * Get a list of all the courses in the ProfessorPaths graph
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @returns List of courses (edges with distinct labels) in ProfessorPaths graph
	 */
	public List<String> getCourses() {
		Map<String, Set<Edge<String, String>>> edges = graph.edges();
		Set<String> output = new HashSet<String>(); //don't want to give a bunch of duplicate courses
		for (String prof : edges.keySet()) {
			for (Edge<String, String> course : edges.get(prof)) {
				output.add(course.label());
			}
		}
		return new ArrayList<String>(output);
	}
	
	// comparator to help with lexicographical ordering in BFS
	private class SortEdges implements Comparator<Edge<String, String>> {
		public int compare(Edge<String, String> a, Edge<String, String> b) {
			if (a.child().equals(b.child())) {
				return a.label().compareTo(b.label());
			}
			return a.child().compareTo(b.child());
		}
	}
	
	/**
	 * Find a path from one professor to another using BFS
	 * @param node1 Name of professor to start search from
	 * @param node2 Name of professor to find a path to
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @returns If node1 isn't a professor in the ProfessorPaths graph, the following
	 *          is returned: "unknown professor node1\n". The same is returned for node2. If both
	 *          professors aren't in the graph and node1 != node2, both messages are returned 
	 *          in one string: "unknown professor node1\nunknown professor node2\n". If both professors
	 *          aren't in the graph and node1 == node2, the following is returned: "unknown professor node1\n".
	 *          
	 *          If a path exists from node1 to node2, a path in the following format is returned:
	 *          "path from node1 to node2:\nnode1 to prof via course\nprof to prof via course\n
	 *          ...\nprof to node2 via course\n".
	 *          The path prioritizes alphabetically least professors and courses (if multiple
	 *          shortest paths exist, the path with the alphabetically least next professor is taken) 
	 *          (if multiple edges exist between professors, the edge with the alphabetically 
	 *          least label is taken).
	 *          
	 *          If node1 == node2, the following is returned: "path from node1 to node2:\n".
	 *          
	 *          If no path exists from node1 to node2, the following is returned:
	 *          "path from node1 to node2:\nno path found".
	 * @throws NullPointerException if node1 == null || node2 == null
	 */
	public String findPath(String node1, String node2) {
		if (node1 == null || node2 == null) {
			throw new NullPointerException("Given node(s) is null");
		}
		
		String msg = "";
		if (!graph.containsNode(node1)) {
			msg += "unknown professor " + node1 + "\n";
		}
		if (!graph.containsNode(node2) && !node1.equals(node2)) {
			msg += "unknown professor " + node2 + "\n";
		}
		if (msg.length() > 0) {
			return msg;
		}
		
		List<Edge<String, String>> path = PathAlgorithms.bfs(graph, node1, node2, new SortEdges());
		if (path == null) {
			return "path from " + node1 + " to " + node2 + ":\nno path found\n";
		}
		
		String parent = node1;
		msg = "path from " + node1 + " to " + node2 + ":\n";
		for (Edge<String, String> edge : path) {
			String child = edge.child();
			String course = edge.label();
			msg += parent + " to " + child + " via " + course + "\n";
			parent = child;
		}
		return msg;
	}
	
//	public static void main(String[] args) {
//		new ProfessorPaths("data/lego2000.csv");
//	}
}