/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Graphics;

import java.awt.image.BufferedImage;
import math.Vector2D;

/**
 *
 * @author sebas
 */
public class Animation {
    private BufferedImage[] frames;
    private int velocity;
    private int index; //FOTOGRAMA ACTUAL EN EL QUE ESTA DIBUJANDO
    private boolean running; //NOS DIRÁ SI LA ANIMACIÓN ESTÁ CORRIENDO O NO
    private Vector2D position; 
    private long time, lastTime;
    
    public Animation(BufferedImage[] frames, int velocity, Vector2D position){
        this.frames = frames;
        this.velocity = velocity;
        this.position = position;
        index = 0;
        running = true;
        time = 0;
        lastTime = System.currentTimeMillis();
    }
    
    //METODO QUE SE ENCARGE DE ACTUALIZAR LOS FOTOGRAMAS CADA VEZ QUE PASE UNA CANTIDAD DE TIEMPO
    public void update(float dt){
        time += dt;
        lastTime = System.currentTimeMillis();
        
        if (time > velocity){
            time = 0;
            index++;
            //PREGUNTAR EI SL INDICE SE SALE DEL BORDE DEL TAMAÑO DE FOTOGRAMAS
            if(index >= frames.length){
                //YA SE ABRÁ ACABADO LA ANIMACIÓN ASÍ QUE LO SETEAMOS A FALSO
                running = false;
                index = 0;
            }
        }
        
    }
    
    public boolean isRunning(){
        return running;
    }
    public Vector2D getPosition(){
        return position;
    }
    
    //VA A OBTENER EL FOTOGRAMA ACTUAL EN EL ARREGLO DE FOTOGRAMAS
    public BufferedImage getCurrentFrame(){
        return frames[index];
    }
    
}
