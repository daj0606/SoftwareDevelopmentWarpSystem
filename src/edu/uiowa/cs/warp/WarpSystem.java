package edu.uiowa.cs.warp;

/**
 * Warp System.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class WarpSystem implements WarpInterface {

  private Program program;
  private WorkLoad workLoad;
  private ReliabilityAnalysis ra;
  private LatencyAnalysis la;
  private ChannelAnalysis ca;
  private ExecutionAnalysis ea;
  private Options warpOptions;
  private FaultModel faultModel;

  /**
   * Constructor to create a Warp System.
   * 
   * @param warpOptions that define the system properties
   */
  public WarpSystem(Options warpOptions) {
    this(new WorkLoad(warpOptions));
  }

  /**
   * Constructor to create a Warp System from a WorkLoad object.
   * 
   * @param workLoad object that defines the flows, nodes, and edges
   */
  public WarpSystem(WorkLoad workLoad) {
    this.warpOptions = workLoad.getOptions();
    this.workLoad = workLoad;
    initializeSystem();
  }

  /**
   * Initialize the system attributes.
   */
  private void initializeSystem() {
    faultModel = warpOptions.getFaultModel();
    program = null;
    ra = null;
    la = null;
    ca = null;
    ea = null;
  }

  /** 
   * Reset the warp objects that were previously
   * created to their initial states using the 
   * current option settings.
   */
  @Override
  public void reset() {
    workLoad = new WorkLoad(warpOptions);
    initializeSystem();
  }

  /**
   * Returns the system WorkLoad object.
   */
  @Override
  public WorkLoad getWorkload() {
    return workLoad;
  }

  /**
   * Returns the program that schedules flow transmissions.
   */
  @Override
  public Program getProgram() {
    ensureProgramExists();
    return program;
  }

  /**
   * Returns the reliability analysis of the program successfully
   * transmitting each flow message from source to sink, with respect
   * to the end-to-end reliability and minimum packet reception link.
   */
  @Override
  public ReliabilityAnalysis getReliabilityAnalysis() {
    if (ra == null) {
      ra = new ReliabilityAnalysis(this);
    }
    return ra;
  }

  /**
   * Sends the warp system to the simulator.
   */
  @Override
  public SimulatorInput toSimulator() {
    return null;
  }

  /**
   * Returns the latency analysis of every flow in the system.
   */
  @Override
  public LatencyAnalysis getLatencyAnalysis() {
    if (la == null) {
      la = new LatencyAnalysis(this);
    }
    return la;
  }

  /**
   * Returns the channel analysis of every flow transmission in the system.
   */
  @Override
  public ChannelAnalysis getChannelAnalysis() {
    if (ca == null) {
      ca = new ChannelAnalysis(this);
    }
    return ca;
  }

  /**
   * Returns the execution analysis of every flow transmission in the system.
   */
  @Override
  public ExecutionAnalysis getExecutionAnalysis() {
    if (ea == null) {
      ea = new ExecutionAnalysis(this);
    }
    return ea;
  }

  /**
   * Returns True if the end-to-end reliability metrics are met every flow.
   */
  @Override
  public Boolean reliabilitiesMet() {
    if (ra == null) {
      ensureProgramExists();
      ra = new ReliabilityAnalysis(this);
    }
    return ra.verifyReliabilities();
  }

  /**
   * Returns True if all deadlines are met.
   */
  @Override
  public Boolean deadlinesMet() {
    Boolean result = true;
    ensureProgramExists();
    if (program.deadlineMisses().size() > 0) {
      result = false;
    }
    return result;
  }

  /**
   * Returns the number of communication channels available to the system.
   */
  @Override
  public Integer getNumChannels() {
    return warpOptions.getNumChannels();
  }

  /**
   * Returns the number of faults any one flow may encounter during
   * its transmissions from source to sink.
   */
  @Override
  public Integer getNumFaults() {
    return warpOptions.getNumFaults();
  }

  /**
   * Returns the minimum percentage of messages that will be transmitted successfully. This is
   * also called the minimum link quality of the wireless transmission link. This should at least
   * 0.7, with a max of 1.0.
   */
  @Override
  public Double getMinPacketReceptionRate() {
    return warpOptions.getMinPacketReceptionRate();
  }

  /**
   * Returns the end-to-end message transmission reliability required. We often want messages
   * transmitted from the flow source to the flow sink with a end-to-end reliability greater than 0.9.
   */
  @Override
  public Double getE2E() {
    return warpOptions.getE2E();
  }

  /**
   * Returns the name of the work load.
   */
  @Override
  public String getName() {
    return workLoad.getName();
  }

  /**
   * Returns the name of the scheduler that produced the program.
   */
  @Override
  public String getSchedulerName() {
    return warpOptions.getSchedulerName();
  }

  /**
   * Returns the enumerated type of scheduler that produced the program.
   */
  @Override
  public ScheduleChoices getScheduleChoice() {
    return warpOptions.getSchedulerSelected();
  }

  /**
   * Returns the warp system options.
   */
  @Override
  public Options getOptions() {
    return warpOptions;
  }

  /**
   * Send the warp system to the sensor network for execution.
   */
  @Override
  public void toSensorNetwork() {
    // TODO Auto-generated method stub

  }

  /**
   * Returns the fault model assumed when building the program.
   */
  @Override
  public FaultModel getFaultModel() {
    return faultModel;
  }

  /**
   * Called to ensure the program has been built before any analysis on it
   * is performed.
   */
  private void ensureProgramExists () {
    if (program == null) {
      program = new Program(workLoad);
    }
  }

}
