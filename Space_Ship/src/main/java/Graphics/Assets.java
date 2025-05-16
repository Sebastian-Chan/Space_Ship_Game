/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Graphics;
/**
 *
 * 
 * @author sebas
 */

import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;

public class Assets {
    
    public static boolean loaded = false;
    public static float count = 0;
    public static float MAX_COUNT = 56;

    public static BufferedImage player;
    //UFO ENEMIGO
    public static BufferedImage ufo;
    
    //EFECTOS DEL JUEGO
    public static BufferedImage speed;
    public static BufferedImage[] shieldEffect = new BufferedImage[3];

    
    //EXPLOSIONES
    public static BufferedImage[] exp = new BufferedImage[9];
    
    //LASERS DEL JUEGO
    public static BufferedImage blueLaser, greenLaser, redLaser;
    
    //METEOROS
    public static BufferedImage[] bigs = new BufferedImage[4];
    public static BufferedImage[] meds = new BufferedImage [2];
    public static BufferedImage[] smalls = new BufferedImage [2];
    public static BufferedImage[] tinies = new BufferedImage[2];
    
    //NUMBERS Y VIDA
    public static BufferedImage[] numbers = new BufferedImage[11];
    public static BufferedImage life;
    
    //FONTS
    
    public static Font fontBig; //MENSAJES GRANDES
    public static Font fontMed;
    
    //SONIDOS
    public static Clip backgroundMusic, explosion, playerLoose, playerShoot, ufoShoot, powerUp;
    
    //UI
    public static BufferedImage blueBtn;
    public static BufferedImage greyBtn;
    
    //POWER UPS
    public static BufferedImage orb,doubleScore, doubleGunPlayer, fastFire, shield, star;
   
    public static void init(){
        player = loadImage("/ships/player.png");
        //EFECTO DE VELOCIDAD
        
        speed = loadImage("/effects/fire08.png");
        
        //LASERS EFECTOS, inicialización 
        blueLaser = loadImage("/lasers/laserBlue01.png");
        greenLaser = loadImage("/lasers/laserGreen11.png");
        redLaser = loadImage("/lasers/laserRed01.png");
        
        //ENEMIGO
        ufo = loadImage("/ships/ufoRed.png");
        
        //VIDA
        life = loadImage("/others/life.png");
        
        //FONT
        fontBig = loadFont("/fonts/kenvector_future.ttf", 42); //MENSAJES GRANDES
        fontMed = loadFont("/fonts/kenvector_future.ttf", 20); //DESPLEGAR MENSAJES DE POWERUPS
        
        //METEOROS, NUMEROS Y POWERUPS INICIALIZACION
        for(int i = 0; i < 3; i++)
            shieldEffect[i] = loadImage("/effects/shield" + (i + 1) +".png"); 
        
        for(int i = 0; i < bigs.length; i++)
            bigs[i] = loadImage("/meteors/meteorGrey_big"+(i+1)+".png");
        for (int i = 0; i < meds.length; i++)
            meds[i] = loadImage("/meteors/meteorGrey_med"+(i+1)+".png");
        for (int i = 0; i < smalls.length; i++)
            smalls[i] = loadImage("/meteors/meteorGrey_small"+(i+1)+".png");
        for (int i = 0; i < tinies.length; i++)
            tinies[i] = loadImage("/meteors/meteorGrey_tiny"+(i+1)+".png");
        for (int i = 0; i < exp.length; i++)
            exp[i] = loadImage("/explosion/"+i+".png");
        //NUMEROS:
        for (int i = 0; i < numbers.length; i++)
            numbers[i] = loadImage("/numbers/"+i+".png");
        
        //SONIDOS 
        backgroundMusic = loadSound("/sounds/backgroundMusic.wav");
        explosion = loadSound("/sounds/explosion.wav");
        playerLoose = loadSound("/sounds/playerLoose.wav");
        playerShoot = loadSound("/sounds/playerShoot.wav");
        ufoShoot = loadSound("/sounds/ufoShoot.wav");
        powerUp = loadSound("/sounds/powerUp.wav");
        
        //UI
        greyBtn = loadImage("/ui/grey_button.png");
        blueBtn = loadImage("/ui/blue_button.png");
        
        orb = loadImage("/powers/orb.png");
	doubleScore = loadImage("/powers/doubleScore.png");
	doubleGunPlayer = loadImage("/powers/doubleGunPlayer.png");
	fastFire = loadImage("/powers/fastFire.png");
	star = loadImage("/powers/star.png");
	shield = loadImage("/powers/shield.png");

        //------
        
        loaded = true;
        
        
        
        
    }
    
    public static BufferedImage loadImage(String path){
        count ++;
        return Loader.ImageLoader(path);        
    }
    public static Font loadFont(String path, int size){
        count ++;
        return Loader.loadFont(path,size);
    }
    public static Clip loadSound(String path){
        count ++;
        return Loader.loadSound(path);
    }
}
