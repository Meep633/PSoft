package hw7;

/**
 * MapNode represents an immutable node on RPI's campus map. It contains data about 
 * the name of a building (empty string if an intersection) and the id
 * of a building/intersection.
 */
public class RPIMapNode {
	private final String name;
	private final int id;
	private final int x;
	private final int y;
	
	// Abstraction function:
	// MapNode represents a building/intersection on RPI's campus map with 
	// name name, id id, and coords (x,y).
	
	// Representation invariant: name != null
	
	/**
	 * Create an RPIMapNode with the given data
	 * @param name Name of node
	 * @param id ID of node
	 * @param x x-coord of node
	 * @param y y-coord of node
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if name == null
	 * @returns RPIMapNode with given data
	 */
	public RPIMapNode(String name, int id, int x, int y) {
		if (name == null) {
			throw new NullPointerException("Given name is null");
		}
		this.name = name;
		this.id = id;
		this.x = x;
		this.y = y;
		checkRep();
	}
	
	/**
	 * Create a copy of an RPIMapNode
	 * @param node Node to be copied
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if node == null
	 * @returns Copy of node
	 */
	public RPIMapNode(RPIMapNode node) {
		if (node == null) {
			throw new NullPointerException("Given node is null");
		}
		this.name = node.name;
		this.id = node.id;
		this.x = node.x;
		this.y = node.y;
	}
	
	/**
	 * Get node's name
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Node name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get node's id
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Node id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Get node's x-coord
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @returns Node's x-coord
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Get node's y-coord
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Node's y-coord
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Check if a node is an intersection or not
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns True if node is an intersection (name is an empty string).
	 *          False otherwise
	 */
	public boolean isIntersection() {
		return this.name.length() == 0;
	}
	
	@Override
	/**
	 * Check if nodes are equal
	 * @param o Object to be compared to this
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns True if o is an RPIMapNode and this.id == o.id. False otherwise
	 */
	public boolean equals(Object o) {
		if (o instanceof RPIMapNode) {
			RPIMapNode r = (RPIMapNode) o;
			return this.id == r.id;
		}
		return false;
	}
	
	@Override
	/**
	 * Standard hash code function
	 * @requires none
	 * @modiifes none
	 * @effects none
	 * @throws none
	 * @returns An int that all objects equal to this will also return
	 */
	public int hashCode() {
		return this.id;
	}
	
	private void checkRep() {
		if (this.name == null) {
			throw new RuntimeException("Name is null");
		}
	}
}
