package Tanks;


import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class SampleTest extends PApplet{
    

    @Test
    public void simpleTest() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"Tanks"}, app);
        app.setup();
        app.delay(1000);
        assertEquals(864, App.WIDTH);
        assertEquals("config.json", app.configPath);
        
    }

    // Test whether the App class loads the sources correctly
    @Test
    public void testAppLoadSources() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"Tanks"}, app);
        app.setup();
        app.delay(1000);
        assertEquals("config.json", app.configPath);
        assertEquals(3, app.levelSize);
        assertEquals(3, app.levels.size());
        assertNotNull(app.playerColors);
        assertEquals(3, app.levelSize);
        assertEquals("level1.txt", app.layoutList.get(0));
        assertEquals("snow.png", app.backgroundList.get(0));
        assertEquals("255,255,255", app.foregroundList.get(0));
        assertEquals("tree2.png", app.treeList.get(0));
        assertNotNull(app.backgroundsiImages[0]);
        assertNotNull(app.player_colours);
        assertNotNull(app.fueliImage);
        assertNotNull(app.parachuteImageLegend);
        assertNotNull(app.parachuteImageOriginal);
        assertNotNull(app.windRightiImage);
        assertNotNull(app.windLeftiImage);
    }

    /**
     *  Test whether the Terrain class loads the sources correctly
     *  Level4.txt contains 7 tanks, 9 ABCDEF (in order)
     */
    @Test
    public void testSetTankList() {
        App app = new App();
        PImage parachuteImage = new PImage();
        Terrain terrain = new Terrain(0, parachuteImage, app);
        HashMap<String, String> playerColours = new HashMap<>();

        playerColours.put("A", "0,0,255");
        playerColours.put("B", "255,0,0");
        playerColours.put("C", "0,255,255");
        playerColours.put("D", "255,255,0");
        playerColours.put("E", "0,255,0");
        playerColours.put("F", "random");
        playerColours.put("G", "random");
        playerColours.put("H", "random");
        playerColours.put("I", "random");
        playerColours.put("0", "0,0,0");
        playerColours.put("1", "0,0,0");
        playerColours.put("2", "0,0,0");
        playerColours.put("3", "0,0,0");
        playerColours.put("4", "0,0,0");
        playerColours.put("5", "0,0,0");
        playerColours.put("6", "0,0,0");
        playerColours.put("7", "0,0,0");
        playerColours.put("8", "0,0,0");
        playerColours.put("9", "0,0,0");
        

        terrain.setPlayer(playerColours);

        ArrayList<String> layoutList = new ArrayList<>();
        layoutList.add("level4.txt");
        layoutList.add("level2.txt");

        terrain.setLayout(layoutList);

        terrain.readtxt(0);

        ArrayList<Tank> tankList = terrain.setTanklist();

        assertNotNull(tankList);
        //Check total number of tanks
        assertEquals(7, tankList.size());
        //Check A tank
        Tank tank1 = tankList.get(0);
        assertEquals("A", tank1.name);
        assertEquals(64, tank1.xpos);
        assertEquals(0, tank1.ypos);
        assertEquals(parachuteImage, tank1.parachuteImage);
        assertEquals(app, tank1.App);
        //Check 9 tank
        Tank tank3 = tankList.get(2);
        assertEquals("9", tank3.name);
        assertEquals(544, tank3.xpos);
        assertEquals(0, tank3.ypos);
        assertEquals(parachuteImage, tank3.parachuteImage);

        //Check F tank
        Tank tank4 = tankList.get(3);
        assertEquals("F", tank4.name);
        assertEquals(576, tank4.xpos);
        assertEquals(0, tank4.ypos);
        assertEquals(parachuteImage, tank4.parachuteImage);

    }


    
    /**
     * Level4.txt contains 7 tanks, A, B, 9, F, C, D, E (in order)
     * After bubble sort, the tanks should be sorted in the order A, B, C, D, E, F, 9
     */
    @Test
    public void testBubbleSortTankListByAscii() {
        App app = new App();
        PImage parachuteImage = new PImage();
        Terrain terrain = new Terrain(0, parachuteImage, app);
        HashMap<String, String> playerColours = new HashMap<>();
        playerColours.put("A", "0,0,255");
        playerColours.put("B", "255,0,0");
        playerColours.put("C", "0,255,255");
        playerColours.put("D", "255,255,0");
        playerColours.put("E", "0,255,0");
        playerColours.put("F", "random");
        playerColours.put("9", "0,0,0");
        terrain.setPlayer(playerColours);

        ArrayList<String> layoutList = new ArrayList<>();
        layoutList.add("level4.txt");
        terrain.setLayout(layoutList);
        terrain.readtxt(0);
        terrain.tanklList = terrain.setTanklist();
        terrain.bubbleSortTankListByAscii();


        assertEquals(terrain.tanklList.get(0).name, "9");
        


        
    }

    @Test
    public void testyposUpdateAndsetdrop() {
        
        App app = new App();
        PImage parachuteImage = new PImage();
        Terrain terrain = new Terrain(0, parachuteImage, app);
        HashMap<String, String> playerColours = new HashMap<>();
        playerColours.put("A", "0,0,255");
        playerColours.put("B", "255,0,0");
        playerColours.put("C", "0,255,255");
        playerColours.put("D", "255,255,0");
        playerColours.put("E", "0,255,0");
        playerColours.put("F", "random");
        playerColours.put("9", "0,0,0");
        terrain.setPlayer(playerColours);

        ArrayList<String> layoutList = new ArrayList<>();
        layoutList.add("level4.txt");
        terrain.setLayout(layoutList);
        terrain.readtxt(0);
        terrain.tanklList = terrain.setTanklist();
        terrain.initialToppixels();
        for(int i = 0; i < 2; i++){
            terrain.moveAverge();
        }


        //imitating the moment when tank drop to the ground with parachute greater than 0
        //This part is used to check tankyposupdate() function
        terrain.tanklList.get(0).ypos = 500;
        terrain.tanklList.get(0).drop = true;
        terrain.tanklList.get(0).parachute = 3;
        terrain.tankyposUpdated();
        int tank1ypos = terrain.columnToppixels[terrain.tanklList.get(0).xpos];

        assertEquals(tank1ypos, terrain.tanklList.get(0).ypos);
        assertEquals(2, terrain.tanklList.get(0).parachute);

        //Set the tank's height higher than terrain's height
        // This part is used to check setdrop() function

        terrain.tanklList.get(1).ypos = 0;
        boolean originaldropstatus = terrain.tanklList.get(1).drop;
        terrain.setDrop();
        assertEquals(!originaldropstatus, terrain.tanklList.get(1).drop);

        


        
    }

    @Test
    public void testkeypressed(){
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"Tanks"}, app);
        app.setup();
        app.delay(1000);
        // press key right arrow once, check whether tank A's xpos change 2 pixels in one frame
        


        // press space key, check whether the number of projectiles increases 
        app.key = ' ';
        app.keyPressed(new KeyEvent(app, 0, 0, 0, ' ', 39));
        // app.draw();
        assertEquals(1, app.projectileList.size());
        
        
  
    }

    @Test
    public void testRkeypressed(){
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"Tanks"}, app);
        app.setup();
        app.delay(1000);
        // press key right arrow once, check whether tank A's xpos change 2 pixels in one frame
        app.terrain.tanklList.get(0).hp = 50;
        app.scoreboardList.put('A', 100f);


        // press space r to check whether the tank's hp incerease 20
        app.key = 'r';
        app.keyPressed(new KeyEvent(app, 0, 0, 0, 'r', 82));
        // app.draw();
        assertEquals(70, app.terrain.tanklList.get(0).hp);

         
        
        
  
    }


    @Test
    public void testFkeypressed(){
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"Tanks"}, app);
        app.setup();
        app.delay(1000);
        // press key right arrow once, check whether tank A's xpos change 2 pixels in one frame
        app.terrain.tanklList.get(0).fuel = 0;
        app.scoreboardList.put('A', 100f);


        // press space f to check whether the tank's fuel incerease 200
        app.key = 'f';
        app.keyPressed(new KeyEvent(app, 0, 0, 0, 'f', 82));
        assertEquals(200, app.terrain.tanklList.get(0).fuel);
        
         
        
        
  
    }


    @Test
    public void TankDie(){
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[]{"Tanks"}, app);
        app.setup();
        app.delay(1000);
        // check tank's death
        app.terrain.tanklList.get(0).hp = -10;
        app.checkTanklist();
        assertEquals(3, app.terrain.tanklList.size());
        

         
        
        
  
    }




}

//gradle test jacocoTestReport