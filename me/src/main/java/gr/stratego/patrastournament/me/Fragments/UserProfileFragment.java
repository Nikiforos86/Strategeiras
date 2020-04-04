package gr.stratego.patrastournament.me.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import gr.stratego.patrastournament.me.Adapters.RankingRecyclerAdapter;
import gr.stratego.patrastournament.me.Models.BattleResultModel;
import gr.stratego.patrastournament.me.Models.PastBattle;
import gr.stratego.patrastournament.me.Models.RankingModel;
import gr.stratego.patrastournament.me.Models.User;
import gr.stratego.patrastournament.me.R;
import gr.stratego.patrastournament.me.StrategoApplication;
import gr.stratego.patrastournament.me.Utils.CollectionUtils;
import gr.stratego.patrastournament.me.Utils.GeneralUtils;
import gr.stratego.patrastournament.me.Utils.SharedPreferencesUtil;
import gr.stratego.patrastournament.me.Utils.StringUtils;


public class UserProfileFragment extends BaseStrategoFragment {

    private OnUserProfileListener mListener;

    private View mLoggedLayout;
    private View mNotLoggedLayout;

    private TextView mWelcomeTextView;
    private TextView mPositionTextView;

    private View mNextRoundView;
    private TextView mNextRoundTextView;
    private TextView mNextRountTableTextView, mLeftName, mRightName, mScore;

    private TextView mTitleTextView;
    private TextInputEditText mMailInput;
    private TextInputEditText mPinInput;

    private Button mLogin;
    private Button mLogout;

    private String mTournamentTitle;

