package edu.uiowa.cs.warp;

/**
 * Simple subscriber interface. The implementer of this
 * interface registers with a Publisher, and the Publisher
 * calls update() to notify the subscriber of an event that
 * has occurred.
 * 
 */
public interface Observer {
    void update(String message);
}
