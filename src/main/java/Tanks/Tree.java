package Tanks;

import Tanks.App;
import processing.core.PApplet;
import org.checkerframework.checker.units.qual.A;
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
public class Tree extends PApplet {

    public PApplet App;
    public int xpos;
    public int xRandom;
    public int ypos;
    public PImage treeImage;
    public Random random = new Random();
    public Tree(int xpos, PImage treeImage, PApplet App){
        
        
        this.xpos = xpos;
        this.App = App;
        this.treeImage = treeImage;
        
    }
    

    /**
     * Sets the x-coordinate of the tree within a random difference from the original x-coordinate in a range from -30 to 30
     * 
     * @param randomdelta the random delta value to add to the current x-coordinate
     */
    public void setXrandom(int randomdelta){
        this.xRandom = xpos + randomdelta;
    }

    /**
     * Draws the tree on the screen based on its x and y position
     */
    public void drawTree(){
        int imgWidth = treeImage.width;
        int imgHeight = treeImage.height;
        PImage scaledImage = treeImage.copy();
        Double scale = 0.1;   
        scaledImage.resize((int)(imgWidth*scale), (int)(imgHeight*scale));
        App.image(scaledImage, xRandom - (int)(imgWidth*scale/2), (int)(-imgHeight*scale/2) + ypos -16);
    }


    /**
     * Updates the y-position of the tree.
     * 
     * @param ypos the new y-position of the tree
     */
    public void updateY(int ypos){
        this.ypos = ypos;
    }


    /**
     * Returns the x-coordinate position of the tree.
     *
     * @return the x-coordinate position of the tree
     */
    public int getXpos(){
        return xpos;
    }

    
}
