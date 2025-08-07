package edu.uiowa.cs.warp;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Action performed to show warp options when selected in a display window.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class ShowOptionsAction extends AbstractWarpAction {

  private static final long serialVersionUID = 1L;
  private JFrame parentFrame;
  private Options options;

  public ShowOptionsAction(GuiWindow guiObject, String menuTitle, int key, int mask) {
    super(guiObject, menuTitle, key, mask);
    this.parentFrame = guiObject.getFrame();
    this.options = guiObject.getOptions();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    StringBuilder optionsText = options.getCoreOptionsForViewing();
    JOptionPane.showMessageDialog(parentFrame, optionsText.toString(), "Warp options for this Visualization", JOptionPane.INFORMATION_MESSAGE);
  }

}
