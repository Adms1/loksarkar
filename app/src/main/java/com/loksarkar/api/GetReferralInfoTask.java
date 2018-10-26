package com.loksarkar.api;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.loksarkar.models.ReferralModel;

import org.json.JSONObject;

import java.util.HashMap;

public class GetReferralInfoTask extends AsyncTask<Void,Void,ReferralModel> {

    private String responseString;


    public GetReferralInfoTask() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ReferralModel doInBackground(Void... voids) {
        ReferralModel referralModel = null;
        boolean success = false;
        try {
            responseString = WebServicesCall.RunScript(AppConfiguration.BASE_URL+"ReferralDetail",0);

            Gson gson = new Gson();
            referralModel = gson.fromJson(responseString,ReferralModel.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return referralModel;
    }

    @Override
    public void onPostExecute(ReferralModel referralModel){
        super.onPostExecute(referralModel);
    }
}
