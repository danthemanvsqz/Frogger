/**
 * 
 */
package frogger;

import java.awt.geom.Point2D;

/**
 * @author dan
 *
 */
public interface ISelectable {
	public void setSelected(boolean yesNo);
	public boolean isSelected();
	public boolean contains(Point2D p);
}
