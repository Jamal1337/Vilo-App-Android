// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.fabian.vilo.around_me_screen;

import android.app.Dialog;
import android.content.Context;


public class SetRadius extends Dialog
{

    Context context;
    Dialog dialog;
    public int radius;

    protected SetRadius(final Context context, boolean flag, final OnCancelListener txtDistance)
    {
        super(context, flag, txtDistance);
        this.context = context;
        dialog = this;
        /*getWindow().setBackgroundDrawable(new ColorDrawable(0));
        requestWindowFeature(1);
        setContentView(0x7f03000a);
        setCanceledOnTouchOutside(flag);
        setCancelable(flag);
        txtDistance = getWindow().getAttributes();
        txtDistance.dimAmount = 0.0F;
        txtDistance.gravity = 51;
        Object obj = new TypedValue();
        if (context.getTheme().resolveAttribute(0x10102eb, ((TypedValue) (obj)), true))
        {
            txtDistance.y = TypedValue.complexToDimensionPixelSize(((TypedValue) (obj)).data, context.getResources().getDisplayMetrics()) - (int)Util.convertDpToPixel(1.0F, context);
        }
        txtDistance.x = (int)Util.convertDpToPixel(10F, context);
        radius = 50;
        txtDistance = (TextView)findViewById(0x7f090032);
        obj = (SeekBar)findViewById(0x7f090033);
        ((SeekBar) (obj)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            final DistancePicker this$0;
            final Context val$context;
            final TextView val$txtDistance;

            public void onProgressChanged(SeekBar seekbar, int i, boolean flag1)
            {
                seekbar = context.getString(0x7f050013);
                txtDistance.setText(String.format(seekbar, new Object[] {
                    String.valueOf((float)i / 10F)
                }));
                radius = i;
            }

            public void onStartTrackingTouch(SeekBar seekbar)
            {
            }

            public void onStopTrackingTouch(SeekBar seekbar)
            {
            }

            
            {
                this$0 = DistancePicker.this;
                context = context1;
                txtDistance = textview;
                super();
            }
        });
        ((SeekBar) (obj)).setProgress(radius);
        findViewById(0x7f090034).setOnClickListener(new View.OnClickListener() {

            final DistancePicker this$0;

            public void onClick(View view)
            {
                dialog.cancel();
            }

            
            {
                this$0 = DistancePicker.this;
                super();
            }
        });*/
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int i)
    {
        radius = i;
    }
}
