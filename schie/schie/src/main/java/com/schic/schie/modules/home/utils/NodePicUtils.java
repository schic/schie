/**
 *
 */
package com.schic.schie.modules.home.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public final class NodePicUtils {

    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue();
    private static ConcurrentLinkedQueue<String> queueUsed = new ConcurrentLinkedQueue();

    static {
        queue.add("ion-android-calendar");
        queue.add("ion-android-hand");
        queue.add("ion-android-send");
        queue.add("ion-android-share");
        queue.add("ion-bookmark");
        queue.add("ion-connection-bars");
        queue.add("ion-contrast");
        queue.add("ion-happy");
        queue.add("ion-heart");
        queue.add("ion-help-buoy");
        queue.add("ion-leaf");
        queue.add("ion-pie-graph");
        queue.add("ion-stats-bars");
    }

    private NodePicUtils() {
        //
    }

    public static String get() {
        if (queue.isEmpty()) {
            queue.addAll(queueUsed.stream().collect(Collectors.toList()));
            queueUsed.clear();
        }
        String item = queue.remove();
        queueUsed.add(item);
        return item;
    }
}
