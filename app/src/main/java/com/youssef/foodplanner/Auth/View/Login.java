package com.youssef.foodplanner.Auth.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.youssef.foodplanner.R;

public class Login extends Fragment {
  private EditText email, password;
  private Button signinButton;
  private TextView signupText;
  private FirebaseAuth auth;

  public Login() {
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
    return inflater.inflate(R.layout.fragment_login, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    email = view.findViewById(R.id.emailLoginField);
    password = view.findViewById(R.id.passwordLoginField);
    signinButton = view.findViewById(R.id.login_button);
    signupText = view.findViewById(R.id.sign_up_link);

    signinButton.setOnClickListener(v -> loginUser());
  }

  private void loginUser() {
    String userEmail = email.getText().toString().trim();
    String userPassword = password.getText().toString().trim();

    Log.d("Login", "Email: " + userEmail);
    Log.d("Login", "Password: " + userPassword);

    if (userEmail.isEmpty() || userPassword.isEmpty()) {
      Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
      return;
    }

    auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnSuccessListener(authResult -> {
              FirebaseUser user = auth.getCurrentUser();
              Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
              Log.d("Login", "User logged in: " + user.getEmail());
            })
            .addOnFailureListener(e -> {
              Toast.makeText(getContext(), "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
              Log.e("Login", "Error: " + e.getMessage());
            });
  }
}
