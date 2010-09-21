package jTree;

import sceneGraph.Group;
import sceneGraph.SceneGraphTree;


import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import components.DrawArea;
import components.MainWindow;

import java.util.ArrayList;

/**
 * <p>
 * Classe du noeud sur lequel on repose la sélection
 * Gère les événements
 * <ul>
 * <li> de survol</li>
 * <li> de lâché</li>
 * <li> ...</li>
 * Et tout cela pour la gestion du graphe de scène avec le Drag'n'Drop
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 */
public class TargetDropNode implements DropTargetListener
{

	DropTarget cible;
	JTree arbreCible;

	public TargetDropNode(JTree tree)
	{
		arbreCible = tree;
		cible = new DropTarget(arbreCible, this);
	}

	private Component getComponentForEvent(DropTargetDragEvent dtde)
	{
		DropTargetContext dtc = dtde.getDropTargetContext();
		return dtc.getComponent();
	}

	/*
	 * Drop Event Handlers
	 */
	private TreeNode getNodeForEvent(DropTargetDragEvent dtde)
	{
		Point p = dtde.getLocation();
		JTree arbre = (JTree) getComponentForEvent(dtde);
		TreePath path = arbre.getClosestPathForLocation(p.x, p.y);
		return (TreeNode) path.getLastPathComponent();
	}


	public void dragOver(DropTargetDragEvent dtde)
	{
		MutableTreeNode noeudSelectionne = (MutableTreeNode) arbreCible.getSelectionPath().getLastPathComponent();		
		MutableTreeNode noeudDepose = (MutableTreeNode) getNodeForEvent(dtde);
		
		/*
		 * On verifie que noeudDepose n'est pas un fils de noeudSelectionne
		 */
		MutableTreeNode tmp = noeudDepose;
		
		while(null != tmp)
		{
			if( tmp.equals(noeudSelectionne) )
			{
				dtde.rejectDrag();
				return;
			}
			tmp = (MutableTreeNode) tmp.getParent();
		}
	
		dtde.acceptDrag(dtde.getDropAction());
			
		return;

	}

	public void drop(DropTargetDropEvent dtde)
	{
		Point pt = dtde.getLocation();
		DropTargetContext dtc = dtde.getDropTargetContext();
		JTree arbre = (JTree) dtc.getComponent();
		TreePath parentpath = arbre.getClosestPathForLocation(pt.x, pt.y);
		MutableTreeNode noeudDepose = (MutableTreeNode) parentpath.getLastPathComponent();
		
		MutableTreeNode noeudSelectionne = (MutableTreeNode) arbreCible.getSelectionPath().getLastPathComponent();
		MutableTreeNode clonedNode = ((SceneGraphTree) noeudSelectionne).clone();
		((SceneGraphTree) noeudSelectionne.getParent()).addNode((SceneGraphTree) clonedNode);

		SceneGraphTree tmpsel;
		/*
		 * On verifie que noeudDepose n'est pas un fils de noeudSelectionne
		 */
		
		MutableTreeNode tmp = noeudDepose;
		while(null != tmp)
		{
			if( tmp.equals(noeudSelectionne) )
			{
				dtde.rejectDrop();
				return;
			}
			tmp = (MutableTreeNode) tmp.getParent();
		}

		dtde.acceptDrop(dtde.getDropAction());


		if(noeudDepose.isLeaf())
		{
			ArrayList<SceneGraphTree> selection = new ArrayList<SceneGraphTree>();
			
			selection.add((SceneGraphTree) noeudDepose);
			selection.add((SceneGraphTree) clonedNode);
			
			SceneGraphTree tmpGraph = (SceneGraphTree) noeudDepose.getParent();
			
			((SceneGraphTree) clonedNode).removeNode();
			((SceneGraphTree) noeudDepose).removeNode();
			
	    	Group group = new Group(selection);
	        tmpGraph.addNode(group);

	        tmpsel = group;
		}
		else
		{
			((SceneGraphTree) noeudDepose).addNode((SceneGraphTree) clonedNode);


	        tmpsel = (SceneGraphTree) clonedNode;
		}

		/*
		 * On selectionne le noeud contenant le noeud que l'on a deplace
		 * qui est directement sous la racine (on ne peut traiter ou selectionner
		 * les elements juste en dessous de la racine
		 */
		while(null != tmpsel.getParent() && null != tmpsel.getParent().getParent())
		{
			tmpsel = (SceneGraphTree) tmpsel.getParent();
		}
		
		DrawArea.selection.clear();
		DrawArea.selection.add(tmpsel);
		MainWindow.getZoneDessin().repaint();
        dtde.dropComplete(true);

		return;
	
	}

	@Override
	public void dragEnter(DropTargetDragEvent arg0)
	{
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0)
	{
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0)
	{
		
	}
}
