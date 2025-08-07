package edu.uiowa.cs.warp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * GuiWindow Class to create a Java Swing window that displays a table of data with column
 * headings.
 * 
 * @author sgoddard
 * @version 2.0 Spring 2025
 *
 */
public class GuiWindow extends JFrame {

  private static final String ZOOM_IN = "Zoom In";
  private static final String ZOOM_OUT = "Zoom Out";
  private static final String PRINT = "Print";
  private static final String OPTIONS = "Show Warp Options";
  private static final int ZOOM_IN_INC = 2;
  private static final int ZOOM_OUT_INC = -2;
  private static final int DEFAULT_HEIGHT = 400;
  private static final int DEFAULT_WIDTH = 1000;

  /**
   * Java frame for the table.
   */
  private JFrame frame;

  /**
   * Reference to the JTable or JTextArea that is added to the frame with scroll bars.
   */
  private JComponent jComponent;

  /**
   * Warp options used to create the display contents.
   */
  private Options warpOptions;

  /**
   * Constructor to create a Java Swing Window for a table of data with column headings. The window
   * is initially not visible, and is made visible by a call to setVisible().
   * 
   * @param title Window title
   * @param columnNames Column heading names
   * @param table table of strings representing the data
   */
  public GuiWindow(Options warpOptions, String title, String[] columnNames, String[][] table) {
    /* store a copy of the options used to create the contents of this display */
    this.warpOptions = new Options(warpOptions);
    /* create and Initialize a JTable with input parameters */
    JTable jTable = new JTable(table, columnNames);
    /* set default row height based on options*/
    this.warpOptions.scaleTableRowHeight(jTable);
    jTable.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT); 
    jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // creates horizontal scrollbar
    resizeColumnWidth(jTable); // uncomment to auto size column widths

    /* Build the Frame with the table as the displayed JComponent */
    JComponent sp = buildSinglePaneFrame(jTable, title);
    frame.add(sp);
    resizeWindowToFitTable(frame);
  }

  /**
   * Constructor to create a Java Swing Window for a text report. The window
   * is initially not visible, and is made visible by a call to setVisible().
   * 
   * @param title Window title
   * @param report text to display
   */
  public GuiWindow(Options warpOptions, String title, Description report) {
    /* store a copy of the options used to create the contents of this display */
    this.warpOptions = new Options(warpOptions);
    // Initializing the JTextArea with input parameters
    JTextArea jTextArea = new JTextArea(report.toString());
    JComponent sp = buildSinglePaneFrame(jTextArea, title);
    frame.add(sp);
    frame.pack();
  }

  /**
   * Constructor to create a Java Swing Window for a text report followed by
   * panel visualization. The window is initially not visible, and is made
   * by a call to setVisible().
   * 
   * @param title Window title
   * @param report text to display
   */
  public GuiWindow(Options warpOptions, String title, Description report, JPanel panel) {
    /* store a copy of the options used to create the contents of this display */
    this.warpOptions = new Options(warpOptions);
    /* Initializing the JTextArea with input parameters */
    JTextArea jTextArea = new JTextArea(report.toString());
    /* Build the Frame with the table as the displayed JComponent */
    JComponent splitPane = buildSplitPaneFrame(jTextArea, title, panel);
    frame.getContentPane().add(splitPane, BorderLayout.CENTER);
    frame.pack();
    frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT*2);
  }

  /**
   * Make the table visible.
   */
  public void setVisible() {
    frame.setVisible(true);
  }

  public void resizeColumnWidth(JTable table) {
    final TableColumnModel columnModel = table.getColumnModel();
    for (int column = 0; column < table.getColumnCount(); column++) {
      int width = 70; // Min width
      for (int row = 0; row < table.getRowCount(); row++) {
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component comp = table.prepareRenderer(renderer, row, column);
        width = Math.max(comp.getPreferredSize().width + 5, width);
      }
      columnModel.getColumn(column).setPreferredWidth(width);
    }
  }

  public JComponent getJComponent() {
    return jComponent; 
  }

  public JFrame getFrame() {
    return frame; 
  }

  /**
   * Return the Warp options used to create the display contents.
   * 
   * @return warpOptions used to create the display contents
   */
  public Options getOptions () {
    return this.warpOptions;
  }

  /* Uses JComponent instead of JTable so it can work with any child object */
  /* I don't think this method is really needed */
  public void resizeWindowToFitTable(JFrame frame) {
    int extraWidth = 10; // Adjust as needed
    int extraHeight = 5; // Adjust as needed
    // Get the current width and height of the panel
    int width = frame.getWidth();
    int height = frame.getHeight();
    frame.setSize(width + extraWidth, height + extraHeight);
  }

  private JComponent buildSinglePaneFrame(JComponent jComponent, String title) {
    /* create the frame, set its size and other attributes */
    frame = this; //new JFrame();
    frame.setTitle(title);
    frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);  
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(false);
    frame.setLocationByPlatform(true);

    /* create a scroll pane for the frame and attached it */
    JScrollPane sp = new JScrollPane(jComponent); // gives table a vertical scrollbar

    /* store the jComponent for window actions */
    this.jComponent = jComponent;

    /* Build and attached simple print and zoom menu actions to the frame */
    buildFlatMenuBar(title);
    return sp;
  }

  private JComponent buildSplitPaneFrame(JComponent jComponent, String title, JPanel panel) {

    JComponent sp = buildSinglePaneFrame(jComponent,title); 
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, panel);
    /* Add a ComponentListener to ensure the JSplitPane is visible before setting divider location */
    splitPane.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        splitPane.setDividerLocation(0.25); // Set to 25%
        splitPane.removeComponentListener(this); // Remove the listener after setting
      }
    });
    return splitPane;
  }

  private JMenuItem createZoomMenuItem(String name, int zoom, int key, int mask) {
    JMenuItem zoomIn = new JMenuItem(name);
    ZoomAction zoomInAction = new ZoomAction(this, name, zoom, key, mask);
    zoomIn.setAction(zoomInAction);
    return zoomIn;
  }

  private JMenuItem createPrintMenuItem(String windowTitle) {    
    JMenuItem print = new JMenuItem(PRINT);
    PrintAction printAction = new PrintAction(this, windowTitle, PRINT, KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
    print.setAction(printAction);
    return print;
  }

  private JMenuItem createShowOptionsItem() {    
    JMenuItem options = new JMenuItem(OPTIONS);
    ShowOptionsAction optionsAction = new ShowOptionsAction(this, OPTIONS, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
    options.setAction(optionsAction);
    return options;
  }

  /* Build and attached simple print and zoom menu actions to the frame */
  private void buildFlatMenuBar(String windowTitle) {
    JMenuBar menuBar = new JMenuBar(); // create a menu bar
    /* Create a bar of menuItems that act as buttons on the menu bar and have 
     * key strokes associated for short cuts 
     * */
    menuBar.add(createPrintMenuItem(windowTitle));
    menuBar.add(createZoomMenuItem(ZOOM_IN, ZOOM_IN_INC, KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK));
    menuBar.add(createZoomMenuItem(ZOOM_OUT,ZOOM_OUT_INC, KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
    menuBar.add(createShowOptionsItem());
    menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
    /* attach the menuBar to the window */
    frame.setJMenuBar(menuBar);
  }
}

