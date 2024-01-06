package com.amit.spotify.util;

import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.model.Album;
import com.amit.spotify.model.Artist;
import com.amit.spotify.model.SearchResult;
import com.amit.spotify.model.Track;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchUtil {


    public SearchResult formatSearchResult(JSONArray itemsArray, String type) {

        SearchResult searchResult = new SearchResult();

        for(int i = 0; i < itemsArray.length(); i++) {

            JSONObject jsonObject = itemsArray.getJSONObject(i);

            if(CommonConstants.ALBUM.equals(type)) {
                searchResult.addAlbum(formatAlbumObject(jsonObject));
            } else if(CommonConstants.ARTIST.equals(type)) {
                searchResult.addArtist(formatArtistObject(jsonObject));
            } else if(CommonConstants.TRACK.equals(type)) {
                searchResult.addTrack(formatTrackObject(jsonObject));
            }

        }


        return searchResult;
    }


    private Album formatAlbumObject(JSONObject jsonObject) {

        List<String> artists = new ArrayList<>();

        JSONArray artistsJsonArray = jsonObject.getJSONArray("artists");

        for(int i = 0; i < artistsJsonArray.length(); i++) {
            artists.add(artistsJsonArray.getJSONObject(i).getString("name"));
        }


        Album album = new Album();
        album.setId(jsonObject.getString("id"));
        album.setName(jsonObject.getString("name"));
        album.setArtists(artists);
        album.setReleaseDate(jsonObject.getString("release_date"));
        album.setImageUrl(extractImageUrlFromImages(jsonObject.getJSONArray("images")));
        album.setTotalTracks(jsonObject.getInt("total_tracks"));


        return album;
    }


    private Artist formatArtistObject(JSONObject jsonObject) {

        List<String> genres = new ArrayList<>();

        JSONArray genresJsonArray = jsonObject.getJSONArray("genres");

        for(int i = 0; i < genresJsonArray.length(); i++) {
            genres.add(genresJsonArray.getString(i));
        }


        Artist artist = new Artist();
        artist.setId(jsonObject.getString("id"));
        artist.setName(jsonObject.getString("name"));
        artist.setGenres(genres);
        artist.setFollowers(jsonObject.getJSONObject("followers").getInt("total"));
        artist.setPopularity(jsonObject.getInt("popularity"));
        artist.setImageUrl(extractImageUrlFromImages(jsonObject.getJSONArray("images")));


        return artist;
    }


    private Track formatTrackObject(JSONObject jsonObject) {

        List<String> artists = new ArrayList<>();

        JSONArray artistsJsonArray = jsonObject.getJSONArray("artists");

        for(int i = 0; i < artistsJsonArray.length(); i++) {
            artists.add(artistsJsonArray.getJSONObject(i).getString("name"));
        }



        Track track = new Track();
        track.setId(jsonObject.getString("id"));
        track.setTitle(jsonObject.getString("name"));
        track.setArtists(artists);
        track.setAlbum(jsonObject.getJSONObject("album").getString("name"));
        track.setDuration(formatTrackDuration(jsonObject.getInt("duration_ms")));
        track.setPopularity(jsonObject.getInt("popularity"));
        track.setImageUrl(extractImageUrlFromImages(jsonObject.getJSONObject("album").getJSONArray("images")));
        track.setReleaseDate(jsonObject.getJSONObject("album").getString("release_date"));


        return track;
    }


    private String extractImageUrlFromImages(JSONArray imagesJsonArray) {

        if(!imagesJsonArray.isEmpty()) {
            return imagesJsonArray.getJSONObject(0).getString("url");
        }

        return CommonConstants.EMPTY_STR;
    }


    private String formatTrackDuration(int durationMs) {

        int roundOffDuration = (int) Math.ceil((double) durationMs / 1000);

        int min = roundOffDuration / 60;
        int sec = roundOffDuration % 60;

        String minStr = min < 10 ? CommonConstants.ZERO + min : CommonConstants.EMPTY_STR + min;
        String secStr = sec < 10 ? CommonConstants.ZERO + sec : CommonConstants.EMPTY_STR + sec;

        return minStr + CommonConstants.COLON + secStr;
    }


}
