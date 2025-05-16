/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameObjects;

/**
 *
 * @author sebas
 */
import Graphics.Animation;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import com.mycompany.space_ship.Window;

import math.Vector2D;
import states.GameState;
import input.KeyBoard;
import Graphics.Assets;
import Graphics.Sound;

public class Player extends MovingObject{ //PLAYER HEREDA DE GAME OBJECT, CLASE ABSTRACTA PARA LUEGO CREAR IMPLEMENTACIONES A LOS METODOS
    private Vector2D heading;
    private Vector2D acceleration;
    //private final double ACC = 0.2; //ACC es Acceleration
    //private final double DELTAANGLE = 0.1;
    private boolean accelerating = false; //VARIABLE PARA SABER CUÁNDO ESTAMOS ACELERANDO
    private long fireRate;
    
    private boolean spawning, visible; //VARIABLES PARA DETERMINAR SI EL JUGADOR ESTA REAPARECIENDO DESPUES DE MORIR
    private long spawnTime, flickerTime, shieldTime, doubleScoreTime, fastFireTime, doubleGunTime;
    
    private Sound shoot, loose;

    private boolean shieldOn, doubleScoreOn, fastFireOn, doubleGunOn;

    private Animation shieldEffect;
    
    private long fireSpeed;
        
    public Player(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState){
        super(position, velocity, maxVel, texture, gameState); // AGREGAR EL ATRIBUTO MAXVEL AL SUPER
        heading =  new Vector2D(0, 1);
        acceleration = new Vector2D();
        fireRate = 0;
        spawnTime = 0;
        flickerTime = 0;
        shieldTime = 0;
        fastFireTime = 0;
        doubleGunTime = 0;

        shoot = new Sound(Assets.playerShoot);
        loose = new Sound(Assets.playerLoose);
        shieldEffect = new Animation(Assets.shieldEffect, 80, null);
		
	visible = true;



    }
    
    
    @Override
    public void update(float dt){
        fireRate += dt;
		
	if(shieldOn)
		shieldTime += dt;
		
	if(doubleScoreOn)
		doubleScoreTime += dt;
		
	if(fastFireOn) {
		fireSpeed = Constants.FIRERATE / 2;
		fastFireTime += dt;
	}else {
		fireSpeed = Constants.FIRERATE;
	}
		
	if(doubleGunOn)
		doubleGunTime += dt;
		
	if(shieldTime > Constants.SHIELD_TIME) {
		shieldTime = 0;
		shieldOn = false;
	}
		
	if(doubleScoreTime > Constants.DOUBLE_SCORE_TIME) {
		doubleScoreOn = false;
		doubleScoreTime = 0;
	}
	
	if(fastFireTime > Constants.FAST_FIRE_TIME) {
		fastFireOn = false;
		fastFireTime = 0;
	}
		
	if(doubleGunTime > Constants.DOUBLE_GUN_TIME) {
		doubleGunOn = false;
		doubleGunTime = 0;
	}
	

        //CODIGO ANTERIOR
        
        if(spawning){
	    flickerTime += dt;
            spawnTime += dt;
            if(flickerTime > Constants.FLICKER_TIME){
                
                flickerTime = 0;
                visible = !visible;
            }
        
            if(spawnTime > Constants.SPAWNING_TIME){
                spawning = false;
                visible = true;
            }
	}
        
        
        if (KeyBoard.SHOOT && fireRate > fireSpeed && !spawning){ //CADA VEZ QUE TIEMPO NO ESTÉ CORRIENDO QUEREMOS DISPARAR
	    if(doubleGunOn) {
		Vector2D leftGun = getCenter();
		Vector2D rightGun = getCenter();
			
		Vector2D temp = new Vector2D(heading);
		temp.normalize();
		temp = temp.setDirection(angle - 1.3f);
		temp = temp.scale(width);
		rightGun = rightGun.add(temp);
				
		temp = temp.setDirection(angle - 1.9f);
		leftGun = leftGun.add(temp);
				
		Laser l = new Laser(leftGun, heading, Constants.LASER_VEL, angle, Assets.blueLaser, gameState);
		Laser r = new Laser(rightGun, heading, Constants.LASER_VEL, angle, Assets.blueLaser, gameState);
				
		gameState.getMovingObjects().add(0, l);
		gameState.getMovingObjects().add(0, r);            




            }else {
                gameState.getMovingObjects().add(0,new Laser(
                    getCenter().add(heading.scale(width)),
                    heading,
                    Constants.LASER_VEL,
                    angle,
                    Assets.blueLaser,
                    gameState
                    ));
            }
	
            fireRate = 0;
            shoot.play();
        }
        if(shoot.getFramePosition() > 8500){
            shoot.stop();
        }
        
        if(KeyBoard.RIGHT)
            angle += Constants.DELTAANGLE;
        if(KeyBoard.LEFT)
            angle -= Constants.DELTAANGLE; // se aumenta de 10 a 20 para una rotación más suave
        if(KeyBoard.UP){
            acceleration = heading.scale(Constants.ACC);   
            accelerating = true; //CADA VEZ QUE ESTEMOS ACELERANDO SERÁ VERDAD
        } else{
            if (velocity.getMagnitude() != 0)
                acceleration = (velocity.scale(-1).normalize()).scale(Constants.ACC/2); // NORMALIZAR ES DIVIDIR EL VECTOR ENTRE SU PROPIA MAGNITUD
            accelerating = false;//CADA VEZ QUE NO ACELEREMOS SERÁ FALSO
        }//SE COLOCA DESACELERACIÓN Y FRENADO
        
        
        
        velocity = velocity.add(acceleration); //cambio de la velocidad con respecto tiempo
        
        velocity = velocity.limit(maxVel);
        
        heading = heading.setDirection(angle - Math.PI/2);
        
        position = position.add(velocity);
        
        if(position.getX() > Constants.WIDTH)//COMENZAR HACIENDO QUE LA NAVE NO SE SALGA DE LA VENTANA
            position.setX(0); //SALGA POR EL LADO IZQUIERDO       
        if(position.getY() > Constants.HEIGHT)
            position.setY(0); //SALGA POR LA PARTE DE ARRIBA     
        
        if(position.getX() < -width)
            position.setX(Constants.WIDTH);
        if(position.getY() < -height)
            position.setY(Constants.HEIGHT);

	if(shieldOn){
            shieldEffect.update(dt);
        }               
        collidesWith(); //ANALIZAR SI LA NAVE CHOCA CON METEORO    
        
    }
    public void setShield(){
	if(shieldOn)
            shieldTime = 0;
        shieldOn = true;
    }
	
