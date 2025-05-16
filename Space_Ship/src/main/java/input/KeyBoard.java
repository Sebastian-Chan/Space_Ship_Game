/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package input;

/**
 *
 * @author sebas
*/

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoard implements KeyListener{
    
    private boolean[] keys = new boolean[256]; //INICIALIZAR LISTA DE UN TAMAÑO DEFINIDO DE 256 
    
    public static boolean UP, LEFT, RIGHT, SHOOT;
    
    public KeyBoard(){
        UP = false;
        RIGHT = false;
        LEFT = false;
        SHOOT = false; //PARA LOS LASERS
    }
    
    public void update(){
        UP = keys[KeyEvent.VK_UP];
        LEFT = keys[KeyEvent.VK_LEFT];
        RIGHT = keys[KeyEvent.VK_RIGHT];
        SHOOT = keys[KeyEvent.VK_P];  //DISPARAR CON TECLA P
        
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        keys[e.getKeyCode()] = true;
    }
    @Override
    public void keyReleased(KeyEvent e){
        keys[e.getKeyCode()] = false;

    }
    
    @Override
    public void keyTyped(KeyEvent e){
        
    }
    
}
