package components;

/**
 * une interface pour décrire les messages de la barre d'état
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
public interface Status
{
	/**
	 * Méthode d'affichage d'un message dans la barre d'état d'une
	 * application ou bien dans la barre d'état d'un bowser
	 * faisant tourner une Applet
	 * @param message le message à afficher
	 */
	public void afficherMessage(String message);
	/**
	 * Méthode d'affichage des coordonées de la souris dans la barre d'état
	 * d'une application ou bien dans la barre d'état d'un browser
	 * faisant tourner une Applet
	 * @param x l'abcsisse de la souris à afficher
	 * @param y l'ordonnée de la souris à afficher
	 * @see DrawArea#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void afficherCoordonnees(float x, float y);
	
	/**
	 * Affichage de coordonnées vide lorsqu'il n'y a pas de coordonnées à
	 * afficher
	 * @see DrawArea#mouseExited(java.awt.event.MouseEvent)
	 */
	public void afficherCoordDefaut();
}