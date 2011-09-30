package com.tomerr.cards;

class Memento {
	
	private Deck deck;

	public Memento(Deck deck) {
		this.deck = deck;
	}
	public Deck getDeck() {
		return deck;
	}
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
}