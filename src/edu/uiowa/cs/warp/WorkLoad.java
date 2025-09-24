package edu.uiowa.cs.warp;

import edu.uiowa.cs.utilities.Utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Build the nodes and flows for the workload described in the workload description file, whose name
 * is passed into the Constructor via Options parameter. 
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class WorkLoad implements ReliabilityParameters {

  private static final Integer DEFAULT_PRIORITY = 0;
  private static final Integer DEFAULT_INDEX = 0;
  private static final Integer DEFAULT_TX_NUM = 0;
  private static final String FLOW_WARNING =
      "\n\tWarning! Bad situation: " + "Flow %s doesn't exist but trying to ";

  private Integer numFaults = 0;
  private Double minPacketReceptionRate = 0.0;
  private Double e2e = 0.0;
  private FaultModel faultModel;
  private FlowMap flows; // map of all flow nodes in the WARP graph (<name, Flow>)
  private NodeMap nodes; // map of all graph nodes in the WARP graph (<name, Node>)
  private String name; // name of the WARP graph defining the workload
  /* array list to hold names of flows to preserve their order */
  private ArrayList<String> flowNamesInOriginalOrder = new ArrayList<>(); 
  private ArrayList<String> flowNamesInPriorityOrder = new ArrayList<>();

  private Options warpOptions;
  
  /**
  * Constructs a new {@code WorkLoad} instance using the provided {@link Options}.
  * <p>
  * This constructor initializes default parameters (flows, nodes, ordering lists),
  * copies/uses the supplied options, sets reliability and fault model parameters,
  * and builds the Nodes and Flows by parsing the workload description file via
  * {@link WorkLoadListener.buildNodesAndFlows(WorkLoad)}.
  * </p>
  *
  * @param options the configuration and workload parameters (input file name,
  * reliability, faults, etc.) for this workload
  */
  WorkLoad (Options options) {
    setDefaultParameters();
    warpOptions = new Options(options);
    minPacketReceptionRate = warpOptions.getMinPacketReceptionRate(); 
    e2e = warpOptions.getE2E(); 
    numFaults = warpOptions.getNumFaults();
    faultModel = warpOptions.getFaultModel();
    /*
     * Read input file, build the AST of graph and the listener will build the node and flow data
     * objects
     */
    WorkLoadListener.buildNodesAndFlows(this);
  }

  private void setDefaultParameters() {
    flows = new FlowMap(); // map of all flow nodes in the WARP graph (<name, Flow>)
    nodes = new NodeMap(); // map of all graph nodes in the WARP graph (<name, Node>)
    flowNamesInOriginalOrder = new ArrayList<>(); // array to hold names of flows to preserve their
    // order
    flowNamesInPriorityOrder = new ArrayList<>();
  }
  /**
   * Gets the user the options
   * @return Options object
   */
  public Options getOptions () {
    return warpOptions;
  }

  /**
   * @return the numFaults
   */
  @Override
  public Integer getNumFaults() {
    return numFaults;
  }

  /**
   * @return the minPacketReceptionRate
   */
  @Override
  public Double getMinPacketReceptionRate() {
    return minPacketReceptionRate;
  }

  /**
   * @return the e2e
   */
  @Override
  public Double getE2E() {
    return e2e;
  }


  @Override
  public FaultModel getFaultModel() {
    return faultModel;
  }

  /**
   * @return the flows
   */
  public FlowMap getFlows() {
    return flows;
  }

  /**
   * @return the nodes
   */
  public NodeMap getNodes() {
    return nodes;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the flowNamesInOriginalOrder
   */
  public ArrayList<String> getFlowNamesInOriginalOrder() {
    return flowNamesInOriginalOrder;
  }

  /**
   * Returns the names of the flows in the most recently determined priority order.
   * <p>
   * The order is set by calling one of the ordering methods such as {@link #setFlowsInPriorityOrder()},
   * {@link #setFlowsInDMorder()}, or {@link #setFlowsInRMorder()}.
   * </p>
   *
   * @return an {@link ArrayList} of flow names in priority order; empty list if none have been set
   */
  public ArrayList<String> getFlowNamesInPriorityOrder() {
    return flowNamesInPriorityOrder;
  }

  /**
   * @param minPacketReceptionRate the minPacketReceptionRate to set
   */
  public void setMinPacketReceptionRate(Double minPacketReceptionRate) {
    this.minPacketReceptionRate = minPacketReceptionRate;
  }

  /**
   * @return the maximum phase of all flows
   */
  public Integer getMaxPhase() {
    var queue = new SchedulableObjectQueue<Flow>(new MaxPhaseComparator<Flow>(), flows.values());
    return queue.poll().getPhase();
  }

  /**
   * @return the minimum period of all flows
   */
  public Integer getMinPeriod() {
    var queue = new SchedulableObjectQueue<Flow>(new PeriodComparator<Flow>(), flows.values());
    return queue.poll().getPeriod();
  }

  /**
   * @param flows the flows to set
   */
  public void setFlows(FlowMap flows) {
    this.flows = flows;
  }

  /**
   * @param nodes the nodes to set
   */
  public void setNodes(NodeMap nodes) {
    this.nodes = nodes;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the list of flow names in original order
   * 
   * @param flowNamesInOriginalOrder the flowNamesInOriginalOrder to set
   */
  public void setFlowNamesInOriginalOrder(ArrayList<String> flowNamesInOriginalOrder) {
    this.flowNamesInOriginalOrder = flowNamesInOriginalOrder;
  }

  /**
   * Sets the list of flow names in priority order
   * 
   * @param flowNamesInPriorityOrder the flowNamesInPriorityOrder to set
   */
  public void setFlowNamesInPriorityOrder(ArrayList<String> flowNamesInPriorityOrder) {
    this.flowNamesInPriorityOrder = flowNamesInPriorityOrder;
  }

  /**
   * @param name the node whose channel is to be set
   * @param channel the channel to set
   */
  public void setNodeChannel(String name, Integer channel) {
    var node = nodes.get(name); // get the node object
    node.setChannel(channel);
    nodes.put(name, node); // update the nodes map with the updated object
  }

  /**
   * @return the node channel
   */
  public Integer getNodeChannel(String name) {
    var node = nodes.get(name); // get the node object
    return node.getChannel();
  }

  public void addFlow(String flowName) {
    /*
     * add a new flow node to the Flows dictionary. Only name, priority, and index are changed from
     * default values priority is set to the number of flows already added (index), 0 for first flow
     * This will create a default priority equal to the order listed in the input graph file. We
     * also set index to the same value so we can preserve that order as a secondary sort key. The
     * initalPriority field is probably not needed, but it might be useful in the future?? If the
     * optional flow parameters (priority, period, ...) is set, then this default priority will be
     * over written
     */
    if (flows.containsKey(flowName)) {
      System.out.printf("\n\tWarning! A flow with name %s already exists. "
          + "It has been replaced with a new flow\n.");
    }
    var index = flows.size();
    var flowNode = new Flow(flowName, index, index);
    flows.put(flowName, flowNode);
    flowNamesInOriginalOrder.add(flowName);
  }

  public void addNodeToFlow(String flowName, String nodeName) {
    if (!nodes.containsKey(nodeName)) { // create the node and add it to nodes if map doesn't have
      // this node already.
      /* If the node already exists, just need to add to the flow */
      var index = nodes.size(); // nodeIndex will be the order added
      var graphNode = new Node(nodeName, DEFAULT_PRIORITY, index); // create a new graph node
      nodes.put(nodeName, graphNode); // add it to the map of nodes
    }
    /*
     * Node is now created and in the nodes map Next we need to get the current flow and add this
     * node to that flow by appending it to the node array for that flow
     */
    var flowNode = getFlow(flowName);
    var graphNode = new Node(nodeName, flowNode.nodes.size(), DEFAULT_INDEX);
    /* the priority is the node's index in the flow, which is the current array size */
    flowNode.addNode(graphNode);
    flowNode.linkTxAndTotalCost.add(DEFAULT_TX_NUM);
  }

/**
 * Retrieves priority ranking of given flows
 * 
 * @param flowName
 * @param nodeName
 * @return index of the source node
 */
  public Integer getFlowPriority(String flowName, String nodeName) {
    var priority = 0;
    var flow = getFlow(flowName);
    Iterator<Node> nodes = flow.nodes.iterator();
    while (nodes.hasNext()) {
      var node = nodes.next();
      if (node.getName() == nodeName) {
        priority = node.getPriority(); // found the src node, set its index
        break;
      }
    }
    return priority;
  }

  /**
   * Sets the priority level for each node
   * 
   * @param flowName the name of the flow getting priority set
   * @param priority integer value assigned to flow
   */
  public void setFlowPriority(String flowName, Integer priority) {
    var flowNode = getFlow(flowName);
    flowNode.setPriority(priority);
  }

  public void setFlowPeriod(String flowName, Integer period) {
    var flowNode = getFlow(flowName);
    flowNode.setPeriod(period);
  }

  public void setFlowDeadline(String flowName, Integer deadline) {
    var flowNode = getFlow(flowName);
    flowNode.setDeadline(deadline);
  }

  public void setFlowPhase(String flowName, Integer phase) {
    var flowNode = getFlow(flowName);
    flowNode.setPhase(phase);
  }

  public Integer getFlowIndex(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.index;
  }

  public Integer getFlowPriority(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.getPriority();
  }

  public Integer getFlowPeriod(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.getPeriod();
  }

  /**
   * Retrieves the deadline for the flow at each given name
   * 
   * @param flowName The name of the flow whose deadline is requested
   * @return deadline for requested flow
   */
  public Integer getFlowDeadline(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.getDeadline();
  }

  public Integer getFlowPhase(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.getPhase();
  }

  public Integer getFlowTxAttemptsPerLink(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.numTxPerLink;
  }

  public void setFlowsInPriorityOrder() {
    // create a list of Flow objects from the FlowMap using the stream interface.
    List<Flow> unsortedFlows = flows.values().stream().collect(Collectors.toList());
    // Now sort by a secondary key, which is index in this case
    List<Flow> sortedByIndex = unsortedFlows.stream().sorted(Comparator.comparing(Flow::getIndex))
        .collect(Collectors.toList());
    // Now sort by primary key, which is priority in this case
    List<Flow> sortedFlows = sortedByIndex.stream().sorted(Comparator.comparing(Flow::getPriority))
        .collect(Collectors.toList());
    // Finally, create a new flowNamesInPriorityOrder that contains the flow names in the requested
    // order
    flowNamesInPriorityOrder = new ArrayList<>();
    sortedFlows.forEach((node) -> flowNamesInPriorityOrder.add(node.getName()));
  }
  
  /**
   * Orders the flows according to Deadline Monotonic (DM) policy.
   * <p>
   * Flows are sorted first by priority (as secondary key), then by deadline (primary key).
   * After sorting, this method updates {@code flowNamesInPriorityOrder} to reflect
   * that DM order.
   * </p>
   */
  public void setFlowsInDMorder() {
    /* create a list of Flow objects from the FlowMap using the stream interface. */
    List<Flow> unsortedFlows = flows.values().stream().collect(Collectors.toList());
    /* Now sort by a secondary key, which is priority in this case */
    List<Flow> sortedByPriority = unsortedFlows.stream()
        .sorted(Comparator.comparing(Flow::getPriority)).collect(Collectors.toList());
    /* Now sort by primary key, which is deadline in this case */
    List<Flow> sortedFlows = sortedByPriority.stream()
        .sorted(Comparator.comparing(Flow::getDeadline)).collect(Collectors.toList());
    /*
     * Finally, create a new flowNamesInPriorityOrder that contains the flow names in the requested
     * order
     */
    flowNamesInPriorityOrder = new ArrayList<>();
    sortedFlows.forEach((node) -> flowNamesInPriorityOrder.add(node.getName()));
  }
  
  /**
   * Orders the flows according to Rate Monotonic (RM) policy.
   * <p>
   * Flows are sorted first by priority (as secondary key), then by period (primary key).
   * After sorting, this method updates {@code flowNamesInPriorityOrder} to reflect
   * that RM order.
   * </p>
   */
  public void setFlowsInRMorder() {
    // create a list of Flow objects from the FlowMap using the stream interface.
    List<Flow> unsortedFlows = flows.values().stream().collect(Collectors.toList());
    // Now sort by a secondary key, which is priority in this case
    List<Flow> sortedByPriority = unsortedFlows.stream()
        .sorted(Comparator.comparing(Flow::getPriority)).collect(Collectors.toList());
    // Now sort by primary key, which is period in this case
    List<Flow> sortedFlows = sortedByPriority.stream().sorted(Comparator.comparing(Flow::getPeriod))
        .collect(Collectors.toList());
    // Finally, create a new flowNamesInPriorityOrder that contains the flow names in the requested
    // order
    flowNamesInPriorityOrder = new ArrayList<>();
    sortedFlows.forEach((node) -> flowNamesInPriorityOrder.add(node.getName()));
  }

  public void setFlowsInRealTimeHARTorder() {
    setFlowsInPriorityOrder(); // use Priority order for RealTimeHART
  }

  public void finalizeCurrentFlow(String flowName) {
    if (faultModel.equals(FaultModel.FIXED)) {
      finalizeFlowWithFixedFaultTolerance(flowName);
    } else {
      finalizeFlowWithE2eParameters(flowName);
    }
  }

  public Integer nextReleaseTime(String flowName, Integer currentTime) {
    var flow = getFlow(flowName);
    flow.setLastUpdateTime(currentTime);
    flow.setNextReleaseTime(currentTime);
    return flow.getReleaseTime(); // next release Time at or after currentTime
  }

  public Integer nextAbsoluteDeadline(String flowName, Integer currentTime) {
    var flow = getFlow(flowName);
    flow.setLastUpdateTime(currentTime);
    flow.setNextReleaseTime(currentTime);
    return flow.getReleaseTime() + flow.getDeadline(); // next deadline after currentTime
  }

  private void finalizeFlowWithE2eParameters(String flowName) {
    var flowNode = flows.get(flowName);
    var m = minPacketReceptionRate; // shorten the name :-)
    if (flowNode != null) {
      var nodes = flowNode.nodes;
      int nHops = nodes.size();
      if (nHops < 1) {
        /*
         * number of hops in flow, but make sure it will be at least 1, else it isn't a flow! || was
         * -1 at end
         */
        nHops = 2;
      }
      Double nTx = 1.0; // set nTx to 1 by default (1 transmission per link required at a minimum
      // and when m == 1.0
      if (m < 1.0) {
        /*
         * now compute nTXper link based on Ryan's formula: log(1 - e2e^(1/hops)) / log(1 - M) = #
         * txs per hop
         */
        nTx = Math.log((1.0 - Math.pow(e2e, (1.0 / (double) nHops)))) / Math.log(1.0 - m);
      }
      /* set numTxPerLink based on M, E2E, and flow length */
      flowNode.numTxPerLink = (int) Math.ceil(nTx);
      /* Now compute nTx per link to reach E2E requirement. */
      ArrayList<Integer> linkTxAndTotalCost =
          numTxAttemptsPerLinkAndTotalTxAttempts(flowNode, e2e, m, true);
      flowNode.linkTxAndTotalCost = linkTxAndTotalCost;
      flows.put(flowName, flowNode); // update flow node in Flows array
    } else { // should never happen...
      System.out.printf("\n\tWarning! Bad situation: Flow %s doesn't exist but "
          + "trying to get its numTxPerLink property\n.", flowName);
    }
  }

  private void finalizeFlowWithFixedFaultTolerance(String flowName) {
    var flowNode = flows.get(flowName);
    if (flowNode != null) {
      /* set numTxPerLink based on numFaults */
      flowNode.numTxPerLink = numFaults + 1;
      /* Now compute nTx per link to reach E2E requirement. */
      ArrayList<Integer> linkTxAndTotalCost = getFixedTxPerLinkAndTotalTxCost(flowNode);
      flowNode.linkTxAndTotalCost = linkTxAndTotalCost;
      flows.put(flowName, flowNode); // update flow node in Flows array
    } else { // should never happen...
      System.out.printf("\n\tWarning! Bad situation: Flow %s doesn't exist but "
          + "trying to get its numTxPerLink property\n.", flowName);
    }
  }

  private ArrayList<Integer> getFixedTxPerLinkAndTotalTxCost(Flow flow) {
    var nodesInFlow = flow.nodes;
    var nNodesInFlow = nodesInFlow.size();
    ArrayList<Integer> txArrayList = new ArrayList<Integer>();
    /*
     * Each node will have at most numFaults+1 transmissions. Because we don't know which nodes will
     * send the message over an edge, we give the cost to each node.
     */
    for (int i = 0; i < nNodesInFlow; i++) {
      txArrayList.add(numFaults + 1);
    }
    /*
     * now compute the maximum # of TX, assuming at most numFaults occur on an edge per period, and
     * each edge requires at least one successful TX.
     */
    var numEdgesInFlow = nNodesInFlow - 1;
    var maxFaultsInFlow = numEdgesInFlow * numFaults;
    txArrayList.add(numEdgesInFlow + maxFaultsInFlow);
    return txArrayList;
  }

  private ArrayList<Integer> numTxAttemptsPerLinkAndTotalTxAttempts(Flow flow, Double e2e, Double M,
      boolean optimizationRequested) {
    var nodesInFlow = flow.nodes;
    var nNodesInFlow = nodesInFlow.size(); // The last entry will contain the worst-case cost of
    // transmitting E2E in isolation
    // var nPushes = Array(repeating: 0, count: nNodesInFlow+1);
    var nPushes = new Integer[nNodesInFlow + 1]; // Array to track nPushes for each node in this
    // flow (same as nTx per link)
    Arrays.fill(nPushes, 0); // initialize to all 0 values
    var nHops = nNodesInFlow - 1;
    /* minLinkReliablityNeded is the minimum reliability needed per link in a flow to hit 
     * E2E reliability for the flow. use max to handle rounding error when e2e == 1.0
     */
    Double minLinkReliablityNeded = Math.max(e2e, Math.pow(e2e, (1.0 / (double) nHops))); 

    /*
     * Now compute reliability of packet reaching each node in the given time slot
     * Start with a 2-D reliability window that is a 2-D matrix of no size
     * each row is a time slot, stating at time 0
     * each column represents the reliability of the packet reaching that node at the
     * current time slot (i.e., the row it is in)
     * will add rows as we compute reliabilities until the final reliability is reached
     * for all nodes.
     */
    var reliabilityWindow = new Vector<Vector<Double>>();
    var newReliabilityRow = new Vector<Double>();
    for (int i = 0; i < nNodesInFlow; i++) {
      newReliabilityRow.add(0.0); // create the the row initialized with 0.0 values
    }
    reliabilityWindow.add(newReliabilityRow); // now add row to the reliability window, Time 0
    Vector<Double> tmpVector = reliabilityWindow.get(0);
    var currentReliabilityRow = tmpVector.toArray(new Double[tmpVector.size()]);
    // var currentReliabilityRow = (Double[]) reliabilityWindow.get(0).toArray();

    /*
     * Want reliabilityWindow[0][0] = 1.0 (i.e., P(packet@FlowSrc) = 1
     * but I din't want to mess with the newReliablityRow vector I use below
     * So, we initialize this first entry to 1.0, which is reliabilityWindow[0][0]
     * We will then update this row with computed values for each node and put it
     * back in the matrix
     */
    currentReliabilityRow[0] = 1.0; // initialize (i.e., P(packet@FlowSrc) = 1
    Double e2eReliabilityState = currentReliabilityRow[nNodesInFlow - 1]; 
    /* the analysis will end when the 2e2 reliability matrix is met, initially the
     * state is not met and will be 0 with this statement
     */
    var timeSlot = 0; // start time at 0
    /* use a while loop and increment timeSlot because
     * we don't know how long this schedule window will last
     */
    while (e2eReliabilityState < e2e) { 
      var prevReliabilityRow = currentReliabilityRow;
      currentReliabilityRow = newReliabilityRow.toArray(new Double[newReliabilityRow.size()]); 
      /* would be reliabilityWindow[timeSlot] if working through a schedule
       * Now use each flow:src->sink to update reliability computations
       * this is the update formula for the state probabilities
       * nextState = (1 - M) * prevState + M*NextHighestFlowState
       * use MinLQ for M in above equation
       * NewSinkNodeState = (1-M)*PrevSnkNodeState + M*PrevSrcNodeState
       */

      /* loop through each node in the flow and update the sates for sink->src pair) */
      for (int nodeIndex = 0; nodeIndex < (nNodesInFlow - 1); nodeIndex++) { 
        var flowSrcNodeindex = nodeIndex;
        var flowSnkNodeindex = nodeIndex + 1;
        var prevSrcNodeState = prevReliabilityRow[flowSrcNodeindex];
        var prevSnkNodeState = prevReliabilityRow[flowSnkNodeindex];
        Double nextSnkState;
        /* do a push until PrevSnk state > e2e to ensure next node
         * reaches target E2E BUT skip if no chance of success (i.e.,
         * source doesn't have packet)
         */
        if ((prevSnkNodeState < minLinkReliablityNeded) && prevSrcNodeState > 0) { 
          /* need to continue attempting to Tx, so update current state */
          nextSnkState = ((1.0 - M) * prevSnkNodeState) + (M * prevSrcNodeState); 
          /* increment the number of pushes for for this node to snk node */
          nPushes[nodeIndex] += 1; 
        } else {
          /* snkNode has met its reliability. Thus move on to the next node 
           * and record the reliability met
           */
          nextSnkState = prevSnkNodeState;   
        }

        /* probabilities are non-decreasing so update if we were higher by carrying old value forward */
        if (currentReliabilityRow[flowSrcNodeindex] < prevReliabilityRow[flowSrcNodeindex]) { 
          /* carry forward the previous state for the src node, which may get over written later
           * by another instruction in this slot.
           */
          currentReliabilityRow[flowSrcNodeindex] = prevReliabilityRow[flowSrcNodeindex]; 
        }
        currentReliabilityRow[flowSnkNodeindex] = nextSnkState;
      }

      e2eReliabilityState = currentReliabilityRow[nNodesInFlow - 1];
      Vector<Double> currentReliabilityVector = new Vector<Double>();
      /* convert the row to a vector so we can add it to the reliability window */
      Collections.addAll(currentReliabilityVector, currentReliabilityRow);
      if (timeSlot < reliabilityWindow.size()) {
        reliabilityWindow.set(timeSlot, (currentReliabilityVector));
      } else {
        reliabilityWindow.add(currentReliabilityVector);
      }
      timeSlot += 1; // increase to next time slot
    }
    /* The total (worst-case) cost to transmit E2E in isolation with
     * specified reliability target is the number of rows in the reliabilityWindow
     */
    var size = reliabilityWindow.size();
    nPushes[nNodesInFlow] = size; 
    /* Now convert the array to the ArrayList needed to return */
    ArrayList<Integer> nPushesArrayList = new ArrayList<Integer>();
    Collections.addAll(nPushesArrayList, nPushes);
    return nPushesArrayList;
  }


  public String[] getNodeNamesOrderedAlphabetically() {
    var nodes = getNodes();
    Set<String> keys = nodes.keySet(); // get the names from the node map
    String[] nodeNames = keys.toArray(new String[keys.size()]);
    Arrays.sort(nodeNames); // NodeNames are now sorted
    // However, if names are actually strings of integers, then the sort doesn't come out
    // the way we would like. So, handle that case
    var nodeNamesAsInts = new Integer[nodeNames.length];
    var allIntNames = true; // flag to see if all names are Ints or not
    for (int i = 0; i < nodeNames.length; i++) {
      var nodeName = nodeNames[i];
      if (Utilities.isInteger(nodeName) && allIntNames) {
        // nodeName is an alpha representation of an integer
        nodeNamesAsInts[i] = Integer.parseInt(nodeName);
      } else {
        // nodeName is an alpha name and not an integer, so set flag and terminate loop
        allIntNames = false; // set false because name not is a number; && above makes sure we only
        // set it once
        break; // can stop the loop once we know not all of the names are ints
      }
    }
    if (allIntNames) {
      // If all names are ints, then we need to sort them accordingly
      // Otherwise, we get names in what appears to not be in order because
      Arrays.sort(nodeNamesAsInts); // sort the ints in assending order
      for (int i = 0; i < nodeNamesAsInts.length; i++) {
        nodeNames[i] = Integer.toString(nodeNamesAsInts[i]); // convert int to string
      }
    }
    return nodeNames;
  }

  // private function to the flow node with specified name
  private Flow getFlow(String flowName) {
    var flow = flows.get(flowName); // get the requested flow node
    if (flow == null) {// return empty node if not found
      flow = new Flow();
      System.out.printf(FLOW_WARNING + "retrieve it\n.", flowName);
    }
    return flow;
  }

  // public function to return an array of flow names in the order they were read from
  // the graph file
  public String[] getFlowNames() {
    return flowNamesInOriginalOrder.toArray(new String[0]);
    // could use new String[list.size()], but due to JVM optimizations new (new String[0] is better
  }

  // public function to return the dictionary of nodes
  public Integer getNodeIndex(String nodeName) {
    var index = 0;
    var node = nodes.get(nodeName); // could throw an exception if null, but just return 0 for now
    if (node != null) {
      index = node.getIndex();
    }
    return index;
  }
  /**
  * Returns the ordered list of node names that constitute the specified flow.
  * <p>
  * The returned array preserves the order in which nodes appear in the flow
  * specification (e.g., as in the input graph file).
  * </p>
  *
  * @param flowName the name of the flow whose nodes are requested
  * @return a string array of node names in the flow, in the flow’s internal order;
  * returns an empty array if no flow with the given name exists
  */
  public String[] getNodesInFlow(String flowName) {
    // get the flow node for requested Flow and then loop through the
    // nodes in the flow to create an array of the node names in
    // the same order as they exist in the flow specification (i.e., Graph file)
    var flow = flows.get(flowName);
    String[] nodes;
    if (flow != null) {
      nodes = new String[flow.nodes.size()];
      for (int i = 0; i < nodes.length; i++) {
        var node = flow.nodes.get(i); // get the node from the arrayList
        nodes[i] = node.getName(); // get the name and store it in the array
      }

    } else {
      nodes = new String[0]; // return empty array
      System.out.printf("\n\t Warning! No Flow with name %s.\n", flowName);
    }
    return nodes;
  }

  public Integer getHyperPeriod() {
    var hyperPeriod = 1; // hyperPeriod is LCM of all periods. Initialize to 1
    for (String flowName : getFlowNames()) {;
    /* Find LCM of hyperPeriod so far and the current period, then update the
     * hyperPeriod with that value
     */
    hyperPeriod = Utilities.lcm(hyperPeriod, getFlowPeriod(flowName)); 

    }
    return hyperPeriod;
  }

  public Integer getTotalTxAttemptsInFlow(String flowName) {
    var flow = getFlow(flowName);
    var linkTxAndTotalCost = flow.getLinkTxAndTotalCost();
    var totalCostIndex = linkTxAndTotalCost.size() - 1;
    var totalCost = linkTxAndTotalCost.get(totalCostIndex);
    return totalCost;
  }

  // return an array of the number of transmission needed for each
  // link (i.e. edge in the flow graph) to meet E2E target
  public Integer[] getNumTxAttemptsPerLink(String flowName) {
    var flow = getFlow(flowName);
    var linkTxAndTotalCost = new ArrayList<Integer>(flow.getLinkTxAndTotalCost());
    var lastElement = linkTxAndTotalCost.size() - 1;
    /* remove the last element, because that is the sum of others */
    linkTxAndTotalCost.remove(lastElement); 

    return linkTxAndTotalCost.toArray(new Integer[0]);
  }


  public void addEdge(String nodeName, Edge edge) {
    var node = nodes.get(nodeName); // get the node object
    node.addEdge(edge);
  }

  public Integer maxFlowLength() {
    Integer maxLength = 0;
    for (Flow flow : flows.values()) {
      maxLength = Math.max(maxLength, flow.nodes.size());
    }
    return maxLength;
  }

  public String getInputGraph() {
    FileManager fm = warpOptions.getFileManager();
    String inputGraphString = fm.readGraphFile(warpOptions.getInputFileName());
    return inputGraphString;
  }
  
  public static void main(String[] args) {
	  Options warpOptions = new Options(); 
	  warpOptions.setInputFile("StressTest.txt");
	  WorkLoad wl = new WorkLoad(warpOptions);
	  WorkLoadVisualization wlv = new WorkLoadVisualization(warpOptions);
	  Description description = wlv.visualization();
	  Map<String,String> byName = new LinkedHashMap<>();
	  
	  //Task e.1
	  System.out.println(wl.getName());
	  wl.setFlowsInDMorder(); //Set Flow in DM order. Otherwise getFlowsInPriorityOrder is an empty arr.
	  System.out.println(wl.getFlowNamesInPriorityOrder());


	  List<String> dmNames = wl.getFlowNamesInPriorityOrder(); // 2) names in DM order

	  String[] lines = description.toString().split("\\R"); // split by line breaks

	  // Parse for Name -> line map from the Description block
	  for (String line : lines) {
	      // skip header/footer like "StressTest {" and "}"
	      if (line.trim().isEmpty() || line.contains("{") || line.equals("}")) continue;
	      // extract flow name before the '('
	      // e.g., "F1 (1, 20, 20, 0) : B -> C -> D"
	      int idx = line.indexOf('(');
	      if (idx > 0) {
	          String name = line.substring(0, idx).trim();
	          byName.put(name, line);
	      }
	  }

	  // Output: DM order
	  System.out.println(wl.getName()); 
	  for (String name : dmNames) {
	      String line = byName.get(name);
	      if (line != null) System.out.println(line);
	  }
  }

}
