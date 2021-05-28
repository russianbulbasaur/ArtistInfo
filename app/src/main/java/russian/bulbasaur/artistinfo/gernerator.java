package russian.bulbasaur.artistinfo;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import russian.bulbasaur.artistinfo.databinding.ActivityGerneratorBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;

public class gernerator extends AppCompatActivity implements View.OnClickListener {
    private ActivityGerneratorBinding binding;
    String name = "";
    String about = "";
    String about_art = "";
    String shop = "";
    String data = "";
    LottieAnimationView lottie;
    Button close;
    TextView tv;
    ImageView im;
    String insta = "";
    Bitmap image;
    String snap = "";
    String face = "";
    String twit = "";
    TextInputEditText name_et,about_et,aboutart_et,shop_et,insta_et,snap_et,face_et,twit_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGerneratorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if(Build.VERSION.SDK_INT>=23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }
        name_et = (TextInputEditText) findViewById(R.id.name);
        about_et = (TextInputEditText) findViewById(R.id.about);
        aboutart_et = (TextInputEditText) findViewById(R.id.aboutart);
        shop_et = (TextInputEditText) findViewById(R.id.shoplink);
        insta_et = (TextInputEditText) findViewById(R.id.instahandle);
        snap_et = (TextInputEditText) findViewById(R.id.snaphandle);
        face_et = (TextInputEditText) findViewById(R.id.facehandle);
        twit_et = (TextInputEditText) findViewById(R.id.twitterhandle);
        Button b = (Button) findViewById(R.id.gen_but);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Handler h = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    digest.update(data.getBytes());
                    byte[] messagedigest = digest.digest();
                    String hash = messagedigest.toString();
                    close.setVisibility(View.VISIBLE);
                    lottie.setVisibility(View.INVISIBLE);
                    tv.setVisibility(View.INVISIBLE);
                    im.setVisibility(View.VISIBLE);
                    im.setImageBitmap(image);
                    Toast.makeText(getApplicationContext(),"QR saved to storage",Toast.LENGTH_LONG).show();
                    File f = new File(Environment.getExternalStorageDirectory().toString() + "/"+hash+".jpeg");
                    f.createNewFile();
                    FileOutputStream fos = new FileOutputStream(f);
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                }catch (Exception e){}
            }
        };
        name = name_et.getText().toString().trim();
        about = about_et.getText().toString().trim();
        about_art = aboutart_et.getText().toString().trim();
        shop = shop_et.getText().toString().trim();
        insta = insta_et.getText().toString().trim();
        snap = snap_et.getText().toString().trim();
        face = face_et.getText().toString().trim();
        twit = twit_et.getText().toString().trim();
        if (name.isEmpty())
            name_et.setError("Required");
        else if (about.isEmpty())
            about_et.setError("Required");
        else if (about_art.isEmpty())
            about_et.setError("Required");
        else {
            insta = insta.isEmpty()?"NA":insta;
            face = face.isEmpty()?"NA":face;
            snap = snap.isEmpty()?"NA":snap;
            twit = twit.isEmpty()?"NA":twit;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(9000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    h.sendEmptyMessage(0);
                }
            });
            data = "{'name' : '" + name + "','about':'" + about + "','about_art':'" + about_art + "','shop':'" + shop + "','insta':'" + insta + "','face':'" + face+ "','snap':'" + snap + "','twitter':'" + twit + "'}";
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                BitMatrix b = writer.encode(data, BarcodeFormat.QR_CODE, 300, 300);
                BarcodeEncoder encoder = new BarcodeEncoder();
                image = encoder.createBitmap(b);
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                v = getLayoutInflater().inflate(R.layout.loading,null);
                ad.setView(v);
                AlertDialog alert = ad.show();
                lottie = v.findViewById(R.id.lottie);
                tv = v.findViewById(R.id.gentext);
                im = v.findViewById(R.id.qr);
                close = v.findViewById(R.id.button);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        alert.cancel();
                    }
                });
                t.start();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}