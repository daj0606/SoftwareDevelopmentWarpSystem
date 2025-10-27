package edu.uiowa.cs.warp;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Creates the File menu to interact with Warp input files. 
 * This menu provides Open, Create, Save, and SaveAS menu items. 
 * There is no Print option, as the file can be printed from the Input File 
 * Visualization option of the View menu. This is a view class in a
 * Model-View-Controller software pattern. Thus, methods are provided
 * that can be called by a controller to attach listeners
 * to the menu items. It is intended that the actual editing and display 
 * of the file is done by the InputFileEditor, with the controller 
 * managing the interactions with the editor, the Warp options, and
 * the Warp system.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class InputFileMenu {

  private static String MENU_NAME = "Input File"; // or File?
  private static String OPEN = "Open";
  private static String CREATE = "Create";
  private static String SAVE = "Save";
  private static String SAVE_AS = "SaveAs";

  private JMenu fileMenu;
  private JMenuItem openItem;
  private JMenuItem createItem;
  private JMenuItem saveItem;
  private JMenuItem saveAsItem;

  /**
   * Constructor to create the menu to interact with Warp input files.
   * Need to call getMenu() to retrieve the menu built and attach it
   * to a a parent menu bar. The name displayed for the menu can be 
   * changed by calling menu.setText(), as it is a JMenu.
   */
  public InputFileMenu() {
    buildMenu(MENU_NAME);
  }

  /**
   * Returns the menu built so that it can be attached to a menu bar.
   * 
   * @return the menu built
   */
  public JMenu getMenu() {
    return fileMenu;
  }

  /**
   * Attach a listener to the Create menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addCreateSourceListener(ActionListener listener) {
    createItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Open menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addOpenSourceListener(ActionListener listener) {
    openItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Save menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addSaveSourceListener(ActionListener listener) {
    saveItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the SaveAs menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addSaveAsSourceListener(ActionListener listener) {
    saveAsItem.addActionListener(listener);
  }

  /**
   * Creates and builds Warp File menu.
   * 
   * @param title is name displayed for the menu.
   */
  private void  buildMenu(String title){
    fileMenu = new JMenu(title);
    openItem = new JMenuItem(OPEN);
    createItem = new JMenuItem(CREATE);
    saveItem = new JMenuItem(SAVE);
    saveAsItem = new JMenuItem(SAVE_AS);
    //  JMenuItem printMenuItem = new JMenuItem("Print");

    fileMenu.add(openItem);
    fileMenu.add(createItem);
    fileMenu.add(saveItem);
    fileMenu.add(saveAsItem);
  }
}
