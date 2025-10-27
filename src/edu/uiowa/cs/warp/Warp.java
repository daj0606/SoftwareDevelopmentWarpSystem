/**
 * WARP: On-the-fly Program Synthesis for Agile, Real-time, and Reliable Wireless Networks. This
 * system generates node communication programs WARP uses programs to specify a network’s behavior
 * and includes a synthesis procedure to automatically generate such programs from a high-level
 * specification of the system’s workload and topology. WARP has three unique features: <br>
 * (1) WARP uses a domain-specific language to specify stateful programs that include conditional
 * statements to control when a flow’s packets are transmitted. The execution paths of programs
 * depend on the pattern of packet losses observed at run-time, thereby enabling WARP to readily
 * adapt to packet losses due to short-term variations in link quality. <br>
 * (2) Our synthesis technique uses heuristics to improve network performance by considering
 * multiple packet loss patterns and associated execution paths when determining the transmissions
 * performed by nodes. Furthermore, the generated programs ensure that the likelihood of a flow
 * delivering its packets by its deadline exceeds a user-specified threshold. <br>
 * (3) WARP can adapt to workload and topology changes without explicitly reconstructing a network’s
 * program based on the observation that nodes can independently synthesize the same program when
 * they share the same workload and topology information. Simulations show that WARP improves
 * network throughput for data collection, dissemination, and mixed workloads on two realistic
 * topologies. Testbed experiments show that WARP reduces the time to add new flows by 5 times over
 * a state-of-the-art centralized control plane and guarantees the real-time and reliability of all
 * flows.
 */

package edu.uiowa.cs.warp;

import javax.swing.SwingUtilities;
import edu.uiowa.cs.warp.SystemAttributes.ScheduleChoices;
import edu.uiowa.cs.warp.Visualizable.SystemVisualizations;

/**
 * Driver class to instantiate the Warp system. All methods are static, and only
 * the main method is Public.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class Warp {
<<<<<<< HEAD
	/**
	 * Global options parsed from command-line arguments and used to configure
	 * workload construction, scheduler choice, visualization selection, and verbosity.
	 */
	private static Options warpOptions;

	/**
	 * Verifies that all deadlines are met, end-to-end message transmission
	 * reliabilities are met, and no channel conflicts exist in the Warp programs
	 * that schedule message flow transmissions.
	 * 
	 * @param warp the WARP system to check
	 */
	private static void verifyPerformanceRequirements(WarpInterface warp) {
		verifyDeadlines(warp);
		verifyReliabilities(warp);
		verifyNoChannelConflicts(warp);
	}
=======
  
  /**
   * Global options that configure WARP execution, visualization, and output.
   */
  private static Options warpOptions;
>>>>>>> branch 'HW5' of https://research-git.uiowa.edu/smukunza/cs2820-smukunza

<<<<<<< HEAD
	/**
	 * Verifies that all end-to-end message transmissions reliabilities were met.
	 * 
	 * An error is reported if the probability that at least one message sent from
	 * the flow's source node is not received by the flow's sink node with the
	 * specified end-to-end reliability. This probability is determined by the
	 * minimum link quality in the sensor network and the number of transmissions
	 * (i.e., push or pull commands) attempted at each node in the path.
	 * 
	 * Otherwise, if if all messages were received by the flow's sink node with a
	 * probability at or greater than the specified end-to-end reliability, a
	 * message is printed to the console that all end-to-end reliabilities were met.
	 * 
	 * @param warp system that is checked for reliable transmissions.
	 */
	private static void verifyReliabilities(WarpInterface warp) {
		if (warpOptions.getSchedulerSelected() != ScheduleChoices.RTHART) {
			/* RealTime HART doesn't adhere to reliability targets */
			if (!warp.reliabilitiesMet()) {
				System.err.printf(
						"\n\tERROR: Not all flows meet the end-to-end " + "reliability of %s under %s scheduling.\n",
						String.valueOf(warp.getE2E()), warp.getScheduleChoice().toString());
			} else if (warpOptions.verboseMode()) {
				System.out.printf("\n\tAll flows meet the end-to-end reliability " + "of %s under %s scheduling.\n",
						String.valueOf(warp.getE2E()), warp.getScheduleChoice().toString());
			}
		}
	}
=======
  /**
   * Verifies that all deadlines are met, end-to-end message transmission
   * reliabilities are met, and no channel conflicts exist in the Warp
   * programs that schedule message flow transmissions.
   * @param warp the WARP system instance to verify
   */
  private static void verifyPerformanceRequirements(WarpInterface warp) {
    verifyDeadlines(warp);
    verifyReliabilities(warp);
    verifyNoChannelConflicts(warp);
  }
