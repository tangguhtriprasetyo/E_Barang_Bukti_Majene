package com.example.e_barangbuktimajene;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Arrays;

public class UploadImageActivity extends AppCompatActivity {

    // Root Database Name for Firebase Database.
    public static final String Database_Path_Process = "On_Process_Item";
    public static final String Database_Path_Taken = "Taken_Item";
    public static final String Database_Path_Return = "Return_Item";
    private static final String[] STATUSITEM = {
            "Diproses",
            "Dirampas",
            "Dikembalikan"
    };
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";

    // Creating EditText.
    EditText ImageName;
    EditText RegistrationName;
    EditText InformationName;
    String StatusName;

    Button UploadButton;

    // Creating ImageView.
    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReferenceProcess;
    DatabaseReference databaseReferenceTaken;
    DatabaseReference databaseReferenceReturn;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReferenceProcess = FirebaseDatabase.getInstance().getReference(Database_Path_Process);
        databaseReferenceTaken = FirebaseDatabase.getInstance().getReference(Database_Path_Taken);
        databaseReferenceReturn = FirebaseDatabase.getInstance().getReference(Database_Path_Return);

        UploadButton = findViewById(R.id.ButtonUploadImage);

        // Assign ID's to EditText.
        ImageName = findViewById(R.id.ImageNameEditText);
        RegistrationName = findViewById(R.id.RegistrationEditText);
        InformationName = findViewById(R.id.InformationEditText);

        // Assign ID'S to image view.
        SelectImage = findViewById(R.id.ShowImageView);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(UploadImageActivity.this);

        openGallery();
        showDialog();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void UploadProcess() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Menambahkan Barang Bukti");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Getting image name from EditText and store into string variable.
                                    String TempImageName = ImageName.getText().toString().trim();
                                    String TempRegistrationName = RegistrationName.getText().toString().trim();
                                    String TempInformationName = InformationName.getText().toString().trim();
                                    String TempStatusName = StatusName.trim();

                                    // Hiding the progressDialog after done uploading.
                                    progressDialog.dismiss();

                                    // Showing toast message after done uploading.
                                    Toast.makeText(getApplicationContext(), "Barang Bukti Berhasil Ditambahkan ", Toast.LENGTH_LONG).show();
                                    Uri downloadUrl = uri;
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, TempRegistrationName, TempInformationName, TempStatusName, downloadUrl.toString());

                                    // Getting image upload ID.
                                    String ImageUploadId = databaseReferenceProcess.push().getKey();

                                    // Adding image upload id s child element into databaseReference.
                                    databaseReferenceProcess.child(ImageUploadId).setValue(imageUploadInfo);

                                    finish();
                                }

                            });


                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception error message.
                            Toast.makeText(UploadImageActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Menambahkan Barang Bukti");

                        }
                    });
        } else {

            Toast.makeText(UploadImageActivity.this, "Silahkan Pilih Barang Bukti", Toast.LENGTH_LONG).show();

        }
    }

    public void UploadTaken() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Menambahkan Barang Bukti");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Getting image name from EditText and store into string variable.
                                    String TempImageName = ImageName.getText().toString().trim();
                                    String TempRegistrationName = RegistrationName.getText().toString().trim();
                                    String TempInformationName = InformationName.getText().toString().trim();
                                    String TempStatusName = StatusName.trim();

                                    // Hiding the progressDialog after done uploading.
                                    progressDialog.dismiss();

                                    // Showing toast message after done uploading.
                                    Toast.makeText(getApplicationContext(), "Barang Bukti Berhasil Ditambahkan ", Toast.LENGTH_LONG).show();
                                    Uri downloadUrl = uri;
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, TempRegistrationName, TempInformationName, TempStatusName, downloadUrl.toString());

                                    // Getting image upload ID.
                                    String ImageUploadId = databaseReferenceTaken.push().getKey();

                                    // Adding image upload id s child element into databaseReference.
                                    databaseReferenceTaken.child(ImageUploadId).setValue(imageUploadInfo);

                                    finish();
                                }

                            });


                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception error message.
                            Toast.makeText(UploadImageActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Menambahkan Barang Bukti");

                        }
                    });
        } else {

            Toast.makeText(UploadImageActivity.this, "Silahkan Pilih Barang Bukti", Toast.LENGTH_LONG).show();

        }
    }

    public void UploadReturn() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Menambahkan Barang Bukti");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Getting image name from EditText and store into string variable.
                                    String TempImageName = ImageName.getText().toString().trim();
                                    String TempRegistrationName = RegistrationName.getText().toString().trim();
                                    String TempInformationName = InformationName.getText().toString().trim();
                                    String TempStatusName = StatusName.trim();

                                    // Hiding the progressDialog after done uploading.
                                    progressDialog.dismiss();

                                    // Showing toast message after done uploading.
                                    Toast.makeText(getApplicationContext(), "Barang Bukti Berhasil Ditambahkan ", Toast.LENGTH_LONG).show();
                                    Uri downloadUrl = uri;
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, TempRegistrationName, TempInformationName, TempStatusName, downloadUrl.toString());

                                    // Getting image upload ID.
                                    String ImageUploadId = databaseReferenceReturn.push().getKey();

                                    // Adding image upload id s child element into databaseReference.
                                    databaseReferenceReturn.child(ImageUploadId).setValue(imageUploadInfo);

                                    finish();
                                }

                            });


                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception error message.
                            Toast.makeText(UploadImageActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Menambahkan Barang Bukti");

                        }
                    });
        } else {

            Toast.makeText(UploadImageActivity.this, "Silahkan Pilih Barang Bukti", Toast.LENGTH_LONG).show();

        }
    }

    public void showDialog() {
        // Initializing a new alert dialog
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        // Set the alert dialog title
        builder.setTitle("Pilih Status Barang Bukti");
        builder.setSingleChoiceItems(
                STATUSITEM,
                0, // Index of checked item (-1 = no selection)
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
                switch (StatusName) {
                    case "Diproses":

                        // Adding click listener to Upload image button.
                        UploadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Calling method to upload selected image on Firebase storage.
                                UploadProcess();

                            }
                        });
                        Toast.makeText(UploadImageActivity.this, "Status Barang Bukti : " + StatusName, Toast.LENGTH_LONG).show();
                        break;

                    case "Dirampas":

                        // Adding click listener to Upload image button.
                        UploadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Calling method to upload selected image on Firebase storage.
                                UploadTaken();

                            }
                        });
                        Toast.makeText(UploadImageActivity.this, "Status Barang Bukti : " + StatusName, Toast.LENGTH_LONG).show();
                        break;

                    case "Dikembalikan":

                        // Adding click listener to Upload image button.
                        UploadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Calling method to upload selected image on Firebase storage.
                                UploadReturn();

                            }
                        });
                        Toast.makeText(UploadImageActivity.this, "Status Barang Bukti : " + StatusName, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }).show();

    }

    public void openGallery() {
        // Creating intent.
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
    }
}
