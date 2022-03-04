package com.Albert.Data;

/*Imports */
import com.Albert.game.Block;
import com.Albert.game.Game;
import com.Albert.game.GameState;
import com.Albert.inout.Audio;
import com.Albert.inout.DataHandler;

public class Collision {

	/*
	 * CollideWithBlock checkt ob der currentBlock mit einem der bereits
	 * gezeichneten Bl�cke in der Map Variable kollidiert und verhindert das Bewegen
	 * falls die Kollision true ist. 
	 * Die Direction -1 checkt die Kollision mit einem Block nach links (x-Achse -1) 
	 * Die Direction 0 checkt eine Kollision mit einem Block unter dem aktuellen Block (0, da keine x �nderung) 
	 * die Direction 1 checkt die Kollision mit einem Block nach rechts (x- Achse +1) 
	 * Es wird bei jeder Kollision geschaut ob der Block im Spielfeld ist, da es sonst Probleme
	 * mit dem Map Array geben w�rde (diese beinhaltet ja nur die map)
	 * 
	 */
	public static boolean collideWithBlock(Block b, int direction) {

		switch (direction) {
		case -1: // linke Blockkollision

			if (b.getY() > 0) { // Ist der Block im Spielfeld (oberer Rand) ?
				if (b.getX() > 0) { // Ist der Block innerhalb des Spielfelds nach links
					for (int i = 0; i < b.getBounds()[b.getRotation()].length; i++) { // 1. der beiden Schleifen, die jedes einzelne Viereck des Blockes durchl�uft (x)
						
						for (int j = 0; j < b.getBounds()[b.getRotation()][i].length; j++) { // 2. der beiden Schleifen, die jedes einzelne Viereck des Blockes durchl�uft (y)
							
							if (b.getBounds()[b.getRotation()][i][j] == 1) { // Nur wahr, wenn das Viereck des Blockes ausgef�llt ist. "Luft Teile" des Blockes werden ignoriert
								
								if (Game.map[b.getX() + i - 1][b.getY() + j] >= 1) { // Schaut ob der Block im verschoben Zustand "gedanklich" �berlappen w�rde

									System.out.println("linksBlock"); // Die Kollision wird in der Konsole ausgegeben und es wird true zur�ckgegeben
									return true;
								}
							}
						}
					}

				}
			}
			break;

		case 0: // untere Blockkollision

			if (b.getY() + b.getSize() > 1) { // Ist der Block innerhalb des Spielfeld (obere Grenze)
				if (b.getY() - b.getSize() < 17) { // Ist der Block innerhalb des Spielfels (untere Grenze)
					try {
						for (int i = 0; i < b.getBounds()[b.getRotation()].length; i++) { // 1. der beiden Schleifen, die jedes einzelne Viereck des Blockes durchl�uft (x)
							
							for (int j = 0; j < b.getBounds()[b.getRotation()][i].length; j++) {// 2. der beiden Schleifen, die jedes einzelne Viereck des Blockes durchl�uft (y)
								
								if (b.getBounds()[b.getRotation()][i][j] == 1) { // Nur wahr, wenn das Viereck des Blockes ausgef�llt ist. "Luft Teile" des Blcokes werden ignoriert

									if (Game.map[b.getX() + i][b.getY() + j + 1] >= 1) { // Der Block wird "fikitv" eins weiter nach unten gesetzt, es wird geschaut ob er mit einem Block unterhalb �berlappt

										Game.spawnNewBlock = true; // Falls der Block mit einem Block unterhalb kollidiert wird das Spawnen eines neuen Blockes auf true gesetzt
										
										fillBlock(b); // der aktuelle Block wird in das 2-dimensional Maparray eingetragen
										System.out.println("unterhalb Block"); // debug
										return true; // true wird als Kollision zur�ckgegeben
									}

								}
							}
						}
					} catch (Exception e) {
						return false;
					}
				}
			}

			break;

		case 1: // rechte Blockkollision

			if (b.getY() > 0) { // Ist der Block bereits im Spielfeld (oben)
				if (b.getX() < 10) { // Ist der Block innerhalb der rechten Grenze (anders als die Afd)
					for (int i = 0; i < b.getBounds()[b.getRotation()].length; i++) { // 1. der beiden Schleifen, die jedes einzelne Viereck des Blockes durchl�uft (x)
						
						for (int j = 0; j < b.getBounds()[b.getRotation()][i].length; j++) {// 2. der beiden Schleifen, die jedes einzelne Viereck des Blockes durchl�uft (y)
							
							if (b.getBounds()[b.getRotation()][i][j] == 1) { // Nur wahr, wenn das Viereck des Blockes ausgef�llt ist. "Luft Teile" des Blockes werden ignoriert
								
								if (Game.map[b.getX() + i + 1][b.getY() + j] >= 1) { // Der Block wird "gedanklich" eins nach rechts gesetzt, und geschaut ob der Block �berlappt

									System.out.println("rechter Block"); // debug
									return true; // gibt true zur�ck, wenn eine Kollision vorliegt
								}
							}
						}
					}

				}
			}
			break;
		}

		return false; // es liegt keine Kollision vor der Block kann bewegt werden
	}

