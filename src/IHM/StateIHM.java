package IHM;

import javax.swing.ImageIcon;

/**
 * La classe permet d'afficher l'etat de chaque case
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class StateIHM {
	// Pour la sérialisation
    private static final long serialVersionUID = 1L;
    
    // une variable pour chaque type d'icone : noire, blanche, icone du coup valide
    public static ImageIcon ihmWHITEState = new ImageIcon(StateIHM.class.getResource("/Images/iconWHITE.png")), 
                            ihmBLACKState = new ImageIcon(StateIHM.class.getResource("/Images/iconBLACK.png")),
                            ihmChoiseState = new ImageIcon(StateIHM.class.getResource("/Images/iconOK.png"));

}
