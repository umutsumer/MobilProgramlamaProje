package com.yildiz.flatsearchapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    EditText userName,passWord;
    TextView registerText, forgotPasswordText;
    FirebaseAuth auth;
    public ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        userName = findViewById(R.id.emailText);
        passWord = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.button);
        registerText = findViewById(R.id.createUsterText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        forgotPasswordText = findViewById(R.id.forgotPassword);
        auth = FirebaseAuth.getInstance();


        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });





        registerText.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                view.getContext().startActivity(intent);
            }catch (RuntimeException e){
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        });



        loginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loginBtn.setEnabled(false);
                auth.signInWithEmailAndPassword(userName.getText().toString(),passWord.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){

                                auth.getCurrentUser();
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mezunapp-5b548-default-rtdb.europe-west1.firebasedatabase.app");
                                DatabaseReference usersRef = firebaseDatabase.getReference("Users");

                                DatabaseReference uidRef = usersRef.child(auth.getCurrentUser().getUid());

                                if (auth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(getApplicationContext(), "Başarıyla giriş yaptınız.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(view.getContext(), FeedActivity.class);
                                    view.getContext().startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Kullanıcı Aktifleştirilmemiş, Mailinizi kontrol ediniz.",Toast.LENGTH_LONG).show();
                                    loginBtn.setEnabled(true);
                                    return;
                                }
                                // Retrieve the specific field using a ValueEventListener
//                                uidRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
//                                        // Check if the field exists
//                                        if (dataSnapshot.exists()) {
//                                            UserItem userItem = dataSnapshot.getValue(UserItem.class);
//                                            if (!userItem.isVerified.equals("Activated")){
//                                                Toast.makeText(getApplicationContext(),"Kullanıcı Aktifleştirilmemiş, Mailinizi kontrol ediniz.",Toast.LENGTH_LONG).show();
//                                                loginBtn.setEnabled(true);
//                                                return;
//                                            }else{
//                                                Toast.makeText(getApplicationContext(),"Başarıyla giriş yaptınız.",Toast.LENGTH_LONG).show();
//                                                Intent intent = new Intent(view.getContext(), FeedActivity.class);
//                                                view.getContext().startActivity(intent);
//                                                finish();
//                                            }
//                                            // Use the field value as needed
//                                            // TODO: Perform your logic with the field value
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NotNull DatabaseError databaseError) {
//                                        // Handle any potential errors
//                                    }
//                                });



                            }else{
                                loginBtn.setEnabled(true);
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    userName.setError("Hatalı Mail Adresi.");
                                    userName.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException e){
                                    passWord.setError("Hatalı Şifre.");
                                    passWord.requestFocus();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                    }
                });
            }
        });

    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Şifreni sıfırla.");

        LinearLayout linearLayout = new LinearLayout(this);
        EditText emailTxt = new EditText(this);
        emailTxt.setHint("E-mail Adresiniz");
        emailTxt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailTxt.setMinEms(16);
        linearLayout.addView(emailTxt);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        builder.setPositiveButton("Sıfırla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = emailTxt.getText().toString().trim();
                sendRecovery(email);
            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void sendRecovery(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Sıfırlama emaili gönderildi.",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Hata! Sıfırlama maili gönderilemedi.",Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    protected void onPause() {

        super.onPause();

    }
}