package hw4;

/**
 * An Edge is an immutable representation of an outgoing edge with a label.
 * 
 */
public class Edge<T1, T2> {
	private final T1 child;
	private final T2 label;
	
	// Abstraction function:
	// Edge represents an edge to node child with label label
	
	// Representation invariant:
	// child != null && label != null
	
	/**
	 * Constructs an edge to node child with label label
	 * @param child Node on outgoing side of edge
	 * @param label Edge label
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if child == null || label == null
	 * @returns An edge from to child with label label
	 */
	public Edge(T1 child, T2 label) {
		if (child == null) {
			throw new NullPointerException("Given child node is null");
		}
		if (label == null) {
			throw new NullPointerException("Given label is null");
		}
		this.child = child;
		this.label = label;
		checkRep();
	}
	
	/**
	 * Constructs a copy of an edge
	 * @param e Edge to be copied
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if e == null
	 * @returns An edge with the same data as e
	 */
	public Edge(Edge<T1,T2> e) throws NullPointerException {
		if (e == null) { //e's fields are guaranteed to not be null from above constructors
			throw new NullPointerException("Given edge is null");
		}
		this.child = e.child;
		this.label = e.label;
		checkRep();
	}
	
	/**
	 * Get edge's child node
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Edge's outgoing node
	 */
	public T1 child() {
		return this.child;
	}
	
	/**
	 * Get edge's label
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Edge's label
	 */
	public T2 label() {
		return this.label;
	}
	
	/**
	 * Check if edges are equal
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns True iff obj is an Edge && obj.child.equals(this.child)
	 *                                  && obj.label.equals(this.label)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Edge) {
			Edge<?,?> e = (Edge<?,?>) obj;
			return e.child.equals(this.child) && e.label.equals(this.label);
		}
		return false;
	}
	
	/**
	 * Standard hashCode function
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns An int that all objects equal to this will also return
	 */
	@Override
	public int hashCode() {
		// https://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method-for-a-collection
		int result = 19;
		result = 37 * result + this.child.hashCode();
		result = 37 * result + this.label.hashCode();
		return result;
	}
	
	private void checkRep() throws RuntimeException {
		if (this.child == null) {
			throw new RuntimeException("Child node is null");
		}
		if (this.label == null) {
			throw new RuntimeException("Label is null");
		}
	}
}
