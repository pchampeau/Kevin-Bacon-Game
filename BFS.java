import java.io.*;
import java.util.*;
import javax.swing.JFileChooser;
import net.datastructures.Edge;
import net.datastructures.Vertex;

/**
 * Breadth-First Search
 * PS-5, Dartmouth CS 10, Winter 2014
 * @author Matthew Ginsberg and Paul Champeau
 * @param <V> the type of the vertex label.  (All must be unique.)
 * @param <E> the type of the edge label.
 */

public class BFS<E, V> {
	private Scanner console;
	public Queue<Vertex<V>> queue = new LinkedList<Vertex<V>>();
	public DirectedAdjListMap<V, E> BFSMap = new DirectedAdjListMap<V, E>();

	public BFS(AdjacencyListGraphMap<V, E> adjlist) {
		this.BFSMap = null;

		console = new Scanner(System.in);
	}
	/**
	 * This method implements BFS to create a Directed Adjacency List Map
	 * @param startVertex
	 * @param baconGraph
	 * @return
	 */
	public DirectedAdjListMap<V, E> implementBFS(Vertex<V> startVertex, AdjacencyListGraphMap<V, E> baconGraph) {
		DirectedAdjListMap<V, E> shortestPath = new DirectedAdjListMap<V, E>();
		//Checking that the start vertex is valid and in the graph
		if (startVertex != null && baconGraph.vertexInGraph(startVertex.element())) {	
			queue.add(startVertex);
			shortestPath.insertVertex(startVertex.element());
			
			//Continue looping while the queue is not empty
			while (queue.peek() != null) {
				Vertex<V> v1 = queue.poll();
				//for all of popped vertex's neighbors
				for (Edge<E> edge: baconGraph.incidentEdges(v1)) {
					Vertex<V> v2 = baconGraph.opposite(v1, edge);

					if (!shortestPath.vertexInGraph(v2.element())) {
						shortestPath.insertVertex(v2.element());
						shortestPath.insertDirectedEdge(v2, v1, edge.element());

						queue.add(v2);
					}
				}
			}		
		}	

		this.BFSMap = shortestPath;

		return shortestPath;

	}
	/**
	 * This is a helper method for implementBFS to check if the starting
	 *  vertex is actually a node in the graph.
	 * @param startVertex
	 * @param baconGraph
	 * @return
	 */
	public DirectedAdjListMap<V, E> implementBFS(V startVertex, AdjacencyListGraphMap<V, E> baconGraph) {
		if (baconGraph.vertexInGraph(startVertex)) {
			return implementBFS(baconGraph.VMap.get(startVertex), baconGraph);
		}
		else {														
			System.out.println("Invalid Vertex");
			return null;
		}
	}
	/**
	 * This is an alternate version of implementBFS that takes a 
	 * String as an input rather than the actual vertex.
	 * @param startVertex
	 * @param baconGraph
	 * @return
	 */
	private DirectedAdjListMap<V, E> implementBFS(String startVertex, AdjacencyListGraphMap<V, E> baconGraph) {
		// TODO Auto-generated method stub
		if (baconGraph.VMap.containsKey(startVertex)) {
			return implementBFS(baconGraph.VMap.get(startVertex), baconGraph);
		}
		else {
			System.out.println("Invalid Vertex");
			return null;
		}
	}
	/**
	 * This method takes a vertex element and finds the corresponding Bacon Number,
	 * while printing out the trace history back to Kevin Bacon
	 * @param name
	 */
	public void findBaconNumber(V name) {
		int baconNumber = 0;
		String s = "";
		//Check that the starting vertex is in the graph
		if (BFSMap.vertexInGraph(name)) {
			Vertex<V> vTemp = BFSMap.getVertex(name);
			//Loop until we find the Kevin Bacon vertex
			while (!vTemp.element().equals("Kevin Bacon")) {

				Iterable<Edge<E>> edges = BFSMap.edges();			
				for (Edge<E> edge: edges) {
					if (BFSMap.endVertices(edge)[0].element().equals(vTemp.element())) {
						baconNumber++;
						Vertex<V> oldV = vTemp;
						vTemp = BFSMap.endVertices(edge)[1];
						s = s + oldV.element() + " appeared in " + edge.element() + " with " + vTemp.element() + ". \n";
					}
				}
			}
			s = name + "'s Bacon Number is " + baconNumber + "\n" + s;
			System.out.println(s);
		}
	}
	/**
	 * This is an alternative findBaconNumber method which accepts the actual
	 * vertex rather than the vertex element.
	 * @param vertex
	 */
	public void findBaconNumber(Vertex<V> vertex) {
		if (vertex != null) {
			findBaconNumber(vertex.element());
		}
	}
	/**
	 * This is another alternate findBaconNumber method which takes a vertex's
	 * String label rather than the actual vertex
	 * @param name
	 * @param baconGraph
	 */
	private void findBaconNumber(String name, AdjacencyListGraphMap<String, String> baconGraph) {
		// TODO Auto-generated method stub
		if (baconGraph.vertexInGraph(name) && BFSMap.VMap.containsKey(name)) {
			findBaconNumber(BFSMap.VMap.get(name));
		}
		else {
			if (baconGraph.vertexInGraph(name)) {
				System.out.println(name + " has no Bacon Number." + "\n");
			}
			else {
				System.out.println("Invalid Vertex." + "\n");
			}
		}
	}
	/**
	 * This method runs through the hand coded test case and allows the user to play
	 * the bacon game with this smaller data set.
	 */
	public static void bfsTest() {

		//implement graph by hand
		AdjacencyListGraphMap<String, String> baconGraph = new AdjacencyListGraphMap<String, String>();
		baconGraph.insertVertex("Kevin Bacon");
		baconGraph.insertVertex("actor1");
		baconGraph.insertVertex("actor2");
		baconGraph.insertVertex("actor3");
		baconGraph.insertVertex("actor4");
		baconGraph.insertVertex("actor5");
		baconGraph.insertVertex("actor6");

		baconGraph.insertEdge("Kevin Bacon", "actor1", "movie1");
		baconGraph.insertEdge("Kevin Bacon", "actor2", "movie1");
		baconGraph.insertEdge("actor1", "actor2", "movie1");
		baconGraph.insertEdge("actor1", "actor3", "movie2");
		baconGraph.insertEdge("actor3", "actor2", "movie3");
		baconGraph.insertEdge("actor3", "actor4", "movie4");
		baconGraph.insertEdge("actor5", "actor6", "movie5");

		//create a BFS for the graph, and proceed to playing Bacon Game.
		printGraph(baconGraph);
		BFS baconBFS = new BFS(baconGraph);
		baconBFS.implementBFS("Kevin Bacon", baconGraph);

		System.out.println("This is the hand-coded test case.");
		System.out.println("Quit test case to enter full Bacon Game.");
		runGame(baconBFS, baconGraph);
	}

