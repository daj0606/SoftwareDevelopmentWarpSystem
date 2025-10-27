package edu.uiowa.cs.warp;

import java.io.File;
import javax.swing.JPanel;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import edu.uiowa.cs.warp.ReliabilityParameters.FaultModel;

/**
 * Provides default visualizations as an abstract class.
 * Uses GraphStream for enhanced visualizations that
 * include text in the top pane and an animated graph
 * in the bottom pane.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
abstract class Visualization implements Visualizable {
	
  /**
   * Empty string place holder for unretrieved data.
   */
  protected static final String EMPTY = "";
  
  /**
   * Constant used to validate access to file. 
   * If file name is not found or invalid store "UNKNOWN" as a placeholder.
   */
  protected static final String UNKNOWN = "UNKNOWN";
  
  /**
   * 2 dimensional array that stores visualization data.
   */
  protected String[][] visualizationData;
  
  /**
   * Stores configuration settings used to generate visualization.
   */
  protected Options options;
  
  private static final String NOT_IMPLEMENTED = "This visualization has not been implemented.";
  private String suffix;
  private String nameExtension;
  private GuiWindow window;
  private String fileName;
  private String inputFileName;
  private String fileNameTemplate;
  private FileManager fm = null;
  
  /**
   * Constructor for WorkLoad visualizations. 
   * 
   * @param workLoad to be shown
   * @param suffix for output files
   */
  Visualization(WorkLoad workLoad, String suffix) {
    this(workLoad.getOptions(), suffix);
  }

  /**
   * Constructor for Warp system visualizations.
   * 
   * @param warp system to be shown
   * @param suffix for output files
   */
  Visualization(SystemAttributes warp, String suffix) {
    this(warp.getOptions(),suffix);
    /* Add the scheduler name to the nameExtension created by the other constructor */
    this.nameExtension = String.format("%s-%s", warp.getSchedulerName(), this.nameExtension);
  }

  /**
   * Constructor for WorkLoad visualizations when the WorkLoad 
   * object has not yet been created.
   * 
   * @param options specified for Warp
   * @param suffix suffix for output files
   */
  Visualization(Options options, String suffix) {
    this.options = options; // need to set options first
    this.nameExtension = reliabilityExtension();
    this.suffix = suffix;
    visualizationData = null;
    if (options != null) {
      fm = options.getFileManager();
      inputFileName = options.getInputFileName(); // .toWorkload().getInputFileName();
      fileNameTemplate = fm.createFileNameTemplate(inputFileName, options.getOutputSubDirectory());
    } else {
      fm = new FileManager();
      inputFileName = UNKNOWN;
      fileNameTemplate = UNKNOWN;
    }
  }
  
  /**
   * Presents the visualization on the display using a graphical 
   * window. 
   */
  @Override
  public GuiWindow toDisplay() {
    window = displayVisualization();
    if (window != null) {
      window.setVisible();
    }
    return window;
  }

  /**
   * Returns the window associated with the display window.
   */
  @Override
  public GuiWindow getDisplay() {
    return window;
  }
  
  /**
   * Writes the visualization to a file.
   */
  @Override
  public File toFile() {
    /* create the file name with output directory path. */
    fileName = createFile(fileNameTemplate); 
    /* create the content and write it to the file */
    Description fileContent = fileVisualization();
    fm.writeFile(fileName, fileContent.toString());
    /* return the file handle */
    return new File(fileName);
  }

  /**
   * Returns the file containing the visualization.
   */
  @Override
  public File getFile() {
    /* create the file name with output directory path. */
    fileName = createFile(fileNameTemplate); 
    if (!fm.fileExists(fileName)) {
      /* If a file with that name doesn't yet exists, create it */ 
      toFile();
    }
    /* return the file handle */
    return new File(fileName);
  }
  
  /**
   * Returns a String representing the visualization. .
   */
  @Override
  public String toString() {
    Description visualization = visualization();
    return visualization.toString();
  }

  /**
   * Defines a string that will be added to the file names.
   * 
   * @param newExtension to be added to file names
   */
  protected void setNameExtension(String newExtension) {
    this.nameExtension = newExtension;
  }
  
  /**
   * Defines an string that includes the reliability parameters
   * to be included in file names.
   * 
   * @return reliability parameter name extension
   */
  private String reliabilityExtension() {
    String extension = null;
    if (options != null) {
      FaultModel faultModel = options.getFaultModel();
      String minLQ = String.valueOf(options.getMinPacketReceptionRate());
      String e2e = String.valueOf(options.getE2E());
      extension =  String.format("%sM-%sE2E", minLQ, e2e);
      if (faultModel.equals(FaultModel.FIXED)) {
        String numFaults = String.valueOf(options.getNumFaults());
        extension = String.format("%s-%sF", extension, numFaults);
      }
    } 
    return extension;
  }
  
  /**
   * Appends a string to the existing name extension.
   * 
   * @param nameExtension to be added to the existing name extension
   */
  protected void addNameExtension(String nameExtension) {
    this.nameExtension = this.nameExtension + nameExtension;
  }

  /**
   * Store the Warp options for future reference.
   * 
   * @param options set for Warp
   */
  protected void setOptions (Options options) {
	  this.options = options;
  }
  
  /**
   * @return Warp options
   */
  protected Options getOptions() {
	  return options;
  }

  /**
   * Builds a text-based visualization stored as a Description. This
   * method might enhance the presentation of the core visualization
   * data and even use header helper methods to create a complete
   * text-based visualization.
   * 
   * @return the visualization content
   */
  protected Description visualization() {
    Description content = new Description();
    var data = createVisualizationData();

    if (data != null) {
      String nodeString = String.join("\t", createColumnHeader()) + "\n";
      content.add(nodeString);

      for (int rowIndex = 0; rowIndex < data.length; rowIndex++) {
        var row = data[rowIndex];
        String rowString = String.join("\t", row) + "\n";
        content.add(rowString);
      }
    } else {
      content.add(NOT_IMPLEMENTED);
    }
    return content;
  }

  /**
   * Creates the visualization file using the input file
   * name template, name extensions, and file suffix.
   * 
   * @param fileNameTemplate for visualizations
   * @return file name
   */
  protected String createFile(String fileNameTemplate) {
    return fm.createFile(fileNameTemplate, nameExtension, suffix);
  }

  /**
   * Creates the contents for a file-based visualization.
   * 
   * @return the file contents
   */
  protected Description fileVisualization() {
    Description fileContent = createHeader();
    fileContent.addAll(visualization());
    fileContent.addAll(createFooter());
    return fileContent;
  }

  /**
   * Creates a Gui window that contains the text-based visualization.
   * 
   * @return the window displayed
   */
  protected GuiWindow displayVisualization() {
   /* 
    * stubbed method: not implemented window
    * returned if possible, otherwise null is returned 
    */
    GuiWindow window = null;
    if (options != null) {
    	window = new GuiWindow(options, NOT_IMPLEMENTED, new Description (NOT_IMPLEMENTED));
    }
    return window; // not implemented
  }

  /**
   * Creates an enhanced GuiWindow visualization in which the top portion is a text
   * representation of the visualization and the bottom portion is a graphical
   * representation using GraphStream.
   * 
   * @param graph based for the graphical visualization
   * @param displayTitle of for the window
   * @return
   */
  protected GuiWindow createEnhancedGraphVisualization(Graph graph, String displayTitle) {
	/* 
	 * Create a SwingViewer that doesn't create its own JFrame, so that it 
	 * can be attached to the JFrame of the GuiWindow.
	 */
	Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
	View view = viewer.addDefaultView(false);   // false indicates "no JFrame".
	/* Create the GuiWindow using the input parameters and the visualiation() method. */
	GuiWindow window = new GuiWindow(options,displayTitle, visualization(), (JPanel)view);
	/* enable the Auto layout feature of graph in the visualization */
	viewer.enableAutoLayout();
	return window;
  }
  
  /**
   * Creates a header for (file) visualizations.
   * 
   * @return the contents of the header
   */
  protected Description createHeader() {
    Description header = new Description();
    return header;
  }

  /**
   * Creates a footer for (file) visualizations.
   * 
   * @return the contents of the header
   */
  protected Description createFooter() {
    Description footer = new Description();
    return footer;
  }

  /**
   * Creates the display header for a table displayed in 
   * a window visualization that contains a label for each column. 
   * 
   * @return the column header
   */
  protected String[] createColumnHeader() {
    return new String[] {NOT_IMPLEMENTED};
  }

  /**
   * Create the body of visualization (sans header or
   * footer).
   * 
   * @return the core visualization data.
   */
  protected String[][] createVisualizationData() {
    return visualizationData; // not implemented--returns null
  }
}
