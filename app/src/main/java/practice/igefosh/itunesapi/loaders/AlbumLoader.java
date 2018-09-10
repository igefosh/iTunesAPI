package practice.igefosh.itunesapi.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import practice.igefosh.itunesapi.myclasses.MusicAlbum;
import practice.igefosh.itunesapi.utils.QueryUtils;

/**
 * Loads search results and sorts it alphabetically
 */
public class AlbumLoader extends AsyncTaskLoader<List<MusicAlbum>> {

    private String mUrl;

    public AlbumLoader (Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MusicAlbum> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        List<MusicAlbum> albums = QueryUtils.searchTheAlbums(mUrl);

        Collections.sort(albums, new Comparator<MusicAlbum>() {
            @Override
            public int compare(MusicAlbum o1, MusicAlbum o2) {
                return o1.getAlbumName().compareTo(o2.getAlbumName()); //sorts the results
            }
        });
        return albums;
    }
}
