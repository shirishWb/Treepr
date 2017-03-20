package com.whitebirdtechnology.treepr.Stories_Home_page;

/**
 * Created by shirish on 14/1/17.
 */

public class FeedItemStories {
    private String stringImagePath,stringCityName,stringInfo,stringImageName,stringPlaceName,stringCityID,stringPlaceID,stringPrfImgPath,stringStatus;
    public FeedItemStories(){

    }

    public FeedItemStories(String ImagePath, String City, String Info, String ImageName,String CityID,String PlaceID,String PlaceName,String CityName){
        this.stringImagePath = ImagePath;
        this.stringCityName = CityName;
        this.stringPlaceID = PlaceID;
        this.stringCityID =CityID;
        this.stringPlaceName = PlaceName;
        this.stringInfo = Info;
        this.stringImageName = ImageName;
    }

    public String getStringPrfImgPath() {
        return stringPrfImgPath;
    }

    public void setStringPrfImgPath(String stringPrfImgPath) {
        this.stringPrfImgPath = stringPrfImgPath;
    }

    public String getStringStatus() {
        return stringStatus;
    }

    public void setStringStatus(String stringStatus) {
        this.stringStatus = stringStatus;
    }

    public String getStringCityName() {
        return stringCityName;
    }

    public String getStringImageName() {
        return stringImageName;
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
