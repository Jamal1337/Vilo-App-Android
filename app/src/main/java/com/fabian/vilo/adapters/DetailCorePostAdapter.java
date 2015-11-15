package com.fabian.vilo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.fabian.vilo.R;
import com.fabian.vilo.Tabbar;
import com.fabian.vilo.custom_methods.Util;
import com.fabian.vilo.detail_views.ImageFullscreen;
import com.fabian.vilo.detail_views.QuickpostDetail;
import com.fabian.vilo.me_screen.Me;
import com.fabian.vilo.models.CDModels.CDComment;
import com.fabian.vilo.models.CDModels.CDPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Fabian on 31/10/15.
 */

public class DetailCorePostAdapter extends ArrayAdapter<RealmObject> {

    private CDPost post;
    private CDComment comment;
    private Context context;
    private final int resourceID;
    private QuickpostDetail currentFragment;
    private List<RealmObject> arrayList;

    public DetailCorePostAdapter(Context context, int textViewResourceId, List<RealmObject> objects, QuickpostDetail currentFragment) {
        super(context, textViewResourceId, objects);

        this.context = context;
        this.resourceID = textViewResourceId;
        this.arrayList = objects;
        this.currentFragment = currentFragment;

    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        // Get the data item_event for this position
        if (position == 0) {
            post = (CDPost) getItem(position);
            ViewHolder viewHolder; // view lookup cache stored in tag
            //if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(getContext());


                if (post.getType() == 1) {
                    convertView = inflater.inflate(R.layout.event_main_detail_cell, parent, false); //inflater.inflate(R.layout.event_main_detail_cell, null);

                    viewHolder.eventPostInfoTitle = (TextView) convertView.findViewById(R.id.eventPostInfoTitle);
                    viewHolder.eventPostLocationName = (TextView) convertView.findViewById(R.id.eventPostLocationName);
                    viewHolder.eventPostAdress = (TextView) convertView.findViewById(R.id.eventPostAdress);
                    viewHolder.eventPostDate = (TextView) convertView.findViewById(R.id.eventPostDate);
                } else {
                    convertView = inflater.inflate(R.layout.quick_main_detail_cell, parent, false);
                }

                viewHolder.mainLine = (View) convertView.findViewById(R.id.mainLine);
                viewHolder.quickMainText = (TextView) convertView.findViewById(R.id.quickMainText);
                viewHolder.quickMainTitle = (TextView) convertView.findViewById(R.id.quickMainTitle);
                viewHolder.quickMainImage = (ImageView) convertView.findViewById(R.id.quickMainImage);
                viewHolder.quickMainUserImage = (ImageView) convertView.findViewById(R.id.quickMainUserImage);
                viewHolder.quickMainUsername = (TextView) convertView.findViewById(R.id.quickMainUsername);
                viewHolder.quickMainTime = (TextView) convertView.findViewById(R.id.quickMainTime);

                viewHolder.quickMainImage.getLayoutParams().height = viewHolder.quickMainImage.getLayoutParams().width;

                convertView.setTag(viewHolder);
            /*} else {
                viewHolder = (ViewHolder) convertView.getTag();
            }*/
            // Populate the data into the template view using the data object


            if (post != null) {

                if (post.getType() == 1) {
                    viewHolder.eventPostInfoTitle.setText(post.getTitle());
                    viewHolder.eventPostLocationName.setText(post.getLocation().getName());
                    viewHolder.eventPostAdress.setText(post.getLocation().getCity() + ", " + post.getLocation().getStreet());
                    viewHolder.eventPostDate.setText(post.getEvent_date());
                }

                setTags(viewHolder.quickMainText, post.getText());
                viewHolder.quickMainText.setMovementMethod(LinkMovementMethod.getInstance());
                viewHolder.quickMainTitle.setText(post.getTitle());
                viewHolder.quickMainUsername.setText(post.getUsername());
                viewHolder.quickMainTime.setText(new Util().calculateElapsedTime(post.getTimestamp()));

                if (post.getImgURL().trim().length() > 0) {

                    viewHolder.quickMainImage.setTag(post.getImgURL());

                    Picasso.with(context)
                            .load(post.getImgURL())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .centerCrop()
                            .fit()
                            .into(viewHolder.quickMainImage);

                    viewHolder.quickMainImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String imageURL = ((ImageView) v).getTag().toString();
                            currentFragment.showFullscreenImage(imageURL);

                        }
                    });

                } else {
                    viewHolder.quickMainImage.setVisibility(View.GONE);
                }

