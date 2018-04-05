package com.curofy.anubhav.assignmentproject.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.curofy.anubhav.assignmentproject.R;

/**
 * Created by anubhav on 5/4/18.
 */

public class SignInPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] staticImages;

    public SignInPagerAdapter(Context context, int[] images) {
        this.context = context;
        this.staticImages = images;
    }

    @Override
    public int getCount() {
        //Since its static
        return staticImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.sign_in_pager_item,container,false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_signIn_pager);
        imageView.setImageResource(staticImages[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
