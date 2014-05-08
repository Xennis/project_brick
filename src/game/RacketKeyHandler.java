package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.StartGame;


public class RacketKeyHandler implements KeyListener{

	private static boolean racketMovingLeft = false;
	private static boolean racketMovingRight = false;
	private boolean gameAlive = false;

	public void keyPressed(KeyEvent arg0) {
		
		switch (arg0.getKeyCode()){
			case 37 : handleLeft(); break;	//linke Pfeiltaste
			case 39 : handleRight(); break;	//rechte Pfeiltaste
		}
	}
	
	public void keyReleased(KeyEvent arg0) {
		switch (arg0.getKeyCode()){
			case 37 : endLeft(); break;
			case 39 : endRight(); break;
		}
	}
	

	public void keyTyped(KeyEvent arg0) {
		
	}
	
	private void handleLeft(){
		if (!racketMovingLeft && gameAlive){
			StartGame.game.racket.startRunning(-1);
			racketMovingLeft = true;
		}		
	}
	
	private void handleRight(){
		if (!racketMovingRight && gameAlive){
			StartGame.game.racket.startRunning(1);
			racketMovingRight = true;
		}
	}
	
	private void endLeft(){
		if (racketMovingLeft){
			StartGame.game.racket.endRunning(-1);
			racketMovingLeft = false;
		}
	}
	
	private void endRight(){
		if (racketMovingRight){
			StartGame.game.racket.endRunning(1);
			racketMovingRight = false;
		}
	}
	
	public void setGameAlive(boolean alive){
		if (alive)
			this.gameAlive = true;
		else{
			endLeft();
			endRight();
			this.gameAlive = false;
		}
	}

}
