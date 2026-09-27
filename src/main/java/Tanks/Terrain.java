package Tanks;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;
import Tanks.Tank;
import Tanks.Tree;
public class Terrain extends PApplet{
    public char[][] terrainCells;
    public int gameState;
    public String layout;
    public PImage backgroundImage;
    public String foregroundColour;
    public PImage treeImage; 
    public HashMap<String, String> playerColours;
    public ArrayList<Tree> treeList = new ArrayList<Tree>();
    public ArrayList<Tank> tanklList = new ArrayList<Tank>();
    public HashMap<String, int[]> colourHashmap = new HashMap<String, int[]>();
    public  int drawtreeCounter = 0;
    public int playerRandomCounter = 0;
    public ArrayList<Integer> drawtreeRandomlist = new ArrayList<Integer>();
    public static final int CELLSIZE = 32; //8;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int horiXcell = 28;
    public static final int vertiYcell = 20;
    public PApplet App;
    public int[] columnToppixels = new int[CELLSIZE*horiXcell];
    public PImage parachuteImage;

    /**
     * Set the gamestate and the terrain with int[y-axis][x-axis]
     * @param gamestate the current game state
     * @param parachuteImage the parachute image    
     * @param App App itself
     */
    public Terrain(int gamestate, PImage parachuteImage ,PApplet App){
        this.gameState = gamestate;
        this.terrainCells = new char[BOARD_HEIGHT][BOARD_WIDTH+1];
        this.App = App;
        this.parachuteImage = parachuteImage;
    }

   /**
    * Set current level's layout
    * @param layoutlist layoutlist read from App
    */
    public void setLayout(ArrayList<String> layoutlist){
        layout = layoutlist.get(gameState);
    }
    /**
     * Set current level's foreground colour
     * @param foregroundlist foregroundlist read from App
     */
    public void setForegroundColour(ArrayList<String> foregroundlist){
        foregroundColour = foregroundlist.get(gameState);
    }


    /**
     * Set current level's background image
     * @param backgroundsiImages backgroundsiImages read from App
     * @return backgroundimage the background image of the current level
     */
    public PImage setBackground(PImage[] backgroundsiImages){
        backgroundImage = backgroundsiImages[gameState];
        return backgroundImage;
        
    }



    /**
     * Set current level's tree image
     * @param treelist ex."tree2.png"
     */
    public void setTrees(ArrayList<String> treelist){
        treeImage = App.loadImage("src/main/resources/Tanks/" + treelist.get(gameState));
        
    }


    /**
     * Set current level's player colours
     * @param player_colours ex."A":"0,0,255"
     */
    public void setPlayer(HashMap<String, String> player_colours){
        this.playerColours = player_colours;
    }



    /**
     * Set the 2-dimention array of terrain by reading the txt file.
     * The whole screen is divided into 28*20 cells.
     * Recognize all the elements in the txt file and store them in terrainCells.
     * Store the information of top pixels of each column in screen.
     * @param gamestate current game state
     */
    public void readtxt(int gamestate){
        String filename = layout;
        
        File layoutfile = new File(filename);
        try{
            Scanner scanlayput = new Scanner(layoutfile);
            
            for (int i = 0; i < vertiYcell && scanlayput.hasNextLine(); i++) {
                String line = scanlayput.nextLine();
                for (int j = 0; j < line.length(); j++) {
                    terrainCells[i][j] = line.charAt(j);
                    
                }
                if(line.length() != 28){
                    terrainCells[i][line.length()] = ' ';
                }
                
            }
            scanlayput.close();
            for (int j = 0; j < horiXcell; j++){
                int startToFill = -1;
                for (int i = 0; i < vertiYcell; i++){
                    if(terrainCells[i][j] == 'X'){
                        startToFill = i;
                        break;
                    }else if (terrainCells[i][j] == 'T'){
                        terrainCells[i][j] = 'T';
                    }else if ((terrainCells[i][j] >= 'A' && terrainCells[i][j] <= 'I')
                          || (terrainCells[i][j] >= '0' && terrainCells[i][j] <= '9')){
                            terrainCells[i][j] = terrainCells[i][j];
                          }
                    
                    
                }
                for(int k = startToFill + 1; k < vertiYcell; k++ ){
                    terrainCells[k][j] = 'X';
                }
            }

        }catch(FileNotFoundException e){
            System.out.println("layout file not found");
        }
       

    
        
    }


