package com.dragonball.demo.ui.score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragonball.demo.R;
import com.dragonball.demo.db.DataBase;
import com.dragonball.demo.db.DataBaseListener;
import com.dragonball.demo.models.players.PlayerModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreFragment
		extends Fragment
		implements DataBaseListener {

	private ScoreAdapter adapter;
	private RecyclerView recyclerView;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DataBase.getInstance().getPlayersList(this);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_score, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		initViews(view);
	}

	private void initViews(View view) {
		initRecycler(view);
	}

	private void initRecycler(View view) {
		recyclerView = view.findViewById(R.id.recycler_view);
	}

	@Override
	public void onPlayerList(ArrayList<PlayerModel> playerList) {
		getSortPlayerList(playerList);
		adapter = new ScoreAdapter(playerList);
		recyclerView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void getSortPlayerList(ArrayList<PlayerModel> playerList) {
		Collections.sort(playerList, new Comparator<PlayerModel>() {
			public int compare(PlayerModel obj1, PlayerModel obj2) {
				return Integer.compare(obj2.score, obj1.score);
			}
		});
	}
}
