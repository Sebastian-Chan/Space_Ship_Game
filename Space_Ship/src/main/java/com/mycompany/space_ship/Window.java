/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.space_ship;

/**
 *
 * @author sebas
 */
import Graphics.Assets;
import gameObjects.Constants;
import input.KeyBoard;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;

import javax.swing.JFrame;
import states.GameState;
import states.State;
import input.MouseInput;
import states.LoadingState;
import states.MenuState;


public class Window extends JFrame implements Runnable{
    //public static final int WIDTH = 800;
    //public static final int HEIGHT = 600; //VARIABLES CONSTANTES QUE NO CAMBIAN
    private Canvas canvas;  //NUEVO LIENZO
    private Thread thread;     //UN HILO, UN PROGRAMA DENTRO DE OTRO PROGRAMA PARA EVITAR SOBRECARGAR EL PRINCIPAL
    private boolean running = false;
    
    private BufferStrategy bs;
    private Graphics g;
    
    private final int FPS=60; //VARIABLE QUE LOCKEA LOS FPS, ASI YA NO DEPENDE DE LA VELOCIDAD DEL EQUIPO LA VELOCIDAD DEL JUEGO
    private double TARGETTIME = 1000000000/FPS;//TIEMPO REQUERIDO PARA PASAR UN FOTOGRAMA
    private double delta = 0; //ALMACENA EL TIEPMO QUE VAYA PASANDO, DELTA ES EL CAMBIO CON RESPECTO AL TIEMPO
    private int AVERAGEFPS = FPS;
    
    
    private KeyBoard keyBoard;//CREAR UN OBJETO Y AGREGARLO AL CANVAS
    private MouseInput mouseInput; //CREAR OBJETO DE QUE RECIBIRÁ EL INPUT DEL MOUSE

    
    
    
    public Window(){
        setTitle("Space Ship Game");
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); //VENTANA APARECIENDO EN EL CENTRO DE LA PANTALLA
        
        canvas= new Canvas();
        keyBoard = new KeyBoard();
        mouseInput = new MouseInput();
        
        
        canvas.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        canvas.setMaximumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        canvas.setMinimumSize(new Dimension (Constants.WIDTH, Constants.HEIGHT));
        canvas.setFocusable(true); //PERMITE RECIBIR ENTRADAS POR PARTE DEL TECLADO 
        
        add(canvas);
        canvas.addKeyListener(keyBoard);
        canvas.addMouseListener(mouseInput); //AÑADIR EL MOUSE AL CANVAS, ASÍ PODRÁ DETECTARLO LA VENTANA
        canvas.addMouseMotionListener(mouseInput); //AGREGAMOS LA PROPIEDAD DE QUE RECIBA CUANDO SE MUEVA EL MOUSE
        setVisible(true); //DESPLEGAR O HACER VISIBLE LA VENTANA

        
    }
    public static void main(String[] args){
        Window window = new Window();
        
        window.init();
        window.start(); 
        
    }
    
    
    private void update(float dt){
        keyBoard.update();
        State.getCurrentState().update(dt);
        
    }
    
    private void draw(){
        bs = canvas.getBufferStrategy();
        if(bs == null){
            canvas.createBufferStrategy(3); //3 ES EL NUMERO ADECUADO DE CANVAS QUE SE USA
            return;
        }
        g = bs.getDrawGraphics();

        //-----------------// COMIENZO DEL DIBUJO
        g.setColor(Color.BLACK);
        
        g.fillRect(0,0, Constants.WIDTH, Constants.HEIGHT);
        
        State.getCurrentState().draw(g);
        
        g.setColor(Color.WHITE);

        
        g.drawString(""+AVERAGEFPS, 10, 20);
        
        //-----------------// FIN DEL DIBUJO
        
        
        
        
        g.dispose();
        bs.show();
    }
    
    private void init(){
        Thread loadingThread = new Thread(new Runnable(){//CARGAR EL HILO
            @Override
            public void run(){
                Assets.init();
            }
        });
        State.changeState(new LoadingState(loadingThread)); //CAMBIAR ESTADO DEL JUEGO
    }
    
    @Override 
    public void run(){ //CICLO QUE MANTIENE A TODOS LOS OBJETOS DEL JUEGO ACTUALIZADOS
        long now = 0; //VARIABLE QUE LLEVA UN REGISTRO DEL TIEMPO
        long lastTime = System.nanoTime(); //REGRESARÁ LA HORA ACTUAL EN NANOSEGUNDOS
        int frames = 0;
        long time = 0;
        
        init();
        
        while(running){
            now = System.nanoTime();
            delta += (now - lastTime)/TARGETTIME;
            time += (now - lastTime);
            lastTime = now; //OBTENEMOS UN NUEVO VALOR AL RESTARSELO AL TIEMPO QUE TENÍAMOS EN LA ANTERIOR ITERACION TENEMOS EL TIEMPO QUE HA PASADO
            
            
            //CUANDO DELTA EQUIVALGA A TARGETTIME ENTONCES AUMENTAMOS UN FOTOGRAMA
            if (delta >= 1){
                update((float)(delta * TARGETTIME * 0.000001f));
                draw();
                delta --;
                frames ++;
                
            }
            //PARA PODER CRONOMETRAR CUANTOS FPS LLEVAMOS PREGUNTAMOS:
            if(time >= 1000000000){
                AVERAGEFPS = frames;
                frames = 0; //IGUALAMOS A 0 PARA VOLVER A EMPEZAR A CONTAR 
                time = 0;
            }
            
        }
        
        
        stop();
        
    }
    //METODOS PARA INICIAR Y DETENER EL HILO "THREAD"
    private void start(){
        thread = new Thread(this); //CREAR EL HILO Y PASARLE LA CLASE QUE IMPLEMENTA ESTA INTERFAZ 
        thread.start();
        running=true;
        
    }
    private void stop(){
        try {
            thread.join();
            running=false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
    


}

