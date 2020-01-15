package com.abia.weatherforcast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BankVerifier extends AppCompatActivity {
    private SmartMaterialSpinner spProvince;
    private List<String> bankList;
    private String settlement_bank, account_number;
    private EditText etAcct;
    private Button btnProceed;
    private ProgressDialog dialog;
    private String fullname, bank, acctNumber, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_verifier);

        dialog = new ProgressDialog(this);

        etAcct = findViewById(R.id.etAcct);
        btnProceed = findViewById(R.id.btnProceed);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed();
            }
        });

        initSpinner();
    }

    private void proceed() {
        account_number = etAcct.getText().toString().trim();
        if (TextUtils.isEmpty(account_number) || account_number.length() < 10) {
            etAcct.setError("Invalide account");
        } else if (isNetworkAvailable()) {
            resolveBank();
        } else {
            Toast.makeText(this, "Check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void resolveBank() {
        dialog.setMessage("Resolving Bank...");
        dialog.show();

        AndroidNetworking.post("https://api.payant.ng/resolve-account")
                .addHeaders("Authorization", "Bearer 1a52017fd82262d6c689b3b3e001ed6243b2dc6f510ba7725abef462")
                .addBodyParameter("settlement_bank", settlement_bank)
                .addBodyParameter("account_number", account_number)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dialog.dismiss();
                        Log.d("succ1", response.toString());
                        try {
                            status = response.getString("status").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (status.equalsIgnoreCase("success")) {
                            try {
                                fullname = response.getJSONObject("data").getString("account_name");
                                acctNumber = response.getJSONObject("data").getString("account_number");
//                                bank = bankList.get(position)
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("succ1", "fullname: " + fullname);
                            Log.d("succ1", "acctNum: " + acctNumber);
                            Log.d("succ1", "bank: " + bank);

                            Intent intent = new Intent(BankVerifier.this, DisplayActivity.class);
                            intent.putExtra("name", fullname);
                            intent.putExtra("acct", acctNumber);
                            intent.putExtra("bank", bank);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        dialog.dismiss();
                        Log.d("err1", error.toString());
                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initSpinner() {
        spProvince = findViewById(R.id.spinner1);
//        spEmptyItem = findViewById(R.id.sp_empty_item);
        bankList = new ArrayList<>();

        bankList.add("ACCESS(DIAMOND) BANK");
        bankList.add("KEYSTONE BANK");
        bankList.add("UBA");
        bankList.add("GTB");


        spProvince.setItem(bankList);

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(BankVerifier.this, bankList.get(position), Toast.LENGTH_SHORT).show();
                Log.d("bank2", bankList.get(position));

                if (bankList.get(position).equalsIgnoreCase("ACCESS(DIAMOND) BANK")) {
                    settlement_bank = "000005";
                    bank = bankList.get(position);
                } else if (bankList.get(position).equalsIgnoreCase("KEYSTONE BANK")) {
                    settlement_bank = "000002";
                    bank = bankList.get(position);
                } else if (bankList.get(position).equalsIgnoreCase("UBA")) {
                    settlement_bank = "000004";
                    bank = bankList.get(position);
                } else if (bankList.get(position).equalsIgnoreCase("GTB")) {
                    settlement_bank = "000013";
                    bank = bankList.get(position);
                } else {
                    Toast.makeText(BankVerifier.this, "your bank does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
