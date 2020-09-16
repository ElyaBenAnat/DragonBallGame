package com.dragonball.demo.models.attack;

public class Attack {

	private int damage;
	private int attackImageId;

	public Attack(int damage, int attackImageId) {
		this.damage = damage;
		this.attackImageId = attackImageId;
	}

	public int getDamage() {
		return damage;
	}

	public int getAttackImageId() {
		return attackImageId;
	}
}
