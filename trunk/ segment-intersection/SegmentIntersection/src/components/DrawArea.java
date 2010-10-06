package components;

import sceneGraph.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Enumeration;


import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import sceneGraph.SceneGraphTree;



/**
 * <p>
 * <b>
 * Zone de dessin
 * </b>
 * </p>
 * <p>
 * Cette classe hérite de JPanel et ne contient aucun widget, on va directement
 * desssiner dedans
 * </p>
 * <p>
 * D'autre part, elle implémente l'interface MouseMotionListener afin de traiter
 * les évènements liés au déplacement de la souris au dessus de ce Panel (affichage
 * des coordonnées de la souris dans la barre d'état).
 * </p>
 * 
 * @author Depoyant Guillaume & Ludmann Michaël
 * @see StatusBar
 * @see Dessin
 */
@SuppressWarnings("serial")
public class DrawArea extends JPanel implements MouseListener,
        MouseMotionListener
{
	
	/**
	 * Caractéristique de la forme
	 */
	public enum CaracForme
	{
		POLYGONE;
		
		
		/**
		 * Enumère les différents types qu'une forme peut adopter
		 * @return String les différents types qu'un forme peut adopter
		 */
		public String toString() {
			StringBuffer stringBuffer = new StringBuffer();
			switch (this) {
	
			case POLYGONE:
				stringBuffer.append("Polygone quelconque");
				break;

			default:
				break;
			}
			return new String(stringBuffer);
		}
	}
	
	/**
	 * Différents type d'action possibles d'éxecution
	 */
	public enum TypeAction
	{
		/** Action de redimmensionnement des rectangles matérialisant la sélection */
		REDIM_HG,REDIM_BG,REDIM_HD,REDIM_BD,REDIM_MG,REDIM_MD,REDIM_HM,REDIM_BM,REDIM_C,
		/** Action de cisaillement des rectangles de la sélection */
		CIS_HG,CIS_BG,CIS_HD,CIS_BD,CIS_MG,CIS_MD,CIS_HM,CIS_BM,
		DESSIN,
		SELECTION,
		TRANSLATION,
		ROTATION;		
	}


	private Shape currentShape = null;
	private CaracForme caracFormeCourante;
	
	
	private Skin currentSkin = new Skin();

	// on a besoin de connaître la barreEtat si l'on veut pouvoir
	// écrire des trucs dedans
	/** la barre d'état où l'on doit afficher les messages */
	private StatusBar statusBar;
	private SceneGraphArea sceneGraphArea;

	public static ArrayList<SceneGraphTree> selection = new ArrayList<SceneGraphTree>();
	private ArrayList<SceneGraphTree> copier = new ArrayList<SceneGraphTree>();
	private ArrayList<float[]> points = new ArrayList<float[]>();
	private ArrayList<int[]> polygoneDefaut = new ArrayList<int[]>();
	private ArrayList<int[]> triangleDefaut = new ArrayList<int[]>();
	private SceneGraphTree transformeNoeud;
	private int[] pointDepart;
	private int numButton;
	private TypeAction typeDessin = TypeAction.DESSIN;
	private Cursor curseurDessin = new Cursor(Cursor.CROSSHAIR_CURSOR);
	private Cursor curseurSelection = new Cursor(Cursor.DEFAULT_CURSOR);
	protected float RADIUS = 2.5F;
	private int nbSegments = 0;

	private boolean mouseMoved;	
	private JPopupMenu popupMenu;	
	
	/**
	 * Constructeur de la zone graphique avec un lien vers la barre d'état pour
	 * mettre à jour celle ci en fonction de l'action courante
	 * @param statusBar la barre d'état
	 * @param sceneGraphArea 
	 * @see Dessin#Dessin()
	 * @see #addMouseListener(MouseListener)
	 * @see #addMouseMotionListener(MouseMotionListener)
	 * @see #setCursor(Cursor)
	 */
	public DrawArea(StatusBar barreInfo, SceneGraphArea sceneGraphArea)
	{
		super(true); // isDoubleBuffered = true;
		
		polygoneDefaut.add(new int[] {0,0});
		polygoneDefaut.add(new int[] {200,100});
		polygoneDefaut.add(new int[] {200,200});
		polygoneDefaut.add(new int[] {100,300});
		triangleDefaut.add(new int[] {0,0});
		triangleDefaut.add(new int[] {200,100});
		triangleDefaut.add(new int[] {200,200});
		this.caracFormeCourante = CaracForme.POLYGONE;
		this.sceneGraphArea = sceneGraphArea;
		this.statusBar = barreInfo;

		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		
		popupMenu = new JPopupMenu();
		
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * Dessin dans la zone graphique : 
	 * 	- Dessin des lignes déjà présente dans la classe {@link Dessin}
	 * 	- Dessin de la {@link Droite} en cours s'il y en a une. 
	 * @param g le contexte graphique qu'il faudra caster en {@link Graphics2D}
	 * pour utiliser les possibilités de Swing.
	 * @see Graphics2D#setRenderingHint(java.awt.RenderingHints.Key, Object)
	 * @see Graphics2D#clearRect(int, int, int, int)
	 * @see Dessin#dessineToi(Graphics2D)
	 * @see Droite#dessineToi(Graphics2D)
	 */
 


    protected void paintComponent(Graphics g)
	{
		// caractéristiques graphiques : mise en place de l'antialiasing
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// taille de la zone de dessin
		Dimension d = getSize();
		// on commence par effacer le fond
		g2d.clearRect(0, 0, d.width, d.height);
		
		MainWindow.root.draw(g2d);
		
		if (currentShape != null)
		{
			currentShape.draw(g2d);
		}
		
		int taillePoints = points.size();
		if (taillePoints > 1)
		{
			g2d.setStroke(new BasicStroke(currentSkin.getLineThickness()));
			g2d.setColor(currentSkin.getLineColor());
			for (int i = 0; i < taillePoints - 1; i++)
			{
				g2d.drawLine(Float.floatToIntBits(points.get(i)[0]), Float.floatToIntBits(points.get(i)[1]), Float.floatToIntBits(points.get(i+1)[0]), Float.floatToIntBits(points.get(i+1)[1]));
			}

		}
		if (!selection.isEmpty())
		{
			setApparenceSelection(new Skin(1, new Color(0,255,0), new Color(0, 255, 0)));

//	        g2d.setStroke(new BasicStroke());
//	        g2d.setColor(new Color(50,80,150));
//			for (SceneGraphTree s : selection)
//			{
//				Rectangle2D rectangle = s.getBounds2D();
//				double larg = rectangle.getMinX() + rectangle.getWidth()/2 - 8;
//				double haut = rectangle.getMinY() + rectangle.getHeight()/2 - 8;              
//              g2d.draw(new Ellipse2D.Double(larg, haut, 12, 12));
//                
//
//			}
		}
		
	}

	// ------------------------------------------------------------------------
	// Méthodes du MouseListener
	// ------------------------------------------------------------------------

	/**
	 * Méthode appellée lorsqu'un bouton de la souris est appuyé puis relaché
	 * à la même position
	 * @param e l'évènement souris
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// Rien à faire : un mouseClicked est déclenché si la souris est
		// pressed puis released à la même position
	}

	/**
	 * Méthode appelée lorsque le pointeur de la souris pénètre dans la zone
	 * graphique
	 * @param e l'évènement souris
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// Rien à faire
	}

	/**
	 * Méthode appellée lorsque l'on sort de la zone graphique.
	 * Auquel cas on cesse d'afficher les coordonnées du pointeur pour les
	 * remplacer par ___
	 * @param e l'évènement souris
	 * @see StatusBar#afficherCoordDefaut()
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		statusBar.afficherCoordDefaut();
	}

	/**
	 * Méthode appelée lorsqu'un bouton de la souris est pressé (mais pas encore
	 * relaché : on initie la création d'une droite
	 * @param e l'évènement souris
	 * @see #initieDroite(int, int)
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{

		int x = e.getX(), y = e.getY();
		int[] point = {x, y};
		
		mouseMoved = false;

		numButton = e.getButton();
		if (numButton == MouseEvent.BUTTON1)
		{
	    	pointDepart = point;

	    	if (typeDessin == TypeAction.DESSIN)
			{
	    		creerForme(e);
			}				
	    	else if (typeDessin == TypeAction.SELECTION || e.isControlDown())
	    	{
	            SceneGraphTree s = MainWindow.root.getNodeAt(x, y);
	            
                if (!e.isControlDown())
        			setApparenceSelection(currentSkin);
                    selection.clear();
                
	            if (s != null)
	            {
	            	if (selection.contains(s))
	            		selection.remove(s);
	            	else
	            		selection.add(s);
	            }
	            
	    	}
	    	
				
			repaint();
		}
		else
			redrawAll();
		
		maybeShowPopup(e);
	}


	/**
	 * Méthode appellée lorsque l'on relâche un bouton après l'avoir pressé : 
	 * on termine alors la création d'une nouvelle droite
	 * @param e l'évènement souris 
	 * @see #termineDroite(int, int)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (numButton == MouseEvent.BUTTON1)
		{
			if (typeDessin == TypeAction.DESSIN)
			{
				
				if (currentShape != null)
				{
			        float[] xSegment = ((Segment) currentShape).getXpoints();
			        float[] ySegment = ((Segment) currentShape).getYpoints();

			        float[] pointSegmentStart = {xSegment[0], ySegment[0]};
			        try {
			        	float[] pointSegmentEnd = {xSegment[1], ySegment[1]};
				        loadSegment(pointSegmentStart, pointSegmentEnd);
			        } catch (ArrayIndexOutOfBoundsException ex) {}
			        
//			        System.out.printf("New Segment start : "+xSegment[0]+" "+ySegment[0]+"\n");
//			        System.out.printf("New Segment end : "+xSegment[1]+" "+ySegment[1]+"\n\n");
//			        
//			        currentShape.setSkin(new Skin(currentSkin));
//			        MainWindow.root.addNode(currentShape);
//			        redrawAll();
//			        nbSegments++;
				}
			}
		}	
		maybeShowPopup(e);
	}
	
	public void loadSegment(float[] pointSegmentStart, float[] pointSegmentEnd)
	{
        points.clear();
		System.out.println("DEBUG "+pointSegmentStart[0]+" "+pointSegmentEnd[0]+" "+pointSegmentStart[1]+" "+pointSegmentEnd[1]);
		points.add(pointSegmentStart);
		points.add(pointSegmentEnd);
        currentShape = new Segment(points, nbSegments);
		currentShape.setSkin(new Skin(currentSkin));
        MainWindow.root.addNode(currentShape);
        redrawAll();		
        nbSegments ++;
	}
	
	public Intersection createIntersection(float x, float y)
	{
		currentShape = new Intersection(x, y, RADIUS);
		currentShape.setSkin(new Skin(1, new Color(255,0,0), new Color(255, 0, 0)));
        MainWindow.root.addNode(currentShape);
        
        Intersection newIntersection = (Intersection) currentShape; // copy intersection because redrawAll() clears currentShape
        redrawAll();
        return newIntersection;
	}

	// ------------------------------------------------------------------------
	// Méthodes du MouseMotionListener
	// ------------------------------------------------------------------------

	/**
	 * Méthode appellée lorsqu'un bouton de la souris a été pressé et que l'on 
	 * déplace la souris : on déplace alors l'extremité de la droite que l'on
	 * a commencé à créer dans {@link #mousePressed(MouseEvent)} et on continue
	 * d'afficher les coordonnées du pointeur dans la zone graphique.
	 * @param e l'évènement souris
	 * @see #mousePressed(MouseEvent)
	 * @see Droite#deplaceExtremite(int, int)
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		float x = e.getX();
		float y = e.getY();

		// affichage des coordonnées
		statusBar.afficherCoordonnees(x, y);


		mouseMoved = true;
		
		if (numButton == MouseEvent.BUTTON1)
		{
			float[] point = {x, y};
			
			if (points.size() >= 2)
			{
				points.remove(1);
			}
			points.add(point);
			//points.set(taillePoints - 1, point);
			if(points.size() == 2)
			{
				try{
					((Segment)currentShape).setSegment(points);
				} catch (NullPointerException ex) { 
					this.typeDessin = TypeAction.DESSIN;
					points.clear();
				}
			}
			repaint();
		}
	}

    public Shape creerForme(MouseEvent e) {
		float x0, y0, x1, y1, largeur, hauteur, angle, nbClics;
		float x, y;
		if (e != null)
		{
			x = e.getX();
			y = e.getY();
			x0 = x;
			y0 = y;
			x1 = x;
			y1 = y;
			largeur = 0;
			hauteur = 0;
			angle = 0;
			nbClics = e.getClickCount();
		}
		else
		{
			largeur = 300;
			hauteur = 200;
			angle = 100;
			x0 = MainWindow.origine[0];
			y0 = MainWindow.origine[1];
			x = x0 - largeur/2;
			y = y0 - hauteur/2;
			x1 = x0 + largeur/2;
			y1 = y0 + hauteur/2;
			nbClics = 2;
		}
		
		float[] point = {x, y};
		
		currentShape = null;

		points.add(point);
        currentShape = new Segment(points, nbSegments);

		if (currentShape != null)
	        currentShape.setSkin(new Skin(currentSkin));
		
		return currentShape;
    }
	
	/**
	 * Méthode appellée lorsque l'on déplace la souris dans la zone graphique : 
	 * on affiche les coordonnées du pointeur dans cette zone
	 * @param e l'évènement souris
	 * @see StatusBar#afficherCoordonnees(float, float)
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		float x = e.getX();
		float y = e.getY();
		statusBar.afficherCoordonnees(x, y);
		
		if (!e.isControlDown() && !selection.isEmpty())
		{
			for (SceneGraphTree s : selection)
			{
				transformeNoeud = s;
				
				setDrawingType(TypeAction.SELECTION);
			}
		}
		else if (typeDessin != TypeAction.DESSIN)
			setDrawingType(TypeAction.SELECTION);
	}

	/**
	 * Calcule l'origine de la zone de dessin
	 */
    public void calculeOrigine() {
        MainWindow.origine[0] = this.getWidth()/ 2;
        MainWindow.origine[1] = this.getHeight() / 2;

        repaint();
    }
    
    /**
     * 
     * @return CaracForme la caractéristique de la forme
     */
	public CaracForme getCaracFormeCourante() {
		return this.caracFormeCourante;
	}
	
	
	/**
	 * Changer le type de forme courant
	 * @param caracForme forme que l'on souhaite définir comme courante
	 */
	public void setCaracFormeCourante(CaracForme caracForme) {
		this.caracFormeCourante = caracForme;
		points.clear();
	}

	/**
	 * Récupérer le type du dessin
	 * @param typeDessin 
	 * @return 
	 */
	public TypeAction getTypeDessin(TypeAction typeDessin) {
		return this.typeDessin;
	}
	
	/**
	 * Définir le type du dessin
	 * @param typeDessin
	 */
	public void setDrawingType(TypeAction typeDessin) {
		this.typeDessin = typeDessin;
        switch (typeDessin)
        {
        case DESSIN:
			setApparenceSelection(currentSkin);
			selection.clear();
            setCursor(curseurDessin);
        	statusBar.afficherMessage("Press and drag mouse to draw");
            break;
        case SELECTION:
           setCursor(curseurSelection);
           if (selection.size() == 2)
           	statusBar.afficherMessage("Right clic to make a composition of both shapes.");
           else
           	statusBar.afficherMessage("Press mouse on shape to select it. Press CTRL to select many.");
           break;
        }
        
        repaint();
	}

	/**
	 * Définir une apparence sur une sélection
	 * @param apparence une apparence à appliquer
	 */
	public void setApparenceSelection(Skin apparence) {
		for (SceneGraphTree node : selection)
		{
			node.setSkinAll(apparence);
		}
		repaint();
	}
	
	/**
	 * Obtenir l'apparence courante
	 * @return l'apprence courante
	 */
	public Skin getCurrentSkin() {
		return currentSkin;
	}
	
	/**
	 * Définir l'apparence courante
	 * @param apparence l'apparence à appliquer
	 */
	public void setCurrentSkin(Skin apparence) {
		this.currentSkin = apparence;
	}
	
	/**
	 * Méthode permettant de tout redessiner, à savoir le graphe de scène
	 * et la zone de dessin
	 */
	public void redrawAll() {
		currentShape = null;
        points.clear();
        sceneGraphArea.reload();
        this.typeDessin = TypeAction.DESSIN;
        repaint();
	}
	
	/**
	 * Permet de tout supprimer à l'écran (zone de dessin + graphe de scène)
	 */
	public void deleteAll() {
		MainWindow.root.removeAll();
    	selection.clear();
		redrawAll();
		nbSegments = 0;
	}

	/**
	 * Permet de supprimer ce qui est dans la sélection
	 */
    public void supprimer() {
        if (!selection.isEmpty())
        {
	    	for (SceneGraphTree node : selection)
	    		node.removeNode();
	    	
	    	selection.clear();
	    	
	    	redrawAll();
        }
    }

    /**
     * Permet d'annuler une action
     */
	public void defaire() {
		SceneGraphTree.history.undo();
    	selection.clear();
		redrawAll();
	}

	/**
	 * Permet de répéter une action
	 */
	public void refaire() {
		SceneGraphTree.history.redo();
    	selection.clear();
		redrawAll();
	}
	
	/**
	 * Coupe la sélection
	 */
    public void couper() { 
        if (!selection.isEmpty())
        {
	    	for (SceneGraphTree node : selection)
	    		node.removeNode();
	    	
	    	copier.clear();
	    	copier.addAll(selection);
	    	selection.clear();
	    	
	    	redrawAll();
        }
    }

    /**
     * Copie la sélection
     */
    public void copier() { 
        if (!selection.isEmpty())
        {
	    	copier.clear();
	    	copier.addAll(selection);
        }
    }

    /**
     * Colle ce qui a été copié ou coupé
     */
    public void coller() { 
        if (!copier.isEmpty())
        {
	    	for (SceneGraphTree node : copier)
				MainWindow.root.addAllNode((SceneGraphTree)node.clone());
	    	
	    	redrawAll();
        }
    }


    /**
     * Sert à la gestion du clic droit dans la zone de dessin
     * @param e mouseEvent
     */
	public void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger())
		{
			boolean selectionSize = (selection.size() == 2);
			for (MenuElement menuItem : popupMenu.getSubElements())
				((JMenuItem)menuItem).setEnabled(selectionSize);
			
			popupMenu.show(this, e.getX(), e.getY());
		}
	}
	

	
	/**
	 * Getter pour la sélection
	 * @return un tableau de SceneGraphTree
	 */
	public ArrayList<SceneGraphTree> getSelection() {
		return selection;
	}
	
	@SuppressWarnings("static-access")
	public void setSelection(ArrayList<SceneGraphTree> selection) {
		this.selection = selection;
		repaint();
	}

	/**
	 */
	private MainWindow mainWindow;

	/**
	 * Getter of the property <tt>fenetrePrincipale</tt>
	 * @return  Returns the fenetrePrincipale.
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	/**
	 * Setter of the property <tt>fenetrePrincipale</tt>
	 * @param fenetrePrincipale  The fenetrePrincipale to set.
	 */
	public void setMainWindow(MainWindow fenetrePrincipale) {
		this.mainWindow = fenetrePrincipale;
	}
	
	public SceneGraphArea getSceneGraphArea(){
		return this.sceneGraphArea;
	}
}
