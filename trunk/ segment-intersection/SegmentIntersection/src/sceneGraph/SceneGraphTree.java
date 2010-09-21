package sceneGraph;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import sceneGraph.History.TypeUndo;

/**
 * <p>
 * <b>SceneGraphTree</b>
 * </p>
 * <p>
 * SceneGraphTree est la classe implémentant le graphe de scène
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
@SuppressWarnings("serial")
public abstract class SceneGraphTree extends DefaultMutableTreeNode {

	public static History history = new History();
	
	private Skin skin = new Skin();
	
	public SceneGraphTree() {
		super();
		setUserObject(this.getClass().getSimpleName());
	}
	
	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}
	
	public void setSkinAll(Skin skin) {
		for (Enumeration<?> e = this.depthFirstEnumeration(); e.hasMoreElements(); )
		{
			((SceneGraphTree) e.nextElement()).setSkin(skin);
		}
	}
	
	public void addNode(SceneGraphTree node) {
		this.add(node);
		SceneGraphTree.history.addUndo(node, TypeUndo.ADD);
	}
	
	public void addAllNode(SceneGraphTree node) {
		this.add(node);
		SceneGraphTree.history.addUndo(node, TypeUndo.ADDALL);
	}
	
	public void removeNode() {
		SceneGraphTree.history.addUndo(this, TypeUndo.REMOVE);
		this.removeFromParent();
	}
	
	public void removeAll() {
		this.removeAllChildren();
		SceneGraphTree.history.reset();
	}
	
	public SceneGraphTree clone() {
		SceneGraphTree node = (SceneGraphTree)super.clone();
		for (Enumeration<?> e = this.children(); e.hasMoreElements(); )
		{
			node.add(((SceneGraphTree) e.nextElement()).clone());
		}
		
		return node;
	}
	
	public Rectangle2D getBounds2D() {
		return this.getArea().getBounds2D();
	}
	
	public abstract void draw(Graphics2D g);
	public abstract Area getArea();
	/**
	 * @uml.property  name="skin1"
	 * @uml.associationEnd  inverse="sceneGraphTree:sceneGraph.Skin"
	 */
	private Skin skin1;

	/**
	 * Getter of the property <tt>skin1</tt>
	 * @return  Returns the skin1.
	 * @uml.property  name="skin1"
	 */
	public Skin getSkin1() {
		return skin1;
	}

	/**
	 * Setter of the property <tt>skin1</tt>
	 * @param skin1  The skin1 to set.
	 * @uml.property  name="skin1"
	 */
	public void setSkin1(Skin skin1) {
		this.skin1 = skin1;
	}
	/**
	 * @uml.property  name="history1"
	 * @uml.associationEnd  inverse="sceneGraphTree:sceneGraph.History"
	 */
	private History history1;


	/**
	 * Getter of the property <tt>history1</tt>
	 * @return  Returns the history1.
	 * @uml.property  name="history1"
	 */
	public History getHistory1() {
		return history1;
	}

	/**
	 * Setter of the property <tt>history1</tt>
	 * @param history1  The history1 to set.
	 * @uml.property  name="history1"
	 */
	public void setHistory1(History history1) {
		this.history1 = history1;
	}
}
