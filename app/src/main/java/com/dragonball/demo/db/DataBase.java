package com.dragonball.demo.db;

import com.dragonball.demo.models.db.DataBaseModel;
import com.dragonball.demo.models.players.Player;
import com.dragonball.demo.models.players.PlayerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class DataBase {

	private static DataBase instance = null;
	private DatabaseReference dbRef;

	private DataBase() {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		dbRef = database.getReference("players");
	}

	public static DataBase getInstance() {
		if (instance == null)
			instance = new DataBase();
		return instance;
	}

	public void insertOrUpdatePlayer(final Player player) {
		dbRef.child(player.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				DataBaseModel value = snapshot.getValue(DataBaseModel.class);
				if (value == null) {
					createPlayer(player);
				} else {
					updatePlayer(value, player);
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				createPlayer(player);
			}
		});
	}

	private void createPlayer(Player player) {
		dbRef.child(player.getName()).setValue(getDataBaseModel(player));
	}

	private void updatePlayer(DataBaseModel value, Player player) {
		int score = value.score + player.getScore();
		double lat = value.lat + player.getLat();
		double log = value.log + player.getLog();
		dbRef.child(player.getName()).setValue(new DataBaseModel(score, lat, log));
	}

	private DataBaseModel getDataBaseModel(Player player) {
		return new DataBaseModel(player.getScore(), player.getLat(), player.getLog());
	}

	public void getPlayersList(final DataBaseListener listener) {
		dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				ArrayList<PlayerModel> playerList = new ArrayList<>();
				for (DataSnapshot data : snapshot.getChildren()) {
					int score = Integer.parseInt(data.child("score").getValue().toString());
					double lat = Double.parseDouble(data.child("lat").getValue().toString());
					double log = Double.parseDouble(data.child("log").getValue().toString());
					playerList.add(new PlayerModel(data.getKey(), score, lat, log));
				}
				if (listener != null) {
					listener.onPlayerList(playerList);
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				if (listener != null) {
					listener.onPlayerList(null);
				}
			}
		});
	}
}
