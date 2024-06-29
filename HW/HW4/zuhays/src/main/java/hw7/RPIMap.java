package hw7;

import hw4.Graph;
import hw4.PathAlgorithms;
import hw4.Edge;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;
import java.io.IOException;

/**
 * RPIMap is a model of a graph which represents buildings and intersections with nodes
 * and paths between buildings and intersections with edges.
 */
public class RPIMap {
	private Graph<RPIMapNode, Double> graph;
	private Map<Integer, RPIMapNode> buildings; // id: <id,name,x,y>
//	private List<View> views;
	
	// RPIMap isn't an ADT
	
	/**
	 * Create an empty RPIMap
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns An empty RPIMap
	 */
	public RPIMap() {
		graph = new Graph<RPIMapNode, Double>();
		buildings = new HashMap<Integer, RPIMapNode>();
//		views = new ArrayList<View>();
	}
	
	/**
	 * Create a new RPIMap with the nodes and edges specified by the given files
	 * @param nodeFileName Path to a CSV file containing node data in the format:
	 *                     name,id,x-coord,y-coord
	 * @param edgeFileName Path to a CSV file containing edge data in the format:
	 *                     id1,id2
	 *                     where id1 and id2 are the ids of a building/intersection
	 * @requires none
	 * @modifies this.graph, this.buildingNames
	 * @effects Fills in this.graph with nodes from nodeFileName and edges from edgeFileName.
	 *          Fills in this.buildingNames with building information from nodeFileName.
	 *          Note: If multiple buildings exist with the same ID, the last building found
	 *          in nodeFileName with that ID is used.
	 * @throws NullPointerException if nodeFileName == null || edgeFileName == null
	 * @throws IOException if nodeFileName or edgeFileName isn't a CSV file that follows the
	 *         correct format
	 * @throws IllegalArgumentException if an edge <id1,id2> is found such that no node from
	 *         nodeFileName has an id of id1 or id2 or if an edge exists between two 
	 *         buildings/intersections with the same coords.
	 * @returns none
	 */
	public void createNewGraph(String nodeFileName, String edgeFileName) throws IOException {
		if (nodeFileName == null) {
			throw new NullPointerException("Given node file name is null");
		}
		if (edgeFileName == null) {
			throw new NullPointerException("Given edge file name is null");
		}
		
		Map<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>> nodes = 
				new HashMap<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>>();
		Map<Integer, Set<Integer>> edges = new HashMap<Integer, Set<Integer>>();
		RPIMapParser.readNodeData(nodeFileName, nodes);
		RPIMapParser.readEdgeData(edgeFileName, edges);
		
		//check first before adding so nothing is modified if an error occurs
		edges.forEach((parent, children) -> { //don't need to check value since every int in the set is guaranteed to be a key
			if (!nodes.containsKey(parent)) {
				throw new IllegalArgumentException("Edge found with invalid node id");
			}
		});
		
		this.graph = new Graph<RPIMapNode, Double>();
		this.buildings = new HashMap<Integer, RPIMapNode>();
		
		nodes.forEach((id, data) -> {
			String name = data.getKey();
			int x = data.getValue().getKey();
			int y = data.getValue().getValue();
			RPIMapNode node = new RPIMapNode(name, id, x, y);
			this.graph.addNode(node);
			this.buildings.put(id, node);
		});
		edges.forEach((parentId, childrenIds) -> {
			String name = nodes.get(parentId).getKey();
			int x = nodes.get(parentId).getValue().getKey();
			int y = nodes.get(parentId).getValue().getValue();
			RPIMapNode parentNode = new RPIMapNode(name, parentId, x, y);
			
			for (int childId : childrenIds) {
				name = nodes.get(childId).getKey();
				x = nodes.get(childId).getValue().getKey();
				y = nodes.get(childId).getValue().getValue();
				RPIMapNode childNode = new RPIMapNode(name, childId, x, y);
				
				double distance = Formulas.euclideanDistance(parentNode.getX(), parentNode.getY(), 
						childNode.getX(), childNode.getY());
				if (distance == 0.0) {
					throw new IllegalArgumentException("Buildings overlap");
				}
				this.graph.addEdge(parentNode, childNode, distance);
			}
			// TO DO: i didn't add reflexive edges but that might cause errors. if it does,
			// uncomment the line below
			// this.graph.addEdge(parentNode, parentNode, 0.0);
		});
	}
	
	/**
	 * Get a copy of the underlying graph
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Copy of underlying graph
	 */
	public Graph<RPIMapNode, Double> underlyingGraph() { //this method is mainly used for testing purposes only
		return new Graph<RPIMapNode, Double>(this.graph); //RPIMapNode and Double is immutable so no rep exposure from this
	}
	
	/**
	 * Get all buildings in the map
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Map of buildings where key = ID and value = entry where key = name and 
	 *          value = entry where key = x-coord and value = y-coord ({id: <name, <x,y>>}).
	 */
	public Map<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>> getBuildings() {
		Map<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>> buildingsMap =
				new HashMap<Integer, SimpleEntry<String, SimpleEntry<Integer, Integer>>>();
		
		for (Integer id : buildings.keySet()) {
			if (buildings.get(id).isIntersection()) {
				continue;
			}
			String name = buildings.get(id).getName();
			int x = buildings.get(id).getX();
			int y = buildings.get(id).getY();
			SimpleEntry<Integer, Integer> coords = new SimpleEntry<Integer, Integer>(x,y);
			SimpleEntry<String, SimpleEntry<Integer, Integer>> nameEntry =
					new SimpleEntry<String, SimpleEntry<Integer, Integer>>(name, coords);
			buildingsMap.put(id, nameEntry);
		}
		
		return buildingsMap;
	}
	
