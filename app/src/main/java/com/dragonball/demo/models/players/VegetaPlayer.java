package com.dragonball.demo.models.players;

import com.dragonball.demo.R;
import com.dragonball.demo.models.attack.Attack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VegetaPlayer
		extends Player {

	private int playerImageId;
	private List<Attack> attacks;

	public VegetaPlayer(String name) {
		super(name);
		playerImageId = R.drawable.ic_vegeta_player;
		attacks = buildAttacks();
	}

	private List<Attack> buildAttacks() {
		return Arrays
				.asList(new Attack(30, R.drawable.ic_vegeta_punch), new Attack(40, R.drawable.ic_vegeta_punch), new Attack(60, R.drawable.ic_vegeta_super_hit));
	}

	@Override
	public int getPlayerImageId() {
		return playerImageId;
	}

	@Override
	public Attack getRandomAttack() {
		int index = new Random().nextInt(attacks.size());
		return attacks.get(index);
	}
}
