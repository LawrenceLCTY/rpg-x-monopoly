package core;

import util.Util;

import java.util.ArrayList;

public class Battle {
	//instance variables for PvM
	Player player;
	Monster[] monsters;

	//instance variables for PvP
	Player player1, player2;

	boolean isPlayerTurn = true;

	public Battle(Player player, int monsterCount) {
		this.player = player;
		monsters = new Monster[monsterCount];

		for (int i = 0; i < monsters.length; i++) {
			monsters[i] = new Monster(player);
		}
	}

	public Battle(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	public void start(Board board, BoardPanel boardPanel) {
		while ((player.Health > 0 && monsters.length > 0)) {
			if (isPlayerTurn) {
				//check for player effect
				if (player.negativeEffect.equals("Poisoned")) {
					System.out.printf("%n%dHP poison damage inflicted on [Player %d](%dHP)%n"
						, ((int) (player.Health * 0.1)), player.getId(), player.Health);
					player.Health -= ((int) (player.Health * 0.1));
				}

				System.out.printf("%n[Player %d](%dHP) is in battle%n", player.getId(), player.Health);
				System.out.println("1. Attack");
				System.out.println("2. Use Item");
				System.out.printf("3. Flee(%d%c)%n", player.Agility, '%');

				int choice = CommandParser.readInt(new int[]{1, 2, 3});

				switch (choice) {
					case 1:
						board.draw();
						System.out.printf("%n[Player %d](%dHP) chose monster to attack%n",
							player.getId(), player.Health);

						System.out.println("1. Back");
						// List all monsters
						for (int i = 0; i < monsters.length; i++) {
							Monster monster = monsters[i];
							System.out.printf("%d. Attack Lv%d %s(%dHP)%n",
								i + 2, monster.Level, monster.Type, monster.Health);
						}

						// Dynamically populate choices based on monsters
						int[] monsterChoices = new int[monsters.length + 1];
						for (int i = 0; i < monsterChoices.length; i++) {
							monsterChoices[i] = i + 1;
						}

						int monsterChoice = CommandParser.readInt(monsterChoices);

						board.draw();

						// Return to battle menu
						if (monsterChoice == 1) {
							continue;
						}

						// Attack monster
						int monsterIndex = monsterChoice - 2;
						Monster monsterToAttack = monsters[monsterIndex];

						//monster dodge attack probability 0~20%
						if (Math.random() * 500 > monsterToAttack.Agility) {

							int damageToMonster = player.Strength * 100 / (100 + monsterToAttack.Defense);
							monsterToAttack.Health -= Math.min(monsterToAttack.Health, damageToMonster);
							System.out.printf("%n[Player %d](%dHP) inflicted %dHP damage on Lv%d %s(%dHP)%n",
								player.getId(), player.Health, damageToMonster, monsterToAttack.Level, monsterToAttack.Type, monsterToAttack.Health);
							if (player.positiveEffect.equals("Weapon Enchant")) {
								monsterToAttack.Health -= 30;
								System.out.printf("%n[Player %d](%dHP)'s enchanted weapon deal another 30HP damage on Lv%d %s(%dHP)%n",
									player.getId(), player.Health, monsterToAttack.Level, monsterToAttack.Type, monsterToAttack.Health);
							}
						}
						else {
							//dodged
							System.out.printf("%nLv %d %s(%dHP) dodged [Player %d](%dHP)'s attack%n",
								monsterToAttack.Level, monsterToAttack.Type, monsterToAttack.Health, player.getId(), player.Health);
						}

						//monster defeated
						if (monsterToAttack.Health <= 0) {
							System.out.printf("%n[Player %d](%dHP) defeated Lv%d %s(%dHP)%n",
								player.getId(), player.Health, monsterToAttack.Level, monsterToAttack.Type, monsterToAttack.Health);
							//remove monsterToAttack from monsters
							Monster[] tempArrayMonsters = new Monster[monsters.length - 1];

							//player gets item
							int dropId = 0;
							int selectDrop = monsterToAttack.Level;
							switch (selectDrop) {
								case 1:
									dropId = Util.RandomBetween(0, 6);
									break;
								case 2:
									dropId = Util.RandomBetween(0, 18);
									break;
								case 3:
									dropId = Util.RandomBetween(0, 25);
									break;
								case 4:
									dropId = Util.RandomBetween(0, 29);
									break;
							}
							Item itemDrop = Game.itemList.get(dropId);

							//player gets item drop
							player.addItem(itemDrop);
							System.out.printf("%n[Player %d](%dHP) got %s from drop%n",
								player.getId(), player.Health, itemDrop.Name);

							//player gets gold
							player.Gold += monsterToAttack.Gold;
							System.out.printf("%n[Player %d](%dHP) gained %d Gold%n",
								player.getId(), player.Health, monsterToAttack.Gold);
							if (player.positiveEffect.equals("Lucky")) {
								player.Gold += monsterToAttack.Gold;
								System.out.printf("%n[Player %d](%dHP) is lucky and gained another %d Gold%n",
									player.getId(), player.Health, monsterToAttack.Gold);
							}

							//player gains exp
							player.Exp += monsterToAttack.Exp;
							System.out.printf("%n[Player %d](%dHP) gained %d EXP%n",
								player.getId(), player.Health, monsterToAttack.Exp);

							//check for level up
							player.levelUp();

							for (int i = 0, k = 0; i < monsters.length; i++) {
								//copy all monsters except monsterToAttack
								if (i != monsterIndex) {
									tempArrayMonsters[k++] = monsters[i];
								}
							}
							monsters = tempArrayMonsters.clone();
						}

						break;
					case 2:
						board.draw();

						int res = tryUseItem(player);

						if (res == 0)
							continue;
						else if (res == 1)
							break;
						else if (res == 2) {
							System.out.printf("%n[Player %d](%dHP) choose monster to use poison%n"
								, player.getId(), player.Health);

							System.out.println("1. Back");
							// List all monsters
							for (int i = 0; i < monsters.length; i++) {
								Monster monster = monsters[i];
								System.out.printf("%d. Attack Lv%d %s(%dHP)%n",
									i + 2, monster.Level, monster.Type, monster.Health);
							}

							// Dynamically populate choices based on monsters
							monsterChoices = new int[monsters.length + 1];
							for (int i = 0; i < monsterChoices.length; i++) {
								monsterChoices[i] = i + 1;
							}

							monsterChoice = CommandParser.readInt(monsterChoices);
							if (monsterChoice == 1) {
								continue;
							}

							int monsterSelectIndex = monsterChoice - 2;
							Monster monsterToPoison = monsters[monsterSelectIndex];
							monsterToPoison.negativeEffect = "Poisoned";
							System.out.printf("%nLv %d %s(%dHP) is poisoned%n",
								monsterToPoison.Level, monsterToPoison.Type, monsterToPoison.Health);
							break;
						}
						else
							return;
					case 3:
						board.draw();

						if (Math.random() * 100 < player.Agility) {
							System.out.printf("%n[Player %d] fled the battle%n",
								player.getId());
							return;
						}
						else {
							System.out.printf("%n[Player %d] flee unsuccessful%n",
								player.getId());
						}

						break;
				}
			}
			else {
				// Monsters turn
				System.out.printf("%nMonsters' turn to attack%n");

				for (Monster attackingMonster : monsters) {
					//each monster attack in turn

					//check monster's effect
					if (attackingMonster.negativeEffect.equals("Poisoned")) {
						System.out.printf("%n%dHP poison damage inflicted on Lv%d %s(%dHP)%n"
							, ((int) (attackingMonster.Health * 0.1)), attackingMonster.Level, attackingMonster.Type, attackingMonster.Health);

						attackingMonster.Health -= ((int) (attackingMonster.Health * 0.1));
					}

					//player dodge attack probability 0~45%
					if (Math.random() * 200 > player.Agility) {
						//dodge failed
						int damageToPlayer = attackingMonster.Strength * 100 / (100 + player.Defense);
						player.Health -= Math.min(player.Health, damageToPlayer);
						System.out.printf("%nLv%d %s(%dHP) inflicted %dHP damage on [Player %d](%dHP)%n",
							attackingMonster.Level, attackingMonster.Type, attackingMonster.Health, damageToPlayer, player.getId(), player.Health);
						attackingMonster.useAbility(player);//use monster ability
						if (player.Health <= 0) {
							//player defeated
							System.out.printf("%n[Player %d](%dHP) was defeated by Lv %d %s(%dHP)%n",
								player.getId(), player.Health, attackingMonster.Level, attackingMonster.Type, attackingMonster.Health);
						}
					}
					else {
						//dodged
						System.out.printf("%n[Player %d](%dHP) dodged Lv%d %s(%dHP)'s attack%n",
							player.getId(), player.Health, attackingMonster.Level, attackingMonster.Type, attackingMonster.Health);
					}
				}

			}

			// Toggle turn
			isPlayerTurn = !isPlayerTurn;
		}

		//player effect, bonus end.
		potionBonusEnd(player);
		player.negativeEffect = "";
		player.positiveEffect = "";
		player.tempAbilityToZero();

	}

	public void PvPStart(Board board) {
		System.out.printf("%n[Player %d](%dHP) will fight [Player %d](%dHP)%n",
			player1.getId(), player1.Health, player2.getId(), player2.Health);

		while ((player1.Health > 0 && player2.Health > 0)) {
			if (isPlayerTurn) {
				//check for effect
				if (player1.negativeEffect.equals("Poisoned")) {
					System.out.printf("%n%dHP poison damage inflicted on [Player %d](%dHP)%n"
						, ((int) (player1.Health * 0.1)), player1.getId(), player1.Health);
					player1.Health -= ((int) (player1.Health * 0.1));
				}

				System.out.printf("%n[Player %d](%dHP) is in battle%n", player1.getId(), player1.Health);
				System.out.printf("1. Attack [Player %d](%dHP)%n", player2.getId(), player2.Health);
				System.out.println("2. Use Item");
				System.out.printf("3. Flee(%d%c)%n", player1.Agility, '%');

				int choice = CommandParser.readInt(new int[]{1, 2, 3});

				switch (choice) {
					case 1:
						board.draw();
						attackPlayer(player1, player2);
						if (player2.Health <= 0) {
							return;//end battle already
						}
						break;
					case 2:
						board.draw();

						int res = tryUseItem(player1);

						if (res == 0)
							continue;
						else if (res == 1)
							break;
						else if (res == 2) {
							player2.negativeEffect = "Poisoned";
							System.out.printf("%n[Player %d](%dHP) is poisoned%n", player2.getId(), player2.Health);
							break;
						}
						else
							return;
					case 3:
						board.draw();

						if (Math.random() * 100 < player1.Agility) {
							System.out.printf("%n[Player %d] fled the battle%n",
								player1.getId());
							return;
						}
						else {
							System.out.printf("%n[Player %d] flee unsuccessful%n",
								player1.getId());
						}
						break;
				}
			}
			else {
				//check for effect
				if (player2.negativeEffect.equals("Poisoned")) {
					System.out.printf("%n%dHP poison damage inflicted on [Player %d](%dHP)%n"
						, ((int) (player2.Health * 0.1)), player2.getId(), player2.Health);
					player2.Health -= ((int) (player2.Health * 0.1));
				}

				System.out.printf("%n[Player %d](%dHP) is in battle%n", player2.getId(), player2.Health);
				System.out.printf("1. Attack [Player %d](%dHP)%n", player1.getId(), player1.Health);
				System.out.println("2. Use Item");
				System.out.printf("3. Flee(%d%c)%n", player2.Agility, '%');

				int choice = CommandParser.readInt(new int[]{1, 2, 3});

				switch (choice) {
					case 1:
						board.draw();
						attackPlayer(player2, player1);
						break;
					case 2:
						board.draw();

						int res = tryUseItem(player2);

						if (res == 0)
							continue;
						else if (res == 1)
							break;
						else if (res == 2) {
							player1.negativeEffect = "Poisoned";
							System.out.printf("%n[Player %d](%dHP) is poisoned%n", player1.getId(), player1.Health);
							break;
						}
						else
							return;
					case 3:
						board.draw();

						if (Math.random() * 100 < player2.Agility) {
							System.out.printf("%n[Player %d] fled the battle%n",
								player2.getId());
							return;
						}
						else {
							System.out.printf("%n[Player %d] flee unsuccessful%n",
								player2.getId());
						}
						break;
				}
			}
			isPlayerTurn = !isPlayerTurn;
		}

		//both player effect, bonus end.
		potionBonusEnd(player1);
		player1.negativeEffect = "";
		player1.positiveEffect = "";
		player1.tempAbilityToZero();
		potionBonusEnd(player2);
		player2.negativeEffect = "";
		player2.positiveEffect = "";
		player2.tempAbilityToZero();
	}

	public void attackPlayer(Player player, Player playerToAttack) {
		if (Math.random() * player.Agility > Math.random() * playerToAttack.Agility) {

			int damage = player.Strength * 100 / (100 + playerToAttack.Defense);
			playerToAttack.Health -= Math.min(playerToAttack.Health, damage);
			System.out.printf("%n[Player %d](%dHP) inflicted %dHP damage on [Player %d](%dHP)%n",
				player.getId(), player.Health, damage, playerToAttack.getId(), playerToAttack.Health);
			if (player.positiveEffect.equals("Weapon Enchant")) {
				playerToAttack.Health -= 30;
				System.out.printf("%n[Player %d](%dHP)'s enchanted weapon deal another 30HP damage on [Player %d](%dHP)%n",
					player.getId(), player.Health, playerToAttack.getId(), playerToAttack.Health);
			}
		}
		else {
			//dodged
			System.out.printf("%n[Player %d](%dHP) dodged [Player %d](%dHP)'s attack%n",
				playerToAttack.getId(), playerToAttack.Health, player.getId(), player.Health);
		}

		//player defeated
		if (playerToAttack.Health <= 0) {
			System.out.printf("%n[Player %d](%dHP) defeated [Player %d](%dHP)%n",
				player.getId(), player.Health, playerToAttack.getId(), playerToAttack.Health);

			//player gets playerToAttack items
			ArrayList<Item> defeatedInventory = playerToAttack.getItems();
			if (defeatedInventory.size() != 0) {
				Item defeatedDrop = defeatedInventory.get(Util.RandomBetween(0, defeatedInventory.size() - 1));
				player.addItem(defeatedDrop);
				System.out.printf("%n[Player %d](%dHP) get %s from [Player %d](%dHP)%n",
					player.getId(), player.Health, defeatedDrop.Name, playerToAttack.getId(), playerToAttack.Health);
			}
			else {
				System.out.printf("%n[Player %d](%dHP) does not have item to give [Player %d](%dHP)%n",
					playerToAttack.getId(), playerToAttack.Health, player.getId(), player.Health);
			}

			//player gets gold = 50% playerToAttack gold 
			player.Gold += (playerToAttack.Gold / 2);
			System.out.printf("%n[Player %d](%dHP) gained %d Gold%n",
				player.getId(), player.Health, (playerToAttack.Gold / 2));
			if (player.positiveEffect.equals("Lucky")) {
				player.Gold += (playerToAttack.Gold / 2);
				System.out.printf("%n[Player %d](%dHP) is lucky and gained another %d Gold%n",
					player.getId(), player.Health, (playerToAttack.Gold / 2));
			}

			//player gains exp = 50% playerToAttack exp
			player.Exp += playerToAttack.Exp / 2;
			System.out.printf("%n[Player %d](%dHP) gained %d EXP%n",
				player.getId(), player.Health, playerToAttack.Exp / 2);

			//check for level up
			player.levelUp();
		}
	}

	// 0 if didn't use item, 1 if used item, 3 if used smoke bomb
	private int tryUseItem(Player player) {
		System.out.printf("%n[Player %d] chose item to use%n", player.getId());
		System.out.println("1. Back");

		int choiceSize = 1;

		for (int i = 0; i < player.getItems().size(); i++) {
			Item item = player.getItems().get(i);

			if (item.IsUsable) {
				System.out.printf("%d. Use %s%n", choiceSize + 1, item.Name);
				choiceSize++;
			}
		}

		int[] itemChoices = new int[choiceSize];
		for (int i = 0; i < itemChoices.length; i++) {
			itemChoices[i] = i + 1;
		}

		int itemChoice = CommandParser.readInt(itemChoices);
		if (itemChoice == 1)
			return 0;

		int itemIndex = itemChoice - 2;

		Item itemToUse = null;

		for (int i = 0, c = 0; i < player.getItems().size(); i++) {
			Item item = player.getItems().get(i);

			if (item.IsUsable) {
				if (itemIndex == c) {
					itemToUse = item;
					player.removeItem(i);
				}

				c++;
			}
		}

		itemToUse.use(player);

		if (itemToUse.Name.equals("Smoke Bomb")) {
			System.out.printf("%n[Player %d] escaped the battle%n", player.getId());
			return 3;
		}
		else if (itemToUse.Name.equals("Poison"))
			return 2;
		else {
			if (itemToUse.Name.equals("Strength Potion")) {
				System.out.printf("%n[Player %d](%dHP) use %s, strength increase by %d%n",
					player.getId(), player.Health, itemToUse.Name, ((int) (player.Strength * 0.2)));
				potionBonusEnd(player);
				player.tempAbilityToZero();
				player.tempStrength = ((int) (player.Strength * 0.2));
				potionBonus(player);
			}
			if (itemToUse.Name.equals("Agility Potion")) {
				System.out.printf("%n[Player %d](%dHP) use %s, agility increase by %d%n",
					player.getId(), player.Health, itemToUse.Name, ((int) (player.Agility * 0.2)));
				potionBonusEnd(player);
				player.tempAbilityToZero();
				player.tempAgility = ((int) (player.Agility * 0.2));
				potionBonus(player);
			}
			if (itemToUse.Name.equals("Defence Potion")) {
				System.out.printf("%n[Player %d](%dHP) use %s, defense increase by %d%n",
					player.getId(), player.Health, itemToUse.Name, ((int) (player.Defense * 0.2)));
				potionBonusEnd(player);
				player.tempAbilityToZero();
				player.tempDefense = ((int) (player.Defense * 0.2));
				potionBonus(player);
			}
			if (itemToUse.Name.equals("Ability Potion")) {
				System.out.printf("%n[Player %d](%dHP) use %s, strength +%d, agility +%d and defense +%d %n",
					player.getId(), player.Health, itemToUse.Name,
					((int) (player.Strength * 0.2)), ((int) (player.Agility * 0.2)), (int) (player.Defense * 0.2));
				potionBonusEnd(player);
				player.tempAbilityToZero();
				player.tempStrength = ((int) (player.Strength * 0.2));
				player.tempAgility = ((int) (player.Agility * 0.2));
				player.tempDefense = ((int) (player.Defense * 0.2));
				potionBonus(player);
			}
			if (itemToUse.Name.equals("Rage Potion")) {
				System.out.printf("%n[Player %d](%dHP) use %s, strength +%d, agility +%d and defense +%d %n",
					player.getId(), player.Health, itemToUse.Name,
					((int) (player.Strength * 0.5)), ((int) (player.Agility * 0.5)), (int) (player.Defense * 0.5));
				potionBonusEnd(player);
				player.tempAbilityToZero();
				player.tempStrength = ((int) (player.Strength * 0.5));
				player.tempAgility = ((int) (player.Agility * 0.5));
				player.tempDefense = ((int) (player.Defense * 0.5));
				potionBonus(player);
			}
			if (itemToUse.Name.equals("HP Potion")) {
				System.out.printf("%n[Player %d](%dHP) use %s, recover %d HP%n",
					player.getId(), player.Health, itemToUse.Name, ((int) (player.Health * 0.2)));
				player.Health += ((int) (player.Health * 0.2));
			}
			if (itemToUse.Name.equals("Nasi Lemak")) {
				System.out.printf("%n[Player %d](%dHP) eat %s, recover 50 HP%n",
					player.getId(), player.Health, itemToUse.Name);
				player.Health += 50;
			}
			if (itemToUse.Name.equals("Antidote")) {
				if (player.negativeEffect.equals("Poisoned")) {
					player.negativeEffect = "";
					System.out.printf("%n[Player %d] release effect of poison with %s%n", player.getId(), itemToUse.Name);
				}
				else {
					System.out.printf("%n[Player %d] is not poisoned%n", player.getId());
				}
			}
			if (itemToUse.Name.equals("Weapon Enchanter")) {
				player.positiveEffect = "Weapon Enchant";
				System.out.printf("%n[Player %d] weapon is enchanted%n", player.getId());
			}
			if (itemToUse.Name.equals("Lucky Potion")) {
				player.positiveEffect = "Lucky";
				System.out.printf("%n[Player %d] is now lucky%n", player.getId());
			}
			return 1;
		}
	}

	public void potionBonus(Player player) {
		player.Strength += player.tempStrength;
		player.Defense += player.tempDefense;
		player.Agility += player.tempAgility;
		player.Agility = Math.max(0, Math.min(player.Agility, 90));
	}

	public void potionBonusEnd(Player player) {
		player.Strength -= player.tempStrength;
		player.Defense -= player.tempDefense;
		player.Agility -= player.tempAgility;
		player.Agility = Math.max(0, Math.min(player.Agility, 90));
	}
}

