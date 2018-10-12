package com.loksarkar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import com.loksarkar.activities.MediaBulletinActivity;
import com.loksarkar.activities.MediaDetailActivity;
import com.loksarkar.activities.WebviewActivty;
import com.loksarkar.activities.Facility;
import com.loksarkar.constants.WebViewURLS;
import com.loksarkar.models.DashBoardModel;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.PrefUtils;

import java.util.List;

import static android.media.MediaFormat.KEY_LANGUAGE;

public class DashboardMenuAdapter extends RecyclerView.Adapter<DashboardMenuAdapter.MyViewHolder> {

    private List<DashBoardModel> dataList;
    private Context context;
    private int lastPosition = -1;
    private int[] bgColors;
    private SharedPreferences pref;
    private String firebase_RegId;
    private String language = "";
    private static SharedPreferences localeSharedPrefs;
    private static final String SP_LOCALE = "LocaleChanger.LocalePersistence";



    public DashboardMenuAdapter(Context context,List<DashBoardModel> dataList,int[] color) {
        this.context = context;
        this.dataList = dataList;
        bgColors = new int[dataList.size()];
        bgColors = color;
        localeSharedPrefs = context.getSharedPreferences(SP_LOCALE, Context.MODE_PRIVATE);
        language =  localeSharedPrefs.getString(KEY_LANGUAGE, "");

        if(language.equalsIgnoreCase("gu")){

            language = "gujarati";

        }else if(language.equalsIgnoreCase("hi")){

            language = "hindi";

        }else if(language.equalsIgnoreCase("en")){
            language ="english";
        }

        try {
            pref = context.getSharedPreferences(PrefUtils.SHARED_PREF, 0);
            firebase_RegId = pref.getString("regId","");

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DashBoardModel dashBoardModel = dataList.get(position);
        holder.view_Click.setCardBackgroundColor(ContextCompat.getColor(context,bgColors[position]));
        holder.menuName.setText(dashBoardModel.getMenuName());
        holder.circleImageView.setImageResource(dashBoardModel.getMenuIcon());

        setAnimation(holder.itemView, position);

        holder.view_Click.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        if(AppUtils.isNetworkConnected(context)) {
                            Intent intent = new Intent(context, WebviewActivty.class);
                            intent.putExtra("url", WebViewURLS.COMPLAIN_FORUM+language);
                            context.startActivity(intent);
                        }else{
                            AppUtils.notify(context,context.getString(R.string.no_internet_title),context.getString(R.string.no_internet_msg));

                        }
                        break;

                    case  1:
                        if(AppUtils.isNetworkConnected(context)) {
                            Intent intent1 = new Intent(context,WebviewActivty.class);
                            intent1.putExtra("url", WebViewURLS.REGISTRATION+language);
                            context.startActivity(intent1);
                            //context.startActivity(new Intent(context, CommitteeRegistrationActivity.class));
                        }else{
                            AppUtils.notify(context,context.getString(R.string.no_internet_title),context.getString(R.string.no_internet_msg));

                        }
                        break;

                    case 2:
                        if(AppUtils.isNetworkConnected(context)) {
                            Intent intent2 = new Intent(context, WebviewActivty.class);
                            intent2.putExtra("url", WebViewURLS.USEFUL_INFO+language);
                            context.startActivity(intent2);
                        }else{
                           AppUtils.notify(context,context.getString(R.string.no_internet_title),context.getString(R.string.no_internet_msg));
                        }
                        break;
                    case 3:
                        Intent intent3 = new Intent(context,Facility.class);
                        //intent2.putExtra("url", WebViewURLS.USEFUL_INFO);
                        context.startActivity(intent3);
                        break;
                    case 4:
                        if(AppUtils.isNetworkConnected(context)) {
                            Intent intent4 = new Intent(context, MediaBulletinActivity.class);
                            //intent2.putExtra("url", WebViewURLS.USEFUL_INFO);
                            context.startActivity(intent4);
                        }else{
                            try {
                                AppUtils.showMessage(v,R.string.no_internet_title,Snackbar.LENGTH_SHORT);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        break;

                    case 5:
                        if(AppUtils.isNetworkConnected(context)) {
                            Intent intent6 = new Intent(context,WebviewActivty.class);
                            intent6.putExtra("url", WebViewURLS.VOICE_OF_PEOPLE+language);
                            context.startActivity(intent6);
                        }else{
                            AppUtils.notify(context,context.getString(R.string.no_internet_title),context.getString(R.string.no_internet_msg));
                        }
                        break;

                    case 6:
                        if(AppUtils.isNetworkConnected(context)) {
                            Intent intent6 = new Intent(context,WebviewActivty.class);
                            intent6.putExtra("url", WebViewURLS.SUGGESTION+language);
                            context.startActivity(intent6);
                        }else{
                            AppUtils.notify(context,context.getString(R.string.no_internet_title),context.getString(R.string.no_internet_msg));
                        }
                        break;

                    case 7:
                        if(AppUtils.isNetworkConnected(context)) {
                            Intent intent7 = new Intent(context,WebviewActivty.class);
                            intent7.putExtra("url", WebViewURLS.JOIN_LOKSAR+language);
                            context.startActivity(intent7);
                        }else{
                            AppUtils.notify(context,context.getString(R.string.no_internet_title),context.getString(R.string.no_internet_msg));
                        }
                        break;




                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    @Override
    public void onViewDetachedFromWindow(DashboardMenuAdapter.MyViewHolder holder) {
        ((DashboardMenuAdapter.MyViewHolder)holder).clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position) {

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView menuName;
        private ImageView circleImageView;
        private CardView view_Click;
        private RelativeLayout rootLayout;

        public MyViewHolder(View view) {
            super(view);
            menuName = (TextView) view.findViewById(R.id.tv_menu_item);
            circleImageView = (ImageView) view.findViewById(R.id.iv_menu);
            view_Click = (CardView)view.findViewById(R.id.view_click);
            rootLayout = (RelativeLayout)view.findViewById(R.id.RL_root);
        }

        public void clearAnimation() {
            rootLayout.clearAnimation();
        }
    }
}
