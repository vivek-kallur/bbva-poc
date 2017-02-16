package com.bbvacompass;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbvacompass.model.Place;
import com.bbvacompass.model.TextSearchModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListItemClickListener}
 * interface.
 */
public class PlaceFragment extends Fragment {

    private static final String ARG_RESPONSE = "RESPONSE";
    private TextSearchModel mTextSearchModel;
    private OnListItemClickListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlaceFragment() {
    }

    public static PlaceFragment newInstance(TextSearchModel textSearchModel) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RESPONSE, textSearchModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTextSearchModel = getArguments().getParcelable(ARG_RESPONSE);
        } else if (savedInstanceState != null) {
            mTextSearchModel = savedInstanceState.getParcelable("MODEL");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new PlacesRecyclerViewAdapter(mTextSearchModel.getPlaceList(), mListItemClickListener));
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("MODEL", mTextSearchModel);
        super.onSaveInstanceState(outState);
    }

    private OnListItemClickListener mListItemClickListener = new OnListItemClickListener() {
        @Override
        public void onListFragmentInteraction(Place place) {

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnListItemClickListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Place place);
    }
}
