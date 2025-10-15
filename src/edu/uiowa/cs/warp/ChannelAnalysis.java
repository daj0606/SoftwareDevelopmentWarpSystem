package edu.uiowa.cs.warp;

/**
 * Performs a channel analysis of the WARP program.
 * <p>
 * 
 * CS2820 Fall 2025 Project: Implement this class to analyze the use of channels
 * by the Warp program. A channel analysis table is created that shows the nodes
 * and flows using each channel in each time slot.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class ChannelAnalysis {
	private Program program; // Represent the current WARP program.
	private ProgramSchedule programTable; // Schedule table which maps time slots and channels to transmissions.
	private Boolean conflictExists; // Flag that indicates whether any channel conflict exists.

	/**
	 * Constructs a new ChannelAnalysis object for the given WARP system.
	 * 
	 * @param warp the WARP system containing the program and its schedule
	 */
	ChannelAnalysis(WarpInterface warp) {
		this.program = warp.getProgram();
		this.programTable = program.getSchedule();
		conflictExists = false;
	}

	/**
	 * Reports whether the analysis has detected a channel conflict.
	 * 
	 * @return true if a conflict has been found, false otherwise
	 */
	public Boolean isChannelConflict() {
		return conflictExists;
	}
	
	public ProgramSchedule getChannelAnalysisTable() {
	      return programTable;
	}
}
