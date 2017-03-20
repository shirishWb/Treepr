package com.whitebirdtechnology.treepr.Home_Page;

import com.whitebirdtechnology.treepr.Visited_Home_Page.FeedItemVisited;

import java.util.ArrayList;

/**
 * Created by dell on 1/3/17.
 */
public class SingltonClsVisited {
    private static SingltonClsVisited ourInstance = new SingltonClsVisited();

    public static SingltonClsVisited getInstance() {
        return ourInstance;
    }
    public static void reset() {
        ourInstance = new SingltonClsVisited();
    }

    ArrayList<FeedItemVisited> arrayListVisited = new ArrayList<>();
    private SingltonClsVisited() {
    }
}
