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
 *
 * Tests the following methods in Channels.java:  
 * 
 * getChannelSet()
 * addNewChannelSet()
 * isEmpty()
 * removeChannel()
 * addChannel()
 * getNumChannels()
 */

class ChannelTests {
	
	private Channels testChannels;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		testChannels = new Channels(4, false);
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getChannelSet(int)}.
	 */
	@Test
	void testGetChannelSet() {
		testChannels.addNewChannelSet(); // "0","1","2","3"
		Set<String> expected = new HashSet<>(Arrays.asList("0", "1", "2", "3"));
		
		Set<String> actual = testChannels.getChannelSet(0);
		
		assertEquals(expected, actual,
				"ChannelSet should contain strings '0' to '3' for nChannels=4");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getChannelSet(int)}.
	 */
	@Test
	void testGetChannelSetReturnsLiveView() {
	    testChannels.addNewChannelSet();

	    Set<String> view = testChannels.getChannelSet(0);
	    view.remove("1");

	    assertFalse(testChannels.getChannelSet(0).contains("1"),
	        "Removal via getChannelSet must persist to prevent conflicts");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getChannelSet(int)}.
	 */
	@Test
	void testGetChannelSetInvalidTimeSlotThrows() {
	    testChannels.addNewChannelSet();

	    assertThrows(IndexOutOfBoundsException.class,
	        () -> testChannels.getChannelSet(1));

	    assertThrows(IndexOutOfBoundsException.class,
	        () -> testChannels.getChannelSet(-1));
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getChannelSet(int)}.
	 */
	@Test
	void testGetChannelSetReturnsSameInstance() {
		testChannels.addNewChannelSet();

		Set<String> first = testChannels.getChannelSet(0);
	    Set<String> second = testChannels.getChannelSet(0);

	    assertSame(first, second, "Should return the live set, not a copy");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getChannelSet(int)}.
	 */
	@Test
	void testGetChannelSetExternalRemoveInteractsWithAddChannel() {
		testChannels.addNewChannelSet();

		Set<String> view = testChannels.getChannelSet(0);
		view.remove("1");
		
		Boolean addedAgain = testChannels.addChannel(0, "1");
		Set<String> after = testChannels.getChannelSet(0);
		
		assertTrue(addedAgain, "addChannel should succeed after external remove");
		assertTrue(after.contains("1"));
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getChannelSet(int)}.
	 */
	@Test
	void testGetChannelSetExternalAddMakesAddChannelReturnFalse() {
	    testChannels.addNewChannelSet();

	    Set<String> view = testChannels.getChannelSet(0);
	    view.remove("0");
	    boolean addedViaView = view.add("0");

	    Boolean addChannelResult = testChannels.addChannel(0, "0");

	    assertTrue(addedViaView, "External add should succeed on removed element");
	    assertFalse(addChannelResult, "addChannel should be false for duplicate");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getChannelSet(int)}.
	 */
	@Test
	void testGetChannelSetMutationAffectsOnlySpecifiedTimeSlot() {
	    testChannels.addNewChannelSet();
	    testChannels.addNewChannelSet();
	    Set<String> expectedSlot1 = new HashSet<>(Arrays.asList("0","1","2","3"));

	    Set<String> slot0 = testChannels.getChannelSet(0);
	    slot0.remove("1");

	    Set<String> actualSlot1 = testChannels.getChannelSet(1);

	    assertEquals(expectedSlot1, actualSlot1, "Slot 1 must remain unchanged");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getChannelSet(int)}.
	 */
	@Test
	void testGetChannelSetEmptyWhenNChannelsZero() {
	    testChannels = new Channels(0, false);
	    testChannels.addNewChannelSet();

	    Set<String> actual = testChannels.getChannelSet(0);

	    assertTrue(actual.isEmpty());
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addNewChannelSet()}.
	 */
	@Test
	void testAddNewChannelSet() {
		Set<String> expectedSet = new HashSet<>(Arrays.asList("0", "1", "2", "3"));
	    int expectedCount = 1;

	    testChannels.addNewChannelSet(); // "0","1","2","3"
	    Set<String> actualSet = testChannels.getChannelSet(0);
	    int actualCount = testChannels.channelsAvailable.size();

	    assertEquals(expectedCount, actualCount,
	        "One ChannelSet should have been added to channelsAvailable.");
	    assertEquals(expectedSet, actualSet,
	        "ChannelSet should contain strings '0' to '3' for nChannels=4");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addNewChannelSet()}.
	 */
	@Test
	void testAddNewChannelSetTwiceCreatesTwoIndependentSlots() {
	    Set<String> expectedSet = new HashSet<>(Arrays.asList("0", "1", "2", "3"));

	    testChannels.addNewChannelSet();
	    testChannels.addNewChannelSet();
	    Set<String> slot0 = testChannels.getChannelSet(0);
	    Set<String> slot1 = testChannels.getChannelSet(1);

	    assertEquals(expectedSet, slot0, "Slot 0 should initialize to channels {0,1,2,3}");
	    assertEquals(expectedSet, slot1, "Slot 1 should initialize to channels {0,1,2,3}");
	    assertNotSame(slot0, slot1, "Each time slot must have its own distinct ChannelSet instance");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addNewChannelSet()}.
	 */
	@Test
	void testAddNewChannelSetSecondCallDoesNotModifyFirstSlot() {
	    Set<String> expectedSlot0 = new HashSet<>(Arrays.asList("0", "2", "3"));
	    Set<String> expectedSlot1 = new HashSet<>(Arrays.asList("0", "1", "2", "3"));

	    testChannels.addNewChannelSet();
	    testChannels.getChannelSet(0).remove("1");
	    testChannels.addNewChannelSet();
	    Set<String> actualSlot0 = testChannels.getChannelSet(0);
	    Set<String> actualSlot1 = testChannels.getChannelSet(1);

	    assertEquals(expectedSlot0, actualSlot0, "Slot 0 should preserve its mutation (channel 1 removed)");
	    assertEquals(expectedSlot1, actualSlot1, "Slot 1 should be freshly initialized to {0,1,2,3}");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addNewChannelSet()}.
	 */
	@Test
	void testAddNewChannelSetSizeIncrementsPerCall() {
	    testChannels.addNewChannelSet();
	    int countAfterOne = testChannels.channelsAvailable.size();

	    testChannels.addNewChannelSet();
	    int countAfterTwo = testChannels.channelsAvailable.size();

	    assertEquals(1, countAfterOne, "channelsAvailable size should be 1 after first add");
	    assertEquals(2, countAfterTwo, "channelsAvailable size should be 2 after second add");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addNewChannelSet()}.
	 */
	@Test
	void testAddNewChannelSetEmptyWhenNChannelsZero() {
	    testChannels = new Channels(0, false);
	    testChannels.addNewChannelSet();
	    Set<String> actual = testChannels.getChannelSet(0);

	    assertTrue(actual.isEmpty(), "Slot should be empty when nChannels == 0");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#isEmpty(int)}.
	 */
	@Test
	void testIsEmptyFalse() {
		testChannels.addNewChannelSet();
		
		boolean actual = testChannels.isEmpty(0);
		
		assertFalse(actual, "Expected non-empty after addNewChannelSet()");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#isEmpty(int)}.
	 */
	@Test
	void testIsEmptyTrue() {
		Channels emptyTrueChannels = new Channels(0, false);
		emptyTrueChannels.addNewChannelSet();
		
		boolean actual = emptyTrueChannels.isEmpty(0);
		
		assertTrue(actual, "Expected empty when nChannels=0");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#isEmpty(int)}.
	 */
	@Test
	void testIsEmptyInvalidTimeSlotThrows() {
	    testChannels.addNewChannelSet();

	    assertThrows(IndexOutOfBoundsException.class,
	        () -> testChannels.isEmpty(1),
	        "isEmpty should throw IndexOutOfBoundsException for out-of-range positive index");

	    assertThrows(IndexOutOfBoundsException.class,
	        () -> testChannels.isEmpty(-1),
	        "isEmpty should throw IndexOutOfBoundsException for negative index");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#isEmpty(int)}.
	 */
	@Test
	void testIsEmptyTrueAfterRemovingAllChannels() {
	    testChannels.addNewChannelSet();

	    testChannels.removeChannel(0, "0");
	    testChannels.removeChannel(0, "1");
	    testChannels.removeChannel(0, "2");
	    testChannels.removeChannel(0, "3");

	    assertTrue(testChannels.isEmpty(0),
	        "isEmpty should return true after removing all channels");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#isEmpty(int)}.
	 */
	@Test
	void testIsEmptyFlipAfterClearThenAdd() {
	    testChannels.addNewChannelSet();

	    testChannels.getChannelSet(0).clear();

	    testChannels.addChannel(0, "0");

	    assertFalse(testChannels.isEmpty(0),
	        "isEmpty should return false after adding a channel back to an empty set");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#removeChannel(int, java.lang.String)}.
	 */
	@Test
	void testRemoveChannelEmptyChannelSet() {
		Channels emptyChannels = new Channels(0, false);
		emptyChannels.addNewChannelSet();
		
		boolean removed = emptyChannels.removeChannel(0, "0");
		
		assertFalse(removed, "Removing from empty set should return false");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#removeChannel(int, java.lang.String)}.
	 */
	@Test
	void testRemoveChannelMissingChannel() {
		testChannels.addNewChannelSet(); // "0","1","2","3"
		Set<String> expected = new HashSet<>(Arrays.asList("0", "1", "2", "3"));
		
		boolean removed = testChannels.removeChannel(0, "9");
		Set<String> actual = testChannels.getChannelSet(0);
		
		assertFalse(removed, "Removing a missing element should return false");
	    assertEquals(expected, actual, "Set should remain unchanged");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#removeChannel(int, java.lang.String)}.
	 */
	@Test
	void testRemoveChannelRemovesAndReturnsTrue() {
		testChannels.addNewChannelSet(); // "0","1","2","3"
		Set<String> expected = new HashSet<>(Arrays.asList("1", "2", "3"));
		
		boolean removed = testChannels.removeChannel(0, "0");
		Set<String> actual = testChannels.getChannelSet(0);
		
		assertTrue(removed, "removeChannel should return true when element existed");
	    assertEquals(expected, actual, "Set should be 1,2,3 after removing 0");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#removeChannel(int, java.lang.String)}.
	 */
	@Test
	void testRemoveMultipleChannels() {
		testChannels.addNewChannelSet(); // "0","1","2","3"
		Set<String> expected = new HashSet<>(Arrays.asList("2", "3"));

	    boolean removal0 = testChannels.removeChannel(0, "0");
	    boolean removal1 = testChannels.removeChannel(0, "1");
	    Set<String> actual = testChannels.getChannelSet(0);

	    assertTrue(removal0, "First removal should succeed");
	    assertTrue(removal1, "Second removal should succeed");
	    assertEquals(expected, actual, "Set should be 2,3 after removing 0 and 1");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#removeChannel(int, java.lang.String)}.
	 */
	@Test
	void testRemoveChannelTwiceSecondReturnsFalse() {
	    testChannels.addNewChannelSet(); // "0","1","2","3"
	    Set<String> expected = new HashSet<>(Arrays.asList("0", "2", "3"));

	    boolean first = testChannels.removeChannel(0, "1");
	    boolean second = testChannels.removeChannel(0, "1");
	    Set<String> actual = testChannels.getChannelSet(0);

	    assertTrue(first, "First removal should return true");
	    assertFalse(second, "Second removal should return false");
	    assertEquals(expected, actual, "Set should be 0,2,3 after one removal of 1");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#removeChannel(int, java.lang.String)}.
	 */
	@Test
	void testRemoveChannelAffectsOnlySpecifiedTimeSlot() {
	    testChannels.addNewChannelSet();  // slot 0 --> "0","1","2","3"
	    testChannels.addNewChannelSet();  // slot 1 --> "0","1","2","3"
	    
	    Set<String> expectedSlot0 = new HashSet<>(Arrays.asList("0","1","3"));
	    Set<String> expectedSlot1 = new HashSet<>(Arrays.asList("0","1","2","3"));
	    	
	    boolean removed = testChannels.removeChannel(0, "2");
	    Set<String> actualSlot0 = testChannels.getChannelSet(0);
	    Set<String> actualSlot1 = testChannels.getChannelSet(1);
	    
	    assertTrue(removed, "removeChannel should return true when element existed");
	    assertEquals(expectedSlot0, actualSlot0, "slot 0 should lose channel 2");
	    assertEquals(expectedSlot1, actualSlot1, "slot 1 should remain unchanged");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#removeChannel(int, java.lang.String)}.
	 */
	@Test
	void testRemoveChannelInvalidTimeSlotThrows() {
		testChannels.addNewChannelSet();
		
	    assertThrows(IndexOutOfBoundsException.class,
	        () -> testChannels.removeChannel(99, "0"));
	}	
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addChannel(int, java.lang.String)}.
	 */
	@Test
	void testAddChannelAddsNewAndReturnsTrue() {
	    testChannels.addNewChannelSet();
	    Set<String> expected = new HashSet<>(Arrays.asList("0","1","2","3","4"));

	    Boolean added = testChannels.addChannel(0, "4");
	    Set<String> actual = testChannels.getChannelSet(0);

	    assertTrue(added, "Adding a new channel should return true");
	    assertEquals(expected, actual, "Set should include the new channel");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addChannel(int, java.lang.String)}.
	 */
	@Test
	void testAddChannelDuplicateReturnsFalseAndUnchanged() {
	    testChannels.addNewChannelSet();
	    Set<String> expected = new HashSet<>(Arrays.asList("0","1","2","3"));

	    Boolean added = testChannels.addChannel(0, "2");
	    Set<String> actual = testChannels.getChannelSet(0);

	    assertFalse(added, "Adding an existing channel should return false");
	    assertEquals(expected, actual, "Set should remain unchanged for duplicate add");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addChannel(int, java.lang.String)}.
	 */
	@Test
	void testAddChannelOnEmptySetAddsFirstElement() {
	    Channels empty = new Channels(0, false);
	    empty.addNewChannelSet();
	    Set<String> expected = new HashSet<>(Arrays.asList("0"));

	    Boolean added = empty.addChannel(0, "0");
	    Set<String> actual = empty.getChannelSet(0);

	    assertTrue(added, "Adding to an empty set should return true");
	    assertEquals(expected, actual, "Empty set should now contain the added channel");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addChannel(int, java.lang.String)}.
	 */
	@Test
	void testAddChannelAffectsOnlySpecifiedTimeSlot() {
	    testChannels.addNewChannelSet();
	    testChannels.addNewChannelSet();
	    Set<String> expectedSlot0 = new HashSet<>(Arrays.asList("0","1","2","3","9"));
	    Set<String> expectedSlot1 = new HashSet<>(Arrays.asList("0","1","2","3"));

	    Boolean added = testChannels.addChannel(0, "9");
	    Set<String> actualSlot0 = testChannels.getChannelSet(0);
	    Set<String> actualSlot1 = testChannels.getChannelSet(1);

	    assertTrue(added, "Adding to slot 0 should return true");
	    assertEquals(expectedSlot0, actualSlot0, "Slot 0 should include the new channel");
	    assertEquals(expectedSlot1, actualSlot1, "Slot 1 should remain unchanged");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addChannel(int, java.lang.String)}.
	 */
	@Test
	void testAddChannelInvalidTimeSlotThrows() {
	    testChannels.addNewChannelSet();

	    assertThrows(IndexOutOfBoundsException.class,
	        () -> testChannels.addChannel(1, "0"));

	    assertThrows(IndexOutOfBoundsException.class,
	        () -> testChannels.addChannel(-1, "0"));
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#addChannel(int, java.lang.String)}.
	 * (Use this if nulls are ALLOWED)
	 */
	 @Test void testAddChannelNullAllowedReturnsTrueFirstTime() {
		 testChannels.addNewChannelSet();
		 
		 Boolean added = testChannels.addChannel(0, null); Set<String> actual =
		 testChannels.getChannelSet(0);
		 
		 assertTrue(added,
		 "HashSet.add(null) should return true if null not present");
		 assertTrue(actual.contains(null), "Set should contain null after add"); 
	 }

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getNumChannels()}.
	 */
	@Test
	void testGetNumChannelsReturnsConstructorValue() {
	    Channels ch = new Channels(4, false);
	    int expected = 4;

	    Integer actual = ch.getNumChannels();

	    assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getNumChannels()}.
	 */
	@Test
	void testGetNumChannelsZero() {
	    Channels ch = new Channels(0, false);
	    int expected = 0;

	    Integer actual = ch.getNumChannels();

	    assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getNumChannels()}.
	 */
	@Test
	void testGetNumChannelsUnaffectedByAddChannel() {
	    Channels ch = new Channels(4, false);
	    int expected = 4;

	    ch.addNewChannelSet();
	    ch.addChannel(0, "9");

	    Integer actual = ch.getNumChannels();

	    assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getNumChannels()}.
	 */
	@Test
	void testGetNumChannelsUnaffectedByMultipleTimeSlots() {
	    Channels ch = new Channels(3, false);
	    int expected = 3;

	    ch.addNewChannelSet();
	    ch.addNewChannelSet();

	    Integer actual = ch.getNumChannels();

	    assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.Channels#getNumChannels()}.
	 */
	@Test
	void testGetNumChannelsUnaffectedByRemoveChannel() {
	    Channels ch = new Channels(3, false);
	    int expected = 3;

	    ch.addNewChannelSet();
	    ch.removeChannel(0, "1");

	    Integer actual = ch.getNumChannels();

	    assertEquals(expected, actual);
	}

}
