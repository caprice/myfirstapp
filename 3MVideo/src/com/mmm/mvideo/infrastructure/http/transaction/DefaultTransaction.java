/*
 * ky
 */
package com.mmm.mvideo.infrastructure.http.transaction;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.util.Log;

import com.mmm.mvideo.infrastructure.http.clienthelper.HttpDelegate;
import com.mmm.mvideo.infrastructure.http.request.RequestParsable;
import com.mmm.mvideo.infrastructure.http.response.ResponseParsable;

// TODO: Auto-generated Javadoc
/**
 * Default implementation of Transaction.
 * @author a37wczz
 * 
 */
public class DefaultTransaction implements Transaction {

    /** status of transaction. */
    protected int status = Transaction.STATUS_READY;

    /** timeout. */
    public int timeout = 10000;

    /** HTTP Client delegate. */
    protected final HttpDelegate httpDelegate = HttpDelegate.getInstance();

    /** task. */
    protected Future<String> future;

    /** request. */
    protected RequestParsable requestObj;

    /** response. */
    protected ResponseParsable responseObj;

    /**
     * Instantiates a new base transaction.
     * 
     * @param requestObj
     *            the request obj
     * @param responseObj
     *            the response obj
     */
    public DefaultTransaction(RequestParsable requestObj,
            ResponseParsable responseObj) {
        super();
        this.requestObj = requestObj;
        this.responseObj = responseObj;
    }

    /**
     * Instantiates a new base transaction.
     */
    public DefaultTransaction() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.yisheng.epayment.http.IExecuteable#execute()
     */
    public final void execute() {
        String url = requestObj.getProtocol() + "?"
                + requestObj.getParamString();
        changeStatus(Transaction.STATUS_RUNNING);
        String resStr = executeInTimeLimited(timeout, url);
        if (status == Transaction.STATUS_EXECUTED) {
            responseObj.parseResponse(resStr);
            changeStatus(Transaction.STATUS_OK);
        }
    }

    /**
     * Sets the request.
     * 
     * @param requestObj
     *            the new request
     */
    public final void setRequest(RequestParsable requestObj) {
        if (status != Transaction.STATUS_READY) {
            return;
        }
        this.requestObj = requestObj;
    }

    /**
     * Gets the request.
     * 
     * @return the request
     */
    public RequestParsable getRequest() {
        return requestObj;
    }

    /**
     * Sets the response.
     * 
     * @param responseObj
     *            the new response
     */
    public void setResponse(ResponseParsable responseObj) {
        this.responseObj = responseObj;
    }

    /**
     * Gets the response.
     * 
     * @return the response
     */
    public ResponseParsable getResponse() {
        return responseObj;
    }

    /**
     * Gets the status.
     * 
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Execute in time limited.
     * 
     * @param timeout
     *            the timeout
     * @param url
     *            the url
     * @return the string
     */
    protected final String executeInTimeLimited(int timeout, final String url) {
        String result = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final HttpDelegate _d = httpDelegate;
        future = executor.submit(new Callable<String>() {
            public String call() {
                String result = null;
                try {
                    result = _d.get(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        });
        try {
            result = future.get(timeout, TimeUnit.MILLISECONDS);
            changeStatus(Transaction.STATUS_EXECUTED);
        } catch (InterruptedException e) {
            future.cancel(true);
            changeStatus(Transaction.STATUS_CANCELED);
            // throw new Exception(e.getMessage());
        } catch (ExecutionException e) {
            future.cancel(true);
            changeStatus(Transaction.STATUS_ERROR);
            // throw new Exception(e.getMessage());
        } catch (TimeoutException e) {
            future.cancel(true);
            changeStatus(Transaction.STATUS_TIMEOUT);
            // throw new Exception(e.getMessage());
        } catch (CancellationException e) {
            future.cancel(true);
            changeStatus(Transaction.STATUS_CANCELED);
        } catch (Exception e) {
            future.cancel(true);
            changeStatus(Transaction.STATUS_ERROR);
        } finally {
            future = null;
            executor.shutdown();
            executor = null;
        }
        if (result == null && status == Transaction.STATUS_EXECUTED) {
            changeStatus(Transaction.STATUS_NETWORK_ERROR);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.yisheng.epayment.http.IExecuteable#cancel()
     */
    @Override
    public final void cancel() {
        if (status == Transaction.STATUS_RUNNING) {
            if (future != null && !future.isDone()) {
                future.cancel(true);
                changeStatus(Transaction.STATUS_CANCELED);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.yisheng.epayment.http.IExecuteable#reset()
     */
    @Override
    public final boolean reset() {
        if (status != Transaction.STATUS_RUNNING) {
            status = Transaction.STATUS_READY;
            future = null;
            requestObj = null;
            responseObj = null;
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.yisheng.epayment.http.Transaction#resetAnyway()
     */
    @Override
    public final void forceToReset() {
        cancel();
        reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.yisheng.epayment.http.Transaction#executeAsyn()
     */
    @Override
    public final synchronized void executeAsyn(final Callback callback) {
        if (status == Transaction.STATUS_RUNNING) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                execute();
                callback.callback(status);
            }
        }).start();

    }

    /**
     * Change status.
     * 
     * @param newStatus
     *            the new status
     */
    private void changeStatus(int newStatus) {
        status = newStatus;
        Log.d("DefaultTransaction", "Status: " + status);

    }
}
