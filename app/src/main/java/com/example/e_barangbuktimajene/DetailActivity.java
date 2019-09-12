package com.example.e_barangbuktimajene;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "Name";
    public static final String EXTRA_REG = "Registratiom";
    public static final String EXTRA_INFO = "Desc";
    public static final String EXTRA_STATUS = "Status";
    public static final String EXTRA_IMAGE = "Image";
    public static final String EXTRA_IMAGEID = "ImageId";

    private static final String[] STATUSITEM = {
            "Diproses",
            "Dirampas",
            "Dikembalikan",
            "Hapus Barang Bukti"
    };

    DatabaseReference databaseReferenceProcess;
    DatabaseReference databaseReferenceTaken;
    DatabaseReference databaseReferenceReturn;

    ImageView showImageView;
    TextView name, reg, info, status;
    Button changeStatusButton;
    String StatusName = "Pilih Status Barang Bukti!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        databaseReferenceProcess = FirebaseDatabase.getInstance().getReference(MainActivity.Database_Path_Process);
        databaseReferenceTaken = FirebaseDatabase.getInstance().getReference(MainActivity.Database_Path_Taken);
        databaseReferenceReturn = FirebaseDatabase.getInstance().getReference(MainActivity.Database_Path_Return);

        changeStatusButton = findViewById(R.id.btn_change_status);

        showImageView = findViewById(R.id.ShowImageView);
        name = findViewById(R.id.tv_item_name);
        reg = findViewById(R.id.tv_item_Registration);
        info = findViewById(R.id.tv_item_info);
        status = findViewById(R.id.tv_item_status);

        getIncomingIntent();

        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();

            }
        });
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

    public void deleteDataProcess() {
        String extNameId = getIntent().getExtras().getString(EXTRA_IMAGEID);
        databaseReferenceProcess.child(extNameId).removeValue();
        Toast.makeText(this, extNameId, Toast.LENGTH_LONG).show();
    }

    public void deleteDataTaken() {
        String extNameId = getIntent().getExtras().getString(EXTRA_IMAGEID);
        databaseReferenceTaken.child(extNameId).removeValue();
        Toast.makeText(this, extNameId, Toast.LENGTH_LONG).show();
    }

    public void deleteDataReturn() {
        String extNameId = getIntent().getExtras().getString(EXTRA_IMAGEID);
        databaseReferenceReturn.child(extNameId).removeValue();
        Toast.makeText(this, extNameId, Toast.LENGTH_LONG).show();
    }

    public void showDialog() {
        // Initializing a new alert dialog
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        // Set the alert dialog title
        builder.setTitle("Pilih Status Barang Bukti");
        builder.setSingleChoiceItems(
                STATUSITEM,
                -1, // Index of checked item (-1 = no selection)
                new DialogInterface.OnClickListener() // Item click listener
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get the alert dialog selected item's text
                        StatusName = Arrays.asList(STATUSITEM).get(i);
                    }
                });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Just dismiss the alert dialog after selection
                // Or do something now
                selectedStatus();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Just dismiss the alert dialog after selection
                // Or do something now
                dialogInterface.cancel();
            }
        }).show();

    }

    public void selectedStatus() {
        switch (StatusName) {

            case "Diproses":

                // Calling method to upload selected image on Firebase storage.
                UploadProcess();
                deleteData();

                Toast.makeText(this, "Status Barang Bukti : " + StatusName, Toast.LENGTH_LONG).show();
                finish();
                break;

            case "Dirampas":

                // Calling method to upload selected image on Firebase storage.
                UploadTaken();
                deleteData();

                Toast.makeText(this, "Status Barang Bukti : " + StatusName, Toast.LENGTH_LONG).show();
                finish();
                break;

            case "Dikembalikan":

                // Calling method to upload selected image on Firebase storage.
                UploadReturn();
                deleteData();

                Toast.makeText(this, "Status Barang Bukti : " + StatusName, Toast.LENGTH_LONG).show();
                finish();
                break;

            case "Hapus Barang Bukti":

                deleteData();
                Toast.makeText(this, "Status Barang Bukti : " + StatusName, Toast.LENGTH_LONG).show();
                finish();
                break;

            default:
                Toast.makeText(this, StatusName, Toast.LENGTH_LONG).show();
                showDialog();
        }
    }

    public void deleteData() {
        String extStatus = getIntent().getExtras().getString(EXTRA_STATUS);
        switch (extStatus) {

            case "Diproses":
                deleteDataProcess();
                break;

            case "Dirampas":
                deleteDataTaken();
                break;

            case "Dikembalikan":
                deleteDataReturn();
                break;
        }
    }

    public void UploadProcess() {

        // Getting image name from EditText and store into string variable.
        String TempImageName = getIntent().getExtras().getString(EXTRA_NAME).trim();
        String TempRegistrationName = getIntent().getExtras().getString(EXTRA_REG).trim();
        String TempInformationName = getIntent().getExtras().getString(EXTRA_INFO).trim();
        String TempStatusName = StatusName.trim();
        String TempImageUrl = getIntent().getExtras().getString(EXTRA_IMAGE).trim();

        // Getting image upload ID.
        String ImageUploadId = databaseReferenceProcess.push().getKey();

        ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, TempRegistrationName, TempInformationName, TempStatusName, TempImageUrl, ImageUploadId);

        // Adding image upload id s child element into databaseReference.
        databaseReferenceProcess.child(ImageUploadId).setValue(imageUploadInfo).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailActivity.this, "Status Berhasil Diubah", Toast.LENGTH_LONG).show();
                finish();
            }
        })

                // If something goes wrong .
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Showing exception error message.
                        Toast.makeText(DetailActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void UploadTaken() {

        // Getting image name from EditText and store into string variable.
        String TempImageName = getIntent().getExtras().getString(EXTRA_NAME).trim();
        String TempRegistrationName = getIntent().getExtras().getString(EXTRA_REG).trim();
        String TempInformationName = getIntent().getExtras().getString(EXTRA_INFO).trim();
        String TempStatusName = StatusName.trim();
        String TempImageUrl = getIntent().getExtras().getString(EXTRA_IMAGE).trim();

        // Getting image upload ID.
        String ImageUploadId = databaseReferenceTaken.push().getKey();

        ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, TempRegistrationName, TempInformationName, TempStatusName, TempImageUrl, ImageUploadId);

        // Adding image upload id s child element into databaseReference.
        databaseReferenceTaken.child(ImageUploadId).setValue(imageUploadInfo).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //progressDialog.dismiss();
                Toast.makeText(DetailActivity.this, "Status Berhasil Diubah", Toast.LENGTH_LONG).show();
                finish();
            }
        })

                // If something goes wrong .
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Showing exception error message.
                        Toast.makeText(DetailActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void UploadReturn() {

        // Getting image name from EditText and store into string variable.
        String TempImageName = getIntent().getExtras().getString(EXTRA_NAME).trim();
        String TempRegistrationName = getIntent().getExtras().getString(EXTRA_REG).trim();
        String TempInformationName = getIntent().getExtras().getString(EXTRA_INFO).trim();
        String TempStatusName = StatusName.trim();
        String TempImageUrl = getIntent().getExtras().getString(EXTRA_IMAGE).trim();

        // Getting image upload ID.
        String ImageUploadId = databaseReferenceReturn.push().getKey();

        ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, TempRegistrationName, TempInformationName, TempStatusName, TempImageUrl, ImageUploadId);

        // Adding image upload id s child element into databaseReference.
        databaseReferenceReturn.child(ImageUploadId).setValue(imageUploadInfo).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailActivity.this, "Status Berhasil Diubah", Toast.LENGTH_LONG).show();
                finish();
            }
        })

                // If something goes wrong .
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Showing exception error message.
                        Toast.makeText(DetailActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }
}
