package main;
import window.Window;
import game.RacketKeyHandler;
import components.*;


public class StartGame {
	
	public static Window window;
	public static Game game;
	public static ComponentMain comp;
	public static RacketKeyHandler keyhndl;
	public static ComponentStats statsbar;
	
	public static void main(String[] args){
		window = new Window("Project Brick");
	}
	
	public static void createNewGame(){
		if (game == null){
			game = new Game();
		} else{
			game.endGame();
			game = new Game();
		}
	}
	
}