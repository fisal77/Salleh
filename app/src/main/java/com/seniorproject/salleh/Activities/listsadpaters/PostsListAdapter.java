package com.seniorproject.salleh.Activities.listsadpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seniorproject.salleh.entities.DomainComment;
import com.seniorproject.salleh.entities.DomainPost;
import com.seniorproject.salleh.R;
import com.seniorproject.salleh.helpers.MyHelper;

import java.util.ArrayList;


public class PostsListAdapter  extends ArrayAdapter<DomainPost> {
    private ArrayList<DomainPost> _items;
    private Context mContext;
    public PostsListAdapter(Context context, ArrayList<DomainPost> items) {
       super(context, R.layout.post_layout, items);
        mContext = context;
        _items = items;

    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public DomainPost getItem(int position) {
        return _items.get(position);
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder viewHolder;
        try {
            if(convertView == null){
                LayoutInflater inflater =
                        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.post_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.userAvatart = (ImageView) convertView.findViewById(R.id.postLayout_imgUserAvatar);
                viewHolder.posDateTextView = (TextView) convertView.findViewById(R.id.postLayout_lblPostDate);
                viewHolder.posterNameTextView = (TextView) convertView.findViewById(R.id.postLayout_lblUserName);
                viewHolder.postImage = (ImageView) convertView.findViewById(R.id.postLayout_imgPostImage);
                viewHolder.subjectTextView = (TextView) convertView.findViewById(R.id.postLayout_txtPosSubject);
                viewHolder.commenterNameTextView = (TextView)convertView.findViewById(R.id.commentLayout_lblUserName);
                viewHolder.commenterAvatar =(ImageView)convertView.findViewById(R.id.commentLayout_imgUserAvatar);
                viewHolder.commentTextView=(TextView)convertView.findViewById(R.id.commentLayout_lblComment);
                viewHolder.commentDateTextView = (TextView)convertView.findViewById(R.id.commentLayout_commentDate);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            DomainPost post = getItem(position);
            //Bind the UI elements to entity
           bindPost(post, viewHolder);
        }
        catch (Exception e){
            //Log the exception so it can be reviewed.
            //Log.d("SALLEM APP", e.getStackTrace().toString());
            throw e;
        }
        return convertView;
    }

    private void bindPost(DomainPost post,ViewHolder holder) {
        holder.posterNameTextView.setText(post.get_user().getFirstName() + " " + post.get_user().getLasttName());
        holder.userAvatart.setImageBitmap(post.get_user().getAvatar());
        holder.posDateTextView.setText(MyHelper.formatDateString(post.get_postedAt()));
        holder.subjectTextView.setText(post.get_subject());
        holder.postImage.setVisibility(View.VISIBLE);
        if(post.get_image() != null){
           holder.postImage.setImageBitmap(post.get_image());
        }
        else{
          holder.postImage.setImageBitmap(null);
            holder.postImage.setVisibility(View.GONE);
        }
        if(post.get_comments()!= null && post.get_comments().size() > 0){
            holder.commenterNameTextView.setVisibility(View.VISIBLE);
            holder.commenterAvatar.setVisibility(View.VISIBLE);
            holder.commentDateTextView.setVisibility(View.VISIBLE);
            holder.commentTextView.setVisibility(View.VISIBLE);

            DomainComment comment = post.get_comments().get(0);
            holder.commenterNameTextView.setText(comment.get_user().getFirstName() + " " +comment.get_user().getLasttName());
            holder.commenterAvatar.setImageBitmap(comment.get_user().getAvatar());
            holder.commentDateTextView.setText(MyHelper.formatDateString(comment.get_commentedAt()));
            holder.commentTextView.setText(comment.get_subject());
        }
        else{
            holder.commenterNameTextView.setVisibility(View.GONE);
            holder.commenterAvatar.setVisibility(View.GONE);
            holder.commentDateTextView.setVisibility(View.GONE);
            holder.commentTextView.setVisibility(View.GONE);
        }
    }

    private static class ViewHolder{
        ImageView userAvatart ;
        TextView posDateTextView ;
        TextView posterNameTextView ;
        ImageView postImage;
        TextView subjectTextView ;
        TextView commenterNameTextView;
        ImageView commenterAvatar;
        TextView commentTextView;
        TextView commentDateTextView;

    }


}
