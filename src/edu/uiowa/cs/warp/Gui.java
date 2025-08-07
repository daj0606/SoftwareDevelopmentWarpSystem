package edu.uiowa.cs.warp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

/** 
 * Singleton Class that creates the Warp Gui.
 * Let's users display Warp visualizations; create, open, and/or edit 
 * graph input files; and change default options. After a change
 * to the input file or Warp options, all future visualizations
 * will be based on those changes. However, previous visualizations
 * will not be modified. This lets users compare visualizations before
 * and after the change to options or input file.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class Gui extends JFrame {

  private static final String WARP_ICON = "WARP.png";
  private static Gui gui = null;
  private Options warpOptions;
  private InputFileMenu inputFileMenu = null;
  private OptionsMenu warpOptionsMenu;
  private ViewMenu viewMenu;

  public static Gui getInstance(WarpInterface warp) {
    if (gui == null) {
      gui = new Gui(warp);
    }
    return gui;
  }

  /**
   * Gets the frame for the Gui. Because Gui extends JFrame, the 
   * Gui itself is a JFrame. When returned via this call, only JFrame
   * calls are available (polymorphism in action).
   * 
   * @return the Gui as a JFrame
   */
  public JFrame getFrame() {
    return this;
  }

  /**
   * Get the Options menu. The controller uses this method
   * to retrieve the options menu as a view in the 
   * Model-View-Controller software design pattern.
   * 
   * @return the Options menu
   */
  public OptionsMenu getOptionsMenu() {
    return warpOptionsMenu;
  }

  /**
   * Get the File menu. The controller uses this method
   * to retrieve the input file menu as a view in the 
   * Model-View-Controller software design pattern.
   * 
   * @return the Options menu
   */
  public InputFileMenu getFileMenu() {
    return inputFileMenu;
  }

  /**
   * Get the visualization views (View) menu. The controller uses this method
   * to retrieve the options menu as a view in the 
   * Model-View-Controller software design pattern.
   * 
   * @return the View menu
   */
  public ViewMenu getViewMenu() {
    return viewMenu;
  }

  /**
   * Private constructor to create the Gui. This constructor can only be
   * called by the public getInstance() method. This constructor and the
   * getInstance() method form the Singleton software design pattern.
   * @param warp
   */
  private Gui(WarpInterface warp) {
    /* The WarpController object will be the controller in the 
     * Model-View-Controller (MVC) software design pattern.
     */
    WarpController warpController;
    /* Java menu items placed on the main menu bar */
    JMenu warpMenu; 
    JMenu fileMenu;
    JMenu viewJMenu;

    /* Store the Warp options that will be used to create the system. */
    this.warpOptions = warp.getOptions();

    // use the mac system menu bar
    //    System.setProperty("apple.laf.useScreenMenuBar", "true");

    /* set the title and size of the display frame */
    setTitle("Warp Program Manager");
    setSize(600, 400);

    /* All opened windows will close when the Gui closes */
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    /* Create the main menu bar */
    JMenuBar menuBar = new JMenuBar();

    /* Create the menu objects to be displayed on the main menu bar */
    viewMenu = new ViewMenu();
    inputFileMenu = new InputFileMenu();
    /* 
     * In the current implementation, the full Options menu is not displayed.
     * The OptionsMenu object is created, and the edit options menu item
     * will be added to the Warp menu, but the full Options menu is not added
     * to the menu bar.
     */
    warpOptionsMenu = new OptionsMenu ();

    /* Get the Warp Menu */
    warpMenu = buildWarpMenu();
    /* Get the other JMenus from the menu objects */
    fileMenu = inputFileMenu.getMenu();
    viewJMenu = viewMenu.getMenu();

    /* Add these menus to the main menu bar */
    menuBar.add(warpMenu);
    menuBar.add(fileMenu);
    menuBar.add(viewJMenu);

    /* Now attach the menu bar to the frame */
    setJMenuBar(menuBar);

    /* Just to make things look nicer, change the default
     * icon to the custom Warp icon.
     */
    setProgramIcon();

    /* 
     * Finish things off by creating the Warp controller. It will
     * take over from here by creating the editors and attaching
     * listeners to the menu items. When those items are selected,
     * the controller will take the appropriate actions. 
     */
    warpController = new WarpController(warp, this);

  }

  /** 
   * Build the Warp Menu for the main menu bar. This menu contains
   * the About, Options, and Quit menu items. The About and Quit menu
   * items are defined and built in this method. The Options menu item
   * is built by the Options object and not in this method. 
   * This is different than the other menus because it is mostly
   * self contained. 
   */
  private JMenu buildWarpMenu() {
    JMenu warpMenu = new JMenu("Warp");
    JMenuItem aboutMenuItem = new JMenuItem("About");
    JMenuItem quitMenuItem = new JMenuItem("Quit");
    JMenuItem optionsMenuItem = warpOptionsMenu.getEditOptionsMenuItem();
    /* 
     * Change the name of the Edit Options menu item to Options,
     * because we are embedding it within this Warp Menu and not
     * using it as part of the full Options menu.
     */
    optionsMenuItem.setText("Options");

    /* 
     * Attached listeners to the About and Quit menu items.
     * Because the listeners are so basic, they are defined
     * 'on the fly' as they are instantiated and passed to the
     * add listener method. This is the way ChatGPT, Co-Pilot,
     * and most Web pages demonstrate Gui menu items. It is OK
     * for simple things, but gets messy and confusing for more
     * complicated actions.
     */
    aboutMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(Gui.this, 
            "Warp GUI Manager v2.0\nDeveloped by Steve Goddard");
      }
    });

    quitMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });

    /* Now add these menu items to the Warp menu and return it */
    warpMenu.add(optionsMenuItem);
    warpMenu.add(aboutMenuItem);
    warpMenu.add(quitMenuItem);

    return warpMenu;
  }

  /**
   * Change the change the default icon to the custom Warp icon.
   */
  private void setProgramIcon() {
    /* Look for the Icon file in the current directory */
    File file = new File(warpOptions.getCurrentDirectory(), WARP_ICON);
    if (file.exists()) {
      BufferedImage bImage;
      /* Now read the file and set the icon image */
      try {
        bImage = ImageIO.read(file);
        /* set icon on JFrame menu bar, as in Windows system */
        setIconImage(bImage);
        /* set icon on system tray, as in Mac OS X system, if supported */
        final Taskbar taskbar = Taskbar.getTaskbar();
        if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
          taskbar.setIconImage(bImage);
        }
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error opening file: " + WARP_ICON);
      }
    }
  }
}


