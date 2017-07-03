package com.ewintory.udacity.popularmovies.ui.fragment;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewintory.udacity.popularmovies.R;
import com.ewintory.udacity.popularmovies.data.model.Genre;
import com.ewintory.udacity.popularmovies.ui.fragment.LeftMenuFragment.OnListFragmentInteractionListener;
import com.ewintory.udacity.popularmovies.ui.fragment.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyleftMenuRecyclerViewAdapter extends RecyclerView.Adapter<MyleftMenuRecyclerViewAdapter.ViewHolder> {

    private final Map<Integer, Genre> mValues;
    private final OnListFragmentInteractionListener mListener;
    private  int selectedPosition=-1;
    public MyleftMenuRecyclerViewAdapter(Map<Integer, Genre> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_leftmenu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        List keys = new ArrayList(mValues.keySet());

        holder.mItem = mValues.get(keys.get(position));

        holder.mIdView.setText("");// + holder.mItem.getId());
        holder.mContentView.setText(holder.mItem.getName());

        if(selectedPosition==position)
            holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), R.color.material_teal300));
        else
            holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), R.color.material_teal600));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    selectedPosition=position;
                    notifyDataSetChanged();
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Genre mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
