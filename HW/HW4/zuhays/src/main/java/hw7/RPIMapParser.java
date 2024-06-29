package hw7;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.AbstractMap.SimpleEntry;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * RPIMapParser parses CSV files for nodes and edges specifically for RPIMap
 */
public class RPIMapParser {
	
	// RPIMapParser isn't an ADT
	
	/**
	 * Read node data from a file
	 * @param filename Path to a CSV file containing node data in the format:
	 *                 name,id,x-coord,y-coord
	 * @param nodes Map of nodes with key = id and value = entry where key = name and 
	 *              value = entry where key = x-coord and value = y-coord
	 * @requires none
	 * @modifies nodes
	 * @effects Adds all nodes from filename into nodes
	 *          Note: If multiple nodes have the same id, only the last node with that id
	 *                is in nodes
	 * @throws NullPointerException if filename == null || nodes == null
	 * @throws IOException if filename can't be read or is in improper format
	 * @returns none
	 */ //Map<Integer, Map<String, List<Integer>>>
	public static void readNodeData(String filename, Map<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>> nodes) 
			throws IOException {
		String error = "File " + filename + " not a CSV (name,id,x-coord,y-coord) file.";
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				//check format
				int ind = line.indexOf(",");
				checkInd(ind, line.length(), error);
				String name = line.substring(0, ind);
				
				int nextInd = line.indexOf(",", ind+1);
				checkInd(nextInd, line.length(), error);
				checkInteger(line.substring(ind+1, nextInd), "ID found that isn't an integer");
				int id = Integer.parseInt(line.substring(ind+1, nextInd));
				
				ind = nextInd;
				nextInd = line.indexOf(",", ind+1);
				checkInd(nextInd, line.length(), error);
				checkInteger(line.substring(ind+1, nextInd), "x-coord found that isn't an integer");
				int x = Integer.parseInt(line.substring(ind+1, nextInd));
				
				checkInd(nextInd+1, line.length(), error);
				checkInteger(line.substring(nextInd+1), "y-coord found that isn't an integer");
				int y = Integer.parseInt(line.substring(nextInd+1));
				
				//add node to map
				SimpleEntry<Integer, Integer> coords = new SimpleEntry<Integer, Integer>(x,y);
				SimpleEntry<String, SimpleEntry<Integer, Integer>> nameEntry = 
						new SimpleEntry<String, SimpleEntry<Integer, Integer>>(name, coords);
				nodes.put(id, nameEntry);
				//didn't make a set of nodes to reduce coupling
				//make map key node ids so that RPIMap can quickly find data on a node based on id
			}
		}
	}
	
	/**
	 * Read node data from a file
	 * @param filename Path to a CSV file containing edge data in the format:
	 *                 id1,id2
	 *                 where id1 and id2 are the ids of a building/intersection
	 * @param edges Map of ids where a key is an id of a node and a value is a set of node ids
	 * @requires none
	 * @modifies edges
	 * @effects For each edge <id1,id2>, id2 is added to edges.get(id1) and id1 is added to 
	 *          edges.get(id2)
	 * @throws NullPointerException if filename == null || edges == null
	 * @throws IOException if filename can't be read or is in improper format
	 * @returns none
	 */
	public static void readEdgeData(String filename, 
			Map<Integer, Set<Integer>> edges) throws IOException {
		String error = "File " + filename + " not a CSV (id1,id2) file.";
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				//check format
				int ind = line.indexOf(",");
				checkInd(ind, line.length(), error);
				checkInteger(line.substring(0, ind), "id1 found that isn't an integer");
				int id1 = Integer.parseInt(line.substring(0, ind));
				
				checkInd(ind+1, line.length(), error);
				checkInteger(line.substring(ind+1), "id2 found that isn't an integer");
				int id2 = Integer.parseInt(line.substring(ind+1));
				
				//add edge to set
				if (!edges.containsKey(id1)) {
					edges.put(id1, new HashSet<Integer>());
				}
				if (!edges.containsKey(id2)) {
					edges.put(id2, new HashSet<Integer>());
				}
				edges.get(id1).add(id2);
				edges.get(id2).add(id1);
			}
		}
	}
	
	//throw ioexception if ind from indexOf = -1 or >= maxLength
	private static void checkInd(int ind, int maxLength, String error) throws IOException {
		if (ind == -1 || ind >= maxLength) {
			throw new IOException(error);
		}
	}
	//throw ioexception if str isnt an integer
	private static void checkInteger(String str, String error) throws IOException {
		try {
			Integer.parseInt(str);
		} catch(NumberFormatException e) {
			throw new IOException(error);
		}
	}
}
