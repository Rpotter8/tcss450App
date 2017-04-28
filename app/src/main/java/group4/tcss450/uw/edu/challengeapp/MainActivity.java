package group4.tcss450.uw.edu.challengeapp;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
                        FirstFragment.OnFragmentInteractionListener,
                        LogInFragment.OnLogInFragmentInteractionListener,
                        RegisterFragment.OnRegisterFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer, new FirstFragment())
                        .commit();
            } }
    }

    @Override
    public void onFragmentInteraction(String text) {
        Log.d("ACTIVITY", text);

        if (text == "login") {
            LogInFragment logInFragment = new LogInFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, logInFragment)
                    .addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        } else if (text == "register") {
            RegisterFragment registerFragment = new RegisterFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, registerFragment)
                    .addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onLogInFragmentInteraction(List<String> data) {

        DisplayFragment displayFragment = new DisplayFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.username_key), data.get(0));
        args.putSerializable(getString(R.string.password_key), data.get(1));
        displayFragment.setArguments(args);
        //displayFragment.updateContent(data.get(0), data.get(1));
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, displayFragment)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onRegisterFragmentInteraction(List<String> data) {

        DisplayFragment displayFragment = new DisplayFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.username_key), data.get(0));
        args.putSerializable(getString(R.string.password_key), data.get(1));
        displayFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, displayFragment)
                .addToBackStack(null);
        transaction.commit();
    }


}