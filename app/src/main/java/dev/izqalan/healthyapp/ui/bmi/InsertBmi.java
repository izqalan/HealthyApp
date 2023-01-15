package dev.izqalan.healthyapp.ui.bmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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


public class InsertBmi extends AppCompatActivity {
    EditText et_weight,et_height;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_bmi);

        et_weight = findViewById(R.id.et_weight_bmi);
        et_height = findViewById(R.id.et_height_bmi);
        save = findViewById(R.id.save_bmi_button);


        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, String> data = new HashMap<>();
        data.put("uid", currentUser.getUid());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> data = new HashMap<>();
                data.put("uid", currentUser.getUid());
                data.put("weight", et_weight.getText().toString());
                data.put("height", et_height.getText().toString());

                mFunctions.getHttpsCallable("addUserBmi ")
                        .call(data)
                        .continueWith(new Continuation<HttpsCallableResult, Object>() {
                            @Override
                            public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InsertBmi.this);

                                if (task.isSuccessful()){
                                    Map<String, Object> response = (Map<String, Object>) task.getResult().getData();

                                    builder.setMessage(response.get("bmi").toString());
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