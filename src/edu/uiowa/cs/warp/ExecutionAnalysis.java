package edu.uiowa.cs.warp;

/**
 * ExecutionAnalysis records the execution in active time slots for each
 * release of each flow for the WARP system. A correct schedule will have
 * a COMPLETE recorded at or before the absolute DEADLINE for each flow 
 * instance released. The missed deadline will be reported in the Latency
 * Report, which in created by another class.
 * 
 * CS2820 Spring 2025 Project: Implement this class to create the 
 * execution table.
 * 
 * @author sgoddard
 * @version 1.10 Spring 2025
 */
public class ExecutionAnalysis {

 
  private ProgramSchedule executionTable;
  

  ExecutionAnalysis(WarpInterface warp) {
	// TODO Auto-generated constructor stub
  }
  
  ExecutionAnalysis(Program program) {
	// TODO Auto-generated constructor stub
  }

  /**
   * Returns the executionTable, once it is implemented in the final project.
   * 
   * @return
   */
  public ProgramSchedule getExecutionTable() {
	// TODO implement this method stub
	  executionTable = new ProgramSchedule();
    return executionTable;
  }

}
