// Frog Class

package frogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Frog extends Moveable 
				implements IMove, IHop, IDrawable, ICollider {
	private boolean state;
	private boolean safety;
	private AffineTransform myAT;
	
	public final static int RED = 0;
	public final static int GREEN = 255;
	public final static int BLUE = 0;
	public final static int WIDTH = 40;
	public final static int HEIGHT = 40;
	
	
	/**
	 * Constructor
	 * @param x location
	 * @param y location 
	 * @param s speed
	 * @param d direction
	 */
	public Frog( double x, double y, double s, String d ) {
		super(x,y, WIDTH, HEIGHT, RED,GREEN,BLUE,s,d);
		state = false;
		setSafety(true);
		myAT = new AffineTransform();
		myAT.translate(Math.round(x - WIDTH / 2.0), 
								Math.round(y - HEIGHT / 2.0));
	}
	
	
	/**
	 * Moves Frog based on current speed and direction
	 */
	public void move(double t) {
		if( getSpeed() == 0 )
			return;
		if( (getDirection().toLowerCase()).compareTo("east") == 0 ) {
			setX( getX() + t * getSpeed() );
			myAT.translate(t * getSpeed() , 0);
		}
		else {
			setX( getX() - t * getSpeed() );	
			myAT.translate(t * getSpeed() * -1, 0);
		}
	}
	
	
	/**
	 * @return string describing attributes
	 */
	public String toString() {
		return "Frogs: " + super.toString() + " On Lilypad=" + getState();
	}
	
	
	/**
	 * @param d direction
	 * hopes frog in given direction and updates attributes
	 */
	public void hop(String d) {
		setSpeed( 0 );
		setDirection( null );
		char c = (d.toLowerCase()).charAt(0);
		switch( c ) {
			case 'e':
				setX( getX() + GameWorld.LANE_SIZE  );
				myAT.translate(GameWorld.LANE_SIZE, 0);
				break;
			case 'w':
				setX( getX() - GameWorld.LANE_SIZE );
				myAT.translate(GameWorld.LANE_SIZE * -1, 0);
				break;
			case 'n':
				setY( getY() + GameWorld.LANE_SIZE  );
				myAT.translate(0, GameWorld.LANE_SIZE);
				break;
			case 's':
				setY( getY() - GameWorld.LANE_SIZE);
				myAT.translate(0, GameWorld.LANE_SIZE * -1);
				break;
			default:
				System.out.println("hop(): Invalid Direction");
				System.exit(0);
		}
	}
	
	
	/**
	 * 
	 * @return Frog's state i.e. on lilypad or active
	 */
	public boolean getState() {
		return state;
	}
	
	
	/**
	 * 
	 * @param s Frog's state
	 */
	public void setState( boolean s ) {
		state = s;
	}



	@Override
	public void setColor(int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(getMyColor());
		AffineTransform saveAT = g2d.getTransform();
		g2d.transform(myAT);
		//g2d.drawOval((int) (getX() - WIDTH / 2.0), (int) (getY() - HEIGHT / 2.0), WIDTH, HEIGHT);	
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
		
		if( otherObject instanceof Lilypad || otherObject instanceof Floater) {
			if( (rightEdge < oRight && leftEdge > oLeft) && !(topEdge < oBottom || bottomEdge > oTop) ) {
				result = true;
			}
		}
		else if( !(rightEdge < oLeft || leftEdge > oRight || topEdge < oBottom || bottomEdge > oTop) ) {
				result = true;
		}
		
		return result;
	}


	@Override
	public void handleCollision(ICollider otherObject) {
		if(!this.getState()) {
			if( otherObject instanceof Lilypad) {
				if(((Lilypad) otherObject).isEmpty()) {
					this.setState(true);
					this.setSafety(true);
				}
				else {
					this.setDeletion(true);
				}
			}
			else if( otherObject instanceof Floater ) {
				this.setSpeed(((Floater) otherObject).getSpeed() );
				this.setDirection(((Floater) otherObject).getDirection() );
				this.setSafety(true);
			}
			else {
				this.setDeletion(true);	
			}
		}
	}


	/**
	 * @return the safety
	 */
	public boolean isSafety() {
		return safety;
	}


	/**
	 * @param safety the safety to set
	 */
	public void setSafety(boolean safety) {
		this.safety = safety;
	}
	
	
}
