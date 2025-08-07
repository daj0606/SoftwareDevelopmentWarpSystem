package edu.uiowa.cs.warp;

import java.util.Collection;
import java.util.Map;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

/**
 * Builds a basic network graph and edge set that can be used for
 * simulations and visualizations. The flows, nodes, and edges 
 * are defined in a WorkLoad object. 
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class WarpGraph {

	/**
	 * CSS Style definition for nodes. This style 
	 * defines nodes to be green circles.
	 */
	public static final String NODE_CSS_STYLE = "node { " +
            "   shape: circle; " +
            "text-size: 30; " + // Set the font size to 30 points
            "   size: 50px; " +
            "   fill-color: green; " +
            "   text-alignment: center; " +
            "}";
	
	/**
	 * CSS Style definition for edge labels. This style 
	 * removes the icon associated with the edge, so
	 * only the text is visible.
	 */
	public static final String EDGE_LABEL_CSS_STYLE = 
            "shape: box; " +
            " text-size: 20; " + 
            " fill-mode: none; " +
            " stroke-mode: none; " +
            " text-alignment: center; " ;
	
	/**
	 * WorkLoad object used to build the network graph.
	 */
	WorkLoad workLoad;
	
	/**
	 * True if a directed graph is to be created. False means an 
	 * undirected graph will be created.
	 */
	Boolean directed;
	
	/**
	 * A map of Edges with the key being the label that
	 * can be used on the edges of the network graph created.
	 */
	EdgeMap edgeMap;
	
	/**
	 * Network graph created.
	 */
	Graph basicGraph;
	
	/**
	 * Constructor to create an undirected network graph. Only nodes are 
	 * labeled. The graph can be attached to a GuiWindow. It can also be 
	 * displayed in its own Swing window with graph.display().
	 * 
	 * @param workLoad
	 * @param directed
	 */
	public WarpGraph(WorkLoad workLoad) {
		this(workLoad, false);
	}
	
	/**
	 * Constructor to create a directed network graph. Only nodes are 
	 * labeled. The graph can be attached to a GuiWindow. It can also be 
	 * displayed in its own Swing window with graph.display().
	 * 
	 * @param workLoad
	 * @param directed
	 */
	public WarpGraph(WorkLoad workLoad, Boolean directed) {
		this.workLoad = workLoad;
		this.directed = directed;
	}
	
	
	
//	 @Override
//	  protected GuiWindow displayVisualization() {
////		 GuiWindow window = new GuiWindow(workLoad.getOptions(), DISPLAY_TITLE, visualization());
//		 Graph graph = createGraphVisualization();
//		 Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//		 View view = viewer.addDefaultView(false);   // false indicates "no JFrame".
//		 GuiWindow window = new GuiWindow(workLoad.getOptions(), DISPLAY_TITLE, visualization(), (JPanel)view);
//		 viewer.enableAutoLayout();
////		 Graph flowGraph =createFlowGraph();
////		 sensorNetwork.display();
////		 flowGraph.display();
//		 return window;
//	  }
	 
	public Graph getGraph() {
		if (basicGraph == null) {
			basicGraph = createNetworkGraph();
		}
		return basicGraph;
	}
	
	public EdgeMap getEdgeMap() {
		if (edgeMap == null) {
			edgeMap = createEdgeMap(directed);
		}
		return edgeMap;
	}
	
	/** 
	 * Created a basic network graph that can
	 * be displayed in a GuiWindow or in its own
	 * Java Swing frame.
	 * 
	 * @return a sensor network graph
	 */
	private Graph createNetworkGraph() {
		/* Create the edges and a map from name to edge object */
		edgeMap = createEdgeMap(directed);
		/* Create a basic sensor network graph using the
		 * the name of the Warp workload graph file 
		 * and default node style.
		 */
		Graph graph = new MultiGraph(workLoad.getName());
		graph.setAttribute("ui.stylesheet", NODE_CSS_STYLE);

		/* Get the names of the nodes in the workload and
		 * create a graph node for each, setting its label
		 * to the node's name in the workload.
		 */
		var nodeNames = workLoad.getNodeNamesOrderedAlphabetically();
		for (String nodeName : nodeNames) {
			org.graphstream.graph.Node graphNode = graph.addNode(nodeName);
			graphNode.setAttribute("ui.label", nodeName);
		}
		
        /* Add the graph edges to the sensor network */
		for (Map.Entry<String, Edge> entry : edgeMap.entrySet()) {
            String name = entry.getKey();
            Edge edge = entry.getValue(); 
            graph.addEdge(name, edge.getSrc(), edge.getSnk(), directed); 
		}	
		/* Use the Swing UI for displaying this graph 
		 * The graph can be displayed in its own frame or
		 * in a GuiWindow
		 * */
		System.setProperty("org.graphstream.ui", "swing"); 
		return graph;
	}
		
	/** 
	 * Add a work load edge to the EdgeMap. If directed is true, 
	 * the name key includes the flow, src, and snk nodes.
	 * If not, the name key is (node1, node2) where the src and
	 * snk are sorted lexicographically.
	 * 
	 * @param map to which the edge is added
	 * @param edge to be added to the map
	 * @param directed indicates if the graph is directed or undirected
	 */
