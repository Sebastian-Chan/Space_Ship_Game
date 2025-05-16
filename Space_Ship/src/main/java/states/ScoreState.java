package states;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import gameObjects.Constants;
import Graphics.Assets;
import Graphics.Text;
import io.JSONParser;
import io.ScoreData;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import math.Vector2D;
import ui.Action;
import ui.Button;

public class ScoreState extends State{ //CLASE DE ESTADO DE PUNTAJES, ALMACENAREMOS PUNTOS, ENTRADA Y SALIDA DE DATOS 
	
	private Button returnButton;
	
	private PriorityQueue<ScoreData> highScores; //LISTA DE PRIORIDAD QUE SOLO ALMACENA LOS PRIMEROS 10 SCORES MAS ALTOS
        private Comparator<ScoreData> scoreComparator; //COMPARADOR DE SCORES, ES UNA INTERFAZ
	
	private ScoreData[] auxArray; //ARREGLO AUXILIAR QUE PERMITE NO SOLO ACCEDER AL HIGH SCORE SINO A CUALQUIERA DE LOS 10
	
	public ScoreState() {
		returnButton = new Button(
				Assets.greyBtn,
				Assets.blueBtn,
				Assets.greyBtn.getHeight(),
				Constants.HEIGHT - Assets.greyBtn.getHeight() * 2,
				Constants.RETURN,
				new Action() {
					@Override
					public void doAction() {
						State.changeState(new MenuState());
					}
				}
			);
		
		scoreComparator = new Comparator<ScoreData>() {
			@Override
			public int compare(ScoreData e1, ScoreData e2) {
				return e1.getScore() < e2.getScore() ? -1: e1.getScore() > e2.getScore() ? 1: 0;
                        }//
		};
		
		highScores = new PriorityQueue<ScoreData>(10, scoreComparator); //SE INCIALIZA highScores, LA CAPACIDAD INICIAL Y EL COMPARADOR
                //highScores.add(new ScoreData(1000)); PRUEBA DE ASIGNAR 1000 COMO PUNTAJE MAYOR 
                
                try{
                    ArrayList<ScoreData> dataList = JSONParser.readFile();
                    for(ScoreData d : dataList){
                        highScores.add(d);
                    }
                    //SOLO QUIERO ALMACENAR LOS MEJORES 10 PUNTAJES:
                    while(highScores.size() > 10){
                        //REMOVER LA CABEZA DEL COMPARADOR (DATO MENOR)
                        highScores.poll(); //METODO QUE DEVUELVE Y REMUEVE DE LA CABEZA
                    }
                    
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
	}
	
	@Override
	public void update(float dt) {
		returnButton.update();
	}

	@Override
	public void draw(Graphics g) {
		returnButton.draw(g);
		
		auxArray = highScores.toArray(new ScoreData[highScores.size()]); //PASAR LA COLA CON PRIORIDAD A UN ARREGLO NORMAL		
		Arrays.sort(auxArray, scoreComparator); //ORDENAMOS EL ARREGLO USANDO EL MISMO COMPARADOR CREADO
		
		
		Vector2D scorePos = new Vector2D(
				Constants.WIDTH / 2 - 200,
				100
				);
		Vector2D datePos = new Vector2D(
				Constants.WIDTH / 2 + 200,
				100
				);
		
		Text.drawText(g, Constants.SCORE, scorePos, true, Color.BLUE, Assets.fontBig); //DIBUJAR EN SCORES EL PUNTAJE
		Text.drawText(g, Constants.DATE, datePos, true, Color.BLUE, Assets.fontBig); //DIBUJAR TEXTO PARA FECHA
		
		scorePos.setY(scorePos.getY() + 40); //DIBUJAR UN PUNTAJE CADA 40 PIXELES
		datePos.setY(datePos.getY() + 40);
		
		for(int i = auxArray.length - 1; i > -1; i--) {//ITERAR EL ARREGLO AUXILIAR AL REVÉS PARA DESPLEGAR LOS PUNTAJES MAYORES
			
			ScoreData d = auxArray[i];
			
			Text.drawText(g, Integer.toString(d.getScore()), scorePos, true, Color.WHITE, Assets.fontMed);
			Text.drawText(g, d.getDate(), datePos, true, Color.WHITE, Assets.fontMed);
			
			scorePos.setY(scorePos.getY() + 40);
			datePos.setY(datePos.getY() + 40);
			
		}
		
	}
	
}
