package hw7;

/**
 * Formulas contains common math formulas
 */
public final class Formulas {
	
	//Formulas isn't an ADT
	
	/**
	 * Get the Euclidean distance between (x1,y1) and (x2,y2)
	 * @param x1 x-coord of first point
	 * @param y1 y-coord of first point
	 * @param x2 x-coord of second point
	 * @param y2 y-coord of second point
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws none
	 * @returns Euclidean distance between (x1,y1) and (x2,y2) (sqrt((x2-x1)^2 + (y2-y1)^2))
	 */
	public static double euclideanDistance(int x1, int y1, int x2, int y2) {
		double x = Math.pow(x2 - x1, 2);
		double y = Math.pow(y2 - y1, 2);
		return Math.sqrt(x + y);
	}
	
	/**
	 * Get the angle of the line going from (x1,y1) to (x2,y2)
	 * @param x1 x-coord of first point
	 * @param y1 y-coord of first point
	 * @param x2 x-coord of second point
	 * @param y2 y-coord of second point
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws IllegalArgumentException if x1 == x2 && y1 == y2
	 * @returns Angle of the line, 0.0 <= angle < 360.0.
	 *          0.0 = North, 90.0 = East, 180.0 = South, 270.0 = West
	 */
	public static double angle(int x1, int y1, int x2, int y2) {
		if (x1 == x2 && y1 == y2) {
			throw new IllegalArgumentException("Points can't overlap");
		}
		
		// https://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
		double angle = Math.toDegrees(Math.atan2(x2-x1, y2-y1));
		if (angle < 0.0) {
			angle += 360.0;
		}
		return angle;
	}
	
	/**
	 * Get the direction (8 cardinal directions) of the line going from (x1,y1) to (x2,y2)
	 * @param x1 x-coord of first point
	 * @param y1 y-coord of first point
	 * @param x2 x-coord of second point
	 * @param y2 y-coord of second point
	 * @requires none
	 * @modifies none
	 * @effects none
	 * @throws IllegalArgumentException if x1 == x2 && y1 == y2
	 * @returns Let angle be the angle of the line.
	 *          "North" if 337.5 <= angle || angle < 22.5, "NorthEast" if 22.5 <= angle < 67.5,
	 *          "East" if 67.5 <= angle < 112.5, "SouthEast" if 112.5 <= angle < 157.5,
	 *          "South" if 157.5 <= angle < 202.5, "SouthWest" if 202.5 <= angle < 247.5,
	 *          "West" if 247.5 <= angle < 292.5, "NorthWest" if 292.5 <= angle < 337.5
	 */
	public static String direction(int x1, int y1, int x2, int y2) {
		double angle = angle(x1,y1,x2,y2);
		if (angle >= 337.5 || angle < 22.5) {
			return "North";
		} else if (angle >= 22.5 && angle < 67.5) {
			return "NorthEast";
		} else if (angle >= 67.5 && angle < 112.5) {
			return "East";
		} else if (angle >= 112.5 && angle < 157.5) {
			return "SouthEast";
		} else if (angle >= 157.5 && angle < 202.5) {
			return "South";
		} else if (angle >= 202.5 && angle < 247.5) {
			return "SouthWest";
		} else if (angle >= 247.5 && angle < 292.5) {
			return "West";
		} else {
			return "NorthWest";
		}
	}
}
