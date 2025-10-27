# CS2820 Fall 2025 WARP Project Code

This is Benjamin Kleiman's version of WARP that will be used for CS:2820 in the year 2025.

HW2 - Dallas Jackson: Added JavaDoc comments for Warp, Channels, and ChannelAnalysis

HW2 - Benjamin Kleiman: Added JavaDoc comments for ProgramVisualization, VisualizationFactory, and WorkLoad

HW3 - Dallas Jackson: Created JUnit tests for all the required methods in Channels.java. Also fixed the error that was described by the assignment in the getChannelsSet method, which previously returned a copy of the HashSet which could cause removals to be lost and lead to channel conflicts. This code was changed such that instead of returning a copy, it returned the actual object. 

HW3 - Benjamin Kleiman: Created JUnit tests for all the required methods in WorkLoad.java. No errors were encountered. 

HW4 - Dallas Jackson: Added UML models Visualizaiton.uml, Workload.uml, and Channel.uml, also added getChannelAnalysisTable method to ChannelAnalysis

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
