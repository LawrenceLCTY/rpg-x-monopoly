package core;

public abstract class Role {
	public int Level;
	public int Health;
	public int Strength;
	public int Defense;
	public int Agility;
	public int Gold;
	public int Exp;
	public String negativeEffect;
	public String positiveEffect;
	public int tempStrength, tempAgility, tempDefense;
	public int maxLevel;

	public String Type;
	public String[] Types;
	public int[][][] Stats;
}
