package com.epicodus.bowloregon.util;

/**
 * Created by Guest on 5/13/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
