package com.dragonball.demo.ui.game;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dragonball.demo.R;
import com.dragonball.demo.db.DataBase;
import com.dragonball.demo.location.LocationListener;
import com.dragonball.demo.location.LocationManager;
import com.dragonball.demo.models.attack.Attack;
import com.dragonball.demo.models.players.GokuPlayer;
import com.dragonball.demo.models.players.Player;
import com.dragonball.demo.models.players.VegetaPlayer;
import com.dragonball.demo.ui.gameResult.GameResultFragment;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class GameFragment
		extends Fragment
		implements OnClickListener, LocationListener {

	private Player currentPlayer;

	private ProgressBar playerOnePb;
	private ProgressBar playerTwoPb;

	private ImageView playerOneImage;
	private ImageView playerTwoImage;

	private EditText playerOneName;
	private EditText playerTwoName;

	private LinearLayout menuContainer;

	private GokuPlayer gokuPlayer;
	private VegetaPlayer vegetaPlayer;
	private Location location;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LocationManager.getInstance(requireContext()).getLocation(this);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_game, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		initViews(view);
	}

	private void buildPlayers(String nameOne, String nameTwo) {
		gokuPlayer = new GokuPlayer(nameOne);
		vegetaPlayer = new VegetaPlayer(nameTwo);
	}

	private void initViews(View view) {
		initProgressBar(view);
		initPlayerImage(view);
		initGameMenu(view);
	}

	private void initGameMenu(View view) {
		Button startBtn = view.findViewById(R.id.start_btn);
		menuContainer = view.findViewById(R.id.game_menu_container);
		playerOneName = view.findViewById(R.id.player_a_et);
		playerTwoName = view.findViewById(R.id.player_b_et);
		startBtn.setOnClickListener(this);
	}

	private void initProgressBar(View view) {
		playerOnePb = view.findViewById(R.id.hp_player_a);
		playerTwoPb = view.findViewById(R.id.hp_player_b);
	}

	private void initPlayerImage(View view) {
		playerOneImage = view.findViewById(R.id.player_a_img);
		playerTwoImage = view.findViewById(R.id.player_b_img);
	}

	private void performAttack() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Attack attack = currentPlayer.getRandomAttack();
				loadImage(attack);
				currentPlayer.performAttack(attack, getIdlePlayer());
				updateHpBar(getIdlePlayer());
				onAttackEnded();
			}
		}, 500);
	}

	private void loadImage(Attack attack) {
		Player idlePlayer = getIdlePlayer();
		ImageView idlePlayerIv = getPlayerImageView(idlePlayer);
		ImageView activePlayerIv = getPlayerImageView(currentPlayer);
		setImage(idlePlayer.getPlayerImageId(), idlePlayerIv);
		setImage(attack.getAttackImageId(), activePlayerIv);
	}

	private ImageView getPlayerImageView(Player player) {
		if (player == gokuPlayer) {
			return playerOneImage;
		}
		return playerTwoImage;
	}

	private Player getIdlePlayer() {
		if (currentPlayer == gokuPlayer) {
			return vegetaPlayer;
		}
		return gokuPlayer;
	}

	private void setImage(int imgId, ImageView imageView) {
		Glide.with(this).load(getResources().getDrawable(imgId, requireContext().getTheme())).into(imageView);
	}

	private void setCurrentPlayer(Player player) {
		currentPlayer = player;
	}

	private void onGameOver() {
		resetPlayerImage();
		setScore(10);
		setLocation();
		updateDataBase();
		showGameResult();
	}

	private void setLocation() {
		if (location == null) {
			return;
		}
		currentPlayer.setLocation(location.getLatitude(), location.getLongitude());
	}

	private void setScore(int score) {
		currentPlayer.setScore(score);
	}

	private void showGameResult() {
		GameResultFragment gameResultFragment = new GameResultFragment(currentPlayer);
		FragmentTransaction ft = getParentFragmentManager().beginTransaction();
		ft.replace(R.id.result_container, gameResultFragment, gameResultFragment.getClass().getSimpleName());
		ft.commit();
	}

	private void updateDataBase() {
		DataBase.getInstance().insertOrUpdatePlayer(currentPlayer);
	}

	private void resetPlayerImage() {
		setImage(gokuPlayer.getPlayerImageId(), playerOneImage);
		setImage(vegetaPlayer.getPlayerImageId(), playerTwoImage);
	}

	private void updateHpBar(Player player) {
		getPlayerHpBar(player).setProgress(player.getHp());
	}

	private ProgressBar getPlayerHpBar(Player player) {
		if (player == gokuPlayer) {
			return playerOnePb;
		}
		return playerTwoPb;
	}

	private void onAttackEnded() {
		if (isGameOver()) {
			onGameOver();
			return;
		}

		setCurrentPlayer(getIdlePlayer());
		performAttack();
	}

	private boolean isGameOver() {
		return getIdlePlayer().getHp() <= 0;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.start_btn) {
			initGame();
		}
	}

	private void initGame() {
		String nameOne = playerOneName.getText().toString();
		String nameTwo = playerTwoName.getText().toString();
		if (nameOne.isEmpty() || nameTwo.isEmpty()) {
			showError("Missing player name");
			return;
		}

		if (nameOne.equals(nameTwo)) {
			showError("Players have the same name");
			return;
		}
		startGame(nameOne, nameTwo);
	}

	private void startGame(String nameOne, String nameTwo) {
		buildPlayers(nameOne, nameTwo);
		setCurrentPlayer(getRandomPlayer());
		menuContainer.setVisibility(View.GONE);
		performAttack();
	}

	private void showError(String error) {
		Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
	}

	private Player getRandomPlayer() {
		int num = new Random().nextInt(2);
		if (num == 1) {
			return gokuPlayer;
		}
		return vegetaPlayer;
	}

	@Override
	public void onLocationReceived(Location location) {
		this.location = location;
	}
}
