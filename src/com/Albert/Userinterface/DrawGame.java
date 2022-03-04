package com.Albert.Userinterface;

/*Import */
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

import com.Albert.Data.Conversion;
import com.Albert.game.Block;
import com.Albert.game.Game;

public class DrawGame extends JLabel {

	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.LIGHT_GRAY); // Farbe f�r das eigentliche Spielfeld wird auf grau gesetzt
		for (int i = 0; i < 10; i++) {// 1. Schleife f�r den x durchlauf
			for (int j = 0; j < 18; j++) { // 2. Schleife f�r den y durchlauf
				g.drawRect(i * 32, j * 32, 32, 32); // Die Rechtecke zeichnen die "grauen" Linien des gro�en Spielfelds
			}
		}


		g.setColor(Color.BLACK); // Farbe wieder auf Schwarz gesetzt um den �u�eren Rand zu verdeutlichen
		g.drawRect(0, 0, 320	, 576); // Das �u�ere Gr��e Rechteck wird f�r die Umrandung gezeichnet

		
		g.setColor(Game.currentBlock.getColor()); // Farbe wird auf dei Farbe des aktuellen Blockes gesetzt
		for (int j = 0; j < Game.currentBlock.getBounds()[Game.currentBlock.getRotation()].length; j++) { // alle Zellen des Blockes wird durchlaufen  1. Schleife f�r den x Durchlauf
			
			for (int k = 0; k < Game.currentBlock.getBounds()[Game.currentBlock.getRotation()][j].length; k++) { // 2 Schleife f�r den y Durchlauf
			
				if (Game.currentBlock.getBounds()[Game.currentBlock.getRotation()][j][k] == 1) { // Leere Teile des Blockes werden ausgelassen, da sie im TXT null sind
					
					g.fillRect(Conversion.cellToCoord(Game.currentBlock.getX() + j), // die Rechtecke des Current Block werden gezeichnet 
							Conversion.cellToCoord(Game.currentBlock.getY() + k), 32, 32); 
			
					
				}
			}
		}
		
		/* Die farbigen Bl�cke im Map Array werden gezeichnet */
		
		for(int i = 0; i < Game.map.length; i++) {  // 1. Schleife f�r die x Werte des Map Array
			for(int j= 0; j< Game.map[i].length; j++) { // 2. Schleife f�r die y Werte des Map array
				
				if(Game.map[i][j] >0) { // falls das Maparray an der Stelle nicht leer ist wird die if Verzweigung durchlaufen 0= luft/leer
					
					switch(Game.map[i][j]) { // switch f�r jeden Punkt mit einem Block des Maparray, je nach Wert des Maparray (Blocktyp) die Farbe f�r das Rechteck ausw�hlt
					
					case 1:
						g.setColor(Color.CYAN); break; //Farbe f�r den Block mit dem Wert 1 (I)
					case 2:
						g.setColor(Color.YELLOW); break; //Farbe f�r den Block mit dem Wert 2 (O)
					case 3:
						g.setColor(Color.MAGENTA); break; //Farbe f�r den Block mit dem Wert 3 (T)
					case 4:
						g.setColor(Color.ORANGE);break; //Farbe f�r den Block mit dem Wert 4 (L)
					case 5:
						g.setColor(Color.BLUE); break; //Farbe f�r den Block mit dem Wert 5 (J)
					case 6:
						g.setColor(Color.RED); break; //Farbe f�r den Block mit dem Wert 6 (Z)
					case 7:
						g.setColor(Color.GREEN); break; //Farbe f�r den Block mit dem Wert 7 (S)
					
					
					
					
					}
					
				g.fillRect(Conversion.cellToCoord(i), Conversion.cellToCoord(j), 32, 32);	//zeichnen des Farbigen(oben) Rechteck an der Position des Maparray
					
					
				}
				
			}
			
		}

		repaint(); // neuzeichenen/aktualisieren
	}

}