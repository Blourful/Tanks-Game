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
import Tanks.Tank;
public class Projectile extends PApplet{
    public float xpos;
    public float ypos;
    public float vx;
    public float vy;
    public float g= .24f;
    public int wind;
    public float speed;
    public float angle;
    public PApplet App;
    public int[] playerRGB;
    public char name;
    public float newvx;

    public Projectile(char name, float xpos, float ypos, float speed, float angle,int[] playerRGB, PApplet App){
        this.name = name;
        this.angle = angle ;
        this.xpos = xpos + cos(angle)*(14) ;
        this.ypos = ypos - sin(angle)*(14);
        this.speed = speed;

        
        
        
        this.playerRGB= playerRGB;
        this.App = App;
        vx = speed*(float)cos(this.angle);
        vy = -speed*(float)sin(this.angle);
    }

    /**
     * Draws the projectile on the screen and updates its position based on the wind.
     * 
     * @param wind the wind value which will cause 0.03 acceleration in the x direction
     * 
     */
    public void drawprojectile(int wind){
        
        App.fill(playerRGB[0], playerRGB[1], playerRGB[2]);
       
        App.ellipse(xpos , ypos, 8, 8); 
        

        xpos += newvx;
        ypos += vy;
        vy += g;
        newvx = vx + wind*0.03f;

    }
    /**
     * Get the integer value of the x position
     * @return the integer value of the x position
     */
    public int getXpos(){
        return round(xpos);
    }


    /**
     * Get the integer value of the y position
     * @return the integer value of the y position
     */
    public int getYpos(){
        return round(ypos);
    }
}
