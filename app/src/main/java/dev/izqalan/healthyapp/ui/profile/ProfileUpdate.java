package dev.izqalan.healthyapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

import dev.izqalan.healthyapp.R;
import dev.izqalan.healthyapp.ui.main.MainActivity;

public class ProfileUpdate extends AppCompatActivity {

    EditText et_name, et_age, et_weight, et_height;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);


        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        submitButton = findViewById(R.id.submit_button);

        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, String> data = new HashMap<>();
        data.put("uid", currentUser.getUid());

        mFunctions.getHttpsCallable("getUserBiodata")
                        .call(data)
                .continueWith(new Continuation<HttpsCallableResult, Object>() {
                    @Override
                    public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        if (task.isSuccessful()){
                            Map<String, Object> response = (Map<String, Object>) task.getResult().getData();

                            if (response.size() > 1){
                                et_name.setText(response.get("name").toString());
                                et_age.setText(response.get("age").toString());
                                et_height.setText(response.get("height").toString());
                                et_weight.setText(response.get("weight").toString());
                            }
                        } else {
                            Log.d("err biodata", task.getResult().getData().toString());
                        }
                        return null;
                    }
                });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> data = new HashMap<>();
                data.put("uid", currentUser.getUid());
                data.put("name", et_name.getText().toString());
                data.put("age", et_age.getText().toString());
                data.put("height", et_height.getText().toString());
                data.put("weight", et_weight.getText().toString());

                mFunctions.getHttpsCallable("updateUserBiodata")
                        .call(data)
                        .continueWith(new Continuation<HttpsCallableResult, Object>() {
                            @Override
                            public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUpdate.this);

                                if (task.isSuccessful()){
                                    Map<String, Object> response = (Map<String, Object>) task.getResult().getData();

                                    builder.setMessage(response.get("message").toString());
                                    builder.create();
                                    builder.show();

                                } else {
                                    builder.setMessage(task.getException().getMessage());
                                    builder.create();
                                    builder.show();
                                }
                                return task.getResult().getData();
                            }
                        });
            }
        });

    }

}