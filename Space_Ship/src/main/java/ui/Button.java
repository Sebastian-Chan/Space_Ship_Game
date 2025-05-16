package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Graphics.Assets;
import Graphics.Text;
import input.MouseInput;
import math.Vector2D;

public class Button {
	
	private BufferedImage mouseOutImg; //SOBRE LA IMÁGEN
	private BufferedImage mouseInImg;
	private boolean mouseIn; //SABER SI EL MOUSE ESTÁ SOBRE EL BOTÓN
	private Rectangle boundingBox; //DETECTAR COLISIÓN ENTRE EL MOUSE Y EL BOTÓN
	private Action action;
	private String text;
	
	public Button(
			BufferedImage mouseOutImg,
			BufferedImage mouseInImg,
			int x, int y,
			String text,
			Action action
			) {
		this.mouseInImg = mouseInImg;
		this.mouseOutImg = mouseOutImg;
		this.text = text;
		boundingBox = new Rectangle(x, y, mouseInImg.getWidth(), mouseInImg.getHeight());
		this.action = action;
	}
	
	public void update() { //LOS BOTONES TAMBIÉN CAMBIAN DE ESTADO
		
		if(boundingBox.contains(MouseInput.X, MouseInput.Y)) { //PREGUNTAR SI LAS COORDENADAS DEL MOUSE ESTÁN DENTRO DEL BOTÓN O NO
			mouseIn = true;
		}else {
			mouseIn = false;
		}
		
		if(mouseIn && MouseInput.MLB) { //SI EL MOUSE ESTA EN EL BOTÓN Y ADEMÁS LO ESTOY PRESIONANDO 
			action.doAction();
		}
	}
	
	public void draw(Graphics g) {
		
		if(mouseIn) {
			g.drawImage(mouseInImg, boundingBox.x, boundingBox.y, null);
		}else {
			g.drawImage(mouseOutImg, boundingBox.x, boundingBox.y, null);
		}
		
		Text.drawText( //DIBUJAR TEXTO
				g,
				text,
				new Vector2D(
						boundingBox.getX() + boundingBox.getWidth() / 2,
						boundingBox.getY() + boundingBox.getHeight()),
				true,
				Color.BLACK,
				Assets.fontMed);
		
		
	}
	
}
