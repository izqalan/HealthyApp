package dev.izqalan.healthyapp.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.izqalan.healthyapp.R;
import dev.izqalan.healthyapp.adapter.BmiAdapter;
import dev.izqalan.healthyapp.models.BmiModel;

public class BmiFragment extends Fragment {

    RecyclerView recyclerView;
    BmiAdapter adapter;
    ArrayList<BmiModel> historyList;



    public BmiFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_bmi, container, false);

        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = rootView.findViewById(R.id.mRecyclerView);
        historyList = new ArrayList<>();
        adapter = new BmiAdapter(rootView.getContext(), historyList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        HashMap<String, String> data = new HashMap<>();
        data.put("uid", currentUser.getUid());
        mFunctions.getHttpsCallable("getUserBmiHistory")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, Object>() {
                    @Override
                    public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        if (task.isSuccessful()){
                            Log.d("user bmi history", task.getResult().getData().toString());
                            ArrayList<Map<String, Object>> history = (ArrayList<Map<String, Object>>) task.getResult().getData();
                            for (Map<String, Object> item : history){
                                String w = item.get("weight").toString();
                                String h = item.get("height").toString();
                                String c = new DecimalFormat("#.##").format(item.get("calculated")).toString();
                                BmiModel bmiModel = new BmiModel(h, w, c);
                                historyList.add(bmiModel);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("cloud functions", "call failed");
                            task.getException().printStackTrace();
                        }
                        return task.getResult().getData();
                    }
                });

        return rootView;
    }
}