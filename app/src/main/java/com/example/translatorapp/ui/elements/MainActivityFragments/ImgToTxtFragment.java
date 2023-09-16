package com.example.translatorapp.ui.elements.MainActivityFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.translatorapp.R;
import com.example.translatorapp.databinding.FragmentImgToTxtBinding;
import com.example.translatorapp.databinding.FragmentTextBinding;

public class ImgToTxtFragment extends Fragment {


   FragmentImgToTxtBinding binding;
    public ImgToTxtFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = FragmentImgToTxtBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}