package basic.siemens.awesomedev.com.siemensapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FaceRecognitionActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = FaceRecognitionActivity.class.getSimpleName();
    EditText etName = null;
    Button btSelectImages = null;
    public int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        etName = (EditText) findViewById(R.id.et_name);
        btSelectImages = (Button) findViewById(R.id.bt_select_image);

        btSelectImages.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.bt_select_image){

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
            Intent chooser = Intent.createChooser(intent,"Select at max 6 images..");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(chooser,PICK_IMAGE);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && requestCode == RESULT_OK && data != null){
            Uri selectedImages = data.getData();
            String imagePath[] = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImages,imagePath,null,null,null);
            if(cursor.getCount() > 6 ){
                Log.d(TAG,"Too many images selected");
                Toast.makeText(getApplicationContext(),"Too many images selected",Toast.LENGTH_SHORT).show();
            }

            else if(cursor.getCount() < 1){
                Log.d(TAG,"Select at least a single image");
                Toast.makeText(getApplicationContext(),"Select at least a single image",Toast.LENGTH_SHORT).show();
            }
            else {
                int index = 0;
                while(cursor.moveToNext()){
                    int columnIndex = cursor.getColumnIndex(imagePath[index++]);
                    String picturePath = cursor.getString(columnIndex);
                    Log.d(TAG,picturePath);
                    Toast.makeText(getApplicationContext(),picturePath,Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();
        }
    }
}
