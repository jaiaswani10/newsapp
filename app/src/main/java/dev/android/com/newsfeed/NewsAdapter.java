package dev.android.com.newsfeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class NewsAdapter extends ArrayAdapter<News>{

    public NewsAdapter(Context context, List<News> newsList){
        super(context,0,newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        News currentNews = getItem(position);

        ImageView ivThumbnail = listItemView.findViewById(R.id.thumbnail);
        TextView tvHeadline = listItemView.findViewById(R.id.heading);
        TextView tvSection = listItemView.findViewById(R.id.section);
        TextView tvTime = listItemView.findViewById(R.id.time);

        TextView tvAuthor = listItemView.findViewById(R.id.author);

        if(TextUtils.isEmpty(currentNews.getmThumbnail()) || currentNews.getmThumbnail() == null){
            View iv = listItemView.findViewById(R.id.thumbnail);
            iv.setVisibility(View.GONE);
        }
        else{
            RequestOptions options = new RequestOptions().error(R.drawable.ic_launcher_background).centerCrop();

            Glide.with(getContext()).load(currentNews.getmThumbnail())
                    .apply(options)
                    .transition(withCrossFade())
                    .into(ivThumbnail);
        }


        tvHeadline.setText(currentNews.getmHeadline());
        tvSection.setText(currentNews.getmTags());
        tvTime.setText(getElapsedTime(currentNews.getmTime()));

        tvAuthor.setText(currentNews.getmAuthor());

        return listItemView;
    }


    private String getElapsedTime(String time){
        String dateStart =time;



        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        java.util.Date currentDate = new java.util.Date();
        java.util.Date d1 = null;

        try {
            d1 = utcFormat.parse(dateStart);
        } catch (Exception e) {
            e.printStackTrace();
        }




        long diff = currentDate.getTime() - d1.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((currentDate.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));


        if (diffInDays == 0)
            if(diffHours == 0)
                if(diffMinutes == 0)
                    if(diffSeconds ==0)  return "Now";
                    else return diffSeconds+" s ago";
                else return diffMinutes + "m ago";
            else return diffHours + "h ago";
        else {

            if(diffInDays >30 && diffInDays<=365){
                int months = diffInDays /30;
                return months+"months ago";
            }
            else if(diffInDays > 365){
                int years = diffInDays / 365;
                return years+"years ago";
            }
            return diffInDays+"days ago";

        }
    }
}
