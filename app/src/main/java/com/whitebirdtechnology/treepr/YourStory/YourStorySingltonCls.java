package com.whitebirdtechnology.treepr.YourStory;


import com.whitebirdtechnology.treepr.Stories_Home_page.FeedItemStories;

import java.util.ArrayList;

/**
 * Created by dell on 25/2/17.
 */
public class YourStorySingltonCls {
    private static YourStorySingltonCls ourInstance = new YourStorySingltonCls();

    public static YourStorySingltonCls getInstance() {
        return ourInstance;
    }
    public static void reset() {
        ourInstance = new YourStorySingltonCls();
    }
    public ArrayList<FeedItemStories> arrayListYourStory = new ArrayList<>();
    private YourStorySingltonCls() {
    }
}
