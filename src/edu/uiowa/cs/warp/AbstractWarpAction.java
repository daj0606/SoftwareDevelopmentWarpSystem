/**
 * 
 */
package edu.uiowa.cs.warp;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * @author sgoddard2
 *
 */
public abstract class AbstractWarpAction extends AbstractAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  protected JComponent jComponent = null;
  protected int key = 0;
  protected int mask = 0;
  protected String actionName= null;
  protected GuiWindow guiObject= null;

  public AbstractWarpAction(GuiWindow guiObject, String menuTitle, int key, int mask) {
	  this.guiObject = guiObject;
	    jComponent = guiObject.getJComponent();
	    this.actionName = menuTitle;
	    putValue(NAME, menuTitle);
	    putValue(SHORT_DESCRIPTION, menuTitle + " window");
	    this.key = key;
		this.mask = mask;
		setKeyStroke(jComponent);
  }
  
//  public AbstractWarpAction(JComponent jComponent, String menuTitle, int key, int mask) {
//    this.guiObject = null;
//      this.jComponent = jComponent; //guiObject.getJComponent();
//      this.actionName = menuTitle;
//      putValue(NAME, menuTitle);
//      putValue(SHORT_DESCRIPTION, menuTitle + " window");
//      this.key = key;
//    this.mask = mask;
//    setKeyStroke (jComponent);
//  }
  
  public void setKeyStroke (JComponent jComponent) {
    if (jComponent != null) {
      /* Create a KeyStroke for the key and mask (e.g., ctrl=) */
      KeyStroke keyStroke = KeyStroke.getKeyStroke(key, mask);
      /* Get the input map for the jComponent content pane; */
      InputMap inputMap = jComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
      /* Add the KeyStroke to the input map */
      inputMap.put(keyStroke, actionName);
      /* Get the action map for the root pane of the content pane */
      ActionMap actionMap = jComponent.getActionMap();
      /* Add the action to the action map */
      actionMap.put(actionName, this);
    }
  }
}
