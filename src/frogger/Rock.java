// Rock Class

package frogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Rock extends FixedObject implements IDrawable, ICollider {
	
	private String size;
	private AffineTransform myTranslation;
	
	public final static int RED = 128;
	public final static int GREEN = 128;
	public final static int BLUE = 64;
	public final static int WIDTH = 40;
	public final static int HEIGHT = 40;
	
	/**
	 * Contructor
	 * @param x location
	 * @param y location
	 */
	public Rock(double x, double y ) {
		super(x,y,WIDTH, HEIGHT, RED, GREEN, BLUE);
		Random r = new Random();
		size = r.nextBoolean() ? "small" : "large";
		myTranslation = new AffineTransform();
		myTranslation.translate(Math.round(x - WIDTH / 2.0), 
								Math.round(y - HEIGHT / 2.0));
		
	}
	
	//@override
	/**
	 * @param string describing attributes
	 */
	public String toString() {
		return "Rock: " + super.toString() + " Size=" + getSize();
	}	
	
	
	/**
	 * 
	 * @return size
	 */
	public String getSize() {
		return size;
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
		AffineTransform saveAT = g2d.getTransform();
		
		
		
		
		g2d.transform(myTranslation);
		g2d.setColor(getMyColor());
				
		g2d.drawRect(0, 0, WIDTH, HEIGHT);
		//g2d.drawRect((int)Math.round(getX() - WIDTH / 2.0),(int) Math.round(getY() - HEIGHT / 2.0), WIDTH, HEIGHT);
		
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
		
		if( !(rightEdge < oLeft || leftEdge > oRight || topEdge < oBottom || bottomEdge > oTop) ) {
			result = true;
		}
		
		return result;
	}

	@Override
	public void handleCollision(ICollider otherObject) {
		// TODO Auto-generated method stub
		
	}
	
	
}
