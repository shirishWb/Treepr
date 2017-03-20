package com.whitebirdtechnology.treepr.Favourite;

import com.whitebirdtechnology.treepr.All_Home_Page.FeedItemAll;

import java.util.ArrayList;

/**
 * Created by dell on 25/2/17.
 */
public class FavoriteSingletonCls {
    private static FavoriteSingletonCls ourInstance = new FavoriteSingletonCls();

    public static FavoriteSingletonCls getInstance() {
        return ourInstance;
    }
    public static void reset() {
        ourInstance = new FavoriteSingletonCls();
    }
    public ArrayList<FeedItemAll> arrayListFav = new ArrayList<>();
    private FavoriteSingletonCls() {
    }
}
