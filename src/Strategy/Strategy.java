package Strategy;

import java.awt.Point;
import java.util.ArrayList;

import Controller.Play;
import Model.Board;
import Model.Player;

/**
 * La classe abstract permet d'implementer la strategie du jeu et la fonction d'evaluation
 * M1 info Rouen (2022/2023)
 * Othello
 */
public abstract class Strategy {
	
	// le plateau du jeu
	protected Board board;
	// la difficulté du jeu 
	protected int difficulte ;
	// joueur 
	protected Player player;
	// playerADV 
	protected Player playerADV;
	// bestMove
	public int bestMove = 0;
	// 
	protected int bestMovedepth0;
	
	// constructeur sans paramètres
	public Strategy(){
	}
	
	// constructeur avec paramètres
	public Strategy(Board boardC, int difficulte, Player player, Player playerADV) {
		board = new Board(boardC);
		this.difficulte = difficulte;
		this.player = player;
		this.playerADV = playerADV;
	}

	// methode permet d'exécuter les differents algos
	public abstract int executeAlgo();
	
	// recuperer les board de differents positions possibles 
	public ArrayList<Board> listBoards(Player p, 
		                              Board board, 
		                              ArrayList<Point> listPointsMove) {
		// la liste des boards
		ArrayList<Board> boards = new ArrayList<>();
		
		for (Point movePoint : listPointsMove) {
			Board resultingBoard = new Board(board);
			
			if(p.getPawn() == player.getPawn()) {
							
				// effectuer le mouvement
				player.PlayerMove.choiceMove(resultingBoard, player, new Point(movePoint));
				// changer les etats du joueur adversaire
				player.PlayerMove.chageStatesAdvrs(player, playerADV, new Point(movePoint), resultingBoard);
				
				boards.add(resultingBoard);
			}else {
				// effectuer le mouvement
				playerADV.PlayerMove.choiceMove(resultingBoard, playerADV, new Point(movePoint));
				// changer les etats du joueur adversaire
				playerADV.PlayerMove.chageStatesAdvrs(playerADV, player, new Point(movePoint), resultingBoard);
				
				boards.add(resultingBoard);
			}
		}
		return boards;
	}
	
	// methode permet d'evaluer chaque position
	public int eval(Point powerPoint, int mobilite, int materiel, Play play) {
		int [][] evalPowerPoint;
		
		// table d'evaluation selon les criteres de la force de position
		evalPowerPoint = new int[][]
	            {
					{ 500, -150, 30, 10, 10, 30, -150,  500},
					
					{-150, -250,  0,  0,  0,  0, -250, -150},
					
					{ 30,     0,  1,  2,  2,  1,    0,   30},
					
					{ 10,     0,  2, 16, 16,  2,    0,   10},
					
					{ 10,     0,  2, 16, 16,  2,    0,   10},
					
					{ 30,     0,  1,  2,  2,  1,    0,   30},
					
					{-150, -250,  0,  0,  0,  1, -250, -150},
					
					{ 500, -150, 30, 10, 10, 30, -150,  500},
	            };
		
				// if nbrcoup <= 30 mobilite et position
				// if nbrcoup > 30 et <= 55  mobilite et position et materiel
				// if nbrcoup > 55 materiel
				if(play.getInbrCoup() <= 30) 
					return evalPowerPoint[powerPoint.x][powerPoint.y] + mobilite;
				else if ((play.getInbrCoup() > 30) && (play.getInbrCoup() <= 56)) 
					return evalPowerPoint[powerPoint.x][powerPoint.y] + mobilite + materiel;
				else return materiel;
	}	
}
