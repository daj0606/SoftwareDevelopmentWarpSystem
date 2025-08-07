package edu.uiowa.cs.warp;

import com.mkyong.system.OSValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;

/**
 * Provides file management support for Warp. Defines file name templates
 * and sets up default base directory and output directory. Provides
 * methods to read the input graph file, etc.
 * 
 * @author sgoddard
 * @version 2.0 Fall 2025
 *
 */
public class FileManager {
  private static final String UNKNOWN = "Unknown";
  private String baseDirectory;
  private Boolean verbose;
  private String graphFileName = UNKNOWN; // name of the input graph file
  private String graphFileContents = UNKNOWN;

  /**
   * Constructor. 
   */
  public FileManager() {
    /* Get current directory path and set to the  base directory  */
    baseDirectory = System.getProperty("user.dir"); 

    verbose = false; // initialize verbose to false
  }

  public String getDocumentsDirectory() { // return the 'Documents Directory' for the appropriate OS
    String documentsDirectory;
    if (OSValidator.isWindows()) {
      documentsDirectory = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    } else if (OSValidator.isMac()) {
      documentsDirectory = System.getProperty("user.home") + File.separator + "Documents";
    } else { // for all other Unix systems
      documentsDirectory = System.getProperty("user.home"); // assume $HOME for Unix systems (not
      // MACOS)
    }
    return documentsDirectory;
  }

  public String getCurrentDirectory() {
    return System.getProperty("user.dir");
  }

  public void setBaseDirectory(String directory) {
    baseDirectory = directory;
  }

  public String getBaseDirectory() {
    return baseDirectory;
  }

  /** 
   * Build the name extension used for all files created by this tool. The nameExtension will be
   * added to the base input file name so that all of the output files can be associated with the
   * input file The file naming pattern is: baseFileNameFileType-NonDefaultParameterList where
   * FileType is Schedule, SimInput, ReliabilityMatrix, or ReliabilityArray Default parameters are
   * not identified in the NameExtension All parameters used to create the file, should be listed
   * near the top of the file in the Parameter Section, followed by the file contents.
   * 
   */
  public String createFile(String file, String nameExtension, String suffix) {
    /*
     * we don't actually create the file...it will be created when written. This routine really
     * just creates the file name ;-)
     */
    Integer suffixIndex = file.lastIndexOf('.');
    String fileString = file;
    if (suffixIndex > 0) { // if a suffix exists, index will be > 0
      fileString = file.substring(0, suffixIndex); // get the file string sans the suffix
    }
    /*
     * fileString has no suffix at this point (removed if it existed) Now add file name extension
     * and suffix
     */
    fileString = fileString + nameExtension + suffix;

    if (verbose) {
      System.out.println("File " + fileString + " is created!");
    }
    return fileString;
  }

  /**
   * Create a template for the base file name that will be used in visualizations. The
   * template includes a directory in which the output visualiation files will be placed. If that
   * output directory does not exists, it will be created. The file name template, including
   * the path name is returned.
   *  
   * @param baseName of file  used for the template
   * @param outputDirectory where files will be stored
   * @return
   */
  public String createFileNameTemplate(String baseName, String outputDirectory) {
    String fileNameTemplate;
    var workingDirectory = getBaseDirectory();
    var newDirectory = createDirectory(workingDirectory, outputDirectory);
    /* Now create the fileNameTemplate using full output path and input filename */
    if (baseName.contains("/")) {
      var index = baseName.lastIndexOf("/") + 1;
      fileNameTemplate = newDirectory + File.separator + baseName.substring(index);
    } else {
      fileNameTemplate = newDirectory + File.separator + baseName;
    }
    return fileNameTemplate;
  }

  public String createFile(String file, String suffix) {
    Integer suffixIndex = file.lastIndexOf('.');
    String fileString = file;
    if (suffixIndex > 0) { 
      /*  if a suffix exists, index will be > 0
       *  get the file string sans the suffix
       */
      fileString = file.substring(0, suffixIndex - 1); 
    }
    /* fileString has no suffix at this point (removed if it existed)
     * Now add file name extension and suffix
     * */
    fileString = fileString + suffix;
    /* we don't actually create the file...it will be created when written to
     * this routine really just creates the file name ;-)
     * */
    if (verbose) {
      System.out.println("File " + fileString + " is created!");
    }
    return fileString;
  }

  public String createDirectory(String directory, String subDirectory) {
    String newDirectory;
    if (subDirectory.startsWith("/")) { 
      /* check if full path provided, and if so, use it.*/
      newDirectory = subDirectory; 
    } else { 
      /* subDirectory has relative path, so just append */
      newDirectory = directory + File.separator + subDirectory;
    }
    try {
      Path path = Paths.get(newDirectory);
      Files.createDirectories(path);
      if (verbose) {
        System.out.println("Directory " + newDirectory + " is created!");
      }
    } catch (IOException e) {
      /* in case of error, just use the initial directory */
      System.err.println("Failed to create directory!" + e.getMessage());
      newDirectory = directory; 
    }
    return newDirectory;
  }

