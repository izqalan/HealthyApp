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

import dev.izqalan.healthyapp.adapter.BpAdapter;
import dev.izqalan.healthyapp.models.BPModel;



public class BPFragment extends Fragment {

    RecyclerView recyclerView;
    BpAdapter adapter;
    ArrayList<BPModel> historyList;


    public BPFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_b_p, container, false);

        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = rootView.findViewById(R.id.bpRecyclerView);
        historyList = new ArrayList<>();
        adapter = new BpAdapter(rootView.getContext(), historyList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        HashMap<String, String> data = new HashMap<>();
        data.put("uid", currentUser.getUid());
        mFunctions.getHttpsCallable("getUserBloodPressureHistory")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, Object>() {
                    @Override
                    public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        if (task.isSuccessful()){
                            Log.d("user bp history", task.getResult().getData().toString());
                            ArrayList<Map<String, Object>> history = (ArrayList<Map<String, Object>>) task.getResult().getData();
                            for (Map<String, Object> item : history){
                                String d = item.get("sys").toString();
                                String s = item.get("dia").toString();
                                String p = item.get("bp").toString();
                                BPModel bpModel = new BPModel(s,d,p);
                                historyList.add(bpModel);
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