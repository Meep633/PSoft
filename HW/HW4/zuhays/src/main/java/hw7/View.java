package hw7;

/**
 * View is a view / observer of a model (based on the Observer / MVC design pattern).
 */
public interface View {
	
	// View isn't an ADT
	
	/**
	 * Update view output
	 * @param data Updated data from model
	 * @requires none
	 * @modifies this.data
	 * @effects Update this.data with given data if applicable
	 *          Ex: If view prints time and time data was updated, view should update.
	 *              If price data was updated, then view shouldn't update.
	 * @throws NullPointerException if data == null
	 * @returns none
	 */
	public void update(Object data);
}
