//즐겨찾기 화면
package com.example.parking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class FragStar extends Fragment {
   public FragStar(){

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

       return inflater.inflate(R.layout.frag_star, container, false);
   }

}
