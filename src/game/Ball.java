package game;

import java.awt.Color;
import java.awt.Graphics;

import main.GameElement;
import main.StartGame;

public class Ball implements GameElement, Runnable {

	private double direction;								// Richtung
	private double startDirection;							// Startrichtung
	private double[] position = new double [2];				// Position
	private double[] startPosition = new double [2];		// Startposition
	private int speed;										// Geschwindigkeit
	private int[] speedMinMax = {140, 300};
	private int radius;										// Radius
	private int[] radiusMinMax = {5, 12};
	private Color color;
	
	private int[] fieldDimension = StartGame.comp.getFieldDimension();
	private boolean isAlive = true;
	private long startTime;
	
	private Thread thread;
	
	private Object lastHitted;
	
	/**
	 * 
	 * Ball Konstruktor
	 * 
	 * @param position
	 * 			Position des Balls
	 * 
	 * @param direction
	 * 			Richtungswinkel
	 * 
	 * @param speed
	 * 			Geschwindigkeit des Balls
	 * 
	 * @param radius
	 * 			Radius des Balls
	 * 
	 * @param color
	 * 			Farbe des Balls
	 */
	public Ball(double[] position, double direction, int speed, int radius, Color color) {
		this.startPosition = position;
		this.startDirection = direction;
		this.speed = speed;
		this.radius = radius;
		this.color = color;
		
		thread = new Thread(this);
		thread.start();
		
		setStart();
		
	}
	
	/**
	 * 
	 * Ball Konstruktor mit den Standard-Einstellungen
	 * 
	 * @param position
	 * 			Position des Balls
	 * 
	 * @param direction
	 * 			Richtungswinkel
	 * 
	 */
	public Ball(double[] position, double direction) {
		this(position, direction, 200, 6, Color.white);
	}
	
	/**
	 * 
	 * Paint-Methode
	 * 
	 * @param g
	 * 			Graphics
	 * 
	 */
	public void paintElement(Graphics g){
		g.setColor(this.color);
		g.fillOval((int)position[0]-radius,(int)position[1]-radius,2*radius,2*radius);
	}
	
	
	/*
	 * 	Ueberarbeitungsidee: Die Game Klasse steuert
	 * das Spiel, der Ball wird jedesmal bei Verlust
	 * neu instanziert
	 */
	/**
	 * Setzt den Ball auf den Startpunkt
	 */
	public void setStart() {
		direction = getAngle(Math.cos(startDirection),Math.sin(startDirection));
		position[0] = startPosition[0];
		position[1] = startPosition[1];
	}
	
	/**
	 * Setzt die Position des Balls
	 * 
	 * @param dir
	 * 			Richtungswinkel
	 */
	public void setPosition(double dir){
		position[0] += Math.cos(dir);
		position[1] += Math.sin(dir);
	}
	
	/**
	 * Run-Methode
	 */
	public void run() {
		startTime = System.currentTimeMillis();
		long logStart;
		while(isAlive) {		
			if (System.currentTimeMillis()-startTime >= 1000/this.speed){
				logStart = System.nanoTime();
				startTime += 1000/this.speed;
				
				// Ueberpruefe: Kollidierung
				if (StartGame.game != null){
					// Mit Stein
					if (StartGame.game.bricks.size() > 0){
						Brick collidated = checkCollidating();
						if (collidated != null){
							collidated.hitted();
							setLastHitted(collidated);
						}
					}
					// Mit Schlaeger
					if (!StartGame.game.racket.equals(lastHitted))
						checkRacketHit();
				}
				
				setPosition(direction);
				
				// Ueberpruefe: Kollidierung mit Wand
				// links || rechts
				if( (position[0] < radius && Math.cos(this.direction) < 0) ||
					(position[0] > fieldDimension[0]-radius && Math.cos(this.direction) > 0)) {
					direction = getAngle(-1*Math.cos(direction),Math.sin(direction));
					setLastHitted(null);
				}
				// oben
				if(position[1] < radius && Math.sin(this.direction) < 0) {
					direction = getAngle(Math.cos(direction),-1*Math.sin(direction));
					setLastHitted(null);
				}
				// unten
				if(position[1] > fieldDimension[1]-radius && Math.sin(this.direction) > 0) {
					StartGame.game.ballLost();
				}				
				
				
				if ((System.nanoTime()-logStart)/1000 > 100)
					System.out.println("Berechnung(>100 mikros) abgeschlossen, benoetigte Zeit : "+((System.nanoTime()-logStart)/1000)+" mikros");
				try{
					int sleepTime = (1000/speed)-5;
					if (sleepTime < 0)
						sleepTime = 0;
					Thread.sleep(sleepTime);
				} catch (InterruptedException ie){
					System.err.println(ie);
				}
			}			
		}
	}
	
