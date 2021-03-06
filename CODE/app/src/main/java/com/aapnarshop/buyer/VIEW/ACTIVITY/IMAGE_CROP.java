package com.aapnarshop.buyer.VIEW.ACTIVITY;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class IMAGE_CROP extends AppCompatActivity {

    Uri pictureUri;
    int width = 10;
    int height = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        if (getIntent().hasExtra("width")) {
            width = getIntent().getExtras().getInt("width");
        }
        if (getIntent().hasExtra("height")) {
            height = getIntent().getExtras().getInt("height");
        }
        pictureUri = Uri.parse(getIntent().getExtras().getString("image"));
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        UCrop.of(pictureUri, destination).withAspectRatio(width, height).start(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Intent i = new Intent();
            i.putExtra("data", resultUri.toString());
            setResult(RESULT_OK, i);
            finish();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Intent i = new Intent();
            i.putExtra("data", cropError.toString());
            setResult(RESULT_CANCELED, i);
            finish();
        } else if (requestCode == 69) {
            Intent i = new Intent();
            i.putExtra("data", "error");
            setResult(RESULT_CANCELED, i);
            finish();
        }
    }
}

