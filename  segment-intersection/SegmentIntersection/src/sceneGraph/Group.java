package sceneGraph;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Enumeration;
import sceneGraph.History.TypeUndo;

/**
 * <p>
 * <b>Group</b>
 * </p>
 * <p>
 * Group est la classe implémentant le groupement de formes
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
@SuppressWarnings("serial")
public class Group extends Operation {
	
	
	public Group(ArrayList<SceneGraphTree> nodes) {
		for (SceneGraphTree node : nodes)
			this.add(node);
	}
	
	public void ungroup() {
		SceneGraphTree.history.addUndo(this, TypeUndo.UNGROUP);
		
		SceneGraphTree parent = (SceneGraphTree) this.getParent();
		if (parent != null)
		{
			parent.remove(this);
			
			ArrayList<SceneGraphTree> childNodes = new ArrayList<SceneGraphTree>();
			for (Enumeration<?> e = this.children(); e.hasMoreElements(); )
			{
				childNodes.add((SceneGraphTree) e.nextElement());
			}
			for (SceneGraphTree childNode : childNodes)
			{
				parent.add(childNode);
			}
		}
	}
	
	public void draw(Graphics2D g) {
		for (Enumeration<?> e = this.children(); e.hasMoreElements(); )
		{
			((SceneGraphTree) e.nextElement()).draw(g);
		}
	}
	
}
