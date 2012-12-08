// GameWorld Class hold much of the game logic

package frogger;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;


public class GameWorld implements IGameWorld, IObservable {
	
	// Constants
	public static final double DELAY_IN_MSEC = 50;
	public static final int STARTING_FROGS = 3;
	public static final double X_MIN = 0;
	public static final double X_MAX = 1000;
	public static final double Y_MIN = 0;
	public static final double Y_MAX = 500;
	public static final double LILYPAD_Y_COORDINATE = 475;
	public static final double ROCK_Y_COORDINATE = 275;		
	public static final int LILYPAD_QTY = 5;
	public static final int ROCK_QTY = 4;
	public static final int STARTING_OBJ_QTY = 10;
	public static final int LANE_SIZE = 50;
	public static final double Y_WATER_START = 6 * LANE_SIZE;
	public static final double Y_WATER_END = Y_MAX;
	public static final int MOVE_BONUS = 50;
	public static final String SOUND_DIRECTORY = "." + File.separator + "src" + File.separator + "frogger" + File.separator + "sounds" + File.separator;
	public static final String BACKGROUND_MUSIC_PATH = SOUND_DIRECTORY + "bgmusic16.wav";
	public static final String FROG_HOP_SOUND_PATH = SOUND_DIRECTORY + "arcade_action_06_zo17632.wav";
	public static final String FROG_DIES_SOUND_PATH = SOUND_DIRECTORY + "arcade_alarm_02_pi24412.wav";
	public static final String GAME_OVER_SOUND_PATH = 
			SOUND_DIRECTORY + "sirens_ambulance_fire_truck_police_car_ki63595.wav";
	
	
	// Attributes
	private double elapsedTime;
	private int frogCount;
	private int score;
	private boolean sound;
	private GameObjectCollection theGameObjectCollection;
	private ArrayList<IObserver> myObserverList;
	private Sound backgroundMusic;
	private Sound frogHopSound;
	private Sound frogDiesSound;
	private Sound gameOverSound;
	private int vehicleTicker;
	private int floatTicker;
	private int birdTicker;
	
	
		
	/*
	 * Constructor
	 */
	public GameWorld() {
		System.out.println("GameWorld");
		Random r = new Random();
		setVehicleTicker(0);
		setFloatTicker(0);
		setBirdTicker(0);
		frogCount = STARTING_FROGS - 1;
		sound = true;
		backgroundMusic = new Sound(BACKGROUND_MUSIC_PATH);
		frogHopSound = new Sound(FROG_HOP_SOUND_PATH);
		frogDiesSound = new Sound(FROG_DIES_SOUND_PATH);
		gameOverSound = new Sound(GAME_OVER_SOUND_PATH);
		theGameObjectCollection = new GameObjectCollection();
		myObserverList = new ArrayList<IObserver>();
		
		placeStartingObjects();
		
		double lilypadSpace = X_MAX / ((double) LILYPAD_QTY);  
		for(int xCoord, i = 0; i < LILYPAD_QTY; i++ ) {
			xCoord = (int) (i * lilypadSpace + lilypadSpace / 2.0);
			theGameObjectCollection.add( new Lilypad( (double) xCoord, LILYPAD_Y_COORDINATE ) );
		}
		

		for( int i = 0; i < ROCK_QTY; i++ ) {
			addRock();
		}
		
		Frog f = new Frog(r.nextDouble() * (X_MAX - Frog.WIDTH / 2.0) + Frog.WIDTH / 2.0,
				Y_MIN + LANE_SIZE / 2.0, 0, null);
		theGameObjectCollection.add( f );
		System.out.println(f.toString());
		addBird();
		
		
		backgroundMusic.loop();
	}
	
	private void addBird() {
		Random r = new Random();
		
		theGameObjectCollection.add(new Bird(r.nextDouble() * ( X_MAX - Bird.WIDTH / 2.0 ) + Bird.WIDTH / 2.0,
				GameWorld.Y_MAX - Bird.HEIGHT / 2.0));
	}
	
