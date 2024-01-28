package com.amit.spotify.constants;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class SpotifyConstants {


    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String GENERIC_ERROR_MESSAGE = "Something went wrong";

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

    public static final List<String> TYPE_LIST = Arrays.asList(ALBUM, ARTIST, TRACK);


}
