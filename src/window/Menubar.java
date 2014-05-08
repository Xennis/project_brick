package window;
import java.awt.event.KeyEvent;
import javax.swing.*;


public class Menubar extends JMenuBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenu file,help;
	JMenuItem newGame,pause,next,options,exit,credits,help2;
	public MenubarActionListener MenubarActionListener = new MenubarActionListener();
	
	private boolean nextGameClickable = false;
	
	public Menubar()
	{
		file	= new JMenu("Datei");
		help 	= new JMenu("Hilfe");
		newGame = new JMenuItem("Neues Spiel");
		pause 	= new JMenuItem("Spiel pausieren");
		next 	= new JMenuItem("Naechster Level");
		options = new JMenuItem("Optionen");
		exit 	= new JMenuItem("Beenden");
		credits = new JMenuItem("Credits");
		help2	= new JMenuItem("?");
		
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0));
		pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,0));
		next.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0));
		
		setClickable(next,nextGameClickable);
		
		exit.setMnemonic('B');
		file.setMnemonic('D');
		
		add(file);
		file.add(newGame);
		newGame.addActionListener(this.MenubarActionListener);
		file.add(pause);
		pause.addActionListener(this.MenubarActionListener);
		file.add(next);
		next.addActionListener(this.MenubarActionListener);
		file.addSeparator();
		file.add(options);
		options.addActionListener(this.MenubarActionListener);
		file.add(exit);
		exit.addActionListener(this.MenubarActionListener);
		
		add(help);
		help.add(credits);
		help.add(help2);
	}
	
	private void setClickable(JMenuItem item,boolean value){
		item.setEnabled(value);
	}
	
	public void setNextLevel(boolean clickable){
		setClickable(next,clickable);
	}
}
