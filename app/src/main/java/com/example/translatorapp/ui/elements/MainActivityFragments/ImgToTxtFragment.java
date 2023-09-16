package com.example.translatorapp.ui.elements.MainActivityFragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.translatorapp.R;
import com.example.translatorapp.databinding.FragmentImgToTxtBinding;
import com.example.translatorapp.databinding.FragmentTextBinding;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;

public class ImgToTxtFragment extends Fragment {


    private static final int REQUEST_EXTERNAL_STORAGE_COD = 101;
    private static final int IMAGE_REQUEST_CODE = 1001;
    FragmentImgToTxtBinding binding;
    private TextRecognizer textRecognizer;
    public ImgToTxtFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = FragmentImgToTxtBinding.inflate(getLayoutInflater());

      binding.getImg.setOnClickListener(view -> {
          if(checkPermisstion())
          {
           GetImage();
          }
          else{
              AskPermission();
          }
      });
        return binding.getRoot();
    }

    private void GetImage() {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        Intent externalStorage = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        externalStorage.setType("image/*");
        Intent intent = Intent.createChooser(gallery,"Choose To Pick Image");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS,externalStorage);
        startActivityForResult(intent,IMAGE_REQUEST_CODE);
    }

    private void AskPermission() {
        if(!checkPermisstion() && Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE_COD);
        }
    }

    private boolean checkPermisstion() {

        String storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        int res = getContext().checkSelfPermission(storagePermission);
        return res == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                if(requestCode==REQUEST_EXTERNAL_STORAGE_COD)
                {
                   for(int perm : grantResults)
                   {
                       if(perm == PackageManager.PERMISSION_GRANTED)
                       {
                           GetImage();
                           Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                           break;
                       }
                       else{
                           Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        break;
                       }
                   }
                }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK)
        {
            try {
                InputImage image = InputImage.fromFilePath(getContext(),data.getData());
                Glide.with(getContext()).load(image).placeholder(getResources().getDrawable(R.drawable.baseline_add_a_photo_24))
                        .into(binding.getImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}