    /**
     * Print out terrainCells in terminal(only for testing)
     */
    public void testTerrainCells(){
        System.out.println("--------------------");
        for (int i = 0; i < vertiYcell; i++) {
            for (int j = 0; j < horiXcell; j++) {
                System.out.print(terrainCells[i][j] + " ");
            }
            System.out.println(); 
        }
        System.out.println("--------------------");
    }



    /**
     * Convert and seperate RGB from string to int[]
     * @param original RGB's string form "0,0,255"
     * @return int[] RGB's array form {0,0,255}
     */
    public int[] StringtoNumber(String original){
        String[] colors = original.split(",");
        int r = Integer.parseInt(colors[0]);
        int g = Integer.parseInt(colors[1]);
        int b = Integer.parseInt(colors[2]);
        return new int[] {r, g, b};

    }

    /**
     * Draw the terrain by top pixels of each column
     * Draw the terrain with coresponing RGB colour
     * @param rgb RGB colour in integer array form 
     */
    public void drawTerrain(int[] rgb) {
        
        for (int i = 0; i < columnToppixels.length; i++) {
            int startpixel = columnToppixels[i];

            for (int j = startpixel; j < HEIGHT; j++) {
                App.fill(rgb[0],rgb[1],rgb[2]);
                App.rect(i, j, 1, 1);

                
            }
        }
    }

    /**
     * Recognize top pixels of initial terrain which hasn't been smoothed in txt file
     */
    public void initialToppixels(){


        for(int j = 0; j < horiXcell; j++){
            for(int i = 0; i < vertiYcell; i++){
                if(terrainCells[i][j] == 'X'){
                    for(int k = j*32 ; k < (j+1)*32 ; k++){
                        columnToppixels[k] = i*CELLSIZE;
                    }
                    break;
                }
            }
        }
    }

    /**
     * Average move process, change the columnToppixels arrays
     */
    public void moveAverge(){
        int[] resultTop = new int[CELLSIZE*horiXcell];
        
        for(int i = 0; i < WIDTH; i++){

            float sum = 0;
            float average;
            for (int j = i+1; j <= i + CELLSIZE; j++ ){
                sum += (HEIGHT - columnToppixels[j]);

            }
            average = sum/CELLSIZE;
            resultTop[i] = round(HEIGHT - average);
            

        }
        for(int i = 0; i < WIDTH; i++){
            columnToppixels[i] = resultTop[i];
        }
        
    }

    /**
     * Set trees and store them into treelist follow the txt file
     * @return treeList the list of tree objects
     */
    public ArrayList<Tree> setTreelist(){
        // treeList.clear();
        for(int j = 0; j < horiXcell; j++){
            for(int i = 0; i < vertiYcell; i++){
                if (terrainCells[i][j] == 'T'){
                    Tree tree = new Tree(j*CELLSIZE, treeImage, App);
                    treeList.add(tree);
                }
            }
        }
        return treeList;
    }

