package components;

import game.Level;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.StartGame;


public class ComponentStats extends ComponentMain{

	private static final long serialVersionUID = -7538367114384095895L;
	private Level curLevel = null;
	private Font stdFont = new Font("Comic Sans MS",0,16);
	
	private int[] fieldDimension = {super.getFieldDimension()[0],50};

	public ComponentStats(){
		setSize(fieldDimension[0],fieldDimension[1]);
		setPreferredSize(getSize());
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,fieldDimension[0],fieldDimension[1]);
		if (curLevel != null){
			g.setColor(Color.WHITE);
			g.fillRect(0,fieldDimension[1]-3,fieldDimension[0],3);
			paintLives(g);
			paintLevel(g);
			paintPoints(g);
		}
	}
	
	private void paintLives(Graphics g){
		g.setColor(Color.WHITE);
		g.setFont(stdFont);
		g.drawString("Leben uebrig:",fieldDimension[0]-180,fieldDimension[1]-(fieldDimension[1]-14)/2);
		for (int i = 0; i < StartGame.game.getLivesLeft(); i++){
			g.fillOval((fieldDimension[0]-70)+(i*20),(fieldDimension[1]-16)/2,16,16);
		}
	}
	
	private void paintLevel(Graphics g){
		g.setColor(Color.WHITE);
		g.setFont(stdFont);
		g.drawString("Level: "+StartGame.game.getLevelCount(),10,fieldDimension[1]-(fieldDimension[1]-14)/2);
	}
	
	private void paintPoints(Graphics g){
		g.setColor(Color.WHITE);
		g.setFont(stdFont);
		g.drawString("Punkte: "+StartGame.game.getPkt(),100,fieldDimension[1]-(fieldDimension[1]-14)/2);
	}
	
	public void setCurrentLevel(Level level){
		this.curLevel = level;
	}
	
}
