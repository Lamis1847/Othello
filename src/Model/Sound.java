package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * La classe permet de modeliser le son
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class Sound {
	
	public Sound(String nameFile) throws FileNotFoundException {
		// Construction du flux audio à partir d'un fichier
		AudioInputStream audioInputStream = null;
		
		try{
		 	  //obtention d'un flux audio à partir d'un fichier 
		      audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/Son/"+nameFile+".wav"));

		    } catch (UnsupportedAudioFileException e1) {
		        	e1.printStackTrace();
		          return;
		    } catch (IOException e) {
		            e.printStackTrace();
		            return;
		    }
		
		// Récupération du format Audio
		//il est nécessaire de connaître le format audio du fichier d'entrée pour permettre à java de créer l'objet DataLine adéquat
		 AudioFormat audioFormat = audioInputStream.getFormat();
		 
		 // Construction d’un objet DataLine.Info
		 // ici le DataLine est un SourceDataLine qui nous permet la lecture (targetDataLine permet l'enregistrement).

	    DataLine.Info info = new DataLine.Info(SourceDataLine.class,
	                audioFormat);
	  
	    //  Récupération de l’objet Line
	    // On récupère le DataLine et on l'ouvre
	    SourceDataLine line;
	    try {
	    line = (SourceDataLine) AudioSystem.getLine(info);
	              
	    } catch (LineUnavailableException e2) {
	      e2.printStackTrace();
	      return;
	    }
	    
	    // Ouverture de la ligne avec le bon format audio
	    try {
	    	line.open(audioFormat);
	    } catch (LineUnavailableException e3) {
	    		e3.printStackTrace();
	    		return;
	    }
	    
	    //  Démarrage de la ligne
	    line.start();
	    
	    // Lecture du flux entrant et écriture sur la ligne
	    try {
	    	byte bytes[] = new byte[2048];
	    	int bytesRead=0;
	    	while (((bytesRead = audioInputStream.read(bytes, 0, bytes.length)) != -1)) {
	    		line.write(bytes, 0, bytesRead);
	    	}
	    } catch (IOException io) {
	    	io.printStackTrace();
	    	return;
	    }
	    
	    // Fermeture de la ligne
	    line.close();
	}	
}