	/**
	 * This method creates an Adjacency List Graph Map from a tree map that
	 * contains movies as keys and an ArrayList of corresponding actors
	 * @param actorMovieMap
	 * @return
	 */
	public static AdjacencyListGraphMap<String, String> createAdjList(Map<String, ArrayList<String>> actorMovieMap) {
		AdjacencyListGraphMap<String, String> graph = new AdjacencyListGraphMap<String, String>();
		Set<String> keys = actorMovieMap.keySet();
		//Create a vertex for each actor
		if (keys != null) {
			for(String movie : keys){
				ArrayList<String> actors = actorMovieMap.get(movie);
				for(String actor : actors){
					if(!graph.vertexInGraph(actor))
						graph.insertVertex(actor);
				}
			}

			//Creating edges for each actor in a movie
			for(String movie : keys){
				ArrayList<String> actors = actorMovieMap.get(movie);
				for (int i = 0; i < actors.size(); i++) {
					for (int next = i+1; next < actors.size(); next++) {
						graph.insertEdge(actors.get(i), actors.get(next), movie);
					}
				}
			}
		}

		return graph;	
	}
	/**
	 * This method creates a map from the actor or movie file that 
	 * is selected by the user.
	 * @param typeName
	 * @return
	 */
	public static Map<Integer, String> makeIDMap(String typeName){								
		System.out.println("Please select " + typeName + " file...");
		String pathName = setFilePath();

		Map<Integer, String> idMap = new TreeMap<Integer, String>();
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(pathName));
			String line;
			while ((line = input.readLine()) != null) {
				if (line.contains("|")) {
					String[] tokens = line.split("\\|");	//tokens[0] = id number. tokens[1] is actor or movie name.
					int number = Integer.valueOf(tokens[0]);
					//Insert the actor and corresponding ID into the map
					if (!idMap.containsKey(number)) {						
						idMap.put(number, tokens[1]);
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return idMap;
	}
	/**
	 * This method prints the entire Kevin Bacon graph by printing all
	 * the edges for each vertex.
	 * @param baconGraph
	 */
	public static void printGraph(AdjacencyListGraphMap<String, String> baconGraph) {
		System.out.println("Printing out edges for each vertex in graph:" + "\n");
		Iterable<String> actorsList = baconGraph.VMap.keySet();
		for (String actor: actorsList) {
			String s = actor + "'s edges: ";
			baconGraph.incidentEdges(actor);

			System.out.println(actor + "'s edges: " + baconGraph.incidentEdges(actor));

		}
		System.out.println("\n");
	}
	/**
	 * This method creates a map that uses movies as keys and an ArrayList of
	 * corresponding actors as values. The inputs are maps that have movies and
	 * actors mapped to their corresponding ID's
	 * @param movieMap
	 * @param actorMap
	 * @return
	 */
	public static Map<String, ArrayList<String>> makeActorMovieMap(Map<Integer,String> movieMap, Map<Integer,String> actorMap){								//what kind of map should I be using....?
		System.out.println("Please select movies-actor file...");
		String pathName = setFilePath();

		Map<String, ArrayList<String>> actorMovieIDMap = new TreeMap<String, ArrayList<String>>();
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(pathName));
			String line;
			while ((line = input.readLine()) != null) {
				if (line.contains("|")) {

					String[] tokens = line.split("\\|");
					int movieID = Integer.valueOf(tokens[0]);
					int actorID = Integer.valueOf(tokens[1]);

					String movie = movieMap.get(movieID);
					String actor = actorMap.get(actorID);
					
					//add the current actor to the ArrayList for the corresponding movie
					ArrayList<String> temp = new ArrayList<String>();
					if (actorMovieIDMap.containsKey(movie)){
						temp = actorMovieIDMap.get(movie);
						temp.add(actor);
					}
					else {
						temp.add(actor);
						actorMovieIDMap.put(movie, temp);
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return actorMovieIDMap;
	}


	/**
	 * This method is a file chooser that returns the path to the
	 * selected file
	 * @return
	 */
	public static String setFilePath() {
		JFileChooser fc = new JFileChooser("."); // start at current directory

		int returnVal = fc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String pathName = file.getAbsolutePath();
			return pathName;
		}
		else
			return "";
	}

	/**
	 * This method implements the game interface.
	 * It takes a fully implemented BFS (not one in stages of being set up).
	 * @param implementedBFS
	 * @param baconGraph
	 */
	public static void runGame(BFS implementedBFS, AdjacencyListGraphMap<String, String> baconGraph) {
		System.out.println("\n" + "Ready to begin game. To quit the program, type return in answer to a question." );
		while(true){
			System.out.println("Please print an actor's name to find his/her bacon number: ");
			String actor = implementedBFS.console.nextLine();
			if(actor.equals("")) break;
			implementedBFS.findBaconNumber(actor, baconGraph);
		}
	}

	public static void main(String [] args) {
		bfsTest();

		Map<Integer, String> actorMap = makeIDMap("actors");
		Map<Integer, String> movieMap = makeIDMap("movies");
		Map<String, ArrayList<String>> actorMovieMap = makeActorMovieMap(movieMap, actorMap);
		AdjacencyListGraphMap<String, String> baconGraph = createAdjList(actorMovieMap);
		printGraph(baconGraph);
		BFS realBFS = new BFS(baconGraph);
		realBFS.implementBFS("Kevin Bacon", baconGraph);
		System.out.println("\n" + "This is the larger text file case example.");
		runGame(realBFS, baconGraph);
	}
}



