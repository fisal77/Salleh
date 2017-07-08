package com.seniorproject.salleh.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.salleh.entities.Post;

/**
 * Created by abdul on 07-Apr-2017.
 */

public class SavePostAsync extends AsyncTask<Void, Void, Object> {
    private Context mContext;
    private Post mPost;
    private Bitmap mImage;
    public SavePostAsync(Post post, Bitmap image, Context context) {
        mPost = post;
        mContext = context;
        mImage = image;
    }

    @Override
    protected Object doInBackground(Void... params) {
       Object result = null;
        try {
           MobileServiceClient client = MyHelper.getAzureClient(mContext);
           MobileServiceTable postTable = client.getTable(Post.class);
           String imagePath = mPost.get_imagePath();
           if (imagePath != null &&  !imagePath.isEmpty()) {
               AzureBlob.upload(imagePath, mImage, mContext);
           }
          result =  postTable.insert(mPost).get();

       }
       catch (Exception e){
           Log.e("Save Post Async", "doInBackground: "  +e.getMessage() );
       }
        return result;
    }

}
