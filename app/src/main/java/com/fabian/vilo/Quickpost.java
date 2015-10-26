// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.fabian.vilo;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.fslabs.demo:
//            Post, Util, PosttypePicker, ImagePicker

public class Quickpost extends Activity
{

    final int IMAGE_INTENT = 1;
    final int REQUEST_TAKE_PHOTO = 2;
    ImageButton btnDelete;
    View btnImagePicker;
    DialogInterface.OnCancelListener imagePickerCancelListener;
    String mCurrentPhotoPath;
    ImageView thumb;
    TextView txtImagePicker;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quickpost);
        
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    public Quickpost()
    {
        /*imagePickerCancelListener = new DialogInterface.OnCancelListener() {

            final Quickpost this$0;

            public void onCancel(DialogInterface dialoginterface)
            {
                int i = ((ImagePicker)dialoginterface).getDialogResult();
                if (i == 1)
                {
                    dispatchTakePictureIntent();
                } else
                if (i == 2)
                {
                    dispatchGalleryIntent();
                    return;
                }
            }

            
            {
                this$0 = Quickpost.this;
                super();
            }
        };*/
    }

    private File createImageFile()
        throws IOException
    {
        Object obj = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
        obj = File.createTempFile((new StringBuilder()).append("JPEG_").append(((String) (obj))).append("_").toString(), ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        mCurrentPhotoPath = ((File) (obj)).getAbsolutePath();
        return ((File) (obj));
    }

    private void dispatchGalleryIntent()
    {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void dispatchTakePictureIntent()
    {
        /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(getActivity().getPackageManager()) == null) goto _L2; else goto _L1
_L1:
        File file = null;
        File file1 = createImageFile();
        file = file1;
_L4:
        if (file != null)
        {
            intent.putExtra("output", Uri.fromFile(file));
            startActivityForResult(intent, 2);
        }
_L2:
        return;
        IOException ioexception;
        ioexception;
        if (true) goto _L4; else goto _L3
_L3:*/
    }

    private void galleryAddPic()
    {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(mCurrentPhotoPath)));
        //getActivity().sendBroadcast(intent);
    }

    private static int getPowerOfTwoForSampleRatio(double d)
    {
        int j = Integer.highestOneBit((int)Math.floor(d));
        int i = j;
        if (j == 0)
        {
            i = 1;
        }
        return i;
    }

    /*private int getThumbHeight()
    {
        return (int)Util.convertDpToPixel(100F, getActivity());
    }

    private int getThumbWidth()
    {
        return (int)Util.convertDpToPixel(200F, getActivity());
    }*/

    public static Quickpost newInstance()
    {
        Quickpost quickpost = new Quickpost();
        //quickpost.setArguments(new Bundle());
        return quickpost;
    }

    private void setThumbnailFromCamera()
    {
        /*int i = getThumbWidth();
        int j = getThumbHeight();
        Object obj = new BitmapFactory.Options();
        obj.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, ((BitmapFactory.Options) (obj)));
        int k = ((BitmapFactory.Options) (obj)).outWidth;
        int l = ((BitmapFactory.Options) (obj)).outHeight;
        i = Math.min(k / i, l / j);
        obj.inJustDecodeBounds = false;
        obj.inSampleSize = i;
        obj.inPurgeable = true;
        obj = BitmapFactory.decodeFile(mCurrentPhotoPath, ((BitmapFactory.Options) (obj)));
        thumb.setImageBitmap(null);
        thumb.setImageBitmap(((android.graphics.Bitmap) (obj)));
        txtImagePicker.setVisibility(8);
        thumb.setVisibility(0);
        btnDelete.setVisibility(0);*/
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        /*super.onActivityResult(i, j, intent);
        if (i == 2 && j == -1 && intent == null)
        {
            galleryAddPic();
            setThumbnailFromCamera();
        }
        if (i != 1 || j != -1 || intent.getData() == null)
        {
            break MISSING_BLOCK_LABEL_54;
        }
        setThumbnailFromGallery(intent.getData());
        return;
        intent;
        intent.printStackTrace();
        return;*/
    }

    /*public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        /*layoutinflater = layoutinflater.inflate(0x7f03001c, viewgroup, false);
        setupActionBar(0x7f030002);
        ((TextView)getViewFromActionBar(0x7f090017)).setOnClickListener(new View.OnClickListener() {

            final Quickpost this$0;

            public void onClick(View view1)
            {
                getFragmentManager().popBackStack();
            }

            
            {
                this$0 = Quickpost.this;
                super();
            }
        });
        ((TextView)getViewFromActionBar(0x7f090016)).setOnClickListener(new View.OnClickListener() {

            final Quickpost this$0;

            public void onClick(View view1)
            {
                getFragmentManager().popBackStack();
            }

            
            {
                this$0 = Quickpost.this;
                super();
            }
        });
        thumb = (ImageView)layoutinflater.findViewById(0x7f090039);
        txtImagePicker = (TextView)layoutinflater.findViewById(0x7f090038);
        btnDelete = (ImageButton)layoutinflater.findViewById(0x7f09003a);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            final Quickpost this$0;

            public void onClick(View view1)
            {
                mCurrentPhotoPath = null;
                thumb.setImageBitmap(null);
                txtImagePicker.setVisibility(0);
                thumb.setVisibility(8);
                btnDelete.setVisibility(8);
            }

            
            {
                this$0 = Quickpost.this;
                super();
            }
        });
        layoutinflater.findViewById(0x7f090035).setOnClickListener(new View.OnClickListener() {

            final Quickpost this$0;

            public void onClick(View view1)
            {
                (new PosttypePicker(getActivity(), true, postTypePickerCancelListener)).show();
            }

            
            {
                this$0 = Quickpost.this;
                super();
            }
        });
        btnImagePicker = layoutinflater.findViewById(0x7f090037);
        btnImagePicker.setOnClickListener(new View.OnClickListener() {

            final Quickpost this$0;

            public void onClick(View view1)
            {
                (new ImagePicker(getActivity(), true, imagePickerCancelListener)).show();
            }

            
            {
                this$0 = Quickpost.this;
                super();
            }
        });
        return layoutinflater;*/
    //}

    /*public void setThumbnailFromGallery(Uri uri)
        throws FileNotFoundException, IOException
    {
        /*Object obj = getActivity().getContentResolver().openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = true;
        options.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(((InputStream) (obj)), null, options);
        ((InputStream) (obj)).close();
        if (options.outWidth == -1 || options.outHeight == -1)
        {
            return;
        }
        double d;
        int i;
        if (options.outHeight > options.outWidth)
        {
            i = options.outHeight;
        } else
        {
            i = options.outWidth;
        }
        if (i > 140)
        {
            d = i / 140;
        } else
        {
            d = 1.0D;
        }
        obj = new BitmapFactory.Options();
        obj.inSampleSize = getPowerOfTwoForSampleRatio(d);
        obj.inDither = true;
        obj.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;
        uri = getActivity().getContentResolver().openInputStream(uri);
        obj = BitmapFactory.decodeStream(uri, null, ((BitmapFactory.Options) (obj)));
        uri.close();
        thumb.setImageBitmap(null);
        thumb.setImageBitmap(((android.graphics.Bitmap) (obj)));
        txtImagePicker.setVisibility(8);
        thumb.setVisibility(0);
        btnDelete.setVisibility(0);
    }*/


}
