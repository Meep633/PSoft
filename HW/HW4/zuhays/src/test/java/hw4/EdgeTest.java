package hw4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EdgeTest {

	@Test
	void testConstructor() {
		new Edge<String, String>("node","label");
		new Edge<Integer, Integer>(1,1);
		new Edge<String, Number>("node",1.0);
		
		assertThrows(NullPointerException.class, () -> new Edge<String, String>(null, "label"));
		assertThrows(NullPointerException.class, () -> new Edge<Integer, Integer>(null, null));
		assertThrows(NullPointerException.class, () -> new Edge<String, Number>("node", null));
	}

	@Test
	void testCopyConstructor() {
		Edge<String, String> e = new Edge<String, String>("node", "label");
		new Edge<String, String>(e);
		
		Edge<String, String> e2 = null;
		assertThrows(NullPointerException.class, () -> new Edge<String, String>(e2));
	}

	@Test
	void testChild() {
		Edge<String, String> e1 = new Edge<String, String>("node", "label");
		assertEquals("node", e1.child(), "Edge doesn't have correct child node");
		Edge<Number, String> e2 = new Edge<Number, String>(1, "label");
		assertEquals(1, e2.child(), "Edge doesn't have correct outgoing node");
	}

	@Test
	void testLabel() {
		Edge<String, String> e1 = new Edge<String, String>("node", "label");
		assertEquals("label", e1.label(), "Edge doesn't have correct label");
		Edge<String, Number> e2 = new Edge<String, Number>("node", 1.0);
		assertEquals(1.0, e2.label(), "Edge doesn't have correct label");
	}

	@Test
	void testEquals() {
		Edge<String, String> e1 = new Edge<String, String>("node", "label");
		assertTrue(e1.equals(e1), "Edge not equal to itself");
		Edge<String, String> e2 = new Edge<String, String>("node", "label");
		assertTrue(e1.equals(e2), "Edge not equal to another edge with same child and label");
		Edge<String, String> e3 = new Edge<String, String>("n", "l");
		assertFalse(e1.equals(e3), "Edge equal to another edge with different child and label");
		Edge<String, Number> e4 = new Edge<String, Number>("node", 1);
		Edge<String, Integer> e5 = new Edge<String, Integer>("node", 1);
		assertTrue(e4.equals(e5), "Edge not equal to another edge with same child and label");
		Object o = new Object();
		assertFalse(e1.equals(o), "Edge equal to object that isn't an edge");
	}
}
