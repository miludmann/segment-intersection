package components;



import manager.*;
import manager.ActionManager.ActionToPerform;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;


/**
 * <p>Une barre d'outils pour créer
 * <ul>
 * <li>les nouvelles formes</li>
 * <li>effacer la dernière forme</li>
 * <li>effacer toute la zone graphique</li>
 * <li>copier</li>
 * <li>coller</li>
 * <li>selectionner</li>
 * <li>grouper</li>
 * </ul>
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 * @see FenetrePrincipale
 */
@SuppressWarnings("serial")
public class ToolBar extends JToolBar
{
	
	private JButton polygone = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/polygone.png")));
    private JButton nouvellePage = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/nouvellePage.png")));
    private JButton defaire = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/defaire.png")));
    private JButton refaire = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/refaire.png")));
    private JButton couper = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/couper.png")));
    private JButton copier = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/copier.png")));
    private JButton coller = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/coller.png")));
    private JButton supprimer = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/supprimer.png")));
    

    
    public ToolBar(OptionsArea optionsArea)
    {
        /* Creation de la BarreOutils horizontale */
        super(JToolBar.HORIZONTAL);
        
        polygone.addActionListener(new ActionManager(ActionToPerform.DESSINER_POLYGONE, optionsArea));
        nouvellePage.addActionListener(new ActionManager(ActionToPerform.NOUVELLE_PAGE, optionsArea));
        defaire.addActionListener(new ActionManager(ActionToPerform.DEFAIRE, optionsArea));
        refaire.addActionListener(new ActionManager(ActionToPerform.REFAIRE, optionsArea));
        couper.addActionListener(new ActionManager(ActionToPerform.COUPER, optionsArea));
        copier.addActionListener(new ActionManager(ActionToPerform.COPIER, optionsArea));
        coller.addActionListener(new ActionManager(ActionToPerform.COLLER, optionsArea));
        supprimer.addActionListener(new ActionManager(ActionToPerform.SUPPRIMER, optionsArea));
        
        polygone.setToolTipText("Dessiner polygone");
        nouvellePage.setToolTipText("Nouvelle zone de dessin");
        defaire.setToolTipText("Défaire");
        refaire.setToolTipText("Refaire");
        couper.setToolTipText("Couper");
        copier.setToolTipText("Copier");
        coller.setToolTipText("Coller");
        supprimer.setToolTipText("Supprimer");
        
        this.setLayout(new GridLayout(1, 0));
        
        this.add(defaire);
        this.add(refaire);
        this.add(supprimer);
        
        this.addSeparator();

        this.add(nouvellePage);
        
        this.addSeparator();

        this.add(couper);
        this.add(copier);
        this.add(coller);
 
        this.addSeparator();

        this.add(polygone);
            
        this.setBorder(BorderFactory.createEtchedBorder());
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
