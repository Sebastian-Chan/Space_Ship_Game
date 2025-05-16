package input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter{
	
	public static int X, Y;
	public static boolean MLB; //MOUSE LEFT BUTTON
	
	@Override
	public void mousePressed(MouseEvent e) { //CUANDO SE PRESIONA UNO DE LOS BOTONES DEL MOUSE
		if(e.getButton() == MouseEvent.BUTTON1) { //BUTTON 1 ES EL CLICK IZQUIERDO
			MLB = true; //SERA VERDADERO PORQUE SE PRESIONÓ
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {//CUANDO SE SUELTA EL MOUSE
		if(e.getButton() == MouseEvent.BUTTON1) {
			MLB = false;//CAMBIA A FALSO AL SOLTARSE
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {//RECIBE DATOS AL MOVER EL MOUSE MIENTRAS PRESIONA BOTONES
		X = e.getX();  //POSICIÓN DEL MOUSE ALMACENADA EN ESTAS VARIABLES 
		Y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) { //MOVER MOUSE
		X = e.getX();
		Y = e.getY();
	}
	
}