	/*
	 * CollideInRotation checkt eine Kollision, wenn der Block gedreht werden soll
	 * Es wird bei jeder Kollision geschaut ob der Block im Spielfeld ist, da es
	 * sonst Probleme mit dem Map Array geben w�rde (diese beinhaltet ja nur die
	 * map) Der Block wird immer mit der neuen Rotation rot auf Kollision �berpr�ft
	 */
	public static boolean collideInRotation(Block b) {

		int rot = b.getRotation() + 1; // Die aktuelle Rotation des Blockes wird aufgerufen und um eins erh�ht. Der Wert wird in der Variable rot gespeichert.
		if (rot == 4) { // Da es nur vier Rotationsstufen gibt, wird bei dem Wert 4 die Rotation wieder auf null gesetzt.
			rot = 0;
		}

		Block block = new Block(); // Ein neuer Block wird f�r die Rotation erzeugt, damit die Kollisionen �berpr�ft werden k�nnen.
		block.setRotation(rot); // Die Rotation wird auf den wert der Variable rot gesetzt (zu �berpr�fende Rotation)
		block.setBounds(b.getBounds()); // Der neue Block wird auf die Werte des alten gesetzt.
		block.setSize(b.getSize()); // Der neue Block wird auf die Werte des alten gestzt,
		block.setX(b.getX() - 1); // ^^ Der Block wird f�r eins nach links gesetzt
		block.setY(b.getY()); // ^^

		/* Kollision mit den W�nden */
		if (collideWithWall(block, 1)) { // Es wird �berpr�ft ob die rotierte Form des alten Blockes (also der neue Block) mit der rechten Wand kollidiert.
			
			System.out.print("RotationKollision");// debug
			return true; // Rotation wird durch den R�ckgabewert true verhindert, da eine Kollision vorliegt.
		}
		
		
		block.setX(b.getX() + 2); // F�r die andere Seite wird der Block 1 nach rechts gesetzt (2, da Ausgleich vom ersten)

		if (collideWithWall(block, -1)) { // Es wird �berpr�ft ob die rotierte Form des alten Blockes (also der neue Block) mit der linken Wand kollidiert.
			System.out.print("RotationKollision");// debug
			return true; // Rotation wird durch den R�ckgabewert true verhindert, da eine Kollision vorliegt.
		}
		/* Kollision mit anderen Bl�cken */
		
		if (b.getY() > 0) { // Es wird �berpr�ft ob der Block innerhalb des Spielfels ist ( oberer Rand)
			for (int i = 0; i < b.getBounds()[rot].length; i++) { // 1. der beiden Schleifen, die jedes einzelne Viereck des Blockes durchl�uft (x)
				
				for (int j = 0; j < b.getBounds()[rot][i].length; j++) {// 2. der beiden Schleifen, die jedes einzelne Viereck des Blockes durchl�uft (y)
					
					if (b.getBounds()[rot][i][j] == 1) { // Nur wahr, wenn das Viereck des Blockes ausgef�llt ist. "Luft Teile" des Blockes werden ignoriert
						
						try {
							if (Game.map[b.getX() + i][b.getY() + j] >= 1) {// Es wird �berpr�ft ob der rotierte Block mit einem andere Block �berlappt (jedes Viereck wird �berpr�ft)
								
								System.out.print("RotationKollision");// debug
								return true; // eine Kollision liegt vor.
							}
						} catch (Exception e) {
							return true;
						}
					}
				}

			}
		}

		return false; // es liegt keine Kollision vor, die Rotation kann ausgef�hrt werden
	}

