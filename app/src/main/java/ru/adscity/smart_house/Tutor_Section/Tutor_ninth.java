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
public class Tutor_ninth extends Fragment {


    private double s;
    public Tutor_ninth(double s) {

        this.s = s;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_ninth, container, false);
        UserLocalStore userLocalStore = new UserLocalStore(this.getActivity());
        if (userLocalStore.getLanguage(this.getActivity()).equals("ru")) {
            if(s > 9){
                TextView textView = view.findViewById(R.id.SensorsScreenTutorTextView);
                textView.setText("Голосовые команды:");
                textView = view.findViewById(R.id.textView2);
                textView.setText("1) Включить/Выключить охрану \n 2) Включить/Выключить пожарную систему \n 3) Включить/Выключить голосовое оповещение \n 4) Включить/Выключить вентилятор");
            } else{
                TextView textView = view.findViewById(R.id.SensorsScreenTutorTextView);
                textView.setText("Голосовые команды:");
                textView.setTextSize(25);
                textView = view.findViewById(R.id.textView2);
                textView.setText("1) Включить/Выключить охрану \n 2) Включить/Выключить пожарную систему \n 3) Включить/Выключить голосовое оповещение \n 4) Включить/Выключить вентилятор");
                textView.setTextSize(16);
            }
        }
        return view;
    }

}
