package hw4;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

public class GraphWrapper {
	private Graph<String, String> graph;
	
	public GraphWrapper() {
		graph = new Graph<String, String>();
	}
	
	public void addNode(String nodeData) {
		graph.addNode(nodeData);
	}
	
	public void addEdge(String parentNode, String childNode, String edgeLabel) {
		if (!graph.containsNode(parentNode)) {
			graph.addNode(parentNode);
		}
		if (!graph.containsNode(childNode)) {
			graph.addNode(childNode);
		}
		graph.addEdge(parentNode, childNode, edgeLabel);
	}
	
	public Iterator<String> listNodes() {
		List<String> nodes = new ArrayList<String>(graph.nodes()); //convert set to list
		nodes.sort(null); //natural ordering = lexicographical
		return nodes.iterator();
	}
	
	public Iterator<String> listChildren(String parentNode) {
		List<Edge<String, String>> edges = new ArrayList<Edge<String, String>>(graph.outgoingEdges(parentNode));
		Collections.sort(edges, new Comparator<Edge<String, String>>() {
			public int compare(Edge<String, String> e1, Edge<String, String> e2) {
				String n1 = e1.child();
				String n2 = e2.child();
		        if (n1.equals(n2)) {
		        	return e1.label().compareTo(e2.label());
		        }
		        return n1.compareTo(n2);
		    }
		});
		
		List<String> children = new ArrayList<String>();
		for (Edge<String, String> edge : edges) {
			children.add(edge.child() + "(" + edge.label() + ")");
		}
		return children.iterator();
	}
}