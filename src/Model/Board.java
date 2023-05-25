package Model;

/**
 * La classe permet de modeliser le plateau du jeu
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class Board {

	// la taille du plateau
	public static int modelBoardSize = 8;
	// la matrice des cases
	public Square[][] modelBoardSqures = new Square[modelBoardSize][modelBoardSize];
	
	// constructeur sans paramètres
	public Board(){  
	    for (int i=0; i<modelBoardSize; i++)
	    {
	      for (int j=0; j<modelBoardSize; j++)
	      {
	    	  modelBoardSqures[i][j] = new Square();
	      }
	    }
	    //initialiser la case 3-3 a blanc
	  modelBoardSqures[3][4].setSquareState(State.WHITEState);
	    //initialiser la case 3-4 a noir
	  modelBoardSqures[3][3].setSquareState(State.BLACKState);
	    //initialiser la case 4-3 a noir
	  modelBoardSqures[4][4].setSquareState(State.BLACKState);
	    //initialiser la case 4-4 a blanc
	  modelBoardSqures[4][3].setSquareState(State.WHITEState);
	    
	}
	
	// constructeur avec paramètres
	public Board(Board b){ 
		
	    for (int i=0; i<modelBoardSize; i++){
	      for (int j=0; j<modelBoardSize; j++){
	    	  if(b.modelBoardSqures[i][j].getSquareState() == State.BLACKState) {
	    		  modelBoardSqures[i][j] = new Square();
	    		 this.modelBoardSqures[i][j].setSquareState(State.BLACKState);
	    	  }else if(b.modelBoardSqures[i][j].getSquareState() == State.NONEState) {
	    		  modelBoardSqures[i][j] = new Square();
	    		  this.modelBoardSqures[i][j].setSquareState(State.NONEState);
	    	  }else if(b.modelBoardSqures[i][j].getSquareState() == State.WHITEState) {
	    		  modelBoardSqures[i][j] = new Square();
	    		  this.modelBoardSqures[i][j].setSquareState(State.WHITEState);
	    	  }
	      }
	    }	    
	}
	
	// conversion de Pawn vers State 
	public static State convertPawntoState(Pawn pawn) {
		if(pawn == Pawn.BLACKState) {
			return State.BLACKState;
		}else if(pawn == Pawn.WHITEState) {
			return State.WHITEState;
		}else return State.NONEState; 
	}
	
	// conversion de State vers Pawn
	public static Pawn convertStatetoPawn(State state) {
		if(state == State.BLACKState) {
			return Pawn.BLACKState;
		}else if(state == State.WHITEState) {
			return Pawn.WHITEState;
		}else return Pawn.NONEState; 
	}
	
	// conversion de String vers Pawn
	public static Pawn convertStringtoPawn(String string) {
		if(string == "NOIR") {
			return Pawn.BLACKState;
		}else if(string == "BLANC") {
			return Pawn.WHITEState;
		}else return Pawn.NONEState; 
	}
	
	// methode permet de recuperer la profondeur à utiliser selon la difficulté de la partie
	public int getDifficulte(String difficult) {
		
		if(difficult == "FACILE") {
			return 1;
		}else if(difficult == "INTERMEDIAIRE"){
			return 2;
		}else if(difficult == "DIFFICILE"){
			return 4;
		}
		return 0;
	}
	
	// méthode permet de verifier si le jeu est fini ou non
	public boolean endGame(){
		Boolean existCaseNULL = false;
		
		for(int i = 0; i < Board.modelBoardSize; i++ ) {
			for(int j = 0; j < Board.modelBoardSize; j++ ) {
				
				if(modelBoardSqures[i][j].getSquareState() == State.NONEState) {
					existCaseNULL  = true;
				}
			}
		}
		
		if( existCaseNULL == false ) {
			return true ;
		}else return false ;
	}
}

