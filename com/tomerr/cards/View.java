package com.tomerr.cards;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
@SuppressWarnings("serial")
public class View extends JFrame implements Observer {

	private ClassLoader cldr = this.getClass().getClassLoader();
	private final String version = "Cards Memory Game v1.02";
	private JDesktopPane panel = new JDesktopPane();
	private static JMenu gameMenu;
	private static JMenuItem newGameItem;
	private static JMenu gameDifficulty;
	private static JMenuItem undoItem;
	private static JRadioButtonMenuItem difficulty1, difficulty2, difficulty3;
	private static View _instance;
	private int currentDifficulty = 1;
	private FlowLayout lo = new FlowLayout(FlowLayout.CENTER);

	//View Singleton
	public static View getInstance() {
		if (_instance == null) {
			_instance = new View();
			}
			return _instance;
	}
	public void init() {
		Model.getInstance().addObserver(this); //Add Observer to Model.
		// get the screen size as a java dimension
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height-33;
		int width = screenSize.width;
		// set the jframe height and width
		setPreferredSize(new Dimension(width, height));
		lo.setHgap(4);
		lo.setVgap(4);
		panel.setLayout(lo);
		panel.setBackground(Color.decode("#006600"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		buildMenu();
		//Window close Listener
		addWindowListener(new WindowAdapter(){
		      public void windowClosing(WindowEvent we){
		        System.exit(0);
		      }
		    });
		setTitle("Cards Memory Game - Score: 0");
		setContentPane(panel);
		setFrameIcon();
		pack();
	}
	private void setFrameIcon() {
		String imagePath = "image/game.gif";
		URL url = cldr.getResource(imagePath);
		Toolkit kit = Toolkit.getDefaultToolkit(); 
		this.setIconImage(kit.getImage(url)); 		
	}
	private void buildMenu() {
		JMenuBar myMenuBar = new JMenuBar();
		JMenu gameMenu = getGameMenu();
		JMenu helpMenu = getInfoMenu();
		myMenuBar.add(gameMenu);
		myMenuBar.add(helpMenu);
		setJMenuBar(myMenuBar);
	}
	private JMenu getGameMenu() {
		gameMenu = new JMenu("Game");
		newGameItem = new JMenuItem("New");
		gameDifficulty = new JMenu("Difficulty");
		undoItem = new JMenuItem("Restart");
		difficulty1 = new JRadioButtonMenuItem("Easy");
		difficulty1.setSelected(true);
		difficulty2 = new JRadioButtonMenuItem("Medium");
		difficulty3 = new JRadioButtonMenuItem("Hard");
		difficulty1.addActionListener(new viewHandler());
		difficulty2.addActionListener(new viewHandler());
		difficulty3.addActionListener(new viewHandler());
		gameMenu.add(newGameItem);
		newGameItem.addActionListener(new newGameListener());
		undoItem.addActionListener(new undoListener());
		undoItem.setEnabled(false);
		gameDifficulty.add(difficulty1);
		gameDifficulty.add(difficulty2);
		gameDifficulty.add(difficulty3);
		gameMenu.add(gameDifficulty);
		gameMenu.addSeparator();
		gameMenu.add(undoItem);
		return gameMenu;
	}
	private JMenu getInfoMenu() {
		JMenu helpMenu = new JMenu("Info");
		JMenuItem aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);
		aboutItem.addActionListener(new aboutListener());
		return helpMenu;
	}
	//End of GUI
	public void updateCards() {
		for (int i=0; i<Model.getInstance().getDeck().getDeckOfCards().size(); i++) {
			//Draw card on screen
			panel.add(Model.getInstance().getDeck().getDeckOfCards().get(i));
			//Add Listener to card
			Model.getInstance().getDeck().getDeckOfCards().get(i).addCardListener(new cardListener());
		}
		validate();
	}
	public int getDifficulty() {
		return currentDifficulty;
	}
	public void reset() {
		panel.removeAll();
		//Model.getInstance().reset();
		//pack();
		repaint();
	}
	class viewHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == difficulty1) {
				difficulty1.setSelected(true);
				difficulty2.setSelected(false);
				difficulty3.setSelected(false);
				if (currentDifficulty != 1) {
					currentDifficulty = 1;
					newGameItem.doClick();
				}
			}
			else if (e.getSource() == difficulty2) {
				difficulty1.setSelected(false);
				difficulty2.setSelected(true);
				difficulty3.setSelected(false);
				if (currentDifficulty != 2) {
					currentDifficulty = 2;
					newGameItem.doClick();
				}
			}
			else if (e.getSource() == difficulty3) {
				difficulty1.setSelected(false);
				difficulty2.setSelected(false);
				difficulty3.setSelected(true);
				if (currentDifficulty != 3) {
					currentDifficulty = 3;
					newGameItem.doClick();
				}
			}
		}
	}
	public void updateScore(int score) {
		setTitle("Card Memory Game - Score: "+score);
	}
	private void endOfGame() {
		JFrame dialog = new JFrame();
		playSound("endOfGame.wav");
		JOptionPane.showMessageDialog(dialog,"Congratulations You Win!","Your Score: "+Model.getInstance().getScore(),JOptionPane.INFORMATION_MESSAGE);
	}
	class newGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			playSound("start.wav");
			reset();
			Model.getInstance().init();
			updateCards();
			Model.getInstance().revealAtBeginning();
			undoItem.setEnabled(true);
		}
	}
	class aboutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame dialog = new JFrame();
			JOptionPane
			.showMessageDialog(
					dialog,
					"  "
					+ version
					+ "\n              Developed by:\n           Tomer Robinson\n      (tomerr@tomerr.com)\n", "About", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	class undoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			reset();
			playSound("start.wav");
			Model.getInstance().restoreMemento(CareTaker.getInstance().getMemento());
			updateCards();
			Model.getInstance().revealAtBeginning();
		}
	}
	class cardListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {	
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
			if (!Model.getInstance().getLock()) {
				Card reference = (Card) e.getComponent();
				Model.getInstance().cardClicked(reference);
			}
		}
	}
	public void removeAllCards() throws ClassNotFoundException {
		for (int i=0; i<this.getComponentCount(); i++) {
			Class c = Class.forName("com.tomerr.cards.Card");
			if (this.getComponent(i).getClass().isInstance(c)) {
				panel.remove(this.getComponent(i));		
			}
		}
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		endOfGame();
	}
	public void playSound(String filename) {
		try {
			// Open an audio input stream.
			URL url = this.getClass().getClassLoader().getResource("sound/"+filename);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);	
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
