package edu.uiowa.cs.warp;

import java.util.ArrayList;
import java.util.List;

public class Observable {
  private List<Observer> observers = new ArrayList<>();

  public void registerObserver(Observer subscriber) {
      observers.add(subscriber);
  }

  public void removeObserver(Observer subscriber) {
      observers.remove(subscriber);
  }

  public void notifyObservers(String message) {
      for (Observer subscriber : observers) {
          subscriber.update(message);
      }
  }
}
