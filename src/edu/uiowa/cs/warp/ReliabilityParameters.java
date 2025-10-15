package edu.uiowa.cs.warp;

/**
 *  Warp Reliability Attributes interface
 *  
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
interface ReliabilityParameters {

  public enum FaultModel() {
    FIXED, PROBABILISTIC
  }
  
	/**
	 * @return the minPacketReceptionRate
	 */
	public Double getMinPacketReceptionRate();

	/**
	 * @return the end-to-end reliability requirement
	 */
	public Double getE2E();
	
	/**
	 * @return number of faults tolerated
	 */
	public Integer getNumFaults();
	
	/**
	 * @return fault model type
	 */
	public FaultModel getFaultModel();
}