	/**
	 * 
	 * Gibt einen Winkel aus den beiden Richtungen x und y zurueck
	 * 
	 * @param double x
	 * 			x-Richtung
	 * 
	 * @param double y
	 * 			y-Richtung
	 * 
	 * @return double angle
	 * 			Winkel aus den beiden Richtungen
	 * 
	 */
	public static double getAngle(double x, double y){
		double angle = 0;
		if (x == 0)
			angle = Math.toRadians(90);
		else if (y == 0)
			angle = 0;
		else
			angle = Math.atan(Math.abs(y) / Math.abs(x));
		if (x < 0){
			if (Math.toDegrees(angle) <= 180)
				angle = Math.toRadians(180) - angle;
			else
				angle = Math.toRadians(540) - angle;
		}
		if (y < 0) {
			angle = Math.toRadians(360)-angle;
		}
		return angle;
	}
	
	/**
	 * 
	 * Ueberprueft, ob der Ball mit einem Stein kollidiert
	 * und liefert diesen zurueck, andernfalls null
	 * 
	 * @return Brick
	 * 			Stein
	 * 
	 */
	public Brick checkCollidating(){
		for (int i = 0; i < StartGame.game.bricks.size(); i++){
			
			Brick stone = StartGame.game.bricks.get(i);
			if (stone != lastHitted){
			
				/*
				 * 
				 * BRICK
				 * x1:		x-coordinate top left corner
				 * y1:		y-coordinate top left corner
				 * x2:		x-coordinate bottom right corner
				 * y2:		y-coordinate bottom right corner
				 * 
				 * xm:		x-coordinate central point
				 * ym:		y-coordinate central point
				 * 
				 * BALL
				 * xBall:	x-coordinate central point
				 * y-Ball:	y-coordinate central point
				 */
				int x1 = stone.getPosition()[0];
				int y1 = stone.getPosition()[1];
				int x2 = stone.getPosition()[2];
				int y2 = stone.getPosition()[3];
				
				int xm = stone.getPos()[0];
				int ym = stone.getPos()[1];
				
				int xBall = (int)this.position[0];
				int yBall = (int)this.position[1];
				
				/*
				 * Check collidating
				 * 		(1) bottom side
				 * 		(2) top side
				 */	
				if(((yBall - ym) >= 0 && ((yBall - ym) <= ((stone.getDims()[1]/2) + this.radius)) && xBall >= x1 && xBall <= x2 && Math.sin(this.direction) < 0) ||
				   ((ym - yBall) >= 0 && ((ym - yBall) <= ((stone.getDims()[1]/2) + this.radius)) && xBall >= x1 && xBall <= x2 && Math.sin(this.direction) > 0)) {
					this.direction = getAngle(Math.cos(direction),-1*Math.sin(direction));
					return stone;
				}
				
				/*
				 * Check collidating
				 * 		(1) right side
				 * 		(2) left side
				 */	
				if(((xBall - xm) >= 0 && ((xBall - xm) <= ((stone.getDims()[0]/2) + this.radius)) && yBall >= y1 && yBall <= y2 && Math.cos(this.direction) < 0) ||
				   ((xm - xBall) >= 0 && ((xm - xBall) <= ((stone.getDims()[0]/2) + this.radius)) && yBall >= y1 && yBall <= y2 && Math.cos(this.direction) > 0)) {
					this.direction = getAngle(-1*Math.cos(direction),Math.sin(direction));
					return stone;
				}
				
				/* 
				 * Check collidating
				 * 		(1) bottom right corner
				 * 		(2) bottom left corner
				 * 		(3) top right corner
				 * 		(4) top left corner
				 */
				if((xBall > x2 && yBall > y2 && Math.sqrt(Math.pow(xBall - x2, 2) + Math.pow(yBall - y2, 2)) <= this.radius) ||
				   (xBall < x2 && yBall > y2 && Math.sqrt(Math.pow(x1 - xBall, 2) + Math.pow(yBall - y2, 2)) <= this.radius) ||
				   (xBall > x2 && yBall < y2 && Math.sqrt(Math.pow(xBall - x2, 2) + Math.pow(y1 - yBall, 2)) <= this.radius) ||
				   (xBall < x2 && yBall < y2 && Math.sqrt(Math.pow(x1 - xBall, 2) + Math.pow(y1 - yBall, 2)) <= this.radius)) {
		
					double vx = x2 - xBall;
					double vy = y2 - yBall;
					double mx = 1;
					double my = -1*(vx/vy);
					double mirror = getAngle(mx,my);
					if (Math.abs(mirror-(direction%360)) > 180)
						mirror = -1*mirror;
					direction = mirror + (mirror - direction);
					return stone;
				}
			}
		}
		return null;
	}
	
