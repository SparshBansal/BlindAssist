package basic.siemens.awesomedev.com.siemensapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PREF_ACCOUNT_NAME = "accountName";

    private Button bPhotoButton = null;
    private TextView tvResultView = null;


    // Request Code for Camera Intent
    private static final int REQUEST_CAPTURE_IMAGE = 1000;
    private static final int REQUEST_RESOLVE_CONNECTION = 1005;

    // Path for the current photo
    private String mCurrentPhotoPath = null;
    private Uri mCurrentPhotoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the views
        bPhotoButton = (Button) findViewById(R.id.b_take_photo);
        tvResultView = (TextView) findViewById(R.id.tv_result_caption);

        bPhotoButton.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.b_take_photo) {
            // Launch camera intent
            // dispatchTakePictureIntent();
            // Try to ping the server
            Call<String> call = RetrofitHelper.getInstance().ping("Hello");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Toast.makeText(MainActivity.this, response.body() , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "onFailure:  Some fuckin error happened Shit!!!");
                }
            });

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CAPTURE_IMAGE:
                Toast.makeText(this, "Captured the image", Toast.LENGTH_SHORT).show();

                break;
        }
    }


    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivityInfo(getPackageManager(), PackageManager.GET_ACTIVITIES) != null) {
            // Create a file for the full sized photo
            File mPhoto = null;

            try {
                mPhoto = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mPhoto != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", mPhoto);
                this.mCurrentPhotoUri = photoUri;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                // Dispatch the camera intent
                startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private File createImageFile() throws IOException {

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFilename = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFilename, ".jpg", storageDir);

        // Get the path
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "createImageFile: " + mCurrentPhotoPath);
        return image;

    }

}