  public void writeFile(String file, String fileContents) {
    Path fileName = Path.of(file);
    try {
      Files.writeString(fileName, fileContents); // comment out if tag is being used (line above)
    } catch (IOException e) {
      /* the file will be closed automatically upon exit of this try block */
      System.err.println("Error on writing file contents to file" + file + ": " + e.getMessage());
    } 
  }

  public String readFile(String file) {
    Path fileName = Path.of(file);
    String contents = null;
    try {
      contents = Files.readString(fileName);
    } catch (IOException e) {
      /* the file will be closed automatically upon exit of this try block */
      System.err.println("Error on reading file" + file + ": " + e.getMessage());
    } 
    return contents;
  }

  /* File Utilities follow */
  public Boolean fileExists(String fileName) {
    File file = new File(fileName);
    Boolean exists = false;
    if (file.exists() && file.isFile()) {
      exists = true; // exists in the current directory
    }
    return exists;
  }

  public Boolean isValidDirectoryName(String name) {
    if (name == null || name.trim().isEmpty()) {
      return false; // Null or empty names are not valid
    }
    try {
      Paths.get(name);
      return true;
    } catch (InvalidPathException | NullPointerException e) {
      return false; // Catches issues with invalid characters or null paths
    }
  }

  public Boolean directoryExists(String directoryName) {
    File directory = new File(directoryName);
    Boolean exists = false;
    if (directory.exists() && directory.isDirectory()) {
      exists = true; // exists in the current directory
    }
    return exists;
  }

  /* Methods to support reading read the input graph file that 
   * describes the workload follows */
  public String getGraphFileName ()  {
    return graphFileName;
  }

  private void setGraphFileName (String inputFile) {
    graphFileName = inputFile;
  }

  public String verifyGraphFileName(String inputFile) {
    String fileName=UNKNOWN;
    /* Read the graph input text file and handle all errors, returning its contents */
    try {
      fileName = getInputGraphFileName(inputFile);
    } catch (Exception e) {
      System.err.println("Failed get an input file.");
      System.exit(-1);
    }
    return fileName;
  }

  public String readGraphFile(String inputFile) {
    /* Read the graph input text file and return its contents */
    verifyGraphFileName(inputFile); // catches exception if a valid file can't be found and exits
    String inputFileName = getGraphFileName();
    graphFileContents = readGraphFileContents(inputFileName);
    return graphFileContents;
  }

  private String getInputGraphFileName (String inputFile) throws RuntimeException {
    String workingDirectory;
    String fileName = UNKNOWN;
    workingDirectory = getBaseDirectory();
    if (inputFile == null || !fileExists(inputFile)) { 
      /* if fileName is nil or it doesn't exist, then prompt for a new input file */
      System.out.printf("Working directory is %s\n", workingDirectory);
      System.out.print("Enter input file: ");
      BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
      try {
        fileName = bufferRead.readLine();
      } catch (IOException e) {
        // e.printStackTrace();
        System.err.println("Failed to read input line from console" + e.getMessage());
      }
    } else {
      fileName = inputFile;
    }
    setGraphFileName(fileName);
    File tempFile = new File(getGraphFileName()); // create a temp File object with graphName
    if (tempFile.exists()) {  // see if the file exists
      if (verbose) {
        printVerboseMessage(workingDirectory, fileName, getGraphFileName());
      }
    } else { // try the working directory
      setGraphFileName(workingDirectory + File.separator + fileName);
      tempFile = new File(getGraphFileName()); // create a temp File object with graphName
      if (tempFile.exists()) {  // see if the file exists
        if (verbose) {
          printVerboseMessage(workingDirectory, fileName, getGraphFileName());
        }
      } else {
        if (verbose) {
          printVerboseMessage(workingDirectory, fileName, getGraphFileName());
        }
        System.err.printf("\n\tERROR: input file %s doesn't exist!!\n", fileName);
        throw new RuntimeException();
      }
    }
    return fileName;
  }

  private String readGraphFileContents (String inputFile)  {
    String graphFileContents = readFile(inputFile);
    if (verbose) {
      System.out.println("************************************");
      System.out.println("Graph File Read:");
      System.out.println(graphFileContents);
      System.out.println("************************************\n");
    }
    return graphFileContents;
  }

  private void printVerboseMessage(String workingDirectory, String fileName, 
      String graphFileName) {
    System.out.printf("Working directory is %s\n", workingDirectory);
    System.out.printf("Input file name is %s\n", fileName);
    System.out.printf("Graph file %s doesn't exist\n", graphFileName);
  }
}
