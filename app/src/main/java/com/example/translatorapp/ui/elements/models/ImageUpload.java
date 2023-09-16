package com.example.translatorapp.ui.elements.models;

import android.net.Uri;

public class ImageUpload
{
    private static ImageUpload instance;
    private Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
    public static ImageUpload getInstance()
    {
        if(instance==null)
        {
            instance = new ImageUpload();
        }
        return instance;
    }
}
