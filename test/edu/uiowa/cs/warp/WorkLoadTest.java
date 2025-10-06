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
