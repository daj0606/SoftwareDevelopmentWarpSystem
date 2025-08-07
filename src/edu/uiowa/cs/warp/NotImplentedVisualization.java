package edu.uiowa.cs.warp;

public class NotImplentedVisualization extends Visualization {

	private static final String SUFFIX = ".txt";
	private static final String CONTENT = "Not Implemented";
	
	NotImplentedVisualization(Options options, String extension) {
		super(options, SUFFIX);
		setNameExtension(extension);
	}

	@Override
	protected Description visualization() {
		return new Description(CONTENT);
	}

}
