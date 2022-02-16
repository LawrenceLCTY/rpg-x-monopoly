package core;

import util.Util;

public class Monster extends Role {
	public Monster(Player player) {
		Level = 1;
		negativeEffect = "";
		maxLevel = 5;

		Types = new String[]{"Tanker", "Lunatic", "Trickster", "Fallen Mage"};
		// Health Strength Defense Agility (By level)
		Stats = new int[][][]{
			/*Tanker*/{{100, 250, 400, 550, 700}, {10, 20, 30, 40, 50}, {50, 60, 70, 80, 90}, {5, 10, 15, 20, 25}},
			/*Lunatic*/{{50, 100, 150, 200, 250}, {30, 50, 70, 90, 110}, {20, 30, 40, 50, 60}, {5, 15, 25, 35, 45}},
			/*Trickster*/{{50, 150, 250, 350, 450}, {20, 30, 40, 50, 60}, {30, 40, 50, 60, 70}, {10, 30, 50, 70, 90}},
			/*Fallen Mage*/{{100, 200, 300, 400, 500}, {10, 20, 30, 40, 50}, {10, 20, 30, 40, 50}, {5, 10, 15, 20, 25}}
		};

		// Generate values and types based on player level.
		int choiceType = Util.RandomBetween(0, 3);
		switch (choiceType) {
			case 0:
				Type = "Tanker";
				break;

			case 1:
				Type = "Lunatic";
				break;

			case 2:
				Type = "Trickster";
				break;
			case 3:
				Type = "Fallen Mage";
				break;
		}

		if (Util.RandomBetween(1, 20) == 1) {
			Level = player.Level + 1;
		}
		else if (Util.RandomBetween(1, 10) == 1) {
			Level = player.Level - 1;
		}
		else {
			Level = player.Level;
		}
		Level = Math.max(1, Math.min(Level, maxLevel));

		Gold = Util.RandomBetween(10, 50) * Level;
		Exp = Util.RandomBetween(10, 30) * Level;

		Health = Stats[choiceType][0][Level - 1];
		Strength = Stats[choiceType][1][Level - 1];
		Defense = Stats[choiceType][2][Level - 1];
		Agility = Stats[choiceType][3][Level - 1];
	}

	public void useAbility(Player player) {
		if (Type.equals("Tanker")) {
			if (Util.RandomBetween(1, 10) == 1) {//10% chance
				System.out.printf("Lv%d %s(%dHP) used ability: Giant's Wrath%n", Level, Type, Health);
				//does 5*Lv% true damage to self and same value true damage to player
				int damageToSelf = Health * Level / 20;
				System.out.printf("Lv%d %s(%dHP) inflicted %dHP damage to self%n", Level, Type, Health, damageToSelf);
				Health -= Math.min(Health, damageToSelf);
				player.Health -= Math.min(player.Health, damageToSelf);
				System.out.printf("Lv%d %s(%dHP) inflicted %dHP damage on [Player %d](%dHP)%n",
					Level, Type, Health, damageToSelf, player.getId(), player.Health);
			}
		}
		else if (Type.equals("Trickster")) {
			if (Util.RandomBetween(1, 10) == 1) {//10%chance
				//causes player to self harm
				System.out.printf("Lv%d %s(%dHP) used ability: Confusion%n", Level, Type, Health);
				int damageToPlayer = player.Strength * 50 / (100 + player.Defense);
				player.Health -= Math.min(player.Health, damageToPlayer);
				System.out.printf("[Player %d](%dHP) inflicted %dHP damage to self%n",
					player.getId(), player.Health, damageToPlayer);
			}
		}
		else if (Type.equals("Lunatic")) {
			if (Util.RandomBetween(1, 10) == 1) {//10%chance
				//critical hit = normal hit * 1.5
				System.out.printf("Lv%d %s(%dHP) used ability: Critical Hit%n", Level, Type, Health);
				int damageToPlayer = Strength * 150 / (100 + player.Defense);
				player.Health -= Math.min(player.Health, damageToPlayer);
				System.out.printf("Lv%d %s(%dHP) inflicted %dHP damage on [Player %d](%dHP)%n",
					Level, Type, Health, damageToPlayer, player.getId(), player.Health);
			}
		}
		else if (Type.equals("Fallen Mage")) {
			if (Util.RandomBetween(1, 10) > 1) {//90%chance{
				//regens 10% current health
				System.out.printf("Lv%d %s(%dHP) used ability: Regeneration%n", Level, Type, Health);
				int regen = Health / 10;
				Health += regen;
				System.out.printf("Lv%d %s(%dHP) recovered %dHP%n", Level, Type, Health, regen);
			}
		}
	}
}
