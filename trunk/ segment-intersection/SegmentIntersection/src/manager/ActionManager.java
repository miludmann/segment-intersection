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
 * Classe de gestion des évenements boutons et liste, des instances de cette
 * classe sont utilisées dans {@link ToolBar} et {@link MenuBar}.
 * @see ToolBar
 * @see MenuBar
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
public class ActionManager implements ActionListener
{
	
	/**
	 * Action spécifique pour laquelle ce Gestionnaire d'action est instancié
	 */
	public enum ActionToPerform 
	{
		NOUVELLE_PAGE, QUITTER, APROPOS, DEFAIRE, REFAIRE,
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
                String mess = "Line segment intersection editor v1.0 \n" +
                        "Create segments and have fun visualing their intersections\n\n" +
                        "Authors : Guillaume Depoyant & Michaël Ludmann\n" +
                        "Computational Geometry Project - Fall 2010 - Aarhus University";
                JOptionPane.showMessageDialog(null, mess, "About this editor", JOptionPane.INFORMATION_MESSAGE);
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
				if(JOptionPane.showConfirmDialog(null, "Ce noeud sera supprimé. Voulez-vous continuer ?", "Suppression", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION){
					ga.getZone().supprimer();	
            	}
				return;	
			case REDESSINER_TOUT:
				ga.getZone().getZoneDessin().redrawAll();
					
			} throw new AssertionError("ActionToPerform::unknown assertion : " + this);
		}
		
		/**
		 * Affichage de la valeur de l'énum
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
			case DESSINER_POLYGONE:
				return new String("Dessiner polygone");
			case REDESSINER_TOUT:
				return new String("Redessiner tout");

			}
			throw new AssertionError("ActionToPerform::unknown assertion : " + this);
		}
		
	}

	
	/**
	 * Action à effectuer
	 */
	private ActionToPerform action;

	/**
	 * référence vers la zone Graphique. Dans la mesure ou les actions traitées
	 * par le GestionActions ont des répercutions dans la zone graphique
	 */
	private OptionsArea zone;

	/**
	 * Constructeur utilisé pour traiter l'action d'un bouton ou d'un item de
	 * menu
	 * @param id indentifiant de l'action demandée
	 * @param zone référence vers la zone graphique qui sera modifiée par cette
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
	 * implémentation de ActionListener. Traitement de l'action générée par
	 * l'appui sur un bouton ou un item de menu.
	 * @param e action courante
	 * @see ActionToPerform#performAction(ActionManager, ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		action.performAction(this, e);
	}
}
