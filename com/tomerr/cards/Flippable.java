package com.tomerr.cards;
import javax.swing.JLabel;

//Composite + Template Method
abstract class Flippable extends JLabel {

	protected boolean flipable = true;
	protected boolean hidden = true;

	abstract public void hide();
	abstract public void reveal();

	//Composite Methods
	public void flip() {
		if (flipable) {
			if (hidden) {
				View.getInstance().playSound("reveal.wav");
				reveal();
			}
			else {
				hide();
			}
		}
	}
}
