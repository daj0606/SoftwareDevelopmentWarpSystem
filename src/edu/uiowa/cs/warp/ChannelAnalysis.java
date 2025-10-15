package edu.uiowa.cs.warp;

/**
 * Performs a channel analysis of the WARP program.
 * <p>
 * CS2820 Fall 2025 Project: Implement this class to analyze the use of channels
 * by the Warp program. A channel analysis table is created that shows the nodes
 * and flows using each channel in each time slot.
 *
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class ChannelAnalysis {

  /** Represents the current WARP program. */
  private Program program;

  /** Schedule table which maps time slots and channels to transmissions. */
  private ProgramSchedule programTable;

  /** Flag that indicates whether any channel conflict exists. */
  private Boolean conflictExists;

  /**
   * Constructs a new ChannelAnalysis object for the given WARP system.
   *
   * @param warp the WARP system containing the program and its schedule
   */
  ChannelAnalysis(WarpInterface warp) {
    this.program = warp.getProgram();
    this.programTable = program.getSchedule();
    this.conflictExists = false;
  }

  /** Returns the analysis table derived from the program schedule. */
  public ProgramSchedule getChannelAnalysisTable() {
    return programTable;
  }

  /**
   * Reports whether the analysis has detected a channel conflict.
   *
   * @return true if a conflict has been found, false otherwise
   */
  public Boolean isChannelConflict() {
    return conflictExists;
  }
}
