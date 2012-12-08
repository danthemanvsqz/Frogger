// Lilypad Class

package frogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Lilypad extends FixedObject implements IDrawable, ICollider {
	private String height;
	private boolean state;
	private AffineTransform myAT;
	
	public final static int RED = 0;
	public final static int GREEN = 128;
	public final static int BLUE = 128;
	public final static int WIDTH = 100;
	public final static int HEIGHT = 40;
	
	/**
	 * Constructor
	 * @param x location
	 * @param y location
	 */
	public Lilypad(double x, double y) {
		super(x,y, WIDTH, HEIGHT, RED, GREEN, BLUE);
		state = false;
		
		// Set the height
		Random r = new Random();
		height = r.nextBoolean() ? "tail" : "short";		
		myAT = new AffineTransform();
		myAT.translate(Math.round(x - GameWorld.LANE_SIZE / 2.0), 
				Math.round(y - GameWorld.LANE_SIZE / 2.0));
	}
	
	/**
	 * @return string describing attributes
	 */
	public String toString() {
		return "Lilypad: " + super.toString() + " height=" + getHeight() + " Vacancy=" + isEmpty();
	}
	
	
	/**
	 * 
	 * @return true if empty otherwise false
	 */
	public boolean isEmpty() {
		return !state;
	}
	
	
	/**
	 * 
	 * @return height
	 */
	public String getHeight() {
		return height;
	}
	
	
	/**
	 * 
	 * @param s state of lilypad
	 */
	public void setState( boolean s ) {
		state = s;
	}

	@Override
	public void setY(double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setX(double x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColor(int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(getMyColor());
		AffineTransform saveAT = g2d.getTransform();
		
		g2d.transform(myAT);
		
		g2d.drawOval(0, 0, WIDTH, HEIGHT);
		g2d.setTransform(saveAT);
	}

	@Override
	public boolean collidesWith(ICollider otherObject) {
		boolean result = false;
		
		double rightEdge = this.getX() + this.getShapeWidth() / 2.0;
		double leftEdge = this.getX() - this.getShapeWidth() / 2.0;
		double oRight = ((GameObject) otherObject).getX() + ((GameObject) otherObject).getShapeWidth() / 2.0;
		double oLeft = ((GameObject) otherObject).getX() - ((GameObject) otherObject).getShapeWidth() / 2.0;
		
		double topEdge = this.getY() + this.getShapeHeight() / 2.0;
		double bottomEdge = this.getY() - this.getShapeHeight() / 2.0;
		double oTop = ((GameObject) otherObject).getY() + ((GameObject) otherObject).getShapeHeight() / 2.0;
		double oBottom = ((GameObject) otherObject).getY() - ((GameObject) otherObject).getShapeHeight() / 2.0;
		
		if( !((rightEdge > oLeft && leftEdge < oRight) || topEdge < oBottom || bottomEdge > oTop) ) {
			result = true;
		}
		
		return result;
	}

	@Override
	public void handleCollision(ICollider otherObject) {
		if(!this.isEmpty()) {
			if(otherObject instanceof Frog) {
				if(((Frog) otherObject).getState()) {
					return;
				}
				else {
					((Frog) otherObject).setDeletion(true);
				}
			}
		}
		else if(otherObject instanceof Frog) {
			this.setState(true);
		}
				
	}
	
	
}
