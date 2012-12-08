// GameObject base class 

package frogger;

import java.awt.Color;

public abstract class GameObject {
	private double xLocation;
	private double yLocation;
	private double shapeWidth;
	private double shapeHeight;
	private boolean deletion;
	private Color myColor;
	
	
	/**
	 * Constructor
	 * @param x location
	 * @param y location
	 */
	public GameObject(double x, double y, double w, double h, int r, int g, int b) {
		xLocation = x;
		yLocation = y;
		shapeWidth = w;
		shapeHeight = h;
		setDeletion(false);
		myColor = new Color(r,g,b);
	}
	
	
	/**
	 * 
	 * @param y location to set
	 */
	public void setY( double y ) {
		yLocation = y;
	}
	
	
	/**
	 * 
	 * @param x location to set
	 */
	public void setX( double x ) {
		xLocation = x;
	}
	
	
	/**
	 * 
	 * @return xLocation
	 */
	public double getX() {
		return xLocation;
	}
	
	
	/**
	 * 
	 * @return yLocation
	 */
	public double getY() {
		return yLocation;
	}
	
	
	/**
	 * 
	 * @return string describing Color components
	 */
	public String getColor() {
		if( myColor != null ) {
			return "[" + myColor.getRed() + "," + myColor.getGreen() + "," + myColor.getBlue() + "]";
		}
		return null;
	}
	
	public Color getMyColor() {
		return myColor;
	}
	
	public double getShapeWidth() {
		return shapeWidth;
	}
	
	public double getShapeHeight() {
		return shapeHeight;
	}
	
	/**
	 * 
	 * @param r red intensity
	 * @param g green intensity
	 * @param b blue intensity
	 */
	public void setColor( int r, int g, int b )	{
		Color tColor = new Color(r,g,b);
		myColor = tColor;
	}
	
	
	/**
	 * @return string describing attributes
	 */
	public String toString() {
		return "x=" + getX() + ",y=" + getY() + " Color=" + getColor();
	}


	public boolean isDeletion() {
		return deletion;
	}


	public void setDeletion(boolean deletion) {
		this.deletion = deletion;
	}
}
