package com.yildiz.flatsearchapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FilteredUserActivity extends AppCompatActivity {

    private TextView userNameTextView, userSurnameTextView, userEmailTextView,
            userPhoneTextView, userDepartmentTextView, userDistanceTextView, userTimeTextView,statusTextView;
    private Button whatsappButton, mailButton;
    String[] statusArray = new String[]{"Ev/Ev Arkadaşı Aramıyor","Ev Arıyor","Ev Arkadaşı Arıyor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdetails_page);

        // Initialize views
        userNameTextView = findViewById(R.id.filteredUserName);
        userSurnameTextView = findViewById(R.id.filteredUserSurname);
        userEmailTextView = findViewById(R.id.filteredUserMail);
        userPhoneTextView = findViewById(R.id.filteredUserPhone);
        userDepartmentTextView = findViewById(R.id.filteredUserDepartment);
        userDistanceTextView = findViewById(R.id.filteredUserDistance);
        userTimeTextView = findViewById(R.id.filteredUserTime);
        whatsappButton = findViewById(R.id.viaWhatsappButton);
        mailButton = findViewById(R.id.viaMailButton);
        statusTextView = findViewById(R.id.filteredUserStatus);
        // Get data from Intent
        UserItem userItem = (UserItem) getIntent().getSerializableExtra("userItem");

        String name = userItem.name;
        String surname = userItem.surname;
        String email = userItem.eMail;
        String phone = userItem.phoneNr;
        String department = userItem.department;
        int distance = userItem.distance;
        int status = userItem.status;
        String time = userItem.timeSpend;

        // Set data to views
        userNameTextView.setText(name);
        userSurnameTextView.setText(surname);
        userEmailTextView.setText(email);
        userPhoneTextView.setText(phone);
        userDepartmentTextView.setText(department);
        userDistanceTextView.setText(String.valueOf(distance));
        userTimeTextView.setText(time);
        statusTextView.setText(statusArray[status]);

        // WhatsApp button click listener
        whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = userPhoneTextView.getText().toString();
                openWhatsAppChat(phoneNumber);
            }
        });

        // Mail button click listener
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = userEmailTextView.getText().toString();
                sendEmail(emailAddress);
            }
        });
    }

    // Open WhatsApp chat with a phone number
    private void openWhatsAppChat(String phoneNumber) {
        String whatsappUrl = "https://api.whatsapp.com/send?phone=" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(whatsappUrl));
        startActivity(intent);
    }

    // Send email
    private void sendEmail(String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        startActivity(intent);
    }
}
