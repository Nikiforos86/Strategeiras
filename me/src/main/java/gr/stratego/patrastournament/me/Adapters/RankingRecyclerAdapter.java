package gr.stratego.patrastournament.me.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gr.stratego.patrastournament.me.Models.BattleResultModel;
import gr.stratego.patrastournament.me.Models.RankingModel;
import gr.stratego.patrastournament.me.R;
import gr.stratego.patrastournament.me.StrategoApplication;
import gr.stratego.patrastournament.me.Utils.GeneralUtils;
import gr.stratego.patrastournament.me.Utils.StringUtils;

public class RankingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int RAMKING = 0;
    private final static int RESULTS = 1;

    private ArrayList<Object> mDataList;
    private Context mContext;
    private GradientDrawable drawable;
    private int textColor;

    public RankingRecyclerAdapter(ArrayList<Object> mDataList, Context context) {
        this.mDataList = mDataList;
        this.mContext = context;
        this.textColor = Color.BLACK;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case RAMKING:
                return new RankingViewHolder(LayoutInflater.from(mContext).inflate(R.layout.ranking_list_item, parent, false));
            case RESULTS:
                return new ResultsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.results_list_item, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (this.drawable == null && StrategoApplication.getAppSettings() != null) {
            this.drawable = new GradientDrawable();
            this.drawable.setColor(Color.parseColor(StrategoApplication.getAppSettings().getAccentColor()));
            this.drawable.setShape(GradientDrawable.OVAL);
        }

        if (mDataList.get(position) instanceof RankingModel && holder instanceof RankingViewHolder) {
            RankingModel rankingModel = (RankingModel) mDataList.get(position);
            RankingViewHolder vh = (RankingViewHolder) holder;
            vh.index.setText(String.valueOf(position + 1));
            vh.name.setText(rankingModel.getName());
            vh.score.setText(rankingModel.getScore());

            vh.name.setTextColor(textColor);
            vh.score.setTextColor(textColor);

            if (this.drawable != null) {
                vh.index.setBackground(drawable);
            }
        } else if (mDataList.get(position) instanceof BattleResultModel && holder instanceof ResultsViewHolder) {
            BattleResultModel resultsModel = (BattleResultModel) mDataList.get(position);
            ResultsViewHolder vh = (ResultsViewHolder) holder;
            vh.index.setText(String.valueOf(position + 1));
            if (this.drawable != null) {
                vh.index.setBackground(drawable);
            }
            vh.leftName.setText(resultsModel.getLeftName());
            vh.rightName.setText(resultsModel.getRightName());
            vh.score.setText(resultsModel.getScore());
            vh.leftName.setTextColor(textColor);
            vh.score.setTextColor(textColor);
            vh.rightName.setTextColor(textColor);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position) instanceof RankingModel) {
            return RAMKING;
        } else {
            return RESULTS;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void updateUI() {
        this.drawable = null;

        if (StrategoApplication.getAppSettings() != null
                && StringUtils.isNotNullOrEmpty(StrategoApplication.getAppSettings().getDarkTextColor())) {
            textColor = Color.parseColor(StrategoApplication.getAppSettings().getDarkTextColor());
        }

        notifyDataSetChanged();
    }

    private class RankingViewHolder extends RecyclerView.ViewHolder {

        private TextView index, name, score;

        public RankingViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.nameTextView);
            this.score = itemView.findViewById(R.id.scoreTextView);
            this.index = itemView.findViewById(R.id.indexTextView);
        }
    }

    private class ResultsViewHolder extends RecyclerView.ViewHolder {

        private TextView index, leftName, rightName, score;

        public ResultsViewHolder(View itemView) {
            super(itemView);
            this.leftName = itemView.findViewById(R.id.leftTextView);
            this.rightName = itemView.findViewById(R.id.rightTextView);
            this.score = itemView.findViewById(R.id.scoreTextView);
            this.index = itemView.findViewById(R.id.indexTextView);
        }
    }
}