//	private void addGraphEdge(EdgeMap map, Edge edge, Boolean directed) {
//		String name;
//		String src = edge.getSrc();
//		String snk = edge.getSnk();
//		if (directed) {
////			name = String.format("%s:%s-%d->%s",edge.getFlow(),src,edge.getNumTx(),snk);
//			name = String.format("%s:%s->%s",edge.getFlow(),src,snk);
//		} else {
//			/* undirected edges requested, so create a new edge without
//			 * flow information in the name and make the src and snk be
//			 * the same for either direction.
//			 */
//			if (src.compareTo(snk) > 0) {
//				name = String.format("(%s,%s)",snk,src);
//				edge = new Edge(edge.getFlow(),
//						snk, src,  edge.getPriority(), 
//						edge.getPeriod(), edge.getDeadline(), edge.getPhase(), edge.getNumTx());
//			} else {
//				// name = src + ":" + snk;
//				name = String.format("(%s,%s)",src,snk);
//				edge = new Edge(edge.getFlow(),
//						src, snk,  edge.getPriority(), 
//						edge.getPeriod(), edge.getDeadline(), edge.getPhase(), edge.getNumTx());
//			}	
//		}
//		if (!map.containsKey(name)) {
//			map.put(name,edge);
//		}
//	}
	
	private String getEdgeLabelForMap(EdgeMap map, Edge edge, Boolean directed) {
		String name;
		if (directed) {
			name = String.format("%s:%s->%s",edge.getFlow(),edge.getSrc(),edge.getSnk()); 
		} else {
			name = String.format("(%s,%s)",edge.getSrc(),edge.getSnk());
		}
		return name;
	}
	
	/**
	 * Create a map of Edges with the key being the label that
	 * can be used on the edges of the basic sensor network.
	 * 
	 * @param directed is true for directed graph and false for undirected
	 * @return the map of edge names to edge objects
	 */
	private EdgeMap createEdgeMap(Boolean directed) {
		/* reinitalize the map */
		EdgeMap map = new EdgeMap();
		/* Get the flows in the workload and then loop through
		 * the nodes in each flow, creating an Edge object and
		 * adding it to the map.
		 */
		Collection<Flow> flows = workLoad.getFlows().values();
		var maxFlowLength = workLoad.maxFlowLength();
		for (Flow flow: flows) {
			/* Extract the real-time properties from the flow so that
			 * they can be stored with the edge.
			 */
			var flowNodes = flow.getNodes();
			var numTxArray = workLoad.getNumTxAttemptsPerLink(flow.getName());
			var flowPhase = flow.getPhase();
			var flowPriority = flow.getPriority();
			var period = flow.getPeriod();
			var deadline = flow.getDeadline();
//			Edge edge = null;
			for (Integer i = 0; i < flowNodes.size() -1 ; i++) {
				/* Create edges, setting priority, release time. etc/ */
				var srcNode = flowNodes.get(i);  // node in the flow
				var snkNode = flowNodes.get(i+1);
				var src = srcNode.getName();
				var snk = snkNode.getName();
				/*
				 * Edge priority within a flow is based on position
				 * in flow. Each Flow has its own priority. This 
				 * creates a 2-dimensional priority. Thus, we
				 * can keep a total ordering of edges if we fold the
				 * table into a single row where we assume each new flow
				 * starts maxFlowSize away from the previous flow nodes:
				 * edgePriority = flowPriority*maxFlowLength + edgePosition
				 */
				var priority = (flowPriority * maxFlowLength) + i;
				var phase = flowPhase + i; 
				var numTx = numTxArray[i];
				/* the name associated with the Edge is the flow name */
				Edge edge =  createEdge(flow.getName(),src,snk,priority, 
						period, deadline, phase, numTx);
				/* Add the edge to map */
				String name = getEdgeLabelForMap(map, edge, directed); 
				if (!map.containsKey(name)) {
					map.put(name,edge);
				}
			}
		}
		return map;
	}
	
	 /**
	   * Create a directed network graph that shows the flows.
	   * 
	   * @return a graph with communication costs on the edges
	   */
	  public Graph addEdgeLabels () {
	    /* First create a directed network graph using
	     * WarpGraph. Then retrieve the Edge map and
	     * add edge labels to the graph.
	     */
	    Graph graph = getGraph();
	    EdgeMap map = getEdgeMap();

	    /* Create a GraphStream sprite manager to add edge 
	     * labels as sprites to the graph.
	     */
	    SpriteManager spriteManager = new SpriteManager(graph);
	    Sprite edgeLabelSprite = null;

	    /* add the edges to the graph */
	    for (Map.Entry<String, Edge> entry : map.entrySet()) {
	      String name = entry.getKey();
	      Edge edge = entry.getValue();
	      String label = buildDirectedEdgeLabel(edge);

	      /* Create a sprite and attach it to the edge */
	      edgeLabelSprite = spriteManager.addSprite(name);
	      edgeLabelSprite.attachToEdge(name);

	      /* Set the position of the sprite relative to the edge
	       * Here, 0.5 places it in the middle and 0.0 puts it on the edge
	       */
	      edgeLabelSprite.setPosition(0.5, 0.0, 0); 

	      /* Set the label for the sprite */
	      edgeLabelSprite.setAttribute("ui.label", label);
	      edgeLabelSprite.setAttribute("ui.style", WarpGraph.EDGE_LABEL_CSS_STYLE);
	    }		
	    return graph;
	  }
	  
	  private Edge createEdge (String name, String src, String snk, Integer priority,
			  Integer period, Integer deadline, Integer phase, Integer numTx) {
		String newSrc = src;
		String newSnk = snk;
//		String name;
		Edge newEdge;
		/* put nodes listed in lexicographical order if graph is not directed */
		if (!directed && src.compareTo(snk) > 0) {
//		  name = String.format("(%s,%s)",snk,src);
			/* Swap src and snk */
			newSrc = snk;
			newSnk = src;
		} 
		newEdge = new Edge(name,newSrc, newSnk,priority, period, deadline, phase, numTx);
		return newEdge;
	  }
	  
	  protected String buildDirectedEdgeLabel(Edge edge) {
		String label = String.format("%s:%s->%s",edge.getFlow(),edge.getSrc(),edge.getSnk()); 
	    return label;
	  }
	  
	  protected String buildEdgeLabel(Edge edge) {
		String label = String.format("(%s,%s)",edge.getSrc(),edge.getSnk());
		return label;
	  }
}
