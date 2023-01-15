package dev.izqalan.healthyapp.ui.bp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

import dev.izqalan.healthyapp.R;
import dev.izqalan.healthyapp.ui.profile.ProfileUpdate;

public class InsertBP extends AppCompatActivity {

    EditText et_sys,et_dia,et_pulse;
    Button save,history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_bp);

        et_sys = findViewById(R.id.et_sys);
        et_dia = findViewById(R.id.et_dia);
        et_pulse = findViewById(R.id.et_pulse);
        save = findViewById(R.id.save_bp_button);


        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, String> data = new HashMap<>();
        data.put("uid", currentUser.getUid());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> data = new HashMap<>();
                data.put("uid", currentUser.getUid());
                data.put("sys", et_sys.getText().toString());
                data.put("dia", et_dia.getText().toString());
                data.put("bp", et_pulse.getText().toString());

                mFunctions.getHttpsCallable("addUserBloodPressure")
                        .call(data)
                        .continueWith(new Continuation<HttpsCallableResult, Object>() {
                            @Override
                            public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InsertBP.this);

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