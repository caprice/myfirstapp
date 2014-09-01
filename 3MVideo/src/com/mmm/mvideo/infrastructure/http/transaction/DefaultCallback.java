/*
 * ky
 */
package com.mmm.mvideo.infrastructure.http.transaction;

import android.content.Context;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * Default implementation of Callback. User can extend it and override the
 * onOK() method
 * @author a37wczz
 */
public class DefaultCallback extends Callback {

    /** The context. */
    private Context context;

    /**
     * Instantiates a new base callback.
     */
    public DefaultCallback() {
        super();
    }

    /**
     * Instantiates a new base callback.
     * 
     * @param context
     *            the context
     */
    public DefaultCallback(Context context) {
        super();
        this.context = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.yisheng.epayment.http.callback.Callback#onOk()
     */
    @Override
    public void onOK() {
        super.onOK();
        Toast.makeText(context, "OK!", Toast.LENGTH_SHORT).show();
    };

    /*
     * (non-Javadoc)
     * 
     * @see com.yisheng.epayment.http.callback.Callback#onTimeout()
     */
    @Override
    public void onTimeout() {
        super.onTimeout();
        Toast.makeText(context, "Time out!", Toast.LENGTH_SHORT).show();
    };

    /*
     * (non-Javadoc)
     * 
     * @see com.yisheng.epayment.http.callback.Callback#onNetworkError()
     */
    @Override
    public void onNetworkError() {
        super.onNetworkError();
        Toast.makeText(context, "No access to the network!", Toast.LENGTH_SHORT)
                .show();
    };
}
