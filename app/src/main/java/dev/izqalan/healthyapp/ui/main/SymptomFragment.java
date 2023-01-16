package dev.izqalan.healthyapp.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import dev.izqalan.healthyapp.R;
import dev.izqalan.healthyapp.ui.bp.InsertBP;

public class SymptomFragment extends Fragment {

    Spinner symptom_1, symptom_2, symptom_3, symptom_4;
    Button checkSymptomsButton;


    public SymptomFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_symptom, container, false);
        symptom_1 = v.findViewById(R.id.symptom_1);
        symptom_2 = v.findViewById(R.id.symptom_2);
        symptom_3 = v.findViewById(R.id.symptom_3);
        symptom_4 = v.findViewById(R.id.symptom_4);
        checkSymptomsButton = v.findViewById(R.id.btn_check_symptom);

        String[] symptoms = new String[]{"Fever or chills", "Continuous cough", "Fatigue", "Loss of taste or smell",
        "Shortness of breath or difficulty breathing", "Sore throat", "Persistent cough (last more than 2 weeks)",
        "Cough with blood in sputum", "Fever for more than 2 weeks", "Pain in chest", "Weight loss",
        "Loss of appetite", "Sudden onset very high fever", "Severe headache", "Muscles and joints become painful",
        "Skin rashes", "Fatigue", "Vomiting"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, symptoms);
        symptom_1.setAdapter(adapter);
        symptom_2.setAdapter(adapter);
        symptom_3.setAdapter(adapter);
        symptom_4.setAdapter(adapter);

        checkSymptomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

                JSONObject data = new JSONObject();
                JSONArray symptomList = new JSONArray();

                symptomList.put(symptom_1.getSelectedItem().toString());
                symptomList.put(symptom_2.getSelectedItem().toString());
                symptomList.put(symptom_3.getSelectedItem().toString());
                symptomList.put(symptom_4.getSelectedItem().toString());

                try {
                    data.put("symptoms", symptomList);

                    mFunctions.getHttpsCallable("getDisease")
                            .call(data)
                            .continueWith(new Continuation<HttpsCallableResult, Object>() {
                                @Override
                                public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                                    if (task.isSuccessful()){
                                        Log.d("Disease", task.getResult().getData().toString());
                                        Map<String, Object> response = (Map<String, Object>) task.getResult().getData();

                                        builder.setMessage("Your symptoms matched with \n" + response.get("disease").toString());

                                        Log.d("Disease", response.get("disease").toString());
                                        Log.d("Disease", response.get("score").toString());
                                        Log.d("Disease", response.get("status").toString());
                                        Log.d("Disease", response.get("message").toString());

                                        builder.create().show();
                                    } else {
                                        Log.d("cloud functions", "call failed");
                                        task.getException().printStackTrace();
                                    }
                                    return task.getResult().getData();
                                }
                            });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return v;
    }
}