package com.tomerr.cards;

public class CareTaker {
	
	private static CareTaker _instance;
	private Memento savedState;

	public static CareTaker getInstance() {
		if (_instance == null) {
			_instance = new CareTaker();
		}
		return _instance;
	}
	public void setMemento(Memento m) {
		savedState = m;
	}
	public Memento getMemento() { 
		return savedState; 
	}
}   


