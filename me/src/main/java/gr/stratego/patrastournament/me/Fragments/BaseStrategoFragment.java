package gr.stratego.patrastournament.me.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gr.stratego.patrastournament.me.R;

public abstract class BaseStrategoFragment extends Fragment {


    public BaseStrategoFragment() {
        // Required empty public constructor
    }


    public abstract void updateData(ArrayList<? extends Object> list);

    public abstract void updateUI();

}
