package Controller;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class WelcomePage extends JFrame {
  
  private JLabel message;
  private JLabel moreText;
  private JLabel text;
  
  public WelcomePage() {
    this.setSize(500, 500);
    this.setTitle("OTHELLO");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel pan = new JPanel(new GridLayout(3,1));
    
    message = new JLabel("<html><div style='text-align: center; color: blue; font-size: 14pt;'>Bienvenue dans notre jeu Othello</div></html>", SwingConstants.CENTER);
    pan.add(message);
    
    moreText = new JLabel("<html><div style='text-align: center; color: blue; font-size: 14pt;'>Projet réalisé par : BOUFAFA LAMIS, SAIDI Yasmine</div></html>", SwingConstants.CENTER);
    pan.add(moreText);
    
    text = new JLabel("<html><div style='text-align: center; color: blue; font-size: 14pt;'>Encadré par : Selmi Carla</div></html>", SwingConstants.CENTER);
    pan.add(text);
    
    this.setContentPane(pan);
    this.setVisible(true);
    this.setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
    
    // Attendre 3 secondes avant de passer à la page suivante
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    // Fermer la page de bienvenue et lancer la page suivante
    this.dispose();
    Start start = new Start();
    start.start();
  }
  
  public static void main(String[] args) {
    new WelcomePage();
  }
}
