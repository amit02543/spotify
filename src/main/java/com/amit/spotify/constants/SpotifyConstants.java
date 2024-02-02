package com.amit.spotify.constants;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class SpotifyConstants {


    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String EMPTY_STR = "";

    public static final String QUERY = "q";

    public static final String TYPE = "type";

    public static final String QUESTION_MARK = "?";

    public static final String EQUALS_TO = "=";

    public static final String AMPERSAND = "&";

    public static final String COLON = ":";

    public static final String ALBUM = "album";

    public static final String ARTIST = "artist";

    public static final String SONG = "song";

    public static final String TRACK = "track";

    public static final String ITEMS = "items";

    public static final String ZERO = "0";

    public static final String UTC = "UTC";

    public static final String IMAGE_DIMENSIONS = "w_400,h_300,c_pad|w_260,h_200,c_crop";

    public static final String IMAGE_SIGNATURE = "eager=%s&public_id=%s&timestamp=%s%s";

    public static final String DELETE_IMAGE_SIGNATURE = "public_id=%s&timestamp=%s%s";

    public static final String FILE = "file";

    public static final String FORM_DATA = "form-data";

    public static final String API_KEY = "api_key";

    public static final String EAGER = "eager";

    public static final String PUBLIC_ID = "public_id";

    public static final String TIMESTAMP = "timestamp";

    public static final String SIGNATURE = "signature";

    public static final String SECURE_URL = "secure_url";

    public static final List<String> TYPE_LIST = Arrays.asList(ALBUM, ARTIST, TRACK);


}
