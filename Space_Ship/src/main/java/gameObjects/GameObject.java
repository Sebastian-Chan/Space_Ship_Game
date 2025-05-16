/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameObjects;

/**
 *
 * @author sebas
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import math.Vector2D;

//CREAMOS PLANTILLAS PARA LOS OBJETOS
public abstract class GameObject {
    protected BufferedImage texture;
    protected Vector2D position; 
    
    public GameObject(Vector2D position, BufferedImage texture){ //RECIBIR PARAMETROS DE LA CLASE VECTOR 2D Y TEXTURA
        this.position = position;
        this.texture = texture;
    }
    
    public abstract void update(float dt);
    
    public abstract void draw(Graphics g);
    
    public Vector2D getPosition(){
        return position;
    }
    public void setPosition(Vector2D position){
        this.position = position;
    }
}
