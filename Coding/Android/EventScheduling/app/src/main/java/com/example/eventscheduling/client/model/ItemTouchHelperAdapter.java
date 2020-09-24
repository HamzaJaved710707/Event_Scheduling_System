package com.example.eventscheduling.client.model;

public interface ItemTouchHelperAdapter {


    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and <strong>not</strong> at the end of a "drop" event.<br/>
     * <br/>

     */
    boolean onItemMove(int fromPosition, int toPosition);


    /**
     * Called when an item has been dismissed by a swipe.<br/>
     * <br/>
     * adjusting the underlying data to reflect this removal.
     */

    void onItemDismiss(int position);
}
