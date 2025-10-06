package edu.uiowa.cs.warp;


import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author sgoddard
 * @version 1.4
 *
 */
public class Channels {


  private class ChannelSet extends HashSet<String> {
    private static final long serialVersionUID = 6725256944325470867L;

    // default constructor
    private ChannelSet() {
      super();
    }

    private ChannelSet(Integer nChannels) {
      super();
      for (int i = 0; i < nChannels; i++) { // ASSUMES channels range from 0 to nChannels-1
        this.add(String.valueOf(i));
      }
    }
  }


  Integer nChannels; // size of the full set of channels
  Boolean verbose;
  ArrayList<ChannelSet> channelsAvailable; // ArrayList to hold channels available in each time slot

  Channels(Integer nChannels, Boolean verbose) {
    this.nChannels = nChannels;
    this.verbose = verbose;
    this.channelsAvailable = new ArrayList<ChannelSet>();
  }

	/*
	 * FIX: Previously returned a copy (new HashSet<>(...)),
	 * which caused removals to be lost and could lead to channel conflicts.
	 */
  public HashSet<String> getChannelSet(int timeSlot) {
    /* get the channel set for this timeSlot */
    HashSet<String> channelSet = channelsAvailable.get(timeSlot);
    return channelSet;
  }

  public void addNewChannelSet() {
    var channels = new ChannelSet(nChannels);
    channelsAvailable.add(channels);
  }

  public Boolean isEmpty(int timeSlot) {
    ChannelSet channelSet = channelsAvailable.get(timeSlot); // get the channel set for this
                                                             // timeSlot
    return channelSet.isEmpty(); // returns true channel set is empty and false if not
  }

  public Boolean removeChannel(int timeSlot, String channel) {
	// Objects.requireNonNull(channel, "channel must not be null");
    Boolean result;
    ChannelSet channels = channelsAvailable.get(timeSlot);
    result = channels.remove(channel);
    return result;
  }

  public Boolean addChannel(int timeSlot, String channel) {
    Boolean result;
    ChannelSet channels = channelsAvailable.get(timeSlot); // get a pointer to the channel set
    result = channels.add(channel); // add the channel, returning the result
    return result;
  }

  public Integer getNumChannels() {
    return nChannels;
  }

}