    private RecyclerView mRecyclerView;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
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
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);
    }

    private void initUI(View view) {
        if (view == null) {
            return;
        }

        mLoggedLayout = view.findViewById(R.id.logged_layout);
        mNotLoggedLayout = view.findViewById(R.id.not_logged_layout);

        mWelcomeTextView = view.findViewById(R.id.welcome_text_view);
        mPositionTextView = view.findViewById(R.id.position_text_view);
        mNextRoundView = view.findViewById(R.id.next_round_layout);
        mNextRoundTextView = view.findViewById(R.id.next_round_text_view);

        mLeftName = view.findViewById(R.id.leftTextView);
        mRightName = view.findViewById(R.id.rightTextView);
        mScore = view.findViewById(R.id.scoreTextView);
        mNextRountTableTextView = view.findViewById(R.id.indexTextView);

        mTitleTextView = view.findViewById(R.id.title_text_view);
        mMailInput = view.findViewById(R.id.email_input);
        mPinInput = view.findViewById(R.id.pin_input);

        mLogin = view.findViewById(R.id.login_button);
        mLogout = view.findViewById(R.id.logout_button);

        mRecyclerView = view.findViewById(R.id.pastBattles);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMailInput.getText() == null || mPinInput.getText() == null) {
                    return;
                }
                if (StringUtils.isNotNullOrEmpty(mMailInput.getText().toString()) && StringUtils.isNotNullOrEmpty(mPinInput.getText().toString())) {
                    mListener.onLogin(mMailInput.getText().toString(), mPinInput.getText().toString());
                }
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.saveSharedPreference(false, SharedPreferencesUtil.UserLogged, SharedPreferencesUtil.UserLogged);
                SharedPreferencesUtil.saveSharedPreference(null, SharedPreferencesUtil.UserId, SharedPreferencesUtil.UserId);
                updateUI();
            }
        });

        updateUI();
    }

    @Override
    public void updateData(ArrayList<?> list) {
        User currentUser = StrategoApplication.getCurrentUser();

        if (CollectionUtils.isNotEmpty(list) && currentUser != null) {
            Object obj = list.get(0);
            if (obj instanceof BattleResultModel) {
                for (int cnt = 0; cnt < list.size(); cnt++) {
                    BattleResultModel battleResult = (BattleResultModel) list.get(cnt);
                    if (StringUtils.areEqual(battleResult.getLeftName(), currentUser.getDisplayName()) || StringUtils.areEqual(battleResult.getRightName(), currentUser.getDisplayName())) {
                        mNextRountTableTextView.setText("Table "+String.valueOf(cnt + 1));
                        mLeftName.setText(battleResult.getLeftName());
                        mRightName.setText(battleResult.getRightName());
                        mScore.setText(battleResult.getScore());
                        setupPastBattles(battleResult);
                        break;
                    }
                }
            } else if (obj instanceof RankingModel) {
                for (int cnt = 0; cnt < list.size(); cnt++) {
                    RankingModel rankingModel = (RankingModel) list.get(cnt);
                    if (StringUtils.areEqual(rankingModel.getName(), currentUser.getFullName())) {
                        String points = "";
                        if (StringUtils.isNotNullOrEmpty(rankingModel.getScore())) {
                            points = "\nPoints: " + rankingModel.getScore();
                        }
                        mPositionTextView.setText("Place: " + (cnt + 1) + points);
                        break;
                    }

                }
            }
        }
        updateUI();
    }

    private void setupPastBattles(final BattleResultModel battleResult) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<Object> playersPastBattles = mListener.findPastBattles(battleResult);
                if(CollectionUtils.isNotEmpty(playersPastBattles)) {
					
					StrategoApplication.getCurrentUser().getFullName();
                    if(StrategoApplication.getCurrentUser() != null){
                       String currentUser = StrategoApplication.getCurrentUser().getFullName();
                       int victoryCounter = 0;
                       int defeatCounter = 0;
                       int drawCounter = 0;
                       String result = "";
                       for (Object pastBattleObj : playersPastBattles) {
                           PastBattle pastBattle = (PastBattle) pastBattleObj;
                           if (pastBattle.getPlayer1().equals(currentUser)) {
                               result = pastBattle.getResultPlayer1();
                           }
                           else if (pastBattle.getPlayer2().equals(currentUser)){
                               result = pastBattle.getResultPlayer2();
                           }

                           switch(result) {
                               case "VICTORY":
                                   victoryCounter++;
                                   break;
                               case "DEFEAT":
                                   defeatCounter++;
                                   break;
                               case "DRAW":
                                   drawCounter++;
                                   break;
                           }
                       }

                    }
					
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    mRecyclerView.setAdapter(new RankingRecyclerAdapter(playersPastBattles, getActivity()));
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        }, 5000);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void updateUI() {
        if (getView() == null) {
            return;
        }
        if (SharedPreferencesUtil.loadSharedPreference(SharedPreferencesUtil.UserLogged, SharedPreferencesUtil.UserLogged, false)) {
            mLoggedLayout.setVisibility(View.VISIBLE);
            mNotLoggedLayout.setVisibility(View.GONE);
        } else {
            mLoggedLayout.setVisibility(View.GONE);
            mNotLoggedLayout.setVisibility(View.VISIBLE);
        }

        int textColor = Color.BLACK;
        int secondaryAccentColor = GeneralUtils.getProperColor(R.color.secondaryColorAccent);

        GradientDrawable bg = new GradientDrawable();
        bg.setStroke(GeneralUtils.convertDptoPx(2f), secondaryAccentColor);
        bg.setCornerRadius(GeneralUtils.convertDptoPx(8f));

        if (StrategoApplication.getAppSettings() != null
                && StringUtils.isNotNullOrEmpty(StrategoApplication.getAppSettings().getDarkTextColor())
                && StringUtils.isNotNullOrEmpty(StrategoApplication.getAppSettings().getPrimaryColor())) {
            textColor = Color.parseColor(StrategoApplication.getAppSettings().getDarkTextColor());
            secondaryAccentColor = Color.parseColor(StrategoApplication.getAppSettings().getSecondaryAccentColor());
            bg.setStroke(GeneralUtils.convertDptoPx(2f), secondaryAccentColor);

        }

        mWelcomeTextView.setTextColor(textColor);
        mWelcomeTextView.setBackground(bg);

        mPositionTextView.setTextColor(textColor);
        mPositionTextView.setBackground(bg);

        mNextRoundView.setBackground(bg);
        mNextRoundTextView.setTextColor(textColor);

        mNextRountTableTextView.setTextColor(textColor);
        mTitleTextView.setTextColor(textColor);

        mLeftName.setTextColor(textColor);
        mRightName.setTextColor(textColor);
        mScore.setTextColor(textColor);

        User currentUser = StrategoApplication.getCurrentUser();
        if (currentUser != null && StringUtils.isNotNullOrEmpty(currentUser.getDisplayName())) {
            if (StringUtils.isNotNullOrEmpty(mTournamentTitle)) {
                mWelcomeTextView.setText(currentUser.getDisplayName() + "\nwelcome to\n" + mTournamentTitle);
            } else {
                mWelcomeTextView.setText(currentUser.getDisplayName() + "\nwelcome!");
            }
        }

        if(mPositionTextView.getText() != null && StringUtils.isNotNullOrEmpty(mPositionTextView.getText().toString())){
            mPositionTextView.setVisibility(View.VISIBLE);
        } else {
            mPositionTextView.setVisibility(View.GONE);
        }

        if(mScore.getText() != null && StringUtils.isNotNullOrEmpty(mScore.getText().toString())){
            mNextRoundView.setVisibility(View.VISIBLE);
        } else {
            mNextRoundView.setVisibility(View.GONE);
        }
    }

    public void updateTournamentTitle(String tournamentTitle) {
        mTournamentTitle = tournamentTitle;
        updateUI();
    }

    public void updateRound(String round) {
        if(getView() != null && mNextRoundTextView != null) {
            mNextRoundTextView.setText("Round "+round);
            updateUI();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserProfileListener) {
            mListener = (OnUserProfileListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserProfileListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUserProfileListener {
        void onLogin(String mail, String pin);
        ArrayList<Object> findPastBattles(BattleResultModel battleResult);
    }
}
