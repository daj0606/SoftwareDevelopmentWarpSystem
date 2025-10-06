package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
/**
 * JUnit tests for the WorkLoad.java class. This version creates a temporary
 * file programmatically before each test and deletes it afterwards. This allows
 * testing without modifying the WorkLoad source code and without relying on a
 * pre-existing file.
 */
class WorkLoadTest {

	private WorkLoad workLoad;
	private final String TEST_FILE_NAME = "temp-test-workload.txt";

	/**
	 * This method runs BEFORE each test. It defines the test graph as a string,
	 * writes it to a temporary file, and then initializes the WorkLoad object from
	 * that file.
	 */
	@BeforeEach
    void setUp() throws IOException {
        // FINAL WORKING VERSION based on the StressTest.txt example
		String graphContent = "testGraph {\n" +
                "  F3 (2, 30, 30, 0) : C -> D\n" +
                "  F1 (0, 20, 20, 0) : A -> B -> C\n" +
                "  F2 (1, 10, 15, 0) : B -> C -> D -> E\n" +
                "}";
        // Create the temporary file and write the content to it
        Path testFilePath = Paths.get(TEST_FILE_NAME);
        Files.write(testFilePath, graphContent.getBytes());

        // Now, initialize WorkLoad using the temporary file
        Options options = new Options();
        options.setInputFile(TEST_FILE_NAME);
        workLoad = new WorkLoad(options);
    }
	/**
	 * This method runs AFTER each test. It cleans up by deleting the temporary file
	 * that was created.
	 */
	@AfterEach
	void tearDown() throws IOException {
		Path testFilePath = Paths.get(TEST_FILE_NAME);
		Files.deleteIfExists(testFilePath);
	}

	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetFlowNamesInPriorityOrder() {
		assertTrue(workLoad.getFlowNamesInPriorityOrder().isEmpty(), "Priority order list should be empty initially");
	}

	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetMinPeriod() {
		assertEquals(10, workLoad.getMinPeriod(), "The minimum period should be 10");
	}

	@Test
	void testGetFlowNamesInOriginalOrder() {
		List<String> expectedOrder = Arrays.asList("F3", "F1", "F2");
		ArrayList<String> actualOrder = workLoad.getFlowNamesInOriginalOrder();
		assertEquals(expectedOrder, actualOrder, "Original flow order should match the input file order");
	}

	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetNodeChannel() {
		workLoad.setNodeChannel("A", 1);
		workLoad.setNodeChannel("C", 3);
		assertEquals(1, workLoad.getNodeChannel("A"), "Channel for node A should be 1");
		assertEquals(3, workLoad.getNodeChannel("C"), "Channel for node C should be 3");
	}

	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetFlowDeadline() {
		assertEquals(15, workLoad.getFlowDeadline("F2"), "Deadline for flow F2 should be 15");
	}

	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testSetFlowsInPriorityOrder() {
		List<String> expectedOrder = Arrays.asList("F1", "F2", "F3");
		workLoad.setFlowsInPriorityOrder();
		assertEquals(expectedOrder, workLoad.getFlowNamesInPriorityOrder(),
				"Flows should be sorted by priority attribute");
	}

	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testSetFlowsInDMorder() {
		List<String> expectedOrder = Arrays.asList("F2", "F1", "F3");
		workLoad.setFlowsInDMorder();
		assertEquals(expectedOrder, workLoad.getFlowNamesInPriorityOrder(),
				"Flows should be sorted by deadline (DM order)");
	}

	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetNodeNamesOrderedAlphabetically() {
		String[] expectedNames = { "A", "B", "C", "D", "E" };
		String[] actualNames = workLoad.getNodeNamesOrderedAlphabetically();
		assertArrayEquals(expectedNames, actualNames, "Node names should be sorted alphabetically");
	}

	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testGetNodesInFlow() {
		String[] expectedNodes = { "A", "B", "C" };
		String[] actualNodes = workLoad.getNodesInFlow("F1");
		assertArrayEquals(expectedNodes, actualNodes, "Node sequence for flow F1 is incorrect");
	}

	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testMaxFlowLength() {
		assertEquals(4, workLoad.maxFlowLength(), "The maximum flow length should be 4");
	}
	
	/**
	 * Boundary Case Test: Tests the behavior with a syntactically valid graph that has no flows.
	 * The grammar requires at least one statement inside {}, so we test with a single, unused node.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testWorkloadWithNoFlows() throws IOException { // I've renamed the method to be more accurate
		// Arrange: Create a graph with one node but no flows.
		String noFlowContent = "noFlowGraph {\n" +
		                       "  JustOneNode : 99\n" +
		                       "}";
		
		Path testFilePath = Paths.get(TEST_FILE_NAME);
		Files.write(testFilePath, noFlowContent.getBytes());
		Options options = new Options();
		options.setInputFile(TEST_FILE_NAME);
		WorkLoad noFlowWorkLoad = new WorkLoad(options);

		// Act & Assert: Verify that flow-related methods return empty/default values.
		assertEquals(0, noFlowWorkLoad.getFlowNamesInOriginalOrder().size(), "Should have no flows");
		assertEquals(1, noFlowWorkLoad.getNodes().size(), "Should have one node");
		assertTrue(noFlowWorkLoad.getNodes().containsKey("JustOneNode"), "The single node should exist in the map");
		assertEquals(1, noFlowWorkLoad.getMinPeriod(), "MinPeriod should return a default value (e.g., 1) for a no-flow graph");
		assertEquals(0, noFlowWorkLoad.maxFlowLength(), "Max flow length should be 0 for a no-flow graph");
	}

	/**
	 * Boundary Case Test: Tests the behavior with just a single flow.
	 * This also addresses the "different input workloads" criterion.
	 */
	@Test
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testWorkloadWithSingleFlow() throws IOException {
		// Arrange: Create a WorkLoad with only one flow.
		String singleFlowContent = "singleFlowGraph {\n" +
		                           "  F1 (0, 99, 99, 0) : A -> B\n" +
		                           "}";
		
		Path testFilePath = Paths.get(TEST_FILE_NAME);
		Files.write(testFilePath, singleFlowContent.getBytes());
		Options options = new Options();
		options.setInputFile(TEST_FILE_NAME);
		WorkLoad singleFlowWorkLoad = new WorkLoad(options);

		// Act & Assert
		assertEquals(1, singleFlowWorkLoad.getFlowNamesInOriginalOrder().size(), "Should have exactly one flow");
		assertEquals("F1", singleFlowWorkLoad.getFlowNamesInOriginalOrder().get(0), "The single flow should be F1");
		assertEquals(99, singleFlowWorkLoad.getMinPeriod(), "MinPeriod should be 99 for the single flow");
		assertEquals(2, singleFlowWorkLoad.maxFlowLength(), "Max flow length should be 2 for the single flow");
	}
	
	
}