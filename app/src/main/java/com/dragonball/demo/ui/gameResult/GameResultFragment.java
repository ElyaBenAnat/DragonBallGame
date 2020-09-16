package com.dragonball.demo.ui.gameResult;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dragonball.demo.R;
import com.dragonball.demo.models.players.Player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameResultFragment
		extends Fragment
		implements OnClickListener {

	private Player player;
	private ImageView imageView;

	public GameResultFragment(Player player) {
		this.player = player;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_game_result, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		initViews(view);
	}

	private void initViews(View view) {
		TextView nameTv = view.findViewById(R.id.player_name);
		TextView attackTv = view.findViewById(R.id.attack_number);
		TextView okTv = view.findViewById(R.id.ok_btn);
		imageView = view.findViewById(R.id.player_img);
		nameTv.setText(player.getName());
		attackTv.setText(String.valueOf(player.getScore()));
		loadPlayerImage();
		okTv.setOnClickListener(this);
	}

	private void loadPlayerImage() {
		Glide.with(this).load(getResources().getDrawable(player.getPlayerImageId(), requireContext().getTheme())).into(imageView);
	}

	@Override
	public void onClick(View v) {
		getParentFragmentManager().popBackStack();
	}
}
