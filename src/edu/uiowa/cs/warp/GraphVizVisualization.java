package edu.uiowa.cs.warp;

import org.graphstream.graph.Graph;

/**
 * Builds a visualization of the sensor network
 * as a directed graph. Each flow is represented
 * as a set of directed edges in the sensor network.
 * The .gv file is a GraphViz formated file that can
 * be used with the GraphViz tools. The display graph
 * is created with GraphStream.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class GraphVizVisualization extends Visualization {

  /* 
   * The gv (GraphViz) file has the format:
   * 
	 strict digraph InputFileBaseName {
	     C  -> B  -> A;
	 labelloc  =  "t"
	 label = <Graph InputFileBaseName <br/>
	 Flow F0: C -&#62; B -&#62; A<br/>
	 >
	 }
   *
   */

  private static final String GRAPH_VIZ_SUFFIX = ".gv";
  private static final String TITLE = "WorkLoad as Di-Graph";

  private String gvFileContents; // contents of Graph Viz File to be created
  private String gvTitleCaption; // Title Caption to be merged with gvFileContents
  private String graphFile; 
  private String graphName;
  private Boolean verbose; // verbose flag
  private GraphVizVisualization gv;
  WorkLoad workLoad; // used access flows as the some of the output files are built
  private String legend;

  /**
   * Creates a GraphViz representation of the Warp WorkLoad. 
   * 
   * @param options for the Warp system
   * @param graphFile describing the input workload
   */
  GraphVizVisualization(Options options, String graphFile) {
    super(options, GRAPH_VIZ_SUFFIX); // Visualization constructor
    /* clear the name extension set by the parent constructor.
     * We want no extension this visualization because it only shows the
     * workload and structure of the sensor network as a di-graph.
     */
    setNameExtension(EMPTY);
    this.graphFile = graphFile;
    this.gv = this;
    verbose = false;
    workLoad = new WorkLoad(options);
    createGraphVizContent();
  }

  @Override
  protected Description visualization() {
    return new Description(getGraphVizContent());
  }

  @Override
  protected GuiWindow displayVisualization() {
    WarpGraph flowDiGraph = new FlowDiGraph(workLoad);
    Graph digraph = flowDiGraph.getGraph();
    String displayTitle = String.format("%s: %s",TITLE, graphName);
    GuiWindow window = createEnhancedGraphVisualization(digraph, displayTitle);
    return window;
  }

  private String createFirstLine(String graphName) {
    return String.format("strict digraph %s {\n",graphName);
  }

  public String getGraphFile () {
    return graphFile;
  }

  public void initializeGraphVizContent (String graphName)  {
    /* Initialize the Legend with a begin comment */
    legend = String.format("/* \n * Graph %s\n", graphName); 
    /* start the gv file by adding the 1st line to the contents string */
    this.graphName = graphName;
    gvFileContents = createFirstLine(graphName);
    /* remove 'strict' if multiple edges in the same direction are to be shown */
    gvTitleCaption = "labelloc  =  \"t\" \n"; // Place the rentered graph's title on top.
    /* 
     * create a title caption for the rendered graph in the .gv file
     * label string is in html format, so need an end delimeter after adding flows
     */
    gvTitleCaption = String.format(gvTitleCaption + "label = <Graph %s <br/>\n",graphName);
  }

  public void finalizeGraphVizContent ( )  {
    /* Finalize the Legend with an end comment */
    legend += " */ \n";
    /* 
     * wrap up the gv file content by finalizing title caption
     * then add it to the file contents and terminate the graph viz structure
     */
    gvTitleCaption += ">\n";
    /* 
     * end the gv file by adding the caption for rendering and the last line 
     * to the contents string
     */
    gvFileContents = legend + gvFileContents + gvTitleCaption + "} \n";  
  }

  /* 
   * private function to return the string gvFileContents.
   * designed to be called after finalizeGraphVizContent() is called by the listener
   * but will return the current state of the contents string anytime it is called
   */
  private String getGraphVizContent ( )  {
    return gvFileContents;
  }

  private String createGraphVizContent ( ) {
    /*
     * GraphVizListener reads the graphFile string and calls the functions
     * in this class, as it walks the input graphFile structure to build
     * the GraphViz contents.
     */
    GraphVizListener.buildGraphViz(gv); 
    if (verbose) {
      System.out.println("************************************");
      System.out.println("GraphViz File Contents:");
      System.out.println(getGraphVizContent());
      System.out.println("************************************\n");
    }
    return getGraphVizContent();
  }

  public void addFlowToGraphViz (String flowName)  {
    /* add a 3 spaces before the start of the chain defining this flow to the gv contents string */
    String flow = String.format("Flow %s: ", flowName);
    gvFileContents = gvFileContents + "   ";  
    gvTitleCaption = gvTitleCaption + flow; 
    /* Add a comment '*' for formatting and start the Flow in the legend */
    addToLegend(" *    " + flow);
  }

  public void addSrcNodeToGraphViz (String nodeName)  {
    String fileContents = String.format(" %s  ->",nodeName);
    String titleCaption = String.format("%s -&#62; ",nodeName);
    addNodeName(fileContents,titleCaption);
  }

  public void addSnkNodeToGraphViz (String nodeName)  {
    String fileContents = String.format(" %s",nodeName);
    String titleCaption = String.format("%s",nodeName);
    addNodeName(fileContents,titleCaption);
  }

  public void finalizeCurrentFlowInGraphViz ( )  {
    /* add the ';' and '\n' to end this line of the chain defined in this flow */
    gvFileContents = gvFileContents + "; \n";
    gvTitleCaption += "<br/>\n"; // terminate this line of the rendered graph caption
    /* add a newline */
    addToLegend("\n"); 
  }

  private void addNodeName(String fileContents, String titleCaption) {
    /* add the node name to the gv file contents string */
    gvFileContents += fileContents;  
    /* add the node name and edge to this line of the rendered graph caption */
    gvTitleCaption += titleCaption;
    addToLegend(fileContents);
  }

  private void addToLegend(String nextEntry) {
    legend = legend + nextEntry;
  }

  /**
   * Creates a directed network graph that shows the flows. This
   * class extends the WarpGraph class so that the graph created
   * is a directed graph with edges labeled with the name of the
   * flow using that edge.
   * 
   * @return a graph with Flow names on the edges
   */
  private class FlowDiGraph extends WarpGraph {
    public FlowDiGraph(WorkLoad workLoad) {
      super(workLoad,true);
      addEdgeLabels();
    }

    /**
     * Set the edge label to the flow using that edge. This method
     * overrides the parent method that defines the label for a
     * directed edge.
     * 
     * @return the flow name as the edge label
     */
    @Override
    protected String buildDirectedEdgeLabel(Edge edge) {
      String label = String.format("%s",edge.getFlow()); 
      return label;
    }
  }
}
