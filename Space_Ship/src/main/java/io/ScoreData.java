package io;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreData { //ENTRADA Y SALIDA DE DATOS
	
	private String date; //ALMACENA LA FECHA DEL PUNTAJE
	private int score;
	
	public ScoreData(int score) {//SCORE COMO PARAMETRO QUE SE LOGRO EN LA PARTIDA
		this.score = score;
		
		Date today = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		date = format.format(today);
		
	}
	
	public ScoreData() {
		
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
	
}
