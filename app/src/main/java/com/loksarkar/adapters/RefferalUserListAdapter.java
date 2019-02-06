package com.loksarkar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
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
import android.widget.Switch;
import android.widget.TextView;

import com.loksarkar.R;
import com.loksarkar.activities.WebviewActivty;
import com.loksarkar.models.ComplaintsModel;
import com.loksarkar.models.RefferalUserListModel;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.media.MediaFormat.KEY_LANGUAGE;

public class RefferalUserListAdapter<T> extends RecyclerView.Adapter<RefferalUserListAdapter.MyViewHolder> {

    private List<T> dataList;
    private Context context;
    private String language = "";
    private static SharedPreferences localeSharedPrefs;
    private int lastPosition = -1;
    private static final String SP_LOCALE = "LocaleChanger.LocalePersistence";
    private int[] mMaterialColors;
    private int type;

    public RefferalUserListAdapter(Context context, List<T> dataList,int type) {
        this.context = context;
        this.type = type;
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
        public RefferalUserListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_referral_user_list, parent, false);
            return new RefferalUserListAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RefferalUserListAdapter.MyViewHolder holder, final int position) {

        switch(type){
            case 1:
                final RefferalUserListModel.RegisterDevice data = (RefferalUserListModel.RegisterDevice)dataList.get(position);
                setAnimation(holder.itemView, position);

                int index  = (position + 1);
                holder.tv_index.setText(String.valueOf(index));
                holder.tv_Name.setText(data.getName());

//            if(position  == 0){
//                data.setPoints(7000);
//            }

                holder.tv_UserCode.setText("("+String.valueOf(data.getCode())+")");
                holder.tv_UserCode.setVisibility(View.GONE);
                holder.tv_points.setText(String.valueOf(data.getPoints()));

//            try {
//                String [] district = data.getDistrict().split(" ");
//                String city  = district[0];
//                holder.tv_disrict.setText(city);
//
//            }catch (Exception ex){
//                ex.getLocalizedMessage();
//                holder.tv_disrict.setText(data.getDistrict());
//            }

                holder.tv_disrict.setText(data.getDistrict());

                holder.iv_rank.setVisibility(View.VISIBLE);

                if(position == (dataList.size() -1)) {
                    holder.viewLine.setVisibility(View.GONE);
                }

                break;

            case 2:
                final RefferalUserListModel.RegisterComplaint data2 = (RefferalUserListModel.RegisterComplaint)dataList.get(position);
                setAnimation(holder.itemView, position);

                int index1  = (position + 1);
                holder.tv_index.setText(String.valueOf(index1));
                holder.tv_Name.setText(data2.getName());

//            if(position  == 0){
//                data.setPoints(7000);
//            }

                holder.tv_UserCode.setText("("+String.valueOf(data2.getCode())+")");
                holder.tv_UserCode.setVisibility(View.GONE);
                holder.tv_points.setText(String.valueOf(data2.getPoints()));

//            try {
//                String [] district = data.getDistrict().split(" ");
//                String city  = district[0];
//                holder.tv_disrict.setText(city);
//
//            }catch (Exception ex){
//                ex.getLocalizedMessage();
//                holder.tv_disrict.setText(data.getDistrict());
//            }

                holder.tv_disrict.setText(data2.getDistrict());

                holder.iv_rank.setVisibility(View.VISIBLE);

                if(position == (dataList.size() -1)) {
                    holder.viewLine.setVisibility(View.GONE);
                }

                break;
        }

//            if(position == 0) {
//
//                holder.iv_rank.setImageResource(R.drawable.ic_goldmedal);
//
//            }else if(position == 1) {
//                holder.iv_rank.setImageResource(R.drawable.ic_silver_medal);
//            } if(position == 2) {
//                holder.iv_rank.setImageResource(R.drawable.ic_bronze_medal);
//            } if(position == 3) {
//                holder.iv_rank.setVisibility(View.GONE);
//            } if(position == 4) {
//                holder.iv_rank.setVisibility(View.GONE);
//
//            }


            //Palette.from(((BitmapDrawable)holder.circleImageView.getDrawable()).getBitmap()).maximumColorCount(3).generate(new PalleteGeneration(holder));

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }


        @Override
        public void onViewDetachedFromWindow(RefferalUserListAdapter.MyViewHolder holder) {
            ((RefferalUserListAdapter.MyViewHolder) holder).clearAnimation();
        }

        private void setAnimation(View viewToAnimate, int position) {
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private AppCompatTextView tv_Name,tv_UserCode,tv_points,tv_index,tv_disrict;
            private CardView itemView;
            private ImageView circleImageView,iv_rank;
            private View viewLine;

            private RelativeLayout mImageConatiner;

            public MyViewHolder(View view) {
                super(view);

                tv_index = (AppCompatTextView) view.findViewById(R.id.tv_index);

                tv_Name = (AppCompatTextView) view.findViewById(R.id.tv_name);
                tv_UserCode = (AppCompatTextView) view.findViewById(R.id.tv_ref_code);
                tv_points = (AppCompatTextView) view.findViewById(R.id.tv_points);
                itemView = (CardView) view.findViewById(R.id.view_item);
                circleImageView = (ImageView)view.findViewById(R.id.iv_doc);
                mImageConatiner = (RelativeLayout) view.findViewById(R.id.LL_ic_container);
                iv_rank = (ImageView)view.findViewById(R.id.iv_medal);
                viewLine = (View)view.findViewById(R.id.view_line);
                tv_disrict = (AppCompatTextView) view.findViewById(R.id.tv_district);
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
                    _holder.itemView.setBackgroundColor(lightVibrant.getTitleTextColor());

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




