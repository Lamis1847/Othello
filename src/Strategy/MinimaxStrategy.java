package Strategy;

import java.awt.Point;
import java.util.ArrayList;

import Controller.Play;
import Model.Board;
import Model.Player;

/**
 * La classe permet d'implementer l'algorithme minimax
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class MinimaxStrategy extends Strategy{
	
	// la partie
	private Play play;
	
	// constructeur
	public MinimaxStrategy(Board boardC, int difficulte, Player player, Player playerADV, Play play){
		super(boardC, difficulte, player, playerADV);
		this.play = play;
	}
	
	// methode permet d'excuter l'algo
	@Override
	public int executeAlgo() {
		return minimax(player, this.board, 0, new Point());
	}
	
	// int fonction minimax (int depth)
	public int minimax(Player p, Board board, int depth, Point point) {
		
		// if (game over or depth = 0)
		if (board.endGame() || (depth == this.difficulte)) {
			
			//récuperer la mobilité : nombre de cases jouables
			ArrayList<Point> listPointsMove = p.PlayerMove.listPointsMovePlayer(p, board);
			//nombre de pions (matériel).
			int nbPawn = p.getScore();
			//  return winning score or eval();
			return eval(point, listPointsMove.size(), nbPawn, play);
		}
		
		int bestScore;
		
		// récuperer la liste des boards
		ArrayList<Point> listPointsMove = p.PlayerMove.listPointsMovePlayer(p, board);
		ArrayList<Board> diffBoardsWithPoints = listBoards(p, board, listPointsMove);
	
		// if (nœud == MAX) { 
		if (p.getPawn() == this.player.getPawn()) {
			// bestScore = -INFINITY;
			bestScore = Integer.MIN_VALUE;

			// for (each possible move m) {
			for (int i = 0; i < diffBoardsWithPoints.size(); i++) {

				if(depth == 0) this.bestMovedepth0 = i;
				// make move m;
				Board succBoard = diffBoardsWithPoints.get(i);
				// int score = minimax (depth - 1)
				int score = minimax(this.playerADV, succBoard, depth + 1, listPointsMove.get(i));		
				// unmake move m;
		        // if (score > bestScore)
				if (score > bestScore) {
					// bestScore = score;
					bestScore = score;
					// bestMove = m ;
					this.bestMove = this.bestMovedepth0;
				}
			}
		} 
		// else { //type MIN = adversaire
		else {
			//  bestScore = +INFINITY;
			bestScore = Integer.MAX_VALUE;
			//  for (each possible move m) {
			for (int i = 0; i < diffBoardsWithPoints.size(); i++) {
				// make move m;
				if(depth == 0) this.bestMovedepth0 = i;
				Board succBoard = diffBoardsWithPoints.get(i);
				// int score = minimax (depth - 1)
				int score = minimax(this.player, succBoard, depth + 1,listPointsMove.get(i));	

				// unmake move m;
				// if (score < bestScore) {   
				if (score < bestScore) {
					// bestScore = score;
					bestScore = score;
					// bestMove = m ;
					this.bestMove = this.bestMovedepth0;
				}
			}
		}
		return bestScore;
	}	
}
