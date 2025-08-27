package edu.uiowa.cs.warp;

import java.awt.Font;
import java.nio.file.Paths;

import javax.swing.JTable;
import javax.swing.UIManager;
import argparser.ArgParser;
import argparser.BooleanHolder;
import argparser.DoubleHolder;
import argparser.IntHolder;
import argparser.StringHolder;
import edu.uiowa.cs.warp.SystemAttributes.ScheduleChoices;
import edu.uiowa.cs.warp.ReliabilityParameters.FaultModel;

/**
 * Options used to build the Warp System. Default Options are defined here, but
 * they can be overridden by command-line options or the graphical user interface.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class Options {
  
  private static final Integer MAX_CHANNELS = 20; // max number of wireless channels available
  private static final Integer NUM_CHANNELS = 16; // default number of wireless channels available
  // for scheduling (command line option)
  private static final Double MIN_RELIABILTY = 0.5; // minimum link and e2e reliability in system 
  private static final Double MAX_RELIABILTY = 1.0; // max link and e2e reliability in system
  private static final Integer MAX_FAULTS = 10; // max link and e2e reliability in system 
  /** The minimum rate at which packets are received over any
   * link in the system. This is normally at least 0.7. 
   */
  private static final Double MIN_PACKET_RECEPTION_RATE = 0.9; 
  private static final Double E2E = 0.99; // default end-to-end reliability for all flows (command
  // line option)
  private static final String DEFAULT_OUTPUT_SUB_DIRECTORY = "OutputFiles/";
  private static final String DEFAULT_INPUT_FILE = "Example.txt";
  private static final ScheduleChoices DEFAULT_SCHEDULER = ScheduleChoices.PRIORITY;
  /* default number of faults to be tolerated per transmission (command-line option */
  private static final Integer DEFAULT_FAULTS_TOLERATED = 0; // => FaultModel.PROBABILISTIC
  private static final Double DEFAULT_FONT_SCALE_FACTOR = 1.0; // 100% => no scaling
  
  private Integer nChannels; // number of wireless channels available for scheduling
  private Integer numFaults; // number of faults tolerated per edge
  /** The minimum rate at which packets are received over any
   * link in the system. This rate can be changed by a command-line option.
   */
  private Double minPacketReceptionRate; 
  private Double e2e; // global variable for minimum Link Quality in system, later we can add
                             // local minPacketReceptionRate for each link
  /* default output subdirectory (from working directory)
   * where output files will be placed (e.g., gv, wf, ra)
   */
  private String outputSubDirectory;
  private Double fontScaleFactor; // amount by which fonts are scaled for GUI
  private Boolean guiRequested; // Gui Visualization selected
  private Boolean displayRequested; // Display Visualization selected
  private Boolean gvRequested; // GraphVis file requested flag
  private Boolean wfRequested; // WARP file requested flag
  private Boolean raRequested; // Reliability Analysis file requested flag
  private Boolean laRequested; // Latency Analysis file requested flag
  private Boolean caRequested; // Channel Analysis file requested flag
  private Boolean eaRequested; // Execution Analysis file requested flag
  private Boolean simRequested; // Simulation file requested flag
  private Boolean allRequested; // all out files requested flag
  private Boolean latencyRequested; // latency report requested flag
  private Boolean schedulerRequested = false;
  private Boolean verboseMode; // verbose mode flag (mainly for running in IDE)
  private String inputFile; // inputFile from which the graph workload is read
  private ScheduleChoices schedulerSelected; // Scheduler requested
  private FaultModel faultModel;
  private String currentDirectory;
  private FileManager fm;

  /**
   * Default constructor. Sets default options for the Warp System.
   */
  public Options () {
    setDefaultOptions();
    /* The Default input graph file name may not be a valid file. 
     * ensureFileExists() will either get a good file or 
     * terminate the program.
     */
    ensureFileExists(getInputFileName());
  }
  
  /**
   * Constructor that parses the input parameter string to set options for the Warp System.
   */
  public Options (String[] args) {
    setWarpOptions(args);
    /* setWarpOptions ensures the input graph file exists, 
     * so no need to check here. 
     */
  }
  
  /**
   * Preference copy constructor. Makes a copy of the input options object
   * so that the current options can be archived and saved with a GuiWindow
   * object.
   * 
   * @param sourceToCopy Options object to clone
   */
  public Options (Options sourceToCopy) {
    this.nChannels = sourceToCopy.getNumChannels();
    this.minPacketReceptionRate = sourceToCopy.getMinPacketReceptionRate();
    this.e2e = sourceToCopy.getE2E();
    this.numFaults = sourceToCopy.getNumFaults();
    this.outputSubDirectory = sourceToCopy.getOutputSubDirectory();
    this.schedulerSelected = sourceToCopy.getSchedulerSelected();
    this.guiRequested = sourceToCopy.guiRequested();
    this.displayRequested = sourceToCopy.displayRequested();
    this.gvRequested = sourceToCopy.gvRequested();
    this.wfRequested = sourceToCopy.wfRequested();
    this.raRequested = sourceToCopy.raRequested();
    this.laRequested = sourceToCopy.laRequested();
    this.caRequested = sourceToCopy.caRequested();
    this.eaRequested = sourceToCopy.eaRequested();
    this.simRequested = sourceToCopy.simRequested();
    this.allRequested = sourceToCopy.allRequested();
    this.latencyRequested = sourceToCopy.latencyRequested();
    this.schedulerRequested = sourceToCopy.schedulerRequested();
    this.verboseMode = sourceToCopy.verboseMode();
    this.inputFile = sourceToCopy.getInputFileName();
    this.faultModel = sourceToCopy.getFaultModel();
    this.currentDirectory = sourceToCopy.getCurrentDirectory();
    this.fm = sourceToCopy.getFileManager();
    this.fontScaleFactor = sourceToCopy.getFontScaleFactor();
   
  }
  
  public StringBuilder getCoreOptionsForViewing() {
    StringBuilder optionsText = new StringBuilder();
    optionsText.append("Current Workload: ").append(this.getInputFileName()).append("\n");
    optionsText.append("Current Scheduler: ").append(this.getSchedulerName()).append("\n");
    if (this.getNumFaults() > 0) {
      optionsText.append("Number of Faults Tolerated: ").append(this.getNumFaults()).append("\n");
    }
    optionsText.append("Min Link Quality: ").append(this.getMinPacketReceptionRate()).append("\n");
    optionsText.append("End-to-End Reliability: ").append(this.getE2E()).append("\n");
    optionsText.append("Fault Model: ").append(this.getFaultModel()).append("\n");
    optionsText.append("Current Directory: ").append(this.getCurrentDirectory()).append("\n");
    optionsText.append("Output Files SubDirectory: ").append(this.getOutputSubDirectory()).append("\n");
    optionsText.append("Verbose Mode: ").append(this.verboseMode()).append("\n");
    
    // JOptionPane.showMessageDialog(parentFrame, optionsText.toString(), "Current Option Settings", JOptionPane.INFORMATION_MESSAGE);
    return optionsText;
}
  
  /* Overriding equals() to compare this Options to input object */
  @Override
  public boolean equals(Object o) {

    /* If the object is compared with itself then return true  */ 
    if (o == this) {
      return true;
    }

    /* Check if o is an instance of Options or not 
     * "null instanceof [type]" also returns false 
     */
    if (!(o instanceof Options)) {
      return false;
    }

    /* typecast o to Complex so that we can compare data members */
    Options oOptions = (Options) o;

    /* Compare the data members and return accordingly  */
    return this.nChannels.equals(oOptions.getNumChannels()) && 
        this.minPacketReceptionRate.equals(oOptions.getMinPacketReceptionRate()) && 
        this.e2e.equals(oOptions.getE2E()) &&
        this.numFaults.equals(oOptions.getNumFaults()) &&
        this.outputSubDirectory.equals(oOptions.getOutputSubDirectory()) &&
        this.schedulerSelected.equals(oOptions.getSchedulerSelected()) &&
        this.guiRequested.equals(oOptions.guiRequested()) &&
        this.displayRequested.equals(oOptions.displayRequested()) &&
        this.gvRequested.equals(oOptions.gvRequested()) &&
        this.wfRequested.equals(oOptions.wfRequested()) &&
        this.raRequested.equals(oOptions.raRequested()) &&
        this.laRequested.equals(oOptions.laRequested()) &&
        this.caRequested.equals(oOptions.caRequested()) &&
        this.eaRequested.equals(oOptions.eaRequested()) &&
        this.simRequested.equals(oOptions.simRequested()) &&
        this.allRequested.equals(oOptions.allRequested()) &&
        this.latencyRequested.equals(oOptions.latencyRequested()) &&
        this.schedulerRequested.equals(oOptions.schedulerRequested()) &&
        this.verboseMode.equals(oOptions.verboseMode()) &&
        this.inputFile.equals(oOptions.getInputFileName()) &&
        this.faultModel.equals(oOptions.getFaultModel()) &&
        this.currentDirectory.equals(oOptions.getCurrentDirectory()) &&
        this.fontScaleFactor.equals(oOptions.getFontScaleFactor()); // &&
        // this.schedulerName.equals(oOptions.getSchedulerName()) ;
    /* not checking FileManager for a match; don't think we need to */
  }
  
  public FileManager getFileManager() {
	  return fm;
  }
  
  public String getCurrentDirectory() {
    return currentDirectory;
  }

  /** Update currentDirectory if the new currentDirectory
   * is not null and different from the stored value.
   * @param currentDirectory
   */
  public void setCurrentDirectory(String currentDirectory) {
    if (fm.directoryExists(currentDirectory)) {
      this.currentDirectory = currentDirectory;
    }
  }
  
  public String getOutputSubDirectory() {
    return outputSubDirectory;
  }

  public void setOutputSubDirectory(String outputSubDirectory) {
    if (fm.isValidDirectoryName(outputSubDirectory)) {
      this.outputSubDirectory = outputSubDirectory;
    }
  }

  public String getInputFileName() {
    return inputFile;
  }
  
  public void ensureFileExists(String file) {
    setInputFile(fm.verifyGraphFileName(file));
  }
  
  public void setInputFile(String inputFile) {
    if (fm.fileExists(inputFile)) {
      this.inputFile = inputFile;
    }
  }
  
  public Double getE2E() {
    return e2e;
  }
  
  public void setE2E(Double e2e) {
    if (e2e >= MIN_RELIABILTY && e2e <= MAX_RELIABILTY) {
    this.e2e = e2e;
    }
  }

  public void setE2E(String e2e) {
    Double converted = convertStringToDouble(e2e, getE2E());
    setE2E(converted);
  }
  
  public Double getMinPacketReceptionRate() {
    return minPacketReceptionRate;
  }
  
  public void setMinPacketReceptionRate(Double minPacketReceptionRate) {
    if (minPacketReceptionRate >= MIN_RELIABILTY && minPacketReceptionRate <= MAX_RELIABILTY) {
    this.minPacketReceptionRate = minPacketReceptionRate;
    }
  }

  public void setMinPacketReceptionRate(String minPacketReceptionRate) {
    Double converted = convertStringToDouble(minPacketReceptionRate, getMinPacketReceptionRate());
    setMinPacketReceptionRate(converted);
  }
  
  
  public Integer getNumChannels() {
    return nChannels;
  }

  public void setnChannels(Integer nChannels) {
    if (nChannels > 0 && nChannels <= MAX_CHANNELS) {
    this.nChannels = nChannels;
    }
  }
  
  public void setnChannels(String nChannels) {
    Integer converted = convertStringToInteger(nChannels, getNumChannels());
    setnChannels(converted);
  }

  public Integer getNumFaults() {
    return numFaults;
  }

  public void setNumFaults(Integer numFaults) {
    if (numFaults >= 0 && numFaults <= MAX_FAULTS ) {
    this.numFaults = numFaults;
    }
    setFaultModel(this.numFaults);
  }

  public void setNumFaults(String numFaults) {
    Integer converted = convertStringToInteger(numFaults, getNumChannels());
    setNumFaults(converted);
  }
  
  public Boolean verboseMode() {
    return verboseMode;
  }

  public void setVerboseMode(Boolean verboseMode) {
    this.verboseMode = verboseMode;
  }

  public Boolean guiRequested() {
    return guiRequested;
  }

  public Boolean displayRequested() {
    return displayRequested;
  }
  
  public Boolean gvRequested() {
    return gvRequested;
  }

  public void setGvRequested(Boolean gvRequested) {
    this.gvRequested = gvRequested;
  }
  
  public Boolean wfRequested() {
    return wfRequested;
  }

  public void setWfRequested(Boolean wfRequested) {
    this.wfRequested = wfRequested;
  }
  
  public Boolean raRequested() {
    return raRequested;
  }

  public void setRaRequested(Boolean raRequested) {
    this.raRequested = raRequested;
  }

  public Boolean laRequested() {
    return laRequested;
  }

  public void setLaRequested(Boolean laRequested) {
    this.laRequested = laRequested;
  }


  public Boolean caRequested() {
    return caRequested;
  }

  public void setCaRequested(Boolean caRequested) {
    this.caRequested = caRequested;
  }

  public Boolean eaRequested() {
    return eaRequested;
  }

  public void setEaRequested(Boolean eaRequested) {
    this.eaRequested = eaRequested;
  }
  
  public Boolean allRequested() {
    return allRequested;
  }

  public void setAllRequested(Boolean allRequested) {
    this.allRequested = allRequested;
  }
  
  public Boolean latencyRequested() {
    return latencyRequested;
  }

  public void setLatencyRequested(Boolean latencyRequested) {
    this.latencyRequested = latencyRequested;
  }

  public Boolean schedulerRequested() {
    return schedulerRequested;
  }
  
  public Boolean simRequested() {
    return simRequested;
  }

  public ScheduleChoices getSchedulerSelected() {
    return schedulerSelected;
  }
  
  public void setSchedulerSelected (String name) {
	  schedulerSelected = DEFAULT_SCHEDULER;
	  if (name != null) { // can't switch on a null value so check then switch
		  switch (name.toLowerCase()) {
		  case "priority":
			  schedulerSelected = ScheduleChoices.PRIORITY;
			  break;
		  case "rm":
			  schedulerSelected = ScheduleChoices.RM;
			  break;
		  case "dm":
			  schedulerSelected = ScheduleChoices.DM;
			  break;
		  case "rthart":
			  schedulerSelected = ScheduleChoices.RTHART;
			  break;
		  case "poset":
		  case "poset_priority":
			  schedulerSelected = ScheduleChoices.POSET_PRIORITY;
			  break;
		  case "poset_rm":
			  schedulerSelected = ScheduleChoices.POSET_RM;
			  break;
		  case "poset_dm":
			  schedulerSelected = ScheduleChoices.POSET_DM;
			  break;
		  case "warp_poset":
		  case "warp_poset_priority":
			  schedulerSelected = ScheduleChoices.WARP_POSET_PRIORITY;
			  break;
		  case "warp_poset_rm":
			  schedulerSelected = ScheduleChoices.WARP_POSET_RM; 
			  break;
		  case "warp_poset_dm":
			  schedulerSelected = ScheduleChoices.WARP_POSET_DM;
			  break;
		  case "connectivity_poset":
		  case "connectivity_poset_priority":
			  schedulerSelected = ScheduleChoices.CONNECTIVITY_POSET_PRIORITY; 
			  break;
		  case "connectivity_poset_rm" :
			  schedulerSelected = ScheduleChoices.CONNECTIVITY_POSET_RM; 
			  break;
		  case "connectivity_poset_dm" :
			  schedulerSelected = ScheduleChoices.CONNECTIVITY_POSET_DM; 
			  break;default:
			  schedulerSelected = ScheduleChoices.PRIORITY;
			  break;
		  }
	  } 
  }
  
  public String getFaultModelName() {
    FaultModel model = getFaultModel();
    return model.toString();
  }
  
  public String getSchedulerName() {
    return schedulerSelected.toString();
  }
  
  public FaultModel getFaultModel() {
    return faultModel;
  }
  
  private void setFaultModel (int numFaults) {
    if (numFaults > 0) {
      faultModel = FaultModel.FIXED;
    } else {
      faultModel = FaultModel.PROBABILISTIC;
    }
  }
  
  public Double getFontScaleFactor() {
	  return fontScaleFactor;
  }
  
  private Double convertStringToDouble (String number, Double defaultValue) {
    Double result = defaultValue;
    try {
      Double convertedNumber = Double.parseDouble(number);
      result = convertedNumber;
    } catch (NumberFormatException e) {
      // System.out.println("Invalid string format for conversion to Double.");
    }
    return result;
  }

  private Integer convertStringToInteger (String number, int defaultValue) {
    Integer result = defaultValue;
    try {
      Integer convertedNumber = Integer.parseInt(number);
      result = convertedNumber;
    } catch (NumberFormatException e) {
      // System.out.println("Invalid string format for conversion to Integer.");
    }
    return result;
  }
 
  private void setDefaultOptions () {
	this.verboseMode = false;
    this.nChannels = NUM_CHANNELS;
    this.minPacketReceptionRate = MIN_PACKET_RECEPTION_RATE;
    this.e2e = E2E;
    this.numFaults = DEFAULT_FAULTS_TOLERATED;
    this.outputSubDirectory = DEFAULT_OUTPUT_SUB_DIRECTORY;
    this.schedulerSelected = DEFAULT_SCHEDULER;
    this.inputFile = DEFAULT_INPUT_FILE;
    currentDirectory = Paths.get("").toAbsolutePath().toString();
    setFaultModel(numFaults);
    faultModel = getFaultModel();
    fm = new FileManager();
    fontScaleFactor = DEFAULT_FONT_SCALE_FACTOR;
    setNewDefaultFontSize(DEFAULT_FONT_SCALE_FACTOR); 
  }
  
  public void setNewDefaultFontSize (double scaleFactor) {
	  scaleFont("MenuItem.font", scaleFactor);
	  scaleFont("Menu.font", scaleFactor);
	  scaleFont("MenuBar.font", scaleFactor);
	  scaleFont("PopupMenu.font", scaleFactor);
	  scaleFont("Button.font", scaleFactor);
	  scaleFont("Label.font", scaleFactor);
	  scaleFont("TextField.font", scaleFactor);
	  scaleFont("TextArea.font", scaleFactor);
	  scaleFont("ComboBox.font", scaleFactor);
	  scaleFont("TableHeader.font", scaleFactor);
	  scaleFont("Table.font", scaleFactor);
  }
  
  /**
   * Scale the font size of the component by the scale factor. If the
   * scale factor == 1, then no change. If less than 1, then shrinks.
   * If greater than 1, then increases.
   * 
   * @param component whose font will be scaled
   * @param scaleFactor amount by which the font is scaled
   */
  private void scaleFont (String component, double scaleFactor) {
	 Font defaultFont = UIManager.getFont(component);
	 int newFontSize = (int) ((double)defaultFont.getSize() * scaleFactor); // increase if > 1
	 Font newFont =  new Font(defaultFont.getName(), Font.PLAIN, newFontSize);
	 UIManager.put(component, newFont);
  } 
  
  /**
   * Scale the table row height by the font scale factor option. 
   * 
   * @param table whose row height will be modified.
   */
  public void scaleTableRowHeight (JTable table) {
	  scaleTableRowHeight (table, getFontScaleFactor());
  }
  
  /**
   * Scale the table row height by the font scale factor option. 
   * 
   * @param table whose row height will be modified.
   * @param scaleFactor by which the height is changed.
   */
  public void scaleTableRowHeight (JTable table, double scaleFactor) {
	  /* Retrieve the table row height and scale it
       * by the same scaleFactor, so that tables rows will scale
       * to the new font size
       */
      int defaultRowHeight = table.getRowHeight(); 
      int newRowHeight = (int) ((double)defaultRowHeight * scaleFactor);
      /* Sets the default row height to scaled height in pixels */
      table.setRowHeight(newRowHeight); 
  }
  
  private void setWarpOptions(String[] args) { 
    /* create holder objects for storing results ... */
    StringHolder scheduler = new StringHolder();
    IntHolder channels = new IntHolder();
    IntHolder faults = new IntHolder();
    DoubleHolder m = new DoubleHolder();
    DoubleHolder end2end = new DoubleHolder();
    BooleanHolder gui = new BooleanHolder();
    BooleanHolder display = new BooleanHolder();
    BooleanHolder gv = new BooleanHolder();
    BooleanHolder wf = new BooleanHolder();
    BooleanHolder ra = new BooleanHolder();
    BooleanHolder la = new BooleanHolder();
    BooleanHolder ca = new BooleanHolder();
    BooleanHolder ea = new BooleanHolder();
    BooleanHolder s = new BooleanHolder();
    BooleanHolder all = new BooleanHolder();
    BooleanHolder latency = new BooleanHolder();
    BooleanHolder verbose = new BooleanHolder();
    StringHolder input = new StringHolder();
    StringHolder output = new StringHolder();
    DoubleHolder fontScaler = new DoubleHolder(); 

    /* New create the parser to read the command line options to be set. 
     * After creating the parser, specify the allowed options ... then parse args
     */
    ArgParser parser = new ArgParser("java -jar warp.jar");
    parser.addOption("-sch, --schedule %s {priority,rm,dm,rtHart,poset} #scheduler options",
        scheduler);
    parser.addOption("-c, --channels %d {[1,16]} #number of wireless channels", channels);
    parser.addOption("-m %f {[0.5,1.0]} #minimum link quality in the system", m);
    parser.addOption(
        "-e, --e2e %f {[0.5,1.0]} #global end-to-end communcation reliability for all flows",
        end2end);
    parser.addOption("-f, --faults %d {[0,10]} #number of faults per edge in a flow (per period)",
        faults);
    parser.addOption("-gui %v #use the Graphical User Interface (GUI)", gui);
    parser.addOption("-d, --display %v #create a display window for visualizations", display);
    parser.addOption("-sf %f {[0.5,4.0]} #font scale factor for GUI", // (1.0 => no scale)",
            fontScaler);
    parser.addOption("-gv %v #create a graph visualization (.gv) file for GraphViz", gv);
    parser.addOption(
        "-wf  %v #create a WARP (.wf) file that shows the maximum number of transmissions on each segment of the flow needed to meet the end-to-end reliability",
        wf);
    parser.addOption(
        "-ra  %v #create a reliability analysis file (tab delimited .csv) for the warp program",
        ra);
    parser.addOption(
        "-la  %v #create a latency analysis file (tab delimited .csv) for the warp program", la);
    parser.addOption(
        "-ca  %v #create a channel analysis file (tab delimited .csv) for the warp program", ca);
    parser.addOption(
        "-ea  %v #create an execution analysis file (tab delimited .csv) for the warp program", ea);
    parser.addOption("-s  %v #create a simulator input file (.txt) for the warp program", s);
    parser.addOption("-a, --all  %v #create all output files (activates -gv, -wf, -ra, -ca -ea -la -l -s)", all);
    parser.addOption("-l, --latency  %v #generates end-to-end latency report file (.txt)", latency);
    parser.addOption("-i, --input %s #<InputFile> of graph flows (workload)", input);
    parser.addOption("-o, --output %s #<OutputDIRECTORY> where output files will be placed",
        output);
    parser.addOption(
        "-v, --verbose %v #Echo input file name and parsed contents. Then for each flow instance: show maximum E2E latency and min/max communication cost for that instance of the flow",
        verbose);

    /* Set the default WARP system configuration */
    setDefaultOptions();
    
    /* Now parse the input arguments */
    parser.matchAllArgs(args);
    
    /* Update WARP system configuration options based on args parsing */
    if (channels.value > 0) {
      nChannels = channels.value; // set option specified
    }
    if (faults.value > 0) { // global variable for # of Faults tolerated per edge
      numFaults = faults.value; // set option specified
    } 
    if (m.value > 0.0) { // global variable for minimum Link Quality in system
      minPacketReceptionRate = m.value; // set option specified
    } 
    if (fontScaler.value > 0.0) { // value by which fonts are scaled for GUI
    	fontScaleFactor = fontScaler.value; // set option specified
    	setNewDefaultFontSize(fontScaleFactor);
    } 
    if (end2end.value > 0.0) { // global variable for minimum Link Quality in system
      e2e = end2end.value; // set option specified
    } 
    if (output.value != null) { // default output subdirectory (from working directory)
      outputSubDirectory = output.value; // set option specified
    } 

    guiRequested = gui.value; // GUI requested flag
    displayRequested = display.value; // Display visualizations requested flag
    gvRequested = gv.value; // GraphVis file requested flag
    wfRequested = wf.value; // WARP file requested flag
    raRequested = ra.value; // Reliability Analysis file requested flag
    laRequested = la.value; // Latency Analysis file requested flag
    caRequested = ca.value; // Channel Analysis file requested flag
    eaRequested = ea.value; // Execution Analysis file requested flag
    simRequested = s.value; // Simulation file requested flag
    allRequested = all.value; // all out files requested flag
    latencyRequested = latency.value; // latency report requested flag
    verboseMode = verbose.value; // verbose mode flag (mainly for running in IDE)
    if (input.value != null) {
      /* input file specified, so update the inputFile attribute */
      inputFile = input.value; 
    }
    if (scheduler.value != null) { // can't get choice from a null value so check first
      schedulerRequested = true;
    } 
    setSchedulerSelected(scheduler.value);
    /* now update fault model and scheduler name based on parsed arguments 
     * If numFaults == 0, then the Probabilistic fault model is used,
     * otherwise, the Fixed fault model is used.
     * */
    setFaultModel(getNumFaults());
    faultModel = getFaultModel();
    /* Now ensure the input file exists. It was set to the
     * default value and possibly changed by the input option.
     * Easiest to check once here at the end.
     */
    ensureFileExists(fm.verifyGraphFileName(inputFile));
  }
  
  public void print() { // print all system configuration parameters
    // Print out each of the system configuration values
    System.out.println("WARP system configuration values:");
    System.out.println("\tScheduler=" + this.getSchedulerSelected());
    System.out.println("\tnChanels=" + this.getNumChannels());
    System.out.println("\tnumFaults=" + this.getNumFaults());
    System.out.println("\tminPacketReceptionRate=" + this.getMinPacketReceptionRate());
    System.out.println("\tE2E=" + this.getE2E());
    System.out.println("\tguiRequest flag=" + this.guiRequested());
    System.out.println("\tdisplayRequest flag=" + this.displayRequested());
    System.out.println("\tgvRequest flag=" + this.gvRequested());
    System.out.println("\twfRequest flag=" + this.wfRequested());
    System.out.println("\traRequest flag=" + this.raRequested());
    System.out.println("\tlaRequest flag=" + this.laRequested());
    System.out.println("\tcaRequest flag=" + this.caRequested());
    System.out.println("\teaRequest flag=" + this.eaRequested());
    System.out.println("\tsimRequest flag=" + this.simRequested());
    System.out.println("\tallOutFilesRequest flag=" + this.allRequested());
    System.out.println("\tlatency flag=" + this.latencyRequested());
    if (this.getInputFileName() != null) {
      System.out.println("\tinput file=" + this.getInputFileName());
    } else {
      System.out.println("\tNo input file specified; will be requested when needed.");
    }
    System.out.println("\toutputSubDirectory=" + this.getOutputSubDirectory());
    System.out.println("\tverbose flag=" + this.verboseMode());
  }
}


