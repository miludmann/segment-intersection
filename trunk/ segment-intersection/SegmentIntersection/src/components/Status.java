package components;

/**
 * une interface pour d�crire les messages de la barre d'�tat
 * @author Depoyant Guillaume & Ludmann Micha�l
 *
 */
public interface Status
{
	/**
	 * M�thode d'affichage d'un message dans la barre d'�tat d'une
	 * application ou bien dans la barre d'�tat d'un bowser
	 * faisant tourner une Applet
	 * @param message le message � afficher
	 */
	public void afficherMessage(String message);
	/**
	 * M�thode d'affichage des coordon�es de la souris dans la barre d'�tat
	 * d'une application ou bien dans la barre d'�tat d'un browser
	 * faisant tourner une Applet
	 * @param x l'abcsisse de la souris � afficher
	 * @param y l'ordonn�e de la souris � afficher
	 * @see DrawArea#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void afficherCoordonnees(float x, float y);
	
	/**
	 * Affichage de coordonn�es vide lorsqu'il n'y a pas de coordonn�es �
	 * afficher
	 * @see DrawArea#mouseExited(java.awt.event.MouseEvent)
	 */
	public void afficherCoordDefaut();
}