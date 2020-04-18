package gr.stratego.patrastournament.me.Fragments;


import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public abstract class BaseStrategoFragment extends Fragment {


    public BaseStrategoFragment() {
        // Required empty public constructor
    }


    public abstract void updateData(ArrayList<? extends Object> list);

    public abstract void updateUI();

    public void closeKeyboard() {
        if (getActivity() != null) {
            if (getActivity().getWindow() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

}
