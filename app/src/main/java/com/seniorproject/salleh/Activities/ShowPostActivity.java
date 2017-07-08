package com.seniorproject.salleh.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.salleh.Activities.listsadpaters.CommentsListAdapter;
import com.seniorproject.salleh.R;
import com.seniorproject.salleh.entities.Comment;
import com.seniorproject.salleh.entities.DomainComment;
import com.seniorproject.salleh.entities.DomainPost;
import com.seniorproject.salleh.entities.DomainUser;
import com.seniorproject.salleh.helpers.CommonMethods;
import com.seniorproject.salleh.helpers.EntityAsyncResult;
import com.seniorproject.salleh.helpers.EntityAsyncResultTwo;
import com.seniorproject.salleh.helpers.MyApplication;
import com.seniorproject.salleh.helpers.MyHelper;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ShowPostActivity extends AppCompatActivity implements EntityAsyncResult<DomainPost>, EntityAsyncResultTwo<DomainComment> {

   private ImageView mUserAvatart;
   private TextView mPosDate;
   private TextView mPoster;
   private TextView mPostSubject;
   private EditText mPostComment;
   private ImageView mPostImage;
   private ListView mCommentsList;
   private ImageButton mSendCommentButton;
   private CommentsListAdapter mCommentsAdapter;
   private DomainPost mCurrentPost;
   private MyApplication myApp;
   private DomainPost mShownPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        mUserAvatart = (ImageView) findViewById(R.id.showPost_imgUserAvatar);
        mPosDate = (TextView)
                findViewById(R.id.showPost_lblPostDate);
        mPoster = (TextView)
                findViewById(R.id.showPost_lblUserName);
        mPostSubject = (TextView)
                findViewById(R.id.showPost_txtPosSubject);
        mPostSubject.setMovementMethod(new ScrollingMovementMethod());
        mPostComment = (EditText)
                findViewById(R.id.showPost_txtComment);
        mPostImage = (ImageView)
                findViewById(R.id.showPost_imgPostImage);
        mSendCommentButton = (ImageButton)
                findViewById(R.id.showPost_btnSendComment);
        mCommentsList = (ListView)
                findViewById(R.id.showPost_listComments);
        attachSendCommentButton();
        ArrayList<DomainComment> comments =new ArrayList();
        mCommentsAdapter = new CommentsListAdapter(this , comments);
        mCommentsList.setAdapter(mCommentsAdapter);
        myApp = (MyApplication) getApplication();
        Bundle b = getIntent().getExtras();
        String postId = b.getString("postId");
        loadPost(postId);
    }

    private void attachSendCommentButton() {

        mSendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPostComment.getText().toString() == null ||
                        mPostComment.getText().toString().isEmpty()){
                    return;
                }
                DomainComment domainComment = new DomainComment();
                domainComment.set_id(UUID.randomUUID().toString());
                String commentedAt = MyHelper.getCurrentDateTime();
                domainComment.set_commentedAt(commentedAt);
                domainComment.set_subject(mPostComment.getText().toString());
                domainComment.set_user(DomainUser.CURRENT_USER);
                domainComment.set_userId(DomainUser.CURRENT_USER.getId());
                domainComment.set_post(mCurrentPost);
                domainComment.set_posId(mCurrentPost.get_id());
                SaveCommentAsync savePostAsync = new
                        SaveCommentAsync(ShowPostActivity.this, domainComment);
                savePostAsync.Delegate = ShowPostActivity.this  ;
                savePostAsync.execute();
                mPostComment.setText("");
            }
        });

    }

    private void loadPost(final String postId) {
//        LoadPost asyncLoading = new LoadPost(this, postId);
//        asyncLoading.Delegate = this;
//        asyncLoading.execute();
         for(DomainPost p: myApp.Posts_Cach){
             if(p.get_id().equals(postId)){
                 mShownPost = p;
                 break;
             }
         }
        processFinish(mShownPost);

    }

    @Override
    public void processFinish(DomainPost result) {
        if(result != null){
            if(result.get_image() != null) {
                mPostImage.setImageBitmap(result.get_image());
            }
            else{
                mPostImage.setVisibility(View.GONE);
            }
            mPoster.setText(result.get_user().getFirstName() + " " + result.get_user().getLasttName());;
            mUserAvatart.setImageBitmap(result.get_user().getAvatar());;
            mPosDate.setText(MyHelper.formatDateString(result.get_postedAt()));
            mPostSubject.setText(result.get_subject());
            mCommentsAdapter.addAll(result.get_comments());
            mCurrentPost = result;
        }
    }


    @Override
    public void processFinishTwo(DomainComment result) {
        mCommentsAdapter.add(result);
        mShownPost.get_comments().add(0, result);
    }




    private class SaveCommentAsync extends AsyncTask<Void, Void, DomainComment>{

        public EntityAsyncResultTwo<DomainComment> Delegate;
        private MobileServiceClient mClient;
        private MobileServiceTable<Comment> mCommentTable;
        private DomainComment mComment;
        private Context mContext;

        public SaveCommentAsync( Context context, DomainComment comment){
            mContext = context;
            mComment = comment;
            try {
                mClient = MyHelper.getAzureClient(mContext);
            }
            catch (MalformedURLException e){
                Log.d("SALLEMAPP", e.getCause().getMessage());

            }


        }
        @Override
        protected DomainComment doInBackground(Void... params){
            mCommentTable = mClient.getTable(Comment.class);

            try {
                Comment dbComment = new Comment();
                dbComment.set_id(mComment.get_id());
                dbComment.set_commentedAt(mComment.get_commentedAt());
                dbComment.set_userId(mComment.get_userId());
                dbComment.set_postId(mComment.get_posId());
                dbComment.set_subject(mComment.get_subject());

                mCommentTable.insert(dbComment).get();

            }
            catch (ExecutionException e) {
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());
                e.printStackTrace();

            } catch (InterruptedException e) {
                Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());
            }


            return mComment;
        }
        @Override
        protected void onPostExecute(DomainComment domainComment) {
            Delegate.processFinishTwo(domainComment);
        }
    }




}