	/*
	 * die Methode collideWithWall �berpr�ft ob eine Kollision mit den au�en W�nden
	 * vorliegt und gibt einen boolean zur�ck Die Direction -1 checkt die Kollision
	 * mit der Wand nach links a Die Direction 0 checkt eine Kollision mit der
	 * unteren Grenze (boden der Map) die Direction 1 checkt die Kollision mit der
	 * rechten Wand
	 */
	public static boolean collideWithWall(Block b, int direction) {
		// direction: -1 = links, 0 = runter, 1 = rechts
		switch (direction) { // je nach ben�tigter Kollision
		case -1: // Linke Wand

			for (int i = 0; i < b.getBounds()[b.getRotation()].length; i++) { // 1. Schleife um alle x Werte des Blockes zu durchlaufen
				
				for (int j = 0; j < b.getBounds()[b.getRotation()][i].length; j++) { // 2. Schleife um alle y Werte des Blockes zu durchlaufen
					
					if (b.getBounds()[b.getRotation()][i][j] == 1) { // Es werden nur de ausgef�llten Bl�cke �berpr�ft die "leeren" Bl�cke werden ausgelassen
						
						// System.out.println(b.getX()); //debug
						if (b.getX() + i == 0) { // Hat der Block mit addiereten x Wert den Wert der Wand?
							System.out.println("linke Wand"); // debug
							return true; // kollision mit der linken Wand true zur�ckgeben
						}
					}
				}
			}
			break;

		case 0: // untere Grenze
			for (int i = 0; i < b.getBounds()[b.getRotation()].length; i++) { // 1. Schleife um alle x Werte des Blockes zu durchlaufen
				
				for (int j = 0; j < b.getBounds()[b.getRotation()][i].length; j++) {// 2. Schleife um alle y Werte des Blockes zu durchlaufen
					
					if (b.getBounds()[b.getRotation()][i][j] == 1) { // Es werden nur de ausgef�llten Bl�cke �berpr�ft die "leeren" Bl�cke werden ausgelassen
						
						if (b.getY() + j == 17) { // Stimmen die y- Werte des Blockes mit der untere Grenze �berein, wenn ja Kollision
							
							Game.spawnNewBlock = true; // boolean f�r neuen Block true
							fillBlock(b); // Block wird in Map array gezeichnet
							System.out.println("unterer Boden");
							return true; // True wird f�r die Kollision zur�ckgegeben
						}
					}
				}
			}
			break;

		case 1: // rechte Wand
			for (int i = 0; i < b.getBounds()[b.getRotation()].length; i++) { // 1. Schleife um alle x Werte des Blockes zu durchlaufen
				
				for (int j = 0; j < b.getBounds()[b.getRotation()][i].length; j++) { // 2. Schleife um alle y Werte des Blockes zu durchlaufen
					
					if (b.getBounds()[b.getRotation()][i][j] == 1) { // Es werden nur de ausgef�llten Bl�cke �berpr�ft die "leeren" Bl�cke werden ausgelassen
						
						if (b.getX() + (i + 2) >= 11) { // versetzte Werte des Blockes = Wand, dann Kollision
							System.out.println("rechte Wand");
							return true; // Kollision vorhanden es wird true zur�ckgegeben.
						}
					}
				}
			}
			break;
		}

		return false; // keine Kollision, false wird zur�ckgegeben
	}

