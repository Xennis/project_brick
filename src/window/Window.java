package window;
import game.RacketKeyHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

import main.StartGame;

import components.ComponentMain;
import components.ComponentStats;
import components.ComponentText;


public class Window extends JFrame{

	private static final long serialVersionUID = 5446208496818554929L;
	private boolean mainLoaded = false;
	public Menubar menuBar = new Menubar();
	
	/**
	 * Konstruktor Window
	 * 
	 * @param title
	 * 			Titel des Fensters
	 */
	public Window(String title){		
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(new BorderLayout());
		setResizable(false);
		loadMainComponent(new ComponentText("Willkommen zu Brick", Color.WHITE));
		mainLoaded = true;
		setJMenuBar(menuBar);
		StartGame.statsbar = new ComponentStats();
		add(StartGame.statsbar,BorderLayout.NORTH);
		StartGame.keyhndl = new RacketKeyHandler();
		addKeyListener(StartGame.keyhndl);
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int)((screen.width - getWidth()) / 2), (int)((screen.height - getHeight()) / 2));
	}
	
	public void loadMainComponent(ComponentMain type){
		if (mainLoaded)
			remove(StartGame.comp);
		StartGame.comp = type;
		add(StartGame.comp,BorderLayout.CENTER);
		validate();
	}
}
