package com.example.translatorapp.ui.elements.MainActivityFragments;

import static android.app.Activity.RESULT_OK;

import static androidx.core.content.ContextCompat.getDrawable;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.translatorapp.ui.elements.CropperActivity;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.Objects;

public class ImgToTxtFragment extends Fragment {


    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private InputImage image;
    private static final int IMAGE_REQUEST_CODE = 1001;
    FragmentImgToTxtBinding binding;
    private String[] storagePermission;
    private static Uri GalData;

ActivityResultLauncher<String> mGetContent;
    public ImgToTxtFragment() {
        // Required empty public constructor
    }

    public static ImgToTxtFragment getInstance(Uri uri)
    {
        ImgToTxtFragment imgToTxtFragment = new ImgToTxtFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("URI",uri);

        imgToTxtFragment.setArguments(bundle);
        return imgToTxtFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        // Handle the result here
                        Intent intent = new Intent(getContext(), CropperActivity.class);
                        intent.putExtra("DATA", result);
                        startActivityForResult(intent, IMAGE_REQUEST_CODE);
                    }
                }
        );
//        if(getArguments()!=null)
//        {
//            GalData = getArguments().getParcelable("URI");
//            Toast.makeText(getContext(), GalData.toString(), Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImgToTxtBinding.inflate(getLayoutInflater());

        binding.getImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckPermission())
                {
                    GetImage();
                }
                else{
                    RequestPermission();
                }

            }
        });
        binding.translateImgTextBtn.setOnClickListener(view -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            if(image!=null) {
                transaction.replace(R.id.fragment_container_main_act, TextFragment.getInstance(GalData));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return binding.getRoot();
    }

    private void RequestPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);
    }

    private boolean CheckPermission() {

        String gallery_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int perm = ContextCompat.checkSelfPermission(getContext(),gallery_permission);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private void GetImage() {
        mGetContent.launch("image/*");
 }


    // checking storage permissions


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int i: grantResults)
        {
            if(i==PackageManager.PERMISSION_GRANTED)
            {
                GetImage();
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Toast.makeText(getContext(), "No issue", Toast.LENGTH_SHORT).show();
//             Handle the result here
            try {
                image = InputImage.fromFilePath(requireContext(), data.getParcelableExtra("CROPPED_IMAGE_URI"));
                this.GalData = data.getParcelableExtra("CROPPED_IMAGE_URI");

//                Toast.makeText(getContext(), ""+data.getParcelableExtra("CROPPED_IMAGE_URI"), Toast.LENGTH_SHORT).show();
                Glide.with(getContext()).load(data.getParcelableExtra("CROPPED_IMAGE_URI").toString()).placeholder(R.drawable.baseline_add_a_photo_24)
                        .into(binding.getImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            Toast.makeText(getContext(), "Hey", Toast.LENGTH_SHORT).show();
        }
    }

}