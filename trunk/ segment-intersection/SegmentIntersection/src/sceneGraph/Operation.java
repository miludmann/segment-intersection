package sceneGraph;

import java.awt.geom.Area;
import java.util.Enumeration;

/**
 * <p>
 * <b>Operation</b>
 * </p>
 * <p>
 * Operation est la classe implémentant les opérations
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
@SuppressWarnings("serial")
public abstract class Operation extends SceneGraphTree {

	
	public Operation() {
		
	}
	
	public Area getArea() {
		Area area = new Area();
		
		for (Enumeration<?> e = this.children(); e.hasMoreElements(); )
		{
			area.add(((SceneGraphTree) e.nextElement()).getArea());
		}
		
		return area;
	}
}
