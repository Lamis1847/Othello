package IHM;

import java.awt.Dimension;
import javax.swing.JButton;

/**
 * La classe permet d'afficher chaque case
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class SquareIHM extends JButton {
	// Pour la sérialisation
	private static final long serialVersionUID = 1L;
	
	// une variable indique si la case est sélectionnée ou pas 
	private boolean ihmSelectedSquare = false;
	//private StateIHM ihmEtatSquare;
	
	// constructeur
	public SquareIHM() {
		super();
		// On definit la taille de chaque case
		this.setMaximumSize(new Dimension(20, 20)); 
		this.setMinimumSize(new Dimension(20, 20));
		this.setPreferredSize(new Dimension(20, 20));
	}

	// # getter et setter
	public boolean getSelectedSquare() {
		return ihmSelectedSquare;
	}

	public void setSelectedSquare(boolean selectedSquare) {
		this.ihmSelectedSquare = selectedSquare;
	}
}
