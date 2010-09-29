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
	private JMenu menuFile = new JMenu("File");
	
	/**
	 * Le sous-menu "Nouveau" du menu "File" de la barre
	 */
    private JMenuItem opNew = new JMenuItem("New");
    
    /*
     * Submenu "Open File" of menu "File" from the MenuBar
     */
    private JMenuItem opOpen = new JMenuItem("Open File");
    
    /**
	 * Le sous-menu "Quitter" du menu "File" de la barre
     */
    private JMenuItem opExit = new JMenuItem("Exit");
    
    
	/**
	 * Le menu "Edition" de la barre
	 */	
    private JMenu menuEdit = new JMenu("Edit");
    
    /**
	 * Le sous-menu "Defaire" du menu "Edition" de la barre
     */
    private JMenuItem opUndo = new JMenuItem("Undo");
    
    /**
	 * Le sous-menu "Refaire" du menu "Edition" de la barre
     */
    private JMenuItem opRedo = new JMenuItem("Redo");
    
    /**
	 * Le sous-menu "Supprimer" du menu "Edition" de la barre
     */
    private JMenuItem opDelete = new JMenuItem("Delete");

	/**
	 * Le menu "A propos" de la barre
	 */	    
    private JMenu menuHelp = new JMenu("Help");
    
    /**
	 * Le sous-menu "A propos" du menu "A propos" de la barre
     */
    private JMenuItem opAbout = new JMenuItem("About");
	
    /**
	 * Constructeur de la barre de menus de l'application
     * @param pseudo
     *            La {@link DrawArea} sur laquelle les actions déclenchées
     */
	public MenuBar(OptionsArea optionsArea) {
		// TODO Auto-generated constructor stub
		/* ActionListeners attribués aux sous-menus */
        opNew.addActionListener(new ActionManager(ActionToPerform.NEW_SHEET, optionsArea));        
        opOpen.addActionListener(new ActionManager(ActionToPerform.OPEN, optionsArea));
        opExit.addActionListener(new ActionManager(ActionToPerform.EXIT, optionsArea));
        
        opUndo.addActionListener(new ActionManager(ActionToPerform.UNDO, optionsArea));        
        opRedo.addActionListener(new ActionManager(ActionToPerform.REDO, optionsArea));        
        opDelete.addActionListener(new ActionManager(ActionToPerform.DELETE, optionsArea));
        
        opAbout.addActionListener(new ActionManager(ActionToPerform.ABOUT, optionsArea));

        /* Quelques raccourcis claviers */
        opNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        opExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        opUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        opRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        opDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        
        /* Ajout des sous-menus aux menus */
        this.menuFile.add(opNew);
        this.menuFile.add(opOpen);
        this.menuFile.addSeparator();
        this.menuFile.add(opExit);
        
        this.menuEdit.add(opUndo);
        this.menuEdit.add(opRedo);

        this.menuEdit.addSeparator();
        this.menuEdit.add(opDelete);
        
        this.menuHelp.add(opAbout);

        /* Ajout des éléments du menu */
        this.add(menuFile);
        this.add(menuEdit);
        this.add(menuHelp);
    
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
