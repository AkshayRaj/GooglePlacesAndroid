package com.ark.googleplaces.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ark.googleplaces.view.MainActivity;
import com.ark.googleplaces.R;
import com.ark.googleplaces.model.Place;
import com.squareup.picasso.Picasso;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
	private Activity mActvity;
	private Context mContext;
	private ArrayList<Place> mPlaces;
	private MainActivity.OnItemClickListener mClickListener;


	public PlaceAdapter(Activity activity, ArrayList<Place> places, MainActivity.OnItemClickListener onItemClickListener) {
		mActvity = activity;
		mContext = mActvity.getApplicationContext();
		mPlaces = places;
		mClickListener = onItemClickListener;
	}

	public void setDataset(ArrayList<Place> placeList){
		mPlaces.clear();
		mPlaces.addAll(placeList);
		notifyDataSetChanged();
	}

	// Create new views (invoked by the layout manager)
	@Override
	public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.placeadapter_item, parent, false);
		// set the view's size, margins, paddings and layout parameters
		PlaceViewHolder viewHolder = new PlaceViewHolder(view);
		return viewHolder;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(PlaceViewHolder holder, final int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element
		holder.bind(mPlaces.get(position), mClickListener);
		Picasso.with(mContext).load(mPlaces.get(position).getIcon()).into(holder.icon);
		final String name = mPlaces.get(position).getName();
		final String vicinity = mPlaces.get(position).getVicinity();
		holder.name.setText(name);
		holder.vicinity.setText(vicinity);
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return mPlaces.size();
	}

	public void add(int position, Place item) {
		mPlaces.add(position, item);
		notifyItemInserted(position);
	}

	public void remove(int position) {
		mPlaces.remove(position);
		notifyItemRemoved(position);
	}

	//ViewHolder for items in mPlaces
	static class PlaceViewHolder extends RecyclerView.ViewHolder {
		ImageView icon;
		TextView name;
		TextView vicinity;//address
		View itemView;

		public PlaceViewHolder(View view) {
			super(view);
			itemView = view;
			icon = (ImageView) view.findViewById(R.id.place_icon);
			name = (TextView) view.findViewById(R.id.firstLine);
			vicinity = (TextView) view.findViewById(R.id.secondLine);
		}

		//Hack to implement clickListener in RecyclerView
		public void bind(final Place place, final MainActivity.OnItemClickListener listener){
			itemView.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					listener.onItemClick(place);
				}
			});
		}
	}
}