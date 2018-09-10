package practice.igefosh.itunesapi.myclasses;

/**
 * This class represents a single Music Album
 */
public class MusicAlbum {

    private String mAlbumName; //for sorting list of albums alphabetically
    private String mCover; //Albums artwork
    private String mAlbumId; //for getting more detailed result like a list of songs
    private String mMusicStyle; //information about the music style
    private String mCopyright; //information about the Copyright

    public MusicAlbum(String name, String cover, String albumId, String style, String copyright){
        mAlbumName = name;
        mCover = cover;
        mAlbumId = albumId;
        mMusicStyle = style;
        mCopyright = copyright;

    }

    public String getCover() {
        return mCover;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public String getAlbumId() {
        return mAlbumId;
    }

    public String getMusicStyle() {
        return mMusicStyle;
    }

    public String getCopyright() {
        return mCopyright;
    }
}
