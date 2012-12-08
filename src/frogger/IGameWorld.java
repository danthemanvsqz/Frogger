package frogger;

import java.util.Iterator;

public interface IGameWorld {
	public Iterator<GameObject> getIterator();
	public int getScore();
	public double getElapsedTime();
	public int getFrogCount();
	public boolean getSound();
}