	/*
	 * Methode die den Block fals diese unten angekommen ist in das 2-dimensional
	 * Maparray �bertr�gt. Der Block b wird dabei mit�bergeben
	 */
	private static void fillBlock(Block b) {
		try {
			for (int i = 0; i < b.getBounds()[b.getRotation()].length; i++) { // 1. Schleife durchl�uft alle x Werte des Blockes
				
				for (int j = 0; j < b.getBounds()[b.getRotation()][i].length; j++) { // 2. Schleife durchl�uft alle y_werte des Blockes
					
					if (b.getBounds()[b.getRotation()][i][j] == 1) { // leere Teile des Blockes werden ausgelassen
						Game.map[b.getX() + i][b.getY() + j] = b.getTypeValue(); // die Positionen des Maparray werden jeweils mit dem Wert des Blockes 1-7

					}

				}
			}

			Audio.play("rsc/Music/fall.wav", false, 0); // Der Sound f�r das Erreichen des Ende des Blockfallens wird abgespielt
			
			System.out.println("Block filled"); // debug
		} catch (Exception e) {

		}
		checkLoose(); // das verlieren wird �berpr�ft (Methode weiter unten)
	}

	/*
	 * Methode zum �berpr�fen der vollst�ndigkeit der Reihen, damit diese leer
	 * gesetzt werden und die Punkte gez�hlt werden k�nnen
	 */

	public static void checkFullRow(int multiplier) {

		int blocksInRow = 0; // Variable zum Z�hlen der gef�llten K�stchen in einer Reihe

		for (int y = Game.map[0].length - 1; y > 0; y--) { // Die einzelnen Reihen werden nach und nach von unten nach oben abgelaufen
			
			for (int x = 0; x < Game.map.length; x++) { // Die einzelnen Zellen einer Reihe werden durchlaufen

				if (Game.map[x][y] > 0) { // f�r jeden ausgef�llten Block wird die Variable eins hochgesetzt
					blocksInRow++;
				}
			}
			if (blocksInRow == 10) { // Falls Reihe ausgef�llt, dann wird dies ausgef�hrt
				Game.scoreToAdd += (10 * multiplier); // der durch die Reihe erzeugte Score wird zu Variable addiert
				delRow(y, multiplier); // die volle Reihe wird gel�scht
				Audio.play("rsc/Music/clear.wav", false, 0); // clear sound wird abgespielt
				break;
			} else {
				blocksInRow = 0;
			}

		}

		Game.score += Game.scoreToAdd; // endg�ltigen Score to add wird auf den eigentlichen Score addiert
		Game.scoreToAdd = 0; // Variable wird zur�ckgesetzt

		if (Game.score > Game.highscore) { // If Verzweigung, fall der neue Score h�her als der Highscore ist
			Game.highscore = Game.score; // Highscore auf neuen Wert setzten
			DataHandler.save(); // Highscore in Datei spiechern
			Audio.play("rsc/Music/succes.wav", false, 0); // Sound wird abgespielt, da Highscore �berholt
		}
	}

	/*
	 * Methode zum l�schen aller Werte einer vollen Reihe
	 */
	private static void delRow(int row, int multiplier) {

		for (int i = 0; i < Game.map.length; i++) { // Alle Werte der Reihe werden auf null gesetzt (keine Bl�cke)
			Game.map[i][row] = 0;
		}

		for (int y = row; y > 1; y--) { // alle reihen oberhalb der gel�schten Reihe werden eins nach unten verschoben
			for (int x = 0; x < Game.map.length; x++) {
				Game.map[x][y] = Game.map[x][y - 1]; // Verschiebung der Reihen nach unten
			}

		}
		checkFullRow(multiplier + 1); // erneutes Checken aller Reihen auf Vollst�ndig, multiplier erh�hung da nun mehrere Reihen auf einmal gel�scht werden
		
	}

	/*
	 * Methode zum �berpr�fen einer Niederlage
	 */
	private static void checkLoose() {
		for (int x = 0; x < Game.map.length; x++) { // Durchlaufen aller x-Werte der obersten Reihe

			if (Game.map[x][0] > 0) { // Falls in der obersten Reihe( au�erhalb des eigentlichen Spielfelds)ein Block
										// ist wird dies ausgef�hrt
				Game.gamestate = GameState.gameover; // Der Gamestate wird auf Gameover gesetzt
				Audio.play("rsc/Music/gameover.wav", false, 0); // Sound des Verlierens
			}

		}
	}

}