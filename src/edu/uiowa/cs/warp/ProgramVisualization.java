package edu.uiowa.cs.warp;

/**
 * Builds a visualization of the Warp program. The program is sometimes called a
 * scheduled because it defines the order in which flows transmit their messages
 * through the sensor network. The display graph is created with GraphStream.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 * 
 */
public class ProgramVisualization extends Visualization {

	private static final String SOURCE_SUFFIX = ".dsl"; // Indicates the type of src file being visualized.
	private ProgramSchedule sourceCode; // The schedule table of the current program.
	private Program program; // The WARP program model being visualized.
	private Boolean deadlinesMet; // True if all flows meet their deadlines, false otherwise.
	private Options warpOptions; // Runtime options that control visualization behavior.

	/**
	 * Constructs a new visualization for the given WARP system.
	 * 
	 * @param warp the WARP system containing the program, schedule, deadlines, and options
	 */
	ProgramVisualization(WarpInterface warp) {
		super(warp, SOURCE_SUFFIX);
		this.program = warp.getProgram();
		this.sourceCode = program.getSchedule();
		this.deadlinesMet = warp.deadlinesMet();
		this.warpOptions = warp.getOptions();
	}

	/**
	 * Builds and returns the GUI window that displays the program schedule.
	 * 
	 * @return a GuiWindow configured with headers, columns, and data
	 */
	@Override
	protected GuiWindow displayVisualization() {
		return new GuiWindow(warpOptions, createTitle(), createColumnHeader(), createVisualizationData());
	}

	/**
	 * Creates the header section of the visualization
	 * 
	 * @return a Description object containing the header information
	 */
	@Override
	protected Description createHeader() {
		Description header = new Description();

		header.add(createTitle());
		header.add(String.format("Scheduler Name: %s\n", program.getSchedulerName()));

		/*
		 * The following parameters are output based on a special schedule or the fault
		 * model
		 */
		if (program.getNumFaults() > 0) { // only specify when deterministic fault model is assumed
			header.add(String.format("numFaults: %d\n", program.getNumFaults()));
		}
		header.add(String.format("M: %s\n", String.valueOf(program.getMinPacketReceptionRate())));
		header.add(String.format("E2E: %s\n", String.valueOf(program.getE2E())));
		header.add(String.format("nChannels: %d\n", program.getNumChannels()));
		return header;
	}

	/**
	 * Creates the footer section of the visualization.
	 * 
	 * @return a Description object containing the footer information
	 */
	@Override
	protected Description createFooter() {
		Description footer = new Description();
		String deadlineMsg = null;

		if (deadlinesMet) {
			deadlineMsg = "All flows meet their deadlines\n";
		} else {
			deadlineMsg = "WARNING: NOT all flows meet their deadlines. See deadline analysis report.\n";
		}
		footer.add(String.format("// %s", deadlineMsg));
		return footer;
	}

	/**
	 * Creates the column headers for the schedule table.
	 * 
	 * @return an array of column header strings 
	 */
	@Override
	protected String[] createColumnHeader() {
		var orderedNodes = program.toWorkLoad().getNodeNamesOrderedAlphabetically();
		String[] columnNames = new String[orderedNodes.length + 1];
		columnNames[0] = "Time Slot"; // add the Time Slot column header first
		/* loop through the node names, adding each to the header */
		for (int i = 0; i < orderedNodes.length; i++) {
			columnNames[i + 1] = orderedNodes[i];
		}
		return columnNames;
	}

	/**
	 * Builds the 2D string table that represents the program schedule.
	 * 
	 * @return a 2D string array representing the schedule table
	 */
	@Override
	protected String[][] createVisualizationData() {
		if (visualizationData == null) {
			int numRows = sourceCode.getNumRows();
			int numColumns = sourceCode.getNumColumns();
			visualizationData = new String[numRows][numColumns + 1];

			for (int row = 0; row < numRows; row++) {
				visualizationData[row][0] = String.format("%s", row);
				for (int column = 0; column < numColumns; column++) {
					visualizationData[row][column + 1] = sourceCode.get(row, column);
				}
			}
		}
		return visualizationData;
	}

	/**
	 * Creates the visualization title string.
	 * 
	 * @return a title string containing the program name 
	 */
	private String createTitle() {
		return String.format("WARP program for graph %s\n", program.getName());
	}
}
