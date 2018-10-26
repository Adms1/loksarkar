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


public class MediaBulletinAdapter extends EndlessRecyclerViewAdapter<String, RecyclerView.ViewHolder, MediaBulletinAdapter.FooterViewHolder>  {

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
    public RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 101:
                View view = this.inflater.inflate(R.layout.mediabulletin_list_item, parent, false);
                return new ImageViewHolder(view);

            case 202:
                View view1 = this.inflater.inflate(R.layout.mediabulletin_list_video_item, parent, false);
                return new MediaBulletinAdapter.VideoViewHolder(view1);

        }
        return null;
    }

    @Override
    public void onBindDataViewHolder(RecyclerView.ViewHolder holder, final int position) {


        switch (holder.getItemViewType()) {
            case 101:
                ImageViewHolder holder1 = (ImageViewHolder) holder;
                holder1.tvTitle.setText(mDataList.get(position).getBulletinTitle());
                holder1.tvCategory.setText(category);
                holder1.tvDate.setText(mDataList.get(position).getBulletinCDate());

                holder1.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentMediaDetail = new Intent(context, MediaDetailActivity.class);
                        intentMediaDetail.putExtra("blog_id",mDataList.get(position).getPKBulletinID());
                        context.startActivity(intentMediaDetail);
                    }
                });
                if(mDataList.get(position).getFileType().equalsIgnoreCase("Image")){
                    holder1.frameImageContainer.setVisibility(View.VISIBLE);
                    holder1.backImage.setVisibility(View.VISIBLE);
                    Glide.with(context).load(mDataList.get(position).getBulletinFileName()).into(holder1.backImage);
                }else{
                    holder1.backImage.setVisibility(View.GONE);
                    holder1.frameImageContainer.setVisibility(View.GONE);
                }
                break;

            case 202:
                VideoViewHolder viewHolder2 = (VideoViewHolder) holder;
                viewHolder2.tvTitle.setText(mDataList.get(position).getBulletinTitle());
                viewHolder2.tvCategory.setText(category);
                viewHolder2.tvDate.setText(mDataList.get(position).getBulletinCDate());
                Glide.with(context).load(R.drawable.video_bg).into(viewHolder2.backImage);

                viewHolder2.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentMediaDetail = new Intent(context, MediaDetailActivity.class);
                        intentMediaDetail.putExtra("blog_id",mDataList.get(position).getPKBulletinID());
                        context.startActivity(intentMediaDetail);
                    }
                });

                break;
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
    @Override
    public int getItemViewType(int position) {
        if(mDataList.get(position).getFileType().equals("Image")){
            return 101;
        }else if(mDataList.get(position).getFileType().equals("Video")){
            return 202;
        }
        return 0;

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvDate,tvCategory;
        private ImageView backImage;
        private FrameLayout frameImageContainer;
        private CardView cardView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.itm_title);
            tvDate = (TextView) itemView.findViewById(R.id.itm_date);
            tvCategory= (TextView) itemView.findViewById(R.id.categoryLabel);
            backImage = (ImageView) itemView.findViewById(R.id.img_preview);
            frameImageContainer = (FrameLayout)itemView.findViewById(R.id.fl_prev_container);
            cardView = (CardView)itemView.findViewById(R.id.cardPlaceItm);
        }

    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvDate,tvCategory;
        private ImageView backImage;
        private FrameLayout frameImageContainer;
        private CardView cardView;

        public VideoViewHolder(View itemView) {
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
