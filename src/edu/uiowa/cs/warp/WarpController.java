package edu.uiowa.cs.warp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import edu.uiowa.cs.warp.Visualizable.SystemVisualizations;
import edu.uiowa.cs.warp.Visualizable.WorkLoadVisualizations;

/**
 * Controller in the Model-View-Controller (MVC) software design 
 * pattern. It interacts with the Gui class, which builds all of
 * the menus, the Warp object, and instantiates this class. A design
 * decision was made to define the actions taken for all menu
 * items within the controller. They could be defined in the menu 
 * classes, but then each menu needs access to warp the editor classes. 
 * Instead, this controller class was created to separate the menu
 * (and views displayed) from the model.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class WarpController {
  private ViewMenu view;
  private Options warpOptions;
  private WarpInterface warp;
  private OptionsMenu optionsMenu;
  private JFrame guiFrame;
  private InputFileMenu fileMenu;
  private InputFileEditor inputFileEditor;
  private OptionsEditor optionsEditor;

  /**
   * Creates the controller object that receives view requests from
   * the Gui and requests the corresponding warp views or option updates.
   * 
   * @param warp object of the MVC pattern.
   * @param gui  view of the MVC pattern.
   */
  public WarpController(WarpInterface warp, Gui gui) {
    /* 
     * The current implementation creates a new window for each visualization, so that 
     * the user can compare different workloads. For example, a user can show the the
     * program source code generated for two (or more) workloads to see the impact
     * of changing any of the Warp options.
     */
    this.warp = warp;  // Warp model of model-view-controller
    this.warpOptions = warp.getOptions(); // model for options
    this.optionsMenu = gui.getOptionsMenu(); // options view of model-view-controller
    this.fileMenu = gui.getFileMenu(); // file view of model-view-controller
    this.view = gui.getViewMenu(); // view of model-view-controller
    this.guiFrame = gui.getFrame(); // Frame to which all views are attached

    /* 
     * Attached all of the listeners that will display the requested System or WorkLoad 
     * visualizations.
     */
    this.view.addProgramSourceListener(
        new DisplayWarpOptionListener(SystemVisualizations.SOURCE));
    this.view.addDeadlineReportListener(
        new DisplayWarpOptionListener(SystemVisualizations.DEADLINE_REPORT));
    this.view.addReliabilityAnalysisListener(
        new DisplayWarpOptionListener(SystemVisualizations.RELIABILITIES));
    this.view.addChannelAnalysisListener(
        new DisplayWarpOptionListener(SystemVisualizations.CHANNEL));
    this.view.addExecutionAnalysisListener(
        new DisplayWarpOptionListener(SystemVisualizations.EXECUTION));
    this.view.addLatencyAnalysisListener(
        new DisplayWarpOptionListener(SystemVisualizations.LATENCY));
    this.view.addLatencyReportListener(
        new DisplayWarpOptionListener(SystemVisualizations.LATENCY_REPORT));
    this.view.addNetworkVisualizationListener(
        new DisplayWarpOptionListener(WorkLoadVisualizations.NETWORK));
    this.view.addCommunicationGraphVisualizationListener(
        new DisplayWarpOptionListener(WorkLoadVisualizations.COMUNICATION_GRAPH));
    this.view.addGraphVisualizationListener(
        new DisplayWarpOptionListener(WorkLoadVisualizations.GRAPHVIZ));
    this.view.addInputGraphVisualizationListener(
        new DisplayWarpOptionListener(WorkLoadVisualizations.INPUT_GRAPH));


    /* Create the Options editor and attach its listener */
    optionsEditor = new OptionsEditor(warpOptions, guiFrame);
    this.optionsMenu.addEditOptionsListener(new ChangeOptionsListener(optionsEditor));

    /* Create the input file editor and attach its listeners */
    inputFileEditor = new InputFileEditor(guiFrame);
    this.fileMenu.addCreateSourceListener(new CreateFileListener(inputFileEditor));
    this.fileMenu.addOpenSourceListener(new OpenFileListener(inputFileEditor));
    this.fileMenu.addSaveSourceListener(
        new SaveFileListener(inputFileEditor, false)); // no newNameNeeded
    this.fileMenu.addSaveAsSourceListener(
        new SaveFileListener(inputFileEditor, true)); // newNameNeeded

    /* Load the default input file into the editor for display and/or edit. */
    File file = new File(warpOptions.getCurrentDirectory(), warpOptions.getInputFileName());
    inputFileEditor.openFile(file);
  }

  /**
   * Creates the requested system visualization choice.
   * 
   * @param warp object
   * @param choice of the visualization to be created.
   * @return a reference to the window displayed
   */
  private GuiWindow visualize(WarpInterface warp, SystemVisualizations choice) {
    Visualizable viz = VisualizationFactory.createVisualization(warp, choice);
    return getWindow(viz);
  }

  /**
   * Creates the requested workload visualization choice.
   * 
   * @param warp object
   * @param choice of the visualization to be created.
   * @return a reference to the window displayed
   */
  private GuiWindow visualize(WarpInterface warp, WorkLoadVisualizations choice) {
    Visualizable viz = VisualizationFactory.createVisualization(warp.getWorkload(), choice);
    return getWindow(viz);
  }

  /**
   * Returns the window showing the visualization.
   * @param viz the visualization
   * @return the window reference associated with viz
   */
  private GuiWindow getWindow(Visualizable viz) {
    GuiWindow window = null;
    if (viz != null) {
      window = viz.getDisplay();
      if (window == null) {
        /* window is null if toDisplay() was not called by the Factory */
        window = viz.toDisplay();
      }
    }
    return window;
  }

  /**
   * Returns the current warp object.
   * 
   * @return warp
   */
  private WarpInterface getWarp() {
    return this.warp;
  }    

  /* 
   * This next class defines the actions taken for the
   * View menu. They could be defined in the menu class,
   * but then the menu needs access to warp. Instead, this
   * controller class was created to separate the menu
   * (and views displayed) from the model.
   */

  /**
   * Defines the action taken in response to a Gui
   * request to display one of the Warp visualizations.
   */
  class DisplayWarpOptionListener implements ActionListener {
    /* The class attribute window stores the reference to the window that
     * will be created to display the requested view.
     * */
    private GuiWindow window = null; // stores a reference to the window
    private SystemVisualizations systemChoice;
    private WorkLoadVisualizations workloadChoice;

    /** 
     * Creates a listener for SystemVisualizations.
     * 
     * @param choice System visualization requested.
     */
    DisplayWarpOptionListener (SystemVisualizations choice) {
      systemChoice = choice;
      workloadChoice = null;
    }

    /** 
     * Creates a listener for WorkLoadVisualizations. This class
     * stores a reference to the window displayed in case
     * the controller wants to re-use the window to update 
     * the visualization. 
     * 
     * @param choice WorkLoad visualization requested.
     */
    DisplayWarpOptionListener (WorkLoadVisualizations choice) {
      systemChoice = null;
      workloadChoice = choice;
    }

    /**
     * Displays the requested visualization and
     * stores a reference to the window displayed. 
     */
    public void actionPerformed(ActionEvent e) {
      //	      System.out.println("Program Source Display requested.\n"); 
      if (systemChoice != null) {
        window = visualize(getWarp(), systemChoice);
      } else {
        window = visualize(getWarp(), workloadChoice);
      }
      // window = visualize(getWarp(), choice);
    }

    public GuiWindow getWindow() {
      return window;
    }
  }

  /* 
   * This next class defines the actions taken for the
   * Options menu. They could be defined in the OptionsEditor
   * class, but then the Observer-Observe(Subject) pattern
   * would need to be used because the controller needs to 
   * know when the options have been changed. When
   * that happens, the warp  object needs to be reset() AND
   * if the input file was changed, then the InputFileEditor
   * needs to be updated as well.
   * 
   * The GUI event-framework implements a form of the
   * Observer-Subject pattern. When the associated GUI-event
   * occurs, the registered action (defined in these classes)
   * is performed. The ChangeOptionsListener defined within 
   * this controller has access to InputFileEditor, Objects, 
   * and Warp objects. Thus, the controller makes all of the updates.
   * 
   * Another implementation would have been to pass the
   * InputFileEditor and WarpSystem objects to the Editor to do the
   * updates, but the OptionsEditor is focused on editing the
   * options, and should not need to worry about the input file
   * editor and warp updates.
   * 
   */

  /**
   * Defines the action taken in response to a Gui
   * request to view or change the warp options.
   */
  class ChangeOptionsListener implements ActionListener {

    private OptionsEditor editor;

    /** 
     * Creates a listener for ChangeOptionsListener.
     * 
     * @param editor making changes to the warp options.
     */
    ChangeOptionsListener (OptionsEditor editor) {
      this.editor = editor;
    }

    /**
     * Displays warp options editor for the user and
     * resets the warp object if updates have been made.
     * The reset results in all future visualizations being
     * based on the new options. 
     */
    public void actionPerformed(ActionEvent e) {
      /* 
       * Store the current input file name, so we
       * can check later if we need to update the
       * InputFileEditor.
       */
      String currentInputFileName = warpOptions.getInputFileName();
      String newInputFileName; // set if options edits were made

      /* Request that the editor prompt the user for
       * updates. 
       */
      Boolean changesNeeded = editor.editOptions();
      if (changesNeeded) {
        /* 
         * Update the File displayed in the Gui if it changed and
         * reset the Warp System objects so they will be
         * rebuilt on demand with the new options.
         */
        newInputFileName = warpOptions.getInputFileName();
        if (!newInputFileName.equals(currentInputFileName)) {
          /* The input file name was updated by the options editor */
          inputFileEditor.openFile(warpOptions.getInputFileName());
        }
        warp.reset();
      }

    }
  }

  /*  
   * The next set of classes define the actions taken for
   * InputFile menu. They could be defined in the InputFileEditor 
   * class, but then the Observer-Observe(Subject)
   * pattern would need to be used because the
   * controller needs to know when the input file has
   * been changed via open(), save(), or saveAs(). When
   * that happens, the Options object needs to be updated
   * AND the warp  object needs to be reset().
   * 
   * The GUI event-framework implements a form of the
   * Observer-Subject pattern. When the associated GUI-event
   * occurs, the registered action (defined in these classes)
   * is performed. The classes defined within the controller
   * have access to Editor, Objects, and Warp objects. Thus,
   * the controller makes all of the updates.
   * 
   * Another implementation would have been to pass the
   * Option and WarpSystem objects to the Editor to do the
   * updates, but the Editor is focused on editing the input
   * file, and should not need to worry about the options
   * and warp updates.
   * 
   */

  /**
   * Defines the action taken in response to a Gui
   * request to create a new input file.
   */
  class CreateFileListener implements ActionListener {

    private InputFileEditor editor;

    CreateFileListener (InputFileEditor editor) {
      this.editor = editor;
    }

    /**
     * Clears the currently displayed file in the
     * editor, so that a new file can be created.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      editor.clearInputFileEditor();
    }
  }

  /**
   * Defines the action taken in response to a Gui
   * request to open a new input file.
   */
  class OpenFileListener implements ActionListener {

    private JFrame frame;
    private InputFileEditor editor;
    private JFileChooser fileChooser;

    OpenFileListener (InputFileEditor editor) {
      this.editor = editor;
      frame = editor.getFrame();
      fileChooser = new JFileChooser();
    }

    /**
     * Directs the editor to open a new input file and then resets
     * the warp object to use that file to define the workload. 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      /* A JFileChooser object is used to let the user select the new input file.  */
      fileChooser.setCurrentDirectory(new java.io.File(warpOptions.getCurrentDirectory()));
      fileChooser.setSelectedFile(new File("")); // clear the file name
      int result = fileChooser.showOpenDialog(frame);
      if (result == JFileChooser.APPROVE_OPTION) {
        /* 
         * If a file was selected and exists, then set the Options accordingly,
         * tell the editor to open the file, and reset the warp object.
         */
        File file = fileChooser.getSelectedFile();
        if (file.exists()) {
          warpOptions.setCurrentDirectory(file.getParent());
          warpOptions.setInputFile(file.getName());
          editor.openFile(file);
          warp.reset(); 
        } else {
          /* if the file doesn't exist, then show an error message */
          JOptionPane.showMessageDialog(frame, "File " + file.getName() + " does not exist!");
        }
      }
    }
  }

  /**
   * Defines the action taken in response to a Gui
   * request to save (or saveAs) a new input file.
   */
  class SaveFileListener implements ActionListener {

    private JFrame frame;
    private InputFileEditor editor;
    private JFileChooser fileChooser;
    private Boolean newNameNeeded; // true for SaveAs and false for Save

    SaveFileListener (InputFileEditor editor, Boolean newNameNeeded) {
      this.editor = editor;
      frame = editor.getFrame(); 
      fileChooser = new JFileChooser();
      this.newNameNeeded = newNameNeeded;
    }

    /**
     * Directs the editor to save the input file in the edit window and
     * resets the warp object to use that file to define the workload. 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      /* A JFileChooser object is used to let the user select the new input file.  */
      fileChooser.setCurrentDirectory(new java.io.File(warpOptions.getCurrentDirectory()));
      if (newNameNeeded) {
        /* SaveAs option: clear the file name to be saved in the chooser window */
        fileChooser.setSelectedFile(new File("")); 
      } else {
        /* Save option: set the default file name to the current graph input file name */
        fileChooser.setSelectedFile(new File(warpOptions.getInputFileName())); 
      }
      int result = fileChooser.showSaveDialog(frame);
      if (result == JFileChooser.APPROVE_OPTION) {
        /* 
         * If a file was selected, then set the Options accordingly,
         * tell the editor to save the file, and reset the warp object.
         */
        File file = fileChooser.getSelectedFile();
        editor.saveFile(file);
        warpOptions.setCurrentDirectory(file.getParent());
        warpOptions.setInputFile(file.getName());
        warp.reset(); // notifyObservers("Newly Saved and working files is: " + file.getName());
      } 
    }
  }


  /**
   * Called by the OptionsView publisher to notify this
   * class that the Options (Warp Options) have been
   * updated.
   */
  //  @Override
  //  public void update(String message) {
  //    /* ignore the input message; could print it out for debugging */
  //    System.out.println(message);
  //    warp = new WarpSystem(warpOptions);
  //  }
}
