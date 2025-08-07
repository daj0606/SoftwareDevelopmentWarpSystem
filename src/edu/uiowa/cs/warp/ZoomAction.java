/**
 * 
 */
package edu.uiowa.cs.warp;

import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

/**
 * Action performed to zoom in/out when selected in a display window.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class ZoomAction extends AbstractWarpAction {

  private static final long serialVersionUID = 1L;

  private int zoomIncrement = 0;


  public ZoomAction(GuiWindow guiObject, String menuTitle, int zoomIncrement, int key, int mask) {
	  super(guiObject, menuTitle, key, mask);
	  this.zoomIncrement = zoomIncrement;
//	  this(guiObject.getJComponent(), menuTitle, zoomIncrement, key, mask);
	  
  }
  
//  public ZoomAction(JComponent jComponent, String menuTitle, int zoomIncrement, int key, int mask) {
////    super(guiObject, menuTitle, key, mask);
//    super(jComponent, menuTitle, key, mask);
//    this.zoomIncrement = zoomIncrement;
//  }
  
  @Override
  public void actionPerformed(ActionEvent e) {

	  Font font = jComponent.getFont();
	  jComponent.setFont(getNewFont(font));

	  if (jComponent instanceof JTable) {
		  JTable table = (JTable) jComponent;
		  Font headerFont = table.getTableHeader().getFont();
		  table.getTableHeader().setFont(getNewFont(headerFont));
		  int newHeight = table.getRowHeight() + zoomIncrement;
		  table.setRowHeight(newHeight);
		  guiObject.resizeColumnWidth(table);
		  /* Commented out the resize. I prefer using the existing size */
		  // guiObject.resizeWindowToFitTable(guiObject.getFrame(), table);
	  } 
	  
	  /*
	  if (table != null) {
		  JTable table = getTable();
		  Font font = table.getFont();
		  table.setFont(getNewFont(font));
		  Font headerFont = table.getTableHeader().getFont();
		  table.getTableHeader().setFont(getNewFont(headerFont));
	  } else if (textArea != null){
		  JTextPane textArea = getTextArea();
		  Font font = textArea.getFont();
		  textArea.setFont(getNewFont(font));
	  }
	  */
  }
  
  private Font getNewFont(Font oldFont) {
    int newFontSize = (int) (oldFont.getSize() + zoomIncrement);
    return new Font(oldFont.getName(), Font.PLAIN, newFontSize);
  }
 
}
