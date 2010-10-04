package computation;

public interface Key
{

 public abstract float getValue();

 public abstract void setValue(float d);

 public abstract void swapValue(Key key);

 public abstract void setNode(RedBlackNode redblacknode);

 public abstract RedBlackNode getNode();

 public abstract String getLabel();

 public abstract boolean lessThan(Object obj);

 public abstract boolean lessThan(Key key);

 public abstract boolean largerThan(Object obj);

 public abstract boolean largerThan(Key key);

 public abstract boolean equals(Object obj);

 public abstract boolean equals(Key key);
}
