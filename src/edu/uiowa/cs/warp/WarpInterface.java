package edu.uiowa.cs.warp;

/**
 * @author sgoddard
 * @version 2.0 Spring 2025
 */
public interface WarpInterface extends SystemAttributes {

  /** 
   * Reset the warp objects that were previously
   * created to be null.
   */
  public void reset();
  
  public WorkLoad getWorkload();

  public Program getProgram();

  public ReliabilityAnalysis getReliabilityAnalysis();

  public SimulatorInput toSimulator(); // exports code for the simulator

  public LatencyAnalysis getLatencyAnalysis();

  public ChannelAnalysis getChannelAnalysis();

  public ExecutionAnalysis getExecutionAnalysis();
  
  public void toSensorNetwork(); // deploys code to the network

  public Boolean reliabilitiesMet();

  public Boolean deadlinesMet();
  
  public ScheduleChoices getScheduleChoice();
  
  public Integer getNumChannels();

  public Integer getNumFaults();

  public Double getMinPacketReceptionRate();

  public Double getE2E();

  public String getSchedulerName();

  public Options getOptions();

}
