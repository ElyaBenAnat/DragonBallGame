package com.dragonball.demo.models.players;

import com.dragonball.demo.models.attack.Attack;

public abstract class Player {

	private String name;
	private int hp = 100;
	private int score = 0;
	private double lat = 0;
	private double log = 0;

	public Player(String name) {
		this.name = name;
	}

	public void performAttack(Attack attack, Player enemyPlayer) {
		enemyPlayer.hp -= attack.getDamage();
	}

	public String getName() {
		return name;
	}

	public int getHp() {
		return hp;
	}

	abstract public Attack getRandomAttack();

	abstract public int getPlayerImageId();

	public void setScore(int score) {
		this.score = score;
	}

	public void setLocation(double lat, double log) {
		this.lat = lat;
		this.log = log;
	}

	public int getScore() {
		return score;
	}

	public double getLat() {
		return lat;
	}

	public double getLog() {
		return log;
	}
}
