package edu.uiowa.cs.warp;

/**
 * Performs a channel analysis of the WARP program.
 * <p>
 * 
 * CS2820 Fall 2025 Project: Implement this class to analyze the use of
 * channels by the Warp program. A channel analysis table is created that
 * shows the nodes and flows using each channel in each time slot.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class ChannelAnalysis {
   
  private Program program;
  
  /**
   * Schedule of the program, used for channel conflicts.
   */
  private ProgramSchedule programTable;
  
  /**
   * Flag indicating whether a channel conflict exists.
   * Initially set to {@code false} until analysis determines otherwise.
   */
  private Boolean conflictExists;

  /**
   * Constructs a {@code ChannelAnalysis} object using the given warp interface.
   * <p>
   * The constructor initializes the program and its schedule from the provided
   * {@code WarpInterface}, and sets the conflict flag to {@code false}.
   * </p>
   *
   * @param warp the {@code WarpInterface} used to retrieve the program
   *             and its associated schedule
   */
  ChannelAnalysis(WarpInterface warp) {
    this.program = warp.getProgram();
    this.programTable = program.getSchedule();
    conflictExists = false;
  }
/**
 * Checks whether a channel conflict exists.
 * 
 * @return {@code true} if a conflict exists; {@code false} otherwise
 */
  public Boolean isChannelConflict() {
    return conflictExists;
  }
  
  /**
   * Program obtained from warp interface.
   */
public ProgramSchedule getChannelAnalysisTable() {
      // TODO implement this operation
      throw new UnsupportedOperationException("not implemented");
   }
}
