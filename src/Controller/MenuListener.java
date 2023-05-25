package Controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * La classe permet de mettre les listener sur le menu du jeu
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class MenuListener implements MouseListener{

	// la partie
	private Play play;
	
	// constructeur
	public MenuListener(Play play) {
		this.play = play;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	// methode permet de gerer le menu choisi
	@Override
	public void mousePressed(MouseEvent e) {
		// le menu aide
		if(e.getSource() == this.play.getControllerbordihm().getIhmMenuHelp()) {
			try {
				
				// arreter le temps
				this.play.getControllerbordihm().getTimer().stop();
               // mettre le jeu en pause
				this.play.getStart().suspend();
	           // afficher l'aide  
               this.play.getControllerbordihm().ihmHelpfortheGame();
				
			} catch (IOException e1) {
				e1.printStackTrace();
				e1.getMessage();
			}
		}
		
		// le menu regles
		else if(e.getSource() == this.play.getControllerbordihm().getIhmMenuRules()) {
			try {
				// arreter le time
				this.play.getControllerbordihm().getTimer().stop();
               // mettre le jeu en pause
				this.play.getStart().suspend();
               // afficher les regles du jeu
               this.play.getControllerbordihm().ihmRulesoftheGame();
               
			} catch (IOException e1) {
				e1.printStackTrace();
				e1.getMessage();
			}
		}
		else if(e.getSource() == this.play.getControllerbordihm().getIhmMenuExemples()) {
			try {
				
				// arreter le temps
				this.play.getControllerbordihm().getTimer().stop();
               // mettre le jeu en pause
				this.play.getStart().suspend();
	           // afficher l'aide  
               this.play.getControllerbordihm().ihmExemplesfortheGame();
				
			} catch (IOException e1) {
				e1.printStackTrace();
				e1.getMessage();
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
}
