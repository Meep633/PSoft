	/**
	 * This is part of HW0: Environment Setup and Java Introduction.
	 */
	package hw0;
	
	/**
	 * Fibonacci calculates the <var>n</var>th term in the Fibonacci sequence.
	 *
	 * The first two terms of the Fibonacci sequence are 0 and 1,
	 * and each subsequent term is the sum of the previous two terms.
	 *
	 */
	public class Fibonacci {
	
	    /**
	     * Calculates the desired term in the Fibonacci sequence.
	     *
	     * @param n the index of the desired term; the first index of the sequence is 0
	     * @return the <var>n</var>th term in the Fibonacci sequence
	     * @throws IllegalArgumentException if <code>n</code> is not a nonnegative number
	     */
	    public long getFibTerm(int n) {
	        if (n < 0) {
	            throw new IllegalArgumentException(n + " is negative");
	        } else {
	            return fibHelper(2, n, 1, 0); //1st term = 1, 0th term = 0 (base cases)
	        }
	    }
	    /**
	     * Helper function to more efficiently calculate a Fibonacci term recursively.
	     * 
	     * @param start index of the current term being calculated
	     * @param end index of the desired term
	     * @param prev1 the term before the current term
	     * @param prev2 the term before prev1
	     * @return the nth term in the Fibonacci sequence
	     */
	    public long fibHelper(int start, int end, long prev1, long prev2) {
	    	//prev1 + prev2 is the (start) term
	    	//prev1 is the (start-1) term
	    	//prev2 is the (start-2) term
	    	if (end < 2) {
	    		return end;
	    	} else if (start == end) {
	    		return prev1 + prev2;
	    	}
	    	return fibHelper(start+1, end, prev1 + prev2, prev1);
	    }
	    //2147483647
	    //1548008755920
	}
