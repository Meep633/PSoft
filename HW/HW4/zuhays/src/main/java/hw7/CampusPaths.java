package hw7;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.Scanner;
import java.io.IOException;

/**
 * CampusPaths is a combined view and controller for RPIMap. It takes in user input and
 * prints building information and the shortest path between two buildings.
 */
public class CampusPaths {
	private RPIMap map;
	
	// CampusPaths isn't an ADT
	
	/**
	 * Initialize RPIMap model to a map with the given nodes and edges
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws IOException if: 
	 *         - "data/RPI_map_data_Nodes.csv" or "data/RPI_map_data_Edges.csv"
	 *         doesn't exist 
	 *         - "data/RPI_map_data_Nodes.csv" doesn't follow the format:
	 *         name,id,x-coord,y-coord
	 *         - "data/RPI_map_data_Edges.csv" doesn't follow the format:
	 *         id1,id2
	 *         where id1 and id2 are the IDs of a building/intersection
	 * @throws IllegalArgumentException if "data/RPI_map_data_Edges.csv" contains an edge
	 *         <id1,id2> such that id1 or id2 isn't an ID in "data/RPI_map_data_Nodes.csv"
	 *         or if an edge exists between two buildings/intersections with the same coords
	 * @returns CampusPaths with RPIMap model with given nodes and edges
	 */
	public CampusPaths() throws IOException {
		map = new RPIMap();
		map.createNewGraph("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");
	}
	
