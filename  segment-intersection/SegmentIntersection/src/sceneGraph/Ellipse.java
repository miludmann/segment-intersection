package sceneGraph;

import java.awt.geom.Ellipse2D;

/**
 * <p>
 * <b>Ellipse</b>
 * </p>
 * <p>
 * Ellipse est la classe implémentant l'ellipse
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
@SuppressWarnings("serial")
public class Ellipse extends Shape {

	private Ellipse2D.Float ellipse;
	
	/**
	 * <p>
	 * Constructeur de la classe
	 * </p>
	 * <p>
	 * Instancie une ellipse
	 * </p>
	 */
	public Ellipse() {
		ellipse = new Ellipse2D.Float();
	}
	
	/**
	 * <p>
	 * Constructeur de la classe
	 * </p>
	 * <p>
	 * Instancie une ellipse avec des paramètres particuliers
	 * </p>
	 * @param x1 1ère abscisse
	 * @param y1 1ère ordonnée
	 * @param x2 2nde abscisse
	 * @param y2 2nde ordonnée
	 */
	public Ellipse(float x1, float y1, float x2, float y2) {
		this();
		setFrame(x1, y1, x2, y2);
	}
	
	public void setFrame(float x1, float y1, float x2, float y2) {
		float x1_ = x1, y1_ = y1, x2_ = x2, y2_ = y2;
		if (x1 > x2)
		{
			x1_ = x2;
			x2_ = x1;
		}
		if (y1 > y2)
		{
			y1_ = y2;
			y2_ = y1;
		}
		ellipse.setFrame(x1_, y1_, x2_-x1_, y2_-y1_);
	}
	
	public Ellipse2D.Float getShape() {
		return ellipse;
	}
}
