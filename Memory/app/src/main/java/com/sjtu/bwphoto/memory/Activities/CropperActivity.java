package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.Util.CropImageView;
import com.sjtu.bwphoto.memory.R;

import org.springframework.core.io.FileSystemResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.Locale;

public class CropperActivity extends Activity {
    private String fileName;
    private Uri imageUri;
    private String croppedName;
    private String userName;
    private int res_id;
    private final static ServerUrl url = new ServerUrl();

    // Private Constants ///////////////////////////////////////////////////////////////////////////

    private static final int GUIDELINES_ON_TOUCH = 1;

    // Activity Methods ////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cropper);

        // Initialize Views.
        final ToggleButton fixedAspectRatioToggleButton = (ToggleButton) findViewById(R.id.fixedAspectRatioToggle);
        final TextView aspectRatioXTextView = (TextView) findViewById(R.id.aspectRatioX);
        final SeekBar aspectRatioXSeekBar = (SeekBar) findViewById(R.id.aspectRatioXSeek);
        final TextView aspectRatioYTextView = (TextView) findViewById(R.id.aspectRatioY);
        final SeekBar aspectRatioYSeekBar = (SeekBar) findViewById(R.id.aspectRatioYSeek);
        final Spinner guidelinesSpinner = (Spinner) findViewById(R.id.showGuidelinesSpin);
        final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        final ImageView croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
        final Button cropButton = (Button) findViewById(R.id.Button_crop);

        //modify image source
        Bundle bundle = this.getIntent().getExtras();
        fileName = bundle.getString("fileName");
        userName = bundle.getString("userName");
        res_id = bundle.getInt("res_id");

        //20160722try=======
        imageUri = this.getIntent().getData();
        System.out.println("CropperA : imageuri :"+imageUri);
        Bitmap bitmap2 = null;
        try {
            InputStream in = getContentResolver().openInputStream(imageUri);
            bitmap2 = BitmapFactory.decodeStream(in);
            in.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        if (bitmap2 == null) System.out.println("bitmap2 null !!!!!!!!!!!!!");
        //========================

        // origin
//        BitmapRegionDecoder bitmapRegionDecoder = null;
//        try {
//            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(fileName, false);
//            if (bitmapRegionDecoder == null) System.out.println("bitmapRegionDecoder null !!!!!");
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        options.inSampleSize = 2;
//        int width = bitmapRegionDecoder.getWidth();  // 图片长宽
//        int height = bitmapRegionDecoder.getHeight();
//        Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect(0,0,width,height),options);

        // 传送bitmap
        cropImageView.setImageBitmap(bitmap2);

        // Initializes fixedAspectRatio toggle button.
        fixedAspectRatioToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cropImageView.setFixedAspectRatio(isChecked);
                cropImageView.setAspectRatio(aspectRatioXSeekBar.getProgress(), aspectRatioYSeekBar.getProgress());
                aspectRatioXSeekBar.setEnabled(isChecked);
                aspectRatioYSeekBar.setEnabled(isChecked);
            }
        });
        // Set seek bars to be disabled until toggle button is checked.
        aspectRatioXSeekBar.setEnabled(false);
        aspectRatioYSeekBar.setEnabled(false);

        aspectRatioXTextView.setText(String.valueOf(aspectRatioXSeekBar.getProgress()));
        aspectRatioYTextView.setText(String.valueOf(aspectRatioXSeekBar.getProgress()));

        // Initialize aspect ratio X SeekBar.
        aspectRatioXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioXSeekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    aspectRatioXSeekBar.setProgress(1);
                }
                cropImageView.setAspectRatio(aspectRatioXSeekBar.getProgress(), aspectRatioYSeekBar.getProgress());
                aspectRatioXTextView.setText(String.valueOf(aspectRatioXSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }
        });

        // Initialize aspect ratio Y SeekBar.
        aspectRatioYSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioYSeekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    aspectRatioYSeekBar.setProgress(1);
                }
                cropImageView.setAspectRatio(aspectRatioXSeekBar.getProgress(), aspectRatioYSeekBar.getProgress());
                aspectRatioYTextView.setText(String.valueOf(aspectRatioYSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }
        });

        // Set up the Guidelines Spinner.
        guidelinesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cropImageView.setGuidelines(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing.
            }
        });
        guidelinesSpinner.setSelection(GUIDELINES_ON_TOUCH);

        // Initialize the Crop button.
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap croppedImage = cropImageView.getCroppedImage();
                croppedImageView.setImageBitmap(croppedImage);
                //生成图片名
                new DateFormat();
                croppedName = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + "_cropped.jpg";
                System.out.println(croppedName);

                //--------tongyong-----------
                File output = new File(Environment.getExternalStorageDirectory(),croppedName);  //获取sd卡根目录
                try {
                    if (output.exists()){
                        output.delete();
                    }
                    output.createNewFile();
                } catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    FileOutputStream out = new FileOutputStream(output);  //存储裁剪图
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    System.out.println("cropped image saved !!!!!");
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //--------------------------

//                File file = new File("/sdcard/DCIM/Camera/", croppedName);
//                croppedName = "/sdcard/DCIM/Camera/"+croppedName;
//                try {
//                    FileOutputStream out = new FileOutputStream(file);
//                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                    System.out.println("cropped image saved !!!!!");
//                    out.flush();
//                    out.close();
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }

                //modified
                //String result = RestUtil.uploadFile(url.url+"/resources/"+res_id+"/image", new FileSystemResource(file), fileName,String.class);
                String result = RestUtil.uploadFile(url.url+"/resources/"+res_id+"/image", new FileSystemResource(output), croppedName,String.class);

                System.out.println(url.url+"/resources/"+res_id+"/image");
                System.out.println(result);
                if (result.contains("success")) System.out.println("upload cropped image Success !!!!!");
                else System.out.println("upload cropped image Fail !!!!!");
                Intent intent = new Intent(CropperActivity.this, AddMemoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName",userName);
                imageUri = Uri.fromFile(output);
                intent.setData(imageUri);
                bundle.putString("croppedName",croppedName);
                bundle.putInt("res_id",res_id);
                intent.putExtras(bundle);
                startActivity(intent);
                CropperActivity.this.finish();
            }
        });
    }
}
