package states;

import java.awt.Graphics;

public abstract class State {
	
	private static State currentState = null;
	
	public static State getCurrentState() {return currentState;}
	public static void changeState(State newState) { //QUE CAMBIE DE ESTADO CADA VEZ QUE PRESIONE JUGAR
		currentState = newState;
	}
	
	
	public abstract void update(float dt);
	public abstract void draw(Graphics g);
	
}
