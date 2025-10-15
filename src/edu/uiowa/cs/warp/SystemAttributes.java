package edu.uiowa.cs.warp;

/**
 * Warp System Attributes interface.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
interface SystemAttributes extends ReliabilityParameters{

  public enum ScheduleChoices {
    PRIORITY("Priority"), RM("RateMonotonic"), DM("DeadlineMonotonic"), RTHART("RealtimeHART"), 
    POSET_PRIORITY("Poset"), POSET_RM("PosetRM"), POSET_DM("PosetDM"),
    WARP_POSET_PRIORITY("WarpPoset"), WARP_POSET_RM("WarpPosetRM"), WARP_POSET_DM("WarpPosetDM"),
    CONNECTIVITY_POSET_PRIORITY("ConnectivityPoset"), CONNECTIVITY_POSET_RM("ConnectivityPosetRM"), 
    CONNECTIVITY_POSET_DM("ConnectivityPosetDM"); //,
    //	    CONNECTIVITY_POSET_PREEMPTIVE_PRIORITY, 
    //	    CONNECTIVITY_POSET_PREEMPTIVE_RM,
    //	    CONNECTIVITY_POSET_PREEMPTIVE_DM	

    ScheduleChoices(String description) {
      this.description = description;
    }

    @Override
    public String toString() {
      return description;
    }
  }

  public Integer getNumChannels();
  public String getName();
  public String getSchedulerName();
  public Options getOptions ();
}
