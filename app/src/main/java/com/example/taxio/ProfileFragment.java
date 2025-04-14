package com.example.taxio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FirebaseDatabase DB;
    FirebaseUser user;
    DatabaseReference reference;
    String userID;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profile,
                container, false);

        user= FirebaseAuth.getInstance().getCurrentUser();
        DB = FirebaseDatabase.getInstance();

        reference=DB.getReference("users");
        userID=user.getUid();



        final TextView profile_username=(TextView) rootView.findViewById(R.id.profile_username);

        final TextInputLayout profile_username2=(TextInputLayout) rootView.findViewById(R.id.profile_username2);
        final TextInputLayout profile_email=(TextInputLayout) rootView.findViewById(R.id.profile_email);
        final TextInputLayout profile_phoneno=(TextInputLayout) rootView.findViewById(R.id.profile_phoneno);
        final TextInputLayout profile_password=(TextInputLayout) rootView.findViewById(R.id.profile_password);



        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG",snapshot.getValue().toString()) ;

                UserHelperClass userProfile = snapshot.getValue(UserHelperClass.class);

                if (userProfile!=null){
                    String username = userProfile.username;
                    String email=userProfile.email;
                    String phoneno = userProfile.phoneno;
                    String password =userProfile.password;

                    profile_username.setText(username);
                    profile_username2.getEditText().setText(username);
                    profile_email.getEditText().setText(email);
                    profile_phoneno.getEditText().setText(phoneno);
                    profile_password.getEditText().setText(password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    public void update(View view){

    }
}