package dev.izqalan.healthyapp.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView tv_name, tv_age, tv_weight, tv_height;
    FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        tv_name = rootView.findViewById(R.id.tv_name);
        tv_age = rootView.findViewById(R.id.tv_age);
        tv_height = rootView.findViewById(R.id.tv_height);
        tv_weight = rootView.findViewById(R.id.tv_weight);

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
                            Log.d("user biodata", task.getResult().getData().toString());
                            Map<String, Object> biodata = (Map<String, Object>) task.getResult().getData();
                            tv_name.setText(biodata.get("name").toString());
                            tv_age.setText(biodata.get("age").toString());
                            tv_height.setText(biodata.get("height").toString());
                            tv_weight.setText(biodata.get("weight").toString());
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