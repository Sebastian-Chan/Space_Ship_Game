package Graphics;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
	
	private Clip clip;
	private FloatControl volume;//ONTROLAR VOLUMEN DE SONIDOS
	
	public Sound(Clip clip) {
		this.clip = clip;
		volume = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
	}
	
	public void play() {//COMENZAR SONIDO
		clip.setFramePosition(0); //PONERLO EN SU POSICION INICIAL
		clip.start();
	}
	
	public void loop() { //LOOP PARA MUSICA DE FONDO
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() { //METODO PARA DETENER SONIDO
		clip.stop();
	}
	
	public int getFramePosition() {//POSICIÓN EN LA QUE VAYA EL SONIDO
		return clip.getFramePosition();
	}
	
	public void changeVolume(float value) {
		volume.setValue(value);
	}
	
}