    /**
     * Set tanks information including colour, position, name, and store them in the tanklist
     * @return tanklList the list of tanks
     */
    public ArrayList<Tank>  setTanklist(){
        
        
        for(int j = 0; j < horiXcell; j++){
            for(int i = 0; i < vertiYcell; i++){
                String name = Character.toString(terrainCells[i][j]);
                int xpos = j*CELLSIZE;
                int ypos = columnToppixels[xpos];
                
                if ((terrainCells[i][j] >= 'A' && terrainCells[i][j] <= 'E') 
                || (terrainCells[i][j] >= '0' && terrainCells[i][j] <= '9')){
                    String color = playerColours.get(Character.toString(terrainCells[i][j]));
                    int[] playerRGB = StringtoNumber(color);
                    
                    Tank tank = new Tank(name, playerRGB, xpos, ypos, parachuteImage, App);
                    tanklList.add(tank);

                } else if (terrainCells[i][j] >= 'F' && terrainCells[i][j] <= 'I'){
                    int[] rgb = new int[3];
                    if (playerRandomCounter == 0){
                        for (int k = 0; k < 3; k++) {
                            rgb[k] = (int)random(256);
                            colourHashmap.put(name, rgb);
                        } 
                    }
                    
                    int[] playerRGB = colourHashmap.get(name);
                    Tank tank = new Tank(name, playerRGB, xpos,ypos, parachuteImage,App);
                    tanklList.add(tank);
                    

                    
                }
            }
        }

        return tanklList;
    }


    /**
     * Sorts the tank list in ascending order based on the ASCII values of tank names with bubble sort algorithm.
     */
    public void bubbleSortTankListByAscii() {
        int n = tanklList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String name1 = tanklList.get(j).name;
                String name2 = tanklList.get(j + 1).name;
    
                if (name1.compareTo(name2) > 0) {
                    Tank tank = tanklList.get(j);
                    tanklList.set(j, tanklList.get(j + 1));
                    tanklList.set(j + 1, tank);
                }
            }
        }
    }



    /**
     * The initial position of trees is randomised up to 30 pixels around its starting point. 
     */
    public void randomTrees(){
        int i = 0;
        while (true){
            if (i >= treeList.size()){
                break;
            }
            int delta = (int)random(-30,30.2f);
            if (treeList.get(i).xpos + delta < 0 || treeList.get(i).xpos + delta > WIDTH){
                continue;
            }
            i++;
            drawtreeRandomlist.add(delta);
        }
            // randomdelta = (int)random(-30,31);

    }
    
    /**
     * Draws the trees on the terrain.
     */
    public void drawTrees(){
        // int randomdelta;

        
        // drawtreeCounter++;
        for(int i = 0; i < treeList.size(); i++){
            int randomdelta = drawtreeRandomlist.get(i);
            treeList.get(i).setXrandom(randomdelta);
            int ypos = columnToppixels[treeList.get(i).getXpos() + randomdelta];
            treeList.get(i).updateY(ypos);
            treeList.get(i).drawTree();
        }
    }


    /**
     * Update the status of the tank when moving or dropping.
     * Update the tank's positon and  parachute status
     * Update other tanks' score when the tank is dropping
     */
    public void tankyposUpdated(){
        for(Tank tank: tanklList){
            if (tank.drop == false){
                tank.ypos = columnToppixels[tank.xpos];  
                tank.causeDrop = '\0';
                tank.causeDropWithParachute = '\0';
               
            }else{
                if (tank.ypos >= columnToppixels[tank.xpos]){
                    if (tank.parachute > 0 && tanklList.size() != 1){
                        tank.parachute -=1; 
                    }
                    tank.ypos = columnToppixels[tank.xpos];
                    tank.drop = false;
                    tank.causeDrop = '\0';
                    tank.causeDropWithParachute = '\0';
                    
                    
                }
            }
            
            
        }
    }


    /**
     * Draws the tanks on the terrain.
     */
    public void drawTanks(){
        for(int i = 0 ; i < tanklList.size(); i++){
            tanklList.get(i).drawTank();
            if (!(tanklList.size() == 1)){
                tanklList.get(i).dropDamage();
            }
            
        }
    }

    /**
     * Updates the tank's drop status.
     */
    public void setDrop(){
        for(Tank tank: tanklList){
            if(tank.ypos < columnToppixels[tank.xpos]){
                tank.drop = true;
            }
        }
    }


}
