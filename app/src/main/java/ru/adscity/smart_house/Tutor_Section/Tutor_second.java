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
public class Tutor_second extends Fragment {

    private double s;

    public Tutor_second(double s){
        this.s = s;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_second, container, false);
        UserLocalStore userLocalStore = new UserLocalStore(this.getActivity());
        if(userLocalStore.getLanguage(this.getActivity()).equals("ru")){
            if(s > 9){
                TextView textView = view.findViewById(R.id.SensorsScreenTutorTextView);
                textView.setText("Датчики");
                textView = view.findViewById(R.id.descriptionSecondScreenTutorTextView);
                textView.setText("Вы сможете увидеть значения температуры и влажности, " +
                        "взятые с датчика в вашем умном доме.");
            } else{
                TextView textView = view.findViewById(R.id.SensorsScreenTutorTextView);
                textView.setText("Датчики");
                textView.setTextSize(25);
                textView = view.findViewById(R.id.descriptionSecondScreenTutorTextView);
                textView.setText("Вы сможете увидеть значения температуры и влажности, " +
                        "взятые с датчика в вашем умном доме.");
                textView.setTextSize(18);
            }

        }
        return view;
    }

}
