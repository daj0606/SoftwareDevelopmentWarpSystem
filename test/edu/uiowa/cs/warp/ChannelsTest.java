package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.Disabled;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for Channels.
 *
 * We verify the public API contracts:
 *  - addNewChannelSet() creates a new time-slot set pre-populated with "0".."n-1"
 *  - getChannelSet(slot) returns a defensive copy (caller mutations do not affect internals)
 *  - addChannel/removeChannel mutate only the specified slot and respect Set semantics
 *  - isEmpty(slot) reflects current contents, not just slot existence
 *  - getNumChannels() reports configured capacity (nChannels), not per-slot size
 */
class ChannelsTest {

	private Channels channels;
	@BeforeEach
	void setUp(){
		channels = new Channels(4, false);
	}
	/**
	 * Builds the expected default channel names: "0","1",...,"n-1".
	 * 
	 * @param n
	 * @return
	 */
	private static java.util.Set<String> defaultNames(int n) {
		java.util.Set<String> s = new java.util.HashSet<>();
		for (int i = 0; i < n; i++) s.add(String.valueOf(i));
		return s;
	}

	//********* Get Channel Set tests **********//

	/**
	 * GIVEN slots 0..1 exist and we add "A","B" (with a duplicate) to slot 1,
	 * WHEN we fetch slot 1,
	 * THEN the returned set equals {defaults ∪ "A","B"} and duplicates don't inflate size.
	 * Why: verifies correct aggregation and true Set semantics for getChannelSet(..).
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetChannelSet_returnsExpectedSet() {
		channels.addNewChannelSet();
		channels.addNewChannelSet();
		
		channels.addChannel(1, "A");
		channels.addChannel(1, "B");
		channels.addChannel(1, "B");
		
		java.util.Set<String> expected = defaultNames(4);
		expected.add("A");
		expected.add("B");
		
		java.util.Set<String> actual = channels.getChannelSet(1);
		assertEquals(expected, actual, "actual= " + actual);
	}

	/**
	 * GIVEN slot 2 exists and contains defaults plus "X","Y",
	 * WHEN we mutate the RETURNED set (add "Z"),
	 * THEN a fresh fetch still equals {defaults ∪ "X","Y"}.
	 * Why: getChannelSet(..) must return a defensive copy, not a live internal reference.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetChannelSet_returnsDefensiveCopy() {
		channels.addNewChannelSet();
		channels.addNewChannelSet();
		channels.addNewChannelSet();
		
		channels.addChannel(2, "X");
		channels.addChannel(2, "Y");
		
		java.util.Set<String> expected = defaultNames(4);
		expected.add("X");
		expected.add("Y");
		
		java.util.Set<String> returned = channels.getChannelSet(2);
		returned.add("Z");
		
		assertEquals(expected, channels.getChannelSet(2));
		
	}

	/**
	 * GIVEN no slot 10 exists,
	 * WHEN we call getChannelSet(10),
	 * THEN an IndexOutOfBoundsException is thrown.
	 * Why: defines boundary behavior for invalid time-slot indices.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetChannelSet_missingSlot_throws() {
		// channelsAvailable.get(missing) -> IndexOutOfBoundsException
		assertThrows(IndexOutOfBoundsException.class, () -> channels.getChannelSet(10));
	}

	//********* Add New Channels tests**********//

	// -------- Add New Channels tests (no streams) --------

