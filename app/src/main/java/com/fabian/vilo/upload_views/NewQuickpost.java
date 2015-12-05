package com.fabian.vilo.upload_views;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fabian.vilo.R;
import com.fabian.vilo.Tabbar;
import com.fabian.vilo.api.ViloApiAdapter;
import com.fabian.vilo.api.ViloApiEndpointInterface;
import com.fabian.vilo.models.Card;
import com.fabian.vilo.custom_methods.GPSTracker;
import com.fabian.vilo.custom_methods.Util;
import com.fabian.vilo.models.CDModels.CDUser;
import com.fabian.vilo.models.CDModels.ModelManager;
import com.fabian.vilo.models.QuickUpload;
import com.fabian.vilo.models.ViloUploadResponse;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Fabian on 03/11/15.
 */
public class NewQuickpost extends Fragment {

    private String title;
    private Uri outputFileUri;
    private LinearLayout choosePhoto;
    private ImageView uploadImage;
    private ImageButton btnDelete;
    private TextView chooseImage;
    private static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 232;

    private EditText uploadTitle;
    private EditText uploadText;
    private Boolean photoSelected = false;

    private ModelManager modelManager = new ModelManager();
    private GPSTracker gps;
    private double current_lattitude;
    private double current_longitude;
    SharedPreferences sharedpreferences;
    Uri selectedImageUri;

    String username;
    String date;
    int userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.quickpost, container,
                false);

        FloatingActionButton uploadBtn = (FloatingActionButton) view.findViewById(R.id.makePost);
        choosePhoto = (LinearLayout) view.findViewById(R.id.containerPhoto);
        uploadImage = (ImageView) view.findViewById(R.id.imageThumb);
        chooseImage = (TextView) view.findViewById(R.id.txtImagePicker);
        btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
        uploadTitle = (EditText) view.findViewById(R.id.uploadTitle);
        uploadText = (EditText) view.findViewById(R.id.uploadText);

        sharedpreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        gps = new GPSTracker(getContext());

        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageIntent();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bla", "upload button pressed!");
                if (gps.canGetLocation())

                {

                    if (uploadTitle.getText().toString().trim().length() > 0 && uploadText.getText().toString().trim().length() > 0) {

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        current_lattitude = gps.getLatitude();
                        current_longitude = gps.getLongitude();

                        editor.putString("lat", "" + current_lattitude);
                        editor.putString("lng", "" + current_longitude);
                        editor.commit();
                        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getContext());

                        ViloApiEndpointInterface apiService = viloAdapter.mApi;

                        final QuickUpload quickUpload = new QuickUpload();
                        quickUpload.withPhoto = (photoSelected == true) ? 1 : 0;
                        quickUpload.title = uploadTitle.getText().toString();
                        quickUpload.text = uploadText.getText().toString();
                        quickUpload.lat = current_lattitude;
                        quickUpload.lng = current_longitude;

                        Realm realm = Realm.getInstance(getContext());

                        // Build the query looking at all users:
                        RealmQuery<CDUser> query = realm.where(CDUser.class);

                        // Execute the query:
                        RealmResults<CDUser> result = query.findAll();

                        username = result.first().getFirst_name();
                        userid = result.first().getUserid();
                        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                        if (photoSelected == true) {

                            //File file = new File(selectedImageUri);
                            File file = new File(new Util().getRealPathFromURI(getContext(), selectedImageUri));

                            File reducedFile = new Util().saveBitmapToFile(file, getContext());

                            RequestBody requestBody =
                                    RequestBody.create(MediaType.parse("multipart/form-data"), reducedFile);

                            Call<ViloUploadResponse> call = apiService.uploadQuickWithImage(quickUpload.withPhoto, quickUpload.title, quickUpload.text, quickUpload.lat, quickUpload.lng, requestBody);

                            call.enqueue(new Callback<ViloUploadResponse>() {
                                @Override
                                public void onResponse(Response<ViloUploadResponse> response, Retrofit retrofit) {
                                    Log.d("upload", "posted!");
                                    Log.d("upload", "msg: " + response.body());
                                    Card newPost = new Card(quickUpload.title, quickUpload.text, response.body().result.id, 0, 0, 0, 0, "", null, username, response.body().result.attachment, date, userid, null, (float) quickUpload.lat, (float) quickUpload.lng, 5000, null, 0, date, 0, 0, null);
                                    if (modelManager.saveNewQuickPost(newPost, getContext())) {
                                        getFragmentManager().popBackStackImmediate();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Log.d("upload", "error: " + t.getMessage());
                                }
                            });
                        } else {
                            Call<ViloUploadResponse> call = apiService.uploadQuickWithoutImage(quickUpload);

                            call.enqueue(new Callback<ViloUploadResponse>() {
                                @Override
                                public void onResponse(Response<ViloUploadResponse> response, Retrofit retrofit) {
                                    Log.d("upload", "posted without image!");
                                    Log.d("upload", "msg: " + response.body());
                                    Card newPost = new Card(quickUpload.title, quickUpload.text, response.body().result.id, 0, 0, 0, 0, "", null, username, "", date, userid, null, (float) quickUpload.lat, (float) quickUpload.lng, 5000, null, 0, date, 0, 0, null);
                                    if (modelManager.saveNewQuickPost(newPost, getContext())) {
                                        getFragmentManager().popBackStackImmediate();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Log.d("upload", "error: " + t.getMessage());
                                }
                            });
                        }
                    } else {
                        // alert dass alles ausgefüllt sein muss
                        Log.d("upload", "Fields not filled correctly!");
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoSelected = false;
                chooseImage.setVisibility(View.VISIBLE);
                uploadImage.setVisibility(View.GONE);
                uploadImage.setImageURI(null);
                btnDelete.setVisibility(View.GONE);
                choosePhoto.setClickable(true);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //getActivity().setTitle(title);

        //((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
        //((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);

        ((Tabbar) getActivity()).getSupportActionBar().show();
        ((Tabbar) getActivity()).setIsVisible(true);

        FragmentManager manager = ((Tabbar)getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(this).commit();

    }

    private void openImageIntent() {

// Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "file";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                Log.d("upload", "image uri: "+selectedImageUri);
                Log.d("upload", "image uri: "+selectedImageUri.getPath());
                Log.d("upload", "image uri: "+new Util().getRealPathFromURI(getContext(), selectedImageUri));
                photoSelected = true;
                chooseImage.setVisibility(View.GONE);
                uploadImage.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(selectedImageUri)
                        .centerCrop()
                        //.fit()
                        .into(uploadImage);
                //uploadImage.setImageURI(null);
                //uploadImage.setImageURI(selectedImageUri);
                btnDelete.setVisibility(View.VISIBLE);
                choosePhoto.setClickable(false);
            }
        }
    }

}
