/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameObjects;

/**
 *
 * @author sebas
 */

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


import math.Vector2D;
import states.GameState;



public class Laser extends MovingObject {
    public Laser (Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture, GameState gameState){
        super(position, velocity, maxVel, texture, gameState);
        this.angle = angle;
        this.velocity = velocity.scale(maxVel); //MISMA VELOCIDAD DEL VECTOR
        
    }
    
    @Override
    public void update(float dt){
        position =  position.add(velocity); //A LA POSICION SUMARLE LA VELOCIDAD
        if(position.getX() < 0 || position.getX() > Constants.WIDTH ||
                position.getY() < 0 || position.getY() > Constants.HEIGHT){
            //REMOVER EL LASER QUE VA AL INFINITO Y NUNCA SE BORRA
            Destroy();
        }
        collidesWith();
        
    }
    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D)g; //DIBUJAMOS EL LASER
        at = AffineTransform.getTranslateInstance(position.getX() - width/2, position.getY());
        
        at.rotate(angle, width/2, 0);
        
        g2d.drawImage(texture, at, null);
        
    }
    
    @Override  //COMIENZO A ESCEPCIÓN DE COLISIÓN EN EL LÁSER, CIRCULO EN LA PUNTA
    public Vector2D getCenter(){
        return new Vector2D(position.getX() + width/2, position.getY() + width/2);
    }
}
