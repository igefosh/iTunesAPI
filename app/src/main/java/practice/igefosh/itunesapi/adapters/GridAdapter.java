package practice.igefosh.itunesapi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;
import practice.igefosh.itunesapi.R;
import practice.igefosh.itunesapi.myclasses.MusicAlbum;

/**
 * Adapter for the gridView that contains searching result
 */
public class GridAdapter extends ArrayAdapter<MusicAlbum> {

    private Context mContext;

    public GridAdapter(Context context, List<MusicAlbum> albums){
        super(context, 0, albums);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView = convertView;
        if (gridItemView == null){
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
        }

        MusicAlbum currentAlbum = getItem(position);

        ImageView cover = gridItemView.findViewById(R.id.album_cover);
        Picasso.with(mContext).load(currentAlbum.getCover())
                .placeholder(R.drawable.ic_warning_black_48dp)
                .error(R.drawable.ic_error_outline_black_48dp).into(cover);
        cover.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return gridItemView;
    }
}
