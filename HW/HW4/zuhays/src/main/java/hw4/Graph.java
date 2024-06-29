package hw4;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * A Graph is a mutable representation of a directed, labeled multigraph. This graph
 * can be an empty graph (no nodes and edges), a graph without edges, a graph with any
 * number of edges between a pair of nodes, and a graph with reflexive edges. All nodes 
 * and edges in the graph are labeled.
 * T1 is the type of the nodes and T2 is the type of the edge labels. T1 and T2 should have
 * Object.equals() and Object.hashCode() implemented.
 * It is not recommended that you mutate any data passed in to the graph outside of the
 * graph, as this can lead to unknown behavior.
 */
public class Graph<T1, T2> {
	private Map<T1, Set<Edge<T1, T2>>> outgoingEdges;
	
	// Abstraction function:
	// A Graph represents a directed, labeled multigraph where the nodes are the keys of 
	// outgoingEdges and the edges are the edges in the sets stored in the values of outgoingEdges
	
	// Representation invariant: (no null nodes/edges, nodes in edges are in graph, edges are in the sets they should be in)
	// forall n in outgoingEdges: n != null &&
	// forall e in outgoingEdges[n]: e != null && n == e.outgoingNode() &&
	//                               e.incomingNode() in outgoingEdges && e.outgoingNode() in outgoingEdges
	
	/**
	 * Constructs an empty graph (no nodes and no edges)
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns A graph with no nodes and no edges
	 */
	public Graph() {
		this.outgoingEdges = new LinkedHashMap<T1, Set<Edge<T1, T2>>>();
		checkRep();
	}
	
	/**
	 * Constructs a copy of a graph
	 * @param g Graph to be copied
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if g == null
	 * @returns A graph with the same data as g
	 */
	public Graph(Graph<T1, T2> g) {
		if (g == null) { //after this g is guaranteed to not violate rep invariant (see above constructors)
			throw new NullPointerException("Given graph is null");
		}
		
		this.outgoingEdges = new LinkedHashMap<T1, Set<Edge<T1, T2>>>();
		g.outgoingEdges.forEach((node, edges) -> {
			this.outgoingEdges.put(node, new LinkedHashSet<Edge<T1, T2>>());
			//copy outgoing edges
			g.outgoingEdges.get(node).iterator().forEachRemaining((edge) -> {
				this.outgoingEdges.get(node).add(edge);	
			});
		});
		
		checkRep();
	}
	
	/**
	 * Get all nodes in the graph
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Set of nodes in the graph
	 */
	public Set<T1> nodes() {
		//not returning this.outgoingEdges.keySet() because that changes when outgoingEdges has a node added/removed
		Set<T1> nodeSet = new LinkedHashSet<T1>();
		
		this.outgoingEdges.forEach((node, edges) -> {
			nodeSet.add(node);
		});
		
		return nodeSet;
	}
	
	/**
	 * Check if a node is in the graph
	 * @param n Node to be checked
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if n == null
	 * @returns True if there exists a node in the graph s.t. node.equals(n). False otherwise
	 */
	public boolean containsNode(T1 n) {
		if (n == null) {
			throw new NullPointerException("Given node is null");
		}
		return this.outgoingEdges.containsKey(n);
	}
	
	/**
	 * Get all edges in the graph
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Map of edges in the graph. 
	 *          Key = node, value = set of outgoing edges for the node.
	 *          Map keys are in insertion order and each key's set of edges is in 
	 *          insertion order.
	 */
	public Map<T1, Set<Edge<T1, T2>>> edges() {
		Map<T1, Set<Edge<T1, T2>>> edgesMap = new LinkedHashMap<T1, Set<Edge<T1, T2>>>();
		
		this.outgoingEdges.forEach((node, edges) -> {
			Set<Edge<T1, T2>> edgeSet = new LinkedHashSet<Edge<T1, T2>>();
			edges.forEach((edge) -> { //don't copy reference to set in outgoingEdges
				edgeSet.add(edge);
			});
			edgesMap.put(node, edgeSet);
		});
		
		return edgesMap;
	}
	
	/**
	 * Get all edges from one node to another node
	 * @param parent Node on the outgoing side of the edge
	 * @param child Node on the incoming side of the edge
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if parent == null || child == null
	 * @returns If parent or child isn't in the graph, an empty set is returned.
	 *          Else, a set of edges <parent, child> is returned.
	 *          The set of edges is in order of when the parent node was added and
	 *          when each parent node's edge was added
	 */
	public Set<Edge<T1, T2>> edges(T1 parent, T1 child) {
		if (parent == null) {
			throw new NullPointerException("Given parent node is null");
		}
		if (child == null) {
			throw new NullPointerException("Given child node is null");
		}
		
		Set<Edge<T1, T2>> edgeSet = new LinkedHashSet<Edge<T1, T2>>();
		if (this.outgoingEdges.containsKey(parent)) {
			this.outgoingEdges.get(parent).iterator().forEachRemaining((edge) -> {
				if (edge.child().equals(child)) {
					edgeSet.add(edge);
				}
			});
		}
		
		return edgeSet;
	}
	
