package com.loksarkar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loksarkar.R;
import com.loksarkar.activities.Facility;
import com.loksarkar.activities.MediaBulletinActivity;
import com.loksarkar.activities.WebviewActivty;
import com.loksarkar.constants.WebViewURLS;
import com.loksarkar.models.ComplaintsModel;
import com.loksarkar.models.DashBoardModel;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.DateUtils;
import com.loksarkar.utils.PrefUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.media.MediaFormat.KEY_LANGUAGE;

public class ComplaintsListAdapter extends RecyclerView.Adapter<ComplaintsListAdapter .MyViewHolder> {
    private List<ComplaintsModel.FinalAry> dataList;
    private Context context;
    private String language = "";
    private static SharedPreferences localeSharedPrefs;
    private int lastPosition = -1;
    private static final String SP_LOCALE = "LocaleChanger.LocalePersistence";
    private int[] mMaterialColors;

    public ComplaintsListAdapter(Context context, List<ComplaintsModel.FinalAry> dataList) {
        this.context = context;
        this.dataList = dataList;

        mMaterialColors = context.getResources().getIntArray(R.array.complaint_status_colors);

        localeSharedPrefs = context.getSharedPreferences(SP_LOCALE, Context.MODE_PRIVATE);
        language = localeSharedPrefs.getString(KEY_LANGUAGE, "");

        if (language.equalsIgnoreCase("gu")) {

            language = "gujarati";

        } else if (language.equalsIgnoreCase("hi")) {
            language = "hindi";

        } else if (language.equalsIgnoreCase("en")) {
            language = "english";
        }

    }

    @Override
    public ComplaintsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_complaint, parent, false);
        return new ComplaintsListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ComplaintsListAdapter.MyViewHolder holder, final int position) {
        final ComplaintsModel.FinalAry complaintsModel = dataList.get(position);
        setAnimation(holder.itemView, position);
        holder.tv_Complaint_Code.setText(complaintsModel.getNumber());

        try {

            if (complaintsModel.getDate() != null) {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                Date date = (Date) formatter.parse(complaintsModel.getDate());
                System.out.println("Today is " + date.getTime());
                String approxtime = DateUtils.toApproximateTime(context, date.getTime());
                holder.tv_Complaint_date.setText(approxtime);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        holder.tv_Complaint_Status.setText(complaintsModel.getComplaintStatus());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (AppUtils.isNetworkConnected(context)) {
                    Intent intent = new Intent(context, WebviewActivty.class);
                    intent.putExtra("url", complaintsModel.getURL());
                    intent.putExtra("lang", "none");
                    context.startActivity(intent);
                } else {
                    AppUtils.notify(context, context.getString(R.string.no_internet_title), context.getString(R.string.no_internet_msg));
                }

            }
        });

        if(complaintsModel.getComplaintStatus().equals("NoAction")){
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
            shape.setColor(ContextCompat.getColor(context,R.color.no_action));
            shape.setStroke(1, ContextCompat.getColor(context,R.color.white));
            holder.mImageConatiner.setBackground(shape);
            holder.circleImageView.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.tv_Complaint_Status.setTextColor(ContextCompat.getColor(context,R.color.no_action_text));


        }else if(complaintsModel.getComplaintStatus().equals("Close")){

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
            shape.setColor(ContextCompat.getColor(context,R.color.close));
            shape.setStroke(1, ContextCompat.getColor(context,R.color.white));
            holder.mImageConatiner.setBackground(shape);

          //  holder.mImageConatiner.setBackgroundColor(ContextCompat.getColor(context,R.color.close));
            holder.circleImageView.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.tv_Complaint_Status.setTextColor(ContextCompat.getColor(context,R.color.close_text));


        }else if(complaintsModel.getComplaintStatus().equals("Pending")){

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
            shape.setColor(ContextCompat.getColor(context,R.color.pending));
            shape.setStroke(1, ContextCompat.getColor(context,R.color.pending));
            holder.mImageConatiner.setBackground(shape);
            holder.circleImageView.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

            //holder.mImageConatiner.setBackgroundColor(ContextCompat.getColor(context,R.color.pending));
            holder.circleImageView.setColorFilter(ContextCompat.getColor(context, R.color.pending), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.tv_Complaint_Status.setTextColor(ContextCompat.getColor(context,R.color.pending_text));

        }


        Palette.from(((BitmapDrawable)holder.circleImageView.getDrawable()).getBitmap()).maximumColorCount(3).generate(new PalleteGeneration(holder));


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onViewDetachedFromWindow(ComplaintsListAdapter.MyViewHolder holder) {
        ((ComplaintsListAdapter.MyViewHolder) holder).clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tv_Complaint_Code, tv_Complaint_Status, tv_Complaint_date;
        private CardView itemView;
        private AppCompatImageView circleImageView;
        private LinearLayout mImageConatiner;

        public MyViewHolder(View view) {
            super(view);
            tv_Complaint_Code = (AppCompatTextView) view.findViewById(R.id.tv_compalaint_code);
            tv_Complaint_Status = (AppCompatTextView) view.findViewById(R.id.tv_status);
            tv_Complaint_date = (AppCompatTextView) view.findViewById(R.id.tv_date);
            itemView = (CardView) view.findViewById(R.id.view_item);
            circleImageView = (AppCompatImageView)view.findViewById(R.id.iv_doc);
            mImageConatiner = (LinearLayout)view.findViewById(R.id.LL_ic_container);
        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }

    public class PalleteGeneration implements Palette.PaletteAsyncListener {

        private MyViewHolder _holder;

        public PalleteGeneration(MyViewHolder holder) {
            _holder = holder;
        }


        @Override
        public void onGenerated(@NonNull Palette palette) {
            if (palette == null)
                return;

            if (palette.getLightMutedSwatch() != null) {
                Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                _holder.itemView.setBackgroundColor(lightVibrant.getRgb());

            }
            else if (palette.getLightMutedSwatch() != null) {
                Palette.Swatch lightVibrant = palette.getLightMutedSwatch();
                _holder.itemView.setBackgroundColor(lightVibrant.getBodyTextColor());
            }
            if (palette.getDarkVibrantSwatch() != null) {
                Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
                _holder.itemView.setBackgroundColor(darkVibrant.getBodyTextColor());
            }
            else if (palette.getDarkMutedSwatch() != null) {
                Palette.Swatch darkVibrant = palette.getDarkMutedSwatch();
                _holder.itemView.setBackgroundColor(darkVibrant.getBodyTextColor());
            }
        }
    }

}

