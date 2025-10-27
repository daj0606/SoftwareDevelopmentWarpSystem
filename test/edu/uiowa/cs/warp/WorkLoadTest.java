<<<<<<< HEAD
/**
 * 
 */
package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the following methods in WorkLoad.java:  
 * 
 * setFlowsInDMorder(),
 * getFlowNamesInPriorityOrder(),
 * getNodeChannel(),
 * getNodesInFlow(),
 * setFlowNamesInOriginalOrder(),
 * setFlowsInPriorityOrder(),
 * getMinPeriod(),
 * maxFlowLength(),
 * getFlowDeadline(),
 * getNodeNamesOrderedAlphabetically()
 * 
 * @author Benjamin Kleiman
 * Oct 3, 2025
 */
class WorkLoadTest {
	
	/** Instantiating the WorkLoad object used to obtain the functions results*/
	private WorkLoad workload;
	
	/** Instantiating the ArrayList object used to store the expected results*/
	private ArrayList<String> expected;
	
	/** Instantiating the NodeMap object used to add nodes to the workload*/
	private NodeMap nodeMap;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {  
		// Initialize new option and workload objects
		Options option = new Options();
		workload = new WorkLoad(option);
		
		// Default parameters have existing flows/nodes, want to clear them
		FlowMap emptyMap = new FlowMap();
		workload.setFlows(emptyMap);
		NodeMap nodes = new NodeMap();
		workload.setNodes(nodes);
		
		// Initialize new ArrayList to store expected results
		expected = new ArrayList<>();
		
		// Initialize nodeMap
		nodeMap = new NodeMap();
	}

	// Helper method that adds a flow and populates it with a list of node names
	private void addFlowWithNodes(String flowName, String... nodeNames) {
	    workload.addFlow(flowName);
	    for (String node : nodeNames) {
	        workload.addNodeToFlow(flowName, node);
	    }
	}

	// Helper method that adds a node to the node map with an optional channel (null is allowed)
	private void addNodeToNodeMap(String nodeName, Integer channel) {
	    Node node = new Node(nodeName, 0, 0);
	    node.setChannel(channel);
	    nodeMap.put(nodeName, node);
	    workload.setNodes(nodeMap);
	}
	

	// Helper method that sets deadline and priority for a flow
	private void setFlowDeadlineAndPriority(String flowName, int deadline, int priority) {
	    workload.setFlowDeadline(flowName, deadline);
	    workload.setFlowPriority(flowName, priority);
	}
	
