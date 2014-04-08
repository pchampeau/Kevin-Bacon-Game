import net.datastructures.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * An realization of a graph according to adjacency list structure.
 * Modified to use a map from vertex names to vertices instead of a 
 *   NodePositionList to hold the vertices.  
 * Assumes that vertex values are unique.
 *
 * @author Roberto Tamassia, Eric Zamore
 * @author Scot Drysdale - modifications 
 */

public class AdjacencyListGraphMap<V,E> implements Graph<V,E> {

	protected Map<V, Vertex<V>> VMap;    // Map from vertex values to vertices
	protected NodePositionList<Edge<E>> EList;	// container for edges


	// **** New methods added and old methods modified ****

	/**
	 * Default constructor that creates an empty graph 
	 */
	public AdjacencyListGraphMap() {
		VMap = new HashMap<V, Vertex<V>>();
		EList = new NodePositionList<Edge<E>>();
	}


	/** 
	 * Insert and return a new vertex with a given element
	 * @param v the vertex to be inserted 
	 * @return the vertex object inserted
	 */
	public Vertex<V> insertVertex(V v) 
			throws InvalidKeyException {
		MyVertex<V> vv =  new MyVertex<V>(v);
		if(VMap.containsKey(v))
			throw new InvalidKeyException(v + " is already a vertex identifier");
		VMap.put(v, vv);
		return vv;
	}

	/** 
	 * Insert and return a new edge with a given element between two vertices 
	 * @param v the first vertex identifier
	 * @param w the second vertex identifier
	 * @param o the label on the edge
	 */
	public Edge<E> insertEdge(V v, V w, E o)
			throws InvalidPositionException {
		return insertEdge(getVertex(v), getVertex(w), o);
	}

	/** 
	 * Remove a vertex and all its incident edges and return the
	 * element stored at the removed vertex 
	 * @param the vertex to remove
	 * @return the element stored at the vertex
	 */
	public V removeVertex(Vertex<V> v)
			throws InvalidPositionException {
		MyVertex<V> vv = checkVertex(v);
		Iterator<Edge<E>> inc = incidentEdges(v).iterator();
		while (inc.hasNext()) {
			MyEdge<E> e = (MyEdge<E>) inc.next();
			if (e.location() != null) // if the edge has not been marked invalid
				removeEdge(e);
		}
		VMap.remove(vv.element());
		return v.element();
	}

	/** 
	 * Remove a vertex and all its incident edges and return the
	 * element stored at the removed vertex 
	 * @param the identifier of the vertex to remove
	 * @return the element stored at the vertex
	 */
	public V removeVertex(V v)
			throws InvalidPositionException {
		return removeVertex(getVertex(v));
	}

	/** 
	 * Return the vertex associated with the vertex value 
	 * @parm value the value stored in the vertex. 
	 * @return the Vertex object associated with the value
	 */
	public Vertex<V> getVertex(V value)
	{  
		return VMap.get(value);
	}

	/**
	 * Is the vertex in the graph?
	 * @param name the name of the vertex
	 * @return true if a vertex with value name is in the graph
	 */
	public boolean vertexInGraph(V name) {
		return VMap.containsKey(name);
	}

	/** 
	 * Return an iterable collection of edges incident on a vertex 
	 * @param v the vertex identifier
	 * @return an iterable collection of edges incident on v
	 */
	public Iterable<Edge<E>> incidentEdges(V v)
			throws InvalidPositionException {
		return incidentEdges(getVertex(v));
	}

	/** 
	 * Return the degree of a given vertex 
	 * @param v the vertex identifier
	 * @return the degree of the vertex with identifier v
	 */
	public int degree(V v) {
		return degree(getVertex(v));
	}

	/** 
	 * Return an iterable collection over the vertices of the graph 
	 * @return an iterable collection of edges in this graph 
	 */
	public Iterable<Vertex<V>> vertices() {
		return VMap.values();
	}

