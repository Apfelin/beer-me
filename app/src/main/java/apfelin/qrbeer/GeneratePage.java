package apfelin.qrbeer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GeneratePage extends Activity {

    private EditText lastNameEdit, firstNameEdit, ageEdit;
    private Button chooseBtn, backBtn, generateBtn, saveBtn, takeBtn;
    private ImageView userImage;
    private Uri takenPhoto;
    private ArrayList<String> usersQR;
    private ArrayList<Integer> ages;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_page);

        lastNameEdit = (EditText) findViewById(R.id.lastname_edit);
        firstNameEdit = (EditText) findViewById(R.id.firstname_edit);
        ageEdit = (EditText) findViewById(R.id.age_edit);
        userImage = (ImageView) findViewById(R.id.photo);

        usersQR = new ArrayList<>();
        ages = new ArrayList<>();

        takeBtn = (Button) findViewById(R.id.take_btn);
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        ex.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, 200);
                    }
                }
            }
        });

        chooseBtn = (Button) findViewById(R.id.choose_btn);
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 300);
            }
        });

        backBtn = (Button) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(0);

                finish();
            }
        });

        generateBtn = (Button) findViewById(R.id.generate_btn);
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String QRCodeGen = firstNameEdit.getText().toString() + " " +
                        lastNameEdit.getText().toString() + " " + takenPhoto.toString();

                addQRAges(QRCodeGen, Integer.parseInt(ageEdit.getText().toString()));

                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(QRCodeGen, BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    ((ImageView) findViewById(R.id.qrcode)).setImageBitmap(bmp);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        saveBtn = (Button) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usersQR != null) {

                    Intent usersReturnIntent = new Intent();

                    usersReturnIntent.putStringArrayListExtra("usersQR", usersQR);
                    usersReturnIntent.putIntegerArrayListExtra("age", ages);

                    setResult(1, usersReturnIntent);

                    finish();
                }
                else {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Nu exista nimic in ArrayList", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == 200 && resultCode == RESULT_OK) {

            takenPhoto = imageReturnedIntent.getData();
            userImage.setImageURI(takenPhoto);
        }
        if (requestCode == 300 && resultCode == RESULT_OK) {

            takenPhoto = imageReturnedIntent.getData();
            userImage.setImageURI(takenPhoto);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName);

        return image;
    }

    public void addQRAges(String _QRCode, int _age) {

        usersQR.add(_QRCode);
        ages.add(_age);
    }

    @Override
    public void onStart ()
    {
        super.onStart();
    }

    @Override
    public void onRestart ()
    {
        super.onRestart();
    }

    @Override
    public void onResume ()
    {
        super.onResume();
    }

    @Override
    public void onPause ()
    {
        super.onPause();
    }

    @Override
    public void onStop ()
    {
        super.onStop();
    }

    @Override
    public void onDestroy ()
    {
        super.onDestroy();
    }

    // functii folosite pentru salvarea si restaurarea starii

    @Override
    public void onSaveInstanceState (Bundle outState)
    {
        // apelarea functiei din activitatea parinte este recomandata, dar nu obligatorie
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState (Bundle inState)
    {
        // apelarea functiei din activitatea parinte este recomandata, dar nu obligatorie
        super.onRestoreInstanceState(inState);
    }
}
