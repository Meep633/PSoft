package hw4;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.AbstractMap.SimpleEntry;

/**
 * PathAlgorithms is a set of path-finding algorithms between 2 nodes.
 */
public final class PathAlgorithms {
	
	//PathAlgorithms is not an ADT
	
	/**
	 * Find a path between two nodes using BFS
	 * @param <T1> Data type of nodes in graph
	 * @param <T2> Data type of labels in graph
	 * @param graph Graph containing nodes and edges
	 * @param start Node at start of path
	 * @param end Node at end of path
	 * @param edgeComparator Comparator to determine what edge to look at first (if a node has
	 *                       multiple edges that haven't been explored, use comparator to 
	 *                       determine which edge to look at first)
	 * @requires graph must contain start and end
	 * @modifies none
	 * @effects none
	 * @returns If no path exists from start to end, null is returned. Else, a list of edges
	 *          in order from start to end is returned.
	 * @throws NullPointerException if graph == null || start == null || end == null || 
	 *                                 edgeComparator == null
	 */
	public static <T1, T2> List<Edge<T1, T2>> bfs(Graph<T1, T2> graph, T1 start, T1 end, Comparator<Edge<T1, T2>> edgeComparator) {
		if (graph == null || start == null || end == null || edgeComparator == null) {
			throw new NullPointerException("Null argument");
		}
		
		Queue<T1> queue = new LinkedList<T1>();
		Map<T1, List<Edge<T1, T2>>> visitedNodePaths = new HashMap<T1, List<Edge<T1, T2>>>();
		
		queue.add(start);
		visitedNodePaths.put(start, new ArrayList<Edge<T1, T2>>());
		while (queue.size() > 0) {
			T1 curr = queue.poll(); //remove oldest node
			if (curr.equals(end)) {
				return visitedNodePaths.get(curr);
			}
			
			List<Edge<T1, T2>> outgoingEdges = new ArrayList<Edge<T1, T2>>(graph.outgoingEdges(curr));
			Collections.sort(outgoingEdges, edgeComparator);
			
			for (Edge<T1, T2> edge : outgoingEdges) {
				T1 next = edge.child();
				if (!visitedNodePaths.containsKey(next)) { //node not visited
					List<Edge<T1, T2>> path = new ArrayList<Edge<T1, T2>>();
					for (Edge<T1, T2> e : visitedNodePaths.get(curr)) {
						path.add(e); //making sure not to copy a reference to the list
					}
					path.add(edge);
					visitedNodePaths.put(next, path);
					queue.add(next);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Find a path between two nodes using Dijkstra's algorithm
	 * @param <T1> Data type of nodes
	 * @param <T2> Data type of edge labels
	 * @param graph Graph containing nodes and edges
	 * @param start Node at start of path
	 * @param end Node at end of path
	 * @param lambda Lambda function that takes in an edge (Edge<T1, T2>) and outputs a weight (Double)
	 * @requires graph must contain start and end. graph cannot contain negative cycles.
	 * @modifies none
	 * @effects none
	 * @returns If no path exists from start to end, null is returned. Else, a list of edges 
	 *          in order from start to end is returned.
	 * @throws NullPointerException if graph == null || start == null || end == null ||
	 *                                 lambda == null
	 */
	public static <T1, T2> List<Edge<T1, T2>> dijkstra(Graph<T1, T2> graph, T1 start, T1 end, EdgeWeightLambda<T1, T2> lambda) {
		if (graph == null || start == null || end == null || lambda == null) {
			throw new NullPointerException("Null argument");
		}
		
		//used to break ties when two nodes have multiple edges between them
		Comparator<Edge<T1, T2>> edgeComparator = new Comparator<Edge<T1, T2>>() {
			@Override
			public int compare(Edge<T1, T2> a, Edge<T1, T2> b) {
				return lambda.calcWeight(a).compareTo(lambda.calcWeight(b));
			}
		};
		//used to determine path priority
		Comparator<SimpleEntry<List<Edge<T1, T2>>, Double>> pathComparator = new Comparator<SimpleEntry<List<Edge<T1, T2>>, Double>>() {
			@Override
			public int compare(SimpleEntry<List<Edge<T1, T2>>, Double> a, SimpleEntry<List<Edge<T1, T2>>, Double> b) {
				return a.getValue().compareTo(b.getValue());
			}
		};
		
		Queue<SimpleEntry<List<Edge<T1, T2>>, Double>> active = new PriorityQueue<SimpleEntry<List<Edge<T1, T2>>, Double>>(pathComparator);
		active.add(new SimpleEntry<List<Edge<T1, T2>>, Double>(new ArrayList<Edge<T1, T2>>(), 0.0));
		Set<T1> finished = new HashSet<T1>();
		
		while (active.size() != 0) {
			T1 minDest = start;
			SimpleEntry<List<Edge<T1, T2>>, Double> minEntry = active.poll();
			List<Edge<T1, T2>> minPath = minEntry.getKey();
			Double minCost = minEntry.getValue();
			if (minPath.size() > 0) { //special case: empty list = at start
				minDest = minPath.get(minPath.size() - 1).child();
			}
			if (minDest.equals(end)) {
				return minPath;
			}
			if (finished.contains(minDest)) {
				continue;
			}
			
			List<Edge<T1, T2>> outgoingEdges = new ArrayList<Edge<T1, T2>>(graph.outgoingEdges(minDest));
			Collections.sort(outgoingEdges, edgeComparator); //if nodes have multiple edges between them, prioritize lowest weight
			for (Edge<T1, T2> edge : outgoingEdges) {
				if (!finished.contains(edge.child())) {
					List<Edge<T1, T2>> newPath = new ArrayList<Edge<T1, T2>>(minPath);
					newPath.add(edge);
					Double newCost = minCost + lambda.calcWeight(edge);
					active.add(new SimpleEntry<List<Edge<T1, T2>>, Double>(newPath, newCost));
				}
			}
			
			finished.add(minDest);
		}
		
		return null;
	}
	
	/**
	 * Interface for edge weight lambda used in Dijkstra's algorithm. Given an edge,
	 * it calculates a weight.
	 * @param <T1> Data type of node
	 * @param <T2> Data type of label
	 */
	public interface EdgeWeightLambda<T1, T2> {
		
		//EdgeWeightLambda isn't an ADT
		
		/**
		 * Calculate weight of edge
		 * @param e Edge to calculate weight of
		 * @requires e != null
		 * @modifies none
		 * @effects none
		 * @returns Edge weight
		 */
		Double calcWeight(Edge<T1, T2> e);
	}
}