/**
 * 
 */
package edu.uiowa.cs.warp;

import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.JTextArea;

/**
 * Action performed to print window content when selected in a display window.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class PrintAction extends AbstractWarpAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String reportTitle = null;

  public PrintAction(GuiWindow guiObject, String reportTitle, String menuTitle, int key, int mask) {
	  super(guiObject, menuTitle, key, mask);
	    this.reportTitle  = reportTitle;
//	    this(guiObject.getJComponent(), reportTitle, menuTitle, key, mask);
  }
  
//  public PrintAction(JComponent jComponent, String reportTitle, String menuTitle, int key, int mask) {
//    super(jComponent, menuTitle, key, mask);
//      this.reportTitle  = reportTitle;
//  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (jComponent != null) {
      MessageFormat header = new MessageFormat(reportTitle);
      MessageFormat footer = new MessageFormat("Page {0}");
      try {
        if (jComponent instanceof JTable) {
          JTable table = (JTable) jComponent;
          table.print(PrintMode.FIT_WIDTH, header, footer);
        } else if (jComponent instanceof JTextArea) {
          JTextArea text = (JTextArea) jComponent;
          text.print(header, footer);
        }
      } catch (PrinterException e1) {
        e1.printStackTrace();
      }
    }
  }
}