	/** 
	 * Return the other end vertex of an incident edge 
	 * @param v the identifier of the vertex
	 * @param e the edge to find the opposite end of
	 * @return the opposite end of e from v
	 */
	public Vertex<V> opposite(V v, Edge<E> e)
			throws InvalidPositionException {
		return opposite(getVertex(v), e);
	}

	/** 
	 * Are u and v adjacent vertices? 
	 * @param u the identifier of the first vertex
	 * @param v the identifier of the second vertex
	 * @return true if u and v are adjacent
	 */
	public boolean areAdjacent(V u, V v)
			throws InvalidPositionException {
		return areAdjacent(getVertex(u), getVertex(v)); 
	}

	/** 
	 * Replace the vertex value in v with the new identifier o.
	 * @param v the vertex whose value is to be updated
	 * @param o the new value
	 * @returns the old value
	 */
	public V replace(Vertex<V> v, V o) throws InvalidPositionException,
	InvalidKeyException {
		V temp = v.element();
		MyVertex<V> vv = checkVertex(v);
		vv.setElement(o);
		if(VMap.containsValue(o))
			throw new InvalidKeyException(o + " is already a vertex identifier");
		VMap.remove(temp);  // Must update map entry
		VMap.put(o, vv);
		return temp;
	}

	/** 
	 * Replace the vertex value in v with the new identifier o.
	 * @param v the vertex whose value is to be updated
	 * @param o the new value
	 * @returns the old value
	 */
	public V replace(V v, V o) throws InvalidPositionException {
		return replace(getVertex(v), o);
	}


	/**
	 * Get the number of vertices
	 * @return the number of vertices in this graph
	 */
	public int numVertices() {
		return VMap.size();
	}

	// **** End of new and modified methods


	/** 
	 * Return an iterable collection of the edges of this graph 
	 * @return an interable collection of the edges of this graph
	 */
	public Iterable<Edge<E>> edges() {
		return EList;
	}


	/** 
	 * Replace the element a given position (vertex or edge) with a new
	 * element and return the old element 
	 * @param p the position (vertex or edge) to be updated
	 * @param o the new value
	 */
	public Object replace(Position p, Object o)
			throws InvalidPositionException {
		MyPosition pp = checkPosition(p);
		Object temp = p.element();
		pp.setElement(o);
		return temp;
	}

	/** 
	 * Return an iterable collection of edges incident on a vertex 
	 * @param v the vertex
	 * @return an iterable collection of edges incident on v
	 */
	public Iterable<Edge<E>> incidentEdges(Vertex<V> v)
			throws InvalidPositionException {
		MyVertex<V> vv = checkVertex(v);
		return vv.incidentEdges();
	}

	/** 
	 * Return the end vertices of a edge in an array of length 2 
	 * @param e the edge whose end vertices are to be returned
	 * @return an array of length 2 of incident vertices
	 */
	public Vertex<V>[] endVertices(Edge<E> e)
			throws InvalidPositionException {
		MyEdge<E> ee = checkEdge(e);
		return ee.endVertices();
	}

	/** 
	 * Return the other end vertex of an incident edge 
	 * @param v the vertex
	 * @param e the edge to find the opposite end of
	 * @return the opposite end of e from v
	 */
	public Vertex<V> opposite(Vertex<V> v, Edge<E> e)
			throws InvalidPositionException {
		checkVertex(v);
		MyEdge<E> ee = checkEdge(e);
		Vertex<V>[] endv = ee.endVertices();
		if (v == endv[0])
			return endv[1];
		else if (v == endv[1])
			return endv[0];
		else
			throw new InvalidPositionException("No such vertex exists");
	}

	/** 
	 * Are u and v adjacent vertices? 
	 * @param u the first vertex
	 * @param v the second vertex
	 * @return true if u and v are adjacent
	 */
	public boolean areAdjacent(Vertex<V> u, Vertex<V> v)
			throws InvalidPositionException {
		// search the incidence list of the vertex with smaller degree
		Iterable<Edge<E>> iterToSearch;
		if (degree(u) < degree(v)) {
			iterToSearch = incidentEdges(u);
		}
		else {
			iterToSearch = incidentEdges(v);
		}
		for (Edge<E> e: iterToSearch ) {
			Vertex<V>[] endV = endVertices(e);
			// if there exists an edge whose endpoints are u and v
			if ((endV[0] == u && endV[1] == v) || (endV[0] == v && endV[1] == u))
				return true;
		}
		return false;
	}

