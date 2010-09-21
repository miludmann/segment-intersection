package sceneGraph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;

/**
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
@SuppressWarnings("serial")
public abstract class Shape extends SceneGraphTree {
	
	
	/**
	 * 
	 */
	public Shape() {
	}
	
	public abstract java.awt.Shape getShape();
	
	public void draw(Graphics2D g2d) {
		Skin skin = this.getSkin();
		g2d.setPaint(skin.getInnerPaint());
		g2d.fill(this.getShape());
		g2d.setStroke(new BasicStroke(skin.getLineThickness()));
		g2d.setColor(skin.getLineColor());
		g2d.draw(this.getShape());
	}
	
	public Area getArea() {
		return new Area(this.getShape());
	}
}
