package edu.uiowa.cs.warp;

import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A basic editor for Warp options. This simple editor 
 * displays showConfirmDialog box in which the JOptionPane 
 * contains the current options. Each field, corresponding
 * to option can be edited (simple text editing). 
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class OptionsEditor {
  private Options options;
  private Options priorOptions;
  private JFrame frame = null;  
  private JPanel panel = null;
  private JTextField workloadField = new JTextField(20);
  private JTextField faultModelField = new JTextField(20);
  private JCheckBox verboseCheckBox = new JCheckBox("Verbose Mode");
  private JTextField outputDirField = new JTextField(20);
  private JTextField homeDirField = new JTextField(20);
  private JTextField schedulerField = new JTextField(20);
  private JTextField numFaultsField = new JTextField(20);
  private JTextField minLQField = new JTextField(20);
  private JTextField e2eField = new JTextField(20);

  /**
   * Create editor.
   * 
   * @param options to be displayed and edited.
   * @param frame attached to the editor.
   */
  OptionsEditor (Options options, JFrame frame) {
    this.options = options;
    this.frame = frame;
    /* store a copy of the options for comparisons to updates */
    this.priorOptions = new Options(options);
    panel = buildPanel();
  }

  /**
   * Returns the frame associated with the editor.
   * 
   * @return the frame to which the editor is attached.
   */
  public JFrame getFrame() {
    return frame;
  }

  /**
   * Loads the current warp options into the fields for display and editing.
   */
  private void loadOptionParameters() { 
    workloadField.setText(options.getInputFileName());
    faultModelField.setText(options.getFaultModelName());
    faultModelField.setEditable(false); // Make the text field non-editable
    outputDirField.setText(options.getOutputSubDirectory());
    homeDirField.setText(options.getCurrentDirectory());
    schedulerField.setText(options.getSchedulerName());
    numFaultsField.setText(options.getNumFaults().toString());
    minLQField.setText(options.getMinPacketReceptionRate().toString());
    e2eField.setText(options.getE2E().toString());
    verboseCheckBox.setSelected(options.verboseMode());
  }

  /**
   * Reads the options from the editor fields and updates 
   * the warp options accordingly.
   */
  private void storeOptionParameters() {
    options.setInputFile(workloadField.getText());
    options.setSchedulerSelected(schedulerField.getText());
    options.setNumFaults(numFaultsField.getText());
    options.setMinPacketReceptionRate(minLQField.getText());
    options.setE2E(e2eField.getText());
    // faultModel = faultModelField.getText();
    options.setVerboseMode(verboseCheckBox.isSelected());
    options.setOutputSubDirectory(outputDirField.getText());
    options.setCurrentDirectory(homeDirField.getText());
    // schedulerName = schedulerField.getText();
  }
  
  /**
   * Displays Warp System options in a Dialog box and
   * returns true if updates have been made.
   */
  public Boolean editOptions() {
    Boolean changesMade = false;

    loadOptionParameters();
    int result = JOptionPane.showConfirmDialog(frame, panel, "Update Options", 
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      storeOptionParameters();
    }
    if (!priorOptions.equals(options) ) {
      /* 
       * options have been updated, so make a copy and
       * notify the caller of the updates by setting
       * the changesMade flag to true.
       */
      priorOptions = new Options(options);
      changesMade = true;
    }
    return changesMade;
  }

  /**
   * Displays Warp System options in a read-only Dialog box.
   */
  public void showCurrentOptions(JFrame frame) {
    StringBuilder optionsText = options.getCoreOptionsForViewing();
    JOptionPane.showMessageDialog(frame, optionsText.toString(), 
                "Current Option Settings", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Build the JPanel used to display and edit the warp options.
   * 
   * @return the panel built
   */
  private JPanel buildPanel() {
    JPanel panel = new JPanel(new GridLayout(0, 2));
    panel.add(new JLabel("Current Workload:"));
    panel.add(workloadField);
    panel.add(new JLabel("Scheduler Name:"));
    panel.add(schedulerField);
    panel.add(new JLabel("Faults Tolerated (> 0 => Fixed Fault Model): "));
    panel.add(numFaultsField);
    panel.add(new JLabel("Min Link Quality: "));
    panel.add(minLQField);
    panel.add(new JLabel("End-to-End Reliability: ")); 
    panel.add(e2eField);
    panel.add(new JLabel("Fault Model:"));
    panel.add(faultModelField);
    panel.add(new JLabel("Current Directory:"));
    panel.add(homeDirField);
    panel.add(new JLabel("Output Files SubDirectory:"));
    panel.add(outputDirField);
    panel.add(verboseCheckBox);

    return panel;
  }
}
