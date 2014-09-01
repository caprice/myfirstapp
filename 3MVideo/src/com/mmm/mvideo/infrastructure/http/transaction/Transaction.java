/*
 * ky
 */
package com.mmm.mvideo.infrastructure.http.transaction;

// TODO: Auto-generated Javadoc
/**
 * The Interface Transaction.
 * @author a37wczz
 */
public interface Transaction {

    /** status ready. */
    int STATUS_READY = 0;

    /** status running. */
    int STATUS_RUNNING = 1;

    /** status executed. */
    int STATUS_EXECUTED = 2;

    /** status OK. */
    int STATUS_OK = 3;

    /** status network error. */
    int STATUS_NETWORK_ERROR = 4;

    /** status canceled. */
    int STATUS_CANCELED = 5;

    /** status error. */
    int STATUS_ERROR = 6;

    /** status timeout. */
    int STATUS_TIMEOUT = 7;

    /**
     * execute.
     */
    public void execute();

    /**
     * cancel current task.
     */
    public void cancel();

    /**
     * reset the transaction.
     * 
     * @return true, if success。 false, if failed (task is running)
     */
    public boolean reset();

    /**
     * force to cancel current task and reset the transaction.
     */
    public void forceToReset();

    /**
     * asyn execute.
     * 
     * @param callback
     *            Callback
     */
    void executeAsyn(Callback callback);
}
