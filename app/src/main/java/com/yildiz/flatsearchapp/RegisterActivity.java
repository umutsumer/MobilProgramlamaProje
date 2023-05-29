package com.yildiz.flatsearchapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    EditText name,surname,email,password,startDate,endDate;
    Button registerButton;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference reference;
    String emailRegEx = ".*std.yildiz.edu.tr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        registerButton = findViewById(R.id.createUserButton);
        name = findViewById(R.id.editTextName);
        surname =  findViewById(R.id.editTextSurname);
        email =  findViewById(R.id.editTextEmailAddress);
        password =  findViewById(R.id.editTextPassword);
        startDate = findViewById(R.id.editBolum);
        endDate = findViewById(R.id.editDistance);
        progressBar = findViewById(R.id.registerProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        database = FirebaseDatabase.getInstance("https://mezunapp-5b548-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("Users");
        auth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            authorization();
        });
    }

    public void saveData(String name,String surname,String email,FirebaseUser user){
        String uid = user.getUid();
        UserItem uItem = new UserItem(uid,name,surname,email,"","",0,"","",0,"Inactive");
        //System.out.printf("%d",uItem.isVerified);
        if(user!=null){
            reference.child(uid).setValue(uItem).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Aktivasyon Linki Mail Adresinize Gönderildi",Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            System.out.println(e.getMessage());

                        }
                    });

                    Toast.makeText(getApplicationContext(),"Başarıyla kayıt oldunuz.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(),"Kayıt Başarısız.",Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void authorization(){
        int check = 1;
        String nameText = name.getText().toString();
        String surnameText = surname.getText().toString();
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
//        if(!emailText.matches(emailRegEx)){ // TODO will actvate later
//            email.setError("Öğrenci mail adresinizi giriniz.");
//            email.requestFocus();
//            check = 0;
//        }
        if(nameText.isEmpty()){
            name.setError("Lütfen adınızı giriniz.");
            name.requestFocus();
            check = 0;
        }
        if(surnameText.isEmpty()){
            surname.setError("Lütfen soyadınızı giriniz.");
            surname.requestFocus();
            check = 0;
        }
        if(passwordText.isEmpty()){
            password.setError("Lütfen parolanızı giriniz.");
            password.requestFocus();
            check = 0;
        }

        if(check == 1){

            auth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    String userEmail = user.getEmail();
                    saveData(name.getText().toString(),surname.getText().toString(),userEmail,user);

                }else{
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (Exception e) {
                        //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        email.setError("Bu e-posta ile ilişkilendirilmiş bir hesap var.");
                        email.requestFocus();
                    }
                }
            });
        }else{
            progressBar.setVisibility(View.GONE);
        }




    }
}
