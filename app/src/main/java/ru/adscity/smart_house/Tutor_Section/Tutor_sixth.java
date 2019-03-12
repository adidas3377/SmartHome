package ru.adscity.smart_house.Tutor_Section;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.adscity.smart_house.R;
import ru.adscity.smart_house.User_Section.UserLocalStore;


@SuppressLint("ValidFragment")
public class Tutor_sixth extends Fragment {


    private double s;
    public Tutor_sixth(double s) {
        this.s = s;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_sixth, container, false);
        UserLocalStore userLocalStore = new UserLocalStore(this.getActivity());
        if(userLocalStore.getLanguage(this.getActivity()).equals("ru")){
            if(s > 9){
                TextView textView = view.findViewById(R.id.fireSystemScreenTutorTextView);
                textView.setText("Пожарная система");
                textView = view.findViewById(R.id.descriptionSixthScreenTutorTextView);
                textView.setText("Система постоянно опрашивает датчик углекислого газа, при срабатывании системы Вы сразу получите уведомление на почту или от бота.");

            } else{
                TextView textView = view.findViewById(R.id.fireSystemScreenTutorTextView);
                textView.setText("Пожарная система");
                textView.setTextSize(25);
                textView = view.findViewById(R.id.descriptionSixthScreenTutorTextView);
                textView.setText("Система постоянно опрашивает датчик углекислого газа, при срабатывании системы Вы сразу получите уведомление на почту или от бота.");
                textView.setTextSize(18);
            }
        }        return view;
    }

}