	/**
	 * Get the ID of the first building inserted with the given name
	 * @param name Building name
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if name == null
	 * @throws IllegalArgumentException if name.equals("")
	 * @returns If a building with the given name isn't found, null is returned.
	 *          Else, the ID of the first building inserted in the map with the 
	 *          given name is returned.
	 */
	public Integer getId(String name) {
		if (name == null) {
			throw new NullPointerException("Given name is null");
		}
		if (name.equals("")) {
			throw new IllegalArgumentException("Cannot retrieve intersection ID");
		}
		
		for (Integer id : buildings.keySet()) {
			if (buildings.get(id).getName().equals(name)) {
				return id;
			}
		}
		return null;
	}
	
	/**
	 * Get the name of the building with the given id
	 * @param id Building ID
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws IllegalArgumentException if given ID is an intersection ID
	 * @returns If no building is found with the given ID or the given ID is an intersection ID, 
	 *          null is returned. Else, the building's name is returned.
	 */
	public String getName(int id) {
		RPIMapNode building = buildings.get(id);
		if (building == null) {
			return null;
		} else if (building.isIntersection()) {
			throw new IllegalArgumentException("Given ID is an intersection ID");
		}
		return building.getName();
	}
	
	/**
	 * Get the coords of the building / intersection with the given id
	 * @param id Building / intersection ID
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns If no building is found with the given id, null is returned.
	 *          Else, the building's coords is returned in an entry <x-coord,y-coord>.
	 */
	public SimpleEntry<Integer, Integer> getCoords(int id) {
		RPIMapNode building = buildings.get(id);
		if (building == null) {
			return null;
		}
		int x = building.getX();
		int y = building.getY();
		return new SimpleEntry<Integer, Integer>(x,y);
	}
	
	/**
	 * Check if there exists a building in the map with the given ID
	 * @param id ID to check
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns True if there exists a building with the given ID. False otherwise.
	 */
	public boolean containsBuilding(int id) {
		return buildings.containsKey(id) && !buildings.get(id).isIntersection();
	}
	
	/**
	 * Get all intersections in the map
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Map of buildings where key = ID and value = entry where key = x-coord and 
	 *          value = y-coord ({id: <x,y>}).
	 */
	public Map<Integer, SimpleEntry<Integer, Integer>> getIntersections() {
		Map<Integer, SimpleEntry<Integer, Integer>> intersectionsMap =
				new HashMap<Integer, SimpleEntry<Integer, Integer>>();
		
		for (Integer id : buildings.keySet()) {
			if (!buildings.get(id).isIntersection()) {
				continue;
			}
			int x = buildings.get(id).getX();
			int y = buildings.get(id).getY();
			SimpleEntry<Integer, Integer> coords = new SimpleEntry<Integer, Integer>(x,y);
			intersectionsMap.put(id, coords);
		}
		
		return intersectionsMap;
	}
	
	/**
	 * Check if there exists an intersection in the map with the given ID
	 * @param id ID to check
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @return True if there exists an intersection with the given ID. False otherwise.
	 */
	public boolean containsIntersection(int id) {
		return buildings.containsKey(id) && buildings.get(id).isIntersection();
	}
	
	/**
	 * Find the shortest path from one building to another using Dijkstra's algorithm
	 * @param id1 ID of starting building
	 * @param id2 ID of ending building
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws IllegalArgumentException if id1 or id2 isn't a building ID in the map
	 * @returns If no path exists from id1 to id2, null is returned.
	 *          Else, a list of entries representing the path from id1 to id2 is returned
	 *          where each key = id of next building and value = distance from previous building
	 *          to next building.
	 */
	public List<SimpleEntry<Integer, Double>> shortestPath(int id1, int id2) {
		RPIMapNode node1 = getNode(id1);
		if (node1 == null || node1.isIntersection()) {
			throw new IllegalArgumentException("id1 isn't a building ID");
		}
		RPIMapNode node2 = getNode(id2);
		if (node2 == null || node2.isIntersection()) {
			throw new IllegalArgumentException("id2 isn't a building ID");
		}
		
		List<Edge<RPIMapNode, Double>> path = PathAlgorithms.dijkstra(this.graph, node1, node2, (edge) -> edge.label());
		if (path == null) { //no path exists
			return null;
		}
		
		List<SimpleEntry<Integer, Double>> returnPath = new ArrayList<SimpleEntry<Integer, Double>>();
		for (Edge<RPIMapNode, Double> edge : path) {
			returnPath.add(new SimpleEntry<Integer, Double>(edge.child().getId(), edge.label()));
		}
		return returnPath;
	}
	
	//privated since it shouldn't matter to clients that buildings are represented like this
	//returns null if no node with id is found
	private RPIMapNode getNode(int id) {
		Set<RPIMapNode> nodes = this.graph.nodes();
		for (RPIMapNode node : nodes) {
			if (node.getId() == id) {
				return node;
			}
		}
		return null;
	}
	
	/* TO DO for HW 8:
	public void addView(View view) {
		throw new RuntimeException("not implemented");
	}
	
	//data is whatever about this model was specifically updated
	private void updateViews(Object data) {
		for (View view : views) {
			view.update(data);
		}
	}
	*/
	
	public static void main(String[] args) {
		RPIMap map = new RPIMap();
		try {
			map.createNewGraph("data/rpi_map_simple_nodes.csv", "data/rpi_map_simple_edges.csv");			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		List<SimpleEntry<Integer, Double>> path = map.shortestPath(0, 9);
		for (SimpleEntry<Integer, Double> edge : path) {
			System.out.println(edge.getKey() + " : " + edge.getValue());
			System.out.println(map.getName(edge.getKey()));
		}
	}
}