	/**
	 * GIVEN a fresh Channels(n=4),
	 * WHEN we call addNewChannelSet() once,
	 * THEN slot 0 exists, has size n, and equals {"0","1","2","3"}.
	 * Why: new slots must be created pre-populated with the default channel names.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testAddNewChannelSet_createsDefaultSet() {
		Channels c = new Channels(4, false);   // fresh instance
		c.addNewChannelSet();                  // slot 0

		var s0 = c.getChannelSet(0);
		var expected = defaultNames(4);        // {"0","1","2","3"}

		assertEquals(4, s0.size(), "new set should have nChannels entries");
		assertEquals(expected, s0, "s0=" + s0);
	}

	/**
	 * GIVEN a fresh Channels,
	 * WHEN we call addNewChannelSet() twice,
	 * THEN slot 1 exists and equals the default set (independent of slot 0).
	 * Why: each call creates a new slot; creation must not overwrite prior slots.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testAddNewChannelSet_addsSecondSlot() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet(); // slot 0
		c.addNewChannelSet(); // slot 1

		var s1 = c.getChannelSet(1);
		var expected = defaultNames(4);

		assertEquals(expected, s1, "s1=" + s1);
	}

	/**
	 * GIVEN slots 0 and 1 exist,
	 * WHEN we add "A" to slot 0 only,
	 * THEN slot 0 contains "A" and slot 1 does not.
	 * Why: slots must be independent; mutations are scoped to the specified slot.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
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
	
	/**
	 * GIVEN a fresh Channels with no slots,
	 * WHEN we call isEmpty(0),
	 * THEN an IndexOutOfBoundsException is thrown.
	 * Why: boundary behavior should be consistent across the API for missing slots.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testIsEmpty_throwsWhenSlotMissing() {
		Channels c = new Channels(4, false);
		// No sets yet; querying slot 0 should fail
		assertThrows(IndexOutOfBoundsException.class, () -> c.isEmpty(0));
	}

	/**
	 * GIVEN a fresh Channels(n=4),
	 * WHEN we create slot 0 via addNewChannelSet(),
	 * THEN isEmpty(0) is false (defaults are present).
	 * Why: emptiness reflects current contents; pre-populated slots are not empty.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testIsEmpty_falseRightAfterAddNewChannelSet() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // creates slot 0 with defaults
		assertFalse(c.isEmpty(0), "A new slot with default channels should not be empty");
	}

	/**
	 * GIVEN slot 0 exists with defaults,
	 * WHEN we remove every default entry ("0".."3"),
	 * THEN isEmpty(0) is true.
	 * Why: emptiness must reflect contents, not merely slot existence.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testIsEmpty_trueAfterRemovingAllChannelsInSlot() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0 with "0","1","2","3"

		// Remove every default channel from slot 0
		for (int i = 0; i < 4; i++) {
			c.removeChannel(0, String.valueOf(i));
		}
		assertTrue(c.isEmpty(0), "After removing all entries, the slot should be empty");
	}

	/**
	 * GIVEN slots 0 and 1 exist,
	 * WHEN we remove all entries from slot 0 only,
	 * THEN isEmpty(0) is true and isEmpty(1) is false.
	 * Why: per-slot emptiness is independent.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
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
	
	/**
	 * GIVEN slot 0 exists with defaults and size recorded,
	 * WHEN we remove an existing default ("2"),
	 * THEN "2" is absent and size decreased by 1.
	 * Why: remove must work for defaults and adjust size correctly.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testRemoveChannel_removesExistingDefault() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0 has "0","1","2","3"
		int before = c.getChannelSet(0).size();

		c.removeChannel(0, "2");

		assertFalse(c.getChannelSet(0).contains("2"), "channel '2' should be removed");
		assertEquals(before - 1, c.getChannelSet(0).size(), "size should decrease by 1");
	}

	/**
	 * GIVEN slot 0 exists and we add "A",
	 * WHEN we remove "A",
	 * THEN "A" is absent and size returns to default count.
	 * Why: remove must work for user-added entries just like defaults.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testRemoveChannel_removesCustomChannel() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0 defaults
		c.addChannel(0, "A");
		assertTrue(c.getChannelSet(0).contains("A"));

		c.removeChannel(0, "A");

		assertFalse(c.getChannelSet(0).contains("A"), "custom channel 'A' should be removed");
		assertEquals(4, c.getChannelSet(0).size(), "back to defaults count");
	}

	/**
	 * GIVEN slot 0 exists and size recorded,
	 * WHEN we remove a non-existent value ("Q"),
	 * THEN size is unchanged and "Q" remains absent.
	 * Why: remove of a missing element should be a no-op (and typically returns false).
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testRemoveChannel_nonExistingIsNoOp() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0 defaults
		int before = c.getChannelSet(0).size();

		// Should not throw and should not change contents
		c.removeChannel(0, "Q");             // "Q" was never added

		assertEquals(before, c.getChannelSet(0).size(), "size should be unchanged");
		assertFalse(c.getChannelSet(0).contains("Q"));
	}

	/**
	 * GIVEN slots 0 and 1 exist and contain "A" (slot 0) and "B" (slot 1),
	 * WHEN we remove "A" from slot 0,
	 * THEN slot 0 no longer has "A" and slot 1 still has "B".
	 * Why: removal must be scoped to the specified slot only.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testRemoveChannel_affectsOnlySpecifiedSlot() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0
		c.addNewChannelSet();                 // slot 1

		c.addChannel(0, "A");
		c.addChannel(1, "B");

		c.removeChannel(0, "A");

		assertFalse(c.getChannelSet(0).contains("A"), "slot 0 should not have A");
		assertTrue(c.getChannelSet(1).contains("B"),  "slot 1 should still have B");
	}

	/**
	 * GIVEN no slots exist,
	 * WHEN we call removeChannel(0,"A"),
	 * THEN an IndexOutOfBoundsException is thrown.
	 * Why: consistent boundary behavior for invalid indices.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testRemoveChannel_missingSlot_throws() {
		Channels c = new Channels(4, false);
		// No sets created yet → any index is out of range
		assertThrows(IndexOutOfBoundsException.class, () -> c.removeChannel(0, "A"));
	}

	//********* Add Channel tests **********//
	
