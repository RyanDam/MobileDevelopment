package com.rstudio.assmb.latie.contentfragment;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rstudio.assmb.latie.R;
import com.rstudio.assmb.latie.contentfragment.ItemModelFragment.OnListFragmentInteractionListener;
import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ItemModelRecyclerViewAdapter extends RecyclerView.Adapter<ItemModelRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ItemModelRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_itemmodel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);

        holder.mDateView.setText(holder.mItem.getDateTime() + " " + holder.mItem.originLink);

        if (position % 4 == 0 || position == 0) {
            holder.mThumbView.setVisibility(View.GONE);
        } else {
            holder.mThumbView.setVisibility(View.VISIBLE);
        }
        holder.mContentView.setText(holder.mItem.getSummary());
        holder.mIdView.setText(holder.mItem.title);




        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
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
        public final TextView mDateView;
        public final View mThumbView;
        public final AppCompatImageView mThumbImageView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mDateView = ((TextView) view.findViewById(R.id.date));
            mThumbImageView = ((AppCompatImageView) view.findViewById(R.id.thumbnail));
            mThumbView = view.findViewById(R.id.thumbnail_holder);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