	/**
	 * Load RPIMap model with buildings and paths specified by given files
	 * @param buildingFileName     Path to a CSV file containing building/intersection 
	 *                             data in the format:
	 *                             name,id,x-coord,y-coord
	 * @param buildingPathFileName Path to a CSV file containing data about paths between 
	 *                             buildings/intersections in the format:
	 *                             id1,id2
	 * @requires none
	 * @modifies this.map
	 * @effects Loads map with buildings and paths from given files
	 *          If an error occurs while a file is parsed, "An error occurred: " followed with
	 *          the error is printed. Else, "Successfully loaded." is printed.
	 * @throws none
	 * @returns none
	 */
	public void loadMap(String buildingFileName, String buildingPathFileName) {
		boolean x = false;
		if (buildingFileName == null) {
			System.out.println("Given building file name is null.");
			x = true;
		}
		if (buildingPathFileName == null) {
			System.out.println("Given building path file name is null.");
			x = true;
		}
		if (x) {
			return;
		}
		
		try {
			map.createNewGraph(buildingFileName, buildingPathFileName);
			System.out.println("Successfully loaded.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}
	
	/**
	 * Print building information in format: name,id
	 * @requires none
	 * @modifies none
	 * @effects Prints out building information in alphabetical order by name in format: name,id\n
	 * @throws none
	 * @returns none
	 */
	public void printBuildings() {
		Map<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>> buildingsMap = map.getBuildings();
		List<SimpleEntry<String, Integer>> buildingsList = new ArrayList<SimpleEntry<String, Integer>>();
		for (Integer id : buildingsMap.keySet()) {
			buildingsList.add(new SimpleEntry<String, Integer>(buildingsMap.get(id).getKey(), id));
		}
		Collections.sort(buildingsList, new BuildingComparator());
		for (SimpleEntry<String, Integer> building : buildingsList) {
			System.out.println(building.getKey() + "," + building.getValue());
		}
	}
	
	//sort entries list by string key
	private class BuildingComparator implements Comparator<SimpleEntry<String, Integer>> {
		public int compare(SimpleEntry<String, Integer> a, SimpleEntry<String, Integer> b) {
			return a.getKey().compareTo(b.getKey());
		}
	}
	
	/**
	 * Print shortest path between two buildings
	 * @param building1 ID/name of starting building
	 * @param building2 ID/name of ending building
	 * @requires none
	 * @modifies none
	 * @effects If building1 and/or building2 can be parsed as an integer, it is assumed to be
	 *          a building ID.
	 *          
	 *          If building1 can't be parsed into an integer and isn't a valid building name, 
	 *          then the following is printed:
	 *          Unknown building: [building1]\n
	 *          The same is printed for building2 if building2 can't be parsed into an integer, 
	 *          isn't a valid building name, and !building1.equals(building2).
	 *          
	 *          If building1 can be parsed into an integer and isn't a valid building ID,
	 *          then the following is printed:
	 *          Unknown building: [id1]\n
	 *          where id1 is the given ID. The same is printed for building2 if building2 can be
	 *          parsed into an integer, isn't a valid building ID, and !building1.equals(building2).
	 *          
	 *          If no path exists from building1 to building2, then the following is printed:
	 *          There is no path from name1 to name2.\n
	 *          where name1 and name2 are the names of building1 and building2.
	 *          
	 *          If a path exists from building1 to building2, then the following is printed:
	 *          Path from name1 to name2:\n
	 *          \tWalk direction to (building)\n
	 *          \tWalk direction to (building)\n
	 *          ...
	 *          \tWalk direction to (name2)\n
	 *          Total distance: n pixel units.\n
	 *          where name1 and name2 are the names of the building1 and building2,
	 *          building is the name of a building / ID of an intersection on the path,
	 *          direction is the direction of an edge on the path (one of the 8 cardinal 
	 *          directions), and n is the total distance of a path in pixels rounded to the
	 *          nearest thousandth.
	 *          
	 *          Note: Intersection IDs are printed as: (Intersection id)
	 *          Note: (0,0) is seen as the top left of the map, so if A has a greater
	 *                y-coordinate than B, A is South of B
	 * @throws none
	 * @returns none
	 */
	public void printShortestPath(String building1, String building2) {
		String msg = "";
		
		Integer id1 = null;
		if (!building1.equals("") && isInteger(building1)) {
			id1 = Integer.parseInt(building1);
		} else if (!building1.equals("")) {
			id1 = map.getId(building1);
		}
		if (id1 == null || !map.containsBuilding(id1)) {
			msg += String.format("Unknown building: [%s]\n", building1);
		}
		
		Integer id2 = null;
		if (!building2.equals("") && isInteger(building2)) {
			id2 = Integer.parseInt(building2);
		} else if (!building2.equals("")) {
			id2 = map.getId(building2);
		}
		if (!building1.equals(building2) && (id2 == null || !map.containsBuilding(id2))) {
			msg += String.format("Unknown building: [%s]\n", building2);
		}
		if (msg.length() > 0) {
			System.out.print(msg);
			return;
		}
		
		String name1 = map.getName(id1);
		String name2 = map.getName(id2);
		List<SimpleEntry<Integer, Double>> path = map.shortestPath(id1, id2);
		
		if (path == null) {
			System.out.format("There is no path from %s to %s.\n", name1, name2);
			return;
		}
		
		double totalDistance = 0.0;
		int prevId = id1;
		
		System.out.format("Path from %s to %s:\n", name1, name2);
		for (SimpleEntry<Integer, Double> edge : path) {
			Integer nextId = edge.getKey();
			String nextBuilding = map.containsIntersection(nextId) ? String.format("Intersection %d", nextId) : map.getName(nextId);
			
			SimpleEntry<Integer, Integer> prevCoords = map.getCoords(prevId);
			SimpleEntry<Integer, Integer> nextCoords = map.getCoords(nextId);
			int x2 = nextCoords.getKey();
			int y2 = nextCoords.getValue();
			int x1 = prevCoords.getKey();
			int y1 = prevCoords.getValue();
			if (y1 < y2) { //shift y-coord by 2*delta-y (doesn't affect coords directly East/West of each other)
				y1 += 2 * Math.abs(y1-y2); //if y1 < y2, prev should be North of next
			} else {
				y1 -= 2 * Math.abs(y1-y2); //if y1 > y2, prev should be South of next
			}
			String direction = Formulas.direction(x1,y1,x2,y2);
			
			System.out.format("\tWalk %s to (%s)\n", direction, nextBuilding);
			totalDistance += edge.getValue();
			prevId = nextId;
		}
		System.out.format("Total distance: %.3f pixel units.\n", totalDistance);
	}
	
	// https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
	private static boolean isInteger(String s) {
	    Scanner sc = new Scanner(s.trim());
	    if(!sc.hasNextInt(10)) return false;
	    // we know it starts with a valid int, now make sure
	    // there's nothing left!
	    sc.nextInt(10);
	    return !sc.hasNext();
	}
	
	/**
	 * Print options available to user on command line
	 * @requires none
	 * @modifies none
	 * @effects Prints options user can use
	 * @throws none
	 * @returns none
	 */
	public void printMenu() {
		System.out.println("Options:");
		System.out.println("\tm - View command options and descriptions");
		System.out.println("\tb - View list of buildings and their IDs. Intersections (nodes with no name) are not considered buildings.");
		System.out.println("\tr - Find the shortest path between two buildings. This option will prompt you to input two building names/IDs and will output the path.");
		System.out.println("\tl - Load map with new building and path data. File names are relative to the root folder of this repo (ex: load data/rpi_map_simple_nodes.csv).");
		System.out.println("\t    The building file must follow the format: \"name,id,x-coord,y-coord\"");
		System.out.println("\t    The path file must follow the format: \"id1,id2\"");
		System.out.println("\tq - Quit application");
	}
	
	public static void main(String[] args) {
		//initialize model
		CampusPaths controller = null;
		try {
			controller = new CampusPaths();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//listen for user input
		Scanner scanner = new Scanner(System.in);
		String input = "";
		while (!input.equals("q")) {
			input = scanner.nextLine();
			if (input.equals("b")) {
				controller.printBuildings();
			} else if (input.equals("r")) {
				System.out.print("First building id/name, followed by Enter: ");
				String building1 = scanner.nextLine();
				System.out.print("Second building id/name, followed by Enter: ");
				String building2 = scanner.nextLine();
				controller.printShortestPath(building1, building2);
			} else if (input.equals("m")) {
				controller.printMenu();
			} else if (input.equals("l")) {
				System.out.print("Building file name, followed by Enter: ");
				String buildingFileName = scanner.nextLine();
				System.out.print("Building paths file name, followed by Enter: ");
				String buildingPathFileName = scanner.nextLine();
				controller.loadMap(buildingFileName, buildingPathFileName);
			} else if (!input.equals("q")) {
				System.out.println("Unknown option");
			}
		}
		scanner.close();
	}
}