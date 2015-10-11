package com.fabian.vilo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Referenced classes of package com.fslabs.demo:
//            BaseFragment, Main, PosttypePicker, Quickpost, 
//            Event, PhotoAlbum, Poll

public class Post extends Fragment
{

    public static int currentPostType = 100;
    public DialogInterface.OnCancelListener postTypePickerCancelListener;

    public Post()
    {
        postTypePickerCancelListener = new DialogInterface.OnCancelListener() {

            final Post this$0;

            public void onCancel(DialogInterface dialoginterface)
            {
                int i = ((PosttypePicker)dialoginterface).getDialogResult();
                if (i != Post.currentPostType)
                {
                    Post.currentPostType = i;
                    if (i == 100)
                    {
                        //setPostType(Quickpost.newInstance());
                        return;
                    }
                    if (i == 101)
                    {
                        //setPostType(Event.newInstance());
                        return;
                    }
                    if (i == 103)
                    {
                        //setPostType(PhotoAlbum.newInstance());
                        return;
                    }
                    if (i == 102)
                    {
                        //setPostType(Poll.newInstance());
                        return;
                    }
                }
            }

            
            {
                this$0 = Post.this;
                //super();
            }
        };
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        return null;
    }

    public void onDestroy()
    {
        super.onDestroy();
        if (getFragmentManager().getBackStackEntryCount() == 0)
        {
            //((Tabbar)getActivity()).showBottombar();
        }
    }

    public void setPostType(Fragment fragment)
    {
        FragmentTransaction fragmenttransaction = getFragmentManager().beginTransaction();
        fragmenttransaction.setCustomAnimations(0x7f040008, 0, 0, 0x7f040001);
        if (getFragmentManager().getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
        }
        fragmenttransaction.addToBackStack(null);
        fragmenttransaction.replace(0x7f090050, fragment, "post");
        fragmenttransaction.commit();
        //((Main)getActivity()).hideBottomBar();
    }

}
