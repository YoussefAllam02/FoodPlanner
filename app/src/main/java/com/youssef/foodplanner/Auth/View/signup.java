package com.youssef.foodplanner.Auth.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.youssef.foodplanner.R;

class Signup extends Fragment {
    private EditText email, name, password, confirmpassword;
    private Button signupButton;
    private FirebaseAuth auth;

    public Signup() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.emailSignupField);
        name = view.findViewById(R.id.nameSignupField);
        password = view.findViewById(R.id.passwordSignupField);
        confirmpassword = view.findViewById(R.id.confirmPasswordSignupField);
        signupButton = view.findViewById(R.id.signup_button);

        signupButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String userEmail = email.getText().toString().trim();
        String userName = name.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userConfirmPassword = confirmpassword.getText().toString().trim();

        Log.d("Signup", "Email: " + userEmail);
        Log.d("Signup", "Name: " + userName);
        Log.d("Signup", "Password: " + userPassword);
        Log.d("Signup", "Confirm Password: " + userConfirmPassword);

        if (userEmail.isEmpty() || userName.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userPassword.equals(userConfirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = auth.getCurrentUser();
                    Toast.makeText(getContext(), "Signup successful", Toast.LENGTH_SHORT).show();
                    Log.d("Signup", "User created: " + user.getEmail());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Signup", "Error: " + e.getMessage());
                });
    }
    public  void logout(){
        auth.signOut();
    }
}
