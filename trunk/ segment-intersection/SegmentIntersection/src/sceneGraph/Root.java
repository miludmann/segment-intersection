package sceneGraph;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.util.Enumeration;

/**
 * <p>
 * <b>Root</b>
 * </p>
 * <p>
 * Root est la classe impl�mentant le noeud "Racine"
 * </p>
 * @author Depoyant Guillaume & Ludmann Micha�l
 *
 */
@SuppressWarnings("serial")
public class Root extends SceneGraphTree {
	
	public void draw(Graphics2D g) {
		for (Enumeration<?> e = this.children(); e.hasMoreElements(); )
		{
			((SceneGraphTree) e.nextElement()).draw(g);
		}
	}

	public Area getArea() {
		Area area = new Area();
		
		for (Enumeration<?> e = this.children(); e.hasMoreElements(); )
		{
			area.add(((SceneGraphTree) e.nextElement()).getArea());
		}
		
		return area;
	}
	
	public SceneGraphTree getNodeAt(double x, double y) {
		SceneGraphTree sceneGraphTree = null;
		for (Enumeration<?> e = this.children(); e.hasMoreElements(); )
		{
			SceneGraphTree el = (SceneGraphTree) e.nextElement();
			if (el.getBounds2D().contains(x, y))
				sceneGraphTree = el;
		}
		return sceneGraphTree;
	}
}
