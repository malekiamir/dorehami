package ir.ac.kntu.patogh.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ir.ac.kntu.patogh.ApiDataTypes.TypeFavDorehamiAdd;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.DetailedBadge;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailedBadgeAdapter extends RecyclerView.Adapter<DetailedBadgeAdapter.ViewHolder> {

    private ArrayList<DetailedBadge> mBadges;
    private Context context;

    public DetailedBadgeAdapter(Context context) {
        this.context = context;
        mBadges = new ArrayList<>();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        ProgressBar progressBar;
        CardView detailedBadgeCard;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_badges_page_badge_image);
            textView = itemView.findViewById(R.id.tv_badges_page_name);
//            progressBar = itemView.findViewById(R.id.pb_badges_page_percent_of_achieve);
            detailedBadgeCard = itemView.findViewById(R.id.layout_detailed_badge_card);
        }
    }

    @Override
    public DetailedBadgeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.detailed_badge_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new DetailedBadgeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailedBadge selectedBadge = mBadges.get(position);
        holder.textView.setText(selectedBadge.getBadgeName());
        holder.imageView.setImageResource(selectedBadge.getImageId());
        //holder.progressBar.setProgress(selectedBadge.getCompletionPercent());
    }

    @Override
    public int getItemCount() {
        if (null == mBadges) return 0;
        return mBadges.size();
    }


    public void setEventData(ArrayList<DetailedBadge> badgesData) {
        mBadges = badgesData;
        notifyDataSetChanged();
    }


}


