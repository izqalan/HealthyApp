package dev.izqalan.healthyapp.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;

import dev.izqalan.healthyapp.R;
import dev.izqalan.healthyapp.api.CloudFunctions;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_message);

        CloudFunctions cloudFunctions = new CloudFunctions();
        cloudFunctions.helloWorld().addOnCompleteListener(new OnCompleteListener<Map<Object, Object>>() {
            @Override
            public void onComplete(@NonNull Task<Map<Object, Object>> task) {
                if (task.isSuccessful()){
                    Map<Object, Object> result = task.getResult();
                    Log.d("MainActivity", result.toString());
                    textView.setText(result.get("message").toString());
                } else {
                    task.getException().printStackTrace();
                }
            }
        });
    }
}