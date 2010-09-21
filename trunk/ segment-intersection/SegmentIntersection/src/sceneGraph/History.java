package sceneGraph;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * <p>
 * <b>History</b>
 * </p>
 * <p>
 * History est la classe implémentant l'historique permettant de
 * <ul>
 * <li> défaire une action</li>
 * <li> refaire une action< (préalablement défaite)</li>
 * </ul>
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
public class History {

	public enum TypeUndo
	{
		ADD,
		ADDALL,
		REMOVE,
		UNGROUP
	}
	
	private LinkedList<Object[]> undoList = new LinkedList<Object[]>();
	private LinkedList<Object[]> redoList = new LinkedList<Object[]>();

	
	/**
	 * 
	 */
	public History() {
		
	}
	
	public void addUndo(SceneGraphTree node, TypeUndo typeUndo) {
		ArrayList<SceneGraphTree> children = new ArrayList<SceneGraphTree>();
		for (Enumeration<?> e = node.children(); e.hasMoreElements(); )
		{
			children.add((SceneGraphTree) e.nextElement());
		}
		SceneGraphTree parent = (SceneGraphTree) node.getParent();
		
		if (node != null && parent != null)
			undoList.addLast(new Object[] {node, parent, children, typeUndo});
		redoList.clear();
	}
	
	public void reset() {
		undoList.clear();
		redoList.clear();
	}
	
	@SuppressWarnings("unchecked")
	public void undo() {
		if (!undoList.isEmpty())
		{
			Object[] object = undoList.removeLast();
			SceneGraphTree node = (SceneGraphTree) object[0];
			SceneGraphTree parent = (SceneGraphTree) object[1];
			ArrayList<SceneGraphTree> children = (ArrayList<SceneGraphTree>) object[2];
			TypeUndo typeUndo = (TypeUndo) object[3];
			
			if (parent != null)
			{
				if (typeUndo == TypeUndo.ADD)
				{
					if (parent.isNodeChild(node))
					{
						parent.remove(node);
						for (SceneGraphTree element : children) {
							parent.add(element);
						}
					}
				}
				else if (typeUndo == TypeUndo.ADDALL)
				{
					if (parent.isNodeChild(node))
						parent.remove(node);
				}
				else if (typeUndo == TypeUndo.REMOVE)
				{
					parent.add(node);
				}
				else if (typeUndo == TypeUndo.UNGROUP)
				{
					for (SceneGraphTree element : children) {
						node.add(element);
					}
					parent.add(node);
				}
			}
			
			redoList.addFirst(object);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void redo() {
		if (!redoList.isEmpty())
		{
			Object[] object = redoList.removeFirst();
			SceneGraphTree node = (SceneGraphTree) object[0];
			SceneGraphTree parent = (SceneGraphTree) object[1];
			ArrayList<SceneGraphTree> children = (ArrayList<SceneGraphTree>) object[2];
			TypeUndo typeUndo = (TypeUndo) object[3];
			
			if (parent != null)
			{
				if (typeUndo == TypeUndo.ADD)
				{
					for (SceneGraphTree element : children) {
						node.add(element);
					}
					parent.add(node);
				}
				else if (typeUndo == TypeUndo.ADDALL)
				{
					parent.add(node);
				}
				else if (typeUndo == TypeUndo.REMOVE)
				{
					parent.remove(node);
				}
				else if (typeUndo == TypeUndo.UNGROUP)
				{
					parent.remove(node);
					for (SceneGraphTree element : children) {
						parent.add(element);
					}
				}
			}
			
			undoList.addLast(object);
		}
	}

	/**
	 * @uml.property  name="sceneGraphTree"
	 * @uml.associationEnd  inverse="history1:sceneGraph.SceneGraphTree"
	 */
	private SceneGraphTree sceneGraphTree;


	/**
	 * Getter of the property <tt>sceneGraphTree</tt>
	 * @return  Returns the sceneGraphTree.
	 * @uml.property  name="sceneGraphTree"
	 */
	public SceneGraphTree getSceneGraphTree() {
		return sceneGraphTree;
	}

	/**
	 * Setter of the property <tt>sceneGraphTree</tt>
	 * @param sceneGraphTree  The sceneGraphTree to set.
	 * @uml.property  name="sceneGraphTree"
	 */
	public void setSceneGraphTree(SceneGraphTree sceneGraphTree) {
		this.sceneGraphTree = sceneGraphTree;
	}
}
