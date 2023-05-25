package Strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * La classe permet d'implementer l'algorithme EXPLORATION
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class RandomStrategy extends Strategy{
		
	// la liste points possibles à jouer  
	private ArrayList<Point> listPointsMove;

	// constructeur
	public RandomStrategy(ArrayList<Point> listPointsMove){
		this.listPointsMove = listPointsMove;
	}
	
	// methode permet d'excuter l'algo aleatoire
	@Override
	public int executeAlgo() {
		Random r = new Random();
		return r.nextInt(this.listPointsMove.size()) ;
	}
}
