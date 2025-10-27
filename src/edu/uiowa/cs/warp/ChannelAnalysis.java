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
<<<<<<< HEAD

  /** Represents the current WARP program. */
=======
   
>>>>>>> branch 'HW5' of https://research-git.uiowa.edu/smukunza/cs2820-smukunza
  private Program program;
<<<<<<< HEAD

  /** Schedule table which maps time slots and channels to transmissions. */
=======
  
  /**
   * Schedule of the program, used for channel conflicts.
   */
>>>>>>> branch 'HW5' of https://research-git.uiowa.edu/smukunza/cs2820-smukunza
  private ProgramSchedule programTable;
<<<<<<< HEAD

  /** Flag that indicates whether any channel conflict exists. */
=======
  
  /**
   * Flag indicating whether a channel conflict exists.
   * Initially set to {@code false} until analysis determines otherwise.
   */
>>>>>>> branch 'HW5' of https://research-git.uiowa.edu/smukunza/cs2820-smukunza
  private Boolean conflictExists;

  /**
<<<<<<< HEAD
   * Constructs a new ChannelAnalysis object for the given WARP system.
   *
   * @param warp the WARP system containing the program and its schedule
=======
   * Constructs a {@code ChannelAnalysis} object using the given warp interface.
   * <p>
   * The constructor initializes the program and its schedule from the provided
   * {@code WarpInterface}, and sets the conflict flag to {@code false}.
   * </p>
   *
   * @param warp the {@code WarpInterface} used to retrieve the program
   *             and its associated schedule
>>>>>>> branch 'HW5' of https://research-git.uiowa.edu/smukunza/cs2820-smukunza
   */
  ChannelAnalysis(WarpInterface warp) {
    this.program = warp.getProgram();
    this.programTable = program.getSchedule();
    this.conflictExists = false;
  }
<<<<<<< HEAD

  /** Returns the analysis table derived from the program schedule. */
  public ProgramSchedule getChannelAnalysisTable() {
    return programTable;
  }

  /**
   * Reports whether the analysis has detected a channel conflict.
   *
   * @return true if a conflict has been found, false otherwise
   */
=======
/**
 * Checks whether a channel conflict exists.
 * 
 * @return {@code true} if a conflict exists; {@code false} otherwise
 */
>>>>>>> branch 'HW5' of https://research-git.uiowa.edu/smukunza/cs2820-smukunza
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
