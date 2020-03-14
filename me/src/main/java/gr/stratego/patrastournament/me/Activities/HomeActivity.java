package gr.stratego.patrastournament.me.Activities;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.ArrayList;

import gr.stratego.patrastournament.me.Adapters.HomeFragmentAdapter;
import gr.stratego.patrastournament.me.Fragments.LiveRankingFragment;
import gr.stratego.patrastournament.me.Fragments.LiveResultsFragment;
import gr.stratego.patrastournament.me.Fragments.MapFragment;
import gr.stratego.patrastournament.me.Fragments.TournamentInfoFragment;
import gr.stratego.patrastournament.me.Fragments.UserProfileFragment;
import gr.stratego.patrastournament.me.Models.AppSettings;
import gr.stratego.patrastournament.me.Models.BattleResultModel;
import gr.stratego.patrastournament.me.Models.RankingModel;
import gr.stratego.patrastournament.me.Models.User;
import gr.stratego.patrastournament.me.R;
import gr.stratego.patrastournament.me.Services.ApiResponses.ApiTournamentProgressResponse;
import gr.stratego.patrastournament.me.Services.DefaultCallback;
import gr.stratego.patrastournament.me.Services.WebServices;
import gr.stratego.patrastournament.me.StrategoApplication;
import gr.stratego.patrastournament.me.Utils.CollectionUtils;
import gr.stratego.patrastournament.me.Utils.GeneralUtils;
import gr.stratego.patrastournament.me.Utils.SharedPreferencesUtil;
import gr.stratego.patrastournament.me.Utils.StringUtils;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity implements UserProfileFragment.OnUserProfileListener {

    private LiveRankingFragment mLiveRankingFragment;
    private LiveResultsFragment mLiveResultsFragment;
    private UserProfileFragment mUserProfileFragment;

    private BottomNavigationView navigationView;
    private HomeFragmentAdapter mAdapter;
    private ViewPager mViewPager;
    private SwipeRefreshLayout mRefreshLayout;
    private String mResultsUri;
    private ArrayList<User> userList;
    private DatabaseReference mDatabase;
    private String mdlMail;
    private String mdlPin;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.navigation_live_results) {
                mViewPager.setCurrentItem(0);
                mRefreshLayout.setEnabled(true);
                return true;
            } else if (id == R.id.navigation_live_round) {
                mViewPager.setCurrentItem(1);
                mRefreshLayout.setEnabled(true);
                return true;
            } else if (id == R.id.navigation_home) {
                mViewPager.setCurrentItem(2);
                mRefreshLayout.setEnabled(true);
                if (mUserProfileFragment != null) {
                    mUserProfileFragment.updateUI();
                }
                return true;
            } else if (id == R.id.navigation_dashboard) {
                mViewPager.setCurrentItem(3);
                mRefreshLayout.setEnabled(false);
                return true;
            } else if (id == R.id.navigation_map) {
                mViewPager.setCurrentItem(4);
                mRefreshLayout.setEnabled(true);
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            if (deepLink != null && deepLink.toString().contains("mail")) {
                                mdlMail = deepLink.getQueryParameter("mail");
                                mdlPin = deepLink.getQueryParameter("pin");
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e("getDynamicLink:onFailure" + e.getMessage());
                    }
                });

        userList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initActivity();
    }

    private void initActivity() {
        DatabaseReference resultsUrlReference = mDatabase.child("appSettings");
        DatabaseReference usersReference = mDatabase.child("Users");

        resultsUrlReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                AppSettings settings = dataSnapshot.getValue(AppSettings.class);
                StrategoApplication.setAppSettings(settings);
                if (StringUtils.isNotNullOrEmpty(settings.getResultsUrl())) {
                    mResultsUri = settings.getResultsUrl();
                    getResultsData(mResultsUri);
                }

                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Timber.e("Failed to read value." + error.toException());
            }
        });

        usersReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                userList.add(user);
                if (SharedPreferencesUtil.loadSharedPreference(SharedPreferencesUtil.UserLogged, SharedPreferencesUtil.UserLogged, false)
                        && StringUtils.areEqual(SharedPreferencesUtil.loadSharedPreference(SharedPreferencesUtil.UserId, SharedPreferencesUtil.UserId), user.getMail())) {
                    StrategoApplication.setCurrentUser(user);
                } else {
                    if(StringUtils.isNotNullOrEmpty(mdlMail) && StringUtils.isNotNullOrEmpty(mdlPin)
                    && StringUtils.isNotNullOrEmpty(user.getMail()) && mdlMail.equalsIgnoreCase(user.getMail())){
                        onLogin(mdlMail, mdlPin);
                    }
                }
                Timber.d(user.getDisplayName());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        initUI();
    }

    private void initUI() {

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mRefreshLayout = findViewById(R.id.refresh);

        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeColors(GeneralUtils.getProperColor(R.color.colorPrimary),
                    GeneralUtils.getProperColor(R.color.colorAccent),
                    GeneralUtils.getProperColor(R.color.colorSecondary));
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (StringUtils.isNotNullOrEmpty(mResultsUri)) {
                        getResultsData(mResultsUri);
                    }
                }
            });

        }
        mViewPager = findViewById(R.id.viewPager);
        initMainAdapter();
    }

    private void updateUI() {
        if (mLiveRankingFragment != null && mLiveResultsFragment != null) {
            mLiveRankingFragment.updateUI();
            mLiveResultsFragment.updateUI();
            mUserProfileFragment.updateUI();
        }

        if (StrategoApplication.getAppSettings() != null) {
            GradientDrawable drawable = new GradientDrawable();
            int colors[] = new int[2];
            colors[0] = Color.parseColor(StrategoApplication.getAppSettings().getPrimaryColor());
            colors[1] = Color.parseColor(StrategoApplication.getAppSettings().getSecondaryColor());

            drawable.setColors(colors);
            mViewPager.setBackground(drawable);
        }
    }

    private void initMainAdapter() {
        mLiveRankingFragment = LiveRankingFragment.newInstance();
        mLiveResultsFragment = LiveResultsFragment.newInstance();
        mUserProfileFragment = UserProfileFragment.newInstance();

        mAdapter = new HomeFragmentAdapter(getSupportFragmentManager(), mLiveRankingFragment, mLiveResultsFragment, mUserProfileFragment, TournamentInfoFragment.newInstance(), MapFragment.newInstance());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        navigationView.setSelectedItemId(R.id.navigation_live_results);
                        break;
                    case 1:
                        navigationView.setSelectedItemId(R.id.navigation_live_round);
                        break;
                    case 2:
                        navigationView.setSelectedItemId(R.id.navigation_home);
                        break;
                    case 3:
                        navigationView.setSelectedItemId(R.id.navigation_dashboard);
                        break;
                    case 4:
                        navigationView.setSelectedItemId(R.id.navigation_map);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setSelectedItemId(R.id.navigation_home);

    }

    private void getResultsData(String url) {
        WebServices.getTournamentProgress(url, new DefaultCallback<ApiTournamentProgressResponse>() {
            @Override
            public void onResponse(ApiTournamentProgressResponse response) {
                super.onResponse(response);
                transformData(response.getTournamentProgressHtml());
            }

            @Override
            public void onFailure(String str) {
                if (mRefreshLayout != null) {
                    mRefreshLayout.setRefreshing(false);
                }

            }
        });
    }

    private void transformData(String tournamentProgressHtml) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }

        String[] htmlLines = tournamentProgressHtml.split("\n");
        boolean readResults = false, readStandings = false;

        ArrayList<BattleResultModel> battleList = new ArrayList<>();
        ArrayList<RankingModel> rankingList = new ArrayList<>();

        try {

            int resultsIndex = 0;
            for (String sCurrentLine : htmlLines) {

                if (sCurrentLine.contains("<title>") && sCurrentLine.contains("</title>")) {
                    String tournamentTitle = sCurrentLine.substring(sCurrentLine.indexOf("<title>") + 7, sCurrentLine.indexOf("</title>"));
                    Timber.d(">|" + tournamentTitle + "|<");
                    if (mUserProfileFragment != null && StringUtils.isNotNullOrEmpty(tournamentTitle)) {
                        mUserProfileFragment.updateTournamentTitle(tournamentTitle);
                    }
                }

                if (sCurrentLine.contains("</pre>")) {
                    readResults = false;
                    readStandings = false;
                }

                if (readResults
                        && !(sCurrentLine.contains("No")
                        && sCurrentLine.contains("Name") && sCurrentLine
                        .contains("Result"))) {
                    String[] splitResult = sCurrentLine.trim().split("\\s+");

                    if (splitResult.length > 1) {
                        battleList.add(getBattleResult(splitResult));
                    }
                }
                if (readStandings
                        && !(sCurrentLine.contains("Place")
                        && sCurrentLine.contains("Name") && sCurrentLine
                        .contains("Feder"))) {

                    String[] splitStanding = sCurrentLine.trim().split("\\s+");

                    if (splitStanding.length > 1) {
                        rankingList.add(setRankingLine(splitStanding));
                    }
                }
                if (sCurrentLine.contains("<pre>")
                        && sCurrentLine.contains("Results")) {
                    readResults = true;
                }
                if (sCurrentLine.contains("<pre>")
                        && sCurrentLine.contains("Standings")) {
                    readStandings = true;
                }
                if (sCurrentLine.contains("<h1>")
                        && sCurrentLine.contains("Round")) {
                    mLiveResultsFragment.updateRound(getRound(sCurrentLine));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        mLiveRankingFragment.updateData(rankingList);
        mLiveResultsFragment.updateData(battleList);
        mUserProfileFragment.updateData(rankingList);
        mUserProfileFragment.updateData(battleList);

    }

    private BattleResultModel getBattleResult(String[] splitResult) {

        String name1 = splitResult[1] + " " + splitResult[2];
        String score = splitResult[4];
        String name2 = "";
        if (splitResult.length > 6) {
            name2 = splitResult[5] + " " + splitResult[6];
        }
        return new BattleResultModel(name1, name2, score);
    }

    private RankingModel setRankingLine(String[] splitRanking) {
        String name;
        String score;
        if (Character.isDigit(splitRanking[0].charAt(0))) {
            name = splitRanking[1].replaceFirst(".$", "") + " " + splitRanking[2];
            score = splitRanking[4] + " (" + splitRanking[5] + ")";
        } else {
            name = splitRanking[0].replaceFirst(".$", "") + " " + splitRanking[1];
            score = splitRanking[3] + " (" + splitRanking[4] + ")";
        }
        return new RankingModel(name, score);
    }

    private String getRound(String sCurrentLine) {
        try {
            String[] splitString = sCurrentLine.trim().split("\\s+");
            String part;
            for (int i = 0; i < splitString.length; i++) {
                part = splitString[i];
                if (part.equalsIgnoreCase("round")) {
                    int round = Integer.parseInt("" + splitString[i + 1].charAt(0));
                    if (mUserProfileFragment != null) {
                        mUserProfileFragment.updateRound("" + splitString[i + 1].charAt(0));
                    }
                    if (round == 1) {
                        return "Results - 1st round";
                    } else if (round == 2) {
                        return "Results - 2nd round";
                    } else if (round == 3) {
                        return "Results - 3rd round";
                    } else {
                        return "Results - " + round + "th round";
                    }
                }
            }
        } catch (Exception e) {

        }
        return "";
    }

    @Override
    public void onLogin(String mail, String pin) {
        if (CollectionUtils.isEmpty(userList)) {
            Toast.makeText(this, "There are no users in the tournament", Toast.LENGTH_SHORT).show();
            return;
        }
        for (User user : userList) {
            if (StringUtils.isNotNullOrEmpty(user.getMail()) && StringUtils.isNotNullOrEmpty(mail)
                    && user.getMail().equalsIgnoreCase(mail) && StringUtils.areEqual(user.getPin(), pin)) {
                SharedPreferencesUtil.saveSharedPreference(user.getMail(), SharedPreferencesUtil.UserId, SharedPreferencesUtil.UserId);
                SharedPreferencesUtil.saveSharedPreference(true, SharedPreferencesUtil.UserLogged, SharedPreferencesUtil.UserLogged);
                StrategoApplication.setCurrentUser(user);
                mUserProfileFragment.updateUI();
                return;
            }
        }
        Toast.makeText(this, "There is no user with those data", Toast.LENGTH_SHORT).show();


    }
}
