package edu.orangecoastcollege.cs273.fjuarez6.petprotector;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class PetListActivity extends AppCompatActivity {

    private ImageView petImageView;
    private static final int REQUEST_CODE = 100;

    // This member variable
    private Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        // Hook up the imageView to the layout:
        petImageView = (ImageView) findViewById(R.id.imageView);

        imageURI = getUriToResource(this, R.drawable.none);

        // set the imageURI of the imageView in code
        petImageView.setImageURI(imageURI);

    }

    public void selectPetImage(View view)
    {
        // List of all the permissions
        ArrayList<String> perList = new ArrayList<>();

        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (cameraPermission != PackageManager.PERMISSION_GRANTED)
            perList.add(Manifest.permission.CAMERA);

        int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            perList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            perList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (perList.size() > 0) {
            String[] perms = new String[perList.size()];

            ActivityCompat.requestPermissions(this, perList.toArray(perms), REQUEST_CODE);
        }

        if (cameraPermission == PackageManager.PERMISSION_GRANTED
                && readExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED)
        {
                // Use an intent to launch the gallery and gat pictures
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_CODE);
        }
        else
        {
            Toast.makeText(this, "Pet Protector requires camera and External storage permission",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Code to handle when the user closes the image gallery (by selecting an image
        // or pressing the back button)

         // The intent data is the URI selected from image gallery
        // Decide if the user selected an image:

        if (data != null && requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Set the ImageURI to the data:
            imageURI = data.getData();
            petImageView.setImageURI(imageURI);
        }
    }

    /**
     * Get uri to any resource type within an Android Studio Project. Method is public static
     * to allow other classes to use it as a helper function
     *
     * @param context The current context
     * @param resId The resource identifier of the drawable
     * @return Uri to the resource by given id
     * @throws Resources.NotFoundException if the given resource id does not exist.
     */
    public  static Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {
        /** Return a resource instance for your application's package. */
        Resources res = context.getResources();
        /** Return URI */
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
    }
}
