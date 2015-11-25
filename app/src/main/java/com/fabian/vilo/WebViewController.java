package com.fabian.vilo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Fabian on 21/11/15.
 */
public class WebViewController extends Fragment {

    private String title;
    private String oldTitle;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (oldTitle != null) {
            getActivity().setTitle(title);
        }

        View view = inflater.inflate(R.layout.webview, container, false);

        WebView webView = (WebView) view.findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().setTitle(oldTitle);

        if (oldTitle != null) {

            ((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
            ((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);

            FragmentManager manager = ((Tabbar) getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(this).commit();

        } else {
            FragmentManager manager = ((Login) getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(this).commit();
        }

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOldTitle(String oldTitle) {
        this.oldTitle = oldTitle;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
