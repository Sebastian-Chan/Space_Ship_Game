/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package states;


/**
 *
 * @author sebas
 */

import Graphics.Animation;
import java.awt.Graphics;
import java.util.ArrayList;


import gameObjects.Player;
import Graphics.Assets;
import Graphics.Sound;
import gameObjects.Constants;
import gameObjects.Message;
import gameObjects.Meteor;
import gameObjects.MovingObject;
import gameObjects.PowerUp;
import gameObjects.PowerUpTypes;
import gameObjects.Size;
import gameObjects.Ufo;
import io.JSONParser;
import io.ScoreData;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import math.Vector2D;
import ui.Action;

public class GameState extends State{
    public static final Vector2D PLAYER_START_POSITION = new Vector2D(Constants.WIDTH/2 - Assets.player.getWidth()/2,
            Constants.HEIGHT/2 - Assets.player.getHeight()/2);
    
    private Player player;
    private ArrayList<MovingObject> movingObjects = new ArrayList<MovingObject>();//SE ENCARGARÁ DE ACTUALIZAR Y DIBUJAR TODOS LOS OBJETOS QUE SE MUEVAN EN EL JUEGO
    private ArrayList<Animation> explosions = new ArrayList<Animation>();
    private ArrayList<Message> messages = new ArrayList<Message>();
    
    private int score = 0;//PUNTAJE ACUMULADO DEL JUGADOR
    private int lives = 3;
    
    private int meteors;
    private int waves = 1;
    
    private Sound backgroundMusic;
    private long gameOverTimer;
    private boolean gameOver;       
    
    private long ufoSpawner;
    private long powerUpSpawner;
    
    public GameState(){
        player = new Player(new Vector2D(Constants.WIDTH/2 - Assets.player.getWidth()/2, 
        Constants.HEIGHT/2 - Assets.player.getHeight()/2), new Vector2D(), Constants.PLAYER_MAX_VEL,  Assets.player, this); // LA MÁX VELOCIDAD VA A SER 5
        movingObjects.add(player);
        
        gameOver = false;
        movingObjects.add(player);
        
        
        meteors = 1;
        
        startWave();
        backgroundMusic = new Sound(Assets.backgroundMusic);
        backgroundMusic.loop();
        backgroundMusic.changeVolume(-20.0f); //BAJARLE VOLÚMEN A MUSICA DE FONDO
        
        gameOverTimer = 0;
        ufoSpawner = 0;
        powerUpSpawner = 0;
        
        gameOver= false;

    }
    
    
    public void addScore(int value, Vector2D position){//POSICION DEL OBJETO QUE SE DESTRUYÓ
        score += value;
        messages.add(new Message(position, true, "+"+value+" score", Color.WHITE, false, Assets.fontMed));
        
    }
    
    //DIVIDIR LOS METEOROS TRAS SER DESTRUIDOS
    public void divideMeteor(Meteor meteor){
        //ENCONTRAR EL TAMAÑO DEL METEORO (TIPO DE NUMERADOR QUE TIENE)
        Size size = meteor.getSize();
        //CREAR UN ARREGLO DE TEXTURAS 
        BufferedImage[] textures = size.textures;
        
        Size newSize = null;
        //PARA ASIGNARLO:
        
        switch(size){
            case BIG:
                newSize = Size.MED;
                break;
            case MED:
                newSize = Size.SMALL;
                break;
            case SMALL:
                newSize = Size.TINY;
                break;
            default:
                return;
        }
        
        for (int i = 0; i < size.quantity; i++){
            movingObjects.add(new Meteor(
                    meteor.getPosition(),
                    new Vector2D(0,1).setDirection(Math.random()*Math.PI*2),    //ELEGIR LA DIRECCION DEL METEORO DE FORMA RANDOM
                    Constants.METEOR_VEL*Math.random() + 1,
                    textures[(int)(Math.random()*textures.length)],
                    this,
                    newSize
                    ));
        }
    }

    
    
