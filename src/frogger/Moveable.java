// Moveable Class

package frogger;

public abstract class Moveable extends GameObject {
	private double speed;
	private String direction;
	
	/**
	 * Contructor
	 * @param x location
	 * @param y location
	 * @param s speed
	 * @param d direction
	 */
	public Moveable(double x, double y, double w, double h, int r, int g, int b, double s, String d) {
		super(x,y,w,h,r,g,b);
		speed = s;
		direction = d;
	}
	
	public Moveable(double x, double y, double w, double h, int r, int g, int b) {
		super(x,y,w,h,r,g,b);
	}
	
	
	/**
	 * 
	 * @return speed
 	 */
	public double getSpeed() {
		return speed;
	}
	
	
	/**
	 * 
	 * @param s speed to set
	 */
	public void setSpeed( double s ) {
		speed = s;
	}
	
	
	/**
	 * 
	 * @return direction
	 */
	public String getDirection() {
		return direction;
	}
	
	
	/**
	 * 
	 * @param d direction to set
	 */
	public void setDirection( String d ) {
		direction = d;
	}
	
	
	/**
	 * @return string describing attributes
	 */
	public String toString() {
		return super.toString() + " Speed=" + getSpeed() + " Direction=" + getDirection();
	}
}
