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
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import Tanks.Terrain;
import Tanks.Tank;


public class App extends PApplet {
    
    
    
    public static final int START_PAGE = 0;
    public int gameState = START_PAGE;
    
    
    public JSONArray levels;
    public JSONObject config;
    public JSONObject playerColors;
    public ArrayList<String> layoutList = new ArrayList<>();
    public ArrayList<String> backgroundList = new ArrayList<>();
    public ArrayList<String> foregroundList = new ArrayList<>();
    public ArrayList<String> treeList = new ArrayList<>();
    public ArrayList<Projectile> projectileList = new ArrayList<>();
    public ArrayList<int[]> explosionList = new ArrayList<>();
    public HashMap<String, Integer> parachuteList = new HashMap<>();
    public HashMap<Character, Float> scoreboardList = new HashMap<>();
    public PImage[] backgroundsiImages = new PImage[3];
    public PImage[] treesImages = new PImage[3];
    public HashMap<String, String> player_colours = new HashMap<>();
    public PImage fueliImage;
    public PImage parachuteImageLegend;
    public PImage parachuteImageOriginal;
    public PImage windRightiImage;
    public PImage windLeftiImage;

    public File backgroundMusicFile;
    public File successMusicFile;
    public Clip backgroundClip;
    public Clip successClip;
    public int firstSetup = 0;
    public int round = 0;
    public int startTime = 0;

    public int delay = 0;
    public int levelSize;
    public String configPath;
    public int wind;
    


    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;
    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;
    public static final int horiXCell = 28;
    public static final int vertiYCell = 20;
    public static final int INITIAL_PARACHUTES = 1;
    public static final int FPS = 30;
    public static Random random = new Random();
    Terrain terrain;
    public ArrayList<Tank> trtanklList = new ArrayList<Tank>();
    public ArrayList<Tree> trtreeList = new ArrayList<Tree>();

    
    public App() {
        
        
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        loadSources();
        frameRate(FPS);

    }
    
    

