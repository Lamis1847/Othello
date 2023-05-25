package Model;

/**
 * La classe permet de modeliser les cases d'un plateau du jeu
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class Square {
	
	//l'etat de la case
	private State SquareState;

	//constrcteur 
	public Square(){
        this.SquareState = State.NONEState;
    }
	
	// modifier l'etat de la case
    public void setSquareState(State squareState){
        this.SquareState = squareState;
    }
    
    // recuperer l'etat actual de la case
    public State getSquareState(){
        return this.SquareState;
    } 
    
    // savoir si la case est libre ou pas
    public boolean isFREE(Square square){
        return square.SquareState == State.NONEState;
    }
}








