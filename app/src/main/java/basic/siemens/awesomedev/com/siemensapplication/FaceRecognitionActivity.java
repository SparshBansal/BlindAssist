package basic.siemens.awesomedev.com.siemensapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaceRecognitionActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lnrImages;
    private Button btnAddPhots;
    private Button btnSaveImages;
    private ArrayList<String> imagesPathList;
    private Bitmap yourbitmap;
    private Bitmap resized;
    private EditText et_name_view = null;
    private final int PICK_IMAGE_MULTIPLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);
        lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
        btnAddPhots = (Button) findViewById(R.id.btnAddPhots);
        btnSaveImages = (Button) findViewById(R.id.btnSaveImages);
        et_name_view = (EditText) findViewById(R.id.et_name_view);
        btnAddPhots.setOnClickListener(this);
        btnSaveImages.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddPhots:
                Intent intent = new Intent(FaceRecognitionActivity.this, CustomPhotoGalleryActivity.class);
                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
                break;
            case R.id.btnSaveImages:
                if (et_name_view.getText().toString().equals("") || et_name_view.getText() == null) {
                    Toast.makeText(this, "Empty Edit Text", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imagesPathList != null) {
                    if (imagesPathList.size() >= 1) {

                        for (int i = 0; i < imagesPathList.size(); i++) {

                            File file = new File(imagesPathList.get(i));
                            RequestBody requestFile =
                                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part body =
                                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                            RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), et_name_view.getText().toString());
                            Call<Ping> call = RetrofitHelper.getInstance().uploadImage(body, name);
                            call.enqueue(new Callback<Ping>() {
                                @Override
                                public void onResponse(Call<Ping> call, Response<Ping> response) {
                                    Toast.makeText(FaceRecognitionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Ping> call, Throwable t) {
                                    Toast.makeText(FaceRecognitionActivity.this, "exception occurred", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else {
                        Toast.makeText(this, "Select at least one ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FaceRecognitionActivity.this, " no images are selected", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_MULTIPLE) {
                imagesPathList = new ArrayList<String>();
                String[] imagesPath = data.getStringExtra("data").split("\\|");
                try {
                    lnrImages.removeAllViews();
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < imagesPath.length; i++) {
                    imagesPathList.add(imagesPath[i]);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    yourbitmap = BitmapFactory.decodeFile(imagesPath[i],options );
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(yourbitmap);
                    imageView.setAdjustViewBounds(true);
                    lnrImages.addView(imageView);

                }


            }
        }

    }

}
