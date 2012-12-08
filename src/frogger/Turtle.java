// Turtle Class

package frogger;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Random;

public class Turtle extends Floater 
			implements IMove, IChangeColor, IDrawable, ICollider, ISelectable {
	private String size;
	private boolean selected;
	private AffineTransform myAT;
	
	public final double MAX_BOUYANCY = 20;
	public final double MIN_BOUYANCY = 15;
	public final double CHANGE_COLOR = 10;
	public final static int WIDTH = 80;
	public final static int HEIGHT = 40;
	public final static int RED = 0;
	public final static int GREEN = 255;
	public final static int BLUE = 0;
	
	
	/**
	 * Constructor
	 * @param x location
	 * @param y location
	 * @param s speed
	 * @param d direction
	 */
	public Turtle(double x, double y, double s, String d) {
		super(x,y, WIDTH, HEIGHT, RED, GREEN, BLUE,s,d);
		Random r = new Random();
		setBouyancy(r.nextDouble() * (MAX_BOUYANCY - MIN_BOUYANCY) + MIN_BOUYANCY );
		size = r.nextBoolean() ? "large" : "small";
		selected = false;
		myAT = new AffineTransform();
		myAT.translate(Math.round(x - WIDTH / 2.0), 
								Math.round(y - HEIGHT / 2.0));
	}
	
	
	/**
	 * @param flag to change color
	 */
	public void updateColor( Boolean t ) {
		if( t ) 
			setColor(0, 255, 0);
		else
			setColor(255, 0, 0);
	}
	
	
	/**
	 * 
	 * @return size
	 */
	public String getSize() {
		return size;
	}
	
	
	/**
	 * @return a string describing the objects attributes
	 */
	public String toString() {
		return "Turtle: " + super.toString() + " Size=" + getSize();
	}
	
	
	/**
	 *  Moves the Turtle based on it's speed and direction, updates bouyancy & color
	 */
	public void move(double t) {
		setX( getX() + t * getSpeed() );
		myAT.translate(t*getSpeed(), 0);
		if( getBouyancy() > 0 ) {
			setBouyancy( getBouyancy() - t );
			if( getBouyancy() <= CHANGE_COLOR )
				updateColor(false);
		}
		else {
			setBouyancy(MAX_BOUYANCY);
			updateColor(true);
		}
	}


	@Override
	public void setY(double y) {
		// TODO Auto-generated method stub
		
	}	
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(getMyColor());
		AffineTransform saveAT = g2d.getTransform();
		
		g2d.transform(myAT);
		if(isSelected()) {
			//g2d.fillRect((int) (getX() - WIDTH /2.0 ), (int) (getY() - HEIGHT / 2.0), WIDTH, HEIGHT);
			g2d.fillOval(0, 0, WIDTH, HEIGHT);
		}
		else {
			//g2d.drawRect((int) (getX() - WIDTH /2.0 ), (int) (getY() - HEIGHT / 2.0), WIDTH, HEIGHT);
			g2d.drawOval(0, 0, WIDTH, HEIGHT);
		}
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
	
	@Override
	public void setSelected(boolean yesNo) {
		this.selected = yesNo;		
	}


	@Override
	public boolean isSelected() {
		return this.selected;
	}


	@Override
	public boolean contains(Point2D p) {
		double rightEdge = this.getX() + this.getShapeWidth() / 2.0;
		double leftEdge = this.getX() - this.getShapeWidth() / 2.0;
		double topEdge = this.getY() + this.getShapeHeight() / 2.0;
		double bottomEdge = this.getY() - this.getShapeHeight() / 2.0;
		return !(rightEdge < p.getX() || leftEdge > p.getX() || topEdge < p.getY() || bottomEdge > p.getY());
	}
}
