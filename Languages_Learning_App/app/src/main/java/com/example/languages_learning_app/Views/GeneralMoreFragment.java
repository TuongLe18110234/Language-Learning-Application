package com.example.languages_learning_app.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.R;
import com.example.languages_learning_app.Views.Trainee.TraineeDetailProfileActivity;
import com.example.languages_learning_app.Views.Trainee.TraineeRankActivity;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMoreFragment extends Fragment {
    TextView edtFullName, edtEmail;
    CircleImageView civAvatar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_more, container, false);

        CardView cvManageProfile = (CardView) view.findViewById(R.id.cvManageProfile);
        CardView cvChangePassword = (CardView) view.findViewById(R.id.cvChangePassword);
        CardView cvRank = (CardView) view.findViewById(R.id.cvRank);
        Button btLogOut = (Button) view.findViewById(R.id.btUpdate);

        edtFullName = view.findViewById(R.id.tvFullName);
        edtEmail = view.findViewById(R.id.tvEmail);

        edtFullName.setText(Common.user.getFullName());
        edtEmail.setText(Common.user.getEmail());

        civAvatar = view.findViewById(R.id.imgProfile);

        cvManageProfile.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), GeneralProfileActivity.class));
        });

        cvChangePassword.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), GeneralChangePasswordActivity.class));
        });

        cvRank.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), TraineeRankActivity.class));
        });

        btLogOut.setOnClickListener((View v) -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this.getContext(), GeneralLoginActivity.class));
            this.getActivity().finish();
        });

        civAvatar.setOnClickListener((View v) -> {
            if(Common.role.equals(Common.RoleTrainee)) {
                startActivity(new Intent(getActivity(), TraineeDetailProfileActivity.class));
            }
        });

        if(!Common.role.equals(Common.RoleAdmin)){
            int imageLanguage = Common.getFlagLanguage(Common.language.getName());
            civAvatar.setImageResource(imageLanguage);
        } else {
            civAvatar.setImageResource(R.drawable.bg_logo);
        }

        if(Common.role.equals(Common.RoleAdmin)){
            cvRank.setVisibility(View.GONE);
        }

        return view;
    }
}
