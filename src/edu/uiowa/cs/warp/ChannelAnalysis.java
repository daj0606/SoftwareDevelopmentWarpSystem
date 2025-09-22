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
  private ProgramSchedule programTable;
  private Boolean conflictExists;

  /**
   * 
   * Constructs channel analysis object using warp interface.
   * @param warp
   */
  ChannelAnalysis(WarpInterface warp) {
    this.program = warp.getProgram();
    this.programTable = program.getSchedule();
    conflictExists = false;
  }
/**
 * 
 * Checks whether there is a channel conflict.
 * @return true if conflict exists
 */
  public Boolean isChannelConflict() {
    return conflictExists;
  }
}
