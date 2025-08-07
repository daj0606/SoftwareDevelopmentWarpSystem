package edu.uiowa.cs.warp;

/**
 * ExecutionVisualization creates the visualizations for the execution analysis of
 * the WARP program.
 * <p>
 * 
 * CS2820 Spring 2025 Project: Implement this class to create the file
 * visualization that is requested in Warp.
 * 
 * @author sgoddard
 * @version 1.10 Spring 2025
 *
 */
public class ExecutionVisualization extends Visualization {

  private static final String SOURCE_SUFFIX = ".ea";
  private static final String OBJECT_NAME = "Execution Analysis";
  private ExecutionAnalysis ea;
  private Program program;
  private WorkLoad workload;

  ExecutionVisualization(WarpInterface warp) {
    super(warp, SOURCE_SUFFIX);
    this.program = warp.getProgram();
    this.workload = warp.getWorkload();
    this.ea = warp.getExecutionAnalysis();
  }

}