                if (arrayList.size() > 1) {
                    viewHolder.mainLine.setBackgroundColor(Color.BLACK);
                } else {
                    viewHolder.mainLine.setBackgroundColor(Color.WHITE);
                }

            }
        } else {
            // TODO: recyclet bei jedem 8. kommentar die main cell....deswegen error => fixed durch auskommentieren von getTag
            // Comments
            comment = (CDComment) getItem(position);
            ViewHolder viewHolder; // view lookup cache stored in tag
            //if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(getContext());

                convertView = inflater.inflate(R.layout.detail_comment_cell, parent, false); //inflater.inflate(R.layout.detail_comment_cell, null);

                viewHolder.commentText = (TextView) convertView.findViewById(R.id.commentText);
                viewHolder.commentImage = (ImageView) convertView.findViewById(R.id.commentImage);
                viewHolder.commentUserImage = (ImageView) convertView.findViewById(R.id.commentUserImage);
                viewHolder.commentUsername = (TextView) convertView.findViewById(R.id.commentUsername);
                viewHolder.commentTime = (TextView) convertView.findViewById(R.id.commentTime);
                viewHolder.commentLine = (View) convertView.findViewById(R.id.commentLine);

                viewHolder.commentImage.getLayoutParams().height = viewHolder.commentImage.getLayoutParams().width;

                convertView.setTag(viewHolder);
            /*} else {
                viewHolder = (ViewHolder) convertView.getTag();
            }*/
            // Populate the data into the template view using the data object

            /*Log.d("bla", "position comment: "+position);
            Log.d("bla", "comment text: "+comment.getValue());
            Log.d("bla", "comment username: "+comment.getUsername());
            Log.d("bla", "comment time: "+new Util().calculateElapsedTime(comment.getTimestamp()));*/

            if (comment != null) {

                setTags(viewHolder.commentText, comment.getValue());
                viewHolder.commentText.setMovementMethod(LinkMovementMethod.getInstance());
                viewHolder.commentUsername.setText(comment.getUsername());
                viewHolder.commentTime.setText(new Util().calculateElapsedTime(comment.getTimestamp()));

                if (comment.getImgURL().trim().length() > 0) {

                    viewHolder.commentImage.setTag(comment.getImgURL());

                    Picasso.with(context)
                            .load(comment.getImgURL())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .centerCrop()
                            .fit()
                            .into(viewHolder.commentImage);

                    viewHolder.commentImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String imageURL = ((ImageView) v).getTag().toString();
                            currentFragment.showFullscreenImage(imageURL);

                        }
                    });

                } else {
                    viewHolder.commentImage.setVisibility(View.GONE);
                }

                if (arrayList.size()-1 == position) {
                    viewHolder.commentLine.setBackgroundColor(Color.WHITE);
                } else {
                    viewHolder.commentLine.setBackgroundColor(Color.BLACK);
                }

            }
        }

        // Return the completed view to render on screen
        return convertView;

    }

    static class ViewHolder
    {
        // general
        View mainLine;

        // quickpost main
        TextView quickMainTitle;
        TextView quickMainText;
        ImageView quickMainImage;
        ImageView quickMainUserImage;
        TextView quickMainUsername;
        TextView quickMainTime;

        // event main
        TextView eventPostInfoTitle;
        TextView eventPostLocationName;
        TextView eventPostAdress;
        TextView eventPostDate;

        // comment
        View commentLine;
        ImageView commentUserImage;
        TextView commentUsername;
        TextView commentTime;
        TextView commentText;
        ImageView commentImage;

    }

    private void setTags(TextView pTextView, String pTagString) {
        SpannableString string = new SpannableString(pTagString);

        int start = -1;
        for (int i = 0; i < pTagString.length(); i++) {
            if (pTagString.charAt(i) == '#') {
                start = i;
            } else if (pTagString.charAt(i) == ' ' || (i == pTagString.length() - 1 && start != -1)) {
                if (start != -1) {
                    if (i == pTagString.length() - 1) {
                        i++; // case for if hash is last word and there is no
                        // space after word
                    }

                    final String tag = pTagString.substring(start, i);
                    string.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            Log.d("Hash", String.format("Clicked %s!", tag));
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // link color
                            //ds.setColor(Color.parseColor("#33b5e5"));
                            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                            ds.setUnderlineText(false);
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = -1;
                }
            }
        }

        //pTextView.setMovementMethod(LinkMovementMethod.getInstance());
        pTextView.setText(string);
    }

}
