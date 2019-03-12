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
public class Tutor_seventh extends Fragment {


    private double s;

    public Tutor_seventh(double s) {

        this.s = s;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_seventh, container, false);
        UserLocalStore userLocalStore = new UserLocalStore(this.getActivity());
        if(userLocalStore.getLanguage(this.getActivity()).equals("ru")){
            if(s > 9){
                TextView textView = view.findViewById(R.id.voiceScreenTutorTextView);
                textView.setText("Голосовое озвучивание");
                textView = view.findViewById(R.id.descriptionSeventhScreenTutorTextView);
                textView.setText("Система голосового озвучивания будет автоматически информировать Вас о состоянии систем в доме.");
            } else{
                TextView textView = view.findViewById(R.id.voiceScreenTutorTextView);
                textView.setText("Голосовое озвучивание");
                textView.setTextSize(25);
                textView = view.findViewById(R.id.descriptionSeventhScreenTutorTextView);
                textView.setText("Система голосового озвучивания будет автоматически информировать Вас о состоянии систем в доме.");
                textView.setTextSize(19);
            }
        }        return view;
    }

}
