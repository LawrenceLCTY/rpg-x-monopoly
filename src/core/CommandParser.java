package core;

import util.Util;

import java.util.Scanner;

// Mostly dealing with int inputs
// ex.
// 1. Option A
// 2. Option B
// 3. Option C
// > [option number]

public class CommandParser {
	private final static Scanner scn = new Scanner(System.in);

	public static int readInt(int[] choices) {
		while (true) {
			try {
				int v = getIntInput();

				if (Util.intArrayContains(choices, v)) {
					return v;
				}
				else {
					System.out.println("Invalid choice. Chose again.");
				}
			} catch (Exception ignored) {
				System.out.println("Invalid format. Try again.");
				scn.next();
			}
		}
	}

	private static int getIntInput() {
		System.out.print("> ");
		return scn.nextInt();
	}
}
