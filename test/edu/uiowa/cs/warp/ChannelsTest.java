package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.util.Set;

class ChannelsTest {

	private Channels channels;
	@BeforeEach
	void setUp(){
		channels = new Channels(4, false);
		
		channels.addNewChannelSet();
	    channels.addNewChannelSet();
	    channels.addNewChannelSet();

	    channels.addChannel(1, "A");
	    channels.addChannel(1, "B");
	    channels.addChannel(1, "B"); // duplicate

	    channels.addChannel(2, "X");
	    channels.addChannel(2, "Y");
	}

	//********* Get Channel Set tests **********//
	
	@Test()
	void testGetChannelSet() {
		fail("Not yet implemented");
	}
	@Test
	void testGetChannelSet_returnsExpectedSet() {
		// duplicates ("B","B") collapse in the returned HashSet
		var actual = channels.getChannelSet(1);
		assertEquals(Set.of("A","B"), actual, "actual=" + actual);
	}//channels.getChannelSet(1)

	@Test
	void testGetChannelSet_returnsDefensiveCopy() {
		// mutate the returned set
		Set<String> out = channels.getChannelSet(2);
		out.add("Z");

		// a fresh call should not reflect that mutation
		assertEquals(Set.of("X","Y"), channels.getChannelSet(2));
	}

	@Test
	void testGetChannelSet_missingSlot_throws() {
		// channelsAvailable.get(missing) -> IndexOutOfBoundsException
		assertThrows(IndexOutOfBoundsException.class, () -> channels.getChannelSet(10));
	}

	//********* Add New Channels tests**********//
	private static java.util.Set<String> defaultNames(int n) {
		  java.util.Set<String> s = new java.util.HashSet<>();
		  for (int i = 0; i < n; i++) s.add(String.valueOf(i));
		  return s;
		}

		// -------- Add New Channels tests (no streams) --------

		@Test
		void testAddNewChannelSet_createsDefaultSet() {
		  Channels c = new Channels(4, false);   // fresh instance
		  c.addNewChannelSet();                  // slot 0

		  var s0 = c.getChannelSet(0);
		  var expected = defaultNames(4);        // {"0","1","2","3"}

		  assertEquals(4, s0.size(), "new set should have nChannels entries");
		  assertEquals(expected, s0, "s0=" + s0);
		}

		@Test
		void testAddNewChannelSet_addsSecondSlot() {
		  Channels c = new Channels(4, false);
		  c.addNewChannelSet(); // slot 0
		  c.addNewChannelSet(); // slot 1

		  var s1 = c.getChannelSet(1);
		  var expected = defaultNames(4);

		  assertEquals(expected, s1, "s1=" + s1);
		}

		@Test
		void testAddNewChannelSet_setsAreIndependent() {
		  Channels c = new Channels(4, false);
		  c.addNewChannelSet(); // slot 0
		  c.addNewChannelSet(); // slot 1

		  c.addChannel(0, "A");  // mutate only slot 0

		  var s0 = c.getChannelSet(0);
		  var s1 = c.getChannelSet(1);

		  assertTrue(s0.contains("A"), "slot 0 should include A; s0=" + s0);
		  assertFalse(s1.contains("A"), "slot 1 should NOT include A; s1=" + s1);
		}
	//********* Is Empty tests **********//
		@Test
		void testIsEmpty_throwsWhenSlotMissing() {
		  Channels c = new Channels(4, false);
		  // No sets yet; querying slot 0 should fail
		  assertThrows(IndexOutOfBoundsException.class, () -> c.isEmpty(0));
		}

		@Test
		void testIsEmpty_falseRightAfterAddNewChannelSet() {
		  Channels c = new Channels(4, false);
		  c.addNewChannelSet();                 // creates slot 0 with defaults
		  assertFalse(c.isEmpty(0), "A new slot with default channels should not be empty");
		}

		@Test
		void testIsEmpty_trueAfterRemovingAllChannelsInSlot() {
		  Channels c = new Channels(4, false);
		  c.addNewChannelSet();                 // slot 0 with "0","1","2","3"

		  // Remove every default channel from slot 0
		  for (int i = 0; i < 4; i++) {
		    c.removeChannel(0, String.valueOf(i));
		  }
		  assertTrue(c.isEmpty(0), "After removing all entries, the slot should be empty");
		}

		@Test
		void testIsEmpty_independentAcrossSlots() {
		  Channels c = new Channels(4, false);
		  c.addNewChannelSet();                 // slot 0
		  c.addNewChannelSet();                 // slot 1

		  // Empty slot 0 only
		  for (int i = 0; i < 4; i++) {
		    c.removeChannel(0, String.valueOf(i));
		  }

		  assertTrue(c.isEmpty(0),  "slot 0 should be empty");
		  assertFalse(c.isEmpty(1), "slot 1 should still contain its defaults");
		}

	//********* Remove Channel tests **********//
	@Disabled("TODO")
	@Test
	void testRemoveChannel() {
		fail("Not yet implemented");
	}

	//********* Add Channel tests **********//
	@Disabled("TODO")
	@Test
	void testAddChannel() {
		fail("Not yet implemented");
	}

	//********* Get Num Channels tests **********//
	@Disabled("TODO")
	@Test
	void testGetNumChannels() {
		fail("Not yet implemented");
	}

}
