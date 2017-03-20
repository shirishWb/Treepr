package com.whitebirdtechnology.treepr.Home_Page;

import com.whitebirdtechnology.treepr.All_Home_Page.FeedItemAll;

import java.util.ArrayList;

/**
 * Created by dell on 1/3/17.
 */
public class SingltonClsAll {

    private static SingltonClsAll ourInstance = new SingltonClsAll();
    public static void reset() {
        ourInstance = new SingltonClsAll();
    }

    public static SingltonClsAll getInstance() {
        return ourInstance;
    }

    ArrayList<FeedItemAll> arrayListAll = new ArrayList<>();
    private SingltonClsAll() {
    }
}
