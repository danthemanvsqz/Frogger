// FixedObject Class

package frogger;

public abstract class FixedObject extends GameObject {
	
	/**
	 * Constructor
	 * @param x location
	 * @param y location
	 */
	public FixedObject(double x, double y, double w, double h, int r, int b, int g) {
		super(x,y,w,h,r,b,g);
	}	
}
