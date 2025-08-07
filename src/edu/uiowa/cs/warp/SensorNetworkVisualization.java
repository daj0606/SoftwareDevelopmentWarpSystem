package edu.uiowa.cs.warp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.graphstream.graph.Graph;

/**
 * Builds the visualization of the sensor network.
 * The display graph is created with GraphStream.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class SensorNetworkVisualization extends Visualization {

  private static final String TITLE = "Sensor Network";
  private static final Integer Max_OBJECTS_PER_LINE = 20;
  private WorkLoad workLoad; 
  private WarpGraph warpGraph ;

  public SensorNetworkVisualization(WorkLoad workLoad) {
    super(workLoad, ".sn");
    this.workLoad = workLoad;
    warpGraph = new WarpGraph(workLoad);
  }

  @Override
  protected GuiWindow displayVisualization() {
    Graph sensorNetwork = warpGraph.getGraph();
    String displayTitle = String.format("%s: %s",TITLE, workLoad.getName());
    GuiWindow window = createEnhancedGraphVisualization(sensorNetwork, displayTitle);
    return window;
  }

  @Override
  protected Description createFooter ( )  {
    Description footer = new Description();
    footer.add("\n");
    return footer;
  }

  @Override
  protected Description visualization ( )  {
    Description content = new Description();
    Graph graph = warpGraph.getGraph();
    EdgeMap map = warpGraph.getEdgeMap();
    /* List of node names as a String for contents */
    String nodes = "Nodes:";
    /* List of edges names as a String for contents */
    String edges = "Edges:";
    /* edge list used to create the list of edges as a String */
    ArrayList<String> list = new ArrayList<String>();

    /* 
     * Add a title line to the content 
     * This is normally the createHeader() content
     * but we will use it here and let createHeader()
     * return its default implementation
     */
    content.add(String.format("%s for %s\n",TITLE, workLoad.getName()));
    /* 
     * First create the list of nodes with at most
     * Max_OBJECTS_PER_LINE. Then create the list of
     * edges.
     */
    for (org.graphstream.graph.Node node : graph) {
      String name = (String) node.getAttribute("ui.label");
      list.add(name);
    }

    /* Add the nodes to the content */
    nodes = addNamesToLine(list,nodes);
    content.add(nodes);

    /* 
     * Create an array list of edges and then
     * sort the list so that edges are printed 
     * in order of nodes.
     */
    list = new ArrayList<String>(); // need a new list object
    for (Map.Entry<String, Edge> entry : map.entrySet()) {
      String name = entry.getKey();
      list.add(name);
    }

    /* Add the edges to the content */
    edges = addNamesToLine(list,edges);
    content.add(edges);

    return content;
  }

  /**
   * Add the list of node or edge names to a formated line to be
   * added to the contents. The line is formated so that at most
   * Max_OBJECTS_PER_LINE are displayed (i.e., a line separator
   * is added after Max_OBJECTS_PER_LINE).
   * 
   * @param list of nodes or edges in the graph
   * @param line formated to be added to the contents
   * @return
   */
  private String addNamesToLine (ArrayList<String> list, String line) {
    int objectsInLine = 0;
    int numItems = list.size();

    /* sort the list so that edges are printed in order of nodes. */
    Collections.sort(list);

    /* 
     * Now create the list of edges with at most
     * Max_OBJECTS_PER_LINE.
     */
    for (int i = 0; i < numItems; i++) {
      line += String.format(" %s", list.get(i));
      objectsInLine++;
      if (objectsInLine >= Max_OBJECTS_PER_LINE) {
        objectsInLine = 0;
        line += System.lineSeparator() + "      ";
      }
    }
    if (objectsInLine > 0) {
      /* add a final line separator if we did't just add one */
      line += System.lineSeparator();
    }
    return line;
  }
}
