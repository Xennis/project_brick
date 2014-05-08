package main;


import game.Ball;
import game.Brick;
import game.Level;
import game.Racket;

import java.awt.Color;
import java.util.ArrayList;


import components.ComponentGame;
import components.ComponentText;


public class Game implements Runnable{
	
	public ArrayList<GameElement> elements = new ArrayList<GameElement>();
	public ArrayList<Brick> bricks = new ArrayList<Brick>();
	public Ball b1;
	public Racket racket;
	private boolean isAlive = true;
	public boolean pause = false;
	
	private Thread gameThread;
	private Thread racketThread;
	private Thread ballThread; // later on there will be an ArrayList<BallThread>
	
	public static int ADD = 1; // Mask for adding points to the actual ones instead of overwriting
	
	/*
	 * Spielerstats-Werte
	 */
	private Level curLevel;
	private ArrayList<Level> levelHistory = new ArrayList<Level>();
	private int lives = 3; 
	private int levelCount = 0;
	private int pkt = 0;
	
	/**
	 * 
	 * Game-Konstruktor
	 * 
	 */
	public Game(){;		
		gameThread = new Thread(this);
		gameThread.setPriority(10);
		gameThread.start();
		
		startLevel();
	}
	
	/*
	 * Der GameThread dient zur Aktualisierung des
	 * GameWindows mittels repaint
	 */
	public void run(){
		while(isAlive){
			StartGame.comp.repaint();
			StartGame.statsbar.repaint();
			try{
				Thread.sleep(30);
			} catch(InterruptedException ie){
				System.err.println(ie);
			}						
		}
	}
	
	/*
	 * Neue Idee der Ballbehandlung:
	 * geht der Ball verloren, ruft der Ball
	 * Game.ballLost() auf, Game verarbeitet
	 * den Verlust und setzt ggf einen neuen Ball
	 */
	
	/**
	 * 
	 * Wird aufgerufen, wenn der Ball verloren geht
	 * 
	 */
	public void ballLost(){
		lives--;
		if (lives < 0){
			StartGame.game.endGame();
			StartGame.game = null;
			StartGame.window.loadMainComponent(new ComponentText("Gameover", Color.RED));
		} else{
			elements.remove(b1);
			b1.endBall();
			double[] ballPosition = {250, 250};
			b1 = new Ball(ballPosition, Ball.getAngle(0,1));
			ballThread = b1.getThread();
			elements.add(b1);
		}
	}
	
	/**
	 * Ueberprueft, ob das Level zu Ende ist
	 */
	public void checkLevelCompleted(){
		if (this.bricks.size() <= 0){
			b1.endBall();
			racket.endRacket();
			bricks.removeAll(bricks);
			elements.removeAll(elements);
			StartGame.keyhndl.setGameAlive(false);
			StartGame.window.menuBar.setNextLevel(true);
			StartGame.window.loadMainComponent(new ComponentText("Level erfolgreich :-)", Color.GREEN));
		}
	}
	
	/**
	 * Startet ein Level
	 * 
	 * @param num
	 * 			Nummer des Levels
	 */
	public void startLevel(int num){
		/* Start Level Creating */
		curLevel = new Level(num);
		loadCurrentLevel();
		/* End Level Creating */
		
		/* Start setting Stats-relevant objects */
		this.levelCount++;
		this.levelHistory.add(curLevel);
		/* End setting Stats-relevant objects
		
		/* Start GameElement Creating */
		racket = new Racket();
		double[] ballPosition = {250, 250};
		b1 = new Ball(ballPosition, Ball.getAngle(0,1));
		/* End GameElement Creating */
		
		/* Adding GameElements into List for painting */
		elements.add(racket);
		elements.add(b1);
		/* End of adding */
		
		/* Getting the Threads of the GameElements */
		racketThread = racket.getThread();
		ballThread = b1.getThread();
		/* End of getting Threads */
		
		/* Starting the game */
		StartGame.statsbar.setCurrentLevel(curLevel);
		StartGame.keyhndl.setGameAlive(true);
		StartGame.window.menuBar.setNextLevel(false);
		StartGame.window.loadMainComponent(new ComponentGame());
		/* Finished starting the game */
	}
	
	public void startLevel(){
		int numberOfAvailableLevels = Level.amountOfLevels-this.levelHistory.size();
		int generatedPosition = (int)((Math.random()*numberOfAvailableLevels)+1);
		ArrayList<Integer> availableIDs = new ArrayList<Integer>();
		for (int i = 1; i <= Level.amountOfLevels; i++){
			availableIDs.add(i);
		}
		for (int i = 0; i < levelHistory.size(); i++){
			
			int id = -1;
			for (int j = 0; j < availableIDs.size(); j++){
				if (availableIDs.get(j).equals(levelHistory.get(i).getID()))
					id = j;
			}
			if (id != -1)
				availableIDs.remove(id);
		}
		if (availableIDs.size() >= generatedPosition){
			startLevel(availableIDs.get(generatedPosition-1));
		} else{
			endGame();
			StartGame.game = null;
			StartGame.window.loadMainComponent(new ComponentText("Gameover", Color.RED));
			System.out.println("Keine Levels mehr verfuegbar");
		}
	}
	
	public void endGame(){
		if (pause)
			pauseGame(); // unpausieren, bevor Spiel geloescht wird
		this.isAlive = false;
		b1.endBall();
		racket.endRacket();
		bricks = null;
		elements = null;
		StartGame.keyhndl.setGameAlive(false);
		StartGame.statsbar.setCurrentLevel(null);
	}
	
	@SuppressWarnings("deprecation")
	public void pauseGame(){
		if (pause){
			racketThread.suspend();
			ballThread.suspend();
		} else{
			racketThread.resume();
			b1.setResume(); // needed for preventing further calculations
			ballThread.resume();
		}
	}
	
	public void loadCurrentLevel() {
		ArrayList<Brick> levelBricks = this.curLevel.getBricks();
		for(int i = 0; i < levelBricks.size(); i++) {
			this.bricks.add(levelBricks.get(i));
			this.elements.add(levelBricks.get(i));
		}
	}
	
	public void setPkt(int pkt, int type){
		switch(type){
			case 1 : this.pkt += pkt; break;
			default : this.pkt = pkt;
		}
	}
	
	public void setPkt(int pkt){
		setPkt(pkt,0);
	}
	
	public int getLevelCount(){
		return this.levelCount;
	}
	
	public int getPkt(){
		return this.pkt;
	}
	
	public Level getCurrentLevel(){
		return this.curLevel;
	}
	
	public int getLivesLeft(){
		return this.lives;
	}
	
}
