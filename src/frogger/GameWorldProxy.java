package frogger;

import java.util.Iterator;

public class GameWorldProxy implements IGameWorld {
	private GameWorld realGameWorld;
	
	public GameWorldProxy( GameWorld gw ) {
		realGameWorld = gw;
	}
	
	public Iterator<GameObject> getIterator() {
		return realGameWorld.getIterator();
	}
	
	public int getScore() {
		return realGameWorld.getScore();
	}
	
	public double getElapsedTime() {
		return realGameWorld.getElapsedTime();
	}
	
	public int getFrogCount() {
		return realGameWorld.getFrogCount();
	}
	
	public boolean getSound() {
		return realGameWorld.getSound();
	}
	
}
