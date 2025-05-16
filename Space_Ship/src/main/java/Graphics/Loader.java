/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Graphics;

/**
 *
 * @author sebas
 */
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.FontFormatException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Loader {//CLASE QUE PROVEERÁ DE MÉTODOS ÚTILES
    
    public static BufferedImage ImageLoader(String path){//RETORNARA UN OBJETO DE TIPO BUFFEREDIMAGE (ASI JAVA GUARDA IMAGENES) Y TOMARA UN STRING PATH QUE ES LA RUTA
        
        try {
            //return ImageIO.read(Loader.class.getResource(path));
            return ImageIO.read(Loader.class.getResourceAsStream(path));
            
        }catch (IOException e){
            System.err.println("Error al cargar: " + path);
            e.printStackTrace();
        }
        return null;    
    }
    
    public static Font loadFont(String path, int size){
        //CARGAR LA FUENTE Y DARLE EL TAMAÑO
        
        try{
            return Font.createFont(Font.TRUETYPE_FONT, Loader.class.getResourceAsStream(path)).deriveFont(Font.PLAIN, size);
           
        }catch (FontFormatException | IOException e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static Clip loadSound(String path){
        
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Loader.class.getResource(path)));
            return clip;
        }catch (LineUnavailableException | IOException | UnsupportedAudioFileException e){
            e.printStackTrace();
        }
        return null;
        
        
    }
}
