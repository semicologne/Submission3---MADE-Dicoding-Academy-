package com.example.favoritesapp.view;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.favoritesapp.model.TvShows;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ViewModelTv extends ViewModel {
    private MutableLiveData<ArrayList<TvShows>> listTv = new MutableLiveData<>();
    public String API_KEY = "666a2970fcc09551a95e628f24baf8dd";
    public void setListTv(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShows> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&primary_release_date.gte=2019-08-01";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject response = new JSONObject(result);
                    JSONArray list = response.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tvShows = list.getJSONObject(i);
                        TvShows tvItems = new TvShows(tvShows);
                        listItems.add(tvItems);
                    }
                    listTv.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }
    public void searchTv(String newText){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShows> listItems = new ArrayList<>();
        String url =  "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=en-US&query=" + newText;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject response = new JSONObject(result);
                    JSONArray list = response.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++){
                        JSONObject tvShows = list.getJSONObject(i);
                        TvShows tvItems = new TvShows(tvShows);
                        listItems.add(tvItems);
                    }
                    listTv.postValue(listItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public LiveData<ArrayList<TvShows>> getTv(){
        return listTv;
    }
}
