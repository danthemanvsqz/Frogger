package frogger;

import java.util.Iterator;

public interface IGameObjectCollection {
	public void add(GameObject x);
	public void remove(GameObject x);
	public Iterator<GameObject> getIterator();
}
