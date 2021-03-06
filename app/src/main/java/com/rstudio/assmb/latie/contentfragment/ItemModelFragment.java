package com.rstudio.assmb.latie.contentfragment;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rstudio.assmb.latie.DownloadHTML;
import com.rstudio.assmb.latie.R;
import com.rstudio.assmb.latie.contentfragment.dummy.AllItemContent;
import com.rstudio.assmb.latie.contentfragment.dummy.DatabaseHandler;
import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent;
import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent.DummyItem;

import java.util.Calendar;
import java.util.Date;
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

    FloatingActionButton addButton;

    View feedMe;

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
            mAdapter = new ItemModelRecyclerViewAdapter(this, mContent.ITEMS, mListener, archivingMode);
            recyclerView.setAdapter(mAdapter);
        }

        addButton = ((FloatingActionButton) view.findViewById(R.id.plusButton));

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

        if (mContent != null) {
            if (mContent instanceof AllItemContent) {
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                        if (dy > 0)
                            addButton.hide();
                        else if (dy < 0)
                            addButton.show();
                    }
                });

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickAddButton();
                    }
                });
            } else {
                addButton.setVisibility(View.GONE);
            }
        }

        feedMe = view.findViewById(R.id.feed_please);

        if (mContent.ITEMS.size() > 0) {
            feedMe.setVisibility(View.GONE);
        }

        return view;
    }

    public void refreshFeed() {
        if (mContent.ITEMS.size() > 0) {
            feedMe.setVisibility(View.GONE);
        } else {
            feedMe.setVisibility(View.VISIBLE);
        }
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

    public void onClickAddButton() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the
        // dialog layout
        builder.setTitle("Add new article");
        builder.setCancelable(true);

        final View v = inflater.inflate(R.layout.add_new_layout, null);

        builder.setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText title = ((EditText) v.findViewById(R.id.title_add));
                            EditText content = ((EditText) v.findViewById(R.id.content_add));
                            final TextView waring = ((TextView) v.findViewById(R.id.error_plus));

                            String contentText = content.getText().toString();

                            if (contentText.length() > 0) {

                                DatabaseHandler dbHandler = new DatabaseHandler(getContext());

                                if (Patterns.WEB_URL.matcher(contentText).matches()) {

                                    DownloadHTML handle = new DownloadHTML(getContext(), contentText, dbHandler);
                                    handle.execute(contentText);

                                    reloadData();

                                } else {

                                    Date date = new Date();
                                    long dateTime = Long.valueOf(date.getTime());
                                    DummyItem newItem = new DummyItem("1", title.getText().toString(), contentText, "", false, dateTime);
                                    dbHandler.addDummyItem(newItem);

                                    reloadData();
                                }

                            } else {
                                waring.setVisibility(View.VISIBLE);

                                new AlertDialog.Builder(getContext())
                                        .setTitle("Alert")
                                        .setMessage("Please fill in content")
                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .show();

                            }

                            content.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    waring.setVisibility(View.GONE);
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                        }
                    })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
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
                refreshFeed();
            }
        }
    }
}
