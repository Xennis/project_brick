package game;
import java.awt.Color;
import java.awt.Graphics;

import main.GameElement;


public class Racket implements GameElement, Runnable{
	
	public double x = 250;
	public double y = 560;
	private int width = 100;
	private int height = 14;
	private int[] widthMinMax = {70, 170};
	
	private int dir = 0;
	private int reserveDir = 0;
	private boolean isAlive = true;
	private static int speed = 150;
	
	private Thread thread;
	
	public Racket(){
		thread = new Thread(this);
		thread.start();
	}
	
	public void run(){
		while (isAlive){
			move(dir*(double)speed/1000);
			try{
				Thread.sleep(1);
			} catch(InterruptedException ie){
				System.err.println(ie);
			}
		}
	}
	
	public void startRunning(int dir){
		if (this.dir == -1*dir){
			this.reserveDir = dir;
		} else if(this.dir == 0){
			this.dir = dir;
		}
	}
	
	public void endRunning(int dir){
		if (this.dir == dir){
			this.dir = 0;
			if (this.reserveDir != 0){
				startRunning(this.reserveDir);
				this.reserveDir = 0;
			}			
		} else if (this.reserveDir == dir){
			this.reserveDir = 0;
		}
	}
	
	public void paintElement(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect((int)x-(width/2),(int)y-(height/2),width,height);
	}
	
	public void move(double x){
		if (!isAtWall(x))
			this.x += x;
	}
	
	private boolean isAtWall(double x){
		if (x > 0 && this.x >= (500-(width/2)))
			return true;
		if (x < 0 && this.x <= (width/2))
			return true;
		return false;
	}
	
	public int[] getPos(){
		int[] array = {(int)this.x,(int)this.y};
		return array;
	}
	
	public int[] getDims(){
		int[] dims = {this.width,this.height};
		return dims;
	}
	
	public Thread getThread(){
		return this.thread;
	}
	
	public void endRacket(){
		this.isAlive = false;
	}

	/**
	 * Multipliziert die Breite des Schlaeger mit dem Faktor d
	 * 
	 * @param d
	 * 			Bsp.: 0.75 (= 75%), 1.25 (125%)
	 */
	public void setWidth(double d) {
		if(this.width > widthMinMax[0] && this.width < widthMinMax[1]) {
			this.width *= d;
		}
	}
}
