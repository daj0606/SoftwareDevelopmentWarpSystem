package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

class ChannelsTest {

	private Channels channels;
	@BeforeEach
	void setUp() throws Exception{
		channels = new Channels(4, false);
	}

	//********* Get Channel Set tests **********//
	@Test()
	void testGetChannelSet() {
		fail("Not yet implemented");
	}
	@Test
	void testGetChannelSet_returnsExpectedSet() {
		// duplicates ("B","B") collapse in the returned HashSet
		assertEquals(Set.of("A","B"), channels.getChannelSet(1));
	}

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
	@Test
	void testAddNewChannelSet() {
		fail("Not yet implemented");
	}
	//********* Is Empty tests **********//
	@Test
	void testIsEmpty() {
		fail("Not yet implemented");
	}

	//********* Remove Channel tests **********//
	@Test
	void testRemoveChannel() {
		fail("Not yet implemented");
	}

	//********* Add Channel tests **********//
	@Test
	void testAddChannel() {
		fail("Not yet implemented");
	}

	//********* Get Num Channels tests **********//
	@Test
	void testGetNumChannels() {
		fail("Not yet implemented");
	}

}
