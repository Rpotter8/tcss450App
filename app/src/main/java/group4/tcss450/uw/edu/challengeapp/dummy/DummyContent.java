package group4.tcss450.uw.edu.challengeapp.dummy;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group4.tcss450.uw.edu.challengeapp.R;
import group4.tcss450.uw.edu.challengeapp.camera.AlbumStorageDirFactory;
import group4.tcss450.uw.edu.challengeapp.camera.BaseAlbumDirFactory;
import group4.tcss450.uw.edu.challengeapp.camera.FroyoAlbumDirFactory;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<String> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, String> ITEM_MAP = new HashMap<String, String>();

    private static final int COUNT = 25;

    private static AlbumStorageDirFactory mAlbumStorageDirFactory;

     static {

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
             mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
         } else {
             mAlbumStorageDirFactory = new BaseAlbumDirFactory();
         }
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem();
        }
    }

    private static File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("CameraSample");

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {

        }


        return storageDir;
    }

    private static void addItem() {


        File f = getAlbumDir();

        int i = 0;

        for (File file : f.listFiles()) {
            Log.d("filename", file.getName());
            if (file.isFile())
                ITEMS.add("/storage/emulated/0/Pictures/CameraSample/" + file.getName());
                ITEM_MAP.put(Integer.toString(i), "/storage/emulated/0/Pictures/CameraSample/" + file.getName());
                i++;
        }



    }


}