	/**
	 * GIVEN slot 0 exists and "A" is not present,
	 * WHEN we add "A",
	 * THEN "A" is present and the per-slot size increases by 1.
	 * Why: add must persist a new element and grow the set.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testAddChannel_newIncrementsSize() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0 has "0","1","2","3"

		int before = c.getChannelSet(0).size();
		assertFalse(c.getChannelSet(0).contains("A"));

		c.addChannel(0, "A");

		assertTrue(c.getChannelSet(0).contains("A"));
		assertEquals(before + 1, c.getChannelSet(0).size());
	}

	/**
	 * GIVEN slot 0 exists and already contains "A",
	 * WHEN we add "A" again,
	 * THEN size is unchanged.
	 * Why: Set semantics—adding a duplicate is a no-op.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testAddChannel_duplicateDoesNotChangeSize() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0

		c.addChannel(0, "A");
		int before = c.getChannelSet(0).size();

		c.addChannel(0, "A");                 // duplicate
		assertEquals(before, c.getChannelSet(0).size(),
				"Adding a duplicate should not change size");
	}

	/**
	 * GIVEN slot 0 exists and already contains default "2",
	 * WHEN we add "2" again,
	 * THEN size is unchanged and "2" remains present.
	 * Why: idempotence holds for defaults as well as custom entries.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testAddChannel_existingDefaultDoesNotChangeSize() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0 has "2" already

		int before = c.getChannelSet(0).size();
		c.addChannel(0, "2");                 // already present by default

		assertTrue(c.getChannelSet(0).contains("2"));
		assertEquals(before, c.getChannelSet(0).size());
	}

	/**
	 * GIVEN slots 0 and 1 exist,
	 * WHEN we add "A" to slot 0,
	 * THEN slot 0 contains "A" and slot 1 does not.
	 * Why: add is scoped to the specified slot.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testAddChannel_affectsOnlySpecifiedSlot() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0
		c.addNewChannelSet();                 // slot 1

		c.addChannel(0, "A");

		assertTrue(c.getChannelSet(0).contains("A"));
		assertFalse(c.getChannelSet(1).contains("A"));
	}

	/**
	 * GIVEN no slots exist,
	 * WHEN we call addChannel(0,"A"),
	 * THEN an IndexOutOfBoundsException is thrown.
	 * Why: consistent boundary behavior for missing slots.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testAddChannel_missingSlot_throws() {
		Channels c = new Channels(4, false);
		// no sets created → any index is out of range
		assertThrows(IndexOutOfBoundsException.class, () -> c.addChannel(0, "A"));
	}

	//********* Get Num Channels tests **********//
	
	/**
	 * GIVEN Channels constructed with n=4,
	 * WHEN we call getNumChannels(),
	 * THEN it returns 4.
	 * Why: getNumChannels reports configured capacity (nChannels), not per-slot size.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetNumChannels_returnsConstructorValue() {
		Channels c = new Channels(4, false);
		assertEquals(4, c.getNumChannels());
	}

	/**
	 * GIVEN slot 0 exists and we add/remove entries,
	 * WHEN we call getNumChannels() after mutations,
	 * THEN it still returns the constructor's n (4).
	 * Why: capacity is invariant; it must not depend on slot contents.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetNumChannels_doesNotChangeOnAddsOrRemoves() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();       // create slot 0 (prepopulated)
		c.addChannel(0, "A");
		c.removeChannel(0, "2");
		assertEquals(4, c.getNumChannels(), "capacity should stay equal to nChannels");
	}

	/**
	 * GIVEN slot 0 exists and we record its size,
	 * WHEN we add "A",
	 * THEN getChannelSet(0).size() increased by 1.
	 * Why: explicit per-slot occupancy increases after an add.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testPerSlotCount_increasesByOneOnNewChannel() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0
		int before = c.getChannelSet(0).size();

		c.addChannel(0, "A");
		assertEquals(before + 1, c.getChannelSet(0).size());
	}

	/**
	 * GIVEN slot 0 exists with defaults and we record its size,
	 * WHEN we remove default "2",
	 * THEN getChannelSet(0).size() decreased by 1.
	 * Why: explicit per-slot occupancy decreases after a removal.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testPerSlotCount_decreasesOnRemoveExisting() {
		Channels c = new Channels(4, false);
		c.addNewChannelSet();                 // slot 0 has "2" by default
		int before = c.getChannelSet(0).size();

		c.removeChannel(0, "2");
		assertEquals(before - 1, c.getChannelSet(0).size());
	}

}
