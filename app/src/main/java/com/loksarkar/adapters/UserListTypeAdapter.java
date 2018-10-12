package com.loksarkar.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loksarkar.R;
import com.loksarkar.activities.CommitteeRegistrationActivity;
import com.loksarkar.models.UserListModel;

import java.util.List;

public class UserListTypeAdapter extends RecyclerView.Adapter<UserListTypeAdapter.MyViewHolder> {

    private List<UserListModel> dataList;
    private Context context;
    private int lastPosition = -1;

    public UserListTypeAdapter(Context context,List<UserListModel> dataList) {
        this.context = context;
        this.dataList = dataList;
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.registration_list_item, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
     public void onBindViewHolder(MyViewHolder holder, final int position) {
        UserListModel userListModel = dataList.get(position);
        holder.userType.setText(userListModel.getTypeOfUser());
        holder.circleImageView.setImageResource(userListModel.getIconOfUser());

        setAnimation(holder.itemView, position);

        holder.view_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentRegistartion = new Intent(context,CommitteeRegistrationActivity.class);
                intentRegistartion.putExtra("pos",position);
                context.startActivity(intentRegistartion);
                }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @Override
    public void onViewDetachedFromWindow(UserListTypeAdapter.MyViewHolder holder) {
        ((MyViewHolder)holder).clearAnimation();
    }


    private void setAnimation(View viewToAnimate, int position) {

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView userType;
        private ImageView circleImageView;
        private CardView view_Click;
        private RelativeLayout rootLayout;

        public MyViewHolder(View view) {
            super(view);
            userType = (TextView) view.findViewById(R.id.tv_usertype);
            circleImageView = (ImageView) view.findViewById(R.id.iv_menu);
            view_Click = (CardView) view.findViewById(R.id.view_click);
            rootLayout = (RelativeLayout)view.findViewById(R.id.RL_root);
           }

        public void clearAnimation() {
            rootLayout.clearAnimation();
        }
    }

}
