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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
             dispatchTakePictureIntent();
            // Try to ping the server
            /*Call<Ping> call = RetrofitHelper.getInstance().ping("Hello");
            call.enqueue(new Callback<Ping>() {
                @Override
                public void onResponse(Call<Ping> call, Response<Ping> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Toast.makeText(MainActivity.this, response.body().getStatusCode() , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Ping> call, Throwable t) {
                    Log.d(TAG, "onFailure:  Some fucking error happened Shit!!!");
                    Log.d(TAG, t.getMessage());
                }
            });*/
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CAPTURE_IMAGE:
                Toast.makeText(this, "Captured the image", Toast.LENGTH_SHORT).show();
                File image = new File(mCurrentPhotoPath);

                if (image.exists()){
                    Toast.makeText(this, "File toh exist karti hai", Toast.LENGTH_SHORT).show();
                }

                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), image);

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", image.getName(), requestFile);

                Call<List<Caption>> call = RetrofitHelper.getInstance().getCaptions(body);
                call.enqueue(new Callback<List<Caption>>() {
                    @Override
                    public void onResponse(Call<List<Caption>> call, Response<List<Caption>> response) {
                        Toast.makeText(MainActivity.this, "Response Received", Toast.LENGTH_SHORT).show();
                        for (Caption caption : response.body()){
                            Log.d(TAG, "onResponse: " + caption.getCaption() + " Confidence Score : " + caption.getConfidenceScore());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Caption>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Some fucking error occurred", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
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
