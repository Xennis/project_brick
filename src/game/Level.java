package game;

import java.util.ArrayList;

import main.StartGame;


public class Level {

	private int[][] level;
	private int space;
	private int id;
	private int maxNeededHits = Brick.getNeededHitsMax();
	public static int amountOfLevels = 4;

	/**
	 * 
	 * Level-Konsturktor
	 * 
	 * @param number
	 * 			Nummer des Levels
	 */
	public Level(int number){
		this.id = number;
		switch(number){
			case 1: level1(); break;
			case 2: level2(); break;
			case 3: level3(); break;
			case 4: level4(); break;
		}
	}
	
    /**
     * Gibt eine Liste mit den Steinen für das jeweile Level entsprechend
     * des Level-Arrays zurueck
     * 
     * @param number
     * 			Nummer des Levels (hat bis jetzt noch keine Funktion)
     * 
     * @return bricks
     * 			Liste mit den Steinen fuer das Level
     */
    public ArrayList<Brick> getBricks() {
    	
    	ArrayList<Brick> bricks = new ArrayList<Brick>();
    	int fieldDimension = StartGame.comp.getFieldDimension()[0];
    	int[] brickDim = new Brick(0, 0, 0).getDims();
    	
    	
    	for(int i=0; i < level.length; i++) {
    		
    		int yBrick = space + i * (space + brickDim[1]);
    		int xBrick = (fieldDimension - brickDim[0] * level[i].length - space * (level[i].length -1)) / 2;
    		
    		for(int j=0; j < level[i].length; j++) {
    			
    			if(level[i][j] != 0) {
	    			
    				Brick brick;

    				if(level[i][j] >= -maxNeededHits && level[i][j] <= maxNeededHits)
    					brick = new Brick(xBrick, yBrick, level[i][j]);
    				else
    					brick = new Brick(xBrick, yBrick);
					bricks.add(brick);
    			}
    			xBrick += space + brickDim[0];
    		}
    	}
    	return bricks;
    }

    /**
     * 
     * Gibt die ID zurueck
     * 
     * @return id
     */
    public int getID(){
    	return this.id;
    }
    
    /*
     * LEVEL
     * 
     * Hier ist meine Idee für das Level-Design. Damit lassen sich Level erstellen,
     * die schon ganz in Ordnung sind.
     * 
     * Erklärung
     * 		0: 			kein Stein
     * 		1, 2, 3:	Stein mit der entsprechenden Stärke
     * 		-1, -2, -3:	Zufaelliger Spezialstein mit der entsprechenden Stärke
     * 		9:			Stein mit zufälliger Stärke
     * 		{}:			leere Zeile
     * 
     * Außerdem lässt sich über die Variable 'space' der Abstand zwischen zwei Steinen
     * einstellen, sodass dieser von Level zu Level variieren könnte.
     * 
     * Jede Zeile wird an der Mittelachse des Spielfeldes zentriert.
     * 
     * Das ganze müsste dann natürlich in eine neue Klasse.
     */
    
	/*
	 * Level 1
	 */
    public void level1() {
        int[][] l = {
            {3, 2, 1, 2, 3},
            {3, 1, 3},
            {3},
            {0},
            {1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1}
        };

        level = l;
        space = 14;

    }
 
    /*
     * Level 2
     */
    public void level2() {
        int[][] l = {
            {3, 2, 3, 0, 3, 2, 3},
            {2, 1, 1, 1, 1, 2},
            {2, 1, 1, 1, 2},
            {2, 1, 1, 2},
            {2, 2, 2},
            {},
            {},
            {3, 3, 3, 0, 3, 3, 3},
            {2, 2, 0, 0, 0, 2, 2},
            {1, 0, 0, 0, 0, 0, 1}
        };

        level = l;
        space = 10;

    }
    
    /*
     * Level 3
     */
    public void level3() {
        int[][] l = {
            {1, 1, 1},
            {2},
            {1,1}
        };

        level = l;
        space = 10;

    }
    
    /*
     * Level 4
     */
    public void level4() {
        int[][] l = {
            {-3},
            {-2},
            {-1},
            {-1},
            {-1},
            {-1}
        };

        level = l;
        space = 10;

    }
    
    
}