	/**
	 * Get all outgoing edges of a node
	 * @param parent Node whose outgoing edges you want
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if parent == null
	 * @returns If parent is not in the graph, an empty set is returned.
	 *          Else, a set of parent's outgoing edges is returned 
	 *          (forall outgoingEdges e: e == <parent, _>).
	 *          The set of edges is in insertion order
	 */
	public Set<Edge<T1, T2>> outgoingEdges(T1 parent) {
		if (parent == null) {
			throw new NullPointerException("Given parent node is null");
		}
		
		Set<Edge<T1, T2>> edgeSet = new LinkedHashSet<Edge<T1, T2>>();
		if (this.outgoingEdges.containsKey(parent)) {
			this.outgoingEdges.get(parent).iterator().forEachRemaining((edge) -> {
				edgeSet.add(edge);
			});
		}
		
		return edgeSet;
	}
	
	/**
	 * Get all incoming edges of a node
	 * @param child Node whose incoming edges you want
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if child == null
	 * @returns If child is not in the graph, an empty map is returned.
	 *          Else, a map with keys parent nodes and value set of incoming 
	 *          edges from parent node is returned  
	 *          (forall incomingEdges parent: forall incomingEdges[parent] e: e == <parent, child>)
	 *          The map's keys are in insertion order and the edges in each set are in insertion order
	 */
	public Map<T1, Set<Edge<T1, T2>>> incomingEdges(T1 child) {
		if (child == null) {
			throw new NullPointerException("Given child node is null");
		}
		
		Map<T1, Set<Edge<T1, T2>>> edgesMap = new LinkedHashMap<T1, Set<Edge<T1, T2>>>();
		
		if (this.outgoingEdges.containsKey(child)) {
			this.outgoingEdges.forEach((node, edges) -> {
				Set<Edge<T1, T2>> edgeSet = new LinkedHashSet<Edge<T1, T2>>();
				edges.iterator().forEachRemaining((edge) -> {
					if (edge.child().equals(child)) {
						edgeSet.add(edge);
					}
				});
				edgesMap.put(node, edgeSet);
			});
		}
		
		return edgesMap;
	}
	
	/**
	 * Get out-degree of a node (number of outgoing edges of a node)
	 * @param parent Node whose out-degree you want
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if parent == null
	 * @returns If parent is not in the graph, -1 is returned.
	 *          Else, out-degree of parent is returned.
	 *          out-degree >= 0
	 */
	public int outDegree(T1 parent) {
		if (parent == null) {
			throw new NullPointerException("Given parent node is null");
		}
		if (!this.outgoingEdges.containsKey(parent)) {
			return -1;
		}
		return this.outgoingEdges.get(parent).size();
	}
	
	/**
	 * Get in-degree of a node (number of incoming edges of a node)
	 * @param child Node whose in-degree you want
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if child == null
	 * @returns If child is not in the graph, -1 is returned.
	 *          Else, in-degree of child is returned.
	 *          in-degree >= 0
	 */
	public int inDegree(T1 child) {
		if (child == null) {
			throw new NullPointerException("Given child node is null");
		}
		if (this.outgoingEdges.containsKey(child)) {
			int degree = 0;
			for (T1 node : this.outgoingEdges.keySet()) {
				for (Edge<T1, T2> edge : this.outgoingEdges.get(node)) {
					if (edge.child().equals(child)) {
						degree++;
					}
				}
			}
			return degree;
		}
		return -1;
	}
	
	/**
	 * Check if an edge is in the graph
	 * @param parent Node on outgoing side of edge
	 * @param child Node on incoming side of edge
	 * @param label Edge label
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws NullPointerException if parent == null || child == null || label == null
	 * @returns True if there exists an edge in the graph s.t. edge.parent == parent, 
	 *          edge.child == child, and edge.label == label. False otherwise
	 */
	public boolean containsEdge(T1 parent, T1 child, T2 label) {
		if (parent == null) {
			throw new NullPointerException("Given parent node is null");
		}
		if (child == null) {
			throw new NullPointerException("Given child node is null");
		}
		if (label == null) {
			throw new NullPointerException("Given label is null");
		}
		if (!this.outgoingEdges.containsKey(parent)) {
			return false;
		}
		return this.outgoingEdges.get(parent).contains(new Edge<T1, T2>(child, label));
	}
	
