package gr.stratego.patrastournament.me.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import gr.stratego.patrastournament.me.Fragments.LiveRankingFragment;
import gr.stratego.patrastournament.me.Fragments.LiveResultsFragment;
import gr.stratego.patrastournament.me.Fragments.MapFragment;
import gr.stratego.patrastournament.me.Fragments.TournamentInfoFragment;
import gr.stratego.patrastournament.me.Fragments.UserProfileFragment;

public class HomeFragmentAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private LiveRankingFragment mLiveRankingFragment;
    private LiveResultsFragment mLiveResultsFragment;
    private UserProfileFragment mUserFragment;
    private TournamentInfoFragment mTournamentFragment;
    private MapFragment mMapFragment;

    public HomeFragmentAdapter(FragmentManager fragmentManager, LiveRankingFragment liveRankingFragment, LiveResultsFragment liveResultsFragment, UserProfileFragment userFragment, TournamentInfoFragment tournamentFragment, MapFragment mapFragment) {
        super(fragmentManager);
        this.mLiveRankingFragment = liveRankingFragment;
        this.mLiveResultsFragment = liveResultsFragment;
        this.mUserFragment = userFragment;
        this.mTournamentFragment = tournamentFragment;
        this.mMapFragment = mapFragment;
        this.fragments.add(mLiveRankingFragment);
        this.fragments.add(mLiveResultsFragment);
        this.fragments.add(mUserFragment);
        this.fragments.add(mTournamentFragment);
        this.fragments.add(mMapFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
