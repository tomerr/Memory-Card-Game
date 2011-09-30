package com.tomerr.cards;
import java.util.ArrayList;
import java.util.Collections;

public class Deck extends Flippable {

	ArrayList<Card> deckOfCards = new ArrayList<Card>();
	private String suits = "shdc";
	private final String faces = "a23456789tjqk";
	//private final String faces = "a2";

	public Deck(int difficulty) {
		deckOfCards.clear();
		createDeck(difficulty);
	}
	private void createDeck(int difficulty) {
		if (difficulty == 1) {
			suits = "s";
		}
		else if (difficulty == 2) {
			suits = "sh";
		}
		else if (difficulty == 3) {
			suits = "shd";
		}
		else if (difficulty == 4) {
			suits = "shdc";
		}
		String symbol;
		// Return pairs of cards.
		for (int i=0; i<2; i++) {
			for (int suit = 0; suit < suits.length(); suit++) {
				for (int face = 0; face < faces.length(); face++) {
					symbol = faces.charAt(face) + "" + suits.charAt(suit) + i;
					Card card = new Card(symbol);
					deckOfCards.add(card);
				}
			}
		}
	}
	public void shuffle() {
		Collections.shuffle(deckOfCards);
	}
	public ArrayList<Card> getDeckOfCards() {
		return deckOfCards;
	}
	public void flipCardInDeck(String symbol) {
		for (int i=0; i<deckOfCards.size(); i++) {
			if (symbol.equals(deckOfCards.get(i))) {
				deckOfCards.get(i).flip();
			}
		}
	}
	//Composite Methods
	@Override
	public void flip() {
		for (int i=0; i<deckOfCards.size(); i++) {
			deckOfCards.get(i).flip();
		}
	}
	@Override
	public void hide() {
		for (int i=0; i<deckOfCards.size(); i++) {
			deckOfCards.get(i).hide();
			deckOfCards.get(i).setFlipable(true);
		}
	}
	@Override
	public void reveal() {
		for (int i=0; i<deckOfCards.size(); i++) {
			deckOfCards.get(i).reveal();
		}
	}
	public void removeDecorator() {
		for (int i=0; i<deckOfCards.size(); i++) {
			deckOfCards.get(i).removeDecorator();
		}
	}
}
