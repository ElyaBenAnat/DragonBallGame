package com.dragonball.demo.ui.score;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragonball.demo.R;
import com.dragonball.demo.models.players.PlayerModel;
import com.dragonball.demo.ui.score.ScoreAdapter.ScoreViewHolder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class ScoreAdapter
		extends RecyclerView.Adapter<ScoreViewHolder> {

	private ArrayList<PlayerModel> data;

	public ScoreAdapter(ArrayList<PlayerModel> data) {
		this.data = data;
	}

	@NonNull
	@Override
	public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ScoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_score, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
		holder.bind(data.get(position));
		holder.initializeMapView();
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	static class ScoreViewHolder
			extends ViewHolder
			implements OnMapReadyCallback {

		GoogleMap map;
		MapView mapView;
		double lat;
		double log;

		public ScoreViewHolder(@NonNull View itemView) {
			super(itemView);
			mapView = itemView.findViewById(R.id.map);
		}

		public void initializeMapView() {
			if (mapView != null) {
				mapView.onCreate(null);
				mapView.onResume();
				mapView.getMapAsync(this);
			}
		}

		public void bind(final PlayerModel playerModel) {
			TextView nameTv = itemView.findViewById(R.id.name_tv);
			TextView scoreTv = itemView.findViewById(R.id.score_tv);
			nameTv.setText(playerModel.name);
			scoreTv.setText(String.valueOf(playerModel.score));
			lat = playerModel.lat;
			log = playerModel.log;
		}

		@Override
		public void onMapReady(GoogleMap googleMap) {
			MapsInitializer.initialize(itemView.getContext());
			map = googleMap;

			LatLng location = new LatLng(lat, log);
			final CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(13).build();

			map.addMarker(new MarkerOptions().position(location).title("test"));
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}
}
