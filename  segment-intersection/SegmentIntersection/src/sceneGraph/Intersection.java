package sceneGraph;

/**
 * <p>
 * <b>Circle</b>
 * </p>
 * <p>
 * Circle est la classe impl�mentant le cercle
 * </p>
 * @author Depoyant Guillaume & Ludmann Micha�l
 *
 */
@SuppressWarnings("serial")
public class Intersection extends Ellipse {
	
	private float radius;
	protected float RADIUS = 10F;
	/**
	 * <p>
	 * Constructeur de la classe
	 * </p>
	 * <p>
	 * Instancie un cercle
	 * </p>
	 */
	public Intersection(float x, float y, float radius) {
		super();
		setFrame(x, y, radius);
	}
	
	public Intersection(float x, float y) {
		super();
		setFrame(x, y, RADIUS);
	}
	
	/**
	 * Getter du rayon
	 * @return le rayon
	 */
	public float getRadius() {
		return radius;
	}
	
	/**
	 * Mise � jour des coordonn�es du cercle
	 * @param x nouvelle abscisse
	 * @param y nouvelle ordonn�e
	 * @param radius nouveau rayon
	 */
	public void setFrame(float x, float y, float radius) {
		setFrame(x - radius, y - radius, x + radius, y + radius);
		this.radius = radius;
	}
}
