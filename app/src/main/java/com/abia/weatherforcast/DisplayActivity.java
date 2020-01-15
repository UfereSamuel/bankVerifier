package com.abia.weatherforcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {
    private TextView tvName, tvBank, tvAcctNo;
    private String name, bank, account;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        tvName = findViewById(R.id.tvFullname);
        tvBank = findViewById(R.id.tvBank);
        tvAcctNo = findViewById(R.id.tvAcctNo);

        intent = new Intent();
        name = intent.getStringExtra("name");
        bank = intent.getStringExtra("bank");
        account = intent.getStringExtra("acct");

        tvName.setText(name);
        tvBank.setText(bank);
        tvAcctNo.setText(account);
    }
}
