package russian.bulbasaur.artistinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import russian.bulbasaur.artistinfo.databinding.ActivityDisplayDisBinding;

import org.json.JSONObject;

public class display_dis extends AppCompatActivity implements View.OnClickListener{
    String name = "";
    String about = "";
    String aboutart = "";
    String shop = "";
    String insta = "";
    String face = "";
    String snap = "";
    String twitter = "";
    private ActivityDisplayDisBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayDisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if(Build.VERSION.SDK_INT>=23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }
        String out = getIntent().getStringExtra("result");
        try {
            JSONObject ob = new JSONObject(out.toString());
            name = ob.getString("name");
            about =  ob.getString("about");
            aboutart =  ob.getString("about_art");
            shop = ob.getString("shop");
            insta =  ob.getString("insta");
            face = ob.getString("face");
            snap = ob.getString("snap");
            twitter =  ob.getString("twitter");
        }catch (Exception e){}
        TextView about_tv = (TextView)findViewById(R.id.about);
        TextView name_tv = (TextView)findViewById(R.id.name);
        TextView aboutart_tv = (TextView)findViewById(R.id.aboutart);
        TextView shop_tv = (TextView)findViewById(R.id.hyperlink);
        ImageView insta_im = (ImageView)findViewById(R.id.instagram);
        ImageView face_im = (ImageView)findViewById(R.id.facebook);
        ImageView snap_im = (ImageView)findViewById(R.id.snapchat);
        ImageView twit_im = (ImageView)findViewById(R.id.twitter);
        about_tv.setText(about);
        aboutart_tv.setText(aboutart);
        shop_tv.setText(shop);
        name_tv.setText(name);
        shop_tv.setOnClickListener(this);
        insta_im.setOnClickListener(this);
        face_im.setOnClickListener(this);
        snap_im.setOnClickListener(this);
        twit_im.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int a = 0;
        String url = "https://www.google.com";
        if(v.getId()==R.id.instagram)
        {
            if(insta.equalsIgnoreCase("NA")){
                Toast.makeText(this,"Not Available",Toast.LENGTH_LONG).show();a=1;}
            else
                url = "https://instagram.com/"+insta;
        }
        else if(v.getId()==R.id.facebook)
        {
            if(face.equalsIgnoreCase("NA")){
                Toast.makeText(this,"Not Available",Toast.LENGTH_LONG).show();a=1;}
            else
                url = "https://facebook.com/"+face;
        }
        else if(v.getId()==R.id.snapchat)
        {
            if(snap.equalsIgnoreCase("NA")){
                Toast.makeText(this,"Not Available",Toast.LENGTH_LONG).show();a=1;}
            else
                url = "https://snapchat.com/"+snap;
        }
        else if(v.getId()==R.id.twitter)
        {
            if(twitter.equalsIgnoreCase("NA")){
                Toast.makeText(this,"Not Available",Toast.LENGTH_LONG).show();a=1;}
            else
                url = "https://twitter.com/"+twitter;
        }
        else if(v.getId()==R.id.hyperlink)
        {
            if(shop.isEmpty()){
                Log.e("","");a=1;}
            else
                url = shop;
        }
        if(a==0) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }
}