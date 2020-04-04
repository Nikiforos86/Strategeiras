package gr.stratego.patrastournament.me.Fragments;


import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public abstract class BaseStrategoFragment extends Fragment {


    public BaseStrategoFragment() {
        // Required empty public constructor
    }


    public abstract void updateData(ArrayList<? extends Object> list);

    public abstract void updateUI();

}
