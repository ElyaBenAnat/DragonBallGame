package com.dragonball.demo.models.players;

public class PlayerModel {

	public String name;
	public int score;
	public double lat;
	public double log;

	public PlayerModel(String name, int score, double lat, double log) {
		this.name = name;
		this.score = score;
		this.lat = lat;
		this.log = log;
	}
}
