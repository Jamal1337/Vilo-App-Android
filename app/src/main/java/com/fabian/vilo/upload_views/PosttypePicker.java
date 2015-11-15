package com.fabian.vilo.upload_views.me_screen;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

public class PosttypePicker extends Dialog
{

    Context context;
    Dialog dialog;
    int dialogResult;

    protected PosttypePicker(Context context1, boolean flag, OnCancelListener oncancellistener)
    {
        super(context1, flag, oncancellistener);
        context = context1;
        dialog = this;
        requestWindowFeature(1);
        //setContentView(0x7f030009);
        setCanceledOnTouchOutside(flag);
        setCancelable(flag);
        getWindow().getAttributes().width = context1.getResources().getDisplayMetrics().widthPixels * 1;
        //context1 = findViewById(0x7f090023);
        //oncancellistener = findViewById(0x7f090026);
        //View view = findViewById(0x7f09002c);
        //View view1 = findViewById(0x7f090029);
        /*context1.setOnClickListener(new View.OnClickListener() {

            final PosttypePicker this$0;

            public void onClick(View view2)
            {
                dialogResult = 100;
                dialog.cancel();
            }

            
            {
                this$0 = PosttypePicker.this;
                super();
            }
        });*/
        /*oncancellistener.setOnClickListener(new View.OnClickListener() {

            final PosttypePicker this$0;

            public void onClick(View view2)
            {
                dialogResult = 101;
                dialog.cancel();
            }

            
            {
                this$0 = PosttypePicker.this;
                super();
            }
        });*/
        /*view.setOnClickListener(new View.OnClickListener() {

            final PosttypePicker this$0;

            public void onClick(View view2)
            {
                dialogResult = 103;
                dialog.cancel();
            }

            
            {
                this$0 = PosttypePicker.this;
                //super();
            }
        });
        view1.setOnClickListener(new View.OnClickListener() {

            final PosttypePicker this$0;

            public void onClick(View view2)
            {
                dialogResult = 102;
                dialog.cancel();
            }

            
            {
                this$0 = PosttypePicker.this;
                //super();
            }
        });*/
    }

    public int getDialogResult()
    {
        return dialogResult;
    }
}
