package fr.louisbompart.blereferencer.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Chip> chips;

    private Uri fileUri;
    File mediaFile;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                    decodeQrCode();

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerview.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        chips = new ArrayList<Chip>();
        adapter = new ChipAdapter(chips);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addBLE(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.e("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void decodeQrCode() {
        Bitmap myBitmap = null;
        try {
            myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        if(!detector.isOperational())
        {
            Toast.makeText(MainActivity.this, "Uhoh we've a detector issue dude.", Toast.LENGTH_SHORT).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        Barcode thisCode = barcodes.valueAt(0);
        TextView txtView = (TextView) findViewById(R.id.txtContent);
        txtView.setText(thisCode.rawValue);
        String string = thisCode.rawValue;
        String[] array = string.split("/");
        chips.add(new Chip(array[0],array[1],array[2]));
        myBitmap = null;
        mediaFile.delete();
    }
}
