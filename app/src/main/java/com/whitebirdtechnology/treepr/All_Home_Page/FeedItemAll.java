package com.whitebirdtechnology.treepr.All_Home_Page;

/**
 * Created by shirish on 14/1/17.
 */

public class FeedItemAll {
    private String stringImagePath,stringInfo,stringImageName,stringCityName,stringPlaceName,stringCityID,stringPlaceID,stringSpotId;
    private Boolean aBooleanFavorite,aBooleanVisited;
    public FeedItemAll(){

    }

    public FeedItemAll(String ImagePath,String CityID,String PlaceID,String PlaceName,String CityName,String Info,String ImageName,Boolean aBooleanFavorite,Boolean aBooleanVisited,String stringSpotId){
        this.stringImagePath = ImagePath;
        this.stringInfo = Info;
        this.stringCityName = CityName;
        this.stringPlaceID = PlaceID;
        this.stringCityID =CityID;
        this.stringSpotId = stringSpotId;
        this.stringPlaceName = PlaceName;
        this.stringImageName = ImageName;
        this.aBooleanFavorite =aBooleanFavorite;
        this.aBooleanVisited =aBooleanVisited;
    }

    public Boolean getaBooleanFavorite() {
        return aBooleanFavorite;
    }

    public String getStringSpotId() {
        return stringSpotId;
    }

    public String getStringCityID() {
        return stringCityID;
    }

    public String getStringPlaceID() {
        return stringPlaceID;
    }

    public String getStringPlaceName() {
        return stringPlaceName;
    }

    public Boolean getaBooleanVisited() {
        return aBooleanVisited;
    }

    public String getStringCityName() {
        return stringCityName;
    }

    public String getStringImageName() {
        return stringImageName;
    }

    public String getStringImagePath() {
        return stringImagePath;
    }

    public String getStringInfo() {
        return stringInfo;
    }

    public void setStringCityName(String stringCityName) {
        this.stringCityName = stringCityName;
    }

    public void setStringImageName(String stringImageName) {
        this.stringImageName = stringImageName;
    }

    public void setStringImagePath(String stringImagePath) {
        this.stringImagePath = stringImagePath;
    }

    public void setStringInfo(String stringInfo) {
        this.stringInfo = stringInfo;
    }

    public void setaBooleanFavorite(Boolean aBooleanFavorite) {
        this.aBooleanFavorite = aBooleanFavorite;
    }

    public void setaBooleanVisited(Boolean aBooleanVisited) {
        this.aBooleanVisited = aBooleanVisited;
    }

    public void setStringSpotId(String stringSpotId) {
        this.stringSpotId = stringSpotId;
    }

    public void setStringCityID(String stringCityID) {
        this.stringCityID = stringCityID;
    }

    public void setStringPlaceID(String stringPlaceID) {
        this.stringPlaceID = stringPlaceID;
    }

    public void setStringPlaceName(String stringPlaceName) {
        this.stringPlaceName = stringPlaceName;
    }
}
