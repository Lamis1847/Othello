package IHM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;

import Controller.BoardListener;
import Controller.MenuListener;
import Controller.Play;
import Controller.Start;
import Model.Board;
import Model.Pawn;
import Model.Player;
import Model.State;

/**
 * La classe permet d'afficher la fenetre et le plateu du jeu (IHM)
 * M1 info Rouen (2022/2023)
 * Othello
 */
public class BoardIHM extends JFrame {

    // #sérial 
	private static final long serialVersionUID = 1L;
	// #chrono 
	public ActionListener tacheTIMER;
	private int delais = 1000;
    private int seconde = 0;
    private Timer timer;
    private JLabel time = new JLabel("00:00");
	// #combo
	// la boutton combo
	private Pawn ihmPlayerPawn;
	private String ihmGameMade;
	
	
	// #panneau de footer
    // déclaration du panel 
    private Box ihmPanelFooter    = Box.createHorizontalBox();
    private JLabel ihmLabelFooter = new JLabel("Othello - THEORIE DES JEUX - M1 INFO");

    //  
    // Label du tour du joueur
    private JLabel ihmTurnPlayer = new JLabel("");
    
    // #panneau du temps et scores 
    // déclaration  du panel 
    private Box ihmPanelScoreTime = Box.createVerticalBox();
    // label de l'algorithme
    private JLabel ihmAlgoGame = new JLabel("");
    // Label de niveau
    private JLabel ihmLevelGame = new JLabel("");
    // Label du score 
    private JLabel ihmScorePlayerBlack = new JLabel("02");
    private JLabel ihmScorePlayerWhite = new JLabel("02");
    
    // #Menu 
    // menu principal contient le jeu, les règles et l'aide 
    private JMenuBar ihmMenuBar = new JMenuBar();
    private JMenu ihmMenuGame   = new JMenu("Jeu");
    private JMenu ihmMenuRules  = new JMenu("Règles");
    private JMenu ihmMenuHelp   = new JMenu("Aide");
    private JMenu ihmMenuExemples  = new JMenu("Exemples");
    // sous menu Game
    private JMenuItem ihmMenuNewGame     = new JMenuItem("Nouvelle partie");
    private JMenuItem ihmMenuPause       = new JMenuItem("Pause");
    private JMenuItem ihmMenuExit        = new JMenuItem("Quitter");
    // sous menu Level
    private JMenuItem ihmMenuLevelEasy   = new JMenuItem("FACILE");
    private JMenuItem ihmMenuLevelMedium = new JMenuItem("INTERMEDIAIRE");
    private JMenuItem ihmMenuLevelHaro   = new JMenuItem("DIFFICILE");
   
    // #plateau du jeu 
    // layout du panneau du plateau du jeu 
	private GridLayout ihmLayoutGame;
	// le panel du plateau de jeu 
	private  JPanel ihmPanelGame = new JPanel();
    // taille du plateau du jeu
    private int ihmSizePanelGame;
    // matrice des cases
    private SquareIHM [][] ihmBoardGame;
    
    private Play play;
    
    // #fenetre 
    // positionner la fenetre 
    private Dimension ihmScreen = Toolkit.getDefaultToolkit().getScreenSize();
    // la police
    private Font fonte = new Font("Poppins", Font.PLAIN, 4);
    
