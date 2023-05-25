package IHM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import Controller.Play;
import Model.Board;
import Model.Pawn;

/**
 * La classe permet d'afficher la fenetre du paramétrage au début
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class InitDialog extends JDialog  {

	  // un label pour pion, niveau, mode, et l'algo du jeu
	  private JLabel  pawnLabel, levelLabel, madeLabel, algoLabel;
	  private JRadioButton tranche1, tranche2, tranche3, tranche4;
	  private JComboBox pawn, level, mode, algo;
	  private JTextField taille;
	  // la partie 
	  private Play play;

	  // constructeur
	  public InitDialog(JFrame parent, String title, boolean modal, Play play){
	    super(parent, title, modal);
	    
	    this.play = play;
	    this.setSize(500, 220);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    this.initComponent();
	  }

	  // methode permet d'initialiser le dialog
	  private void initComponent(){
	    
		  // choix du pion
	    JPanel panPawn = new JPanel();
	    panPawn.setBackground(Color.blue);
	    panPawn.setPreferredSize(new Dimension(220, 60));
	    TitledBorder titledBorder = BorderFactory.createTitledBorder("Jouer avec");
	    titledBorder.setTitleColor(Color.white);
	    panPawn.setBorder(titledBorder);
	  
	    panPawn.setForeground(Color.RED);
	    pawn = new JComboBox();
	    pawn.addItem("NOIR");
	    pawn.addItem("BLANC");
	    pawnLabel = new JLabel("Pion : ");
	    panPawn.add(pawnLabel);
	    panPawn.add(pawn);
	    
	    pawnLabel.setForeground(Color.white);

	    // choix du niveau du jeu
	    JPanel panLevel = new JPanel();
	    panLevel.setBackground(Color.blue);
	    panLevel.setPreferredSize(new Dimension(220, 60));
		    
	    TitledBorder niveaudBorder = BorderFactory.createTitledBorder("Niveau de difficulté");
	    niveaudBorder.setTitleColor(Color.white);
	    panLevel.setBorder(niveaudBorder);
	    
	    level = new JComboBox();
	    level.addItem("FACILE");
	    level.addItem("INTERMEDIAIRE");
	    level.addItem("DIFFICILE");
	    levelLabel = new JLabel("Niveau : ");
	    panLevel.add(levelLabel);
	    panLevel.add(level);
	    levelLabel.setForeground(Color.white);
	    // choix du mode du jeu
	    JPanel panMade = new JPanel();
	    panMade.setBackground(Color.blue);
	    panMade.setPreferredSize(new Dimension(220, 60));
	    
	    TitledBorder modeBorder = BorderFactory.createTitledBorder("Mode");
	    modeBorder.setTitleColor(Color.white);
	    panMade.setBorder(modeBorder);
	    
	    mode = new JComboBox();
	    mode.addItem("PL vs AI");
	    mode.addItem("AI vs AI");
	    mode.addItem("PL vs PL");
	    madeLabel = new JLabel("Mode : ");
	    panMade.add(madeLabel);
	    panMade.add(mode);
	    madeLabel.setForeground(Color.white);

	    // choix de l'algorithme
	    JPanel panAlgo = new JPanel();
	    panAlgo.setBackground(Color.blue);
	    panAlgo.setPreferredSize(new Dimension(220, 60));
	    
	    TitledBorder algoBorder = BorderFactory.createTitledBorder("L'algorithme");
	    algoBorder.setTitleColor(Color.white);
	    panAlgo.setBorder(algoBorder);
	    
	    algo = new JComboBox();
	    algo.addItem("EXPLORATION");
	    algo.addItem("MINIMAX");
	    algo.addItem("ALPHABETA");
	    algoLabel = new JLabel("Algo : ");
	    panAlgo.add(algoLabel);
	    panAlgo.add(algo);
	    algoLabel.setForeground(Color.white);
	    
	    JPanel content = new JPanel();
	    content.setBackground(Color.white);
	    content.add(panPawn);
	    content.add(panMade);
	    content.add(panAlgo);
	    content.add(panLevel);
	    
	    JPanel control = new JPanel();
	    JButton okBouton = new JButton("JOUER");
	    
	    // le boutton ok 
	    okBouton.addActionListener(new ActionListener(){
	    
	    // listener pour le boutton ok
		public void actionPerformed(ActionEvent e) {
				
			// le joueur choisit le pion qu'il veut jouer avec
	    	Pawn Cpawn = Board.convertStringtoPawn(pawn.getSelectedItem().toString());
	    	if (Cpawn == null) {
	    		// récuperer le pion 
				play.setPlayerPawn(Pawn.BLACKState);
	    	}else{
	    		
				play.setPlayerPawn(Cpawn);
	    	};
	    	
	    	// le joueur choisit le niveau du jeu
	    	String CLevel = level.getSelectedItem().toString();
	    	if (CLevel == null) {
	    		// récuperer le niveau de difficulté
	    		play.setLevelGame("FACILE");
	    	}else{
	    		
	    		play.setLevelGame(CLevel);
	    	};
	    	
	    	// le joueur choisit l'algorithme
	    	String CAlgo = algo.getSelectedItem().toString();
	    	if (CAlgo == null) {
	    		// récuperer l'algo
	    		play.setAlgoGame("EXPLORATION");
	    	}else{
	  
	    		play.setAlgoGame(CAlgo);
	    	};
	    				
	    	// le joueur choisit le mode du jeu 
	    	String Cmade = mode.getSelectedItem().toString();
	    	if (Cmade == null) {
	    		// récuperer le mode
	    		play.setGameMade("PL VS AI");
	    	}else{
	    		// récuperer le mode
	    		play.setGameMade(Cmade);
	    	};
			
			setVisible(false);
		}      
	    });

	    // le boutton retour
	    JButton cancelBouton = new JButton("RETOUR");
	    // listener pour le boutton retour
	    cancelBouton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent e) {
		    	setVisible(false);
		    	System.exit(0);
	      }      
	    });

	    control.add(okBouton);
	    control.add(cancelBouton);
	    this.getContentPane().add(content, BorderLayout.CENTER);
	    this.getContentPane().add(control, BorderLayout.SOUTH);
	  }

	// l'ensemble des getter et setter
	public Play getPlay() {
		return play;
	}

	public void setPlay(Play play) {
		this.play = play;
	}  
}