	/** 
	 * Insert and return a new edge with the given element and end vertices 
	 * @param v the first vertex 
	 * @param w the second vertex 
	 * @param o the label on the edge
	 */
	public Edge<E> insertEdge(Vertex<V> v, Vertex<V> w, E o)
			throws InvalidPositionException {
		MyVertex<V> vv = checkVertex(v);
		MyVertex<V> ww = checkVertex(w);
		MyEdge<E> ee = new MyEdge<E>(v, w, o);
		Position<Edge<E>> pv = vv.insertIncidence(ee);
		Position<Edge<E>> pw = ww.insertIncidence(ee);
		ee.setIncidences(pv, pw);
		EList.addLast(ee);
		Position<Edge<E>> pe = EList.last();
		ee.setLocation(pe);
		return ee;
	}

	/**
	 * Remove an edge and return its element 
	 * @param e the edge to remove
	 * @return the element in the edge that was removed
	 */
	public E removeEdge(Edge<E> e)
			throws InvalidPositionException {
		MyEdge<E> ee = checkEdge(e);
		MyVertex<V>[] endv = ee.endVertices();
		Position<Edge<E>>[] inc = ee.incidences();
		endv[0].removeIncidence(inc[0]);
		endv[1].removeIncidence(inc[1]);
		EList.remove(ee.location());
		ee.setLocation(null);	// invalidating this edge
		return e.element();
	}

	// Auxiliary methods

	/**
	 * Return the degree of a given vertex 
	 * @param v the vertex to examine
	 * @return the degree of v
	 */
	public int degree(Vertex<V> v) {
		MyVertex<V> vv = checkVertex(v);
		return vv.degree();
	}

	/** 
	 * Determines whether a given position is valid. 
	 * @param the position to check for validity
	 * @return the position p cast to a MyPosition
	 */
	protected MyPosition checkPosition(Position p)
			throws InvalidPositionException {
		if (p == null || !(p instanceof MyPosition))
			throw new InvalidPositionException("Position is invalid");
		return (MyPosition) p;
	}

	/** 
	 * Determines whether a given vertex is valid. 
	 * @param the vertex to check for validity
	 * @return the vertex v cast to a MyVertex
	 */
	protected MyVertex<V> checkVertex(Vertex<V> v)
			throws InvalidPositionException {
		if (v == null || !(v instanceof MyVertex))
			throw new InvalidPositionException("Vertex is invalid");
		return (MyVertex<V>) v;
	}

	/** 
	 * Determines whether a given edge is valid. 
	 * @param e the edge to check for validity
	 * @return the edge e cast to a MyEdge
	 */
	protected MyEdge<E> checkEdge(Edge<E> e)
			throws InvalidPositionException {
		if (e == null || !(e instanceof MyEdge))
			throw new InvalidPositionException("Edge is invalid");
		return (MyEdge<E>) e;
	}

	/** 
	 * Implementation of a decorable position by means of a hash table. 
	 */
	protected static class MyPosition<T>
	extends HashTableMap<Object,Object> implements DecorablePosition<T> {
		/** The element stored at this position. */
		protected T elem;

		/**
		 * Default constructor
		 */
		public MyPosition() {
			super(3);   // Reduce the size of the HashTableMap from 1000 to 3.
		}

		/**
		 * Returns the element stored at this position. 
		 * @return the element stored at this position.
		 */
		public T element() {
			return elem;
		}

		/** 
		 * Sets the element stored at this position. 
		 * @param o the new element value
		 */
		public void setElement(T o) {
			elem = o;
		}
	}

	/** 
	 * Returns a string representation of the vertex and edge lists.
	 * @return string represention of vertex list and edge list,
	 * separated by a newline. 
	 */
	public String toString() {
		return VMap.toString() + "\n" +  EList.toString();
	}

