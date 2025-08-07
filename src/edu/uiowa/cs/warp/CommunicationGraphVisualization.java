package edu.uiowa.cs.warp;

import org.graphstream.graph.Graph;

/**
 * Builds a visualization of the sensor network
 * as a directed graph with flow communication costs  
 * on each edge (i.e., max number of push/pull attempts 
 * to transmit the message on that edge). Each flow is 
 * represented as a set of directed edges in the sensor network.
 * The display graph is created with GraphStream.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class CommunicationGraphVisualization extends Visualization {

  private static final String TITLE = "Communication Costs";
  FileManager wfm; // Warp File Manager object
  String graphFileName; // name of the input graph file
  Boolean verbose; // verbose flag
  WorkLoad workLoad; // used access flows as the some of the output files are built

  public CommunicationGraphVisualization(WorkLoad workLoad) {
    super(workLoad, ".wf");
    this.workLoad = workLoad;
    verbose = false; // initialize verbose to false
  }

  @Override
  protected GuiWindow displayVisualization() {
    WarpGraph comCostGraph = new CommunicationCostGraph(workLoad);
    Graph graph = comCostGraph.getGraph();
    String displayTitle = String.format("%s for %s",TITLE, workLoad.getName());
    GuiWindow window =createEnhancedGraphVisualization(graph, displayTitle);
    return window;
  }

  @Override
  protected Description createHeader ( )  {
    Description header = new Description();
    header.add(String.format("WARP_%s{\n",workLoad.getName()));
    return header;
  }

  @Override
  protected Description createFooter ( )  {
    Description footer = new Description();
    footer.add("}\n");
    return footer;
  }

  @Override
  protected Description visualization ( )  {
    Description content = new Description();
    /* start the content description by adding the 1st line */
    String line = String.format(
        "// M = %s and End-to-End reliability = %s\n",
        String.valueOf(workLoad.getMinPacketReceptionRate()),
        String.valueOf(workLoad.getE2E()));
    content.add(line);

    /* Add the flows and their communication costs per link */
    var flowNames = workLoad.getFlowNames();
    for (int flowIndex = 0; flowIndex < flowNames.length; flowIndex++) {
      var flowName = flowNames[flowIndex];
      var nodesInFlow = workLoad.getNodesInFlow(flowName);
      var nNodesInFlow = (nodesInFlow.length);
      /* array of communications costs per link */
      var linkCostArray = workLoad.getNumTxAttemptsPerLink(flowName); 
      var totalCost = workLoad.getTotalTxAttemptsInFlow(flowName);
      var minCost = nNodesInFlow - 1 ;
      /*
       * nEdges in the flow is the minimum communication cost, 
       * i.e., min nTx to go E2E with no errors
       */
      line = String.format(
          "// Flow %s has a minimum communication cost of %d "
              + "and a worst-case communication cost of %d\n", 
              flowName, minCost, totalCost);
      content.add(line);
      line = String.format(
          " %s (%d,%d,%d,%d) : ", flowName, 
          workLoad.getFlowPriority(flowName), workLoad.getFlowPeriod(flowName), 
          workLoad.getFlowDeadline(flowName), workLoad.getFlowPhase(flowName)); 
      String nodeName;
      for (int i = 0; i < nNodesInFlow-1; i++) {  
        nodeName = nodesInFlow[i];
        var nTx = linkCostArray[i];
        line = String.format(line + "%s -%d-> ",nodeName,nTx); 
        /* This version of the file has nTx in the edges of the flow */
      }
      nodeName = nodesInFlow[nNodesInFlow-1];
      line = String.format(line + "%s\n",nodeName); // add last node on the flow
      content.add(line);
    }
    return content;
  }

  /**
   * Creates a directed network graph that shows the flow communication
   * cost on each edge. This class extends the WarpGraph class
   * so that the graph created is a directed graph with edges  
   * labeled with the number of maximum transmissions attempted
   * on that edge for that hop in the flow.
   * 
   * @return a graph with Flow communication costs on the edges
   */
  private class CommunicationCostGraph extends WarpGraph {
    public CommunicationCostGraph(WorkLoad workLoad) {
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
      String label = String.format("%s:%s-%d->%s",edge.getFlow(), edge.getSrc(),edge.getNumTx(),edge.getSnk());  
      return label;
    }
  }
}
