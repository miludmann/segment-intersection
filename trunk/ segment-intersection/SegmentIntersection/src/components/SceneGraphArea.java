/**
 * 
 */
package components;

import sceneGraph.*;

import jTree.DragNode;
import jTree.TargetDropNode;

import java.awt.BorderLayout;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


/**
 * <p>
 * <b>Zone du scène de graphe</b>
 * </p>
 * <p>
 * Classe gérant
 * <ul>
 * <li>le panneau d'affichage du graphe de scène</li>
 * <li>les clics droit dans ladite zone</li>
 * </ul>
 * 
 * @author Depoyant Guillaume & Ludmann Michaël
 */
@SuppressWarnings("serial")
public class SceneGraphArea extends JPanel {

	
	/* Affichage du graphe de scene */

    public JTree tree;
    DefaultTreeModel arbreModel = new DefaultTreeModel(MainWindow.root);
    
    /**
     * Le graphe de scène
     */
    public SceneGraphTree sceneGraphTree = null;
    private SceneGraphTree noeudCopie;
    private SceneGraphTree nouveauNoeud;
    
    /* Menu s'ouvrant si clic droit sur un noeud */
    private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem menuForme;
    private JMenuItem supprimer = new JMenuItem("Supprimer");
    private JMenuItem copier = new JMenuItem("Copier");
    private JMenuItem coller = new JMenuItem("Coller");
    
    private ArrayList<SceneGraphTree> selection = new ArrayList<SceneGraphTree>();

    /**
     * Contructeur, intanciation de la classe
     */
    public SceneGraphArea() {
    	this.tree = new JTree(MainWindow.root);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        
        @SuppressWarnings("unused")
		DragNode ds = new DragNode(this.tree, DnDConstants.ACTION_COPY_OR_MOVE);
        @SuppressWarnings("unused")
		TargetDropNode dt = new TargetDropNode(this.tree);
        setVisible(true);
           
        /* Occupation par défaut de l'espace disponible */
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(tree), BorderLayout.CENTER);
    }
    
  

    /**
     * Permet le rafraichissement du graphe de scène
     */
    public void reload() {
        ((DefaultTreeModel) tree.getModel()).reload();
        
        int nbSegments = ((DefaultTreeModel) tree.getModel()).getChildCount(MainWindow.root);
        int i;
        System.out.printf("Nb segments : "+nbSegments+"\n");
        for (i=0; i<nbSegments; i++)
        {
        	Segment segTmp = (Segment) ((DefaultTreeModel) tree.getModel()).getChild(MainWindow.root, i);
        	System.out.printf("Seg"+i+" = [{"+segTmp.getXpoints()[0]+"; "+segTmp.getYpoints()[0]+"}, {"+segTmp.getXpoints()[1]+"; "+segTmp.getYpoints()[1]+"}] ; ");
        }
        System.out.println();
       
    }
    
    /**
     * Permet l'ajout d'un nouveau noeud au graphe de scène
     * @param child enfant à rajouter au graphe de scène
     * @return
     */
    public SceneGraphTree addObject(Object child) {
        SceneGraphTree parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            //Pas de sélection : racine est choisi par défaut
            parentNode = MainWindow.root;
        } else {
            parentNode = (SceneGraphTree)
                         (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    public SceneGraphTree addObject(SceneGraphTree parent,
                                            Object child,
                                            boolean shouldBeVisible) {
        SceneGraphTree childNode = (SceneGraphTree) child;
        
        arbreModel.insertNodeInto(childNode, parent,
                                 parent.getChildCount());

        //On rend visible le nouveau noeud
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return (SceneGraphTree) childNode;
    }

	/**
	 * @uml.property  name="fenetrePrincipale"
	 * @uml.associationEnd  inverse="zoneSceneGraph1:composants.FenetrePrincipale"
	 */
	private MainWindow mainWindow;

	/**
	 * Getter of the property <tt>fenetrePrincipale</tt>
	 * @return  Returns the fenetrePrincipale.
	 * @uml.property  name="fenetrePrincipale"
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}


	/**
	 * Setter of the property <tt>mainWindow</tt>
	 * @param fenetrePrincipale  The mainWindow to set.
	 * @uml.property  name="mainWindow"
	 */
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

}