	/**
	 * return the number of edges in this graph
	 * @return the number of edges in this graph
	 */
	public int numEdges() {
		return EList.size();
	}

	/** 
	 * Replace the element in an edge by a new value
	 * @param p the edge to update
	 * @param o the new value
	 * @return the old value stored in the edge
	 */
	public E replace(Edge<E> p, E o) throws InvalidPositionException {
		E temp = p.element();
		MyEdge<E> ee = checkEdge(p);
		ee.setElement(o);
		return temp;
	}

	/** 
	 * Implementation of a vertex for an undirected adjacency list
	 * graph.  Each vertex stores its incidence container and position
	 * in the vertex container of the graph. 
	 */
	protected  class MyVertex<V> 
	extends MyPosition<V> implements Vertex<V> {
		/** Incidence container of the vertex. */
		protected PositionList<Edge<E>> incEdges;
		/** Position of the vertex in the vertex container of the graph. */
		protected Position<Vertex<V>> loc;

		/** 
		 * Constructs a vertex with the given element. 
		 * @param o the value to store in this edge
		 */
		public MyVertex(V o) {
			elem = o;
			incEdges = new NodePositionList<Edge<E>>();
		}

		/** 
		 * Return the degree of this vertex 
		 * @return the degree of this vertex
		 */
		public int degree() {
			return incEdges.size();
		}

		/** 
		 * Returns the incident edges on this vertex. 
		 * @return an iterable collection of incident edges
		 */
		public Iterable<Edge<E>> incidentEdges() {
			return incEdges;
		}

		/** 
		 * Inserts an edge into the incidence container of this vertex. 
		 * @param e the edge to insert
		 * @return the position holding edge e
		 */
		public Position<Edge<E>> insertIncidence(Edge<E> e) {
			incEdges.addLast(e);
			return incEdges.last();
		}

		/** 
		 * Removes an edge from the incidence container of this vertex. 
		 * @param p the edge-containing position to remove from its container
		 */
		public void removeIncidence(Position<Edge<E>> p) {
			incEdges.remove(p);
		}

		/** 
		 * Returns the position of this vertex in the vertex container of
		 * the graph. 
		 * @return the position
		 */
		public Position<Vertex<V>> location() {
			return loc;
		}

		/** 
		 * Sets the position of this vertex in the vertex container of
		 * the graph. 
		 * @param the new value of the position of this vertex
		 */
		public void setLocation(Position<Vertex<V>> p) {
			loc = p;
		}

		/** 
		 * Returns a string representation of the element stored at this vertex. 
		 * @return a string representation of this vertex
		 */
		public String toString() {
			return elem.toString();
		}
	}

	/** 
	 * Implementation of an edge for an undirected adjacency list
	 * graph.  Each edge stores its endpoints (end vertices), its
	 * positions within the incidence containers of its endpoints, and
	 * position in the edge container of the graph. 
	 */
	protected class MyEdge<E> extends MyPosition<E> implements Edge<E> {

		/** The end vertices of the edge. If the edge is directed  the
		 * source is in endVertices[0] and the destination is in 
		 * endVertices[1]*/
		protected MyVertex<V>[] endVertices;

		/** The positions of the entries for the edge in the incidence
		 * containers of the end vertices. If the edge is directed  the
		 * source is in inc[0] and the destination is in inc[1]*/
		protected Position<Edge<E>>[] inc;

		/** The position of the edge in the edge container of the
		 * graph. */
		protected Position<Edge<E>> loc;

		/** 
		 * Constructs an edge with the given endpoints and elements. 
		 * @param v the first vertex (source vertex if directed)
		 * @param w the second vertex (destination vertex if directed)
		 * @parem o the edge label
		 */
		protected MyEdge (Vertex<V> v, Vertex<V> w, E o) {
			elem = o;
			endVertices = (MyVertex<V>[]) new MyVertex[2];
			endVertices[0] = (MyVertex<V>)v;
			endVertices[1] = (MyVertex<V>)w;
			inc = (Position<Edge<E>>[]) new Position[2];
		}

