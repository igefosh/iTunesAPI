package practice.igefosh.itunesapi.utils;

// contains base url
public final class Constants {
   public static String BASE_SEARCH_URL = "https://itunes.apple.com/search";
    public static String BASE_LOOKUP_URL = "https://itunes.apple.com/lookup";

    public static final int ALBUM_LOADER_ID = 1;
    public static final int TRACK_LOADER_ID = 2;


    /* private constructor overrides default so no one can create an instance of this class.
    *  access to the variables allowed only through the class name*/
    private Constants(){}
}
