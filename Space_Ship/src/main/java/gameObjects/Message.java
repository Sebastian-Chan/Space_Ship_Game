/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gameObjects;

import Graphics.Text;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import math.Vector2D;
import states.GameState;

/**
 *
 * @author sebas
 */
public class Message {
    private float alpha; //VALOR DE LOS COLORES RGB QUE INDICA LA TRANSPARENCIA
    private String text;
    private Vector2D position;
    private Color color;
    private boolean center;
    private boolean fade;
    private Font font;
    private final float deltaAlpha = 0.01f;
    private boolean dead;
    
    public Message(Vector2D position, boolean fade, String text, Color color, 
            boolean center, Font font){
        this.font = font;
        this.position = position;
        this.text = text;
        this.color = color;
        this.center = center;
        this.fade = fade;
        this.dead = false;
        
        if(fade)
            alpha = 1; //VISIBLE
        else
            alpha = 0;
    }
    
    public void draw(Graphics2D g2d){
        alpha = Math.max(0.0f, Math.min(1.0f, alpha));        
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); //METODO DE TRANSPARENCIA        
        Text.drawText(g2d, text, position, center, color, font);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1)); //METODO DE TRANSPARENCIA
        
        position.setY(position.getY() - 1);
        
        if(fade){
            alpha -= deltaAlpha;
            if(alpha <= 0) {  // SI ALPHA LLEGA A 0 LO MARCAMOS COMO MUERTO
                dead = true;
            }
        }else{
            alpha +=deltaAlpha; //DESPUES BORRAMOS MENSAJE DE GAME STATE
            if(alpha >= 1) {  // Si alpha llega a 1, dejar de incrementar
                alpha = 1;
            }
        }
        
        
        
    }
    
    public boolean isDead(){return dead;}
    
}
