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

    public JTree arbre;
    DefaultTreeModel arbreModel = new DefaultTreeModel(MainWindow.racine);
    
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
    	this.arbre = new JTree(MainWindow.racine);
        arbre.setEditable(true);
        arbre.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
        arbre.setShowsRootHandles(true);
        
        @SuppressWarnings("unused")
		DragNode ds = new DragNode(this.arbre, DnDConstants.ACTION_COPY_OR_MOVE);
        @SuppressWarnings("unused")
		TargetDropNode dt = new TargetDropNode(this.arbre);
        setVisible(true);
           
        /* Occupation par défaut de l'espace disponible */
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(arbre), BorderLayout.CENTER);
    }
    
    
    /**
     * Permet d'ajouter un menu contextuel par clic droit sur un élément de l'arbre
     * @param  optionsArea Zone dans laquelle seront déclenchées les actions
     * @see    OptionsArea
     * @see    FenetrePrincipale
     * @param drawPanel 
     */
    public void ajouterMenus(final OptionsArea optionsArea)
    {
        /** Listeners pour menu contextuel s'ouvrant en cas de clic droit sur un noeud de l'arbre */
        /* Permet de supprimer un noeud, et de redessiner toute la zone de dessin avec la(les) forme(s) supprimée(s) en moins
        Confirmation demandée, par sécurité */
        popupMenu.add(supprimer);       
        supprimer.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                        if(JOptionPane.showConfirmDialog(null, "Ce noeud sera supprimé. Voulez-vous continuer ?", "Suppression", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    ((SceneGraphTree) arbre.getLastSelectedPathComponent()).removeNode();
                         optionsArea.getZoneDessin().getSelection().clear();
                         optionsArea.getZoneDessin().redessinerTout();
                }            
                }

        });
        
        popupMenu.addSeparator();
        /* Permet de Copier un noeud */
        popupMenu.add(copier);
        copier.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                noeudCopie = ((SceneGraphTree) arbre.getLastSelectedPathComponent());
                coller.setEnabled(true);
                }
        });
        
        /* Permet de coller un noeud qui viendrait d'être copié, confirmation demandée */
        popupMenu.add(coller);
        coller.setEnabled(false);
        coller.addActionListener(new ActionListener(){           
                public void actionPerformed(ActionEvent arg0) {
                        if(noeudCopie != null){
                                if(JOptionPane.showConfirmDialog(null, "Copier ici ?", "Coller ?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                	MainWindow.racine.addAllNode(noeudCopie.clone());
                            optionsArea.getZoneDessin().redessinerTout();
                        }            
                        }
            }
        });
        
        popupMenu.addSeparator();
        
                /* Permet d'ajouter une nouvelle forme par défaut */
        menuForme = new JMenuItem("Ajouter la forme en cours");
        menuForme.setMnemonic(KeyEvent.VK_S);
        
        
        popupMenu.add(menuForme);
        menuForme.addActionListener(new ActionListener(){           
            public void actionPerformed(ActionEvent arg0) {
            nouveauNoeud = optionsArea.getZoneDessin().creerForme(null);
            ajouterObjet(nouveauNoeud.clone());
            optionsArea.getZoneDessin().redessinerTout();
            }            
    });        
       

        /* Ajout du listener gérant le clic droit */
        arbre.addMouseListener(new MouseAdapter() 
        {
                /* Les deux méthodes suivantes sont identiques, il s'agit ici de gérer les types de clics qui diffèrent suivant les OS, mais qui doivent produire le même résultat */
            public void mouseReleased(MouseEvent e) 
            {
                int selRow = arbre.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = arbre.getPathForLocation(e.getX(), e.getY());
                arbre.clearSelection();
                arbre.setSelectionPath(selPath);
                SceneGraphTree node = ((SceneGraphTree) arbre.getLastSelectedPathComponent());
                    if (selRow != -1) 
                    {
                         if (e.isPopupTrigger()) 
                         {
                                // On affiche le menu
                        		menuForme.setEnabled(!(node instanceof Shape));
                                popupMenu.show(arbre, e.getX(), e.getY());
                                System.out.println("Clic droit sur : "+node);
                                
                         }
                         
                         selection.clear();
                         if (node.getLevel() == 1)
                                 selection.add(node);
                         
                         optionsArea.getZoneDessin().setSelection(selection);
                   }
            }
            public void mousePressed(MouseEvent e) 
            {
                this.mouseReleased(e);
            }
        });
    }
    
    /**
     * Permet le rafraichissement du graphe de scène
     */
    public void refraichir() {
        ((DefaultTreeModel) arbre.getModel()).reload();
    }
    
    /**
     * Permet l'ajout d'un nouveau noeud au graphe de scène
     * @param child enfant à rajouter au graphe de scène
     * @return
     */
    public SceneGraphTree ajouterObjet(Object child) {
        SceneGraphTree parentNode = null;
        TreePath parentPath = arbre.getSelectionPath();

        if (parentPath == null) {
            //Pas de sélection : racine est choisi par défaut
            parentNode = MainWindow.racine;
        } else {
            parentNode = (SceneGraphTree)
                         (parentPath.getLastPathComponent());
        }

        return ajouterObjet(parentNode, child, true);
    }

    public SceneGraphTree ajouterObjet(SceneGraphTree parent,
                                            Object child,
                                            boolean shouldBeVisible) {
        SceneGraphTree childNode = (SceneGraphTree) child;
        
        arbreModel.insertNodeInto(childNode, parent,
                                 parent.getChildCount());

        //On rend visible le nouveau noeud
        if (shouldBeVisible) {
            arbre.scrollPathToVisible(new TreePath(childNode.getPath()));
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
	 * Setter of the property <tt>fenetrePrincipale</tt>
	 * @param fenetrePrincipale  The fenetrePrincipale to set.
	 * @uml.property  name="fenetrePrincipale"
	 */
	public void setFenetrePrincipale(MainWindow fenetrePrincipale) {
		this.mainWindow = fenetrePrincipale;
	}

}
