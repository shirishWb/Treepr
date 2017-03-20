package com.whitebirdtechnology.treepr.Home_Page;

import com.whitebirdtechnology.treepr.Stories_Home_page.FeedItemStories;

import java.util.ArrayList;

/**
 * Created by dell on 1/3/17.
 */
public class SingltonClsStory {
    private static SingltonClsStory ourInstance = new SingltonClsStory();

    public static SingltonClsStory getInstance() {
        return ourInstance;
    }
    public static void reset() {
        ourInstance = new SingltonClsStory();
    }

    ArrayList<FeedItemStories> arrayListStory = new ArrayList<>();
    private SingltonClsStory() {
    }
}