	/**
	 * Add a node to the graph
	 * @param n Node to be added
	 * @requires none
	 * @modifies this
	 * @effects Adds n to the graph
	 * @throws NullPointerException if n == null
	 * @returns True if n isn't in the graph already. False otherwise
	 */
	public boolean addNode(T1 n) {
		if (n == null) {
			throw new NullPointerException("Given node is null");
		}
		if (this.outgoingEdges.containsKey(n)) {
			return false;
		}
		
		this.outgoingEdges.put(n, new LinkedHashSet<Edge<T1, T2>>());
		
//		checkRep();
		
		return true;
	}
	
	/**
	 * Remove a node from the graph
	 * @param n Node to be removed
	 * @requires none
	 * @modifies this
	 * @effects Removes n and any connected edges from the graph
	 * @throws NullPointerException if n == null
	 * @returns True if n was in the graph. False otherwise
	 */
	public boolean removeNode(T1 n) {
		if (n == null) {
			throw new NullPointerException("Given node is null");
		}
		if (!this.outgoingEdges.containsKey(n)) {
			return false;
		}
		
		//remove outgoing edges
		this.outgoingEdges.remove(n);
		
		//remove incoming edges
		Map<T1, Set<Edge<T1, T2>>> removeEdges = new LinkedHashMap<T1, Set<Edge<T1, T2>>>();
		this.outgoingEdges.forEach((node, edges) -> {
			Set<Edge<T1, T2>> edgeSet = new LinkedHashSet<Edge<T1, T2>>();
			for (Edge<T1, T2> edge : edges) { //can't remove in loop cuz funky stuff happens
				if (edge.child().equals(n)) {
					edgeSet.add(edge);
				}
			}
			removeEdges.put(node, edgeSet);
		});
		removeEdges.forEach((node, edges) -> {
			edges.forEach((edge) -> {
				this.removeEdge(node, edge.child(), edge.label());
			});
		});
		
		return true;
	}
	
	/**
	 * Add an edge to the graph
	 * @param parent Node on outgoing side of edge
	 * @param child Node on incoming side of edge
	 * @param label Edge label
	 * @requires none
	 * @modifies this
	 * @effects Adds edge to the graph
	 * @throws NullPointerException if parent == null || child == null || label == null
	 * @returns True if parent and child is in the graph and edge isn't in
	 *          the graph already. False otherwise
	 */
	public boolean addEdge(T1 parent, T1 child, T2 label) {
		if (parent == null) {
			throw new NullPointerException("Given parent node is null");
		}
		if (child == null) {
			throw new NullPointerException("Given child node is null");
		}
		if (label == null) {
			throw new NullPointerException("Given label is null");
		}
		Edge<T1, T2> e = new Edge<T1, T2>(child, label);
		if (!this.outgoingEdges.containsKey(parent) || !this.outgoingEdges.containsKey(child) || 
				this.outgoingEdges.get(parent).contains(e)) {
			return false;
		}
		this.outgoingEdges.get(parent).add(e);
		
//		checkRep();
		
		return true;
	}
	
	/**
	 * Remove an edge from the graph
	 * @param parent Node on outgoing side of edge
	 * @param child Node on incoming side of edge
	 * @param label Edge label
	 * @requires none
	 * @modifies this
	 * @effects Removes edge from the graph
	 * @throws NullPointerException if parent == null || child == null || label == null
	 * @returns True if parent and child is in graph and parent has outgoing edge 
	 *          <parent, child> with label label. False otherwise
	 */
	public boolean removeEdge(T1 parent, T1 child, T2 label) {
		if (parent == null) {
			throw new NullPointerException("Given parent node is null");
		}
		if (child == null) {
			throw new NullPointerException("Given child node is null");
		}
		if (label == null) {
			throw new NullPointerException("Given label is null");
		}
		if (!this.outgoingEdges.containsKey(parent)) {
			return false;
		}
		Edge<T1, T2> e = new Edge<T1, T2>(child, label);
		return this.outgoingEdges.get(parent).remove(e);
	}
	
	private void checkRep() {
		this.outgoingEdges.forEach((node, edges) -> {
			//check for null node
			if (node == null) {
				throw new RuntimeException("Null node");				
			}
			
			this.outgoingEdges.get(node).forEach((edge) -> {
				//check for null edge
				if (edge == null) {
					throw new RuntimeException("Null outgoing edge");
				}
				//check that edge's nodes are in nodes set
				if (!this.outgoingEdges.containsKey(edge.child())) {
					throw new RuntimeException("Outgoing edge's child node is not a key in outgoingEdges map");
				}
			});
		});
	}
}
