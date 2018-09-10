package practice.igefosh.itunesapi;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import practice.igefosh.itunesapi.adapters.TrackAdapter;
import practice.igefosh.itunesapi.loaders.TrackLoader;
import practice.igefosh.itunesapi.myclasses.Track;
import practice.igefosh.itunesapi.utils.Constants;

public class AlbumActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Track>>{

    private TrackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        //Setting album information
        TextView albumName = findViewById(R.id.album_name_textview);
        albumName.setText(getIntent().getExtras().getString("name"));

        TextView style = findViewById(R.id.style_textview);
        style.setText(getIntent().getExtras().getString("style"));

        TextView copyright = findViewById(R.id.copyright_textview);
        copyright.setText(getIntent().getExtras().getString("copyright"));

        ListView tracklistView = findViewById(R.id._songlist);
        adapter = new TrackAdapter(this, new ArrayList<Track>());
        tracklistView.setAdapter(adapter);

        ImageView albumCover = findViewById(R.id.cover_album_activity);
        albumCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(getApplicationContext()).load(getIntent().getExtras()
                .getString("cover")).into(albumCover);

        //check is network active
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //run the loaders if the network is active
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(Constants.TRACK_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Track>> onCreateLoader(int id, Bundle args) {

        //set the url for the lookup request
        Uri baseUri = Uri.parse(Constants.BASE_LOOKUP_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("media", "music");
        uriBuilder.appendQueryParameter("entity", "song");
        uriBuilder.appendQueryParameter("country", "RU");
        uriBuilder.appendQueryParameter("id", getIntent().getExtras().getString("albumId"));
        return new TrackLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Track>> loader, List<Track> data) {
        adapter.clear();

        //if everything fine, give the data to the adapter
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Track>> loader) {
        adapter.clear();
    }
}
