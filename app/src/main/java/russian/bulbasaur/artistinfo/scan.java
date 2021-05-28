package russian.bulbasaur.artistinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import russian.bulbasaur.artistinfo.databinding.ActivityScanBinding;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

public class scan extends AppCompatActivity {
    private boolean lock;
    private AppBarConfiguration appBarConfiguration;
    private ActivityScanBinding binding;
    private CodeScanner scan;
    private ImageView files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if(Build.VERSION.SDK_INT>=23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }
        files = (ImageView)findViewById(R.id.files);
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_read();
            }
        });
        CodeScannerView scanner = (CodeScannerView)findViewById(R.id.scanner);
        scan = new CodeScanner(this,scanner);
        scan.setScanMode(ScanMode.SINGLE);
            scan.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                test(result.getText());
                        }
                    });

                }
            });
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.howto,null);
        ad.setView(v);
        ad.setCancelable(false);
        Button b = v.findViewById(R.id.button);
        AlertDialog alert = ad.show();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
                alert.dismiss();
                scan.startPreview();
            }
        });
    }
    public void file_read()
    {
       Intent i = new Intent();
       i.setType("image/*");
       i.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(i,3);
    }
public void decode_file(Bitmap bitmap)
{
    try {
        String decoded = null;
        int[] intArray = new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(intArray,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(),bitmap.getHeight(),intArray);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new MultiFormatReader();
        Result res = reader.decode(bitmap1);
        decoded = res.getText();
        test(decoded);
    }
    catch (Exception e)
    {
        Toast.makeText(this, "Error while decoding file", Toast.LENGTH_SHORT).show();
    }
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==3)
            {
                Uri uri = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    decode_file(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void test(String res)
    {
        Intent i =new Intent(this,display_dis.class);
        i.putExtra("result",res);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scan.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scan.releaseResources();
    }

}