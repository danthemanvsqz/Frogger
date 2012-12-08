package frogger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Bird extends Moveable implements IMove, IDrawable, ICollider {
	public static final int RED = 255;
	public static final int GREEN = 255;
	public static final int BLUE = 0;
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	public static final double SPEED = 50;
	public static final double MAX_WING_OFFSET = .5;
	
	private int iDirection;
	private AffineTransform myTranslation;
	private AffineTransform myScale;
	private AffineTransform myRotation;
	private double wingOffset = 0;
	private double wingIncrement = .05;
	private BirdBody myBody ;
	private BirdWing[] myWings;
	private BirdLeg[] myLegs;
		
	public Bird(double x, double y) {
		super(x, y, WIDTH, HEIGHT, RED, GREEN, BLUE);
		this.setSpeed(SPEED);
		Random r = new Random();
		iDirection = r.nextInt(180) + 90;
		this.setDirection(((Integer) iDirection).toString());	
		myTranslation = new AffineTransform();
		myScale = new AffineTransform();
		myRotation = new AffineTransform();
		
		myTranslation = new AffineTransform();
		myTranslation.translate(Math.round(x - WIDTH / 2.0), 
								Math.round(y - HEIGHT / 2.0));
		myBody = new BirdBody();
		myBody.scale(WIDTH / 2.0, HEIGHT / 2.0);
		
		myWings = new BirdWing[2];
		myLegs = new BirdLeg[2];
		for(int i = 0; i < 2; i++){
			myWings[i] = new BirdWing();
			myLegs[i] = new BirdLeg();
			myWings[i].translate(0, 7);
			myWings[i].translate(0, 5);
			myWings[i].scale(WIDTH / 5.0 , HEIGHT / 5.0);
			myLegs[i].scale(WIDTH / 5.0, HEIGHT / 5.0);
			myWings[i].rotate(i * 180 + 90);
			myLegs[i].rotate(i * -45 + 22.5);
		}
		
		
	}
	
	public void update() {
		//myTranslation.translate(1, 1);
		//myRotation.rotate(Math.toRadians(1));
		wingOffset += wingIncrement;
		for( BirdWing w : myWings ) {
			w.translate(0, wingOffset);
		}
		
		if( Math.abs(wingOffset) >= MAX_WING_OFFSET ) {
			wingIncrement *= -1;
		}
	}

	@Override
	public void move(double t) {
		this.setX(this.getX() + this.getSpeed() * t * Math.sin(Math.toRadians(iDirection)));
		this.setY(this.getY() + this.getSpeed() * t * Math.cos(Math.toRadians(iDirection)));
		
		myTranslation.translate(this.getSpeed() * t * Math.sin(Math.toRadians(iDirection)),
				this.getSpeed() * t * Math.cos(Math.toRadians(iDirection)));
		this.update();
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(this.getMyColor());
		AffineTransform saveAT = g2d.getTransform();
		g2d.transform(myTranslation);
		g2d.transform(myRotation);
		g2d.transform(myScale);
		//g2d.drawOval((int) (getX() - WIDTH/2), (int) (getY() - HEIGHT/2), WIDTH, HEIGHT);
		//g2d.drawOval(0, 0, WIDTH, HEIGHT);
		myBody.draw(g2d);
		for(BirdWing w : myWings) {
			w.draw(g2d);
		}
		
		for( BirdLeg l : myLegs ) {
			l.draw(g2d);
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
	
	private class BirdBody {
		public static final int RED = Bird.RED;
		public static final int GREEN = Bird.GREEN;
		public static final int BLUE = Bird.BLUE;
		public static final int RADIUS = 1;
		
		private AffineTransform myTranslation;
		private AffineTransform myScale;
		private AffineTransform myRotation;
		private Color myColor;
		
		public BirdBody() {
			myTranslation = new AffineTransform();
			myScale = new AffineTransform();
			myRotation = new AffineTransform();
			myColor = new Color( RED, GREEN, BLUE );
			
		}
		
		public void draw(Graphics2D g2d) {
			g2d.setColor(getMyColor());
			AffineTransform saveAT = g2d.getTransform();
			g2d.transform(myTranslation);
            g2d.transform(myRotation);
            g2d.transform(myScale);
            
            g2d.setColor(myColor);
            Point upperleft = new java.awt.Point(-RADIUS, -RADIUS);
            g2d.fillOval(upperleft.x,upperleft.y, RADIUS * 2, RADIUS * 2);
            
            g2d.setTransform(saveAT);
		}
		
		@SuppressWarnings("unused")
		public void translate(double tx, double ty) {
			myTranslation.translate(tx, ty);
		}
		
		@SuppressWarnings("unused")
		public void rotate(double angdeg) {
			myRotation.rotate(Math.toRadians(angdeg));
		}
		
		public void scale(double sx, double sy) {
			myScale.scale(sx, sy);
		}
	}
	
	private class BirdWing {
		public static final int RED = Bird.RED;
		public static final int GREEN = Bird.GREEN;
		public static final int BLUE = Bird.BLUE;
		
		private AffineTransform myTranslation;
		private AffineTransform myScale;
		private AffineTransform myRotation;
		private Color myColor;
		private Point top, bottomLeft, bottomRight;
		
		
		public BirdWing() {
			myTranslation = new AffineTransform();
			myScale = new AffineTransform();
			myRotation = new AffineTransform();
			myColor = new Color( RED, GREEN, BLUE );
			top = new Point(0,2);
	        bottomLeft = new Point(-1, -2);
	        bottomRight = new Point(1, 2);			
		}
		
		public void draw(Graphics2D g2d) {
			g2d.setColor(getMyColor());
			AffineTransform saveAT = g2d.getTransform();
			g2d.transform(myRotation);
            g2d.transform(myScale);
            g2d.transform(myTranslation);
            
            g2d.setColor(myColor);
          
            int[] xPts = new int[] {top.x, bottomLeft.x, bottomRight.y};
            int[] yPts = new int[] {top.y, bottomLeft.y, bottomRight.x};
            g2d.fillPolygon(xPts, yPts, 3);            
            
            g2d.setTransform(saveAT);
		}
		
		public void translate(double tx, double ty) {
			myTranslation.translate(tx, ty);
		}
		
		public void rotate(double angdeg) {
			myRotation.rotate(Math.toRadians(angdeg));
		}
		
		public void scale(double sx, double sy) {
			myScale.scale(sx, sy);
		}
	}
	
	private class BirdLeg {
		public static final int RED = Bird.RED;
		public static final int GREEN = Bird.GREEN;
		public static final int BLUE = Bird.BLUE;
	
        private Point top, bottom;
        private Color myColor;
        private AffineTransform myTranslation, myRotation, myScale;
       
        public BirdLeg() {
                top = new Point(0,4);
                bottom = new Point(0,-1);
                myColor = new Color(RED, GREEN, BLUE);
                myTranslation = new AffineTransform();
                myRotation = new AffineTransform();
                myScale = new AffineTransform();
        }
       
        public void rotate(double angdeg) {
                myRotation.rotate(Math.toRadians(angdeg));
        }
        public void scale(double sx, double sy) {
                myScale.scale(sx, sy);
        }
        @SuppressWarnings("unused")
		public void translate(double dx, double dy) {
                myTranslation.translate(dx, dy);
        }
       
        public void draw(Graphics2D g2d) {
                AffineTransform saveAT = g2d.getTransform();
               
                g2d.transform(myRotation);
                g2d.transform(myScale);
                g2d.transform(myTranslation);
               
                g2d.setColor(myColor);
                g2d.drawLine(top.x, top.y, bottom.x, bottom.y);
               
                g2d.setTransform(saveAT);
        }
	}
}
