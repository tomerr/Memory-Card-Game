package com.tomerr.cards;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

public class Card extends Flippable {

	private ClassLoader cldr = this.getClass().getClassLoader();
	private String symbol;
	private URL imageURL;
	private ImageIcon img;
	private Dimension d = new Dimension(77, 97);
	
	public Card(String symbol) {
		super();
		this.symbol = symbol;
		this.setSize(d);
		hide();
		addDecorator(Color.decode("#006600"));
	}
	
	public void hide(){
		hidden = true;
		String imagePath = "image/b.gif";
		this.imageURL = cldr.getResource(imagePath);
		this.img = new ImageIcon(imageURL);
		this.setIcon(this.img);
	}
	public void reveal() {	
		hidden = false;
		String imagePath = "image/" + symbol.charAt(0) + symbol.charAt(1) + ".gif";
		this.imageURL = cldr.getResource(imagePath);
		this.img = new ImageIcon(imageURL);
		this.setIcon(this.img);
	}
	//End of Composite Methods
	public ImageIcon getImageIcon() {
		return img;
	}
	public void setFlipable(boolean val) {
		this.flipable = val;
	}
	public boolean isFlipable() {
		return flipable;
	}
	public void addCardListener(MouseListener al) {
		this.addMouseListener(al);
	}
	public String getSymbol() {
		return symbol;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void addDecorator(Color color) {
	//Decorator.
	this.setBorder(new MatteBorder(4, 4, 4, 4, color));
	}
	public void removeDecorator() {
		this.setBorder(new MatteBorder(4, 4, 4, 4, Color.decode("#006600")));
	}
}

