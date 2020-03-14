package gr.stratego.patrastournament.me.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gr.stratego.patrastournament.me.Adapters.RankingRecyclerAdapter;
import gr.stratego.patrastournament.me.Models.BattleResultModel;
import gr.stratego.patrastournament.me.R;
import gr.stratego.patrastournament.me.StrategoApplication;
import gr.stratego.patrastournament.me.Utils.CollectionUtils;
import gr.stratego.patrastournament.me.Utils.StringUtils;

public class LiveResultsFragment extends BaseStrategoFragment {

    private RankingRecyclerAdapter mAdapter;
    private View mEmptyState;
    private RecyclerView mRecyclerView;
    private ArrayList<Object> mData;

    public LiveResultsFragment() {
        // Required empty public constructor
    }


    public static LiveResultsFragment newInstance() {
        LiveResultsFragment fragment = new LiveResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_with_recycler, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mRecyclerView = view.findViewById(R.id.rv_main);
        mEmptyState = view.findViewById(R.id.noResultsLayout);
        mData = new ArrayList<>();
        mAdapter = new RankingRecyclerAdapter(mData, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        setEmptyState();
    }

    @Override
    public void updateData(ArrayList<?> list) {
        mData.clear();
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
        setEmptyState();
    }

    @Override
    public void updateUI(){
        if(getView() == null){
            return;
        }

        if(getView().findViewById(R.id.top_line) != null) {
            getView().findViewById(R.id.top_line).setBackgroundColor(Color.parseColor(StrategoApplication.getAppSettings().getAccentColor()));
        }

        mAdapter.updateUI();
        setEmptyState();
    }

    public void updateRound(String title) {
        if(getView() == null){
            return;
        }

        TextView titleView = getView().findViewById(R.id.title);
        if(titleView != null && StringUtils.isNotNullOrEmpty(title)){
            titleView.setText(title);
            if(StrategoApplication.getAppSettings() != null
                    && StringUtils.isNotNullOrEmpty(StrategoApplication.getAppSettings().getLightTextColor())){
                titleView.setTextColor(Color.parseColor(StrategoApplication.getAppSettings().getLightTextColor()));
            }
        }
    }

    private void setEmptyState(){
        if(mEmptyState == null || mRecyclerView == null){
            return;
        }
        if(CollectionUtils.isEmpty(mData)){
            mEmptyState.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyState.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
