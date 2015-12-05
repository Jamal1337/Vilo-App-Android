package com.fabian.vilo.detail_views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fabian.vilo.R;
import com.fabian.vilo.Tabbar;

/**
 * Created by Fabian on 06/11/15.
 */
public class ImageFullscreen extends Fragment {

    private String imgURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_image_fullscreen, container,
                false);

        ImageView imageView = (ImageView) view.findViewById(R.id.imgDisplay);

        // TODO: manche bilder sind zu groß, gibts memory error
        String replacedString = imgURL.replace("-thumbnail.", "-original.");

        Log.d("image", "image url: "+replacedString);

        Glide.with(getContext())
                .load(replacedString)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                //.centerCrop()
                //.fit()
                .into(imageView);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //getActivity().setTitle(title);

        //((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
        //((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);

        ((Tabbar) getActivity()).getSupportActionBar().show();

        FragmentManager manager = ((Tabbar)getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(this).commit();

    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

}
