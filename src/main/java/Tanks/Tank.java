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

public class Tank extends PApplet  {

    public PApplet App;
    public int[] playerRGB;
    public String name;
    public int xpos;
    public int ypos;
    public float angle;
    public boolean drop;
    public int fuel = 250;
    public int parachute;
    public PImage parachuteImage;
    public float hp;
    public float power;
    public int wind;
    public Character causeDrop = '\0';
    public Character causeDropWithParachute = '\0';

    /**
     * Constructor for the Tank class
     * @param name the name of the tank
     * @param rgb the RGB value of the tank
     * @param xpos the x-coordinate of the tank
     * @param ypos  the y-coordinate of the tank
     * @param parachuteiImage the image of the parachute
     * @param App App itself
     */
    public Tank(String name, int[] rgb, int xpos, int ypos,PImage parachuteiImage, PApplet App){
        this.App = App;
        this.playerRGB = rgb;
        this.name = name;
        this.xpos = xpos;
        this.ypos = ypos;
        this.angle = HALF_PI;
        this.drop = false;
        this.parachuteImage = parachuteiImage;
        this.hp = 100;
        this.power = 50;
        
    } 

    
    /**
     * Draw the tank based on its center xpos and ypos
     * Draw parachute if the tank is dropping and has a parachute
     * Draw the tank's tube
     */
    public void drawTank(){
        

        
        
        drawTube();
        int longlineLength = 18;
        int shortlineLength = 10;
        int thickness = 5;
        setFill(playerRGB);
        if (drop == true){
            if (parachute >0){
                App.image(parachuteImage, xpos - parachuteImage.width/2, ypos-parachuteImage.height);
                App.rect(xpos - longlineLength/2, ypos - thickness/2 , longlineLength, thickness);
                App.rect(xpos - shortlineLength/2, ypos - thickness , shortlineLength, thickness);
                
                ypos +=2;


                
            }else{
                App.rect(xpos - longlineLength/2, ypos - thickness/2 , longlineLength, thickness);
                App.rect(xpos - shortlineLength/2, ypos - thickness , shortlineLength, thickness);
                ypos +=4;
                
                

            }
        }else{
            App.rect(xpos - longlineLength/2, ypos - thickness/2 , longlineLength, thickness);
            App.rect(xpos - shortlineLength/2, ypos - thickness , shortlineLength, thickness);
            
            
        }


        

        

        

    }

    /**
     * If the tank is dropping and the tank has no parachute, the tank will lose 4 hp each frame
     * (Tank lose 1 hp each pixel when dropping) 
     */
    public void dropDamage(){
        if (drop == true && parachute == 0){
            hp -= 4;
        }
    }


    /**
     * Draw each tank's tube
     */
    public void drawTube(){
        
        App.fill(0);
        int height = 14;
        App.stroke(0);
        App.strokeWeight(3);
        App.line(xpos, ypos, xpos + cos(angle)*height, ypos - sin(angle)*height);
        App.noStroke();
    }

    

    /**
     * Rotates the angle of the tank by the specified amount.
     * 
     * @param deltangle the amount by which to rotate the angle
     */
    public void rorateAngle(float deltangle){
        angle = angle + deltangle;
    }

    /**
     * Fill rgb with integer array
     * @param rgb RGB value in integer array form
     */
    public void setFill(int[] rgb){
        App.fill(rgb[0], rgb[1], rgb[2]);
    }

    
    /**
     * Draw the tank's health and power bar
     */
    public void drawPowerHp(){
        if (power > hp){
            power = hp;
        }// when tank was hurt and cannot sustain the previous power

        if(hp < 0){
            hp = 0;
        }
        float hpremainpercentage = hp/100;   //hp's percentage compared to its original value
        float powerpercentage = power/100; //power's percentage in 100 hp
        
        
        App.fill(0);
        App.rect(442, 7, 166, 26); // outside black
        App.fill(255);
        App.rect(445 + 160*(hpremainpercentage), 9, 160*(1- hpremainpercentage), 22);// concentric white
        setFill(playerRGB);
        App.rect(445, 9, 160*(hpremainpercentage), 22);
        
        
        
        App.fill(102, 139, 139);
        App.rect(440, 5 , powerpercentage * 160 + 7, 30);//grey box
        setFill(playerRGB);
        App.rect(445, 9, powerpercentage* 158, 22);//color hp
        App.fill(178, 34, 34);
        App.rect(445 + powerpercentage*160 - 1, 3, 2, 34);//redline



        App.fill(0);
        App.textSize(17);
        App.text("Health:", 380, 25);
        App.text("Power:", 380, 55);
        App.text(round(power), 450, 55);
        App.text(round(hp), 620, 25);
        
    }

    /**
     * After each turn, an arrow will be drawn on top of tank to indicate whose turn it is and remain for 2 seconds
     */
    public void drawArrow(){

        float arrowSize = 50;
        float arrowHeadSize = 60;
        
        
        App.stroke(0);
        App.strokeWeight(1);
        App.line(xpos, ypos - arrowSize - 50, xpos, ypos - 50  );
        App.line(xpos - 10, ypos - arrowHeadSize, xpos, ypos -50);
        App.line(xpos + 10, ypos - arrowHeadSize, xpos, ypos -50);
        
        App.noStroke();
    }

}

