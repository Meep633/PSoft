package hw5;

import java.util.*;
import java.io.*;

public class CSVParser {
	
	/**
	 * @param: filename        The path to a "CSV" file that contains the
	 *                         "node","relationship" pairs
	 * @param: relationships   The Map that stores parsed <relationship,
	 *                         Set-of-nodes> pairs; usually an empty Map.
	 * @param: nodes           The Set that stores parsed nodes; usually an empty
	 *                         Set.
	 * @requires: filename != null && relationships != null && nodes != null
	 * @modifies: relationships, nodes
	 * @effects: adds parsed <relationship, Set-of-nodes> pairs to Map
	 *           relationships; adds parsed nodes to Set nodes.
	 * @throws: IOException if file cannot be read or file not a CSV file following
	 *                      the proper format.
	 * @returns: None
	 */
	public static void readData(String filename, Map<String, Set<String>> relationships, Set<String> nodes)
			throws IOException {

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line = null;

			while ((line = reader.readLine()) != null) {
				int i = line.indexOf("\",\"");
				if ((i == -1) || (line.charAt(0) != '\"') || (line.charAt(line.length() - 1) != '\"')) {
					throw new IOException("File " + filename + " not a CSV (\"RELATIONSHIP\",\"NODE\") file.");
				}
				String node = line.substring(1, i);
				String relationship = line.substring(i + 3, line.length() - 1);

				// Adds the professor to the professor set. If professor is already in, add has
				// no effect.
				nodes.add(node);

				// Adds the professor to the set for the given course
				Set<String> s = relationships.get(relationship);
				if (s == null) {
					s = new HashSet<String>();
					relationships.put(relationship, s);
				}
				s.add(node);
			}
		}
	}

	public static void main(String[] arg) {

		String file = "data/big_data.csv";

		try {
			Map<String, Set<String>> profsTeaching = new HashMap<String, Set<String>>();
			Set<String> profs = new HashSet<String>();
			readData(file, profsTeaching, profs);
//			Map<String, Set<String[]>> edges = new HashMap<String, Set<String[]>>();
//			for (String p : profs) {
//				edges.put(p, new HashSet<String[]>());
//			}
//			profsTeaching.forEach((course, professors) -> {
//				for (String p1 : professors) {
//					for (String p2 : professors) {
//						String[] arr = {p2, course};
//						edges.get(p1).add(arr);
//					}
//				}
//			});
			System.out.println(
					"Read " + profs.size() + " profs who have taught " + profsTeaching.keySet().size() + " courses.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