    private void startWave(){
        //CREAR NUEVO MENSAJE AL COMIENZO DE CADA OLEADA YA APLICANDO LA TRANSPARENCIA
        messages.add(new Message(new Vector2D(Constants.WIDTH/2, Constants.HEIGHT/2), true,
        "WAVE "+waves,Color.WHITE, true, Assets.fontBig));
        
        double x,y;
        for (int i = 0; i < meteors; i++){//DAR POSICIONES ALEATORIAS SOBRE EL VORDE DEL MARCO
            x = i % 2 == 0 ? Math.random()*Constants.WIDTH: 0; //SI ESTE CONTADOR ES PAR ENTONCES SU RESIDUO ES IGUAL A 0 ENTONCES X SERÁ UN NUM RANDOM ENTRE 0 Y ANCHO
            y = i % 2 == 0 ? 0: Math.random()*Constants.HEIGHT;
            
            BufferedImage texture = Assets.bigs[(int)(Math.random()*Assets.bigs.length)]; 
            
            movingObjects.add(new Meteor(
                    new Vector2D(x,y),
                    new Vector2D(0,1).setDirection(Math.random()*Math.PI*2),    //ELEGIR LA DIRECCION DEL METEORO DE FORMA RANDOM
                    Constants.METEOR_VEL*Math.random() + 1,
                    texture,
                    this,
                    Size.BIG
                    ));

            
        }
        meteors++;
    }    
    
    public void playExplosion(Vector2D position){
        explosions.add(new Animation(
                Assets.exp,
                50,
                position.subtract(new Vector2D(Assets.exp[0].getWidth()/2, Assets.exp[0].getHeight()/2 ))
        
        ));
    }
    
    private void spawnUfo(){
        int rand = (int) (Math.random()*2);
        double x = rand == 0 ? (Math.random()*Constants.WIDTH): 0;
        double y = rand == 0 ? 0: (Math.random()*Constants.HEIGHT);
        ArrayList <Vector2D> path = new ArrayList <Vector2D>(); //ARREGLO QUE REPRESENTA EL CAMINO
        double posX, posY; //VARIABLES QUE FORMAN EL CAMINO
        
        //VALOR AL AZAR EN SECTOR SUPERIOR IZQUIERDO
        posX = Math.random()*Constants.WIDTH/2;
        posY = Math.random()*Constants.HEIGHT/2;
        path.add(new Vector2D(posX, posY));
        
        //VECTOR AL AZAR EN SECTOR SUPERIOR DERECHO
        posX = Math.random()*(Constants.WIDTH/2) + Constants.WIDTH/2;
        posY = Math.random()*Constants.HEIGHT/2;
        path.add(new Vector2D(posX, posY));
        //VECTOR AL AZAR EN SECTOR INFERIOR IZQUIERDO 
        posX = Math.random()*Constants.WIDTH/2;
        posY = Math.random()*(Constants.HEIGHT/2) + Constants.HEIGHT/2;
        path.add(new Vector2D(posX, posY));
        //VECTOR AL AZAR EN SECTOR INFERIOR DERECHO
        posX = Math.random()*(Constants.WIDTH/2) + Constants.WIDTH/2;
        posY = Math.random()*(Constants.HEIGHT/2) + Constants.HEIGHT/2;
        path.add(new Vector2D(posX, posY));
        
        movingObjects.add(new Ufo( //LO AGREGO EN ARREGLO DE OBJETOS MÓVILES
        new Vector2D(x,y),//POSICION
        new Vector2D(),//VELOCIDAD
        Constants.UFO_MAX_VEL,//VELOCIDAD MAX
        Assets.ufo, 
        path,//CAMINO Y ESTADO DE JUEGO
        this
        ));
    }
    
