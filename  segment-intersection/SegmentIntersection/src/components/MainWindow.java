package components;


import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import sceneGraph.Root;

/**
 * <p>
 * Fen�tre de l'�diteur de formes
 * </p>
 * @author Depoyant Guillaume & Ludmann Micha�l
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
	/**
	 * Largeur de notre fen�tre
	 */
	final static int HSIZE = 1024;
	
	/**Hauteur de notre fen�tre
	 */
	final static int VSIZE = 660;
	
	/**
	 * Nom par d�faut
	 */
	final static String WINDOWSNAME="Line segment intersection - Computational Geometry";

	private static DrawArea drawArea;
	private MenuBar menuBar;
	private OptionsArea optionsArea;
    private StatusBar statusBar;
    private ToolBar toolBar;
    private SceneGraphArea sceneGraphArea;
    private JSplitPane splitArea;

    /**
     * Origine du graphe 
     */
    public static int[] origine;
    
	 /**
	  * Le graphe de sc�ne 
	  */
    public static Root root;
    
    /**
     * <p>
     * Constructeur de la classe permettant:
     * <ul>
     * <li>Cr�ation de la fen�tre principale de notre �diteur avec un nom</li>
     * <li>D�termination du titre et de la taille de la fen�tre</li>
     * <li>Positionnement de la Barre d'outils, la zone graphique et la barre d'�tat</li>
     * <li>Ajoute du Gestionnire de fen�tre qui permet de quitter l'application en fermant la fen�tre</li>
     * </ul>
     * </p>
     * @param windowName Nom de la fen�tre
     * @see MenuBar
     * @see ToolBar
     * @see DrawArea
     * @see StatusBar
     * @see SceneGraphArea
     * @see ActionManager
     * @see ColorManager
     * @see ThicknessManager
     * @throws HeadlessException
     */
    public MainWindow()throws HeadlessException
    {
        /* Creation de la fenetre */
        super();

        // 2. Comportement quand la fenetre est ferm�e
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /* */
        root = new Root();
        origine = new int[2];

        /* Proprietes generales de la fen�tre */
        /* Taille de la fen�tre */
        this.setSize(HSIZE, VSIZE);
        /* Titre de la fen�tre */
        this.setTitle(WINDOWSNAME);
        /* Centrer la fen�tre dans l'�cran */
        this.setLocationRelativeTo(null);
        /* Comportement lorsque la fen�tre est ferm�e */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Cr�ation des diff�rents �l�ments de la fen�tre */
        statusBar = new StatusBar();
        sceneGraphArea = new SceneGraphArea();
        setDrawArea(new DrawArea(statusBar, sceneGraphArea));
        optionsArea = new OptionsArea(getZoneDessin());
        toolBar = new ToolBar(optionsArea);
        menuBar = new MenuBar(optionsArea);
               
        /* S�paration de la fen�tre en une zone de dessin et une zone de graphe de sc�ne */
        splitArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sceneGraphArea, getZoneDessin());
        sceneGraphArea.addMenus(optionsArea);

        
        /* Ajout de la barre de menu a la fenetre */
        this.setJMenuBar(menuBar);
        /* Ajout des differents sous-conteneurs au conteneur principal */
        this.getContentPane().setLayout(new BorderLayout());
        this.add(optionsArea, BorderLayout.EAST);
        this.add(toolBar, BorderLayout.NORTH);
        this.add(splitArea, BorderLayout.CENTER);
        this.add(statusBar, BorderLayout.SOUTH);
        

        /* Montrer la frame */
        this.setVisible(true);
        
        splitArea.setDividerLocation(0.35);

        this.addComponentListener(new ComponentAdapter() 
        {        	
            public void componentResized(ComponentEvent e) 
            {
                getZoneDessin().calculeOrigine();
            }
        });
    }

	public static DrawArea getZoneDessin() {
		return drawArea;
	}

	@SuppressWarnings("static-access")
	public void setDrawArea(DrawArea drawPanel) {
		this.drawArea = drawPanel;
	}

	/**
	 * @uml.property  name="barreMenu1"
	 * @uml.associationEnd  inverse="fenetrePrincipale:composants.BarreMenu"
	 */
	private MenuBar barreMenu1;

	/**
	 * Getter of the property <tt>barreMenu1</tt>
	 * @return  Returns the barreMenu1.
	 * @uml.property  name="barreMenu1"
	 */
	public MenuBar getBarreMenu1() {
		return barreMenu1;
	}

	/**
	 * Setter of the property <tt>barreMenu1</tt>
	 * @param barreMenu1  The barreMenu1 to set.
	 * @uml.property  name="barreMenu1"
	 */
	public void setBarreMenu1(MenuBar barreMenu1) {
		this.barreMenu1 = barreMenu1;
	}

	/**
	 * @uml.property  name="zoneOptions1"
	 * @uml.associationEnd  inverse="fenetrePrincipale:composants.ZoneOptions"
	 */
	private OptionsArea zoneOptions1;

	/**
	 * Getter of the property <tt>zoneOptions1</tt>
	 * @return  Returns the zoneOptions1.
	 * @uml.property  name="zoneOptions1"
	 */
	public OptionsArea getZoneOptions1() {
		return zoneOptions1;
	}

	/**
	 * Setter of the property <tt>zoneOptions1</tt>
	 * @param zoneOptions1  The zoneOptions1 to set.
	 * @uml.property  name="zoneOptions1"
	 */
	public void setZoneOptions1(OptionsArea zoneOptions1) {
		this.zoneOptions1 = zoneOptions1;
	}

	/**
	 * @uml.property  name="barreStatus1"
	 * @uml.associationEnd  inverse="fenetrePrincipale:composants.BarreStatus"
	 */
	private StatusBar barreStatus1;

	/**
	 * Getter of the property <tt>barreStatus1</tt>
	 * @return  Returns the barreStatus1.
	 * @uml.property  name="barreStatus1"
	 */
	public StatusBar getBarreStatus1() {
		return barreStatus1;
	}

	/**
	 * Setter of the property <tt>barreStatus1</tt>
	 * @param barreStatus1  The barreStatus1 to set.
	 * @uml.property  name="barreStatus1"
	 */
	public void setBarreStatus1(StatusBar barreStatus1) {
		this.barreStatus1 = barreStatus1;
	}

	/**
	 * @uml.property  name="zoneSceneGraph1"
	 * @uml.associationEnd  inverse="fenetrePrincipale:composants.ZoneSceneGraph"
	 */
	private SceneGraphArea zoneSceneGraph1;

	/**
	 * Getter of the property <tt>zoneSceneGraph1</tt>
	 * @return  Returns the zoneSceneGraph1.
	 * @uml.property  name="zoneSceneGraph1"
	 */
	public SceneGraphArea getZoneSceneGraph1() {
		return zoneSceneGraph1;
	}

	/**
	 * Setter of the property <tt>zoneSceneGraph1</tt>
	 * @param zoneSceneGraph1  The zoneSceneGraph1 to set.
	 * @uml.property  name="zoneSceneGraph1"
	 */
	public void setZoneSceneGraph1(SceneGraphArea zoneSceneGraph1) {
		this.zoneSceneGraph1 = zoneSceneGraph1;
	}

	/**
	 * @uml.property  name="barreOutils1"
	 * @uml.associationEnd  inverse="fenetrePrincipale:composants.BarreOutils"
	 */
	private ToolBar barreOutils1;

	/**
	 * Getter of the property <tt>barreOutils1</tt>
	 * @return  Returns the barreOutils1.
	 * @uml.property  name="barreOutils1"
	 */
	public ToolBar getBarreOutils1() {
		return barreOutils1;
	}

	/**
	 * Setter of the property <tt>barreOutils1</tt>
	 * @param barreOutils1  The barreOutils1 to set.
	 * @uml.property  name="barreOutils1"
	 */
	public void setBarreOutils1(ToolBar barreOutils1) {
		this.barreOutils1 = barreOutils1;
	}

	/**
	 */
	private DrawArea zoneDessin1;

	/**
	 * Getter of the property <tt>zoneDessin1</tt>
	 * @return  Returns the zoneDessin1.
	 */
	public DrawArea getZoneDessin1() {
		return zoneDessin1;
	}

	/**
	 * Setter of the property <tt>zoneDessin1</tt>
	 * @param zoneDessin1  The zoneDessin1 to set.
	 */
	public void setZoneDessin1(DrawArea zoneDessin1) {
		this.zoneDessin1 = zoneDessin1;
	}
}
