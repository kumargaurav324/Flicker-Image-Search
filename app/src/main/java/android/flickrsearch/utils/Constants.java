package android.flickrsearch.utils;

public class Constants {
    private static final String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";
    public static final String QUERY_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=" + API_KEY + "& format=json&nojsoncallback=1&safe_search=1";
    public static final String QUERY_TEXT = "text";
    public static final String QUERY_PAGE = "page";
    public static final String EQUALS = "=";
    public static final String AND = "&";
    public static final String GET_REQUEST = "GET";
    public static final int MAX_SIZE = 2000;
    public static final String PUBLIC = "Public";
    public static final String FRIEND = "Friend";
    public static final String FAMILY = "Family";

    public static final String KEY_STATUS = "stat";
    public static final String OK = "ok";
    public static final String KEY_ID = "id";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_SECRET = "secret";
    public static final String KEY_SERVER = "server";
    public static final String KEY_FARM = "farm";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ISPUBLIC = "ispublic";
    public static final String KEY_ISFRIEND = "isfriend";
    public static final String KEY_ISFAMILY = "isfamily";
    public static final String KEY_PHOTOS = "photos";
    public static final String KEY_PAGE = "page";
    public static final String KEY_PAGES = "pages";
    public static final String KEY_PHOTO = "photo";

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;
}
