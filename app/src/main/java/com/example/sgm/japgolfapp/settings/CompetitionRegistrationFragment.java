package com.example.sgm.japgolfapp.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.Competition.CreateClosedCompetitionFragment;
import com.example.sgm.japgolfapp.Competition.CreateClosedCompetitionGroupFragment;
import com.example.sgm.japgolfapp.Competition.JoinClosedCompetitionFragment;
import com.example.sgm.japgolfapp.Competition.ViewClosedCompetitionGroupsFragment;
import com.example.sgm.japgolfapp.Competition.ViewClosedCompetitionsFragment;
import com.example.sgm.japgolfapp.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompetitionRegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompetitionRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompetitionRegistrationFragment extends BaseFragment {
    View view_container;
    boolean shown = false;

    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.fragment_close_competition, rl, false);
        rl.addView(item);

        Button createCompetitionB = (Button)view.findViewById(R.id.createCompetitionB);
        createCompetitionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CreateClosedCompetitionFragment());
            }
        });

        ImageButton createCompetitionB2 = (ImageButton)view.findViewById(R.id.imageSettingButton);
        createCompetitionB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CreateClosedCompetitionFragment());
            }
        });

        Button viewCompetitionB = (Button)view.findViewById(R.id.viewCompetitionB);
        viewCompetitionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ViewClosedCompetitionsFragment());
            }
        });

        ImageButton viewCompetitionB2 = (ImageButton)view.findViewById(R.id.imageButton2);
        viewCompetitionB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ViewClosedCompetitionsFragment());
            }
        });

        Button joinCompetitionB = (Button)view.findViewById(R.id.joinCompetitionB);
        joinCompetitionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new JoinClosedCompetitionFragment());
            }
        });

        ImageButton joinCompetitionB2 = (ImageButton)view.findViewById(R.id.imageButton3);
        joinCompetitionB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new JoinClosedCompetitionFragment());
            }
        });

        Button createGroupB = (Button)view.findViewById(R.id.createGroupB);
        createGroupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CreateClosedCompetitionGroupFragment());
            }
        });

        ImageButton createGroupB2 = (ImageButton)view.findViewById(R.id.imageButton4);
        createGroupB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CreateClosedCompetitionGroupFragment());
            }
        });

        Button viewGroupsB = (Button)view.findViewById(R.id.viewGroupsB);
        viewGroupsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TEMP_FILE_NAME = "competition_number.txt";
                File tempFile;
                File cDir = getActivity().getCacheDir();
                tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
                FileWriter writer=null;
                try {
                    writer = new FileWriter(tempFile);
                    writer.write("");
                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                showFragmentAndAddToBackStack(new ViewClosedCompetitionGroupsFragment());
            }
        });

        ImageButton viewGroupsB2 = (ImageButton)view.findViewById(R.id.imageButtson4);
        viewGroupsB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TEMP_FILE_NAME = "competition_number.txt";
                File tempFile;
                File cDir = getActivity().getCacheDir();
                tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
                FileWriter writer=null;
                try {
                    writer = new FileWriter(tempFile);
                    writer.write("");
                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                showFragmentAndAddToBackStack(new ViewClosedCompetitionGroupsFragment());
            }
        });
    }
}
