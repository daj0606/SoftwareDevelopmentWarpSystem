package edu.uiowa.cs.warp;

import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A basic editor for Warp input graph files (upon which the workload,
 * program, and all visualizations are based). This simple text editor 
 * supports create, open, and save requests. 
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class InputFileEditor {

  private JTextArea textArea;
  private JFrame frame;

  /**
   * Create the editor.
   * 
   * @param frame to which the editor is attached.
   */
  InputFileEditor(JFrame frame) {
    this.textArea = new JTextArea();
    this.frame = frame;
    frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
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
   * Clear the contents displayed in the editor.
   */
  public void clearInputFileEditor() {
    textArea.setText("");
  }

  /**
   * Open the file by name and display it in the edit window.
   * 
   * @param fileName to be opened.
   */
  public void openFile(String fileName) {
    File file = new File(fileName);
    if (file.exists()) {
      openFile(file);
    } else {
      JOptionPane.showMessageDialog(frame, "File " + file.getName() + " does not exist!");
    }
  }

  /**
   * Open the file object and set display its contents.
   * 
   * @param file object to be opened.
   */
  public void openFile(File file) {
    try {
      String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
      textArea.setText(content);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(frame, "Error opening file: " + e.getMessage());
    }
  }

  /**
   * Save the file object.
   * 
   * @param file object to be saved
   */
  public void saveFile(File file) {
    try  {
      BufferedWriter writer = new BufferedWriter(new FileWriter(file));
      textArea.write(writer);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(frame, "Error saving file: " + e.getMessage());
    }
  }
}
