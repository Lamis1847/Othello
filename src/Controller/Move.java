package Controller;

import java.awt.Point;
import java.util.ArrayList;

import Model.Board;
import Model.Pawn;
import Model.Player;
import Model.State;

/**
 * La classe permet aux joueurs d'effectuer les mouvements
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class Move {
	
	// methode permet de récuperer les positions du joueur
	public ArrayList<Point> ControllerPointsPlayer(Player player, Board board){
		ArrayList<Point> pointsPlayer = new ArrayList<Point>();
		
		for(int i = 0; i < Board.modelBoardSize; i++) {
			for(int j = 0; j < Board.modelBoardSize; j++) {
				if(player.getPawn().toString() == board.modelBoardSqures[i][j].getSquareState().toString()) {
					pointsPlayer.add(new Point(i, j));
					
				}
			}
		}
		
		return pointsPlayer;
	}
	
	// methode permet de récuperer la liste des positions possibles du deplacement 
	public ArrayList<Point> listPointsMovePlayer(Player player, Board board){
		
		ArrayList<Point> listPointsPlayer = new ArrayList<Point>();
		ArrayList<Point> listPointsMovePlayer = new ArrayList<Point>();
		ArrayList<Point> listALLPointsMovePlayer = new ArrayList<Point>();
		
		// récuperer les points des pions existants d'un joueur donné 
		listPointsPlayer = player.PlayerMove.ControllerPointsPlayer(player, board);
	
		// récuperer toutes les positions possibles pour un deplacement
		for(int i = 0; i < listPointsPlayer.size(); i++) {
			
			listPointsMovePlayer = player.PlayerMove.listPointValidMove(player, listPointsPlayer.get(i), board);
			if(listPointsMovePlayer != null) listALLPointsMovePlayer.addAll(listPointsMovePlayer);
		}

		return listALLPointsMovePlayer;
	}
	
	// methode permet de récuperer le pion adversaire
	public String getPawnAdvers(Pawn pawn){
		if (pawn == pawn.BLACKState)
			return pawn.WHITEState.toString();
		else if (pawn == pawn.WHITEState)
			return pawn.BLACKState.toString();
		return null;
	}
	
	// methode permet de récuperer les etats des 8 cases    
	public ArrayList<Point> listPointValidMove(Player player, Point position, Board board){
		Point pointDirectionvalid;
		ArrayList<Point> listAllDirection = new ArrayList<Point>();
		
		if (!isValidPosition(position))
			return listAllDirection;

		// Droit
		pointDirectionvalid = validPointInDirection(player, position, 1,  0, board);	
		if(pointDirectionvalid != null) listAllDirection.add(pointDirectionvalid);
		// Gauche
		
		pointDirectionvalid = validPointInDirection(player, position, -1,  0, board);
		if(pointDirectionvalid != null) listAllDirection.add(pointDirectionvalid);
		
		// Haut
		pointDirectionvalid = validPointInDirection(player, position, 0,  1, board);	
		if(pointDirectionvalid != null) listAllDirection.add(pointDirectionvalid);
		
		// Bas
		pointDirectionvalid = validPointInDirection(player, position, 0,  -1, board);	
		if(pointDirectionvalid != null) listAllDirection.add(pointDirectionvalid);
		
		// NordEast
		pointDirectionvalid = validPointInDirection(player, position, 1,  1, board);	
		if(pointDirectionvalid != null) listAllDirection.add(pointDirectionvalid);
		
		//NordWest
		pointDirectionvalid = validPointInDirection(player, position, 1,  -1, board);	
		if(pointDirectionvalid != null) listAllDirection.add(pointDirectionvalid);
		
		// SudEast
		pointDirectionvalid = validPointInDirection(player, position, -1,  1, board);	
		if(pointDirectionvalid != null) listAllDirection.add(pointDirectionvalid);
		
		// SudWest
		pointDirectionvalid = validPointInDirection(player, position, -1,  -1, board);
		if(pointDirectionvalid != null) listAllDirection.add(pointDirectionvalid);
		
		return listAllDirection;
		
	}
		
	// methode permet de verifier si on est toujours dans le board 
	public boolean isValidPosition(Point pos){
		return (pos.x >= 0 && pos.y >= 0 && pos.x < Board.modelBoardSize && pos.y < Board.modelBoardSize);
	}
	
	// methode permet de récuperer la couleur de la case 
	public String StatePosition(Point pos, Board board){
		return board.modelBoardSqures[pos.x][pos.y].getSquareState().toString();
	}
	
	// methode permet de valider les points pour chaque direction
	public Point validPointInDirection (Player player, Point position, int xDirection, int yDirection, Board board){
		Point pos = new Point(position);
		
		// récuperer le pion adversaire 
		String pawnAdv = getPawnAdvers(player.getPawn());
	
		pos.x += xDirection;
		pos.y += yDirection;
		
		if (!isValidPosition(pos) || StatePosition(pos, board) != pawnAdv)
			return null;
				
		pos.x += xDirection;
		pos.y += yDirection;
				
		while(isValidPosition(pos)){
			if (StatePosition(pos, board) == player.getPawn().toString())
				return null;
			else if (StatePosition(pos, board) == State.NONEState.toString())
				return pos;
			
			pos.x += xDirection;
			pos.y += yDirection;
		}
		return null;
	}
	
	// methode permet de choisir le deplacement 
	public void choiceMove(Board board, Player player, Point position) {
		
		if(isValidPosition(position) && board.modelBoardSqures[position.x][position.y].getSquareState() == State.NONEState) {
			board.modelBoardSqures[position.x][position.y].setSquareState(Board.convertPawntoState(player.getPawn()));
		}
	}
	
	// methode permet de changer les états du joueur adversaire     
	public boolean chageStatesAdvrs(Player player,Player playeradv,  Point position, Board board){
		
		ArrayList<Point> listAllPoints = new ArrayList<Point>();
		
		if (!isValidPosition(position))
			return false;

		// Droit
		listAllPoints = listSquarsAdvrs(player, position, 1,  0, board);	
		if(listAllPoints != null) changeState(player, playeradv, listAllPoints, board);
		
		// Gauche
		listAllPoints = listSquarsAdvrs(player, position, -1,  0, board);
		if(listAllPoints != null)  changeState(player, playeradv, listAllPoints, board);
		
		// Haut
		listAllPoints = listSquarsAdvrs(player, position, 0,  1, board);	
		if(listAllPoints != null)  changeState(player, playeradv, listAllPoints, board);
		
		// Bas
		listAllPoints = listSquarsAdvrs(player, position, 0,  -1, board);	
		if(listAllPoints!= null)  changeState(player, playeradv, listAllPoints, board);
		
		// NordEast
		listAllPoints = listSquarsAdvrs(player, position, 1,  1, board);	
		if(listAllPoints != null)  changeState(player, playeradv, listAllPoints, board);
		
		// NordWest
		listAllPoints = listSquarsAdvrs(player, position, 1,  -1, board);	
		if(listAllPoints != null)  changeState(player, playeradv, listAllPoints, board);
		
		// SudEast
		listAllPoints = listSquarsAdvrs(player, position, -1,  1, board);	
		if(listAllPoints != null)  changeState(player, playeradv, listAllPoints, board);
		
		// SudWest
		listAllPoints = listSquarsAdvrs(player, position, -1,  -1, board);
		if(listAllPoints!= null)  changeState(player, playeradv, listAllPoints, board);
			
		return true;
	}
	
	// methode permet de changer l'etat d'un ensemble des cases
	public void changeState(Player player, Player playeradv, ArrayList<Point> listpoints, Board board) {

		for(int i=0; i< listpoints.size(); i++){
			if(isValidPosition(listpoints.get(i)) && board.modelBoardSqures[listpoints.get(i).x][listpoints.get(i).y].getSquareState().toString() == getPawnAdvers(player.getPawn())) {
				// augmenter le score 
				player.setScore(player.getScore() + 1);
				// diminuer le score de l'adversaire 
				playeradv.setScore(playeradv.getScore() - 1);

				board.modelBoardSqures[listpoints.get(i).x][listpoints.get(i).y].setSquareState(Board.convertPawntoState(player.getPawn()));
			}
		}
	}
		
	// methode permet de recuperer la liste des cases occupées par le joueur adversaire
	public ArrayList<Point> listSquarsAdvrs(Player player, Point position, int xDirection, int yDirection, Board board){
		ArrayList<Point> listAllPoints = new ArrayList<Point>();
		
		Point pos = new Point(position);
		
		// récuperer le pion adversaire 
		String pawnAdv = getPawnAdvers(player.getPawn());

		pos.x += xDirection;
		pos.y += yDirection;
		
		if (!isValidPosition(pos) || StatePosition(pos, board) != pawnAdv)
		return null;
		
		listAllPoints.add(new Point(pos));

		pos.x += xDirection;
		pos.y += yDirection;
		
		while(isValidPosition(pos)){
			if (StatePosition(pos, board) == player.getPawn().toString()) {
				
				return listAllPoints;
			}
			else if (StatePosition(pos, board) == State.NONEState.toString())
				return null;
			
			listAllPoints.add(new Point(pos));
			
			pos.x += xDirection;
			pos.y += yDirection;
		}
		return null;			
	}
}
