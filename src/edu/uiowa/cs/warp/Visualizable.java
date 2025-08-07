package edu.uiowa.cs.warp;

import java.io.File;

/**
 * Interface for visualizable objects.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public interface Visualizable {

  /** Enumeration class defining the types of Warp system visualizations possible. */
  public enum SystemVisualizations {
    SOURCE, RELIABILITIES, SIMULATOR_INPUT, LATENCY, LATENCY_REPORT, DEADLINE_REPORT, CHANNEL, EXECUTION
  }

  /** Enumeration class defining the types of Warp workload visualizations possible. */
  public enum WorkLoadVisualizations {
    INPUT_GRAPH, COMUNICATION_GRAPH, GRAPHVIZ, NETWORK
  }

  /**
   * Presents the visualization on the display using a graphical 
   * window. 
   */
  public GuiWindow toDisplay();

  /** Returns the window associated with the display window. */
  public GuiWindow getDisplay();

  /** Writes the visualization to a file. */
  public File toFile();

  /** Returns the file containing the visualization. */
  public File getFile();

  /** Returns a String representing the visualization. */
  public String toString();
}
