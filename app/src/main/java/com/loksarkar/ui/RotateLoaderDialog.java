package com.loksarkar.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.Window;

import com.loksarkar.R;
public class RotateLoaderDialog extends Dialog {

    private Dialog dialog;
    private RotateLoading rotateLoading;


    public RotateLoaderDialog(@NonNull Context context) {
        super(context);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loader);
        rotateLoading = (RotateLoading)dialog.findViewById(R.id.rotate_loader);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }


    public void showLoader(){
        try {
            if (dialog != null) {
                if (rotateLoading.isStart()) {
                    rotateLoading.stop();
                }else{
                    rotateLoading.start();
                }

                dialog.show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void dismissLoader(){
        try {
            if (dialog != null) {
                dialog.dismiss();
                if(rotateLoading != null && rotateLoading.isShown()){
                    rotateLoading.stop();

                }else{
                    rotateLoading.stop();
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
