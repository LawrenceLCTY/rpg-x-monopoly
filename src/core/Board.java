package core;

import util.Util;

import java.util.Random;

public class Board {

	TileType[] boardTiles;

	public Board() {
		Random rand = new Random();

		boardTiles = new TileType[32];
		for (int i = 0; i < 32; i++) {
			//initialize all tiles as empty
			boardTiles[i] = TileType.EMPTY;
		}
		//hard code non-monster tiles
		boardTiles[0] = TileType.START;
		boardTiles[16] = TileType.CHEST;
		boardTiles[8] = boardTiles[24] = TileType.SHOP;

		TileType[] monsterTypes = {TileType.SIN_M, TileType.DUO_M, TileType.TRI_M};
		int[] monsterCount = {12, 8, 4};
		//generate monster_type[i] tiles for monsterCount[i] times
		for (int i = 0; i < 3; i++) {
			int count = 0;
			do {
				//generate number from 1 to 31 (since 0 is START TILE)
				int pos = rand.nextInt(31) + 1;
				//leave hard-coded tiles untouched
				if (pos % 4 != 0 && boardTiles[pos] == TileType.EMPTY) {
					count++;
					boardTiles[pos] = monsterTypes[i];
				}
			}
			while (count < monsterCount[i]);
		}
	}

	public TileType getTileOn(int index) {
		index = Util.wrapAroundClamp(index, 0, boardTiles.length - 1);
		return boardTiles[index];
	}

	public void draw() {
		clearScreen();
	}

	private void clearScreen() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (Exception E) {
			System.out.println(E);
		}
	}
}
