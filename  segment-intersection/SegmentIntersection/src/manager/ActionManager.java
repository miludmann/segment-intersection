package manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import components.MenuBar;
import components.OptionsArea;
import components.ToolBar;
import components.DrawArea.CaracForme;
import computation.FindIntersections;


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
		NEW_SHEET, EXIT, ABOUT, UNDO, REDO,
		ACK_DEL,DELETE,
		DRAW_SEGMENT,
		REDRAW_ALL, OPEN, FIND_INTER,
		COLOR_DCEL, FIND_DCEL, SAVE_DCEL, OPEN_DCEL,
		LOCATE_POINT;
						
		/**
		 * Demande au Gestionnaire d'effectuer une action 
		 * @param am
		 * @param e
		 * @throws AssertionError
		 */
		public void performAction(ActionManager am, ActionEvent e) throws AssertionError
		{

			switch (this) 
			{
			case NEW_SHEET:
				am.getZone().deleteAll();	
				return;
			case OPEN:
				JFileChooser fc;
			    fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(fc);
			      if (returnVal == JFileChooser.APPROVE_OPTION) {
			        File file = fc.getSelectedFile();
			        //This is where a real application would open the file.
			        am.getZone().openFile(file);
			      } else {
			    	System.out.printf("Open command cancelled by user.\n");
			      }
				return;
			case EXIT:
				System.exit(0);
				return;
			case ABOUT:
                String mess = "Line segment intersection editor v1.0 \n" +
                        "Create segments and have fun visualing their intersections\n" +
                        "Compute and save DCELs !\n\n" +
                        "Authors : Guillaume Depoyant & Micha�l Ludmann\n" +
                        "Computational Geometry Project - Fall 2010 - Aarhus University";
                JOptionPane.showMessageDialog(null, mess, "About this editor", JOptionPane.INFORMATION_MESSAGE);
				return;
			case UNDO:
				am.getZone().defaire();	
				return;
			case REDO:
				am.getZone().refaire();	
				return;

			case DELETE:
				am.getZone().supprimer();	
				return;
			case DRAW_SEGMENT:
				am.getZone().addShape(CaracForme.POLYGONE);	
				return;
			case ACK_DEL:
				if(JOptionPane.showConfirmDialog(null, "Delete node ?", "Deletion", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION){
					am.getZone().supprimer();	
            	}
				return;	
			case REDRAW_ALL:
				am.getZone().getDrawArea().redrawAll();
				return;
				
			case FIND_INTER:
				am.getZone().findIntersections();
				return;
				
			case FIND_DCEL:
				am.getZone().findDCEL();
				return;
				
			case COLOR_DCEL:
				am.getZone().colorDCEL();
				return;
				
			case SAVE_DCEL:
				am.getZone().saveDCEL();
				return;
				
			case OPEN_DCEL:
				am.getZone().openDCEL();
				return;
				
			case LOCATE_POINT:
				am.getZone().locatePoint();
				return;
			
					
			} throw new AssertionError("ActionToPerform::unknown assertion : " + this);
		}
		
		/**
		 * Affichage de la valeur de l'�num
		 */
		public String toString()
		{
			switch (this)
			{
			case NEW_SHEET:
				return new String("New Sheet");
			case EXIT:
				return new String("Exit");
			case ABOUT:
				return new String("About");
			case UNDO:
				return new String("Undo");
			case REDO:
				return new String("Redo");
			case DELETE:
				return new String("Delete");
			case ACK_DEL:
				return new String("Acknowledge deletion");
			case DRAW_SEGMENT:
				return new String("Draw segment");
			case REDRAW_ALL:
				return new String("Redraw all");
			case FIND_INTER:
				return new String("Find intersections");
			case FIND_DCEL:
				return new String("Find DCEL");
			case LOCATE_POINT:
				return new String("Locate point");
			

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
