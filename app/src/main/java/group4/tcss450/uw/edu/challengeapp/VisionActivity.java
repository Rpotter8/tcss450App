package group4.tcss450.uw.edu.challengeapp;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.io.BaseEncoding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import group4.tcss450.uw.edu.challengeapp.camera.CameraActivity;
import group4.tcss450.uw.edu.challengeapp.wikipedia.ResultListActivity;

/**
 * Activity to handle sending and display information to and from the Google Cloud Vision API.
 */
public class VisionActivity extends AppCompatActivity {

    private static final String WIKIPEDIA_QUERY = "tulip";

    private static final String CLOUD_VISION_API_KEY = "AIzaSyA8P-CF0GLshIbesA60rLH16grktY8rks4";


    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String TAG = VisionActivity.class.getSimpleName();

    private TextView mImageDetails;
    private String mPhotoPath;  // path to photo taken in Camera Activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_vision);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CameraActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        mImageDetails = (TextView) findViewById(R.id.image_details);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Integer.toString(resultCode), Integer.toString(resultCode));

        mPhotoPath = data.getStringExtra("result");
        Log.v("RETURNED PHOTO PATH", mPhotoPath);
        Bitmap bit = null;
        File f= new File(mPhotoPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bit = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Log.d("Call", "To Google");
            bit = scaleBitmapDown(bit, 1200);
            callGoogle(bit);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Calls the google vision API with the image.
     * @param bit the image being passed on to google.
     * @throws IOException
     */
    private void callGoogle(final Bitmap bit) throws IOException {
        mImageDetails.setText("Loading Image, Please Wait!");

        Log.d("Stat", "at call google");

        new AsyncTask<Object, Void, String>() {


            @Override
            protected String doInBackground(Object... objects) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {

                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                      //Must encode image in base64 for transmission
                        Image base64EncodedImage = new Image();

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bit.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();


                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);


                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});


                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);

                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return response.toString();

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {

                mImageDetails.setText(result);
                if(!(result.toLowerCase().contains("plant")
                        || result.toLowerCase().contains("Plant")
                        || result.toLowerCase().contains("leaf")
                        || result.toLowerCase().contains("petal"))) {
                    Toast.makeText(VisionActivity.this,"No plant was detected please try again.",
                            Toast.LENGTH_SHORT).show();
                } else {

                    // Start new activity with list of wikipedia results
                    Intent intent = new Intent(getBaseContext(), ResultListActivity.class);
                    intent.putExtra("query", handleJSON(result));
                    startActivity(intent);
                }

            }




        }.execute();


    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private String handleJSON(String result) {
        String parsed = null;

        try {
            JSONObject res = new JSONObject(result);
            JSONArray plants = loadJSONFromAsset();
            JSONArray resArr = res.getJSONArray("responses");
            res = resArr.optJSONObject(0);
            resArr = res.getJSONArray("labelAnnotations");
            for(int i = 0; i < resArr.length(); i ++) {
                JSONObject current = resArr.getJSONObject(i);
                String value = current.getString("description").toLowerCase();
                for(int j = 0; j < plants.length(); j ++) {
                    if(value.equals(plants.getJSONObject(j).getString("FIELD4").toLowerCase())
                            && !value.equals("flower")
                            && !value.equals("plant")
                            && !value.equals("petal")
                            && !value.equals("pink")
                            && !value.equals("blue")
                            && !value.equals("red")
                            && !value.equals("green")
                            && !value.equals("orange")
                            && !value.equals("brown")
                            && !value.equals("black")
                            && !value.equals("yellow")
                            && !value.equals("white")
                            && !value.equals("purple")
                            && !value.equals("violet")
                            && !value.equals("magenta")
                            && !value.equals("teal")
                            && !value.equals("cyan")
                            && !value.equals("red")) {
                        parsed = value;
                        break;
                    }
                }
                if(parsed != null) {
                    break;
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("PARSED", parsed);
        return parsed;
        }

    public JSONArray loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("convertcsv.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");



        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        JSONArray js = new JSONArray();
        try {
            js = new JSONArray(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return js;

    }








    /**
     * Sets up the signutres requied for for conecting to the API
     * @param pm
     * @param packageName
     * @return A string representation of the signature
     */
    private static String getSignature(@NonNull PackageManager pm, @NonNull String packageName) {
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            if (packageInfo == null
                    || packageInfo.signatures == null
                    || packageInfo.signatures.length == 0
                    || packageInfo.signatures[0] == null) {
                return null;
            }
            return signatureDigest(packageInfo.signatures[0]);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * Digests the signature to the correct length using SHA1 hash function
     * @param sig the signature to digest
     * @return the digested signature
     */
    private static String signatureDigest(Signature sig) {
        byte[] signature = sig.toByteArray();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(signature);
            return BaseEncoding.base16().lowerCase().encode(digest);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