	private void placeStartingObjects() {
		double x, y, s, rightEdge, leftEdge;
		int lane;
		String d;
		Random r = new Random();
		boolean flag;
		Iterator<GameObject> it;
		GameObject g;
		for( int i = 0; i < STARTING_OBJ_QTY; i++ ) {
			switch( r.nextInt(4) ) {
			case 0:
				do {
					flag = false;
					lane = getVehicleLane(); 
					x = (int) (r.nextDouble() * (X_MAX - Car.WIDTH / 2.0) + Car.WIDTH / 2.0);
					y = getVehicleY( lane );
					s = getVehicleSpeed( lane );
					d = getVehicleDirection( lane );
					it = theGameObjectCollection.getIterator();
					while( it.hasNext() ) {
						g = it.next();
						if( Math.abs(g.getY() - y) < .001 ) {
							rightEdge = g.getX() + g.getShapeWidth()/2;
							leftEdge = g.getX() - g.getShapeWidth()/2;
							if( !(x + Car.WIDTH / 2.0 < leftEdge || x - Car.WIDTH / 2.0 > rightEdge )) {
								flag = true;
								break;
							}							
						}
					}
				} while( flag );
				theGameObjectCollection.add( new Car(x,y,s,d) );				
				break;
			case 1:
				do {
					flag = false;
					lane = getVehicleLane(); 
					x = (int) (r.nextDouble() * (X_MAX - Truck.WIDTH / 2.0) + Truck.WIDTH / 2.0);
					y = getVehicleY( lane );
					s = getVehicleSpeed( lane );
					d = getVehicleDirection( lane );
					it = theGameObjectCollection.getIterator();
					while( it.hasNext() ) {
						g = it.next();
						if( Math.abs(g.getY() - y) < .001 ) {
							rightEdge = g.getX() + g.getShapeWidth()/2;
							leftEdge = g.getX() - g.getShapeWidth()/2;
							if( !(x + Truck.WIDTH / 2.0 < leftEdge || x - Truck.WIDTH / 2.0 > rightEdge )) {
								flag = true;
								break;
							}
						}
					}
				} while( flag );
				theGameObjectCollection.add( new Truck(x,y,s,d) );
				break;
			case 2:				
				do {
					flag = false;
					lane = getFloatChannel();					
					x = (int) (r.nextDouble() * (X_MAX - Log.WIDTH / 2.0) + Log.WIDTH / 2.0);
					y = getFloatY( lane );
					s = getFloatSpeed( lane );
					d = "east";
					it = theGameObjectCollection.getIterator();
					while( it.hasNext() ) {
						g = it.next();
						if( Math.abs(g.getY() - y) < .001 ) {
							rightEdge = g.getX() + g.getShapeWidth()/2;
							leftEdge = g.getX() - g.getShapeWidth()/2;
							if( !(x + Log.WIDTH / 2.0  < leftEdge || x - Log.WIDTH / 2.0 > rightEdge )) {
								flag = true;
								break;
							}
						}
					}
				} while( flag );
				theGameObjectCollection.add( new Log(x,y,s,d) );
				break;
			case 3:
				do {
					flag = false;
					lane = getFloatChannel();					
					x = (int) (r.nextDouble() * (X_MAX - Turtle.WIDTH / 2.0) + Turtle.WIDTH / 2.0);
					y = getFloatY( lane );
					s = getFloatSpeed( lane );
					d = "east";
					it = theGameObjectCollection.getIterator();
					while( it.hasNext() ) {
						g = it.next();
						if( Math.abs(g.getY() - y) < .001 ) {
							rightEdge = g.getX() + g.getShapeWidth()/2;
							leftEdge = g.getX() - g.getShapeWidth()/2;
							if( !(x + Turtle.WIDTH / 2.0 < leftEdge || x - Turtle.WIDTH / 2.0 > rightEdge )) {
								flag = true;
								break;
							}
						}
					}
				} while( flag );
				theGameObjectCollection.add( new Turtle(x,y,s,d) );
				break;
			}
		}
	}
	
	public void addObserver( IObserver o ) {
		myObserverList.add(o);
	}
	
	public void notifyObservers() {
		GameWorldProxy proxy = new GameWorldProxy( this );
		collisionDetection();
		deleteGameObjects();
		for( IObserver o : myObserverList ) {
			o.update((IGameWorld) proxy, null ); 
		}
	}
	
