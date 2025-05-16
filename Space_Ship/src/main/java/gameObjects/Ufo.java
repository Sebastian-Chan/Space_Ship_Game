/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameObjects;

import Graphics.Assets;
import Graphics.Sound;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import math.Vector2D;
import states.GameState;

/**
 *
 * @author sebas
 */
public class Ufo extends MovingObject{
    //ARREGLO QUE CONTENDRÁ LOS NODOS QUE FORMARÁN EL CAMINO
    private ArrayList<Vector2D> path;
    
    private Vector2D currentNode;
    
    private int index;
    
    private boolean following; //SERA FALSO CUANDO LA NAVE HAYA TERMINADO DE RECORRER TODO EL CAMINO

    private long fireRate;
    
    private Sound shoot;
    
    public Ufo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, 
            ArrayList<Vector2D> path, GameState gameState) {
        
        super(position, velocity, maxVel, texture, gameState);
        this.path = path;
        index = 0;
        following = true;
        fireRate = 0;
        shoot = new Sound(Assets.ufoShoot);
    }
    
//VECTOR QUE GESTIONARÁ EL CAMINO DEL UFO:
    private Vector2D pathFollowing(){
        currentNode = path.get(index);
        double distanceToNode = currentNode.subtract(getCenter()).getMagnitude();
        //SI LA DISTANCIA AL NODO ES MENOR QUE EL RADIO DE CADA NODO
        if(distanceToNode < Constants.NODE_RADIUS){
            index++;
            if(index >= path.size()){//SI EL INDICE LLEGO AL FINAL ENTONCES SIGNIFICA QUE EL RECORRIDO HA TERMINADO
                following = false;
            }
        }
        return seekForce(currentNode);
    }

//VECTOR QUE PERMITA CALCULAR LA FUERZA
    private Vector2D seekForce(Vector2D target){
        Vector2D desiredVelocity = target.subtract(getCenter());
        desiredVelocity = desiredVelocity.normalize().scale(maxVel);//magnitud sea 1
        return desiredVelocity.subtract(velocity);
        
    }

    @Override
    public void update(float dt) {
        fireRate += dt;
        Vector2D pathFollowing;
        if(following)
            pathFollowing = pathFollowing();
        else
            pathFollowing = new Vector2D();
        
        pathFollowing = pathFollowing.scale(1/Constants.UFO_MASS);//calculo de aceleracion
        velocity = velocity.add(pathFollowing);
        
        velocity = velocity.limit(maxVel);
        
        position = position.add(velocity);
        //ESCRIBIR EL UFO EN CASO DE QUE HAYA TERMINADO SU RECORRIDO
        if(position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT
                || position.getX() < -width || position.getY() < -height)
            Destroy();
        
        //CADA VEZ QUE EL CRONOMETRO NO ESTE CORRIENDO DEBERIA CALCULAR UN NUEVO DISPARO
        if (fireRate > Constants.UFO_FIRE_RATE){
            Vector2D toPlayer = gameState.getPlayer().getCenter().subtract(getCenter());
            toPlayer = toPlayer.normalize();
            
            double currentAngle = toPlayer.getAngle();
            currentAngle += Math.random()*Constants.UFO_ANGLE_RATE - Constants.UFO_ANGLE_RATE/2;
            
            if(toPlayer.getX() < 0)
                currentAngle = -currentAngle + Math.PI;
           
            toPlayer = toPlayer.setDirection(currentAngle);
            
            Laser laser = new Laser(
                    getCenter().add(toPlayer.scale(width)),
                    toPlayer,
                    Constants.LASER_VEL,
                    currentAngle + Math.PI/2,
                    Assets.redLaser,
                    gameState
                    );
            
            gameState.getMovingObjects().add(0, laser);
            fireRate = 0;
            
            shoot.play();
            
        }
        if(shoot.getFramePosition() > 8500){
            shoot.stop();
        }
        
        angle += 0.05;
        collidesWith();
    }
    
    @Override
    public void Destroy(){
        gameState.addScore(Constants.UFO_SCORE, position);
        gameState.playExplosion(position);
        super.Destroy();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        at = AffineTransform.getTranslateInstance(position.getX(),position.getY());
        at.rotate(angle, width/2, height/2);
        
        g2d.drawImage(texture, at, null);
        
        
    }
    
    
}
