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
public class Tutor_third extends Fragment {

    private double s;

    public Tutor_third(double s){
        this.s  = s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor__third, container, false);
        UserLocalStore userLocalStore = new UserLocalStore(this.getActivity());
        if(userLocalStore.getLanguage(this.getActivity()).equals("ru")){
            if(s > 9){
                TextView textView = view.findViewById(R.id.lightScreenTutorTextView);
                textView.setText("Свет");
                textView = view.findViewById(R.id.descriptionThirdScreenTutorTextView);
                textView.setText("Вы можете управлять режимами света, выключать и включать свет, а также задавать цвет свечения источника света.");
            } else{
                TextView textView = view.findViewById(R.id.lightScreenTutorTextView);
                textView.setText("Свет");
                textView.setTextSize(25);
                textView = view.findViewById(R.id.descriptionThirdScreenTutorTextView);
                textView.setText("Вы можете управлять режимами света, выключать и включать свет, а также задавать цвет свечения источника света.");
                textView.setTextSize(18);
            }
        }
        // Inflate the layout for this fragment
        return view;
    }

}
