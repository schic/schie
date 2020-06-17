/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

public class QueuingSwingWorker<T, V> extends SwingWorker<T, V> {

    private static Map<String, WorkerInfo> workerInfoMap = new ConcurrentHashMap<String, WorkerInfo>();
    private static Logger logger = Logger.getLogger(QueuingSwingWorker.class);

    private QueuingSwingWorkerTask<T, V> task;
    private String workingId;

    private enum WorkerState {
        IDLE, WORKING, QUEUED
    }

    private static class WorkerInfo {
        public WorkerState workerState = WorkerState.IDLE;
    }

    public QueuingSwingWorker(QueuingSwingWorkerTask<T, V> task, boolean queue) {
        this.task = task;
        WorkerInfo workerInfo;

        workerInfo = workerInfoMap.get(task.getKey());
        if (workerInfo == null) {
            /*
             * Even though the map is concurrent, we need to synchronize here to ensure that two
             * different threads don't try to put a new WorkerInfo object in at the same time. For a
             * particular key, a new WorkerInfo object will be created and stored only once, and
             * kept in the map thereafter.
             */
            synchronized (workerInfoMap) {
                workerInfo = workerInfoMap.get(task.getKey());
                if (workerInfo == null) {
                    workerInfo = new WorkerInfo();
                    workerInfoMap.put(task.getKey(), workerInfo);
                }
            }
        }

        // Whenever we read or update the worker state, we first synchronize on the WorkerInfo specific to the given key. 
        synchronized (workerInfo) {
            logger.debug(Messages.getString("QueuingSwingWorker.0") + task.getKey() + Messages.getString("QueuingSwingWorker.1") + queue + Messages.getString("QueuingSwingWorker.2") + workerInfo.workerState); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            // If a different worker is already executing this task, do not create a SwingWorker
            if (workerInfo.workerState != WorkerState.IDLE) {
                /*
                 * However if queue is true, then we set the worker status to QUEUED, indicating
                 * that after the current worker finishes, it should fire off another one.
                 */
                if (queue) {
                    workerInfo.workerState = WorkerState.QUEUED;
                }
                return;
            }

            // If no other worker is currently executing this task, we're safe to execute it now
            workerInfo.workerState = WorkerState.WORKING;
            logger.debug(Messages.getString("QueuingSwingWorker.3") + task.getKey() + Messages.getString("QueuingSwingWorker.4") + queue + Messages.getString("QueuingSwingWorker.5") + workerInfo.workerState); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        workingId = PlatformUI.MIRTH_FRAME.startWorking(task.getDisplayText());
        task.setWorker(this);
    }

    protected final void publishDelegate(V... chunks) {
        logger.debug(Messages.getString("QueuingSwingWorker.6") + task.getKey()); //$NON-NLS-1$
        publish(chunks);
    }

    public final void executeDelegate() {
        // Only execute the worker if a working ID was created
        if (workingId != null) {
            execute();
        }
    }

    protected T doInBackground() throws Exception {
        logger.debug(Messages.getString("QueuingSwingWorker.7") + task.getKey()); //$NON-NLS-1$
        return task.doInBackground();
    }

    protected void process(List<V> chunks) {
        logger.debug(Messages.getString("QueuingSwingWorker.8") + task.getKey()); //$NON-NLS-1$
        task.process(chunks);
    }

    protected void done() {
        logger.debug(Messages.getString("QueuingSwingWorker.9") + task.getKey()); //$NON-NLS-1$
        task.done();

        PlatformUI.MIRTH_FRAME.stopWorking(workingId);

        /*
         * We don't need to synchronize here because there's no chance of a different thread
         * updating the value. That only happens the first time when the WorkerInfo object is
         * created.
         */
        WorkerInfo workerInfo = workerInfoMap.get(task.getKey());
        boolean queued = false;

        // Again here, synchronize before doing anything with the worker count
        synchronized (workerInfo) {
            logger.debug(Messages.getString("QueuingSwingWorker.10") + task.getKey() + Messages.getString("QueuingSwingWorker.11") + workerInfo.workerState); //$NON-NLS-1$ //$NON-NLS-2$

            // Detect if a task was previously queued up
            if (workerInfo.workerState == WorkerState.QUEUED) {
                queued = true;
            }

            // Reset the worker state since this one is now done
            workerInfo.workerState = WorkerState.IDLE;
            logger.debug(Messages.getString("QueuingSwingWorker.12") + task.getKey() + Messages.getString("QueuingSwingWorker.13") + workerInfo.workerState); //$NON-NLS-1$ //$NON-NLS-2$
        }

        /*
         * If a task was queued up, execute the new worker. This will be the same as the current
         * worker (uses the same task with the same overridden methods), except that if a task is
         * already queued/working by the time it obtains the synchronization lock, it does not queue
         * up another one.
         */
        if (queued) {
            logger.debug(Messages.getString("QueuingSwingWorker.14") + task.getKey()); //$NON-NLS-1$
            new QueuingSwingWorker<T, V>(task, false).executeDelegate();
        }
    }
}