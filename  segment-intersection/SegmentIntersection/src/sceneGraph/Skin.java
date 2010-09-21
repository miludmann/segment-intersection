package sceneGraph;

import java.awt.Color;
import java.awt.Paint;

/**
 * <p>
 * <b>Skin</b>
 * </p>
 * <p>
 * Skin est la classe implémentant l'apparence des formes
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
public class Skin {
	
	private float lineThickness;
	private Color lineColor;
	private Paint innerPaint;

	
	/**
	 * 
	 */
	public Skin() {
		lineThickness = 1;
		lineColor = new Color(0);
		innerPaint = new Color(0);
	}
	
	public Skin(float lineThickness, Color lineColor, Paint innerPaint) {
		this.lineThickness = lineThickness;
		this.lineColor = lineColor;
		this.innerPaint = innerPaint;
	}
	
	public Skin(Skin skin) {
		lineThickness = skin.getLineThickness();
		lineColor = skin.getLineColor();
		innerPaint = skin.getInnerPaint();
	}

	public float getLineThickness() {
		return lineThickness;
	}

	public void setLineThickness(float lineThickness) {
		this.lineThickness = lineThickness;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public Paint getInnerPaint() {
		return innerPaint;
	}

	public void setInnerPaint(Paint innerPaint) {
		this.innerPaint = innerPaint;
	}

	/**
	 * @uml.property  name="sceneGraphTree"
	 * @uml.associationEnd  inverse="skin1:sceneGraph.SceneGraphTree"
	 */
	private SceneGraphTree sceneGraphTree;


	/**
	 * Getter of the property <tt>sceneGraphTree</tt>
	 * @return  Returns the sceneGraphTree.
	 * @uml.property  name="sceneGraphTree"
	 */
	public SceneGraphTree getSceneGraphTree() {
		return sceneGraphTree;
	}

	/**
	 * Setter of the property <tt>sceneGraphTree</tt>
	 * @param sceneGraphTree  The sceneGraphTree to set.
	 * @uml.property  name="sceneGraphTree"
	 */
	public void setSceneGraphTree(SceneGraphTree sceneGraphTree) {
		this.sceneGraphTree = sceneGraphTree;
	}
}
