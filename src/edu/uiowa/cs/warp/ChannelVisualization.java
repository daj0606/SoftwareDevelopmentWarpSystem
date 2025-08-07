package edu.uiowa.cs.warp;

/**
 * Creates the visualizations for the channel analysis of the WARP program.
 * <p>
 * 
 * CS2820 Fall 2025 Project: Implement this class to create the file visualization that is requested
 * in Warp.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class ChannelVisualization extends Visualization {

  private static final String SOURCE_SUFFIX = ".ch";
  private static final String OBJECT_NAME = "Channel Analysis";
  private WarpInterface warp;
  private ChannelAnalysis ca;

  ChannelVisualization(WarpInterface warp) {
    super(warp, SOURCE_SUFFIX);
    this.warp = warp;
    this.ca = warp.getChannelAnalysis();
  }
}
