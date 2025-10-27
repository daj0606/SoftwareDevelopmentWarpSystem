package edu.uiowa.cs.warp;

/**
 * Builds a visualization of the Warp workload as
 * defined by the input file. 
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class WorkLoadVisualization extends Visualization {

  private static final String INPUT_FILE_SUFFIX = ".wld";
  private Description description;
  private String inputGraphString;
  private String inputFileName;
  
  /**
   * Creates a visualization of the workload specified by the input
   * file in the Options object.
   * 
   * @param options for the Warp System
   */
  WorkLoadVisualization(Options options) {
    super(options, INPUT_FILE_SUFFIX); // Visualization constructor
    inputFileName = options.getInputFileName();
    initialize(inputFileName);
    /* clear the default name extension because the workload description
     * is independent of reliability parameters or the scheduler used
     * to create a program.
     */
    setNameExtension(EMPTY);
  }

  @Override
  protected GuiWindow displayVisualization() {
    return new GuiWindow(options, inputFileName, visualization());
  }
  
  @Override
  protected Description visualization() {
    return description;
  }

  @Override
  protected Description fileVisualization() {
    return description;
  }

  @Override
  public String toString() {
    return inputGraphString;
  }

  private void initialize(String inputFile) {
	/* Get the input graph file name and read its contents */
    FileManager fm = options.getFileManager();
    inputGraphString = fm.readGraphFile(inputFile);
    /* Update the input file name from the FileManager in case it had
     * to to get a new file name from the user, because of an error
     * reading the original name.
     */
    this.inputFileName = fm.getGraphFileName();
    /* Store input graph describing the workload as a Description object
    /* with each line of the file is an entry (string).
     */
    description = new Description(inputGraphString);
  }
}
