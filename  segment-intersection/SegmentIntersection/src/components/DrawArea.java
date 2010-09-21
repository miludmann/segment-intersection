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
import java.awt.geom.Point2D;
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


	private Shape formeCourante = null;
	private CaracForme caracFormeCourante;
	
	
	private Skin apparenceCourante = new Skin();

	// on a besoin de connaître la barreEtat si l'on veut pouvoir
	// écrire des trucs dedans
	/** la barre d'état où l'on doit afficher les messages */
	private StatusBar statusBar;
	private SceneGraphArea sceneGraphArea;

	public static ArrayList<SceneGraphTree> selection = new ArrayList<SceneGraphTree>();
	private ArrayList<SceneGraphTree> copier = new ArrayList<SceneGraphTree>();
	private ArrayList<int[]> points = new ArrayList<int[]>();
	private ArrayList<int[]> polygoneDefaut = new ArrayList<int[]>();
	private ArrayList<int[]> triangleDefaut = new ArrayList<int[]>();
	private SceneGraphTree transformeNoeud;
	private int[] pointDepart;
	private int numButton;
	private TypeAction typeDessin = TypeAction.DESSIN;
	private Cursor curseurDessin = new Cursor(Cursor.CROSSHAIR_CURSOR);
	private Cursor curseurSelection = new Cursor(Cursor.DEFAULT_CURSOR);
	private Cursor curseurRedimNW = new Cursor(Cursor.NW_RESIZE_CURSOR);
	private Cursor curseurRedimNE = new Cursor(Cursor.NE_RESIZE_CURSOR);
	private Cursor curseurEchelleN = new Cursor(Cursor.N_RESIZE_CURSOR);
	private Cursor curseurEchelleW = new Cursor(Cursor.W_RESIZE_CURSOR);
	private Cursor curseurMain = new Cursor(Cursor.HAND_CURSOR);
	private Cursor curseurDeplacement = new Cursor(Cursor.MOVE_CURSOR);
	private boolean sourisDeplacee;	
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
		
		MainWindow.racine.draw(g2d);
		
		if (formeCourante != null)
		{
			formeCourante.draw(g2d);
		}
		
		int taillePoints = points.size();
		if (taillePoints > 1)
		{
			g2d.setStroke(new BasicStroke(apparenceCourante.getLineThickness()));
			g2d.setColor(apparenceCourante.getLineColor());
			for (int i = 0; i < taillePoints - 1; i++)
			{
				g2d.drawLine(points.get(i)[0], points.get(i)[1], points.get(i+1)[0], points.get(i+1)[1]);
			}

		}
		
		if (!selection.isEmpty())
		{
	        g2d.setStroke(new BasicStroke());
	        g2d.setColor(new Color(50,80,150));
			for (SceneGraphTree s : selection)
			{
				Rectangle2D rectangle = s.getBounds2D();
				double x0 = rectangle.getMinX() - 3;
				double x1 = rectangle.getMaxX() - 3;
				double y0 = rectangle.getMinY() - 3;
				double y1 = rectangle.getMaxY() - 3;
				double w = rectangle.getMinX() + rectangle.getWidth()/2 - 3;
				double h = rectangle.getMinY() + rectangle.getHeight()/2 - 3;
				double larg = rectangle.getMinX() + rectangle.getWidth()/2 - 8;
				double haut = rectangle.getMinY() + rectangle.getHeight()/2 - 8;
				
                g2d.draw(rectangle);
                
                g2d.draw(new Rectangle2D.Double(x0, y0, 6, 6));
                g2d.draw(new Rectangle2D.Double(x0, y1, 6, 6));
                g2d.draw(new Rectangle2D.Double(x1, y0, 6, 6));
                g2d.draw(new Rectangle2D.Double(x1, y1, 6, 6));
                g2d.draw(new Rectangle2D.Double(x0, h, 6, 6));
                g2d.draw(new Rectangle2D.Double(x1, h, 6, 6));
                g2d.draw(new Rectangle2D.Double(w, y0, 6, 6));
                g2d.draw(new Rectangle2D.Double(w, y1, 6, 6));
                g2d.draw(new Ellipse2D.Double(larg, haut, 12, 12));
			}
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
		//initieDroite(e.getX(), e.getY());
		/*
		if (shapeType.equals("Ellipse")) {
            shape = new Shape(new Ellipse(Math.abs(e.getX() - FenetrePrincipale.origine[0]), Math.abs(e.getY() - FenetrePrincipale.origine[1])));
		*/
		int x = e.getX(), y = e.getY();
		int[] point = {x, y};
		
		sourisDeplacee = false;

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
	            SceneGraphTree s = MainWindow.racine.getNodeAt(x, y);
	            
                if (!e.isControlDown())
                    selection.clear();
                
	            if (s != null)
	            {
	            	if (selection.contains(s))
	            		selection.remove(s);
	            	else
	            		selection.add(s);
	            }
	            
				setTypeDessin(TypeAction.SELECTION);
	    	}
	    	
				
			repaint();
		}
		else
			redessinerTout();
		
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
				
				if (formeCourante != null)
				{
			        formeCourante.setSkin(new Skin(apparenceCourante));
			        MainWindow.racine.addNode(formeCourante);
			        redessinerTout();
				}
			}
			
			
		}
		
		maybeShowPopup(e);
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
		int x = e.getX();
		int y = e.getY();

		// affichage des coordonnées
		statusBar.afficherCoordonnees(x, y);


		sourisDeplacee = true;
		
		if (numButton == MouseEvent.BUTTON1)
		{
			int[] point = {x, y};
			int taillePoints = points.size();
			
			switch (typeDessin)
			{
			case DESSIN:				
				switch (caracFormeCourante) {
				
				case POLYGONE:
					points.set(taillePoints - 1, point);
					break;
				
		
				default:
					break;
				}
				break;

			}
			
			repaint();
		}
	}

    public Shape creerForme(MouseEvent e) {
		int x, y, x0, y0, x1, y1, largeur, hauteur, angle, nbClics;
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
		
		int[] point = {x, y};
		
		formeCourante = null;
		
		switch (caracFormeCourante) {
		
		case POLYGONE:
			if (e != null)
			{
		    	points.add(point);
	            if (points.size() == 2)
	                formeCourante = new Segment(points);
	            else if (points.size() < 1)
	            	points.add(point);
			}
			else
                formeCourante = new Segment(polygoneDefaut);
			break;
	

		default:
			break;
		}
		
		if (formeCourante != null)
	        formeCourante.setSkin(new Skin(apparenceCourante));
		
		return formeCourante;
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
		int x = e.getX();
		int y = e.getY();
		statusBar.afficherCoordonnees(x, y);
		
		if (!e.isControlDown() && !selection.isEmpty())
		{
			for (SceneGraphTree s : selection)
			{
				transformeNoeud = s;
				
				Rectangle2D rect = s.getBounds2D();
				double x0 = rect.getMinX() - 3;
				double x1 = rect.getMaxX() - 3;
				double y0 = rect.getMinY() - 3;
				double y1 = rect.getMaxY() - 3;
				double w = rect.getMinX() + rect.getWidth()/2 - 3;
				double h = rect.getMinY() + rect.getHeight()/2 - 3;
				double larg = rect.getMinX() + rect.getWidth()/2 - 8;
				double haut = rect.getMinY() + rect.getHeight()/2 - 8;
				Rectangle2D rect1 = new Rectangle2D.Double(x0, y0, 6, 6);
				Rectangle2D rect2 = new Rectangle2D.Double(x0, y1, 6, 6);
				Rectangle2D rect3 = new Rectangle2D.Double(x1, y0, 6, 6);
				Rectangle2D rect4 = new Rectangle2D.Double(x1, y1, 6, 6);
				Rectangle2D rect5 = new Rectangle2D.Double(x0, h, 6, 6);
				Rectangle2D rect6 = new Rectangle2D.Double(x1, h, 6, 6);
				Rectangle2D rect7 = new Rectangle2D.Double(w, y0, 6, 6);
				Rectangle2D rect8 = new Rectangle2D.Double(w, y1, 6, 6);
				Ellipse2D ellipseCentre = new Ellipse2D.Double(larg, haut, 6, 6);
				
				if (rect1.contains(x, y))
				{
					setTypeDessin(TypeAction.REDIM_HG);
					break;
				}
				else if (rect2.contains(x, y))
				{
					setTypeDessin(TypeAction.REDIM_BG);
					break;
				}
				else if (rect3.contains(x, y))
				{
					setTypeDessin(TypeAction.REDIM_HD);
					break;
				}
				else if (rect4.contains(x, y))
				{
					setTypeDessin(TypeAction.REDIM_BD);
					break;
				}
				else if (rect5.contains(x, y))
				{
					setTypeDessin(TypeAction.REDIM_MG);
					break;
				}
				else if (rect6.contains(x, y))
				{
					setTypeDessin(TypeAction.REDIM_MD);
					break;
				}
				else if (rect7.contains(x, y))
				{
					setTypeDessin(TypeAction.REDIM_HM);
					break;
				}
				else if (rect8.contains(x, y))
				{
					setTypeDessin(TypeAction.REDIM_BM);
					break;
				}
				else if (ellipseCentre.contains(x, y))
				{
					setTypeDessin(TypeAction.REDIM_C);
					break;
				}
				else if (rect.contains(x, y))
				{
					setTypeDessin(TypeAction.TRANSLATION);
					break;
				}
				else
					setTypeDessin(TypeAction.SELECTION);
			}
		}
		else if (typeDessin != TypeAction.DESSIN)
			setTypeDessin(TypeAction.SELECTION);
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
	public void setTypeDessin(TypeAction typeDessin) {
		this.typeDessin = typeDessin;
        switch (typeDessin)
        {
        case DESSIN:
			selection.clear();
            setCursor(curseurDessin);
        	statusBar.afficherMessage("Cliquez puis faîtes glisser le pointeur pour dessiner.");
            break;
        case SELECTION:
           setCursor(curseurSelection);
           if (selection.size() == 2)
           	statusBar.afficherMessage("Clic droit pour composer les deux figures.");
           else
           	statusBar.afficherMessage("Cliquez sur une figure pour la sélectionner. Maintenez la touche Ctrl enfoncée pour sélectionner plusieurs figures.");
           break;
        case REDIM_C:
        case ROTATION:
         	setCursor(curseurMain);
        	statusBar.afficherMessage("Cliquez puis faîtes glisser le pointeur pour redimensionner. Maintenez précédemment la touche Maj enfoncée pour faire tourner.");
	        break;
        case TRANSLATION:
            setCursor(curseurDeplacement);
        	statusBar.afficherMessage("Cliquez puis faîtes glisser le pointeur pour déplacer.");
            break;
        case REDIM_HG:
        case REDIM_BD:
        case CIS_HG:
        case CIS_BD:
            setCursor(curseurRedimNW);
        	statusBar.afficherMessage("Cliquez puis faîtes glisser le pointeur pour redimensionner. Maintenez précédemment la touche Maj enfoncée pour cisailler.");
            break;
        case REDIM_BG:
        case REDIM_HD:
        case CIS_BG:
        case CIS_HD:
            setCursor(curseurRedimNE);
        	statusBar.afficherMessage("Cliquez puis faîtes glisser le pointeur pour redimensionner. Maintenez précédemment la touche Maj enfoncée pour cisailler.");
            break;
        case REDIM_MG:
        case REDIM_MD:
        case CIS_MG:
        case CIS_MD:
            setCursor(curseurEchelleN);
        	statusBar.afficherMessage("Cliquez puis faîtes glisser le pointeur pour redimensionner. Maintenez précédemment la touche Maj enfoncée pour cisailler.");
           break;
        case REDIM_HM:
        case REDIM_BM:
        case CIS_HM:
        case CIS_BM:
            setCursor(curseurEchelleW);
        	statusBar.afficherMessage("Cliquez puis faîtes glisser le pointeur pour redimensionner. Maintenez précédemment la touche Maj enfoncée pour cisailler.");
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
	public Skin getApparenceCourante() {
		return apparenceCourante;
	}
	
	/**
	 * Définir l'apparence courante
	 * @param apparence l'apparence à appliquer
	 */
	public void setApparenceCourante(Skin apparence) {
		this.apparenceCourante = apparence;
	}
	
	/**
	 * Méthode permettant de tout redessiner, à savoir le graphe de scène
	 * et la zone de dessin
	 */
	public void redessinerTout() {
		formeCourante = null;
        points.clear();
        sceneGraphArea.refraichir();
        repaint();
	}
	
	/**
	 * Permet de tout supprimer à l'écran (zone de dessin + graphe de scène)
	 */
	public void supprimerTout() {
		MainWindow.racine.removeAll();
    	selection.clear();
		redessinerTout();
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
	    	
	    	redessinerTout();
        }
    }

    /**
     * Permet d'annuler une action
     */
	public void defaire() {
		SceneGraphTree.history.undo();
    	selection.clear();
		redessinerTout();
	}

	/**
	 * Permet de répéter une action
	 */
	public void refaire() {
		SceneGraphTree.history.redo();
    	selection.clear();
		redessinerTout();
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
	    	
	    	redessinerTout();
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
				MainWindow.racine.addAllNode((SceneGraphTree)node.clone());
	    	
	    	redessinerTout();
        }
    }

    /**
     * Groupe la sélection
     */
    public void grouper() {
        if (!selection.isEmpty())
        {
        	Group group = new Group(selection);
        	MainWindow.racine.addNode(group);
            selection.clear();
            selection.add(group);
            redessinerTout();
        }
    }

    /**
     * Dégroupe le groupe sélectionné
     */
    public void degrouper() {
        if (selection.size() == 1)
        {
        	SceneGraphTree node = selection.get(0);
            if (node instanceof Group)
            {
            	Group group = (Group) node;
            	
            	selection.clear();
        		for (Enumeration<?> e = group.children(); e.hasMoreElements(); )
        		{
        			selection.add((SceneGraphTree) e.nextElement());
        		}
        		
        		group.ungroup();
        		
            	redessinerTout();
            }
        }
    }

    /**
     * Sélectionne tous les noeuds fils de la racine
     */
    public void selectionnerTout() {
    	selection.clear();
		for (Enumeration<?> e = MainWindow.racine.children(); e.hasMoreElements(); )
		{
			selection.add((SceneGraphTree) e.nextElement());
		}
    	
    	redessinerTout();
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
	public void setFenetrePrincipale(MainWindow fenetrePrincipale) {
		this.mainWindow = fenetrePrincipale;
	}
}
