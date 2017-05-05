package group4.tcss450.uw.edu.LeafLets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment {


    public DisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    public void updateContent(String username, String password) {
        String welcomeText = "Welcome, " + username + "!";
        TextView tvUsername = (TextView) getActivity().findViewById(R.id.usernameView);
        tvUsername.setText(welcomeText);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            String username = getArguments().getString("usernameKey");
            String password = getArguments().getString("passwordKey");
            updateContent(username, password);
        }
    }

}
