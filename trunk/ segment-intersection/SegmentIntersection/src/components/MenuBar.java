package components;

import manager.ActionManager;
import manager.ActionManager.ActionToPerform;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;



/**
 * <b>La barre de menu de notre l'application</b>
 * <p>
 * Génère les boutons de navigation
 * <ul>
 * <li>Fichier</li>
 * <li>Edition</li>
 * <li>A propos</li>
 * </ul>
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 * @see FenetrePrincipale
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar
{
	/**
	 * Le menu "Fichier" de la barre
	 */	
	private JMenu menuFichier = new JMenu("Fichier");
	
	/**
	 * Le sous-menu "Nouveau" du menu "Fichier" de la barre
	 */
    private JMenuItem opNouveau = new JMenuItem("Nouveau");
    
    /**
	 * Le sous-menu "Quitter" du menu "Fichier" de la barre
     */
    private JMenuItem opQuitter = new JMenuItem("Quitter");
    
    
	/**
	 * Le menu "Edition" de la barre
	 */	
    private JMenu menuEdition = new JMenu("Edition");
    
    /**
	 * Le sous-menu "Defaire" du menu "Edition" de la barre
     */
    private JMenuItem opDefaire = new JMenuItem("Défaire");
    
    /**
	 * Le sous-menu "Refaire" du menu "Edition" de la barre
     */
    private JMenuItem opRefaire = new JMenuItem("Refaire");
    
    /**
	 * Le sous-menu "Couper" du menu "Edition" de la barre
     */
    private JMenuItem opCouper = new JMenuItem("Couper");
    
    /**
	 * Le sous-menu "Copier" du menu "Edition" de la barre
     */
    private JMenuItem opCopier = new JMenuItem("Copier");
    
    /**
	 * Le sous-menu "Coller" du menu "Edition" de la barre
     */
    private JMenuItem opColler = new JMenuItem("Coller");
    
    /**
	 * Le sous-menu "Supprimer" du menu "Edition" de la barre
     */
    private JMenuItem opSupprimer = new JMenuItem("Supprimer");

	/**
	 * Le menu "A propos" de la barre
	 */	    private JMenu menuAide = new JMenu("Aide");
    
    /**
	 * Le sous-menu "A propos" du menu "A propos" de la barre
     */
    private JMenuItem opAPropos = new JMenuItem("About");
	
    /**
	 * Constructeur de la barre de menus de l'application
     * @param pseudo
     *            La {@link DrawArea} sur laquelle les actions déclenchées
     */
	public MenuBar(OptionsArea optionsArea) {
		// TODO Auto-generated constructor stub
		/* ActionListeners attribués aux sous-menus */
        opNouveau.addActionListener(new ActionManager(ActionToPerform.NOUVELLE_PAGE, optionsArea));        
        opQuitter.addActionListener(new ActionManager(ActionToPerform.QUITTER, optionsArea));
        
        opDefaire.addActionListener(new ActionManager(ActionToPerform.DEFAIRE, optionsArea));        
        opRefaire.addActionListener(new ActionManager(ActionToPerform.REFAIRE, optionsArea));        
        opCouper.addActionListener(new ActionManager(ActionToPerform.COUPER, optionsArea));        
        opCopier.addActionListener(new ActionManager(ActionToPerform.COPIER, optionsArea));        
        opColler.addActionListener(new ActionManager(ActionToPerform.COLLER, optionsArea)); 
        opSupprimer.addActionListener(new ActionManager(ActionToPerform.SUPPRIMER, optionsArea));
        
        opAPropos.addActionListener(new ActionManager(ActionToPerform.APROPOS, optionsArea));

        /* Quelques raccourcis claviers */
        opNouveau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        opQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        opDefaire.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        opRefaire.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        opCouper.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        opCopier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        opColler.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        opSupprimer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        
        /* Ajout des sous-menus aux menus */
        this.menuFichier.add(opNouveau);
        this.menuFichier.addSeparator();
        this.menuFichier.add(opQuitter);
        
        this.menuEdition.add(opDefaire);
        this.menuEdition.add(opRefaire);
        this.menuEdition.addSeparator();
        this.menuEdition.add(opCouper);
        this.menuEdition.add(opCopier);
        this.menuEdition.add(opColler);
        this.menuEdition.addSeparator();
        this.menuEdition.add(opSupprimer);
        
        this.menuAide.add(opAPropos);

        /* Ajout des éléments du menu */
        this.add(menuFichier);
        this.add(menuEdition);
        this.add(menuAide);
    
	}

	/**
	 */
	private MainWindow mainWindow;



	/**
	 * Getter of the property <tt>mainWindow</tt>
	 * @return  Returns the mainWindow.
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}



	/**
	 * Setter of the property <tt>mainWindow</tt>
	 * @param fenetrePrincipale  The mainWindow to set.
	 */
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
}
