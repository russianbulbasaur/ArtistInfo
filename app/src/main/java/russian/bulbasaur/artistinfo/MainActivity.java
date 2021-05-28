package russian.bulbasaur.artistinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT>=23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] s = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(this,s,2);
            }
        }
    }
    public void scan(View view)
    {
        Intent i = new Intent(this,scan.class);
        startActivity(i);
    }
    public void gen(View view)
    {
        Intent i = new Intent(this,gernerator.class);
        startActivity(i);
    }
}