    /**
     * Load all resources such as images and store them in the arraylist or hashmap
     */
    /**
     * Loads the game sources from the configuration file.
     * Reads the levels, player colors, and other resources required for the game.
     */
    public void loadSources(){
        configPath = "config.json";
        wind = (int)random(-35, 36f);
        config = loadJSONObject(configPath);
   
        
        levels = config.getJSONArray("levels");
        playerColors = config.getJSONObject("player_colours");
        levelSize = levels.size();
        for(int i = 0; i < levels.size(); i++){
            JSONObject level = levels.getJSONObject(i);
            String layout = level.getString("layout");
            String background = level.getString("background");
            String foregroundColour = level.getString("foreground-colour");
            String trees = level.getString("trees");
            
            layoutList.add(layout);
            backgroundList.add(background);
            foregroundList.add(foregroundColour);
            if (trees == null){
                trees = "tree1.png";
            }
            treeList.add(trees);
            backgroundsiImages[i] = loadImage("src/main/resources/Tanks/" + background);
            // treesImages[i] = loadImage("src/main/resources/Tanks/" + trees);
        }

        for(Object key: playerColors.keys()){
            String keyStr = (String) key;
            String value = playerColors.getString(keyStr);
            player_colours.put(keyStr, value);
        }

        fueliImage = loadImage("src/main/resources/Tanks/fuel.png");
        parachuteImageOriginal = loadImage("src/main/resources/Tanks/parachute.png");
        parachuteImageLegend = parachuteImageOriginal.copy();
        float scalefuel = 0.08f;
        float scalep = 0.4f;
        fueliImage.resize((int)(fueliImage.width*scalefuel), (int)(fueliImage.height*scalefuel));
        parachuteImageLegend.resize((int)(parachuteImageLegend.width*scalep), (int)(parachuteImageLegend.height*scalep));
        

        windRightiImage = loadImage("src/main/resources/Tanks/wind.png");
        windLeftiImage = loadImage("src/main/resources/Tanks/wind-1.png");
        float xscale = 0.7f;
        float yscale = 0.65f;
        windRightiImage.resize((int)(windRightiImage.width*xscale), (int)(windRightiImage.height*yscale));
        windLeftiImage.resize((int)(windLeftiImage.width*xscale), (int)(windLeftiImage.height*yscale));
        try{
            backgroundMusicFile = new File("src/main/resources/Tanks/Objection.wav");
            AudioInputStream backgroundAudioInputStream = AudioSystem.getAudioInputStream(backgroundMusicFile);
            successMusicFile = new File("src/main/resources/Tanks/success.wav");
            AudioInputStream successAudioInputStream = AudioSystem.getAudioInputStream(successMusicFile);
            backgroundClip = AudioSystem.getClip();
            successClip = AudioSystem.getClip();
            successClip.open(successAudioInputStream);
            backgroundClip.open(backgroundAudioInputStream);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){

        if (key == 'q') {
            if(terrain.tanklList.get(0).drop){
                if(terrain.tanklList.get(0).parachute>0){
                    parachuteList.put(terrain.tanklList.get(0).name, terrain.tanklList.get(0).parachute-1);
                }
                
            }
            gameState = (gameState + 1)%levelSize;
            firstSetup = 0;
            delay = 0;


        }
        if (key == ' '){
            Tank tank = terrain.tanklList.get(round);
            startTime = millis();
            projectileList.add(new Projectile(tank.name.charAt(0), tank.xpos, tank.ypos, tank.power/100*16+2, tank.angle, tank.playerRGB, this));
            // terrainn.tanklList.get(round).projectile.drawprojectile();
            round = (round + 1)%(terrain.tanklList.size());
            changeWind();
        }

        //
        if (key == 'r'){
            
            if (gameOver() && gameState == levelSize){
                gameState = 0;
                firstSetup = 0;
                delay = 0;
                successClip.stop();
            }else{
                Tank tank = terrain.tanklList.get(round);
                if (scoreboardList.get(tank.name.charAt(0)) >=20){
                    if(tank.hp + 20 >= 100){
                        tank.hp = 100;
                    }else{
                        tank.hp +=20;
                    }
                    scoreboardList.put(tank.name.charAt(0), scoreboardList.get(tank.name.charAt(0))-20);    
                }
            }
            
        }
        if (key == 'f'){
            Tank tank = terrain.tanklList.get(round);
            if (scoreboardList.get(tank.name.charAt(0)) >=10){
                tank.fuel +=200;
                scoreboardList.put(tank.name.charAt(0), scoreboardList.get(tank.name.charAt(0))-10);
            }
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
    }

    /**
     * Receive mouse pressed signal from the mouse.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport


    }

    /**
     * Receive mouse released signal from the mouse.
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    /**
     * draw backgrounds
     * @param gamestate the current game state
     */
    public void initialDraw(int gamestate){
        image(backgroundsiImages[gamestate], 0, 0);
        
    }



    

    
    
    /**
     * Checks if the game is over.
     * If there is only one tank left, the game is over.    
     *
     * @return true if the game is over, false otherwise
     */
    private boolean gameOver(){
        if(terrain.tanklList.size() == 1){
            
            return true;
        }else{   
            return false;
        }
        
    }

    
    
    /**
     * Displays the equipment information for the current player's tank.
     */
    public void tankEquip(){
        fill(0);
        textSize(16);
        
        String equip = "Player " + terrain.tanklList.get(round).name + "'s turn";
        text(equip, 20, 30);
        
        image(fueliImage, 160, 30 - fueliImage.height+5);
        image(parachuteImageLegend, 160, 40);
        text(terrain.tanklList.get(round).fuel, 190, 30);
        text(terrain.tanklList.get(round).parachute, 190, 60);
        
    }

    
    /**
     * Changes the wind direction by generating a random value between -5 and 5 (inclusive)
     * and adding it to the current wind value.
     */
    public void changeWind(){
        
        int delta = (int)random(-5,6f);
        wind += delta;

    }

    /**
     * draw graphics of wind
     */
    /**
     * Draws the wind indicator on the screen.
     * The wind indicator points the direction of the wind.
     * The wind value is displayed as text next to the wind indicator image.
     */
    public void drawWind(){
        if (wind < 0) {
            image(windLeftiImage,WIDTH - 50 - windLeftiImage.width , 0);
        }else{
            image(windRightiImage, WIDTH - 50 - windLeftiImage.width, 0);
        }
        fill(0);
        textSize(20);   
        text(wind, WIDTH - 40, windLeftiImage.height-15);
        
    }

    /**
     * Sets the parachute for each tank in the tank list.
     * This function is used to pass the parachute value from last level to the next level.
     */
    public void setParachute(){
        for(int i = 0; i < terrain.tanklList.size(); i++){
            terrain.tanklList.get(i).parachute = parachuteList.get(terrain.tanklList.get(i).name);
        }
    }

    /**
     * draw all projectiles
     */
    public void drawProjectiles(){
        for (Projectile projectile:projectileList){
            projectile.drawprojectile(wind);
        }
    }


    
    /**
     * Removes projectiles that are out of bounds from the projectile list.
     */
    public void cleanProjectiles(){
        for(int i = 0; i < projectileList.size(); i++){
            if(projectileList.get(i).ypos > HEIGHT + 10
            || projectileList.get(i).getXpos() <= 0 
            || projectileList.get(i).getXpos() >= WIDTH){
                projectileList.remove(i);
                
            }

           
            
        }
    }

    /**
     * This function is used to remove trees that have fallen off the screen.
     */
    public void cleanTreelist(){
        for(int i = 0; i < terrain.treeList.size(); i++){
            if(terrain.treeList.get(i).ypos > 640){
                terrain.treeList.remove(i);
                terrain.drawtreeRandomlist.remove(i);
                
            }
        }
    }
    
    /**
     * Store the positon of the projectile that hit the terrain in the explosion list.
     * Remove the projectile from the projectile list after it hits the terrain.
     */
    public void explosion(){
        int defaultradius = 30;
        
        for(int i = 0; i < projectileList.size(); i++){
            Projectile singleprojectile = projectileList.get(i);
            char name = singleprojectile.name;
            if (singleprojectile.getXpos() >=0 && singleprojectile.getXpos() <= WIDTH){
                
                if (singleprojectile.getYpos() >= terrain.columnToppixels[singleprojectile.getXpos()]){
                    int startime = millis();
                    int[] explo = {startime, singleprojectile.getXpos(), singleprojectile.getYpos(), defaultradius, (int)name};
                    explosionList.add(explo);
                    projectileList.remove(i);
                }
            }
        }
        
        
    }

    
    /**
     * Draws all the explosion events in the explosionList.
     * The explosion is drwan as a concentric circle with red, orange, yellow color.
     * After the explosion is drawn, the terrain is destroyed and the tanks are damaged.
     */
    public void drawAllExplosion(){
        for(int i = 0; i < explosionList.size(); i++){
            int[] data = explosionList.get(i);
            
            if (countTime((int)data[0], 0.2f)){    
                
                float percentage = countTimepercentage((int)data[0], 0.2f);
                
                
                drawExplosion(data, data[3], percentage);

            }else{
                
                destroyTerrain(round(data[1]), round(data[2]), data[3]);
                if (!(terrain.tanklList.size() == 1)){// if there is only one tank left, no need to check the tank's hp
                    explosionDamage(round(data[1]), round(data[2]), data[3], data[4]);
                }
                
                explosionList.remove(i);
            }
            
            
        }
    }

    
    /**
     * Destroys the terrain at all the locations specified in the explosion list.
     */
    public void destroy(){
        for (int i = 0; i < explosionList.size(); i++ ){
            int[] data = explosionList.get(i);
            destroyTerrain(round(data[1]), round(data[2]), 30);
            explosionList.remove(i);
        }
        
    }



    /**
     * Destroys the terrain within a specified explosion radius around the given impact point.
     * 
     * @param impactX The x-coordinate of the impact point.
     * @param impactY The y-coordinate of the impact point.
     * @param explosionRadius The radius of the explosion.
     */
    public void destroyTerrain(int impactX, int impactY, int explosionRadius){
        for (int x = impactX - explosionRadius; x < impactX + explosionRadius; x++){
            
            int deltaY = 2 * round((float)Math.sqrt(explosionRadius * explosionRadius - (x - impactX) * (x - impactX)));
            int ydown = impactY + round((float)Math.sqrt(explosionRadius * explosionRadius - (x - impactX) * (x - impactX)));
            int yup = impactY - round((float)Math.sqrt(explosionRadius * explosionRadius - (x - impactX) * (x - impactX)));
            
            
            if (x >= 0 && x <= terrain.columnToppixels.length){
                
                if (terrain.columnToppixels[x] < yup){
                    terrain.columnToppixels[x] += deltaY;
                    
                }else if (terrain.columnToppixels[x] > ydown){
                    
                }else{
                    terrain.columnToppixels[x] = ydown;
                }
            }
            
            
        }
    }

    
    /**
     * draw signle explosion event
     * @param data information of explosion including start time, x position, y position, radius, and name
     * @param defaultradius the default radius of the explosion
     * @param percentage the percentage of time passed, which will determine the size of the explosion
     */
    public void drawExplosion(int[] data, int defaultradius, float percentage){
        fill(255, 0, 0);
        ellipse(data[1], data[2], defaultradius*percentage*2, defaultradius*percentage*2);
        fill(255, 165, 0);
        ellipse(data[1], data[2], defaultradius*0.5f*percentage*2, defaultradius*0.5f*percentage*2);
        fill(255, 255, 0);
        ellipse(data[1], data[2], defaultradius*0.2f*percentage*2, defaultradius*0.2f*percentage*2);
    }



    /**
     * Applies explosion damage to tanks within a specified radius from a given position.
     * The damage inflicted on each tank is calculated in a linear way based on its distance from the explosion center .
     * The projectile that caused the explosion or tank's drop will be stored in the tank's causedrop or causedropwithparachute.
     *
     * @param xpos The x-coordinate of the explosion center.
     * @param ypos The y-coordinate of the explosion center.
     * @param defaultradius The default radius of the explosion.
     * @param projectilenameASCII The ASCII value of the projectile name.
     */
    public void explosionDamage(int xpos, int ypos, int defaultradius, int projectilenameASCII){
        int maxDamage = 60;
        int minDamage = 0;
        char projectilename = (char)projectilenameASCII;
        for(int i = 0; i < terrain.tanklList.size(); i++){
            Tank tank = terrain.tanklList.get(i);
            int x = tank.xpos;
            int y = tank.ypos;
            float distance = (float)Math.sqrt((x - xpos)*(x - xpos) + (y - ypos)*(y - ypos));
            if (distance <= defaultradius){
               
                float damage = maxDamage - (distance/defaultradius * (maxDamage - minDamage));
                if(tank.hp < damage){
                    damage = tank.hp;
                    tank.hp = 0;
                    
                }else{
                   tank.hp -= damage; 
                }
                
                // System.out.println("damage on :" + tank.name + " is " + damage);
                // System.out.println(projectilename + "       " + tank.name.charAt(0));
                float currentScore = scoreboardList.get(projectilename);
                if(tank.name.charAt(0) == projectilename){
                    scoreboardList.put((projectilename), currentScore);
                }else{
                    

                    scoreboardList.put((projectilename), currentScore + damage);  
                    
                }
                
            }
            
            if (x >= xpos - defaultradius && x <= xpos + defaultradius 
                && tank.drop == false && projectilenameASCII != tank.name.charAt(0)) {
                if (tank.parachute == 0) {
                    tank.causeDrop = projectilename;
                } else if (tank.parachute > 0) {
                    tank.causeDropWithParachute = projectilename;
                }
            }

            
        }
    }

    /**
     * Checks if the tank has fallen off the screen or has been destroyed.
     * Tanks fallen off the screen will explode in a radius of 30 pixels.
     * Tanks that have been destroyed will explode in a radius of 15 pixels.
     * Both types of explosions will be stored in the explosion list and cause damage to the terrain as well as other tanks.
     * If tank is destroyed when dropping, if the parachute is greater than 0, the tank's parachute will decrease by 1.
     * Once tank is destroyed, the tank will be removed from the tank list.
     * Explosion information is stored in the explosion list in the type of array {starttime, xpos, ypos, radius, name}.
     */
    public void checkTanklist(){
        
        for(int i = 0; i < terrain.tanklList.size(); i++){
            
            Tank tank = terrain.tanklList.get(i);
            int xpos = tank.xpos;
            int ypos = tank.ypos;
            String name = tank.name;
            
            parachuteList.put(name, tank.parachute);
            if (round(tank.hp) <= 0|| ypos > 640){
                if(tank.drop && tank.parachute > 0){
                    tank.parachute -=1;
                    parachuteList.put(name, tank.parachute);
                }
                
                startTime = millis();
                String roundname = terrain.tanklList.get(round).name;
                
                
                
                if (terrain.tanklList.size() == 1){
                    round = 0;
                    return;
                }else{
                    terrain.tanklList.remove(i);
                    if (name.compareTo(roundname) < 0){
                        round = (round - 1 )%terrain.tanklList.size();
                    }else if (name.compareTo(roundname) == 0){
                        round = (round )%(terrain.tanklList.size());
                    }
                }
                
                if (ypos > 640) {
                    explosionList.add(new int[]{startTime, xpos, ypos, 30, name.charAt(0)});
                }else{
                    explosionList.add(new int[]{startTime, xpos, ypos, 15, name.charAt(0)});
                    
                }

                
                
                
                
            }

            
            
        }

    }


    /**
     * Checks if the specified amount of time has passed since the given start time.
     * 
     * @param startime the start time in milliseconds
     * @param n the duration in seconds
     * @return true if the specified time has not passed yet, false otherwise
     */
    public boolean countTime(int startime, float n){
        if (millis() - startime > n*1000 ){
            return false;
        }else{
            return true;
        }
    }

    
    /**
     * Calculates the percentage of time elapsed since the given start time.
     * 
     * @param startime the start time in milliseconds
     * @param n the total duration in seconds
     * @return the percentage of time elapsed
     */
    public float countTimepercentage(int startime, float n){
        return (millis() - startime) / (n*1000);

    }
    /**
     * Initialises the parachute list with 3 parachutes 
     */
    public void initialParachutelist(){
        for(int i = 0; i < terrain.tanklList.size(); i++){
            parachuteList.put(terrain.tanklList.get(i).name, 3);
        }
    }


    /**
     * Initialises the scoreboard list with same number of tanks in the tank list.
     * Each tank's score is 0;
     */
    public void initialScoreboard(){
        for(int i = 0; i < trtanklList.size(); i++){
            scoreboardList.put(trtanklList.get(i).name.charAt(0), 0f);
        }
            
    }

    /**
     * Draws the scoreboard on the right top corner on the screen.
     * @param people the number of total palyers
     */
    public void drawScoreboard(int people){
        textAlign(BOTTOM, LEFT);
        fill(0);
        stroke(0);
        strokeWeight(2);
        noFill();
        //draw the scoreboard on the top right corner
        rect(WIDTH-150, 50, 140, 20 + 20*people+10);
        rect(WIDTH - 150, 70, 140, 2);
        text("Scores", WIDTH-140, 67);
        //draw scoreboard witch is a concentric rectangle 
        int displaypeople = 0;
        for(int i = 0; i < trtanklList.size(); i++){
            
            Tank tank = trtanklList.get(i);
            char name = tank.name.charAt(0);
            float score = scoreboardList.get(name);
            int[] rgb = trtanklList.get(i).playerRGB;
            fill(rgb[0], rgb[1], rgb[2]);
            textSize(16);
            text("Player " + name , WIDTH-140, 90 + displaypeople*20);
            
            textAlign(CENTER, LEFT);
            text(round(score), 820 , displaypeople*20 + 90);
            textAlign(BOTTOM, LEFT);
            displaypeople++;
        }
        
    }

    /**
     * If tank's hp is decreased because of dropping, then the function will check whether other tank's projectile caused the drop.
     * If ture, then the tank's score will be updated.
     * If the tank drops off the screen, then update the other tank score who causes the drop with the rest of the dropping tank's hp.
     */
    public void getDropscore(){
        for(int i = 0; i <terrain.tanklList.size(); i++){
            Tank tank = terrain.tanklList.get(i);
            if(tank.causeDrop != '\0' ){
                float currentScore = scoreboardList.get(tank.causeDrop);
                if (tank.ypos > 640 && terrain.tanklList.size() != 1){
                    scoreboardList.put(tank.causeDrop, tank.hp + currentScore);
                }
                else if (terrain.tanklList.size() != 1){
                    scoreboardList.put(tank.causeDrop, currentScore + 4);
                }                                 
                
            }
            if(tank.causeDropWithParachute != '\0'){
                float currentScore = scoreboardList.get(tank.causeDropWithParachute);
                if (tank.ypos >  640 && terrain.tanklList.size() != 1){
                    scoreboardList.put(tank.causeDropWithParachute, tank.hp + currentScore);
                }
                
            }
        }    
    }
    

    
/**
 * This method is responsible for drawing the game elements on the screen.
 * It is called repeatedly each frame.
 * It is responsible for handling user's input and updating the game state.
 * 
 */
	@Override
    public void draw() {
        
        if(gameState < levelSize){

            
            this.noStroke();
            
            // load the game resources in every level
            if(firstSetup == 0){
                round = 0;
                projectileList.clear();
                explosionList.clear();
                terrain = new Terrain(gameState, parachuteImageOriginal, this);

                terrain.setForegroundColour(foregroundList);
                
                terrain.setBackground(backgroundsiImages);
                
                terrain.setTrees(treeList);
                terrain.setPlayer(player_colours);
                terrain.setLayout(layoutList);
                terrain.readtxt(gameState);
                terrain.initialToppixels();
                terrain.treeList = terrain.setTreelist();
                terrain.randomTrees();
                terrain.setTanklist();
                terrain.bubbleSortTankListByAscii();


                Terrain terraincopy = new Terrain(gameState, parachuteImageOriginal, this);
                // terraincopy.setForegroundColour(foregroundlist);
                // terraincopy.setBackground(backgroundsiImages);
                // terraincopy.setTrees(treelist);
                terraincopy.setPlayer(player_colours);
                terraincopy.setLayout(layoutList);
                terraincopy.readtxt(gameState);
                // terraincopy.initialToppixels();
                // terraincopy.treeList = terraincopy.setTreelist();
                // terraincopy.randomTrees();
                trtanklList = terraincopy.setTanklist();
                terraincopy.bubbleSortTankListByAscii();
                wind = (int)random(-35, 36f);

                for(int i = 0; i < 2; i++){
                    terrain.moveAverge();
                
                }
                firstSetup++;
                
                if(gameState == 0){
                    initialParachutelist();
                    initialScoreboard();
                    for(Tank tank: terrain.tanklList){
                        scoreboardList.put(tank.name.charAt(0), 0f);
                    }

                }
                setParachute();
                terrain.tankyposUpdated();
            }
            
            
            initialDraw(gameState);
            terrain.drawTerrain(terrain.StringtoNumber(terrain.foregroundColour));
            terrain.setDrop();
            // terrainn.tankyposUpdated();
            
            tankEquip();
            terrain.tanklList.get(round).drawPowerHp();
            drawWind();    
            
            drawScoreboard(scoreboardList.size());
                  
            // if(round == 1){
            //     terrainn.tanklList.get(0).hp = 0;
            // }
            
            if (keyPressed) {
                if(terrain.tanklList.get(round).drop == false){
                    if(keyCode == RIGHT){
                    
                        if (terrain.tanklList.get(round).xpos >= WIDTH 
                        ||  terrain.tanklList.get(round).fuel <= 0){
                            // pass
                        }else{
                            terrain.tanklList.get(round).xpos +=2;
                            terrain.tanklList.get(round).fuel -=2;
                            
                        }
                    }else if (keyCode == LEFT){
                        if(terrain.tanklList.get(round).xpos <= 0 
                        || terrain.tanklList.get(round).fuel <= 0){
                            // pass
                        }else{
                            terrain.tanklList.get(round).xpos -=2;
                            terrain.tanklList.get(round).fuel -=2;
                        }
                        
                    }
                
                    if (keyCode == UP){
                        if (terrain.tanklList.get(round).angle +0.1f >= PI) {
                            terrain.tanklList.get(round).angle = PI;
                        }else{
                            terrain.tanklList.get(round).rorateAngle(0.1f);
                        }
                        
                    }else if (keyCode == DOWN){
                        if (terrain.tanklList.get(round).angle - 0.1f <= 0) {
                            terrain.tanklList.get(round).angle = 0;
                        }else{
                            terrain.tanklList.get(round).rorateAngle(-0.1f);
                        }
                        
                    }

                    if (key == 'w'){
                        if (terrain.tanklList.get(round).power + 1.2 >=terrain.tanklList.get(round).hp) {
                            terrain.tanklList.get(round).power = terrain.tanklList.get(round).hp;
                        }else{
                            terrain.tanklList.get(round).power += 1.2;
                        }
                        
                    }else if (key == 's'){
                        if (terrain.tanklList.get(round).power - 1.2 <= 0 ) {
                            terrain.tanklList.get(round).power = 0;
                        }else{
                            terrain.tanklList.get(round).power -= 1.2;
                        }
                        
                    }

                    

                    
                    
                }
                
            }
            terrain.tankyposUpdated();
            
            terrain.drawTrees();
            terrain.drawTanks();
            getDropscore(); 
            if(countTime(startTime, 2)){
                terrain.tanklList.get(round).drawArrow();// wait for 2 seconds
                
            }
            drawProjectiles();
            // getparachutelist();
            
            
            
            // cleanprojectiles();
            
            checkTanklist();
            explosion();
            
            drawAllExplosion();
            
            cleanProjectiles();
            cleanTreelist();
            
            
            

            
            
            
            

        

            if (gameOver()) {
                backgroundClip.stop();
                if (gameState < levelSize ){  
                    if (delay == 0) {
                        delay = millis();
                        
                    } else if (millis() - delay > 1000) {
                        if(terrain.tanklList.get(0).drop){
                            if(terrain.tanklList.get(0).parachute>0){
                                parachuteList.put(terrain.tanklList.get(0).name, terrain.tanklList.get(0).parachute-1);
                            }
                            
                        }
                        
                        gameState++;
                        firstSetup = 0;
                        delay = 0;
                        backgroundClip.setFramePosition(0);
                        
                        
                    }
                }
                
            }else{
                
                backgroundClip.start();
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }else{
            successClip.start();
            if(delay == 0){
                delay = millis();
            }
            int currentPlayer = 0;
            
            int width = 400;
            int height = scoreboardList.size()* 40+ 40;
            
            fill(187, 255, 255);

            rect(WIDTH/2 - width/2, 150, width, height);
            noFill();
            stroke(0);
            strokeWeight(5);
            rect(WIDTH/2 - width/2, 150, width, height);
            rect(WIDTH/2 - width/2, 150 + 38, width, 1);
            fill(0);
            textSize(20);
            text("Final Scores", WIDTH/2 - width/2 + 30, 150+30 );

            //find the highest score in scoreboard and use text print on screen
            float max = 0;
            char maxname = trtanklList.get(0).name.charAt(0);
            int[] playerrgb = trtanklList.get(0).playerRGB;
            for(char key: scoreboardList.keySet()){
                if(scoreboardList.get(key) > max){
                    max = scoreboardList.get(key);
                    maxname = key;
                }
            }
            for (Tank tank : trtanklList) {
                if (tank.name.charAt(0) == maxname) {
                    playerrgb = tank.playerRGB;
                    break;
                }
            }
            fill(playerrgb[0], playerrgb[1], playerrgb[2]);
            text("Player " + maxname + " wins!", WIDTH/2 - width/2 + 30, 150 -10);


            // print out name and score in descending order in the box, player name coresponed to the player's rgb color
            List<Map.Entry<Character, Float>> list = new ArrayList<>(scoreboardList.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<Character, Float>>() {
                @Override
                public int compare(Map.Entry<Character, Float> o1, Map.Entry<Character, Float> o2) {
                    return o2.getValue().compareTo(o1.getValue()); // decending order
                }
            });

            while (currentPlayer < list.size() && millis() - delay >= 700*(currentPlayer+1)) {
                Map.Entry<Character, Float> entry = list.get(currentPlayer);
                char name = entry.getKey();
                float score = entry.getValue();
                int[] rgb = trtanklList.get(0).playerRGB;
                for (Tank tank : trtanklList) {
                    if (tank.name.charAt(0) == name) {
                        rgb = tank.playerRGB;
                        break;
                    }
                }
                fill(rgb[0], rgb[1], rgb[2]);
                textSize(20);
                text("Player " + name , WIDTH/2 - width/2 + 30, 150 + currentPlayer*40 + 70);
                textAlign(CENTER, LEFT);
                text(round(score), WIDTH/2 - width/2 + 30 + 200, 150 + currentPlayer*40 + 70);
                textAlign(BOTTOM, LEFT);

                currentPlayer++;

                
                
            }
            

        }

        



    }


    /**
        * The main method starts the game.
        *
        * @param args the command line arguments
        */
    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

}



