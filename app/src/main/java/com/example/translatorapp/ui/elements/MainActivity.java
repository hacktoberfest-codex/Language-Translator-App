package com.example.translatorapp.ui.elements;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.translatorapp.R;
import com.example.translatorapp.databinding.ActivityMainBinding;
import com.example.translatorapp.ui.elements.MainActivityFragments.ChatBotFragment;
import com.example.translatorapp.ui.elements.MainActivityFragments.ImgToTxtFragment;
import com.example.translatorapp.ui.elements.MainActivityFragments.TextFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private TextFragment textFragment;
    private ImgToTxtFragment imgToTxtFragment;
    private ChatBotFragment chatBotFragment;
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new TextFragment());

        binding.bottomNavMainAct.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.text_menuitem){
                replaceFragment(new TextFragment());
            }
            else if(item.getItemId()==R.id.imgtotxt_menuitem){
                replaceFragment(new ImgToTxtFragment());
            }
            else{
                replaceFragment(new ChatBotFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_main_act,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
             finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}