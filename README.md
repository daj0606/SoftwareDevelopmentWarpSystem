# CS2820 Fall 2025 WARP Project Code
This version 2.0 code base will be used for the University of Iowa 
CS 2820 Introduction to Software Development course. The code was 
developed by Steve Goddard for the WARP sensor network research project. 
It was first written in Swift and rewritten in Java. It was then 
rewritten again in an object-oriented programming style. It was a quick
hack, and it needs a lot of cleanup and refactoring. A perfect code base to teach
the value of software developement fundamentals!
<p>
This latest version (2.0) contains a full Graphical User Interface (GUI) and 
updated windows for displaying reports, tables, and GraphStream graphs. This 
version has also undergone signficant refactoring of GUI-related code, Warp.java, 
and the factory classes.
<br>
Remember to add the scheduler to all --all configurations to control data output.
#HW2
This assignment was completed by Sean Mukunza and David Lin. 
David Lin completed the javadoc for ProgramVisualization.java, Workload.java(a-e), and Program.java.
Sean Mukunza completed the javadoc for ChannelAnalysis.java, VisualizationFactory.java,
Workload.java (f-j). 
Warp.java and Channels.java were completed jointly. 
#HW3
The tests for WorkLoad.java were completed my David Lin, and the tests for Channels.java were completed by Sean Mukunza. 
While writing the JUnit tests for `WorkLoad.java`, we ran into several parsing errors that we investigated. This led us to a key discovery about the project's input file grammar.

Problem Description:
    We found a conflict between the features in the `WorkLoad.java` code and the rules of the input file grammar. The Java code has methods like `setNodeChannel`, which suggests we should be able to define a node's channel in the input file. Our tests were originally written to check this functionality. However, after many attempts, we found that the grammar doesn't actually support a syntax for defining nodes and their properties.

How We Found The Issue:**
    We tried many different syntax formats to define a node with a channel, such as `A : 1;`, `A (1);`, and `A : (1)`. All of these attempts caused different parsing errors.

There were potential errors in Channels.java, however we were not able to find them. 
Notable Edge Cases Covered:
Missing slot for getChannelSet, isEmpty, addChannel, removeChannel → throws.
Defensive copy proof: mutate returned set, then fetch again (unchanged).
Duplicate adds / removes of non-existent entries don’t change size.
#HW4
Added UML Diagrams for the Channel associated classes, 'Visualization.java', and 'WorkLoad.java'.
Performed Reverse engineering with ChannelAnalysis to build getChannelAnalysisTable(). Used update class path method in diagram window, as I was not prompted by to reverse engineer on re-launch.
#HW5
Refactored the method findNextAvailableChannel in Program.java, reducing method length and duplicate code. Created a helper method to remove unavailable channels from the pool of potential options. Added Javadoc comments to both the findNextAvailableChannel method and helper method. Created patched for updated code in UML diagrams. Screenshots of a closeup Program.java and and overall view of the UML diagram are stored in the UML_LAB folder.
Assignment was completed via pair programming between Sean Mukunza and Dallas Jackson.