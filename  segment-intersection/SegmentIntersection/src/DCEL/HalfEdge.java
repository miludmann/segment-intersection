package DCEL;

public class HalfEdge {
	private Vertex origin;
	private HalfEdge next;
	private HalfEdge prev;
	private HalfEdge twin;
	private int id;

	
	public HalfEdge(Vertex v, int id){
		this.origin = v;
		this.id = id;
	}
	
	public void setOrigin(Vertex origin) {
		this.origin = origin;
	}

	public Vertex getOrigin() {
		return origin;
	}

	public void setNext(HalfEdge next) {
		this.next = next;
	}

	public HalfEdge getNext() {
		return next;
	}

	public void setPrev(HalfEdge prev) {
		this.prev = prev;
	}

	public HalfEdge getPrev() {
		return prev;
	}

	public void setTwin(HalfEdge twin) {
		this.twin = twin;
	}

	public HalfEdge getTwin() {
		return twin;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
}
