// Vehicle Class

package frogger;


public abstract class Vehicle extends Moveable implements IMove {
	private int vin;
	private static int nextAvailableVin = 1;
	
	
	/**
	 * Constructor
	 * @param x = x Coordinate for object
	 * @param y = y Coordinate
	 * @param s = Vehicle speed
	 * @param d = direction
	 */
	public Vehicle(double x, double y, double w, double h, int r, int g, int b, double s, String d) {
		super(x,y,w,h,r,g,b,s,d);
		vin = nextAvailableVin++;
	}
	
	
	
	
	
	/**
	 * @return the vin#
	 */
	public int getVin() {
		return vin;
	}
	
	
	/**
	 * (non-Javadoc)
	 * @see a1.gameobjects.Moveable#toString()
	 */
	public String toString() {
		return super.toString() + " Vin=" + getVin();
	}
}
