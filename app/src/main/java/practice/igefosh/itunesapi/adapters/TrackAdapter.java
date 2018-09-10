package practice.igefosh.itunesapi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import practice.igefosh.itunesapi.R;
import practice.igefosh.itunesapi.myclasses.Track;

/**
 * Adapter for the ListView that contains songs for the selected album
 */
public class TrackAdapter extends ArrayAdapter<Track> {

    public TrackAdapter(Context context, List<Track> tracks) {
        super(context, 0, tracks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View tracklistView = convertView;
        if (tracklistView == null){
            tracklistView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Track currentTrack = getItem(position);

        TextView name = tracklistView.findViewById(R.id.track);
        name.setText(currentTrack.getTrackName());

        return tracklistView;
    }
}