>>>>>>> branch 'HW5' of https://research-git.uiowa.edu/smukunza/cs2820-smukunza

	/**
	 * Verifies that all deadlines are met. A deadline is missed if the time it
	 * takes to reliably send a message from the flow's source node to the flow's
	 * sink node exceeds the relative deadline parameter specified for the flow.
	 * 
	 * Reports an error and displays a deadline report visualization if a a deadline
	 * is missed in the Warp program. If all deadlines are met, this fact is
	 * reported in the console only when the verbose mode option is set.
	 * 
	 * @param warp system that is checked for deadline misses.
	 */
	private static void verifyDeadlines(WarpInterface warp) {
		if (!warp.deadlinesMet()) {
			System.err.printf("\n\tERROR: Not all flows meet their deadlines under %s scheduling.\n",
					warp.getScheduleChoice().toString());
			VisualizationFactory.createVisualization(warp, SystemVisualizations.DEADLINE_REPORT);
		} else if (warpOptions.verboseMode()) {
			System.out.printf("\n\tAll flows meet their deadlines under %s scheduling.\n",
					warp.getScheduleChoice().toString());
		}
	}

	/**
	 * Reports an error and displays the channel analysis visualization if a channel
	 * conflict exists in the Warp program. A channel conflict occurs when two
	 * different nodes attempt to push or pull a message on the same channel during
	 * the same time slot. If no channel conflict exists, this fact is reported in
	 * the console only when the verbose mode option is set.
	 * 
	 * @param warp system that is checked for channel conflicts.
	 */
	private static void verifyNoChannelConflicts(WarpInterface warp) {
		if (warp.getChannelAnalysis().isChannelConflict()) {
			System.err.printf("\n\tERROR: Channel conficts exists. See Channel Visualization for details.\n");
			if (!warpOptions.caRequested()) { // only need to create the visualization if not already requested
				VisualizationFactory.createVisualization(warp, SystemVisualizations.CHANNEL);
			}
		} else if (warpOptions.verboseMode()) {
			System.out.printf("\n\tNo channel conflicts detected.\n");
		}
	}

	/**
	 * Launch the Warp Gui to interact with the Warp system passed to it.
	 * 
	 * @param warp System model associated with the Gui
	 */
	private static void launchGui(WarpInterface warp) {
		Gui warpGui = Gui.getInstance(warp);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				warpGui.setVisible(true);
			}
		});
	}

	/**
	 * Main entry point for Warp. This class is really just a driver class. This
	 * method is the method called by the Java Virtual Machine when you run the
	 * program. The input arguments are either passed via the command-line when the
	 * program is run from the console. Or, the input arguments are set in the
	 * Arguments tab of the Run Configurations menu for the IDE.
	 * 
	 * @param args input arguments for the program.
	 */
	public static void main(String[] args) {
		/*
		 * Create the Options object. It will set all of the default Warp options and
		 * then parse command-line arguments to override the defaults.
		 */
		warpOptions = new Options(args);

		/*
		 * Now create the workLoad and Warp system with the specified configuration.
		 */
		WorkLoad workLoad = new WorkLoad(warpOptions);
		WarpInterface warp = new WarpSystem(workLoad);

		/*
		 * If the Warp Gui is requested, then launch the Gui. The Gui sets up the views
		 * and launches a controller, which interacts with the Gui and Warp objects as
		 * part of the Model-View-Controller (MVC) software design pattern.
		 */
		if (warpOptions.guiRequested()) {
			launchGui(warp);
		} else {
			/*
			 * If the full Gui wasn't requested, then create some visualizations based on
			 * the options specified. If the display option was set, then both file and
			 * display window visualizations are created.
			 */

			/* first print the options if verbose mode is set. */
			if (warpOptions.verboseMode()) {
				warpOptions.print();
			}

			/*
			 * Use the Visualization Factory to create visualizations based on command-line
			 * arguments and/or default options.
			 */
			if (warpOptions.allRequested()) {
				/*
				 * When the -a or --all option is set, then all visualizations are created.
				 */
				if (warpOptions.schedulerRequested()) {
					/*
					 * Create all visualizations for the Warp System created with the scheduler
					 * selected and verify the performance requirements were met
					 */
					VisualizationFactory.createWarpVisualizations(warp);
					/*
					 * Now verify the deadlines and reliabilities are met with no channel conflicts
					 */
					verifyPerformanceRequirements(warp);
				} else {
					/*
					 * If a scheduler wasn't specified in the command line arguments, then all
					 * visualizations for each scheduler is created.
					 */

					/*
					 * For each scheduler choice, create a corresponding Warp System and all of its
					 * visualizations
					 */
					warpOptions.setAllRequested(true);
					for (ScheduleChoices sch : ScheduleChoices.values()) {
						/* set the scheduler and then create the corresponding Warp System */
						warpOptions.setSchedulerSelected(sch.name());
						workLoad = new WorkLoad(warpOptions);
						warp = new WarpSystem(workLoad);
						/* Now create all visualizations using the Factory */
						VisualizationFactory.createWarpVisualizations(warp);
						verifyPerformanceRequirements(warp);
					}
				}
			} else {
				/*
				 * When the -a or --a option wasn't set by the command-line arguments, create
				 * the System visualizations with the selected scheduler (or default scheduler
				 * if the user didn't specify one in the command-line options), and then verify
				 * the Performance Requirements.
				 */
				VisualizationFactory.createWarpVisualizations(warp);
				verifyPerformanceRequirements(warp);
			}
		}
	}
}
