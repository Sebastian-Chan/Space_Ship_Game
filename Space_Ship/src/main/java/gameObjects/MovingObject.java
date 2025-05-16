/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameObjects;

/**
*
 * @author sebas
 
 
 */
import Graphics.Assets;
import Graphics.Sound;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import math.Vector2D;
import states.GameState;

public abstract class MovingObject extends GameObject {
    protected Vector2D velocity;
    protected AffineTransform at;
    protected double angle;
    protected double maxVel; // establecer un máximo de velocidad para un fácil manejo de la nave
    protected int width;
    protected int height;
    protected GameState gameState;
    
    private Sound explosion;
    
    protected boolean Dead;
    
    
    public MovingObject(Vector2D position,Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState){
        super(position, texture);
        this.velocity = velocity;
        this.maxVel = maxVel;
        this.gameState=gameState;
        width = texture.getWidth();
        height = texture.getHeight();
        angle = 0;
        explosion = new Sound(Assets.explosion); 
        Dead = false;
    }
    
    protected void collidesWith(){
        ArrayList <MovingObject> movingObjects = gameState.getMovingObjects();
        for (int i = 0; i < movingObjects.size(); i++){
            MovingObject m = movingObjects.get(i);
            //CADA OBJETO CHECARÁ SUS COLISIONES PERO NO ES POSIBLE COLISIÓN CONSIGO MISMO
            if(m.equals(this))
                continue;
            //CALCULAR LA DISTANCIA ENTRE EL OBJETO Y EL JUGADOR
            double distance = m.getCenter().subtract(getCenter()).getMagnitude();
            
            //COMPROBAR SENTENCIA DE COLISIÓN COMPARANDO SUMATORIA DE RADIOS A DISTANCIA            
            if(distance < m.width/2 + width/2 && movingObjects.contains(this) && !m.Dead && !Dead){ //VERIFICAR QUE EL OBJETO TAMBIÉN ESTÉ EN EL ARREGLO PARA SABER SI NO SE HA DESTRUIDO AÚN
                objectCollision(m,this);
            }    
           
        }
    }
    
    private void objectCollision(MovingObject a, MovingObject b){
        //PREGUNTAR SI LOS OBJETOS A Y B ES EL JUGADOR, Y SI LO ES PREGUNTAR SI SPAWNIGN ES TRUE Y NO DETECTAR COLISIÓN
        Player p = null;
        
        if(a instanceof Player){
            p = (Player)a;
        }
        else if(b instanceof Player){
            p = (Player)b;
        }
        
        if(p != null && p.isSpawning())
            return;
        
        if(a instanceof Meteor && b instanceof Meteor)
            return;
        
        if(!(a instanceof PowerUp || b instanceof PowerUp)){
            a.Destroy();
            b.Destroy();
            return;
        }
		
	if(p != null){
            if(a instanceof Player){
                ((PowerUp)b).executeAction();	
                b.Destroy();
            }else if(b instanceof Player){
                ((PowerUp)a).executeAction();
		a.Destroy();
            }
	}
    }
    
    protected void Destroy(){
        Dead = true;
        if(!(this instanceof Laser) && !(this instanceof PowerUp))
            explosion.play();
    }    
    protected Vector2D getCenter(){
        return new Vector2D(position.getX() + width/2, position.getY() + height/2);
    }
    public boolean isDead(){return Dead;}
    
}
