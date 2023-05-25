package Controller;


import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import IHM.BoardIHM;
import IHM.InitDialog;
import Model.Board;
import Model.Player;
import Model.Sound;
import Strategy.AlphaBetaStrategy;
import Strategy.MinimaxStrategy;
import Strategy.RandomStrategy;
import Model.Pawn;

/**
 * La classe pour creer une partie du jeu 
 * cette classe contient l'IHM et les deux joueurs et le bouard du jeu
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class Play {

	// la partie du jeu en cours 
	private Start start;
	// jouer avec le poin noir
	private Player ControllerplayerBlack;
	// jouer avec le poin blanc
	private Player ControllerplayerWhite;
	// plateau du jeu
	private Board  controllerplayBoard;
	// IHM du jeu 
	private BoardIHM controllerbordihm;
	// verifie si le joueur noir est bloqué
	private boolean isblockedBlack = false;
	// verifie si le joueur blanc est bloqué
	private boolean isblockedWhite = false;
	// le jeu en pause 
	public boolean pauseGame = false;
	// indique nouveau jeu
	public boolean playnewGame = false;
	// indique la fin du jeu
	public boolean playendGame = false;
	// nombre des coups
	private int nbrCoup = 0;
	
	// # variable pour l'ihm 
	// ihm playerPawn
	private Pawn playerPawn;
	// ihm levelGame
	private String levelGame;
	// ihm AlgoGame
	private String algoGame;
	// GameMade 
	private String gameMade;
	
	// constructeur
	public Play(Start start) {
		this.start = start;
	}
	
	// méthode permet de créer l'ihm et le plateu du jeu et les deux joueurs 
	public void startGame() throws InterruptedException, IOException {
		
		InitDialog id = new InitDialog(null, "Paramètres", true, this);
    	id.show(true);  	
    	
    	if(this.playerPawn != null){
    		
			// créer le plateau du jeu 
			controllerplayBoard = new Board();
			// l'affichage IHM 
			controllerbordihm = new BoardIHM(controllerplayBoard, this);
			setProertyBoardIHM();
			
			// creation des joueurs selon le choix 
			// joueur VS ordinateur
			if(controllerbordihm.getIhmGameMade() == "PL vs AI") {
				// joueur -> noir / ordinateur -> blanc
				if(controllerbordihm.getIhmPlayerPawn() == Pawn.BLACKState) {
					// creation du premier joueur 
					ControllerplayerBlack = new Player(Pawn.BLACKState, false);
					// creation du joueur ordinateur 
					ControllerplayerWhite = new Player(Pawn.WHITEState, true);
				} 
				// joueur -> balnc / orinateur -> noir
				else {
					// creation de l'ordinateur 
					ControllerplayerBlack = new Player(Pawn.BLACKState, true);
					// création du premier joueur 
					ControllerplayerWhite = new Player(Pawn.WHITEState, false);
				}	
			} 
			// AI vs AI
			else if (controllerbordihm.getIhmGameMade() == "AI vs AI") {
				// creation du joueur ordinateur 
				ControllerplayerBlack = new Player(Pawn.BLACKState, true);
				// création du premier joueur 
				ControllerplayerWhite = new Player(Pawn.WHITEState, true);
			}
			// PL vs PL
			else if (controllerbordihm.getIhmGameMade() == "PL vs PL") {
				// creation du premier joueur 
				ControllerplayerBlack = new Player(Pawn.BLACKState, false);
				// création du deuxieme joueur 
				ControllerplayerWhite = new Player(Pawn.WHITEState, false);
			}
			// les coups entre les deux joueurs 
			playersTurn();
    	}
	}
	
	// methode permet de mettre à jour les preprietés de la partie
	public void setProertyBoardIHM() {
		// le pion choisi par le joueur
		this.controllerbordihm.setIhmPlayerPawn(this.playerPawn);
		// le niveau de la partie
		this.controllerbordihm.getIhmLevelGame().setText(this.levelGame);
		// le mode du jeu
		this.controllerbordihm.setIhmGameMade(this.gameMade);
		// l'algorithme utilisé pour la partie
		this.controllerbordihm.getIhmAlgoGame().setText(this.algoGame);
	}
	
	// méthode permet d'alterner les tours entre les deux joueurs
	public void playersTurn() throws InterruptedException, IOException {
		nbrCoup = 0;
		int k;
		
		//Demarrer le chronometre
		controllerbordihm.getTimer().start();
		
		while(!controllerplayBoard.endGame() && (isblockedBlack == false || isblockedWhite == false )){		
			k = nbrCoup % 2;
			
			if(Start.newPlay == true) {
				break;
			}
			
			// mettre le jeu en pause
			if(pauseGame == true) pauseGame();
						
			if(k == 0) {
				
				controllerbordihm.getIhmTurnPlayer().setText("NOIR");
				blackPlayerTurn();
	
			}else {
				
				controllerbordihm.getIhmTurnPlayer().setText("BLANC");
				whitePlayerTurn();
			}
			// restaurer le temps 
			controllerbordihm.setSeconde(0);
			nbrCoup++;
		}
		
		
		if(controllerplayBoard.endGame() || (isblockedBlack == true && isblockedWhite == true )) {
			
			// indique la fin du jeu
			playendGame = true;
			
			if(ControllerplayerWhite.getScore() < ControllerplayerBlack.getScore()) {
				// afficher le message 
				stopGame("Le joueur Black a gagné", ControllerplayerBlack); 
				
			}else if(ControllerplayerWhite.getScore() > ControllerplayerBlack.getScore()) {
				// afficher le message
				stopGame("Le joueur White a gagné", ControllerplayerWhite);
			}else {
				// afficher le message
				stopGame("partie nulle", null);
			}
		}
		
		if(Start.newPlay == true) {
			start.newGame();
		}
		
	}
	
	// methode permet d'arreter le jeu et gerer le son de la fin du jeu
	public void stopGame(String msg, Player player) throws IOException {
		//arreter le chrono
		controllerbordihm.getTimer().stop();
		
		// son
		Sound sound;
		if(controllerbordihm.getIhmGameMade() == "PL vs AI"){
			// le son pour gagner
			if(!player.isIsComputer()) sound = new Sound("win");
			// le son pour perdre
			else sound = new Sound("lost");
		}
				
		// afficher le message
		controllerbordihm.ihmWiner(msg);		
	}
	
	// algorithme EXPLORATION 
	private int randomALG(ArrayList<Point> listPointsMove) {
		// choisir aléatoiremet une position
		RandomStrategy randomSTG = new RandomStrategy(listPointsMove);
		return randomSTG.executeAlgo();
	}
	
	// algorithme minimax
	private int minimaxALG(int difficulte, Player player, Player playerADV) {
		//choisir aléatoiremet une position minamax
		MinimaxStrategy minmaxSTG = new MinimaxStrategy(controllerplayBoard, difficulte, player, playerADV, this);
		minmaxSTG.executeAlgo();
		return minmaxSTG.bestMove;
	}
	
	// algorithme alphabeta
	private int alphabetaALG(int difficulte, Player player, Player playerADV) {
		//choisir aléatoiremet une position alphabeta
		AlphaBetaStrategy alphabetaSTG = new AlphaBetaStrategy(controllerplayBoard, difficulte, player, playerADV, this);
		alphabetaSTG.executeAlgo();
		return alphabetaSTG.bestMove;
	}
	
	// joueur noir
	private void blackPlayerTurn() throws InterruptedException {
		
		// if non computer 
		if(!ControllerplayerBlack.isIsComputer()) {
			// pour alterner entre deux joueurs
			if (controllerbordihm.getIhmGameMade() == "PL vs PL") controllerbordihm.setIhmPlayerPawn(Pawn.BLACKState);
			
			
			// indique que le joueur n'est pas en etat de blocage
			isblockedBlack = false; 
			
			// le joueur récupère les positions possibles du deplacement
			ArrayList<Point> listPointsBlackMove;
			listPointsBlackMove = ControllerplayerBlack.PlayerMove.listPointsMovePlayer(ControllerplayerBlack, this.controllerplayBoard);
				
			if(listPointsBlackMove.size() != 0) controllerbordihm.ihmUpdatePointsMove(listPointsBlackMove);
			else isblockedBlack = true;
			
		}else {
			
			// AI noir
			// AI récupere les positions possibles du deplacement
			ArrayList<Point> listPointsBlackMove;
			listPointsBlackMove = ControllerplayerBlack.PlayerMove.listPointsMovePlayer(ControllerplayerBlack, this.controllerplayBoard);

			if(listPointsBlackMove.size() != 0) {
				// indique que AI n'est pas en etat de blocage
				isblockedBlack = false;
				
				// indexe de la position
				int bestMove = 0;
				
				// niveau du jeu 
				if(controllerbordihm.getIhmAlgoGame().getText().toString() == "EXPLORATION") {
					// indice de la position
					bestMove = randomALG(listPointsBlackMove);
				}else if(((controllerbordihm.getIhmAlgoGame().getText().toString() == "MINIMAX")&&(controllerbordihm.getIhmGameMade() == "AI vs AI")) 
						|| ((controllerbordihm.getIhmAlgoGame().getText().toString() == "MINIMAX")&&(controllerbordihm.getIhmGameMade() != "AI vs AI"))) {
					// indice de la position
					bestMove = minimaxALG(controllerplayBoard.getDifficulte(controllerbordihm.getIhmLevelGame().getText()),
							new Player(ControllerplayerBlack), new Player(ControllerplayerWhite));
				}else if(controllerbordihm.getIhmAlgoGame().getText().toString() == "ALPHABETA") {
					// indice de la position
					bestMove = alphabetaALG(controllerplayBoard.getDifficulte(controllerbordihm.getIhmLevelGame().getText()),
							new Player(ControllerplayerBlack), new Player(ControllerplayerWhite));
				}
				
				// attendre 2 secondes
				Thread.sleep(2000) ;
				// son
				try {
    				// le son
					Sound s = new Sound("move");
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}				
				// effectuer le deplacement
				ControllerplayerBlack.PlayerMove.choiceMove(controllerplayBoard, 
						ControllerplayerBlack, 
						new Point(listPointsBlackMove.get(bestMove)));
				
				// changer les états du joueur adversaire
				ControllerplayerBlack.PlayerMove.chageStatesAdvrs(ControllerplayerBlack, 
						ControllerplayerWhite, new Point(listPointsBlackMove.get(bestMove )), 
						this.controllerplayBoard);				
				
				// changer le score
				controllerbordihm.ihmNewScore(ControllerplayerBlack, ControllerplayerWhite);	    				

			}else isblockedBlack = true;
		}
		
		if(isblockedBlack == false) {
	        
			while(!ControllerplayerBlack.getIsPlayed() && controllerbordihm.getSeconde() < 20) {
				// attendre que le joueur de choisit la position
				// modifier l'affichage et sortir de la boucle
				
				// nouveau jeu 
				if(Start.newPlay == true) break;
				
				// afficher le nouveau boardIHM
				controllerbordihm.ihmUpdateDisplay(this.controllerplayBoard);
				if(ControllerplayerBlack.isIsComputer()) {
					ControllerplayerBlack.setIsPlayed(true);
				}
			}
			
			// suprimer les icons des cases des deplacements possibles 
			if(!ControllerplayerBlack.isIsComputer()) {
				controllerbordihm.ihmdeletePointsMove();
			}

			// attendre le prochain coup
			ControllerplayerBlack.setIsPlayed(false);	
		}
	}
	
	// joueur blanc
	private void whitePlayerTurn() throws InterruptedException {
						
		// if non computer 
		if(!ControllerplayerWhite.isIsComputer()) {
			// pour alterner entre les deux joueurs
			if (controllerbordihm.getIhmGameMade() == "PL vs PL") controllerbordihm.setIhmPlayerPawn(Pawn.WHITEState);

			// indique que le joueur n'est pas en etat de blocage
			isblockedWhite = false; 
						
			// le joueur récupere les positions possibles du deplacement
			ArrayList<Point> listPointsWhiteMove;
			listPointsWhiteMove = ControllerplayerWhite.PlayerMove.listPointsMovePlayer(ControllerplayerWhite, this.controllerplayBoard);
			// mettre à jour l'ihm
			if(listPointsWhiteMove.size() != 0) controllerbordihm.ihmUpdatePointsMove(listPointsWhiteMove);
			else isblockedWhite = true;
			
		}else { 
			// AI Blanc
			// AI récupere les positions possible du deplacement
			ArrayList<Point> listPointsWhiteMove;
			listPointsWhiteMove = ControllerplayerWhite.PlayerMove.listPointsMovePlayer(ControllerplayerWhite, this.controllerplayBoard);
			
			if(listPointsWhiteMove.size() != 0) {
				// indique que AI n'est pas en etat de blocage
				isblockedWhite = false;
				
				// indexe de la position
				int bestMove = 0;
				
				// niveau du jeu 
				if(controllerbordihm.getIhmAlgoGame().getText().toString() == "EXPLORATION") {
					// indice de la position
					bestMove = randomALG(listPointsWhiteMove);
				}else if(controllerbordihm.getIhmAlgoGame().getText().toString() == "MINIMAX") {
					// indice de la position
					bestMove = minimaxALG(controllerplayBoard.getDifficulte(controllerbordihm.getIhmLevelGame().getText()), 
							new Player(ControllerplayerWhite), new Player(ControllerplayerBlack));
				}else if(((controllerbordihm.getIhmAlgoGame().getText().toString() == "ALPHABETA")&&(controllerbordihm.getIhmGameMade() == "AI vs AI"))
					|| ((controllerbordihm.getIhmAlgoGame().getText().toString() == "ALPHABETA")&&(controllerbordihm.getIhmGameMade() != "AI vs AI"))){
					// indice de la position
					bestMove = alphabetaALG(controllerplayBoard.getDifficulte(controllerbordihm.getIhmLevelGame().getText()), 
							new Player(ControllerplayerWhite), new Player(ControllerplayerBlack));
				}
				
				// attendre 2 scondes
				Thread.sleep(2000) ;
						
				// son
				try {
    				// le son pour chaque deplacement
					Sound s = new Sound("move");
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
				// effectuer le deplacement
				ControllerplayerWhite.PlayerMove.choiceMove(controllerplayBoard, 
						ControllerplayerWhite, new Point(listPointsWhiteMove.get(bestMove)));
				// changer les états du joueur adversaire
				ControllerplayerWhite.PlayerMove.chageStatesAdvrs(ControllerplayerWhite, 
						ControllerplayerBlack, new Point(listPointsWhiteMove.get(bestMove)), 
						this.controllerplayBoard);
								
				// changer le score
				controllerbordihm.ihmNewScore(ControllerplayerWhite, ControllerplayerBlack);
			
			}else isblockedWhite = true;	
		}

		if(isblockedWhite == false) {

			while(!ControllerplayerWhite.getIsPlayed() && controllerbordihm.getSeconde() < 20 ) {
				// attendre le joueur de choisir la position
				// modifier l'affichage et sortir de la boucle

				// bloquer les case
				// methode permet à l'ordinateur de jouer 
				// nouvelle partie 
				if(Start.newPlay == true) break;
				
				// indique que le joueur a joué son tour 
				// afficher le nouveau boardIHM
				controllerbordihm.ihmUpdateDisplay(this.controllerplayBoard);
				if(ControllerplayerWhite.isIsComputer()) {
					ControllerplayerWhite.setIsPlayed(true);
				}
			}
			
			// suprimer les icons des cases du deplacement possibles 
			if(!ControllerplayerWhite.isIsComputer()) {
				controllerbordihm.ihmdeletePointsMove();
			}
			// attendre le prochain coup
			ControllerplayerWhite.setIsPlayed(false);
		}
	}
	
	// mettre le jeu en pause 
	public int pauseGame() {

		this.start.suspend();
		int option;
		if(playnewGame == false) {
			String msg ="Voulez vous un résumé de la partie ?";
			
	       	String title ="Jeu en pause";
	       	JOptionPane jop = new JOptionPane();	
	       	
	       	UIManager.put("OptionPane.yesButtonText", "Oui");
	       	UIManager.put("OptionPane.noButtonText", "Non");

	        option = jop.showConfirmDialog(null, msg, title,
	       	        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}else {
			String msg ="Vous voulez commencer une nouvelle partie ? ";
	    	String title ="Nouvelle partie";
	    	JOptionPane jop = new JOptionPane();			
	    	UIManager.put("OptionPane.cancelButtonText", "Annuler");
	    	option = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		}
		
		this.start.resume();
       	return option;
	}
	
	// # l'ensemble des getter et setter
	// lire le nombre des coups
	public int getInbrCoup(){
		return nbrCoup;
	}

	public Player getControllerplayerBlack() {
		return ControllerplayerBlack;
	}

	public void setControllerplayerBlack(Player controllerplayerBlack) {
		ControllerplayerBlack = controllerplayerBlack;
	}

	public Player getControllerplayerWhite() {
		return ControllerplayerWhite;
	}

	public void setControllerplayerWhite(Player controllerplayerWhite) {
		ControllerplayerWhite = controllerplayerWhite;
	}

	public Board getControllerplayBoard() {
		return controllerplayBoard;
	}

	public void setControllerplayBoard(Board controllerplayBoard) {
		this.controllerplayBoard = controllerplayBoard;
	}

	public BoardIHM getControllerbordihm() {
		return controllerbordihm;
	}

	public void setControllerbordihm(BoardIHM controllerbordihm) {
		this.controllerbordihm = controllerbordihm;
	}

	public Pawn getPlayerPawn() {
		return playerPawn;
	}

	public void setPlayerPawn(Pawn playerPawn) {
		this.playerPawn = playerPawn;
	}

	public String getLevelGame() {
		return levelGame;
	}

	public void setLevelGame(String levelGame) {
		this.levelGame = levelGame;
	}

	public String getAlgoGame() {
		return algoGame;
	}

	public void setAlgoGame(String algoGame) {
		this.algoGame = algoGame;
	}

	public String getGameMade() {
		return gameMade;
	}

	public void setGameMade(String gameMade) {
		this.gameMade = gameMade;
	}

	public Start getStart() {
		return start;
	}

	public void setStart(Start start) {
		this.start = start;
	}
}
