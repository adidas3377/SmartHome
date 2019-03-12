package ru.adscity.smart_house.Tutor_Section;


import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.adscity.smart_house.R;
import ru.adscity.smart_house.User_Section.UserLocalStore;


@SuppressLint("ValidFragment")
public class Tutor_first extends Fragment {


    private double s;
    public Tutor_first(double s){
        this.s = s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_first, container, false);
        UserLocalStore userLocalStore = new UserLocalStore(this.getActivity());
        if(userLocalStore.getLanguage(this.getActivity()).equals("ru")){
            if(s > 9){
                TextView textView = view.findViewById(R.id.descriptionFirstScreenTutorTextView);
                textView.setText("Это приложение поможет Вам управлять вашим домом.");
                textView = view.findViewById(R.id.WelcomeFirstScreenTutortextView);
                textView.setText("Добро пожаловать!");
            } else{
                TextView textView = view.findViewById(R.id.descriptionFirstScreenTutorTextView);
                textView.setText("Это приложение поможет Вам управлять вашим домом.");
                textView.setTextSize(25);
                textView = view.findViewById(R.id.WelcomeFirstScreenTutortextView);
                textView.setText("Добро пожаловать!");
                textView.setTextSize(35);
            }

        }
        // Inflate the layout for this fragment
        return view;
    }

}