	public void deleteGameObjects() {
		Iterator<GameObject> it = theGameObjectCollection.getIterator();
		GameObject g;
		while(it.hasNext()) {
			g = it.next();
			if(g.isDeletion()) {
				if( g instanceof Frog && !((Frog) g).getState() ) {
					if( frogCount > 0 ) {
						if(sound) {
							frogDiesSound.play();
						}
						addFrog();
					}
					else {
						if(sound) {
							backgroundMusic.stop();
							gameOverSound.play();
						}
						int result = JOptionPane.showConfirmDialog(null,
								"I'm sorry but you're a loser", "Final Score: " + this.getScore(), 
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
						if( result == JOptionPane.OK_OPTION ) {
							System.exit(0);
						}
					}
				}
				theGameObjectCollection.remove(g);
				it = theGameObjectCollection.getIterator();
			}
			else if( g instanceof Frog && !((Frog) g).getState()) {
				if( !((Frog) g).isSafety() ) {
					if( frogCount > 0 ) {
						if(sound) {
							frogDiesSound.play();
						}
						addFrog();
					}
					else {
						if(sound) {
							backgroundMusic.stop();
							gameOverSound.play();
						}
						int result = JOptionPane.showConfirmDialog(null,
								"I'm sorry but you're a loser", "Final Score: " + this.getScore(), 
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
						if( result == JOptionPane.OK_OPTION ) {
							System.exit(0);
						}
					}
					theGameObjectCollection.remove(g);
					it = theGameObjectCollection.getIterator();
				}
			}
		}
	}
	
	public Iterator<GameObject> getIterator() {
		return theGameObjectCollection.getIterator();
	}
	
	
	
	public void removeGameObject(GameObject g) {
		theGameObjectCollection.remove(g);
	}
	
	
	
	/*
	 * Adds a new Rock with random location along river bank
	 */
	public void addRock() {
		Random rand = new Random();
		GameObject g;
		double x, leftEdge, rightEdge;
		boolean flag;
		Iterator<GameObject> i;
		do {
			flag = false;
			x = (int) (rand.nextDouble() * ( X_MAX - Rock.WIDTH / 2.0 ) + Rock.WIDTH / 2.0);
			i = theGameObjectCollection.getIterator();
			while( i.hasNext() ) {
				g = i.next();
				if( g instanceof Rock ) {
					rightEdge = g.getX() + g.getShapeWidth()/2;
					leftEdge = g.getX() - g.getShapeWidth()/2;
					if( !(x + Rock.WIDTH / 2.0 < leftEdge || x - Rock.WIDTH / 2.0 > rightEdge )) {
						flag = true;
						break;
					}
				}
			}
		} while( flag );
		Rock r = new Rock(x,ROCK_Y_COORDINATE);
		theGameObjectCollection.add( r );
		System.out.println(r.toString());
	}
	
	
	/*
	 * Adds a new Car in random traffic lane
	 */
	public void addCar( ) {
		double x, y, s, leftEdge, rightEdge;
		int lane = getVehicleLane(); 
		String d;
		GameObject g;
		boolean flag;
		Iterator<GameObject> i;
		do {
			flag = false;
			x = getVehicleX( lane, Car.WIDTH );
			y = getVehicleY( lane );
			s = getVehicleSpeed( lane );
			d = getVehicleDirection( lane );
			i = theGameObjectCollection.getIterator();
			while( i.hasNext() ) {
				g = i.next();
				if( Math.abs(g.getY() - y) < .001 ) {
					rightEdge = g.getX() + g.getShapeWidth()/2;
					leftEdge = g.getX() - g.getShapeWidth()/2;
					if( !(x + Car.WIDTH / 2.0 < leftEdge || x - Car.WIDTH / 2.0 > rightEdge )) {
						flag = true;
						break;
					}
				}
			}
		} while(flag);
		theGameObjectCollection.add( new Car(x,y,s,d) );
		//notifyObservers();
	}
	
	
	/*
	 * Adds new truck in random traffic lane
	 */
	public void addTruck( ) {
		double x, y, s, leftEdge, rightEdge;
		int lane = getVehicleLane(); 
		String d;
		GameObject g;
		boolean flag;
		Iterator<GameObject> i;
		do {
			flag = false;
			x = getVehicleX( lane, Truck.WIDTH );
			y = getVehicleY( lane );
			s = getVehicleSpeed( lane );
			d = getVehicleDirection( lane );
			i = theGameObjectCollection.getIterator();
			while( i.hasNext() ) {
				g = i.next();
				if( Math.abs(g.getY() - y) < .001) {
					rightEdge = g.getX() + g.getShapeWidth()/2;
					leftEdge = g.getX() - g.getShapeWidth()/2;
					if( !(x + Truck.WIDTH / 2.0 < leftEdge || x - Truck.WIDTH / 2.0 > rightEdge )) {
						flag = true;
						break;
					}
				}
			}
		} while(flag);
		
		theGameObjectCollection.add( new Truck(x,y,s,d) );
		//notifyObservers();
	}
	
	
	/*
	 * Adds new log in random river channel
	 */
	public void addLog( ) {
		double x = X_MIN + Log.WIDTH / 2.0, y, s;
		int channel; 
		String d = "east";
		GameObject g;
		boolean flag;
		Iterator<GameObject> i;
		do {
			flag = false;
			channel = getFloatChannel();
			y = getFloatY( channel );
			s = getFloatSpeed( channel);
			i = theGameObjectCollection.getIterator();
			while( i.hasNext() ) {
				g = i.next();
				if( Math.abs(g.getY() - y) < .001 ) {
					if( x + Log.WIDTH / 2.0 > g.getX() - g.getShapeWidth() / 2.0 ) {
						flag = true;
						break;
					}
				}
			}
		} while(flag);
		theGameObjectCollection.add( new Log(x,y,s,d) );
		//notifyObservers();
	}
	
	
	/*
	 * Adds new turtle in random river channel
	 */
	public void addTurtle( ) {
		double x = X_MIN + Turtle.WIDTH / 2.0, y, s;
		int channel; 
		String d = "east";
		GameObject g;
		boolean flag;
		Iterator<GameObject> i;
		do {
			flag = false;
			channel = getFloatChannel();
			y = getFloatY( channel );
			s = getFloatSpeed( channel);
			i = theGameObjectCollection.getIterator();
			while( i.hasNext() ) {
				g = i.next();
				if( Math.abs(g.getY() - y) < .001 ) {
					if( x + Turtle.WIDTH / 2.0 > g.getX() - g.getShapeWidth() / 2.0 ) {
						flag = true;
						break;
					}
				}
			}
		} while(flag);
		theGameObjectCollection.add( new Turtle(x,y,s,d) );
		//notifyObservers();
	}
	
	public void hopFrogSound() {
		if(sound) frogHopSound.play();
	}
	
	/*
	 * @param direction to hop
	 * Hops frog one unit in direction passed kills frog if out of bounds
	 */
	public void hopFrog(String direction ) {
		char d = (direction.toLowerCase()).charAt(0);
		Frog f = null;
		Iterator<GameObject> i = theGameObjectCollection.getIterator();
		if( frogCount < 0 ) {
			System.out.println("hopFrog(): Error no frogs in game");
			return;
		}
		while( i.hasNext() ){
			GameObject g = i.next();
			if( g instanceof Frog ) {
				if( ((Frog) g).getState() == false) {
					f = (Frog) g;
					f.hop(direction);
					switch( d ) {
						case 'n':
							if( f.getY() + f.getShapeHeight() / 2.0 > GameWorld.Y_WATER_START ) {
								f.setSafety(false);
								setScore(getScore() + 5);
							}
							else {
								f.setSafety(true);
								setScore(getScore() + 5);
							}
							if(sound) {
								frogHopSound.play();
							}
							this.collisionDetection();
							if( f.getState() ) {
								this.addBonusFrog();
							}
							this.notifyObservers();
						return;
						case 's':
							if( f.getY() - f.getShapeHeight() / 2.0 > GameWorld.Y_WATER_START ) {
								if(f.getY() - f.getShapeHeight() / 2.0 < GameWorld.Y_MIN ) {
									f.setDeletion(true);
								}
								else {
									f.setSafety(false);
									setScore(getScore() + 5);
								}
							}
							else {
								f.setSafety(true);
								setScore(getScore() + 5);
							}
							if(sound) {
								frogHopSound.play();
							}
							this.collisionDetection();
							if( f.getState() ) {
								this.addBonusFrog();
							}
							this.notifyObservers();
							return;
						case 'e':
							if( f.getY() - f.getShapeHeight() / 2.0 > GameWorld.Y_WATER_START ) {
								f.setSafety(false);
								setScore(getScore() + 5);								
							}
							else {
								if( f.getX() + f.getShapeWidth() / 2.0 > GameWorld.X_MAX ) {
									f.setDeletion(true);
								}
								else {
									f.setSafety(true);
									setScore(getScore() + 5);
								}								
							}
							if(sound) {
								frogHopSound.play();
							}
							this.collisionDetection();
							if( f.getState() ) {
								this.addBonusFrog();
							}
							this.notifyObservers();
							return;
						case 'w':
							if( f.getY() - f.getShapeHeight() / 2.0 > GameWorld.Y_WATER_START ) {
								f.setSafety(false);
								setScore(getScore() + 5);								
							}
							else {
								if( f.getX() - f.getShapeWidth() / 2.0 < GameWorld.X_MIN ) {
									f.setDeletion(true);
								}
								else {
									f.setSafety(true);
									setScore(getScore() + 5);
								}								
							}
							if(sound) {
								frogHopSound.play();
							}
							this.collisionDetection();
							if( f.getState() ) {
								this.addBonusFrog();
							}
							this.notifyObservers();
							return;
					}
				}
			}
		}	
		System.out.println("Error no active frogs exist");
	}	
	
	
	
	public void addFrog() {
		Random r = new Random();
		Frog f;
		if(getFrogCount() > 0) {
			f = new Frog(r.nextDouble() * (X_MAX - Frog.WIDTH / 2.0) + Frog.WIDTH / 2.0, Y_MIN + LANE_SIZE / 2.0, 0.0, null);
			theGameObjectCollection.add( f );
			frogCount--;
			//notifyObservers();
			System.out.println(f.toString());
		}
		else {
			System.out.println("Error no extra frogs are available");
		}
	}
	
	public void addBonusFrog() {
		Random r = new Random();
		theGameObjectCollection.add( new Frog(r.nextDouble() * (X_MAX - Frog.WIDTH / 2.0) + Frog.WIDTH / 2.0, Y_MIN + LANE_SIZE / 2.0, 0.0, null));		
	}
	
	/**
	 * @return the elapsedTime
	 */
	public double getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * @param elapsedTime the elapsedTime to set
	 */
	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public int setScore(int score) {
		this.score = score;
		return score;
	}
	
	/**
	 * Updates updates elapsedTime and then moves all moveable objects
	 */
	public void clockTick() {
		Iterator<GameObject> i = theGameObjectCollection.getIterator();
		Random r = new Random();
		setVehicleTicker( getVehicleTicker() + 1 );
		setBirdTicker( getBirdTicker() + 1 );
		setFloatTicker( getFloatTicker() + 1 );
				
		setElapsedTime( getElapsedTime() + 1.0 / DELAY_IN_MSEC );
		
		
		while( i.hasNext() ) {
			GameObject g = i.next();
			if( g instanceof Turtle ) {
				if( ((Turtle) g).getBouyancy() < 1) {
					g.setDeletion(true);
					Iterator<GameObject> it = theGameObjectCollection.getIterator();
					while( it.hasNext() ) {
						GameObject go = it.next();
						if(go instanceof Frog) {
							if(((ICollider) go).collidesWith((ICollider) g)) {
								go.setDeletion(true);
							}
						}							
					}
				}
				else {
					((Turtle) g).move(1.0 / (DELAY_IN_MSEC));
					if( g.getX() + g.getShapeWidth() / 2.0 > X_MAX ) {
						g.setDeletion(true);
						Iterator<GameObject> it = theGameObjectCollection.getIterator();
						while( it.hasNext() ) {
							GameObject go = it.next();
							if(go instanceof Frog) {
								if(((ICollider) go).collidesWith((ICollider) g)) {
									go.setDeletion(true);
								}
							}
						}
					}
				}
			}
			else if( g instanceof Moveable ) {
				((IMove) g).move(1.0 / (DELAY_IN_MSEC));
				double x = ((Moveable) g).getX();
				double y = ((Moveable) g).getY();
				if( x - g.getShapeWidth() / 2.0 < X_MIN || x + g.getShapeWidth() / 2.0  > X_MAX || 
						y - g.getShapeHeight() / 2.0 < Y_MIN || y + g.getShapeHeight() / 2.0  > Y_MAX  ) {
					g.setDeletion(true);	
					if( g instanceof Log ) {
						Iterator<GameObject> it = theGameObjectCollection.getIterator();
						while( it.hasNext() ) {
							GameObject go = it.next();
							if(go instanceof Frog) {
								if(((ICollider) go).collidesWith((ICollider) g)) {
									go.setDeletion(true);
								}
							}
						}
					}
				}
			}
		}
		
		
		
		if( getVehicleTicker() == 120 ) {
			switch(r.nextInt(2)){
			case 0:
				addCar();
				setVehicleTicker( 0 );
				break;
			case 1:
				addTruck();
				setVehicleTicker( 0 );
				break;
			}
		}
		
		if( getFloatTicker() == 50 ) {
			switch(r.nextInt(5)) {
			case 0:
			case 1:
				addLog();
				setFloatTicker(0);
				break;
			case 2:
			case 3:
			case 4:
				addTurtle();
				setFloatTicker(0);
				break;
			}
		}
		
		if( getBirdTicker() == 500) {
			addBird();
			setBirdTicker(0);
		}
		
		notifyObservers();
	}
	
	public void collisionDetection() {
		Iterator<GameObject> iter1 = theGameObjectCollection.getIterator();
		while(iter1.hasNext() ) {
			ICollider ic1 = (ICollider) iter1.next();
			Iterator<GameObject> iter2 = theGameObjectCollection.getIterator();
			while( iter2.hasNext() ) {
				ICollider ic2 = (ICollider) iter2.next();
				if( ic1 != ic2 ) {
					if(ic1.collidesWith(ic2)) {
						ic1.handleCollision(ic2);
						
					}
				}
			}
		}
	}
	
	
	/**
	 * @return frogcount
	 */
	public int getFrogCount() {
		return frogCount;
	}
	
	public boolean getSound() {
		return sound;
	}
	
	public void setSound( boolean t ) {
		if( t == false && sound == true ) {
			backgroundMusic.stop();
		}
		else if( t == true && sound == false ) {
			backgroundMusic.loop();
		}
		sound = t;
		notifyObservers();
	}
	
	private double getFloatSpeed( int lane ) {
		switch ( lane ) {
			case 0:
				return 40;
			case 1:
				return 60;
			case 2:
				return 75;
		}	
		return 0;
	}
	
	private double getFloatY( int channel ) {
		return (channel + 6) * LANE_SIZE + LANE_SIZE / 2.0;
	}
	
	private int getFloatChannel() {
		Random r = new Random();
		return r.nextInt(3);
	}
	
	private int getVehicleLane() {
		Random r = new Random();
		return r.nextInt(4);
	}
	
	private double getVehicleX( int lane, int w ) {
		switch( lane ) {
			case 0:
			case 2:
				return X_MIN + w/2.0;
			case 1:
			case 3:
				return X_MAX - w/2.0;
			default:
		}
		return -1;
	}
	
	private double getVehicleY( int lane ) {
		return (lane + 1) * LANE_SIZE + LANE_SIZE / 2.0;
	}
	
	private double getVehicleSpeed( int lane ) {
		switch ( lane ) {
			case 0:
				return 200;
			case 1:
				return 250;
			case 2:
				return 150;
			case 3:
				return 225;
		}	
		return 0;
	}
	
		
	private String getVehicleDirection( int lane ) {
		switch( lane ) {
			case 0:
			case 2:
				return "east";
			case 1:
			case 3:
				return "west";
		}
		return "error";
	}

	/**
	 * @return the vehicleTicker
	 */
	private int getVehicleTicker() {
		return vehicleTicker;
	}

	/**
	 * @param vehicleTicker the vehicleTicker to set
	 */
	private void setVehicleTicker(int vehicleTicker) {
		this.vehicleTicker = vehicleTicker;
	}

	/**
	 * @return the floatTicker
	 */
	private int getFloatTicker() {
		return floatTicker;
	}

	/**
	 * @param floatTicker the floatTicker to set
	 */
	private void setFloatTicker(int floatTicker) {
		this.floatTicker = floatTicker;
	}

	/**
	 * @return the birdTicker
	 */
	private int getBirdTicker() {
		return birdTicker;
	}

	/**
	 * @param birdTicker the birdTicker to set
	 */
	private void setBirdTicker(int birdTicker) {
		this.birdTicker = birdTicker;
	}
	
	public void stopBackgroundMusic() {
		backgroundMusic.stop();
	}
	
	public void startBackgroundMusic() {
		backgroundMusic.play();
	}
	
}
