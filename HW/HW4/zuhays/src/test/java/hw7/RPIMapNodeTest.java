package hw7;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RPIMapNodeTest {

	@Test
	void testEmptyConstructor() {
		new RPIMapNode("name", 0, 0, 0);
		new RPIMapNode("", 100, 100, 100);
		new RPIMapNode("building", -50, -50, -50);
		assertThrows(NullPointerException.class, () -> new RPIMapNode(null, 1, 1, 1));
	}
	
	@Test
	void testCopyConstructor() {
		RPIMapNode n = new RPIMapNode(new RPIMapNode("name", 0, 0, 0));
		assertTrue(n.getName().equals("name"), "Unexpected name");
		assertTrue(n.getId() == 0, "Unexpected ID");
		assertTrue(n.getX() == 0, "Unexpected x-coord");
		assertTrue(n.getY() == 0, "Unexpected y-coord");
		assertThrows(NullPointerException.class, () -> new RPIMapNode(null));
	}

	@Test
	void testGetName() {
		RPIMapNode r = new RPIMapNode("name", 0, 0, 0);
		assertTrue(r.getName().equals("name"), "Unexpected name");
		r = new RPIMapNode("", 100, 100, 100);
		assertTrue(r.getName().equals(""), "Unexpected name");
		r = new RPIMapNode("building", -50, -50, -50);
		assertTrue(r.getName().equals("building"), "Unexpected name");
	}

	@Test
	void testGetId() {
		RPIMapNode r = new RPIMapNode("name", 0, 0, 0);
		assertTrue(r.getId() == 0, "Unexpected ID");
		r = new RPIMapNode("", 100, 100, 100);
		assertTrue(r.getId() == 100, "Unexpected ID");
		r = new RPIMapNode("building", -50, -50, -50);
		assertTrue(r.getId() == -50, "Unexpected ID");
	}

	@Test
	void testGetX() {
		RPIMapNode r = new RPIMapNode("name", 0, 0, 0);
		assertTrue(r.getX() == 0, "Unexpected x-coord");
		r = new RPIMapNode("", 100, 100, 100);
		assertTrue(r.getX() == 100, "Unexpected x-coord");
		r = new RPIMapNode("building", -50, -50, -50);
		assertTrue(r.getX() == -50, "Unexpected x-coord");
	}

	@Test
	void testGetY() {
		RPIMapNode r = new RPIMapNode("name", 0, 0, 0);
		assertTrue(r.getY() == 0, "Unexpected y-coord");
		r = new RPIMapNode("", 100, 100, 100);
		assertTrue(r.getY() == 100, "Unexpected y-coord");
		r = new RPIMapNode("building", -50, -50, -50);
		assertTrue(r.getY() == -50, "Unexpected y-coord");
	}

	@Test
	void testIsIntersection() {
		RPIMapNode r = new RPIMapNode("name", 0, 0, 0);
		assertFalse(r.isIntersection(), "Node shouldn't be interesection");
		r = new RPIMapNode("", 100, 100, 100);
		assertTrue(r.isIntersection(), "Node should be intersection");
		r = new RPIMapNode("building", -50, -50, -50);
		assertFalse(r.isIntersection(), "Node shouldn't be interesection");
	}

	@Test
	void testEqualsObject() {
		RPIMapNode r1 = new RPIMapNode("1", 0, 0, 0);
		RPIMapNode r2 = new RPIMapNode("2", 0, 1, 1);
		RPIMapNode r3 = new RPIMapNode("3", 0, 2, 2);
		RPIMapNode r4 = new RPIMapNode("4", 1, 3, 3);
		assertTrue(r1.equals(r2), "Nodes should be equal");
		assertFalse(r1.equals(r4), "Nodes shouldn't be equal");
		assertFalse(r1.equals(new Object()), "Node shouldn't be equal to a different object");
		assertTrue(r1.equals(r1), "equals isn't reflexive");
		assertTrue(r1.equals(r2) && r2.equals(r1), "equals isn't symmetric");
		assertTrue(!(r1.equals(r2) && r2.equals(r3)) || r1.equals(r3), "equals isn't transitive");
	}
	
	@Test
	void testHashCode() {
		RPIMapNode r = new RPIMapNode("name", 0, 0, 0);
		assertTrue(r.hashCode() == 0, "Unexpected hashcode");
		r = new RPIMapNode("", 100, 100, 100);
		assertTrue(r.hashCode() == 100, "Unexpected hashcode");
		r = new RPIMapNode("building", -50, -50, -50);
		assertTrue(r.hashCode() == -50, "Unexpected hashcode");
	}

}
