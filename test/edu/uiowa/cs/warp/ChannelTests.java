package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Dallas Jackson
 * Oct 3, 2025
 */
class ChannelTests {
	
	private Channels testChannel;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		testChannel = new Channels(4, false);
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getChannelSet(java.lang.Integer)}.
	 */
	@Test
	void testGetChannelSet() {
		testChannel.addNewChannelSet();
		
		Set<String> expected = new HashSet<>(Arrays.asList("0", "1", "2", "3"));
		HashSet<String> actual = testChannel.getChannelSet(0);
		
		assertEquals(expected, actual,
				"ChannelSet should contain strings '0' to '3' for nChannels=4");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addNewChannelSet()}.
	 */
	@Test
	void testAddNewChannelSet() {
		testChannel.addNewChannelSet();
		
		assertEquals(1, testChannel.channelsAvailable.size(),
				"One ChannelSet should have been added to channelsAvailable.");
		
		Set<String> expected = new HashSet<>(Arrays.asList("0", "1", "2", "3"));
		HashSet<String> actual = testChannel.channelsAvailable.get(0);
		
		assertEquals(expected, actual,
				"ChannelSet should contain strings '0' to '3' for nChannels=4");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#isEmpty(int)}.
	 */
	@Test
	void testIsEmptyFalse() {
		testChannel.addNewChannelSet();
		
		boolean actual = testChannel.isEmpty(0);
		
		assertFalse(actual);
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#isEmpty(int)}.
	 */
	@Test
	void testIsEmptyTrue() {
		Channels emptyTrue = new Channels(0, false);
		emptyTrue.addNewChannelSet();
		
		boolean actual = emptyTrue.isEmpty(0);
		
		assertTrue(actual);
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#removeChannel(int, java.lang.String)}.
	 */
	@Test
	void testRemoveChannel() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addChannel(int, java.lang.String)}.
	 */
	@Test
	void testAddChannel() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getNumChannels()}.
	 */
	@Test
	void testGetNumChannels() {
		fail("Not yet implemented");
	}

}