    public void setDoubleScore() {
	if(doubleScoreOn)
		doubleScoreTime = 0;
	doubleScoreOn = true;
}
	
    public void setFastFire() {
	if(fastFireOn)
		fastFireTime = 0;
	fastFireOn = true;
}
	
    public void setDoubleGun() {
	if(doubleGunOn)
		doubleGunTime = 0;
	doubleGunOn = true;
    }



    @Override
    public void Destroy(){
        spawning = true;
        gameState.playExplosion(position);
        spawnTime = 0;
        loose.play();
        
        if(!gameState.subtractLife(position)){
            gameState.gameOver();
            super.Destroy();
        }        
        resetValues();        
    }
    
    //RESETEAR LOS VALORES Y POSICIÓN DEL JUGADOR
    private void resetValues(){
        angle = 0;
        velocity = new Vector2D();
        position = GameState.PLAYER_START_POSITION;
    }
    
    @Override
    public void draw(Graphics g){
        if(!visible)
            return;
        
        Graphics2D g2d = (Graphics2D)g;
        
        AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX() + width/2 + 5, position.getY() + height/2 + 10); //PASAR LA POSICION DEL CENTRO DE LA NAVE A AT1
        AffineTransform at2 = AffineTransform.getTranslateInstance(position.getX() + 5, position.getY() + height/2 +10); //PASAR LA POSICION DEL CENTRO DE LA NAVE A AT1

        at1.rotate(angle, -5, -10);  //ROTAR
        at2.rotate(angle, width/2 -5, -10); //LE PASO EL ANCHO PARTIDO ENTRE 2 (EL MEDIO) Y Y=0 PORQUE YA ESTAMOS EN EL MEDIO  
        //DIBUJAR CADA VEZ QUE ESTÉ ACELERANDO:
        if(accelerating){
            g2d.drawImage(Assets.speed, at1, null); //DIBUJAR EL EFECTo
            g2d.drawImage(Assets.speed, at2, null); //DIBUJAR EL EFECTO

        }
	if(shieldOn) {
            BufferedImage currentFrame = shieldEffect.getCurrentFrame();
            AffineTransform at3 = AffineTransform.getTranslateInstance(
       				position.getX() - currentFrame.getWidth() / 2 + width/2,
                                position.getY() - currentFrame.getHeight() / 2 + height/2);
			
            at3.rotate(angle, currentFrame.getWidth() / 2, currentFrame.getHeight() / 2);
					
            g2d.drawImage(shieldEffect.getCurrentFrame(), at3, null);
	}
        
        
        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        at.rotate(angle, width/2, height/2);

	if(doubleGunOn)
            g2d.drawImage(Assets.doubleGunPlayer, at, null);
	else
            g2d.drawImage(texture, at, null);
	
        g2d.drawImage(texture, at, null);
    }
    public boolean isSpawning(){return spawning;} //PARA QUE LOS OBJETOS SEPAN SI EL JUGADOR ESTÁ DENTRO DEL PERIODO O NO
    public boolean isShieldOn(){return shieldOn;}
    public boolean isDoubleScoreOn(){return doubleScoreOn;}

}