    private void spawnPowerUp() {
		
	final int x = (int) ((Constants.WIDTH - Assets.orb.getWidth()) * Math.random());
	final int y = (int) ((Constants.HEIGHT - Assets.orb.getHeight()) * Math.random());
		
	int index = (int) (Math.random() * (PowerUpTypes.values().length));
		
	PowerUpTypes p = PowerUpTypes.values()[index];
        
        if (p.texture == null) {
        System.err.println("Error: Textura nula para PowerUpType " + p.name());
        return; // No crear el PowerUp si no hay textura
        }
		
	final String text = p.text;
	Action action = null;
	Vector2D position = new Vector2D(x , y);
		
	switch(p) {
	case LIFE:
            action = new Action() {
                    @Override
                    public void doAction() {
			
                            lives ++;
                            messages.add(new Message(
                                            position,
                                            false,
                                            text,
                                            Color.GREEN,
                                            false,
                                            Assets.fontMed
                                            ));
                    }
            };
            break;
        case SHIELD:
            action = new Action() {
                    @Override
                    public void doAction() {
                            player.setShield();
                            messages.add(new Message(
                                            position,
                                            false,
                                            text,
                                            Color.DARK_GRAY,
                                            false,
                                            Assets.fontMed
                                            ));
                    }
            };
            break;
        case SCORE_X2:
            action = new Action() {
                    @Override
                    public void doAction() {
                            player.setDoubleScore();
                            messages.add(new Message(
                                            position,
                                            false,
                                            text,
                                            Color.YELLOW,
                                            false,
                                            Assets.fontMed
                                            ));
                    }
            };
            break;
        case FASTER_FIRE:
            action = new Action() {
                    @Override
                    public void doAction() {
                            player.setFastFire();
                            messages.add(new Message(
                                            position,
                                            false,
                                            text,
                                            Color.BLUE,
                                            false,
                                            Assets.fontMed
                                            ));
                    }
            };
            break;
        case SCORE_STACK:
            action = new Action() {
                    @Override
                    public void doAction() {
                            score += 1000;
                            messages.add(new Message(
                                            position,
                                            false,
                                            text,
                                            Color.MAGENTA,
                                            false,
                                            Assets.fontMed
                                            ));
                    }
            };
            break;
        case DOUBLE_GUN:
            action = new Action() {
                    @Override
                    public void doAction() {
                            player.setDoubleGun();
                            messages.add(new Message(
                                            position,
                                            false,
                                            text,
                                            Color.ORANGE,
                                            false,
                                            Assets.fontMed
                                            ));
                    }
            };
            break;
        default:
            break;
        }
		
        this.movingObjects.add(new PowerUp(
                            position,
                            p.texture,
                            action,
                            this
                            ));
		
		
    }
    
    
    public void update(float dt){
        if(gameOver)
            gameOverTimer += dt;
        powerUpSpawner += dt;
        ufoSpawner += dt;
        
        for(int i = 0; i < movingObjects.size(); i++){//ACTUALIZAR Y DIBUJAR
            MovingObject mo = movingObjects.get(i);
            mo.update(dt);
            if(mo.isDead()){
                movingObjects.remove(i);
                i--;
            }
        }
        
        for(int i = 0; i < explosions.size(); i++){//ACTUALIZAR Y DIBUJAR
            Animation anim = explosions.get(i);
            anim.update(dt);
            if(!anim.isRunning()){                
                explosions.remove(i);
            }
        }
        if (gameOverTimer > Constants.GAME_OVER_TIME){
            //PARA GUARDAR UN PUNTAJE DEBO LEER LOS QUE YA HAY EN LA LISTA
            try{
                ArrayList<ScoreData> dataList = JSONParser.readFile();
                //AGREGAR A ESTA LISTA EL NUEVO PUNTAJE
                dataList.add(new ScoreData(score));
                JSONParser.writeFile(dataList);
                
            } catch (IOException e) {
                e.printStackTrace();

            }
            backgroundMusic.stop();
                          
            State.changeState(new MenuState()); //CREAR UN NUEVO MENU AL PERDER
        }
        
        if(powerUpSpawner > Constants.POWER_UP_SPAWN_TIME){
            spawnPowerUp();
            powerUpSpawner = 0;
        }
        
        if (ufoSpawner > Constants.UFO_SPAWN_RATE){
            spawnUfo();
            ufoSpawner = 0;
        }
        
        
        for(int i = 0; i < movingObjects.size(); i++)//SI DESPUÉS DE ITERAR TODO NO HAY NINGÚN METEORO ENTONCES COMENZARÁ UNA NUEVA OLEADA
            if(movingObjects.get(i) instanceof Meteor)
                return;
        
        startWave();
    }
    
    
    