	/**
	* 
	* Checks collidation of the ball with the racket
	* and sets the correct change of direction
	* 
	*/
	public void checkRacketHit(){
		Racket r = StartGame.game.racket; // Pointing the racket on r for easier usage
		int bending = 60; // sets the bending rate of the racket (50 - 150 recommended)
		/*
		 * Checks top side
		 */
		if (this.position[0] >= (r.getPos()[0]-(r.getDims()[0]/2)) && this.position[0] <= (r.getPos()[0]+(r.getDims()[0]/2)) && (r.getPos()[1]-(r.getDims()[1]/2)-this.position[1]) >= 0 && (r.getPos()[1]-(r.getDims()[1]/2)-this.position[1]) <= this.radius && Math.sin(this.direction) > 0){
			double vx = r.getPos()[0]-this.position[0];
			double vy = r.getPos()[1]+bending-this.position[1];
			double mx = 1;
			double my = -1*(vx/vy);
			double mirror = getAngle(mx,my);
			direction = mirror + (mirror - direction);
			if (Math.sin(direction) > 0)
				direction = getAngle(Math.cos(direction),1-1*Math.sin(direction));
			setLastHitted(r);
		}
		/*
		 * Checks left side
		 */
		if (this.position[1] >= (r.getPos()[1]-(r.getDims()[1]/2)) && this.position[1] <= (r.getPos()[1]+(r.getDims()[1]/2)) && (r.getPos()[0]-(r.getDims()[0]/2)-this.position[0]) >= 0 && (r.getPos()[0]-(r.getDims()[0]/2)-this.position[0]) <= this.radius){
			this.direction = getAngle(-1*Math.cos(this.direction),Math.sin(this.direction));
			setLastHitted(r);
		}
		/*
		 * Checks right side
		 */
		if (this.position[1] >= (r.getPos()[1]-(r.getDims()[1]/2)) && this.position[1] <= (r.getPos()[1]+(r.getDims()[1]/2)) && (this.position[0]-r.getPos()[0]-(r.getDims()[0]/2)) >= 0 && (this.position[0]-r.getPos()[0]-(r.getDims()[0]/2)) <= this.radius){
			this.direction = getAngle(-1*Math.cos(this.direction),Math.sin(this.direction));
			setLastHitted(r);
		}
		/*
		 * Checks corners
		 * (0) top left
		 * (1) top right
		 */
		if ((this.position[0] < (r.getPos()[0]-(r.getDims()[0]/2)) && this.position[1] < (r.getPos()[1]-(r.getDims()[1]/2)) && Math.sqrt(Math.pow(r.getPos()[0]-(r.getDims()[0]/2)-this.position[0],2)+Math.pow(r.getPos()[1]-(r.getDims()[1]/2)-this.position[1],2)) <= this.radius) ||
			(this.position[0] > (r.getPos()[0]+(r.getDims()[0]/2)) && this.position[1] < (r.getPos()[1]-(r.getDims()[1]/2)) && Math.sqrt(Math.pow(this.position[0]-(r.getPos()[0]+(r.getDims()[0]/2)),2)+Math.pow(r.getPos()[1]-(r.getDims()[1]/2)-this.position[1],2)) <= this.radius)){
			double vx = r.getPos()[0]-this.position[0];
			double vy = r.getPos()[1]+bending-this.position[1];
			double mx = 1;
			double my = -1*(vx/vy);
			double mirror = getAngle(mx,my);
			direction = mirror + (mirror - direction);
			setLastHitted(r);
		}
	}
	
	/**
	 * 
	 * @param o
	 */
	private void setLastHitted(Object o){
		this.lastHitted = o;
	}
	
	/**
	 * 
	 */
	public void setResume(){
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * 
	 */
	public void endBall(){
		this.isAlive = false;
	}

	/**
	 * 
	 * @return thread
	 */
	public Thread getThread(){
		return this.thread;
	}
	
	/**
	 * Multipliziert die Geschwindigkeit mit dem Faktor d
	 * 
	 * @param d
	 * 			Bsp.: 0.75 (= 75%), 1.25 (125%)
	 */
	public void setSpeed(double d) {
		if(this.speed > speedMinMax[0] && this.speed < speedMinMax[1]) {
			this.speed *= d;
		}
	}

	/**
	 * Multipliziert den Radius mit dem Faktor d
	 * 
	 * @param d
	 * 			Bsp.: 0.75 (= 75%), 1.25 (125%)
	 */
	public void setRadius(double d) {
		if(this.radius > radiusMinMax[0] && this.radius < radiusMinMax[1]) {
			this.radius *= d;
		}
	}
}
