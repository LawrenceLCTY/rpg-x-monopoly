package core;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;


public class BoardPanel extends JPanel {

	private JTextArea message;
	private JTextArea command;

	private ArrayList<Square> allSquares = new ArrayList<>();
	private ArrayList<Square> unbuyableSquares = new ArrayList<>(); // squares like "Go", "Chances" etc...

	public JTextArea getMessage() {
		return message;
	}

	public JTextArea getCommand() {
		return command;
	}

	public Square getSquareAtIndex(int location) {
		return allSquares.get(location);
	}

	public BoardPanel(TileType[] boardTiles) {
		JFrame jFrame = new JFrame();
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setBounds(100, 100, 450, 300);
		jFrame.setSize(1080, 720);

		setBorder(new LineBorder(new Color(0, 0, 0)));
		setSize(1080, 720);
		this.setLayout(null);
		initializeSquares(boardTiles);

		jFrame.add(this);
		jFrame.setVisible(true);
	}

	private void initializeSquares(TileType[] boardTiles) {


		int startX = 1080;
		int startY = 720;
		int width = 120;
		int height = 80;
		for (int i = 0; i < 9; i++) {

			Square square;
			if (i == 4) {
				square = new Square(startX - width, startY - height, width, height, "");
			}
			else {
				square = new Square(startX - width, startY - height, width, height, boardTiles[i].name());
			}

			this.add(square);
			allSquares.add(square);
			unbuyableSquares.add(square);
			startX = startX - width;
		}

		for (int i = 9; i < 17; i++) {

			startY = startY - height;

			Square square;
			if (i == 12) {
				square = new Square(startX, startY - height, width, height, "");
			}
			else {
				square = new Square(startX, startY - height, width, height, boardTiles[i].name());
			}
			this.add(square);
			allSquares.add(square);
			unbuyableSquares.add(square);

		}

		for (int i = 17; i < 25; i++) {

			startX = startX + width;

			Square square;
			if (i == 20) {
				square = new Square(startX, startY - height, width, height, "");
			}
			else {
				square = new Square(startX, startY - height, width, height, boardTiles[i].name());
			}
			this.add(square);
			allSquares.add(square);
			unbuyableSquares.add(square);
		}

		for (int i = 25; i < 32; i++) {

			startY = startY + height;

			Square square;
			if (i == 28) {
				square = new Square(startX, startY - height, width, height, "");
			}
			else {
				square = new Square(startX, startY - height, width, height, boardTiles[i].name());
			}
			this.add(square);
			allSquares.add(square);
			unbuyableSquares.add(square);
		}

		message = new JTextArea("");
		message.setEditable(false);
		message.setBounds(120, 80, 840, 340);
		message.setFont(new Font("Consolas", Font.PLAIN, 16));
		message.setBorder(BorderFactory.createLineBorder(Color.gray, 5));
		this.add(message);

		command = new JTextArea("");
		command.setEditable(false);
		command.setBounds(120, 420, 840, 220);
		command.setFont(new Font("Consolas", Font.PLAIN, 16));
		command.setBorder(BorderFactory.createLineBorder(Color.gray, 5));
		this.add(command);

	}

}
