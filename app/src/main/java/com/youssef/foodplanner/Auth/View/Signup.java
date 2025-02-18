package com.youssef.foodplanner.Auth.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.youssef.foodplanner.R;

public class Signup extends Fragment {

    private EditText email, name, password, confirmPassword;
    private Button signupButton;
    private SignInButton googleSignUpButton;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 123;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.emailSignupField);
        name = view.findViewById(R.id.nameSignupField);
        password = view.findViewById(R.id.passwordSignupField);
        confirmPassword = view.findViewById(R.id.confirmPasswordSignupField);
        signupButton = view.findViewById(R.id.signup_button);
        googleSignUpButton = view.findViewById(R.id.google_sign_up_button);

        signupButton.setOnClickListener(v -> registerUser());
        googleSignUpButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void registerUser() {
        String userEmail = email.getText().toString().trim();
        String userName = name.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userConfirmPassword = confirmPassword.getText().toString().trim();

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
                    Toast.makeText(getContext(), "Signup successful", Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_signup_to_home);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account.getIdToken());
            }
        } catch (ApiException e) {
            Toast.makeText(getContext(), "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(getContext(), "Google sign-in successful", Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_signup_to_home);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}