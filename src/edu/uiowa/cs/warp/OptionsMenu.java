package edu.uiowa.cs.warp;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Creates the Options menu to display or change the Warp System options. 
 * This class allows the creation of an Options
 * JMenu or it can be used to create Show and Edit Options menu
 * items for embedding in another menu. This is a view class in a
 * Model-View-Controller software pattern. Thus, methods are provided
 * that can be called by a controller to attach listeners
 * to the menu items. It is intended that the actual editing and display 
 * of the options is done by the OptionsEditor, with the controller 
 * managing the interactions with the editor, the input file editor
 * (if one exists), and the Warp system.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class OptionsMenu {

  private static String MENU_NAME = "Options";
  private static String SHOW_OPTIONS = "Show Options";
  private static String EDIT_OPTIONS = "Edit Options";
  private JMenuItem editOptionsMenuItem = null;
  private JMenuItem showOptionsMenuItem = null;  

  JMenu menu;

  /**
   * Constructor for the Options Menu. 
   */
  OptionsMenu () {
    /*
     * Create the show and edit menu items immediately.
     * The full Options menu is built when it is requested.
     * This is a design choice made based on its current
     * use case. The current Gui only used the editOptions
     * menu item as part of the Warp menu, which changes
     * the menu itme name by calling editOptionsMenuItem.setText().
     */
    showOptionsMenuItem = new JMenuItem(SHOW_OPTIONS);
    editOptionsMenuItem = new JMenuItem(EDIT_OPTIONS); 
  }

  /**
   * Returns the menu so that it can be attached to a menu bar.
   * 
   * @return the menu built
   */
  public JMenu getMenu() {
    /* if the menu wasn't yet built, then do so now. */
    if (menu == null) {
      menu = buildMenu(MENU_NAME);
    }
    return menu;
  }

  /**
   * Returns the menu item so that it can be attached to
   * a different menu.
   * 
   * @return the menu item requested
   */
  public JMenuItem getEditOptionsMenuItem() {
    return editOptionsMenuItem;
  }

  /**
   * Returns the menu item so that it can be attached to
   * a different menu.
   * 
   * @return the menu item requested
   */
  public JMenuItem getShowOptionsMenuItem() {
    return showOptionsMenuItem;
  }

  /**
   * Creates and builds Warp Options menu.
   * 
   * @param title is name displayed for the menu.
   */
  private JMenu buildMenu(String title) {
    JMenu menu = new JMenu(title);

    menu.add(showOptionsMenuItem);
    menu.add(editOptionsMenuItem);
    return menu;
  }

  /**
   * Attach a listener to the edit options menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addEditOptionsListener(ActionListener listener) {
    editOptionsMenuItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the show options menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addShowOptionsListener(ActionListener listener) {
    showOptionsMenuItem.addActionListener(listener);
  }

}
