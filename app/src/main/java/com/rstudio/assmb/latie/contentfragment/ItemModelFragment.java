package com.rstudio.assmb.latie.contentfragment;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rstudio.assmb.latie.R;
import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent;
import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemModelFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_ARCHIVE = "archiving";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private boolean archivingMode;

    RecyclerView recyclerView;

    DummyContent mContent;

    ItemModelRecyclerViewAdapter mAdapter;

    SearchView searchView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemModelFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemModelFragment newInstance(int columnCount, boolean archiving) {
        ItemModelFragment fragment = new ItemModelFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putBoolean(ARG_ARCHIVE, archiving);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            archivingMode = getArguments().getBoolean(ARG_ARCHIVE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itemmodel_list, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(mColumnCount, StaggeredGridLayoutManager.VERTICAL));
        }

        if (mContent != null) {
            mContent.initData(getContext());
            mAdapter = new ItemModelRecyclerViewAdapter(getContext(), mContent.ITEMS, mListener, archivingMode);
            recyclerView.setAdapter(mAdapter);
        }

        searchView = ((SearchView) view.findViewById(R.id.search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.i("ABC", "onQueryTextSubmit: " + query);

                mAdapter.filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.i("III", "onQueryTextSubmit: " + newText);

                mAdapter.filter(newText);

                return true;
            }
        });


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DummyItem item);
    }

    public void setContent(DummyContent content) {
        mContent = content;

//        if (mContent != null) {
//            recyclerView.setAdapter(new ItemModelRecyclerViewAdapter(mContent.ITEMS, mListener));
//        }
    }

    public void reloadData() {

        if (recyclerView != null) {

            if (mContent != null) {
                mContent.initData(getContext());
            }

            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
