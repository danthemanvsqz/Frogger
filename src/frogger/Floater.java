//Floater Class

package frogger;

public abstract class Floater extends Moveable {
	private double bouyancy;
	
	
	/**
	 * Contructor
	 * @param x location
	 * @param y location
	 * @param s speed
	 * @param d direction
	 */
	public Floater(double x,double y, double w, double h, int r, int g, int b, double s, String d) {
		super(x,y,w,h,r,g,b,s,d);
	}
	
	
	/**
	 * 
	 * @return bouyancy
	 */
	public double getBouyancy() {
		return bouyancy;
	}
	
	
	/**
	 * 
	 * @param b bouyancy to set
	 */
	protected void setBouyancy( double b ) {
		bouyancy = b;
	}
	
	
	/**
	 * @return string describing attributes
	 */
	public String toString() {
		return super.toString() + " Bouyancy=" + getBouyancy();
	}
}
