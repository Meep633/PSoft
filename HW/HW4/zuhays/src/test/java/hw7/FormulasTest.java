package hw7;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FormulasTest {
	@Test
	void testEuclideanDistance() {
		assertTrue(Formulas.euclideanDistance(0,0,0,0) == 0.0, "Incorrect euclid distance");
		assertTrue(Formulas.euclideanDistance(2,2,0,0) == Math.sqrt(8.0), "Incorrect euclid distance");
		assertTrue(Formulas.euclideanDistance(2,2,-2,-2) == Math.sqrt(32.0), "Incorrect euclid distance");
	}
	
	@Test
	void testAngle() {
		assertEquals(0.0, Formulas.angle(0, 0, 0, 1), "Incorrect angle");
		assertEquals(45.0, Formulas.angle(0, 0, 1, 1), "Incorrect angle");
		assertEquals(90.0, Formulas.angle(0, 0, 1, 0), "Incorrect angle");
		assertEquals(135.0, Formulas.angle(0, 1, 1, 0), "Incorrect angle");
		assertEquals(180.0, Formulas.angle(0, 1, 0, 0), "Incorrect angle");
		assertEquals(225.0, Formulas.angle(1, 1, 0, 0), "Incorrect angle");
		assertEquals(270.0, Formulas.angle(1, 0, 0, 0), "Incorrect angle");
		assertEquals(315.0, Formulas.angle(1, 0, 0, 1), "Incorrect angle");
		
		assertThrows(IllegalArgumentException.class, () -> Formulas.angle(0,0,0,0));
	}
	
	@Test
	void testDirection() {
		assertEquals("North", Formulas.direction(0, 0, -10, 1000), "Incorrect direction");
		assertEquals("North", Formulas.direction(0, 0, 0, 1), "Incorrect direction");
		assertEquals("North", Formulas.direction(0, 0, 10, 1000), "Incorrect direction");
		
		assertEquals("NorthEast", Formulas.direction(0, 0, 1, 1), "Incorrect direction");
		
		assertEquals("East", Formulas.direction(0, 0, 1000, -10), "Incorrect direction");
		assertEquals("East", Formulas.direction(0, 0, 1, 0), "Incorrect direction");
		assertEquals("East", Formulas.direction(0, 0, 1000, 10), "Incorrect direction");
		
		assertEquals("SouthEast", Formulas.direction(0, 1, 1, 0), "Incorrect direction");
		
		assertEquals("South", Formulas.direction(0, 1000, 10, 0), "Incorrect direction");
		assertEquals("South", Formulas.direction(0, 1, 0, 0), "Incorrect direction");
		assertEquals("South", Formulas.direction(0, 1000, -10, 0), "Incorrect direction");
		
		assertEquals("SouthWest", Formulas.direction(1, 1, 0, 0), "Incorrect direction");
		
		assertEquals("West", Formulas.direction(1000, 0, 0, -10), "Incorrect direction");
		assertEquals("West", Formulas.direction(1, 0, 0, 0), "Incorrect direction");
		assertEquals("West", Formulas.direction(1000, 0, 0, 10), "Incorrect direction");
		
		assertEquals("NorthWest", Formulas.direction(1, 0, 0, 1), "Incorrect direction");
		
		assertThrows(IllegalArgumentException.class, () -> Formulas.direction(0,0,0,0));
	}
}
