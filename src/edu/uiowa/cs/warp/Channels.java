package edu.uiowa.cs.warp;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Tracks which communication channels are free in each time slot of a WARP schedule.
 * 
 * @author sgoddard
 * @version 1.4
 *
 */
public class Channels {
	
	/**
	 * A set of channel identifiers (as strings) for a single time slot.
	 */
	private class ChannelSet extends HashSet<String> {
		private static final long serialVersionUID = 6725256944325470867L;

		/** Creates an empty channel set. */
		private ChannelSet() {
			super();
		}

		/**
		 * Creates a channel set pre-populated with all channel IDs
		 * from 0 to nChannels-1
		 * 
		 * @param nchannels total number of channels in the system
		 */
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

	/**
	 * Creates a channel manager with the given number of channels and a verbosity flag.
	 * Initially, no time slots exist. Call addNewChannelSet() to start adding time slots.
	 * 
	 * @param nChannels number of channels
	 * @param verbose whether to enable verbose behavior
	 */
	Channels(Integer nChannels, Boolean verbose) {
		this.nChannels = nChannels;
		this.verbose = verbose;
		this.channelsAvailable = new ArrayList<ChannelSet>();
	}

	/**
	 * Returns copy of the channel set for the given time slot.
	 * 
	 * @param timeSlot index of the time slot
	 * @return a new HashSet containing the available channels in that slot.
	 */
	public HashSet<String> getChannelSet(Integer timeSlot) {
		/* get the channel set for this timeSlot */
		HashSet<String> channelSet = new HashSet<String>(channelsAvailable.get(timeSlot));
		return channelSet;
	}

	/**
	 * Appends a new time slot to the schedule. The new slot starts with all
	 * channels marked as available.
	 */
	public void addNewChannelSet() {
		var channels = new ChannelSet(nChannels);
		channelsAvailable.add(channels);
	}

	/**
	 * Checks whether the given time slot has no channels available.
	 * 
	 * @param timeSlot idnex of the time slot
	 * @return true if the set is empty, false otherwise
	 */
	public Boolean isEmpty(int timeSlot) {
		ChannelSet channelSet = channelsAvailable.get(timeSlot); // get the channel set for this
																	// timeSlot
		return channelSet.isEmpty(); // returns true channel set is empty and false if not
	}

	/**
	 * Removes a channel from the availability set of the given time slot,
	 * marking it as occupied by a transmission
	 * 
	 * @param timeSlot index of the time lot
	 * @param channel channel ID
	 * @return true if the channel was present and removed, false otherwise
	 */
	public Boolean removeChannel(int timeSlot, String channel) {
		Boolean result;
		ChannelSet channels = channelsAvailable.get(timeSlot);
		result = channels.remove(channel);
		return result;
	}

	/**
	 * Adds a channel back into the availability set for the given time slot, 
	 * marking it as free again.
	 * 
	 * @param timeSlot index of the time slot
	 * @param channel channel ID
	 * @return true if the channel was not already present and is now added,
	 * 		   false if it was already present
	 */
	public Boolean addChannel(int timeSlot, String channel) {
		Boolean result;
		ChannelSet channels = channelsAvailable.get(timeSlot); // get a pointer to the channel set
		result = channels.add(channel); // add the channel, returning the result
		return result;
	}

	/**
	 * Returns the total number of channels in the system.
	 * 
	 * @return number of channels
	 */
	public Integer getNumChannels() {
		return nChannels;
	}

}