    // constructeur
    public BoardIHM(Board board, Play play) throws InterruptedException{
    	    	
    	// fermeture de la fenetre 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // play 
        this.play = play;
        
        // lancer le chronomètre
        chrono();
        
        // ajouter le layout 
        ihmLayoutGame = new GridLayout(Board.modelBoardSize, Board.modelBoardSize);
        this.ihmPanelGame.setLayout(ihmLayoutGame);
        // ajouter le border au panneau du jeu
        this.ihmPanelGame.setBorder(BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 156)));
        
        // la taille du plateau du jeu 
        this.ihmSizePanelGame = Board.modelBoardSize * 70 + 60; 
        this.ihmPanelGame.setPreferredSize(new Dimension(ihmSizePanelGame, ihmSizePanelGame));
        // creer le plateau des cases
        this.ihmBoardGame = new SquareIHM[Board.modelBoardSize][Board.modelBoardSize];

        // creation du panneau des score et time du jeu
        creatIhmPanelScoreTime();
        // ajouter le paneau à la fenetre 
        this.getContentPane().add(ihmPanelScoreTime, BorderLayout.EAST);
        
        // creation de l'interface du panneau du jeu
        creatIhmBoardGame(board);
        // ajouter le panneau à la fenetre 
        this.getContentPane().add(ihmPanelGame, BorderLayout.CENTER);  
        
        // creation du Menu du jeu
        createIhmMenu();        
        // ajouter le menu à la fenetre 
        this.getContentPane().add(ihmMenuBar, BorderLayout.NORTH);
        
        // ajouter le panneau de footer 
        creatIhmFooter();
        this.getContentPane().add(ihmPanelFooter, BorderLayout.SOUTH);
            
        // #fenetre 
        // titre de la fenetre
        this.setTitle("Othello");
        // fixation de la taille du panneau du jeu 
        this.ihmPanelGame.setMinimumSize(new Dimension(ihmSizePanelGame + 6, ihmSizePanelGame + 25)); 
        this.ihmPanelGame.setMaximumSize(new Dimension(ihmSizePanelGame + 6, ihmSizePanelGame + 25));
        
        // fixation de la taille du panneau du score & time
        this.ihmPanelScoreTime.setMinimumSize(new Dimension(125, ihmSizePanelGame + 25)); 
        this.ihmPanelScoreTime.setMaximumSize(new Dimension(125, ihmSizePanelGame + 25));
        this.ihmPanelScoreTime.setPreferredSize(new Dimension(125, ihmSizePanelGame + 25));
        
        // fixation de la taille de la fenetre 
        this.setMinimumSize(new Dimension(ihmSizePanelGame + 50, ihmSizePanelGame + 50));
        
        // visibilite de la fenetre et les panneaux
        this.ihmPanelGame.setVisible(true);

        //Stop Chrono;
        this.setVisible(true);
               
        // Centrer la fenetre du jeu paraport à l'écran
        this.setLocation((ihmScreen.width - this.getSize().width)/2,(ihmScreen.height - this.getSize().height)/2);
    }
    
    // lire l'aide du jeu
    public void ihmHelpfortheGame() throws IOException {

    	String helpText = "<html><body><p>Pour avoir plus d'informations sur le jeu, " 
                + "visitez le site : <a href=\"https://www.ffothello.org/othello/regles-du-jeu/\">"
                + "https://www.ffothello.org/othello/regles-du-jeu/</a></p></body></html>";

		JLabel linkLabel = new JLabel(helpText);
		linkLabel.setFont(linkLabel.getFont().deriveFont(Font.PLAIN));
		linkLabel.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (Desktop.isDesktopSupported()) {
		            try {
		                Desktop.getDesktop().browse(new URI("https://www.ffothello.org/othello/regles-du-jeu/"));
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            } catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
		    }
		});

JOptionPane.showMessageDialog(null, linkLabel, "Aide", JOptionPane.INFORMATION_MESSAGE);

    	
    	// reprendre le jeu 
    	this.play.getStart().resume();
    	play.pauseGame = false;
   		this.timer.restart();
    }
    // lire les exemples du jeu
    
    public void ihmExemplesfortheGame() throws IOException {
    	String exemplesText = "<html><body><p>Pour voir les exemples disponibles, " 
                + "visitez le site : <a href=\"https://drive.google.com/drive/folders/1q1Pr_DMN2GCmbOLpucTGueAcJEQb03JK?usp=share_link\">"
                + "https://drive.google.com/drive/folders/1q1Pr_DMN2GCmbOLpucTGueAcJEQb03JK?usp=share_link</a></p></body></html>";

		JLabel linkLabel = new JLabel(exemplesText);
		linkLabel.setFont(linkLabel.getFont().deriveFont(Font.PLAIN));
		linkLabel.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (Desktop.isDesktopSupported()) {
		            try {
		                Desktop.getDesktop().browse(new URI("https://drive.google.com/drive/folders/1q1Pr_DMN2GCmbOLpucTGueAcJEQb03JK?usp=share_link"));
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            } catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
		    }
		});

