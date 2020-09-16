package com.dragonball.demo.ui.menu;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dragonball.demo.R;
import com.dragonball.demo.ui.game.GameFragment;
import com.dragonball.demo.ui.score.ScoreFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MenuFragment
		extends Fragment
		implements OnClickListener {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_menu, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		initButtons(view);
	}

	private void initButtons(View view) {
		View playBtn = view.findViewById(R.id.play_btn);
		View scoreBtn = view.findViewById(R.id.score_btn);
		playBtn.setOnClickListener(this);
		scoreBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.play_btn:
				if (haveLocationPermission()) {
					startGame();
				} else {
					requestPermission();
				}
				break;

			case R.id.score_btn:
				showScoreBoard();
				break;
		}
	}

	private void requestPermission() {
		String[] p = {permission.ACCESS_FINE_LOCATION};
		requestPermissions(p, 123);
	}

	private void showScoreBoard() {
		ScoreFragment scoreFragment = new ScoreFragment();
		FragmentTransaction ft = getParentFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, scoreFragment, scoreFragment.getClass().getSimpleName()).addToBackStack(scoreFragment.getClass().getSimpleName());
		ft.commit();
	}

	private void startGame() {
		GameFragment gameFragment = new GameFragment();
		FragmentTransaction ft = getParentFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, gameFragment, gameFragment.getClass().getSimpleName()).addToBackStack(gameFragment.getClass().getSimpleName());
		ft.commit();
	}

	private boolean haveLocationPermission() {
		int permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);
		return permission == PackageManager.PERMISSION_GRANTED;
	}
}
