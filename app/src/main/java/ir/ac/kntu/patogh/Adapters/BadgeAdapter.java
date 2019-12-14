package ir.ac.kntu.patogh.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Badge;

import static ir.ac.kntu.patogh.R.id.badge_icon;
import static ir.ac.kntu.patogh.R.id.content;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeAdapterViewHolder> {

    private ArrayList<Badge> eventsData;
    private Context context;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;
    private final BadgeAdapter.BadgeAdapterOnClickHandler mClickHandler;
    long now;
    boolean success = false;
    private ImageView badgeImage;

    public interface BadgeAdapterOnClickHandler {
        void onClick(Badge selectedBadge, ImageView imageView);
    }

    public BadgeAdapter(BadgeAdapter.BadgeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class BadgeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView badgeImage;

        public BadgeAdapterViewHolder(View view) {
            super(view);
            badgeImage = view.findViewById(badge_icon);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            now = System.currentTimeMillis();
            if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                return;
            }
            mLastClickTime = now;
            int adapterPosition = getAdapterPosition();
            Badge selectedBadge = eventsData.get(adapterPosition);
            mClickHandler.onClick(selectedBadge, badgeImage);
        }

    }


    @Override
    public BadgeAdapter.BadgeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.badge_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new BadgeAdapter.BadgeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeAdapterViewHolder holder, int position) {
        Badge selectedBadge = eventsData.get(position);
        holder.badgeImage.setImageResource(selectedBadge.getImageId());
    }

    @Override
    public int getItemCount() {
        if (null == eventsData) return 0;
        return eventsData.size();
    }


    public void setEventData(ArrayList<Badge> eventData) {
        eventsData = eventData;
        notifyDataSetChanged();
    }


//    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
//        Bitmap result = null;
//        try {
//            result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(result);
//
//            int color = 0xff424242;
//            Paint paint = new Paint();
//            Rect rect = new Rect(0, 0, 200, 200);
//
//            paint.setAntiAlias(true);
//            canvas.drawARGB(0, 0, 0, 0);
//            paint.setColor(color);
//            canvas.drawCircle(50, 50, 50, paint);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//            canvas.drawBitmap(bitmap, rect, rect, paint);
//
//        } catch (NullPointerException e) {
//        } catch (OutOfMemoryError o) {
//        }
//        return result;
//    }

}





