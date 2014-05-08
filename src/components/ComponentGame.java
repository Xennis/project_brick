package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.StartGame;


public class ComponentGame extends ComponentMain{

	private static final long serialVersionUID = 3724293496270766642L;
	private int[] fieldDimension = StartGame.comp.getFieldDimension();
	
	public ComponentGame(){
		super();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,g.getClipBounds().width,g.getClipBounds().height);
		if (StartGame.game != null)
			for (int i = 0; i < StartGame.game.elements.size(); i++){
				StartGame.game.elements.get(i).paintElement(g);
			}
		if (StartGame.game.pause){
			g.setColor(new Color(255,255,255,200));
			g.fillRect(0,0,fieldDimension[0]-1,fieldDimension[1]-1);
			g.setColor(Color.darkGray);
			g.fillRect(0,(fieldDimension[1]/2)-50,fieldDimension[0]-1,100);
			g.setColor(Color.RED);
			g.drawRect(0,(fieldDimension[1]/2)-50,fieldDimension[0]-1,100);
			g.setColor(Color.RED);
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
			g.drawString("Paused - Press p for resume", (fieldDimension[0]-330)/2, (fieldDimension[1]+20)/2);
		}
	}	
}
