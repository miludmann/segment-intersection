package manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import components.MenuBar;
import components.OptionsArea;
import components.ToolBar;
import components.DrawArea.TypeAction;
import components.DrawArea.CaracForme;


/**
 * Classe de gestion des �venements boutons et liste, des instances de cette
 * classe sont utilis�es dans {@link ToolBar} et {@link MenuBar}.
 * @see ToolBar
 * @see MenuBar
 * @author Depoyant Guillaume & Ludmann Micha�l
 *
 */
public class ActionManager implements ActionListener
{
	
	/**
	 * Action sp�cifique pour laquelle ce Gestionnaire d'action est instanci�
	 */
	public enum ActionToPerform 
	{
		NOUVELLE_PAGE, QUITTER, APROPOS, DEFAIRE, REFAIRE,
		SELECTIONNER_TOUT, GROUPER, DEGROUPER, SELECTIONNER,
		COUPER,	COPIER,	COLLER,	CONFIRMER_SUPPRESSION,SUPPRIMER,
		DESSINER_POLYGONE,
		REDESSINER_TOUT;
						
		/**
		 * Demande au Gestionnaire d'effectuer une action 
		 * @param ga
		 * @param e
		 * @throws AssertionError
		 */
		public void performAction(ActionManager ga, ActionEvent e) throws AssertionError
		{

			switch (this) 
			{
			case NOUVELLE_PAGE:
				ga.getZone().supprimerTout();	
				return;
			case QUITTER:
				System.exit(0);
				return;
			case APROPOS:
                //JOptionPane jop = new JOptionPane();
                //ImageIcon img = new ImageIcon("images/Vert.png");
                String mess = "Editeur de formes g�om�triques v1.0 \n" +
                        "Cr�ez et �ditez des formes g�om�triques � votre guise !\n\n" +
                        "Auteurs : Guillaume Depoyant & Micha�l Ludmann\n" +
                        "Projet ILO - ENSIIE 2010";
                JOptionPane.showMessageDialog(null, mess, "A propos de cet �diteur", JOptionPane.INFORMATION_MESSAGE);
				return;
			case SELECTIONNER_TOUT:
				ga.getZone().setTypeDessin(TypeAction.SELECTION);	
				ga.getZone().selectionnerTout();	
				return;
			case GROUPER:
				ga.getZone().grouper();	
				return;
			case DEGROUPER:
				ga.getZone().degrouper();	
				return;
			case SELECTIONNER:
				ga.getZone().setTypeDessin(TypeAction.SELECTION);	
				return;
			case DEFAIRE:
				ga.getZone().defaire();	
				return;
			case REFAIRE:
				ga.getZone().refaire();	
				return;
			case COUPER:
				ga.getZone().couper();	
				return;
			case COPIER:
				ga.getZone().copier();	
				return;
			case COLLER:
				ga.getZone().coller();	
				return;
			case SUPPRIMER:
				ga.getZone().supprimer();	
				return;
			case DESSINER_POLYGONE:
				ga.getZone().addShape(CaracForme.POLYGONE);	
				return;
			case CONFIRMER_SUPPRESSION:
				if(JOptionPane.showConfirmDialog(null, "Ce noeud sera supprim�. Voulez-vous continuer ?", "Suppression", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION){
					ga.getZone().supprimer();	
            	}
				return;	
			case REDESSINER_TOUT:
				ga.getZone().getZoneDessin().redessinerTout();
					
			} throw new AssertionError("ActionToPerform::unknown assertion : " + this);
		}
		
		/**
		 * Affichage de la valeur de l'�num
		 */
		public String toString()
		{
			switch (this)
			{
			case NOUVELLE_PAGE:
				return new String("Nouvelle Page");
			case QUITTER:
				return new String("Quitter");
			case APROPOS:
				return new String("A propos");
			case DEFAIRE:
				return new String("Defaire");
			case REFAIRE:
				return new String("Refaire");
			case COUPER:
				return new String("Couper");
			case COPIER:
				return new String("Copier");
			case COLLER:
				return new String("Coller");
			case SUPPRIMER:
				return new String("Supprimer");
			case CONFIRMER_SUPPRESSION:
				return new String("Confirmer Suppression");
			case SELECTIONNER_TOUT:
				return new String("Selectionner Tout");
			case GROUPER:
				return new String("Grouper");
			case DEGROUPER:
				return new String("Degrouper");
			case SELECTIONNER:
				return new String("S�lectionner");
			case DESSINER_POLYGONE:
				return new String("Dessiner polygone");
			case REDESSINER_TOUT:
				return new String("Redessiner tout");

			}
			throw new AssertionError("ActionToPerform::unknown assertion : " + this);
		}
		
	}

	
	/**
	 * Action � effectuer
	 */
	private ActionToPerform action;

	/**
	 * r�f�rence vers la zone Graphique. Dans la mesure ou les actions trait�es
	 * par le GestionActions ont des r�percutions dans la zone graphique
	 */
	private OptionsArea zone;

	/**
	 * Constructeur utilis� pour traiter l'action d'un bouton ou d'un item de
	 * menu
	 * @param id indentifiant de l'action demand�e
	 * @param zone r�f�rence vers la zone graphique qui sera modifi�e par cette
	 *            action
	 */
	public ActionManager(ActionToPerform id, OptionsArea zone)
	{
		action = id;
		this.zone = zone;
	}

	/**
	 * Accesseur en lecture de la zone
	 * @return the zone
	 */
	public OptionsArea getZone()
	{
		return zone;
	}

	/**
	 * impl�mentation de ActionListener. Traitement de l'action g�n�r�e par
	 * l'appui sur un bouton ou un item de menu.
	 * @param e action courante
	 * @see ActionToPerform#performAction(ActionManager, ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		action.performAction(this, e);
	}
}