		/** 
		 * Returns the end vertices of the edge. There are always two
		 * elements in the returned array.  If directed the 0th array
		 * position contains the source, the 1st the destination. 
		 * @return the end vertices of the edge.
		 */
		public MyVertex<V>[] endVertices() {
			return endVertices;
		}

		/** 
		 * Returns the positions of the edge in the incidence containers
		 * of its end vertices.  The returned array always contains two
		 * elements. 
		 * @return the positions of the edge in the incidence containers
		 *   of its end vertices.
		 */
		public Position<Edge<E>>[] incidences() {
			return inc;
		}

		/** 
		 * Sets the positions of the edge in the incidence containers of
		 * its end vertices. 
		 * @param pv the position of the first vertex
		 * @param pw the position of the second vertex
		 */
		public void setIncidences(Position<Edge<E>> pv, Position<Edge<E>> pw) {
			inc[0] = pv;
			inc[1] = pw;
		}

		/** 
		 * Returns the position of the edge in the edge container of the
		 * graph. 
		 * @return the position of the edge in the edge container
		 */
		public Position<Edge<E>> location() {
			return loc;
		}

		/**
		 * Sets the position of the edge in the edge container of the graph. 
		 * @param p the new value for the position of this edge
		 */
		public void setLocation(Position<Edge<E>> p) {
			loc = p;
		}

		/** 
		 * Returns a string representation of the edge via a tuple of
		 * vertices. 
		 * @return the string representation of this edge
		 */
		public String toString() {
			return element() + "(" + endVertices[0].toString() +
					"," + endVertices[1].toString() + ")";
		}
	}

	/**
	 * Test program
	 */
	public static void main(String [] args) {
		AdjacencyListGraphMap<String, String> baconGraph = 
				new AdjacencyListGraphMap<String, String>();

		baconGraph.insertVertex("Kevin Bacon");
		baconGraph.insertVertex("Laura Linney");
		baconGraph.insertVertex("Tom Hanks");
		baconGraph.insertVertex("Liam Neeson");
		baconGraph.insertEdge("Kevin Bacon", "Laura Linney", "Mystic River");
		baconGraph.insertEdge("Laura Linney", "Liam Neeson", "Kinsey");
		baconGraph.insertEdge("Kevin Bacon", "Tom Hanks", "Apollo 13");

		System.out.println("The graph:");
		System.out.println(baconGraph);

		System.out.println("\nDegree of Kevin Bacon = " + 
				baconGraph.degree("Kevin Bacon"));

		System.out.println("\nEdges adjacent to Kevin Bacon:");
		for(Edge<String> edge : baconGraph.incidentEdges("Kevin Bacon"))
			System.out.println(edge);

		for(Vertex<String> vertex : baconGraph.vertices()) {
			System.out.println("\nEdges adjacent to " + vertex + ":");
			for(Edge<String> edge : baconGraph.incidentEdges(vertex)) 
				System.out.println(edge);   		
		}

		System.out.println("\nRenaming Laura Linney to L. Linney");
		baconGraph.replace("Laura Linney", "L. Linney");
		System.out.println("\nGetting Laura Linney: " + 
				baconGraph.getVertex("Laura Linney"));

		for(Vertex<String> vertex : baconGraph.vertices()) {
			System.out.println("\nEdges adjacent to " + vertex + ":");
			for(Edge<String> edge : baconGraph.incidentEdges(vertex)) 
				System.out.println(edge);   		
		}
		System.out.println("\nRemoving L. Linney");
		baconGraph.removeVertex("L. Linney");

		for(Vertex<String> vertex : baconGraph.vertices()) {
			System.out.println("\nEdges adjacent to " + vertex + ":");
			for(Edge<String> edge : baconGraph.incidentEdges(vertex)) 
				System.out.println(edge);   		
		}  		

		baconGraph.insertVertex("Kevin Bacon");
	}
}


