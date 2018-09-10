package practice.igefosh.itunesapi;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import practice.igefosh.itunesapi.adapters.GridAdapter;
import practice.igefosh.itunesapi.loaders.AlbumLoader;
import practice.igefosh.itunesapi.myclasses.MusicAlbum;
import practice.igefosh.itunesapi.utils.Constants;

public class SearchableActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MusicAlbum>> {

    private GridAdapter albumAdapter;
    private TextView emptyStateTextView; //if there is no internet connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        GridView gridView = findViewById(R.id._grid_v);
        emptyStateTextView = findViewById(R.id.empty_view);
        gridView.setEmptyView(emptyStateTextView);

        albumAdapter = new GridAdapter(this, new ArrayList<MusicAlbum>());
        gridView.setAdapter(albumAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                MusicAlbum currentAlbum = albumAdapter.getItem(position);

                //make intent with all information that could need for the AlbumActivity
                Intent startMusicAlbumActivity = new Intent(SearchableActivity.this, AlbumActivity.class);
                startMusicAlbumActivity.putExtra("albumId", currentAlbum.getAlbumId());
                startMusicAlbumActivity.putExtra("cover", currentAlbum.getCover());
                startMusicAlbumActivity.putExtra("name", currentAlbum.getAlbumName());
                startMusicAlbumActivity.putExtra("copyright", currentAlbum.getCopyright());
                startMusicAlbumActivity.putExtra("style", currentAlbum.getMusicStyle());
                // Send the intent to launch a new activity
                startActivity(startMusicAlbumActivity);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //start loader, or show 'no internet' warning
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(Constants.ALBUM_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet);
        }

    }

    @Override
    public Loader<List<MusicAlbum>> onCreateLoader(int id, Bundle args) {

        //set the url for the search request
        Uri baseUri = Uri.parse(Constants.BASE_SEARCH_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("entity", "album");
        uriBuilder.appendQueryParameter("country", "RU");
        uriBuilder.appendQueryParameter("attribute", "artistTerm");
        uriBuilder.appendQueryParameter("term", getIntent().getStringExtra(SearchManager.QUERY));
        return new AlbumLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<MusicAlbum>> loader, List<MusicAlbum> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous data
        albumAdapter.clear();

        // If there is a valid list of MusicAlbum, then add them to the adapter's data set.
        if (data != null && !data.isEmpty()) {
            albumAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MusicAlbum>> loader) {
        albumAdapter.clear();

    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_button) {
            onSearchRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
