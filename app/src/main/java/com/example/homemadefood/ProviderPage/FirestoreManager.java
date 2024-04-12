package com.example.homemadefood.ProviderPage;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreManager {

    private static final String TAG = "FirestoreManager";

    private final FirebaseFirestore db;

    public FirestoreManager() {
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
    }

    public void getUserData(UserDataListener listener) {
        // Assuming you have a user ID, replace "userId" with the actual user ID
        String userId = "userId";

        // Get a reference to the user's document in Firestore
        DocumentReference userRef = db.collection("users").document(userId);

        // Retrieve the user's document from Firestore
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Pass the document snapshot to the listener
                listener.onUserDataReceived(documentSnapshot);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur during data retrieval
                Log.e(TAG, "Error fetching user data: " + e.getMessage());
                listener.onFailure(e.getMessage());
            }
        });
    }

    public interface UserDataListener {
        void onUserDataReceived(DocumentSnapshot userDataSnapshot);
        void onFailure(String errorMessage);
    }
}
