package practice.igefosh.itunesapi.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import practice.igefosh.itunesapi.myclasses.Track;
import practice.igefosh.itunesapi.utils.QueryUtils;

public class TrackLoader extends AsyncTaskLoader<List<Track>> {

    private String mUrl;

    public TrackLoader (Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Track> loadInBackground() {
        if (mUrl == null){
            return null;
        }
        return QueryUtils.receiveTracklist(mUrl);
    }
}
