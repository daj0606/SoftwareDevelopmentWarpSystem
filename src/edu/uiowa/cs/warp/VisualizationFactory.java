package edu.uiowa.cs.warp;

import edu.uiowa.cs.warp.Visualizable.SystemVisualizations;
import edu.uiowa.cs.warp.Visualizable.WorkLoadVisualizations;

/**
 * Factory Class to create a visualization instance.
 * This factory can be used to create different
 * types of visualizations.
 *
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class VisualizationFactory {

  /** 
   * Create requested workload file and display visualizations.  
   * 
   * @param workload from which file and display visualizations will be created
   * @param choice visualization requested
   * @return viz visualization created
   */
  public static Visualizable createVisualization(WorkLoad workload, WorkLoadVisualizations choice) {
    Visualization viz = null;
    if (workload != null) {
      Options options = workload.getOptions();
      viz = createWorkLoadVisualization(workload, choice);
      executeVisualizations(viz);
      if (viz != null && options.verboseMode()) {
        System.out.println(viz.toString());
      }
    }
    return viz;
  }

  /** 
   * Create requested Warp system file and display visualizations.  
   * 
   * @param warp system from which file and display visualizations will be created
   * @param choice visualization requested
   * @return viz visualization created
   */
  public static Visualizable createVisualization(WarpInterface warp, SystemVisualizations choice) {
    Visualization viz = null;
    if (warp != null) {
      viz = createSystemVisualization(warp, choice);
      executeVisualizations(viz);
    }
    return viz;
  }	 

  /** 
   * Create multiple visualizations for the Warp System, as specified by the Options object. 
   * 
   * @param warp System for which the visualizations will be created
   * @return viz The last visualization created for this system
   */
  public static Visualizable createWarpVisualizations(WarpInterface warp) {
    Visualizable viz = null;
    if (warp != null) {
      Options warpOptions = warp.getOptions();
      if (warpOptions.allRequested()) {
        /* Create all visualizations for the Warp System, including the WorkLoad visualizations */
        for (WorkLoadVisualizations choice : WorkLoadVisualizations.values()) {
          viz = createVisualization(warp.getWorkload(), choice); // visualize all System choices
        }
        for (SystemVisualizations choice : SystemVisualizations.values()) {
          viz = createVisualization(warp, choice); // visualize all System choices
        }
      } else {      
        /* Always create the visualizations for the input graph and Warp Program source code (DSL) 
         * Followed by the other requested visualizations
         * */
        viz = createVisualization(warp.getWorkload(), WorkLoadVisualizations.INPUT_GRAPH);
        viz = createVisualization(warp, SystemVisualizations.SOURCE);
        /* Create other visualizations as specified by the Options object */
        if (warpOptions.wfRequested()) {
          viz = createVisualization(warp.getWorkload(), WorkLoadVisualizations.COMUNICATION_GRAPH);
        }
        if (warpOptions.gvRequested()) {
          viz = createVisualization(warp.getWorkload(), WorkLoadVisualizations.GRAPHVIZ);
        }

        if (warpOptions.caRequested()) {
          viz = createVisualization(warp, SystemVisualizations.CHANNEL);
        }
        if (warpOptions.laRequested()) {
          viz = createVisualization(warp, SystemVisualizations.LATENCY);
        }
        if (warpOptions.eaRequested()) {
          viz = createVisualization(warp, SystemVisualizations.EXECUTION);
        }
        if (warpOptions.latencyRequested() || warpOptions.laRequested() || warpOptions.eaRequested()) {
          viz = createVisualization(warp, SystemVisualizations.LATENCY_REPORT);
        }
        if (warpOptions.raRequested()) {
          viz = createVisualization(warp, SystemVisualizations.RELIABILITIES);
        }
      }

    }
    return viz;
  }

  /**
   * Generates output for the visualization by writing it to a file,
   * displays the visualization if the Options object requests display 
   * 
   * @param viz A WorkLoad or Warp visualization 
   */
  private static void executeVisualizations(Visualization viz) {
    if (viz != null) {
      Options options = viz.getOptions();
      viz.toFile();   
      if (options.displayRequested()) { 
        viz.toDisplay();
      }
    }
  }

  /**
   * Creates and returns a visualization for the given workload based on the specified visualization type
   * 
   * @param workload the workload to visualization
   * @param choice the visualization type to create 
   * @return a visualization object corresponding to the given choice and workload.	
   */
  private static Visualization createWorkLoadVisualization(WorkLoad workload, WorkLoadVisualizations choice) {
    Visualization viz = null;
    /* create the requested visualization */
    switch (choice) { 
    case COMUNICATION_GRAPH:
      viz = (new CommunicationGraphVisualization(workload));
      break;

    case GRAPHVIZ:
      viz = (new GraphVizVisualization(workload.getOptions(), workload.getInputGraph()));
      break;

    case INPUT_GRAPH:
      viz = (new WorkLoadVisualization(workload.getOptions()));
      break;

    case NETWORK:
      viz = (new SensorNetworkVisualization(workload));
      break;

    default:
      viz = (new NotImplentedVisualization(workload.getOptions(),"UnexpectedChoice"));
      break;
    }
    /* return the requested visualization */
    return viz;
  }

  /**
   * Creates and returns a visualization for the given warp system and specified visualization type
   * 
   * @param warp the warp system to visualize
   * @param choice the visualization type to create
   * @return a visualization object corresponding to the given choice and warp system
   */
  private static Visualization createSystemVisualization(WarpInterface warp, SystemVisualizations choice) {
    Visualization viz = null;
    switch (choice) { // select the requested visualization
    case SOURCE:
      viz = new ProgramVisualization(warp);
      break;

    case RELIABILITIES:
      viz = new ReliabilityVisualization(warp);
      break;

    case SIMULATOR_INPUT:
      // TODO Implement Simulator Input Visualization
      viz = new NotImplentedVisualization(warp.getOptions(),"SimInputNotImplemented");
      break;

    case LATENCY:
      viz = (new LatencyVisualization(warp));
      break;

    case CHANNEL:
      viz = (new ChannelVisualization(warp));
      break;

    case EXECUTION:
      viz = (new ExecutionVisualization(warp));
      break;

    case LATENCY_REPORT:
      viz = (new ReportVisualization(warp,
          new LatencyAnalysis(warp).latencyReport(), "Latency"));
      break;

    case DEADLINE_REPORT:
      viz = 
      new ReportVisualization(warp, warp.getProgram().deadlineMisses(), "DeadlineMisses");
      break;

    default:
      viz = (new NotImplentedVisualization(warp.getOptions(),"UnexpectedChoice"));
      break;
    }
    return viz;
  }
}

