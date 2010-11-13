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
	
	private JButton segment = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/segment.png")));
    private JButton nouvellePage = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/nouvellePage.png")));
    private JButton openFile = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/open.png")));
    private JButton defaire = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/defaire.png")));
    private JButton refaire = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/refaire.png")));
    private JButton supprimer = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/supprimer.png")));
    private JButton findInter = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/find_inter.png")));
    private JButton findDCEL = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/calc.png")));
    private JButton colorDCEL = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/polygone.png")));
    private JButton saveDCEL = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/save.png")));
    private JButton openDCEL = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/refresh.png")));
    private JButton locatePoint = new JButton(new ImageIcon(OptionsArea.class.getResource("/images/selectionnerTout.png")));
    

    
    public ToolBar(OptionsArea optionsArea)
    {
        /* Creation de la BarreOutils horizontale */
        super(JToolBar.HORIZONTAL);
        optionsArea.setTb(this);
        
        segment.addActionListener(new ActionManager(ActionToPerform.DRAW_SEGMENT, optionsArea));
        nouvellePage.addActionListener(new ActionManager(ActionToPerform.NEW_SHEET, optionsArea));
        openFile.addActionListener(new ActionManager(ActionToPerform.OPEN, optionsArea));
        defaire.addActionListener(new ActionManager(ActionToPerform.UNDO, optionsArea));
        refaire.addActionListener(new ActionManager(ActionToPerform.REDO, optionsArea));
        supprimer.addActionListener(new ActionManager(ActionToPerform.DELETE, optionsArea));
        findInter.addActionListener(new ActionManager(ActionToPerform.FIND_INTER, optionsArea));
        findDCEL.addActionListener(new ActionManager(ActionToPerform.FIND_DCEL, optionsArea));
        colorDCEL.addActionListener(new ActionManager(ActionToPerform.COLOR_DCEL, optionsArea));
        saveDCEL.addActionListener(new ActionManager(ActionToPerform.SAVE_DCEL, optionsArea));
        openDCEL.addActionListener(new ActionManager(ActionToPerform.OPEN_DCEL, optionsArea));
        locatePoint.addActionListener(new ActionManager(ActionToPerform.LOCATE_POINT, optionsArea));
               

        
        //segment.setToolTipText("Draw segment");
        nouvellePage.setToolTipText("New sheet");
        openFile.setToolTipText("Open File");
        defaire.setToolTipText("Undo");
        refaire.setToolTipText("Redo");
        supprimer.setToolTipText("Delete");
        findInter.setToolTipText("Find Intersections");
        findDCEL.setToolTipText("Find DCEL");
        colorDCEL.setToolTipText("Color DCEL");
        saveDCEL.setToolTipText("Save DCEL");
        openDCEL.setToolTipText("Open DCEL");
        locatePoint.setToolTipText("Locate point");

        
        this.setLayout(new GridLayout(1, 0));
        
        this.add(defaire);
        this.add(refaire);
        this.add(supprimer);
        
        this.addSeparator();

        this.add(nouvellePage);
        this.add(openFile);
        
        this.addSeparator();
        
        this.add(findInter);
        
        this.addSeparator();
        
        this.add(findDCEL);
        this.add(colorDCEL);
        this.add(saveDCEL);
        this.add(openDCEL);
        
        this.addSeparator();

        this.add(locatePoint);
            
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
	
	public JButton getLocateBox(){
		return locatePoint;
	}
	
}
