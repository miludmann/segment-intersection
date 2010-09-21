package jTree;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.tree.TreePath;

/**
 * Gestion des noeuds déplaçables
 * @author Depoyant Guillaume & Ludmann Michaël
 */
class MovableNode implements Transferable
{

	public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class, "Tree Path");
	DataFlavor flavors[] = { TREE_PATH_FLAVOR };
	TreePath path;

	public MovableNode(TreePath tp)
	{
		path = tp;
	}

	public synchronized DataFlavor[] getTransferDataFlavors()
	{
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return (flavor.getRepresentationClass() == TreePath.class);
	}

	public synchronized Object getTransferData(DataFlavor flavor)
	        throws UnsupportedFlavorException, IOException
	{
		if (isDataFlavorSupported(flavor))
		{
			return path;
		}
		else
		{
			throw new UnsupportedFlavorException(flavor);
		}
	}
}
