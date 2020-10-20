package com.example.udpchat;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private EditText nameSource;
    private EditText sourcePort;
    private EditText destinationIP;
    private EditText destinationPort;
    private TextView getIpAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameSource = findViewById(R.id.name_source);
        sourcePort = findViewById(R.id.sourcePort);
        destinationIP = findViewById(R.id.ip_destination);
        destinationPort = findViewById(R.id.port_destination);
        btn = findViewById(R.id.sendBtn);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        getIpAddress = findViewById(R.id.wifiManagerTxtView);
        getIpAddress.setText("Your IP: " + ipAddress);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("name", nameSource.getText().toString());
                intent.putExtra("sourcePort", sourcePort.getText().toString());
                intent.putExtra("destinationIP", destinationIP.getText().toString());
                intent.putExtra("destinationPort", destinationPort.getText().toString());
//                intent.putExtra("destinationPort", "8998");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}