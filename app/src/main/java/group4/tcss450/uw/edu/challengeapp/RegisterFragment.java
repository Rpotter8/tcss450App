package group4.tcss450.uw.edu.challengeapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * A fragment for registering a user
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" + "~rjp24/register";


    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" + "~rjp24/register";

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
        AsyncTask<String, Void, String> task = null;
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
<<<<<<< HEAD
                        task = new RegisterFragment.RegisterWebServiceTask();
                        //String message = "'" + mUname.getText().toString() + "'";

                        //mPwd = "[{\"pwd:\"" + mPassword.getText().toString() + "\"}]";
                        //Log.d("ON_CLICK", mPwd);
                        String username = mUname.getText().toString();
                        String password1 = mPassword.getText().toString();
                        String password2 = mConfirmPassword.getText().toString();
                        if(password1.equals(password2)) {
                            task.execute(PARTIAL_URL, username, password1, password2);
                        } else {
                            Toast.makeText(getActivity(), "Passwords do not match!",
                                    Toast.LENGTH_LONG).show();
                        }
=======
                        Log.d("ACTIVITY", "Register onClick");

                        new RegisterWebServiceTask().execute(PARTIAL_URL,
                                mUname.getText().toString(),
                                mPassword.getText().toString(),
                                mConfirmPassword.getText().toString());
>>>>>>> 28a410c8c3b34796d3315fd400af579def26b156


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


    private class RegisterWebServiceTask extends AsyncTask<String, Void, String> {
        //private final String SERVICE = "_post.php";
        @Override
        protected String doInBackground(String... strings) {
            Log.d("doInBackground", "hello");
            if (strings.length != 4) {
                throw new IllegalArgumentException("Four String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                //URL urlObject = new URL(url + SERVICE);
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("user", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("password1", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8")
                        + "&" + URLEncoder.encode("password2", "UTF-8")
<<<<<<< HEAD
                        + "=" + URLEncoder.encode(strings[3], "UTF-8");
=======
                        + "=" + URLEncoder.encode(strings[3], "UTF-8");;
>>>>>>> 28a410c8c3b34796d3315fd400af579def26b156
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("POST_EXECUTE", result);
<<<<<<< HEAD
            if(result.equals("true")){
                mListener.onRegisterFragmentInteraction(null);
            } else {
                Toast.makeText(getActivity(), "Invalid Username",
                        Toast.LENGTH_LONG).show();
=======
            if(result.equals("true")) {
                mListener.onRegisterFragmentInteraction(null);
            } else {
                Toast.makeText(getActivity(),"Invalid Username or Password",
                        Toast.LENGTH_SHORT).show();
>>>>>>> 28a410c8c3b34796d3315fd400af579def26b156
            }




        }
    }
}
