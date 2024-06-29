package hw6;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import hw4.Edge;
import hw4.Graph;
import hw4.PathAlgorithms;
import hw5.CSVParser;

/**
 * LegoPaths is a graph representing the relationship between lego parts and sets. Each node
 * is a lego part and any 2 nodes can have at most 1 edge between them. If 2 nodes don't have
 * a set in common, they don't have any edges between them. If 2 nodes are the same node, they
 * have an edge with a cost of 0.0. If 2 nodes are different and share at least one set in 
 * common, then they have an edge with a cost of 1 / (# of common sets).
 */
public class LegoPaths {
	private Graph<String, Double> graph;
	
	//LegoPaths is not an ADT
	
	/**
	 * Initialize graph to empty graph
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @returns Empty LegoPaths graph
	 */
	public LegoPaths() {
		graph = new Graph<String, Double>();
	}
	
	/**
	 * Resets graph and fills out with nodes (parts) and edges (1/(# of common sets)) according
	 * to the CSV file
	 * @param filename Path to CSV file containing data about professors and courses
	 * @requires none
	 * @modifies this
	 * @effects Resets graph and fills it out with nodes and edges according to the given file.
	 *          If filename isn't a CSV file that follows the format "part","set", the error stack 
	 *          trace of an IOException is printed and the method exits early.
	 * @returns none
	 * @throws NullPointerException if filename == null
	 */
	public void createNewGraph(String filename) {
		if (filename == null) {
			throw new NullPointerException("Given filename is null");
		}
		
		Map<String, Set<String>> partsPerSet = new HashMap<String, Set<String>>();
		Set<String> parts = new HashSet<String>();
		try {			
			CSVParser.readData(filename, partsPerSet, parts);
		} catch (Exception e) {
			System.out.println("Error parsing given file");
			e.printStackTrace();
			return;
		}
		
		graph = new Graph<String, Double>();
		
		//keep track of common set counts and add edges afterwards
		Map<String, Map<String, Integer>> numParts = new HashMap<String, Map<String, Integer>>();
		for (String set : partsPerSet.keySet()) {
			for (String part1 : partsPerSet.get(set)) {
				Map<String, Integer> countMap = numParts.get(part1);
				if (countMap == null) { //new part
					countMap = new HashMap<String, Integer>();
					numParts.put(part1, countMap);
					graph.addNode(part1);
				}
				for (String part2 : partsPerSet.get(set)) {
					if (!countMap.containsKey(part2)) { //new relationship between parts
						countMap.put(part2, 0);
					}
					countMap.put(part2, countMap.get(part2)+1); //increment count
				}
			}
		}
		
		for (String part1 : numParts.keySet()) {
			for (String part2 : numParts.get(part1).keySet()) {
				if (part1.equals(part2)) {
					graph.addEdge(part1, part2, 0.0);
				} else if (numParts.get(part1).get(part2) > 0) {
					graph.addEdge(part1, part2, 1.0/numParts.get(part1).get(part2));
				}
			}
		}
	}
	
	/**
	 * Find the shortest path from one path to another using Dijkstra's algorithm
	 * @param PART1 Part where path should begin
	 * @param PART2 Part where path should end
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @returns If PART1 isn't a node in the LegoPaths graph, the following is returned:
	 *          "unknown part PART1\n". The same is returned for PART2. If both parts aren't
	 *          in the graph and PART1 != PART2, both messages are returned in one string:
	 *          "unknown part PART1\nunknown part PART2\n". If both parts aren't in the graph
	 *          and PART1 == PART2, the following is returned: "unknown part PART1\n".
	 *          
	 *          If a path exists from PART1 to PART2, a path in the following format is returned:
	 *          "path from PART1 to PART2:\nPART1 to part with weight edge_weight\n
	 *          part to part with weight edge_weight\n...\npart to PART2 with weight edge_weight\n
	 *          total cost: total_weight\n"
	 *          edge_weight is the weight of each edge in the graph and total_weight is the total
	 *          weight of the edges on the path. Outputted edge weights are rounded to the nearest
	 *          thousandth.
	 *          
	 *          If PART1 == PART2, the following is returned:
	 *          "path from PART1 to PART2:\ntotal cost: 0.000\n"
	 *          
	 *          If no path from PART1 to PART2 exists, the following is returned:
	 *          "path form PART1 to PART2:\nno path found\n"
	 * @throws NullPointerException if PART1 == null || PART2 == null
	 */
	public String findPath(String PART1, String PART2) {
		if (PART1 == null || PART2 == null) {
			throw new NullPointerException("Given part(s) is null");
		}
		
		String msg = "";
		if (!graph.containsNode(PART1)) {
			msg += "unknown part " + PART1 + "\n";
		}
		if (!graph.containsNode(PART2) && !PART1.equals(PART2)) {
			msg += "unknown part " + PART2 + "\n";
		}
		if (msg.length() > 0) {
			return msg;
		}
		
		List<Edge<String, Double>> path = PathAlgorithms.dijkstra(graph, PART1, PART2, (Edge<String, Double> e) -> e.label());
		if (path == null) {
			return "path from " + PART1 + " to " + PART2 + ":\nno path found\n";
		}
		
		String parent = PART1;
		double minCost = 0.0;
		msg = "path from " + PART1 + " to " + PART2 + ":\n";
		for (Edge<String, Double> edge : path) {
			msg += parent + " to " + edge.child() + " with weight ";
			msg += String.format("%.3f\n", edge.label());
			parent = edge.child();
			minCost += edge.label();
		}
		msg += String.format("total cost: %.3f\n", minCost);
		return msg;
	}
	
	/**
	 * Get a copy of the graph underlying LegoPaths
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @returns A copy of the underlying graph of LegoPaths
	 */
	public Graph<String, Double> underlyingGraph() {
		return new Graph<String, Double>(graph);
	}
	
//	public static void main(String args[]) {
//		LegoPaths lp = new LegoPaths();
//		lp.createNewGraph("data/empty_lego.csv");
//		String part1 = "31367 Green Duplo Egg Base";
//		String part2 = "10000 Violet Duplo Animal Brick 2 x 2 Elephant Head";
//		System.out.println(lp.findPath(part1, part2));
//	}
}