package com.dragonball.demo.db;

import com.dragonball.demo.models.players.PlayerModel;

import java.util.ArrayList;

public interface DataBaseListener {

	void onPlayerList(ArrayList<PlayerModel> playerList);
}
