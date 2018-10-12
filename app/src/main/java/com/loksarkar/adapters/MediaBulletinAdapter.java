package com.loksarkar.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loksarkar.R;
import com.loksarkar.activities.MediaDetailActivity;
import com.loksarkar.models.MediaBulletinModel;
import java.util.List;


public class MediaBulletinAdapter extends EndlessRecyclerViewAdapter<String, MediaBulletinAdapter.MyViewHolder, MediaBulletinAdapter.FooterViewHolder>  {

    private Context context;
    private List<MediaBulletinModel.FinalArray> mDataList;
    private String category;

    public MediaBulletinAdapter(List<MediaBulletinModel.FinalArray> mDataList, Context context,String category) {
        super(mDataList,context);
        this.context = context;
        this.mDataList = mDataList;
        this.category = category;

    }

    @Override
    public MediaBulletinAdapter.MyViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.mediabulletin_list_item, parent, false);
        return new MediaBulletinAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindDataViewHolder(MediaBulletinAdapter.MyViewHolder holder, final int position) {
        holder.tvTitle.setText(mDataList.get(position).getBulletinTitle());
        holder.tvCategory.setText(category);
        holder.tvDate.setText(mDataList.get(position).getBulletinCDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMediaDetail = new Intent(context, MediaDetailActivity.class);
                intentMediaDetail.putExtra("blog_id",mDataList.get(position).getPKBulletinID());
                context.startActivity(intentMediaDetail);
            }
        });
        if(mDataList.get(position).getFileType().equalsIgnoreCase("Image")){
            holder.frameImageContainer.setVisibility(View.VISIBLE);
            holder.backImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(mDataList.get(position).getBulletinFileName()).into(holder.backImage);
        }else{
            holder.backImage.setVisibility(View.GONE);
            holder.frameImageContainer.setVisibility(View.GONE);

        }


    }

    @Override
    public MediaBulletinAdapter.FooterViewHolder onCreateFooterViewHolder(ViewGroup parent, MediaBulletinAdapter.FooterViewHolder reusableFooterHolder) {
        if (reusableFooterHolder == null) {
            View view = inflater.inflate(R.layout.loading_footer_item, parent, false);
            return new FooterViewHolder(view);
        }
        return reusableFooterHolder;
    }

    @Override
    public void onBindFooterViewHolder(MediaBulletinAdapter.FooterViewHolder holder, int position) {
        holder.textView.setText("Loading from item " + String.valueOf(position));
    }
    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvDate,tvCategory;
        private ImageView backImage;
        private FrameLayout frameImageContainer;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.itm_title);
            tvDate = (TextView) itemView.findViewById(R.id.itm_date);
            tvCategory= (TextView) itemView.findViewById(R.id.categoryLabel);
            backImage = (ImageView) itemView.findViewById(R.id.img_preview);
            frameImageContainer = (FrameLayout)itemView.findViewById(R.id.fl_prev_container);
            cardView = (CardView)itemView.findViewById(R.id.cardPlaceItm);
        }

    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.footer_text);

        }
    }

}
