package com.example.translatorapp.ui.elements.MainActivityFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.translatorapp.R;
import com.example.translatorapp.databinding.FragmentTextBinding;
import com.example.translatorapp.ui.elements.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentTextBinding binding;

    public TextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TextFragment newInstance(String param1, String param2) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTextBinding.inflate(inflater,container,false);
        binding.fromLang.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(),v);

            popupMenu.getMenu().add("English");
            popupMenu.getMenu().add("Punjabi");
            popupMenu.getMenu().add("Odia");
            popupMenu.getMenu().add("Hindi");
            popupMenu.getMenu().add("Bangla");
            Log.d("ONClick","Should happen");
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                binding.fromLang.setText(menuItem.getTitle());
                return true;
            });
            popupMenu.show();
        });

        binding.toLang.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(),v);

            popupMenu.getMenu().add("English");
            popupMenu.getMenu().add("Punjabi");
            popupMenu.getMenu().add("Odia");
            popupMenu.getMenu().add("Hindi");
            popupMenu.getMenu().add("Bangla");

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                binding.toLang.setText(menuItem.getTitle());
                return true;
            });
            popupMenu.show();
        });
        return (View)binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void switchTextBox(View view){

    }
}