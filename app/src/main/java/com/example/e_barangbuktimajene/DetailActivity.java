package com.example.e_barangbuktimajene;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "Name";
    public static final String EXTRA_REG = "Registratiom";
    public static final String EXTRA_INFO = "Desc";
    public static final String EXTRA_STATUS = "Status";
    public static final String EXTRA_IMAGE = "Image";

    ImageView showImageView;
    TextView name, reg, info, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        showImageView = findViewById(R.id.ShowImageView);
        name = findViewById(R.id.tv_item_name);
        reg = findViewById(R.id.tv_item_Registration);
        info = findViewById(R.id.tv_item_info);
        status = findViewById(R.id.tv_item_status);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra(EXTRA_NAME)) {

            String extName = getIntent().getExtras().getString(EXTRA_NAME);
            String extReg = getIntent().getExtras().getString(EXTRA_REG);
            String extInfo = getIntent().getExtras().getString(EXTRA_INFO);
            String extStatus = getIntent().getExtras().getString(EXTRA_STATUS);
            String extImage = getIntent().getExtras().getString(EXTRA_IMAGE);

            name.setText(extName);
            reg.setText(extReg);
            info.setText(extInfo);
            status.setText(extStatus);

            Glide.with(this)
                    .load(extImage)
                    .apply(new RequestOptions())
                    .into(showImageView);

            DetailActivity.this.setTitle(extName);
        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }

    }
}
