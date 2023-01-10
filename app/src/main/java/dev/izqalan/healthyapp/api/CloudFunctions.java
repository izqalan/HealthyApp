package dev.izqalan.healthyapp.api;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.Map;

public class CloudFunctions {
    private FirebaseFunctions mFunctions;

    public Task<Map<Object, Object>> helloWorld() {
        mFunctions = FirebaseFunctions.getInstance();
        return mFunctions.getHttpsCallable("helloWorld")
                .call()
                .continueWith(new Continuation<HttpsCallableResult, Map<Object, Object>>() {
                    @Override
                    public Map<Object, Object> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return (Map<Object, Object>) task.getResult().getData();
                    }
                });
    }
}
