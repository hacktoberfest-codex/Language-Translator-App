package com.example.translatorapp.ui.elements;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.translatorapp.R;
import com.example.translatorapp.ui.elements.MainActivityFragments.ImgToTxtFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CropperActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CROP = 123;
    private Uri selectedImageUri;
    private Uri croppedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);

        // Retrieve the image URI passed from your previous activity
        Intent intent = getIntent();
        if (intent != null) {
            selectedImageUri = intent.getParcelableExtra("DATA");
            startCropActivity(selectedImageUri);
        }
    }

    private void startCropActivity(Uri sourceUri) {
        // Create a destination URI for the cropped image
        String destUri = UUID.randomUUID().toString() + ".jpg";
        croppedImageUri = Uri.fromFile(new File(getCacheDir(), destUri));

        // Configure UCrop options
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(80); // Adjust compression quality as needed

        // Start the UCrop activity
        UCrop.of(sourceUri, croppedImageUri)
                .withOptions(options)
                .start(this, REQUEST_IMAGE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CROP) {
            if (resultCode == RESULT_OK) {
                // The image was cropped successfully, and the result is in the 'croppedImageUri'
                Intent resultIntent = new Intent();
                resultIntent.putExtra("CROPPED_IMAGE_URI", croppedImageUri);
                setResult(RESULT_OK, resultIntent);

            } else if (resultCode == UCrop.RESULT_ERROR) {
                // There was an error during cropping, handle it here
                final Throwable cropError = UCrop.getError(data);
                // Log the error or display a message to the user
            }
            finish();

            // Finish the CropperActivity

        }
    }
}