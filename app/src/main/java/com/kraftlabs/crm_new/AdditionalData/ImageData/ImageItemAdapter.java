package com.kraftlabs.crm_new.AdditionalData.ImageData;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kraftlabs.crm_new.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;



/**
 * Created by ashik on 21/8/17.
 */

public class ImageItemAdapter extends ArrayAdapter<ImageModel> {
    Context context;
    int layoutResourceId;
    ArrayList<ImageModel> data = new ArrayList<ImageModel>();

    public ImageItemAdapter(Context context, int layoutResourceId, ArrayList<ImageModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        //View row = convertView;
        ImageModel data = getItem(position);
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);

            viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            viewHolder.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imgIcon.setImageBitmap(convertToBitmap(data.getImage()));
        return view;

    }

    private Bitmap convertToBitmap(byte[] b) {

        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(b);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
        /*return BitmapFactory.decodeByteArray(b, 0, b.length);*/

    }

    static class ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
