/**
 *
 */
package com.schic.schie.modules.home.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public final class NodeBgUtils {

    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue();
    private static ConcurrentLinkedQueue<String> queueUsed = new ConcurrentLinkedQueue();

    static {
        queue.add("bg-yellow");
        queue.add("bg-aqua");
        queue.add("bg-blue");
        queue.add("bg-green");
        queue.add("bg-teal");
        queue.add("bg-olive");
        queue.add("bg-fuchsia");
        queue.add("bg-purple");
    }

    private NodeBgUtils() {
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
