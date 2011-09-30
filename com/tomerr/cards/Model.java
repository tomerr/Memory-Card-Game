package com.tomerr.cards;

import java.awt.Color;
import java.util.Observable;
import javax.swing.SwingUtilities;

public class Model extends Observable {

	private final int REVEAL_TIMEOUT = 2000;
	private static Model _instance;
	private Deck deck;
	private int score;
	private int flippedCards = 0;
	private boolean locked = false;
	private Card flipped[] = new Card[2];

	//Model Singleton
	public static Model getInstance() {
		if (_instance == null) {
			_instance = new Model();
		}
		return _instance;
	}
	public void reset() {
		flippedCards = 0;
		this.score = 0;
		View.getInstance().updateScore(score);
	}
	public void revealAtBeginning() {
		//This is implementing the Deck Composite.
		locked = true;
		deck.reveal();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(REVEAL_TIMEOUT);
					deck.hide();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				locked = false;
			}
		});
	}
	public void init() {
		reset();
		deck = new Deck(View.getInstance().getDifficulty());
		deck.shuffle();
		//Save State (Memento)
		CareTaker.getInstance().setMemento(saveMemento());
	}
	public Deck getDeck() {
		return deck;
	}
	public int getScore() {
		return score;
	}
	public boolean getLock() {
		return locked;
	}
	public void publishToNet(final Card[] cards) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (int i=0; i<2; i++) {
					if (i==1) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					cards[i].flip();		
				}
				flipped = new Card[2];
				flippedCards = 0;
				if (score > 0 && !checkEndOfGame() && cards[0].isFlipable() &&  cards[1].isFlipable()) {
					score--;
					View.getInstance().updateScore(score);
				}
				locked = false;
			}
		});
	}
	private boolean checkEndOfGame() {
		int count = 0;
		for (int i=0; i<deck.deckOfCards.size(); i++) {
			if (!deck.getDeckOfCards().get(i).isFlipable()) {
				count++;
			}
		}
		if (count == deck.deckOfCards.size()) {
			return true;
		}
		else {
			return false;
		}
	}
	public void cardClicked(Card clickedCard) {
	if (!locked){
			if (clickedCard != flipped[0] && clickedCard != flipped[1] && clickedCard.isFlipable()) {
				if (flippedCards < 2) {
					if (clickedCard.isFlipable()) {
						clickedCard.flip();
					}
					else {
						flippedCards--;
					}
					if (flippedCards == 0) {
						synchronized (this) {
							flippedCards++;
						}
						flipped[0] = clickedCard;
					}
					else {
						synchronized (this) {
							flippedCards++;
						}
						flipped[1] = clickedCard;
					}
				}
				if (flippedCards == 2) {
					locked = true;
					if (flipped[1].getSymbol().substring(0,2).equals(flipped[0].getSymbol().substring(0,2))) {
						if (flipped[0].isFlipable() && flipped[1].isFlipable()) {
							// Pair found
							score+=2;
							View.getInstance().updateScore(score);
							View.getInstance().playSound("chime.wav");
							flipped[0].setFlipable(false);
							flipped[1].setFlipable(false);
							//Change Decorator (Border) for found pair.
							flipped[0].addDecorator(Color.decode("#FF33FF"));
							flipped[1].addDecorator(Color.decode("#FF33FF"));
							flipped = new Card[2];
						}
						flippedCards = 0;
						locked = false;
						if (checkEndOfGame()) {
							//Notify Observers on End Of Game.
							setChanged();
							notifyObservers();
						}
					}
					// If non-Pair, flip back.
					else {
						publishToNet(flipped);
						View.getInstance().playSound("hide.wav");
					}
				}
			}
		}
	}
	//Memento to save current Deck of Cards (For the Restart Game Option).
	public Memento saveMemento() {
		return new Memento(deck);
	}
	public void restoreMemento(Memento memento) {
		reset();
		this.deck = CareTaker.getInstance().getMemento().getDeck();
		this.deck.removeDecorator();
	}
}