    public void draw(Graphics g){
        
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //EL FAMOSO CONCEPTO DE ANTIALIASING PARA QUE NO PIERDA CALIDAD LA IMÁGEN DE LA NAVE
        
        for(int i = 0; i < messages.size(); i++){//ACTUALIZAR Y DIBUJAR
            messages.get(i).draw(g2d);
            if(messages.get(i).isDead())
                messages.remove(i);
        }    

        
        for(int i = 0; i < movingObjects.size(); i++)//ACTUALIZAR Y DIBUJAR
            movingObjects.get(i).draw(g);
        
        for(int i = 0; i < explosions.size(); i++){//ACTUALIZAR Y DIBUJAR
            Animation anim = explosions.get(i);
            g2d.drawImage(anim.getCurrentFrame(), (int)anim.getPosition().getX(),(int)anim.getPosition().getY(),null );  
        }
        drawScore(g);
        drawLives(g);
        
    }
    
    private void drawScore(Graphics g){
        Vector2D pos = new Vector2D(850, 25); //VECTOR PARA REPRESENTAR LA POSICION DONDE DIBUJAREMOS EL SCORE
        
        String scoreToString = Integer.toString(score);
        
        for (int i = 0; i < scoreToString.length(); i++){
            g.drawImage(Assets.numbers[Integer.parseInt(scoreToString.substring(i, i + 1))], 
                    (int)pos.getX(),(int)pos.getY(), null);
            pos.setX(pos.getX() + 20);
        }        
    }
    private void drawLives(Graphics g){
        if(lives < 1)
            return;
        
        Vector2D livePosition = new Vector2D(25,25);
        
        g.drawImage(Assets.life, (int)livePosition.getX(), (int)livePosition.getY(),null); //PRIMERO DIBUJAMOS EL SIMBOLO
        g.drawImage(Assets.numbers[10], (int)livePosition.getX() + 40, //DIBUJAMOS LA LETRA X QUE SE ENCUENTRA EN ASSETS
                (int)livePosition.getY() + 5,null);
        String livesToString = Integer.toString(lives);
        
        Vector2D pos = new Vector2D(livePosition.getX(),livePosition.getY());
        
        for(int i = 0; i < livesToString.length(); i++){
            int number = Integer.parseInt(livesToString.substring(i, i+1));
            if(number < 0) //COMPROBAR QUE NUMERO DE VIDAS SEA MAYOR QUE 0
                break;
            g.drawImage(Assets.numbers[number],
                    (int)pos.getX() + 60, (int)pos.getY() + 5, null);
            pos.setX(pos.getX()+20);
        }
    }
    
    public ArrayList<MovingObject> getMovingObjects(){
        return movingObjects;
    }
    public ArrayList<Message> getMessages(){
        return messages;
    }
    
    public Player getPlayer(){
        return player;
    }
    public boolean subtractLife(Vector2D position){
        lives --;
        Message lifeLostMesg = new Message (
            position,
            false,
            "-1 LIFE",
            Color.RED,
            false,
            Assets.fontMed);
        messages.add(lifeLostMesg);
        
        
        return lives > 0;
    }
    
    public void gameOver(){
        Message gameOverMsg = new Message(
        PLAYER_START_POSITION, 
                true, 
                "GAME OVER",
                Color.WHITE,
                true,
                Assets.fontBig);
        this.messages.add(gameOverMsg);
        gameOver = true;
    }
}
