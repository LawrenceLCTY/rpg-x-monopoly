package core;

import java.util.Random;

public class DiceRoller {
	public static int Roll() {
		return new Random().nextInt(6) + 1;
	}
}
