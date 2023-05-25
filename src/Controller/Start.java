package Controller;

import java.io.IOException;

/**
 * La classe permet de commencer le jeu
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class Start extends Thread{
	
	// pour creer un noueau jeu 
	public static boolean newPlay;
	// pour la partie du jeu 
	private Play play;
	
	// __construct
	public Start() {
		this.play = new Play(this);
		newPlay = false;
	}
	
	// pour commencer une nouvelle partie 
	@SuppressWarnings("deprecation")
	public void newGame() throws InterruptedException, IOException {
		
		// fermer la fenetre d'affichage 
		play.getControllerbordihm().dispose();
		this.stop();
		// creer une nouvelle partie
		Start.main(null);
	}
	
	// recuperer la partie en cours 
	public Play getPlay() {
		return this.play;
	}
	
	// setPlay
	public void setPlay(Play play) {
		this.play = play;
	}
	
	// methode pour commencer le jeu
	@Override
	public void run() {
		try {
			
			this.play.startGame();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// la fonction main de notre application
	public static void main(String[] args){
		// lancer le jeu
		Start start = new Start();
	    start.start();
	}	
}
