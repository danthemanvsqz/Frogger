package frogger;

import java.util.ArrayList;
import java.util.Iterator;

public class GameObjectCollection implements IGameObjectCollection {
	private ArrayList<GameObject> theGameObjectList;
	
	public GameObjectCollection() {
		theGameObjectList = new ArrayList<GameObject>();
	}
	
	public void add(GameObject x){
		theGameObjectList.add(x);
	}
	public void remove(GameObject x){
		theGameObjectList.remove(x);
	}
	public Iterator<GameObject> getIterator(){
		return new GameObjectIterator();
	}
	
	public int getSize() {
		return theGameObjectList.size();
	}
	
	private class GameObjectIterator implements Iterator<GameObject> {
		private int currElementIndex;
		
		public GameObjectIterator(){
			currElementIndex = -1;
		}
		
		public boolean hasNext(){
			if( theGameObjectList.isEmpty() ) {
				return false;
			}
			if( currElementIndex >= theGameObjectList.size() - 1 ) {
				return false;
			}
			return true;
		}
		
		public GameObject next(){
			currElementIndex ++;
			return theGameObjectList.get(currElementIndex);
		}
		
		public void remove(){}
	}
}
