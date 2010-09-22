package jTree;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import sceneGraph.SceneGraphTree;

/**
 * <p>
 * Classe du noeud que l'on sélectionne pour le Drag'n'Drop
 * Gère les événements
 * <ul>
 * <li> de sélection</li>
 * <li> de survol</li>
 * <li> de lâché</li>
 * <li> ...</li>
 * Et tout cela pour la gestion du graphe de scène avec le Drag'n'Drop
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 */
public class DragNode implements DragSourceListener, DragGestureListener
{

	DragSource source;
	DragGestureRecognizer recognizer;
	MutableNode transferable;
	MutableTreeNode oldNode;
	JTree sourceTree;

	public DragNode(JTree tree, int actions)
	{
		sourceTree = tree;
		source = new DragSource();
		recognizer = source.createDefaultDragGestureRecognizer(sourceTree, actions, this);
	}

	/*
	 * Drag Gesture Handler
	 */
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		TreePath path = sourceTree.getSelectionPath();
		if ((path == null) || (path.getPathCount() <= 1))
		{
			return;
		}
		oldNode = (MutableTreeNode) path.getLastPathComponent();
		transferable = new MutableNode(path);
		source.startDrag(dge, null, transferable, this);

	}

	/*
	 * Drag Event Handlers
	 */
	public void dragEnter(DragSourceDragEvent dsde)
	{

	}

	public void dragExit(DragSourceEvent dse)
	{
	}

	public void dragOver(DragSourceDragEvent dsde)
	{
		
	}

	public void dropActionChanged(DragSourceDragEvent dsde)
	{
		//System.out.println("Action: " + dsde.getDropAction());
		//System.out.println("Target Action: " + dsde.getTargetActions());
		//System.out.println("User Action: " + dsde.getUserAction());
	}

	public void dragDropEnd(DragSourceDropEvent dsde)
	{
		/*
		 * to support move or copy, we have to check which occurred:
		 */

		if ( dsde.getDropSuccess() )
		{
			DefaultTreeModel tm = (DefaultTreeModel) sourceTree.getModel();
			
			if( dsde.getDropAction() == 2 )
			{
				((SceneGraphTree) oldNode).removeNode();
			}
			
			tm.reload();
		}
	}
}
