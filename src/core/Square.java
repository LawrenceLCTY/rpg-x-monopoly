package core;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Square extends JPanel {

	int number;
	private String name;
	JLabel nameLabel;
	JLabel[] players = new JLabel[4];
	static int totalSquares = 0;

	public String getName() {
		return name;
	}


	public Square(int xCoord, int yCoord, int width, int height, String labelString) {
		number = totalSquares;
		totalSquares++;
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(xCoord, yCoord, width, height);
		name = labelString;

		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;

		nameLabel = new JLabel(labelString);
		nameLabel.setFont(new Font("Consolas", Font.PLAIN, 9));
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setVerticalAlignment(SwingConstants.CENTER);
		nameLabel.setBounds(0, 20, this.getWidth(), 20);
		this.add(nameLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		Color[] colors = new Color[]{Color.CYAN, Color.PINK, Color.YELLOW, Color.GREEN};
		for (int i = 0; i < 4; i++) {
			JLabel player = new JLabel("" + (i + 1));
			player.setFont(new Font("Consolas", Font.PLAIN, 16));
			player.setHorizontalAlignment(SwingConstants.CENTER);
			player.setVerticalAlignment(SwingConstants.BOTTOM);
			player.setBounds(0, 20, this.getWidth(), 20);
			player.setOpaque(true);
			player.setBackground(colors[i]);
			player.setForeground(Color.BLUE);
			player.setVisible(false);
			players[i] = player;
			gbc.gridx = i;
			this.add(player, gbc);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

	}

}
