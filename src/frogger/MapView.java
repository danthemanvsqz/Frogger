package frogger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class MapView extends JPanel implements IObserver, MouseMotionListener, MouseListener, MouseWheelListener {
	public static final int X_MIN = 0;
	public static final int X_MAX = 1000;
	public static final int Y_MIN = 0;
	public static final int Y_MAX = 500;
	
	private GameWorld gw;
	private boolean paused;
	private AffineTransform worldToND, ndToScreen, theVTM;
	private double windowLeft, windowRight, windowTop, windowBottom;
	private Point2D mousePosition;

	public MapView( IObservable obs ) {
		obs.addObserver(this);
		setBorder(new EtchedBorder());
		setBackground(Color.black);
		//setBackground(new Color(255,0,100));
		gw = (GameWorld) obs;
		setPaused(false);
		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		windowLeft = GameWorld.X_MIN;
		windowRight = GameWorld.X_MAX;
		windowTop = GameWorld.Y_MAX;
		windowBottom = GameWorld.Y_MIN;		
		try {
			mousePosition = this.getMousePosition();
		}
		catch(HeadlessException he) {
			System.out.println(he);
		}
	}
	
	public void update( IGameWorld g, Object arg ) {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform saveAT = g2d.getTransform();
		
		worldToND = buildWorldToNDXform(windowRight, windowTop, windowLeft, windowBottom);
		ndToScreen = buildNDToScreenXform(this.getWidth(),this.getHeight());
		theVTM = (AffineTransform) ndToScreen.clone();
		theVTM.concatenate(worldToND);
		g2d.transform(theVTM);
		
		Iterator<GameObject> i = gw.getIterator();
		while( i.hasNext() ) {
			((IDrawable) i.next()).draw(g2d);;
			
		}
		
		g2d.setTransform(saveAT);
	}
	
	public void zoomIn() {
		double h = windowTop -windowBottom;
		double w = windowRight - windowLeft;
		windowLeft += w*0.05;
		windowRight -= w*0.05;
		windowTop -= h*0.05;
		windowBottom += h*0.05;
		this.repaint();
	}

	public void zoomOut() {
		double h = windowTop -windowBottom;
		double w = windowRight - windowLeft;
		windowLeft -= w*0.05;
		windowRight += w*0.05;
		windowTop += h*0.05;
		windowBottom -= h*0.05;
		this.repaint();
	}
	
	private AffineTransform buildWorldToNDXform(double xMax, double yMax,
			double xMin, double yMin) {
		
		AffineTransform myAT = new AffineTransform();
		myAT.scale(1/xMax, 1/yMax);
		myAT.translate(-xMin, -yMin);
		return myAT;
	}

	private AffineTransform buildNDToScreenXform(int width, int height) {
		AffineTransform myAT = new AffineTransform();
		myAT.translate(0, height);
		myAT.scale(width, -height);
		return myAT;
	}

	

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!isPaused()){
			e.consume();
			return;
		}
		
		AffineTransform inverse;
		try{
			inverse = this.theVTM.createInverse();
		
			Point p = e.getPoint();
			Point2D p2d = inverse.transform(p, null);
		
			Iterator<GameObject> i = gw.getIterator();
			GameObject go;
			while( i.hasNext() ) {
				go = i.next();
				if(go instanceof ISelectable) {
					if(e.isControlDown()) {
						if(((ISelectable) go).contains(p2d)) {
							((ISelectable) go).setSelected(true);
						}
					}
					else { 
						if(((ISelectable) go).contains(p2d)) {
							((ISelectable) go).setSelected(true);
						}
						else {
							((ISelectable) go).setSelected(false);
						}
					}
				}
			}
			this.repaint();
		}
		catch (NoninvertibleTransformException e1) {
			System.out.println("Create Inverse Failure");
			System.exit(0);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the isPaused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param isPaused the isPaused to set
	 */
	public void setPaused(boolean isPaused) {
		this.paused = isPaused;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if( e.getWheelRotation() < 0 ) {
			this.zoomIn();
		}
		else {
			this.zoomOut();
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		Point2D oldMousePosition;
		AffineTransform inverse;
		double x,y;
		try{
			inverse = this.theVTM.createInverse();
			oldMousePosition = mousePosition;
			mousePosition = inverse.transform(e.getPoint(), null);
			if(e.isShiftDown()) {
			
					if( mousePosition != null && oldMousePosition != null ) {
						x = oldMousePosition.getX() - mousePosition.getX();
						y = oldMousePosition.getY() - mousePosition.getY();
						windowLeft += x * 0.25;
						windowRight += x * 0.25;
						windowTop += y * 0.25;
						windowBottom += y * 0.25;
						this.repaint();
					}
			}
		}
		catch (NoninvertibleTransformException e1) {
			System.out.println("Create Inverse Failure");
			System.exit(0);
		}
	}
}