JOptionPane.showMessageDialog(null, linkLabel, "Exemples", JOptionPane.INFORMATION_MESSAGE);

    	
    	// reprendre le jeu 
    	this.play.getStart().resume();
    	play.pauseGame = false;
   		this.timer.restart();
    }
    
    // lire les règles du jeu
    public void ihmRulesoftheGame() throws IOException {
    	// boite d'affichage
    	JOptionPane d = new JOptionPane();
    	// lire le fichier regles.txt
    	String rules = readFile(getClass().getResource("/Files/regles.txt"));
    	
    	// afficher les regles 
    	d.showMessageDialog( null, rules, 
    	      "Regles du jeu", JOptionPane.INFORMATION_MESSAGE);
    	
    	// reprendre le jeu 
    	this.play.getStart().resume();
    	play.pauseGame = false;
   		this.timer.restart();
    }
    
    // methode permet d'afficher la fin du jeu 
    public void ihmWiner(String msg) throws IOException {    	
    	// boite d'affichage
    	JOptionPane d = new JOptionPane(); 	

    	// afficher le message 
    	d.showMessageDialog( null, msg, 
    	      "Jeu terminé", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // retourner menu aide 
    public JMenu getIhmMenuHelp() {
    	return this.ihmMenuHelp;
    }
    public JMenu getIhmMenuExemples() {
    	return this.ihmMenuExemples;
    }
    
    // retourner menu regles 
    public JMenu getIhmMenuRules() {
    	return this.ihmMenuRules;
    }
    
    // retourner menu nouvelle partie 
    public JMenuItem getIhmMenuNewGame() {
    	return this.ihmMenuNewGame;
    }
    
    // retourner menu Pause 
    public JMenuItem getIhmMenuPause() {
    	return this.ihmMenuPause;
    }
    
    // retourner menu Quitter 
    public JMenuItem getIhmMenuExit() {
    	return this.ihmMenuExit;
    }
 
    // lire le fichier regles.txt
	public String readFile(URL url) throws IOException {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String rules = "", line;
		while ((line = in.readLine()) != null)
		{
	      // Afficher le contenu du fichier
			 rules = rules.concat(line + "\n");
		}
		in.close();
				
		return rules;
	}
	
    // chronometre
    public void chrono() throws InterruptedException{
        
        tacheTIMER = (ActionEvent e1) -> {
         seconde++;

         //Afficher le chrono dans un JLabel
         if(seconde < 10) time.setText("00:0"+seconde);
         else time.setText("00:"+seconde);
        };
        //temps d'éxecution de la tache
        timer = new Timer(delais,tacheTIMER);
        //Demarrer le chrono
        timer.start(); 

    }

    // creation du footer
    private void creatIhmFooter(){
    	this.ihmPanelFooter.add(Box.createHorizontalGlue());
    	this.ihmPanelFooter.add(this.ihmLabelFooter);
    	this.ihmPanelFooter.add(Box.createHorizontalGlue());
    	
    	// modification de la couleur   
    	ihmLabelFooter.setForeground(Color.WHITE);
    	ihmPanelFooter.setOpaque(true);
    	this.ihmPanelFooter.setBackground(new Color(63,72,204));
    	Border ihmBorderPanelGame = BorderFactory.createLineBorder(new Color(63,72,204), 10);
        this.ihmPanelFooter.setBorder(ihmBorderPanelGame);
    }
    
    // creation du panneau des score et time du jeu
    private void creatIhmPanelScoreTime() {
    	// les boxs pour l'affichage des icons et score
    	Box ihmBoxBlack = Box.createHorizontalBox();
    	Box ihmBoxWhite = Box.createHorizontalBox();
    	Box ihmBoxLevelGame = Box.createHorizontalBox();
    	Box ihmBoxAlgoGame = Box.createHorizontalBox();
    	Box ihmBoxTurnPlayer = Box.createHorizontalBox();
    	JLabel Tplayer = new JLabel("Tour : ");
    	
    	// label pour le niveau du jeu
    	JLabel level = new JLabel("Niveau : ");
    	// label pour l'algo du jeu
    	JLabel algo  = new JLabel("Algo : ");
    	
    	// box pour l'affichage de la durée 
    	Box ihmBoxMoveDuration = Box.createHorizontalBox();
    	Box ihmBoxTime         = Box.createHorizontalBox();
    	
    	// le label de l'icon Blanc
    	JLabel ihmIconPlayerWhite = new JLabel();
    	ihmIconPlayerWhite.setIcon(StateIHM.ihmWHITEState);
    	// le label de l'icon Noir
    	JLabel ihmIconPlayerBlack = new JLabel();
    	ihmIconPlayerBlack.setIcon(StateIHM.ihmBLACKState);
    	// le label de la durée 
    	JLabel ihmLabelMoveDuration = new JLabel("Durée");
    	
    	
    	// les boxs du temps  
    	ihmBoxMoveDuration.add(Box.createHorizontalStrut(5));
    	ihmBoxMoveDuration.add(ihmLabelMoveDuration);
    	ihmBoxMoveDuration.add(Box.createHorizontalStrut(5));
    	ihmBoxTime.add(time);
    	ihmBoxMoveDuration.setBackground(new Color(63,72,204));
    	
    	// ajouter l'icon et le score dans le box noir
    	ihmBoxBlack.add(ihmIconPlayerBlack);
    	ihmBoxBlack.add(Box.createHorizontalStrut(18));
    	ihmBoxBlack.add(this.ihmScorePlayerBlack);
   	
    	// ajouter l'icon et le score dans le box blanc
    	ihmBoxWhite.add(ihmIconPlayerWhite);
    	ihmBoxWhite.add(Box.createHorizontalStrut(10));
    	ihmBoxWhite.add(this.ihmScorePlayerWhite);
    	
    	// niveau du jeu
    	ihmBoxLevelGame.add(level);
    	ihmBoxLevelGame.add(ihmLevelGame);
    	
    	// tour du joueur 
    	ihmBoxTurnPlayer.add(Tplayer);
    	ihmBoxTurnPlayer.add(ihmTurnPlayer);
    	
    	// algorithme du jeu
    	ihmBoxAlgoGame.add(algo);
    	ihmBoxAlgoGame.add(ihmAlgoGame);
    	
    	
    	// box d'affichage du score
    	Box ihmBox = Box.createVerticalBox();
    	ihmBox.add(Box.createVerticalStrut(50));
    	ihmBox.add(ihmBoxAlgoGame);
    	ihmBox.add(Box.createVerticalStrut(20));
    	ihmBox.add(ihmBoxTurnPlayer);
    	ihmBox.add(Box.createVerticalStrut(70));
    	ihmBox.add(ihmBoxLevelGame);
    	ihmBox.add(Box.createVerticalStrut(20));
    	ihmBox.add(ihmBoxWhite);
    	ihmBox.add(Box.createVerticalStrut(10));
    	ihmBox.add(ihmBoxBlack);
    	ihmBox.add(Box.createVerticalStrut(150));
    	
    	Border ihmBorderPanelGame = BorderFactory.createLineBorder(new Color(63,72,204), 1);
    	ihmBoxAlgoGame.setBorder(ihmBorderPanelGame);
    	// ajouter les boxs dans le paneau du score et time
    	ihmPanelScoreTime.add(ihmBox);
    	ihmPanelScoreTime.add(ihmBoxMoveDuration);
    	ihmPanelScoreTime.add(ihmBoxTime);
    	ihmPanelScoreTime.add(Box.createVerticalStrut(200));
    	
    	// modification de la couleur pour chaque label 
    	ihmAlgoGame.setForeground(Color.RED);
    	algo.setForeground(Color.WHITE);
    	level.setForeground(Color.WHITE);
    	Tplayer.setForeground(Color.WHITE);
    	this.ihmTurnPlayer.setForeground(Color.RED);
    	this.ihmLevelGame.setForeground(Color.RED);
    	this.ihmScorePlayerBlack.setForeground(Color.WHITE);
    	this.ihmScorePlayerWhite.setForeground(Color.WHITE);
    	ihmLabelMoveDuration.setForeground(Color.WHITE);
    	ihmPanelScoreTime.setBackground(new Color(63,72,204));
    	ihmPanelScoreTime.setOpaque(true);
    	// modification de la couleur de l'horloge 
    	time.setForeground(Color.RED);
    }
      
    // creation du menu du jeu
    private void createIhmMenu() {
    	// creation du menu principal
        this.ihmMenuBar.add(ihmMenuGame);
        this.ihmMenuBar.add(ihmMenuRules);
        this.ihmMenuBar.add(ihmMenuHelp);
        this.ihmMenuBar.add(ihmMenuExemples);
        
        // ajouter le sous menu Jeu
        ihmMenuGame.add(ihmMenuNewGame);
        ihmMenuGame.add(ihmMenuPause);
        ihmMenuGame.add(ihmMenuExit);
        
        // ajouter les listeners
        ihmMenuRules.addMouseListener(new MenuListener(this.play));
        ihmMenuHelp.addMouseListener(new MenuListener(this.play));
        ihmMenuExemples.addMouseListener(new MenuListener(this.play));
        
        // listener pour la nouvelle partie
        ihmMenuNewGame.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
	        	
	        	// Faire une pause
			   timer.stop();
               play.pauseGame = true;
               play.playnewGame = true;
               
               int option = play.pauseGame();
               
               if(	option != JOptionPane.CANCEL_OPTION && 
               		option != JOptionPane.CLOSED_OPTION){	
            	  
            	   // fermer la fenetre d'affichage
            	   setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                   play.pauseGame = false;
              	   play.playnewGame = false;
              	   // demander de jouer un nouvelle partie
            	   Start.newPlay = true;
               	   
            	   // appelle à la fonction nouvelle partie 
            	   if(play.playendGame = true) {
           				try {
           					play.getStart().newGame();
           				} catch (InterruptedException | IOException e) {
           					e.printStackTrace();
           				}
               		}
               	}else {
               		// le boutton annuler
               		play.pauseGame = false;
               		play.playnewGame = false;
               		timer.restart();
               	}
	        }
        });
       
        // listener pour mettre la partie en pause
        ihmMenuPause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
            
            	// arreter le time
               timer.stop();
               play.pauseGame = true;
               
               int option = play.pauseGame();
	           	// boutton ok
	           	if(	option == JOptionPane.NO_OPTION || 
	           		option == JOptionPane.CLOSED_OPTION){	
	           		System.exit(0);
	           	}
	           	
	           	// le boutton annuler
	           	if(	option == JOptionPane.OK_OPTION ){	
	           		play.pauseGame = false;
	           		timer.restart();
		        }
               
            }
        });
        
        // listener pour quitter le jeu
        ihmMenuExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
            	timer.stop();
            	play.getStart().suspend();
            	
            	// la boite de dialogue
            	String msg ="Etes vous surs de vouloir quitter cette partie ? \n Vous allez perdre votre progrès.";
            	String title ="Quitter le jeu";
            	JOptionPane jop = new JOptionPane();			
            	UIManager.put("OptionPane.cancelButtonText", "Annuler");
            	int option = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            	            	
            	// le bouton ok
            	if(	option != JOptionPane.CANCEL_OPTION && 
            		option != JOptionPane.CLOSED_OPTION){	
            		System.exit(0);
            	} 
            	
            	play.getStart().resume();
            	timer.restart();
            }
        });
    }
    
    // creation de l'interface du panneau du jeu
    public void creatIhmBoardGame(Board board){
    	
    	// creer un border pour le panneau
        Border ihmBorderPanelGame = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.ihmPanelGame.setBorder(ihmBorderPanelGame);
    	
        // RW == ligne, Col == colone
        // poser les quatres pions de demarrage 
    	int posRWSquareWHITE = Board.modelBoardSize / 2; 
        int posCOLSquareWHITE = Board.modelBoardSize / 2 - 1;
        int posRWSquareBLACK = Board.modelBoardSize / 2;
        int posCOLSquareBLACK = Board.modelBoardSize / 2;
        
        // Creation de l'ensemble des cases 
        for (int i = 0; i < Board.modelBoardSize; i++) {
            for (int j = 0; j < Board.modelBoardSize; j++) {
            	ihmBoardGame[i][j] = null;
            	ihmBoardGame[i][j] = new SquareIHM();
            	ihmBoardGame[i][j].setOpaque(true);
            	
            	/*GrigBagConstraint.NONE pour ne pas redimensionner le composant.*/
               this.ihmPanelGame.add(ihmBoardGame[i][j], new GridBagConstraints(i, j, 1, 1, 0.0, 0.0, 
            		   GridBagConstraints.CENTER, GridBagConstraints.NONE, 
            		   new Insets(0, 0, 0, 0), 0, 0));
               
               	// remplir le board ihm à partir de board 
	             if(board.modelBoardSqures[i][j].getSquareState() == State.BLACKState) {
	            	 ihmBoardGame[i][j].setIcon(StateIHM.ihmBLACKState);
	             }else if(board.modelBoardSqures[i][j].getSquareState() == State.WHITEState) {
	            	 ihmBoardGame[i][j].setIcon(StateIHM.ihmWHITEState);
	             }
                                            
               ihmBoardGame[i][j].setBackground(new Color(18,11,239));
               this.ihmPanelGame.setBackground(new Color(18,11,239));
               
               //pour eviter un bug lors de l'appel de la fonction nouveau() 
               ihmBoardGame[i][j].removeMouseListener(new BoardListener(this.play));
               // ajouter les listener pour les clics
               ihmBoardGame[i][j].addMouseListener(new BoardListener(this.play)); 
            }
        }
    }
    
    // afficher le board ihm
    public void ihmUpdateDisplay(Board board) {
    	
    	for (int i = 0; i <Board.modelBoardSize; i++) {
            for (int j = 0; j < Board.modelBoardSize; j++) {
		    	// remplir le board ihm à partir du board 
		        if(board.modelBoardSqures[i][j].getSquareState() == State.BLACKState) {
		       	 ihmBoardGame[i][j].setIcon(StateIHM.ihmBLACKState);
		        }else if(board.modelBoardSqures[i][j].getSquareState() == State.WHITEState) {
		       	 ihmBoardGame[i][j].setIcon(StateIHM.ihmWHITEState);
		        }
            }
    	}
    }
    
    // afficher toutes les positions possibles du deplacement
    public void ihmUpdatePointsMove(ArrayList<Point> listpoints) {
    	
    	for(int i=0; i < listpoints.size(); i++) {
    		ihmBoardGame[(int) listpoints.get(i).getX()][(int) listpoints.get(i).getY()].setIcon(StateIHM.ihmChoiseState);
    	}
    }
    
    // la methode pour afficher le score 
    public void ihmNewScore(Player player, Player playerAdv) {
    	// changer le score
		player.setScore(player.getScore() + 1);
		int scorePlayer = player.getScore();
				
		
		if(player.getPawn() == Pawn.BLACKState) {
			if(scorePlayer  < 10) {
				ihmScorePlayerBlack.setText("0"+ (scorePlayer ));
			}else ihmScorePlayerBlack.setText(""+ (scorePlayer ));
		}else {
			if(scorePlayer  < 10) {
				ihmScorePlayerWhite.setText("0"+ (scorePlayer ));
			}else ihmScorePlayerWhite.setText(""+ (scorePlayer ));
		}
		
		// player adversaire 
		int scorePlayerAdv = playerAdv.getScore();
		if(playerAdv.getPawn() == Pawn.BLACKState) {
			if(scorePlayerAdv  < 10) {
				ihmScorePlayerBlack.setText("0"+ (scorePlayerAdv ));
			}else ihmScorePlayerBlack.setText(""+ (scorePlayerAdv));
		}else {
			if(scorePlayerAdv  < 10) {
				ihmScorePlayerWhite.setText("0"+ (scorePlayerAdv ));
			}else ihmScorePlayerWhite.setText(""+ (scorePlayerAdv));
		}
    }
    
    // afficher les positions possibles du deplacement
    public void ihmdeletePointsMove() {
    	
    	for (int i = 0; i <Board.modelBoardSize; i++) {
            for (int j = 0; j < Board.modelBoardSize; j++) {
		    	// remplir le board ihm à partir du board 
            	if(ihmBoardGame[i][j].getIcon() == StateIHM.ihmChoiseState) ihmBoardGame[i][j].setIcon(null);
            }
    	}
    }
    
    // # getter et setter 
    // l'ensemble des getter et setter
	public Pawn getIhmPlayerPawn() {
		return ihmPlayerPawn;
	}


	public void setIhmPlayerPawn(Pawn ihmPlayerPawn) {
		this.ihmPlayerPawn = ihmPlayerPawn;
	}


	public String getIhmGameMade() {
		return ihmGameMade;
	}


	public void setIhmGameMade(String ihmGameMade) {
		this.ihmGameMade = ihmGameMade;
	}


	public Box getIhmPanelFooter() {
		return ihmPanelFooter;
	}


	public void setIhmPanelFooter(Box ihmPanelFooter) {
		this.ihmPanelFooter = ihmPanelFooter;
	}

	public JLabel getIhmLabelFooter() {
		return ihmLabelFooter;
	}

	public void setIhmLabelFooter(JLabel ihmLabelFooter) {
		this.ihmLabelFooter = ihmLabelFooter;
	}

	public JLabel getIhmTurnPlayer() {
		return ihmTurnPlayer;
	}

	public void setIhmTurnPlayer(JLabel ihmTurnPlayer) {
		this.ihmTurnPlayer = ihmTurnPlayer;
	}

	public Box getIhmPanelScoreTime() {
		return ihmPanelScoreTime;
	}

	public void setIhmPanelScoreTime(Box ihmPanelScoreTime) {
		this.ihmPanelScoreTime = ihmPanelScoreTime;
	}

	public JLabel getIhmAlgoGame() {
		return ihmAlgoGame;
	}
	public void setIhmAlgoGame(JLabel ihmAlgoGame) {
		this.ihmAlgoGame = ihmAlgoGame;
	}
		

	public JLabel getIhmLevelGame() {
		return ihmLevelGame;
	}

	public void setIhmLevelGame(JLabel ihmLevelGame) {
		this.ihmLevelGame = ihmLevelGame;
	}

	public JLabel getIhmScorePlayerBlack() {
		return ihmScorePlayerBlack;
	}

	public void setIhmScorePlayerBlack(JLabel ihmScorePlayerBlack) {
		this.ihmScorePlayerBlack = ihmScorePlayerBlack;
	}

	public JLabel getIhmScorePlayerWhite() {
		return ihmScorePlayerWhite;
	}

	public void setIhmScorePlayerWhite(JLabel ihmScorePlayerWhite) {
		this.ihmScorePlayerWhite = ihmScorePlayerWhite;
	}

	public JMenuBar getIhmMenuBar() {
		return ihmMenuBar;
	}

	public void setIhmMenuBar(JMenuBar ihmMenuBar) {
		this.ihmMenuBar = ihmMenuBar;
	}

	public JMenu getIhmMenuGame() {
		return ihmMenuGame;
	}

	public void setIhmMenuGame(JMenu ihmMenuGame) {
		this.ihmMenuGame = ihmMenuGame;
	}

	public JMenuItem getIhmMenuLevelEasy() {
		return ihmMenuLevelEasy;
	}

	public void setIhmMenuLevelEasy(JMenuItem ihmMenuLevelEasy) {
		this.ihmMenuLevelEasy = ihmMenuLevelEasy;
	}

	public JMenuItem getIhmMenuLevelMedium() {
		return ihmMenuLevelMedium;
	}

	public void setIhmMenuLevelMedium(JMenuItem ihmMenuLevelMedium) {
		this.ihmMenuLevelMedium = ihmMenuLevelMedium;
	}

	public JMenuItem getIhmMenuLevelHaro() {
		return ihmMenuLevelHaro;
	}

	public void setIhmMenuLevelHaro(JMenuItem ihmMenuLevelHaro) {
		this.ihmMenuLevelHaro = ihmMenuLevelHaro;
	}

	public GridLayout getIhmLayoutGame() {
		return ihmLayoutGame;
	}

	public void setIhmLayoutGame(GridLayout ihmLayoutGame) {
		this.ihmLayoutGame = ihmLayoutGame;
	}

	public JPanel getIhmPanelGame() {
		return ihmPanelGame;
	}

	public void setIhmPanelGame(JPanel ihmPanelGame) {
		this.ihmPanelGame = ihmPanelGame;
	}

	public int getIhmSizePanelGame() {
		return ihmSizePanelGame;
	}

	public void setIhmSizePanelGame(int ihmSizePanelGame) {
		this.ihmSizePanelGame = ihmSizePanelGame;
	}

	public SquareIHM[][] getIhmBoardGame() {
		return ihmBoardGame;
	}

	public void setIhmBoardGame(SquareIHM[][] ihmBoardGame) {
		this.ihmBoardGame = ihmBoardGame;
	}

	public Dimension getIhmScreen() {
		return ihmScreen;
	}

	public void setIhmScreen(Dimension ihmScreen) {
		this.ihmScreen = ihmScreen;
	}

	public void setIhmMenuRules(JMenu ihmMenuRules) {
		this.ihmMenuRules = ihmMenuRules;
	}

	public void setIhmMenuHelp(JMenu ihmMenuHelp) {
		this.ihmMenuHelp = ihmMenuHelp;
	}
	
	public void setIhmMenuExemples(JMenu ihmMenuExemples) {
		this.ihmMenuExemples = ihmMenuExemples;
	}

	public void setIhmMenuNewGame(JMenuItem ihmMenuNewGame) {
		this.ihmMenuNewGame = ihmMenuNewGame;
	}

	public void setIhmMenuPause(JMenuItem ihmMenuPause) {
		this.ihmMenuPause = ihmMenuPause;
	}

	public void setIhmMenuExit(JMenuItem ihmMenuExit) {
		this.ihmMenuExit = ihmMenuExit;
	}

	public int getSeconde() {
		return seconde;
	}

	public void setSeconde(int seconde) {
		this.seconde = seconde;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public Play getPlay() {
		return play;
	}

	public void setPlay(Play play) {
		this.play = play;
	}
}
