package dev.izqalan.healthyapp.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

import dev.izqalan.healthyapp.R;
import dev.izqalan.healthyapp.api.CloudFunctions;
import dev.izqalan.healthyapp.databinding.ActivityMainBinding;
import dev.izqalan.healthyapp.ui.login.LoginActivity;
import dev.izqalan.healthyapp.ui.profile.ProfileUpdate;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        BottomNavigationView bottomNav = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_home:
                    Log.d("nav", "pressed home");
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.menu_bmi:
                    Log.d("nav", "pressed bmi");
                    replaceFragment(new BmiFragment());
                    break;
                case R.id.menu_bp:
                    Log.d("nav", "pressed bp");
                    replaceFragment(new BPFragment());
                    break;
                case R.id.menu_symptom:
                    Log.d("nav", "pressed symptom");
                    replaceFragment(new SymptomFragment());
                    break;
            }
            return true;
        });

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
                            Map<String, Object> biodata = (Map<String, Object>) task.getResult().getData();
                            if (biodata.size() < 4){
                                Intent intent = new Intent(getApplicationContext(), ProfileUpdate.class);
                                startActivity(intent);
                            }
                        } else {
                            Log.d("cloud functions", "call failed");
                            task.getException().printStackTrace();
                        }
                        return task.getResult().getData();
                    }
                });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_logout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_profile:
                intent = new Intent(this, ProfileUpdate.class);
                startActivity(intent);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}