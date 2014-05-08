package window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.StartGame;


public class MenubarActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="Neues Spiel"){
			StartGame.createNewGame();
		}
		
		if(e.getActionCommand()=="Spiel pausieren"){
			if (StartGame.game != null){
				if(StartGame.game.pause==true){
					StartGame.game.pause = false;
				}
				else{
					StartGame.game.pause = true;					
				}
				StartGame.game.pauseGame();
			}
		}
		
		if (e.getActionCommand()=="Naechster Level"){
			StartGame.game.startLevel();
		}
		
		if(e.getActionCommand()=="Optionen"){
			
		}
		
		if(e.getActionCommand()=="Credits"){
			
		}
				
		if(e.getActionCommand()=="Beenden"){
			System.exit(0);
		}
	}

}
