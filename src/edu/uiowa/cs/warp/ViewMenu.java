package edu.uiowa.cs.warp;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Creates the menu for all Warp visualizations and provides
 * methods that can be called by a controller to attached 
 * listeners to the menu items. This is a view class in a
 * Model-View-Controller software pattern. It is intended that the  
 * actual creation and display of the visualizations will be done in a
 * controller object.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 */
public class ViewMenu {

  private static String MENU_NAME = "View";

  private JMenu menu;
  private JMenuItem programSourceItem = new JMenuItem("Program Source");
  private JMenuItem deadlineReportItem = new JMenuItem("Deadline Report");
  private JMenuItem reliabilityAnalysisItem = new JMenuItem("Reliability Analysis");
  private JMenuItem channelAnalysisItem = new JMenuItem("Channel Analysis");
  private JMenuItem executionAnalysisItem = new JMenuItem("Execution Analysis");
  private JMenuItem latencyAnalysisItem = new JMenuItem("Latency Analysis");
  private JMenuItem latencyReportItem = new JMenuItem("Latency Report");
  private JMenuItem networkVisualizationItem = new JMenuItem("Sensor Network");
  private JMenuItem communicationGraphVisualizationItem = new JMenuItem("WorkLoad Communication Costs");
  private JMenuItem graphVisualizationItem = new JMenuItem("Workload as a Di-Graph");
  private JMenuItem inputGraphVisualizationItem = new JMenuItem("Input WorkLoad");

  /**
   * Constructor to create the menu from which Warp visualizations can be
   * requested. Need to call getMenu() to retrieve the menu built and attach 
   * it to a parent menu bar. The name displayed for the menu can be changed
   * by calling menu.setText(), as it is a JMenu.
   */
  public ViewMenu () {
    buildMenu(MENU_NAME);
  }

  /**
   * Returns the menu built so that it can be attached to a menu bar.
   * 
   * @return the menu built
   */
  public JMenu getMenu() {
    return menu;
  }

  /**
   * Creates and builds Warp View menu.
   * 
   * @param title is name displayed for the menu.
   */
  private void  buildMenu(String title){
    /* Create the menu and all of the Menu Items */
    menu = new JMenu(title);

    /* Add the menu items to the menu */
    menu.add(programSourceItem);
    menu.add(deadlineReportItem);
    menu.add(reliabilityAnalysisItem);
    menu.add(channelAnalysisItem);
    menu.add(executionAnalysisItem);
    menu.add(latencyAnalysisItem);
    menu.add(latencyReportItem);
    menu.add(networkVisualizationItem);
    menu.add(communicationGraphVisualizationItem);
    menu.add(graphVisualizationItem);
    menu.add(inputGraphVisualizationItem); 
  }

  /**
   * Attach a listener to the Program Source menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addProgramSourceListener(ActionListener listener) {
    programSourceItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Deadline Report menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addDeadlineReportListener(ActionListener listener) {
    deadlineReportItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Reliability Analysi menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addReliabilityAnalysisListener(ActionListener listener) {
    reliabilityAnalysisItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Channel Analysis menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addChannelAnalysisListener(ActionListener listener) {
    channelAnalysisItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Execution Analysis menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addExecutionAnalysisListener(ActionListener listener) {
    executionAnalysisItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Latency Analysis menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addLatencyAnalysisListener(ActionListener listener) {
    latencyAnalysisItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Latency Report menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addLatencyReportListener(ActionListener listener) {
    latencyReportItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Sensor Network menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addNetworkVisualizationListener(ActionListener listener) {
    networkVisualizationItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Communication Graph menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addCommunicationGraphVisualizationListener(ActionListener listener) {
    communicationGraphVisualizationItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Graph Visualization menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addGraphVisualizationListener(ActionListener listener) {
    graphVisualizationItem.addActionListener(listener);
  }

  /**
   * Attach a listener to the Input Graph menu item. 
   * 
   * @param listener to respond to the menu item selection.
   */
  public void addInputGraphVisualizationListener(ActionListener listener) {
    inputGraphVisualizationItem.addActionListener(listener);
  }
}
