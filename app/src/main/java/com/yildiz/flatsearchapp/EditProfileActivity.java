package com.yildiz.flatsearchapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText phoneNr, editBolum, editDistance, editTime;
    ImageView profilePicture;
    TextView nameTxt,surnameTxt,emailTxt;
    Button degisiklikKaydet;
    Spinner spinner;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public static final int GALLERY_REQUEST_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 103;
    String[] status = new String[]{"Ev/Ev Arkadaşı Aramıyor","Ev Arıyor","Ev Arkadaşı Arıyor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useredit_page);
        phoneNr = findViewById(R.id.editTextPhoneNr);
        profilePicture = findViewById(R.id.imageView);
        editBolum =findViewById(R.id.editBolum);
        editDistance =findViewById(R.id.editDistance);
//        oldPass = findViewById(R.id.editPasswordOldPass);
//        newPass =findViewById(R.id.editPasswordNewPass);
//        newPassAgain = findViewById(R.id.editPasswordNewPassAgain);
        degisiklikKaydet =  findViewById(R.id.saveChangesButton);
//        sifreKaydet = findViewById(R.id.savePasswordButton);
        emailTxt = findViewById(R.id.TextEmail);
        nameTxt = findViewById(R.id.TextName);
        surnameTxt = findViewById(R.id.TextSurname);
        editTime = findViewById(R.id.editTime);
        spinner = findViewById(R.id.spinner);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance("https://mezunapp-5b548-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("Users");
        Query query = databaseReference.orderByChild("eMail").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String userID = firebaseAuth.getCurrentUser().getUid(); // Get the user's unique ID

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference profilePictureRef = storageRef.child("profile_pictures").child(userID + ".jpg");



                profilePictureRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();

                        // Load the image into the ImageView using Glide or Picasso
                        Glide.with(EditProfileActivity.this).load(imageUrl).into(profilePicture);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NotNull Exception e) {
                        // Failed to retrieve the download URL
                        // Handle the failure scenario as per your requirements
                    }
                });




                String name,surname,phoneNum,email,bolum,mesafe, time;
                int indexStatus;
                for(DataSnapshot ds: snapshot.getChildren()){

                    UserItem userItem = ds.getValue(UserItem.class);

                    indexStatus = userItem.status;
                    name = userItem.name;
                    surname =userItem.surname;
                    phoneNum = userItem.phoneNr;
                    email = userItem.eMail;
                    bolum = userItem.department;
                    mesafe = Integer.toString(userItem.distance);
                    time = userItem.timeSpend;
                    nameTxt.setText(name);
                    surnameTxt.setText(surname);
                    emailTxt.setText(email);
                    phoneNr.setText(phoneNum);
                    editBolum.setText(bolum);
                    editDistance.setText(mesafe);
                    editTime.setText(time);
                    spinner.setSelection(indexStatus);
                  // Load the profile picture into the ImageView using Glide
//                    Glide.with(getApplicationContext())
//                            .load(profilePictureRef)
//                            .into(profilePicture);



                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        degisiklikKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int spinnerSelectedItemPosition = spinner.getSelectedItemPosition();

                String uid = firebaseAuth.getCurrentUser().getUid();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("phoneNr",phoneNr.getText().toString());
                hashMap.put("department",editBolum.getText().toString());
                hashMap.put("distance",Integer.parseInt(editDistance.getText().toString()));
                hashMap.put("status",spinnerSelectedItemPosition);
                hashMap.put("timeSpend",editTime.getText().toString());
                databaseReference.child(uid).updateChildren(hashMap);
                Toast.makeText(getApplicationContext(),"Değişiklikler Kaydedildi.",Toast.LENGTH_LONG).show();
            }
        });



    }


    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(EditProfileActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_edit_picture:
                        showPictureDialog();
                        return true;
                    case R.id.menu_remove_picture:
                        // Remove picture logic
                        removeProfilePicture();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(EditProfileActivity.this);
        pictureDialog.setTitle("Seçiniz");
        String[] pictureDialogItems = {
                "Galeriden seç",
                "Kamera ile çek"
        };
        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 1) {
                    // Check camera permission before taking a photo from the camera
                    if (checkCameraPermission()) {
                        takePhotoFromCamera();
                    } else {
                        requestCameraPermission();
                    }
                } else {
                    choosePhotoFromGallery();
                }
            }
        });
        pictureDialog.show();

    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void takePhotoFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    uploadImageToFirebaseStorage(selectedImage);
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null && data.getExtras() != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    Uri selectedImage = getImageUri(getApplicationContext(), imageBitmap);
                    uploadImageToFirebaseStorage(selectedImage);
                }
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {

        String userID = firebaseAuth.getCurrentUser().getUid(); // Get the user's unique ID
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profilePictureRef = storageRef.child("profile_pictures/" + userID + ".jpg");

        profilePictureRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Profile picture uploaded successfully
                        // Retrieve the download URL of the uploaded image
                        profilePictureRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();

                                // Load the image into the ImageView using Glide or Picasso
                                Glide.with(EditProfileActivity.this).load(imageUrl).into(profilePicture);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NotNull Exception e) {
                                // Failed to retrieve the download URL
                                // Handle the failure scenario as per your requirements
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NotNull Exception e) {
                        // Failed to upload the profile picture
                        // Handle the failure scenario as per your requirements
                    }
                });
    }


    private void removeProfilePicture() {
        // Remove the profile picture logic
        // Handle the removal as per your requirements
        Glide.with(EditProfileActivity.this)
                .clear(profilePicture);
    }



    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted
                takePhotoFromCamera();
            } else {
                // Camera permission has been denied
                // Handle the denial case, e.g., display a message or disable camera-related functionality
            }
        }
    }




}
