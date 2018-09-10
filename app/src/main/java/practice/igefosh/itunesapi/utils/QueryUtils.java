package practice.igefosh.itunesapi.utils;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import practice.igefosh.itunesapi.myclasses.MusicAlbum;
import practice.igefosh.itunesapi.myclasses.Track;

/**
 * Different useful methods for the multiple using
 */
public class QueryUtils {
    private static final String LOG_TAG = "QueryUtils";

    private QueryUtils(){}

    /**
     * generalized method for loading search results
     * @param url search request
     * @return List<MusicAlbum> containing search results
     */
    public static List<MusicAlbum> searchTheAlbums(String url){
        URL albumsUrl = createUrl(url);

        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(albumsUrl);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return parseAlbums(jsonResponse);
    }

    /**
     * generalized method for loading list of songs
     * @param url lookup request
     * @return List<Track> containing lookup results
     */
    public static List<Track> receiveTracklist(String url){
        URL trackUrl = createUrl(url);

        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(trackUrl);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return parseTracks(jsonResponse);
    }

    /**
     * Parses raw json string to the list of albums
     * @param jsonResponse raw json string
     * @return List<MusicAlbum> albums
     */
    private static List<MusicAlbum> parseAlbums(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        List<MusicAlbum> albums = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray jsonAlbumsArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < jsonAlbumsArray.length(); i++){
                JSONObject currentAlbum = jsonAlbumsArray.getJSONObject(i);
                String name = currentAlbum.getString("collectionName");
                String cover = currentAlbum.getString("artworkUrl100");
                String albumId = currentAlbum.getString("collectionId");
                String musicStyle = currentAlbum.getString("primaryGenreName");
                String copyright = currentAlbum.getString("copyright");
                MusicAlbum album = new MusicAlbum(name, cover, albumId, musicStyle, copyright);
                albums.add(album);
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing Albums JSON", e);
        }

        return albums;
    }

    /**
     * Parses raw json string to the list of tracks
     * @param jsonResponse raw json string
     * @return List<Track> tracks
     */
    private static List<Track> parseTracks(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        List<Track> albums = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray jsonAlbumsArray = baseJsonResponse.getJSONArray("results");
            for (int i = 1; i < jsonAlbumsArray.length(); i++){ //starting from 1 because first element is "wrapperType":"collection"
                JSONObject currentAlbum = jsonAlbumsArray.getJSONObject(i);
                String name = currentAlbum.getString("trackName");
                Track track = new Track(name);
                albums.add(track);
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing Albums JSON", e);
        }
        return albums;
    }

    /**
     * Set the connection parameters and handle possible exceptions
     * @param url - url for the request
     * @return JSON response from the given URL
     * @throws IOException can be received when InputStream is closing
     */
    private static String makeHTTPRequest(URL url) throws IOException{
        String jsonResponse = "";

        // if we have a bad url, then jsonResponse will be en empty String
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream stream = null;
        try{
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200){
                stream = connection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + connection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (connection != null){
                connection.disconnect();
            }
            if (stream != null){
                stream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Reads line one by one and builds one result string
     * @param stream input stream
     * @return string containing a response from the input stream
     * @throws IOException may occurs wile readLine()
     */
    private static String readFromStream(InputStream stream) throws IOException{
        StringBuilder result = new StringBuilder();
        if (stream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                result.append(line);
                line = reader.readLine();
            }
        }
        return result.toString();
    }

    /**
     * Returns URL from the giving String
     */
    private static URL createUrl(String url) {
        URL goodUrl = null;
        try{
            goodUrl = new URL(url);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return goodUrl;
    }
}
