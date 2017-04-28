package group4.tcss450.uw.edu.challengeapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private OnRegisterFragmentInteractionListener mListener;
    private EditText mUname;
    private EditText mPassword;
    private EditText mConfirmPassword;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        mUname = (EditText) v.findViewById(R.id.usernameText);
        mPassword = (EditText) v.findViewById(R.id.passwordText);
        mConfirmPassword = (EditText) v.findViewById(R.id.confirmPasswordText);
        Button b = (Button) v.findViewById(R.id.register_submit_button);
        b.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.register_submit_button:
                    if (mUname.getText().toString().equals("")) {
                        mUname.setError("username cannot be empty");
                    } else if (mPassword.getText().toString().equals("")) {
                        mPassword.setError("password cannot be empty");
                    } else if (mConfirmPassword.getText().toString().equals("")) {
                        mConfirmPassword.setError("must confirm password");
                    } else if (!mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                        mConfirmPassword.setError("passwords must match");
                    } else {
                        Log.d("ACTIVITY", "Register onClick");
                        List<String> data = new ArrayList<>();
                        data.add(0, mUname.getText().toString());
                        data.add(1, mPassword.getText().toString());
                        mListener.onRegisterFragmentInteraction(data);
                        break;
                    }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRegisterFragmentInteractionListener {

        void onRegisterFragmentInteraction(List<String> data);
    }
}
