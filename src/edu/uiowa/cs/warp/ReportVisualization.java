package edu.uiowa.cs.warp;

/**
 * ReportVisualization class to create a Java Swing window that displays 
 * a report of type Description.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class ReportVisualization extends Visualization{

  private static final String SUFFIX = ".txt";
  private Description content;
  private String title;
  private String reportWindowTitle;
  private Options warpOptions;

  ReportVisualization (SystemAttributes warp, 
      Description content, String title) {
    super(warp, SUFFIX);
    this.content = content;
    this.title = title;
    reportWindowTitle = title + "Report";
    addNameExtension(reportWindowTitle);
    warpOptions = warp.getOptions();
  }

  @Override
  protected GuiWindow displayVisualization() {
    return new GuiWindow(warpOptions, reportWindowTitle, createReport());
  }

  @Override
  protected Description visualization() {
    return createReport();
  }

  private Description createReport () {
    Description report;
    if (content.size() > 0) {
      report = new Description(new String (title + " Report"));
      report.addAll(content);
    } else {
      report = new Description(new String ("No " + title + " to report"));
    }
    return report;
  }

}
