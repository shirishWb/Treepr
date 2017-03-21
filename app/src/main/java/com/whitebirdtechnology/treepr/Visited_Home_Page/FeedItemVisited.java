package com.whitebirdtechnology.treepr.Visited_Home_Page;

/**
 * Created by shirish on 14/1/17.
 */

public class FeedItemVisited {
    private String stringImagePath,stringCityName,stringImageName,stringPlaceName,stringCityID,stringPlaceID,stringSpotName,stringSpotId;
    public FeedItemVisited(){

    }

    public FeedItemVisited(String ImagePath, String City, String Info, String ImageName, Boolean aBooleanFavorite, Boolean aBooleanVisited,String CityID,String PlaceID,String PlaceName,String CityName){
        this.stringImagePath = ImagePath;
        this.stringCityName = CityName;
        this.stringPlaceID = PlaceID;
        this.stringCityID =CityID;
        this.stringPlaceName = PlaceName;

        this.stringImageName = ImageName;

    }

    public String getStringSpotId() {
        return stringSpotId;
    }

    public void setStringSpotId(String stringSpotId) {
        this.stringSpotId = stringSpotId;
    }

    public String getStringSpotName() {
        return stringSpotName;
    }

    public void setStringSpotName(String stringSpotName) {
        this.stringSpotName = stringSpotName;
    }

    public String getStringCityName() {
        return stringCityName;
    }

    public String getStringImageName() {
        return stringImageName;
    }

    public String getStringPlaceName() {
        return stringPlaceName;
    }

    public String getStringPlaceID() {
        return stringPlaceID;
    }

    public String getStringCityID() {
        return stringCityID;
    }

    public String getStringImagePath() {
        return stringImagePath;
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

    public void setStringPlaceName(String stringPlaceName) {
        this.stringPlaceName = stringPlaceName;
    }

    public void setStringPlaceID(String stringPlaceID) {
        this.stringPlaceID = stringPlaceID;
    }

    public void setStringCityID(String stringCityID) {
        this.stringCityID = stringCityID;
    }
}
