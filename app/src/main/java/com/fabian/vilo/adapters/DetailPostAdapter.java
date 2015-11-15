package com.fabian.vilo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.TextView;

import com.fabian.vilo.R;
import com.fabian.vilo.detail_views.QuickpostDetail;
import com.fabian.vilo.models.Card;
import com.fabian.vilo.custom_methods.Util;
import com.fabian.vilo.models.Comment;
import com.fabian.vilo.models.NewQuickComments;
import com.fabian.vilo.models.WebPostInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 31/10/15.
 */

public class DetailPostAdapter extends ArrayAdapter<WebPostInterface> {

    private Card post;
    private Comment comment;
    private Context context;
    private final int resourceID;
    private QuickpostDetail currentFragment;
    private List<WebPostInterface> arrayList;

    public DetailPostAdapter(Context context, int textViewResourceId, List<WebPostInterface> objects, QuickpostDetail currentFragment) {
        super(context, textViewResourceId, objects);

        this.context = context;
        this.resourceID = textViewResourceId;
        this.arrayList = objects;
        this.currentFragment = currentFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        if (position == 0) {
            // Get the data item_event for this position
            post = (Card) getItem(position);
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(getContext());


                if (post.type == 1) {
                    convertView = inflater.inflate(R.layout.event_main_detail_cell, parent, false);

                    viewHolder.eventPostInfoTitle = (TextView) convertView.findViewById(R.id.eventPostInfoTitle);
                    viewHolder.eventPostLocationName = (TextView) convertView.findViewById(R.id.eventPostLocationName);
                    viewHolder.eventPostAdress = (TextView) convertView.findViewById(R.id.eventPostAdress);
                    viewHolder.eventPostDate = (TextView) convertView.findViewById(R.id.eventPostDate);
                } else {
                    convertView = inflater.inflate(R.layout.quick_main_detail_cell, null);
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
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // Populate the data into the template view using the data object


            if (post != null) {

                if (post.type == 1) {
                    viewHolder.eventPostInfoTitle.setText(post.title);
                    viewHolder.eventPostLocationName.setText(post.location.get(0).name);
                    viewHolder.eventPostAdress.setText(post.location.get(0).city + ", " + post.location.get(0).street);
                    viewHolder.eventPostDate.setText(post.event_date);
                }

                setTags(viewHolder.quickMainText, post.text);
                viewHolder.quickMainTitle.setText(post.title);
                viewHolder.quickMainUsername.setText(post.username);
                viewHolder.quickMainTime.setText(new Util().calculateElapsedTime(post.timestamp));
                Picasso.with(context)
                        .load(post.photo)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .centerCrop()
                        .fit()
                        .into(viewHolder.quickMainUserImage);

                if (post.attachment.trim().length() > 0) {

                    viewHolder.quickMainImage.setTag(post.attachment);

                    Picasso.with(context)
                            .load(post.attachment)
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
            // comments
            comment = (Comment) getItem(position);
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

                setTags(viewHolder.commentText, comment.value);
                viewHolder.commentText.setMovementMethod(LinkMovementMethod.getInstance());
                viewHolder.commentUsername.setText(comment.username);
                viewHolder.commentTime.setText(new Util().calculateElapsedTime(comment.timestamp));

                if (comment.imgURL.trim().length() > 0) {

                    viewHolder.commentImage.setTag(comment.imgURL);

                    Picasso.with(context)
                            .load(comment.imgURL)
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

}