	// Helper method that adds multiple flows at once
	private void addMultipleFlows(String... flowNames) {
	    for (String flow : flowNames) {
	        workload.addFlow(flow);
	    }
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowNamesInPriorityOrder()}.
	 */
    @Test
    void testGetEmptyPriorityOrderList() {
    	// workload is initialized with flows being empty
        assertTrue(workload.getFlowNamesInPriorityOrder().isEmpty(),
                "Should return an empty list when no flows are defined.");
    }
    
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowNamesInPriorityOrder()}.
	 */
    @Test
    void testGetterReturnsExactlyWhatWasSet() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("FlowOne");
        expected.add("FlowTwo");

        workload.setFlowNamesInPriorityOrder(expected);

        // The getter should return the same list object
        assertSame(expected, workload.getFlowNamesInPriorityOrder(),
            "Getter should return the exact same list object that was set");
    }
    
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInPriorityOrder()}.
	 */
	@Test
	void testSetFlowsInPriorityOrder() {
	    addMultipleFlows("FlowOne", "FlowTwo", "FlowThree");

	    setFlowDeadlineAndPriority("FlowOne", 0, 3);
	    setFlowDeadlineAndPriority("FlowTwo", 0, 1);
	    setFlowDeadlineAndPriority("FlowThree", 0, 2);

        workload.setFlowsInPriorityOrder();
        
        // Lower number means the flow has higher priority 
        expected.add("FlowTwo"); // priority 1
        expected.add("FlowThree"); // priority 2
        expected.add("FlowOne"); // priority 3

        assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
        		"Flow names should be sorted and returned in priority order");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInPriorityOrder()}.
	 */
	@Test
	void testSetAlreadySortedPriorityFlows() {
	    addMultipleFlows("FlowOne", "FlowTwo", "FlowThree");

	    setFlowDeadlineAndPriority("FlowOne", 0, 1);
	    setFlowDeadlineAndPriority("FlowTwo", 0, 2);
	    setFlowDeadlineAndPriority("FlowThree", 0, 3);

	    workload.setFlowsInPriorityOrder();

	    expected.add("FlowOne");
	    expected.add("FlowTwo");
	    expected.add("FlowThree");

	    assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
	        "Flow names should remain in correct order if already sorted by priority");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInPriorityOrder()}.
	 */
	@Test
	void testSetSinglePriorityFlow() {
	    workload.addFlow("OnlyFlow");
	    workload.setFlowPriority("OnlyFlow", 5);

	    workload.setFlowsInPriorityOrder();

	    expected.add("OnlyFlow");

	    assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
	        "Single flow should result in a list with one element");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInPriorityOrder()}.
	 */
	@Test
	void testSetEmptyPriorityFlow() {
	    workload.setFlowsInPriorityOrder();

	    assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
	        "Empty workload should result in an empty flowNamesInPriorityOrder list");
	}
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getMinPeriod()}.
	 */
	@Test
    void testGetMinPeriodZeroFlows() {
		// Expect a NullPointerException when trying to get min period of a FlowMap that doesn't exist
		assertThrows(NullPointerException.class, () -> {
			workload.getMinPeriod();
	    });
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getMinPeriod()}.
	 */
	@Test
    void testGetMinPeriodOneFlow() {
        workload.addFlow("FlowOne");
        workload.setFlowPeriod("FlowOne", 50);
        assertEquals(50, workload.getMinPeriod(), "Min period should be 50 when only one flow exists");
    }

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getMinPeriod()}.
	 */
	@Test
    void testGetMinPeriodThreeFlowsDifferentPeriods() {
	    addMultipleFlows("FlowOne", "FlowTwo", "FlowThree");
	    workload.setFlowPeriod("FlowOne", 50);
	    workload.setFlowPeriod("FlowTwo", 70);
	    workload.setFlowPeriod("FlowThree", 30);
        assertEquals(30, workload.getMinPeriod(), "Min period should be 30 among three different flows");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getMinPeriod()}.
	 */
	@Test
    void testGetMinPeriodThreeFlowsSamePeriods() {
	    addMultipleFlows("FlowOne", "FlowTwo", "FlowThree");
	    workload.setFlowPeriod("FlowOne", 30);
	    workload.setFlowPeriod("FlowTwo", 30);
	    workload.setFlowPeriod("FlowThree", 30);
        assertEquals(30, workload.getMinPeriod(), "Min period should be 30 among three different flows");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowNamesInOriginalOrder(java.util.ArrayList)}.
	 */
    @Test
    void testSetFlowNamesInOriginalOrderEmpty() {
        ArrayList<String> flows = new ArrayList<>();
        workload.setFlowNamesInOriginalOrder(flows);
        assertEquals(flows, workload.getFlowNamesInOriginalOrder(), 
        		"Flows should be returned as empty");
    }
    
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowNamesInOriginalOrder(java.util.ArrayList)}.
	 */
    @Test
    void testSetFlowNamesInOriginalOrderNull() {
    	workload.setFlowNamesInOriginalOrder(null);
        assertNull(workload.getFlowNamesInOriginalOrder(), "Should return null");
    }
    
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowNamesInOriginalOrder(java.util.ArrayList)}.
	 */
    @Test
    void testSetFlowNamesInOriginalOrderThreeFlows() {
        ArrayList<String> flows = new ArrayList<>(Arrays.asList("FlowTwo", "FlowOne", "FlowThree"));
        workload.setFlowNamesInOriginalOrder(flows);
        assertEquals(flows, workload.getFlowNamesInOriginalOrder(), 
        		"Flows should be returned in the initial order");
    }
    
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowNamesInOriginalOrder(java.util.ArrayList)}.
	 */
    @Test
    void testSetFlowNamesInOriginalOrderWithDifferentPriorityOrder() {
        ArrayList<String> flows = new ArrayList<>(Arrays.asList("FlowTwo", "FlowOne", "FlowThree"));
	    workload.setFlowPriority("FlowOne", 1);
	    workload.setFlowPriority("FlowTwo", 2);
	    workload.setFlowPriority("FlowThree", 3);
        workload.setFlowNamesInOriginalOrder(flows);
        assertEquals(flows, workload.getFlowNamesInOriginalOrder(), 
        		"Flows should be returned in the initial order");
    }
    
    /**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeChannel(java.lang.String)}.
	 */
	@Test
    public void testGetNodeChannelNonexistantNode() {		
	    // Expect a NullPointerException when trying to get channel of a node that doesn't exist
	    assertThrows(NullPointerException.class, () -> {
	        workload.getNodeChannel("node1");
	    });
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeChannel(java.lang.String)}.
	 */
	@Test
    public void testGetNodeChannelOneNode() {		
	    addNodeToNodeMap("node1", 5);
        assertEquals(5, workload.getNodeChannel("node1"), "Should return the channel value 5");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeChannel(java.lang.String)}.
	 */
	@Test
    public void testGetNodeChannelNullValueNode() {		
		addNodeToNodeMap("node1", null);
        assertNull(workload.getNodeChannel("node1"), "Should return the channel value null");
    }

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeChannel(java.lang.String)}.
	 */
	@Test
    public void testGetNodeChannelMultipleNodes() {		
	    addNodeToNodeMap("node1", 5);
	    addNodeToNodeMap("node2", 3);
	    addNodeToNodeMap("node3", 2);
        assertEquals(5, workload.getNodeChannel("node1"), "Should return the channel value 5");
        assertEquals(3, workload.getNodeChannel("node2"), "Should return the channel value 3");
        assertEquals(2, workload.getNodeChannel("node3"), "Should return the channel value 2");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowDeadline(java.lang.String)}.
	 */
	@Test
    void testGetFlowDeadlineNonexistantFlow() {
	    // Even a non-existant flow should return the default value as per getFlowDeadline error handling
	    assertEquals(100, workload.getFlowDeadline("Flow"), 
	    	"Should return 100 (the default value) for a nonexistant flow");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowDeadline(java.lang.String)}.
	 */
	@Test
    void testGetFlowDeadlineOneFlow() {
		workload.addFlow("FlowOne");
	    workload.setFlowDeadline("FlowOne", 50);
        assertEquals(50, workload.getFlowDeadline("FlowOne"),
                "Should return the deadline 50 for newly added flow");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowDeadline(java.lang.String)}.
	 */
	@Test
    void testGetFlowDeadlineThreeDifferentFlows() {
		addMultipleFlows("FlowOne", "FlowTwo", "FlowThree");
	    setFlowDeadlineAndPriority("FlowOne", 50, 0);
	    setFlowDeadlineAndPriority("FlowTwo", 100, 0);
	    setFlowDeadlineAndPriority("FlowThree", 200, 0);
	    
        assertEquals(50, workload.getFlowDeadline("FlowOne"),
                "Should return the deadline 50 for the first flow");
        assertEquals(100, workload.getFlowDeadline("FlowTwo"),
                "Should return the deadline 100 for the second flow");
        assertEquals(200, workload.getFlowDeadline("FlowThree"),
                "Should return the deadline 200 for the third flow");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInDMorder()}.
	 */
	@Test
    void testSetFlowsInDMorderSamePriorityDifferentDeadlines() {
		addMultipleFlows("FlowOne", "FlowTwo", "FlowThree");

	    // Different deadlines and same priorities
	    setFlowDeadlineAndPriority("FlowOne", 20, 1);
	    setFlowDeadlineAndPriority("FlowTwo", 15, 1);
	    setFlowDeadlineAndPriority("FlowThree", 30, 1);

        // Expected sort order: FlowTwo, followed by FlowOne, then FlowThree
        workload.setFlowsInDMorder();

        expected.add("FlowTwo");
        expected.add("FlowOne");
        expected.add("FlowThree");

        assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
            "Flows should be sorted by deadline");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInDMorder()}.
	 */
	@Test
    void testSetFlowsInDMorderDifferentPrioritySameDeadlines() {
		addMultipleFlows("FlowOne", "FlowTwo", "FlowThree");

	    // Deadlines same, different priorities
	    setFlowDeadlineAndPriority("FlowOne", 5, 1);
	    setFlowDeadlineAndPriority("FlowTwo", 5, 3);
	    setFlowDeadlineAndPriority("FlowThree", 5, 2);
	    
        // Expected sort order: FlowOne, followed by FlowThree, then FlowTwo
        workload.setFlowsInDMorder();

        expected.add("FlowOne");
        expected.add("FlowThree");
        expected.add("FlowTwo");

        assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
            "Flows should be sorted by priority");
    }
	
	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInDMorder()}.
	 */
	@Test
    void testSetFlowsInDMorderTortureTest() {
		addMultipleFlows("FlowOne", "FlowTwo", "FlowThree", "FlowFour", "FlowFive", "FlowSix",
                "FlowSeven", "FlowEight", "FlowNine", "FlowTen");

		setFlowDeadlineAndPriority("FlowOne", 1, 1);
		setFlowDeadlineAndPriority("FlowTwo", 2, 3);
		setFlowDeadlineAndPriority("FlowThree", 3, 2);
		setFlowDeadlineAndPriority("FlowFour", 10, 4);
		setFlowDeadlineAndPriority("FlowFive", 2, 2);
		setFlowDeadlineAndPriority("FlowSix", 1, 2);
		setFlowDeadlineAndPriority("FlowSeven", 1, 4);
		setFlowDeadlineAndPriority("FlowEight", 8, 3);
		setFlowDeadlineAndPriority("FlowNine", 4, 2);
		setFlowDeadlineAndPriority("FlowTen", 4, 1);
        
        /*
         * Expected order:
         * F1, F6, F7, F5, F2, F3, F10, F9, F8, F4
         */
        workload.setFlowsInDMorder();

        expected.add("FlowOne");
        expected.add("FlowSix");
        expected.add("FlowSeven");
        expected.add("FlowFive");
        expected.add("FlowTwo");
        expected.add("FlowThree");
        expected.add("FlowTen");
        expected.add("FlowNine");
        expected.add("FlowEight");
        expected.add("FlowFour");
      

        assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
            "Flows should be sorted by deadline first, and priority second");
    }
	

    /**
     * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInDMorder()}.
     */
    @Test
    void testSetFlowsInDMorderAlreadySorted() {
        addMultipleFlows("FlowOne", "FlowTwo", "FlowThree");

        setFlowDeadlineAndPriority("FlowOne", 5, 1);
        setFlowDeadlineAndPriority("FlowTwo", 10, 2);
        setFlowDeadlineAndPriority("FlowThree", 20, 3);

        workload.setFlowsInDMorder();

        expected.add("FlowOne");
        expected.add("FlowTwo");
        expected.add("FlowThree");

        assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
            "Flows already in DM order should remain unchanged");
    }

    /**
     * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInDMorder()}.
     */
    @Test
    void testSetFlowsInDMorderSingleFlow() {
        workload.addFlow("SoloFlow");
        setFlowDeadlineAndPriority("SoloFlow", 4, 5);

        workload.setFlowsInDMorder();

        expected.add("SoloFlow");

        assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
            "Single flow should result in one-element list");
    }

    /**
     * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInDMorder()}.
     */
    @Test
    void testSetFlowsInDMorderEmptyFlows() {
        workload.setFlowsInDMorder();

        assertEquals(expected, workload.getFlowNamesInPriorityOrder(),
            "Empty workload should result in an empty flowNamesInPriorityOrder list");
    }


	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
	 */
	@Test
    void testGetNodeNamesEmpty() {
        String[] result = workload.getNodeNamesOrderedAlphabetically();
        assertArrayEquals(new String[]{}, result, "No nodes should return an empty array");
    }

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
	 */
	@Test
    void testGetNodeNamesOneString() {
        // Add a flow and assign one node
		addFlowWithNodes("FlowOne", "Alpha");

        String[] result = workload.getNodeNamesOrderedAlphabetically();
        assertArrayEquals(new String[]{"Alpha"}, result, "Single node name should be returned as-is");
    }

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
	 */
	@Test
    void testGetNodeNamesOneInteger() {
		addFlowWithNodes("FlowOne", "5");

        String[] result = workload.getNodeNamesOrderedAlphabetically();
        assertArrayEquals(new String[]{"5"}, result, "Single numeric node name should be returned as-is");
    }

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
	 */
	@Test
    void testGetNodeNamesThreeStrings() {
		addFlowWithNodes("FlowOne", "Charlie", "Alpha", "Bravo");

        String[] result = workload.getNodeNamesOrderedAlphabetically();
        assertArrayEquals(new String[]{"Alpha", "Bravo", "Charlie"}, result,
            "Alphabetic strings should be sorted alphabetically");
    }

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
	 */
	@Test
    void testGetNodeNamesThreeIntegers() {
		addFlowWithNodes("FlowOne", "10", "2", "1");

        String[] result = workload.getNodeNamesOrderedAlphabetically();
        assertArrayEquals(new String[]{"1", "2", "10"}, result,
            "Numeric node names should be sorted numerically");
    }

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
	 */
	@Test
    void testGetNodeNamesTortureMixedIntAndString() {
		addFlowWithNodes("FlowOne",
		        "Zebra", "1", "Alpha", "10", "Bravo", "2", "Delta", "Echo", "Foxtrot", "3");

        String[] result = workload.getNodeNamesOrderedAlphabetically();

        // Lexicographic sort (not numeric!) since not all names are integers
        String[] expected = {
            "1", "10", "2", "3", "Alpha", "Bravo", "Delta", "Echo", "Foxtrot", "Zebra"
        };
        Arrays.sort(expected); // simulate Java's default lex sort

        assertArrayEquals(expected, result,
            "Mixed numeric and string names should be sorted alphabetically (not numerically)");
    }

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodesInFlow(java.lang.String)}.
	 */
	@Test
	void testGetNodesInFlowMultipleNodes() {
		addFlowWithNodes("FlowOne", "NodeA", "NodeB", "NodeC");

	    String[] expected = {"NodeA", "NodeB", "NodeC"};

	    assertArrayEquals(expected, workload.getNodesInFlow("FlowOne"), 
	    		"Nodes should be returned in the order added to the flow");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodesInFlow(java.lang.String)}.
	 */
	@Test
	void testGetNodesInFlowSingleNode() {
		addFlowWithNodes("FlowOne", "NodeOnly");
	    String[] expected = {"NodeOnly"};

	    assertArrayEquals(expected, workload.getNodesInFlow("FlowOne"), 
	    		"Flow with one node should return a single-element array");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodesInFlow(java.lang.String)}.
	 */
	@Test
	void testGetNodesInFlowEmptyFlow() {
	    workload.addFlow("FlowOne");

	    String[] expected = {};

	    assertArrayEquals(expected, workload.getNodesInFlow("FlowOne"), 
	    		"Flow with no nodes should return an empty array");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodesInFlow(java.lang.String)}.
	 */
	@Test
	void testGetNodesInFlowNonExistentFlow() {
	    String[] expected = {};

	    assertArrayEquals(expected, workload.getNodesInFlow("NonExistentFlow"), 
	    		"Non-existent flow should return an empty array");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
	 */
	@Test
	void testMaxFlowLengthNoFlows() {
	    assertEquals(0, workload.maxFlowLength(),
	        "No flows should return max length of 0");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
	 */
	@Test
	void testMaxFlowLengthSingleFlowOneNode() {
		addFlowWithNodes("FlowOne", "NodeA");

	    assertEquals(1, workload.maxFlowLength(),
	        "Single flow with one node should return 1");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
	 */
	@Test
	void testMaxFlowLengthSingleFlowMultipleNodes() {
		addFlowWithNodes("FlowOne", "NodeA", "NodeB", "NodeC");

	    assertEquals(3, workload.maxFlowLength(),
	        "Single flow with three nodes should return 3");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
	 */
	@Test
	void testMaxFlowLengthMultipleFlows() {
		addFlowWithNodes("FlowOne", "NodeA");
	    addFlowWithNodes("FlowTwo", "NodeB", "NodeC");
	    addFlowWithNodes("FlowThree", "NodeD", "NodeE", "NodeF", "NodeG");

	    assertEquals(4, workload.maxFlowLength(),
	        "Should return the length of the longest flow (4)");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
	 */
	@Test
	void testMaxFlowLengthMultipleFlowsSameLength() {
		addFlowWithNodes("FlowOne", "NodeA", "NodeB");
	    addFlowWithNodes("FlowTwo", "NodeC", "NodeD");

	    assertEquals(2, workload.maxFlowLength(),
	        "When multiple flows have the same max length, that length should be returned");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
	 */
	@Test
	void testMaxFlowLengthWithEmptyFlowIncluded() {
		addFlowWithNodes("FlowOne"); // no nodes
	    addFlowWithNodes("FlowTwo", "NodeA");

	    assertEquals(1, workload.maxFlowLength(),
	        "Empty flows should not affect the max length calculation");
	}

}
=======
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
>>>>>>> branch 'HW5' of https://research-git.uiowa.edu/smukunza/cs2820-smukunza
