package com.mmm.mvideo.infrastructure.http.clienthelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * Delegate of HttpCilent.
 * @author a37wczz
 */
public class HttpDelegate {

    /** The Constant TAG. */
    private static final String TAG = "HttpDelegate";

    /** The instance. */
    private static HttpDelegate instance;

    /** The http client. */
    private HttpClient httpClient;

    // private HttpClient httpsClient;

    /**
     * Instantiates a new http delegate.
     */
    private HttpDelegate() {
        super();
        HttpParams params = new BasicHttpParams();
        // some base param
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpProtocolParams
                .setUserAgent(
                        params,
                        "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        // set timeout
        /* timeout of taking connection from pool */
        ConnManagerParams.setTimeout(params, 3 * 1000);
        /* timeout of connection */
        HttpConnectionParams.setConnectionTimeout(params, 60 * 1000);
        /* timeout of request */
        HttpConnectionParams.setSoTimeout(params, 120 * 1000);

        // support http and https model
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        schReg.register(new Scheme("https", new MySSLSocketFactory(), 443));
        schReg.register(new Scheme("https", new MySSLSocketFactory(), 8443));

        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                params, schReg);
        httpClient = new DefaultHttpClient(conMgr, params);
    }

    /**
     * Gets the single instance of HttpDelegate.
     * 
     * @return single instance of HttpDelegate
     */
    public static synchronized HttpDelegate getInstance() {
        if (instance == null) {
            instance = new HttpDelegate();
        }
        return instance;
    }

    /**
     * Send request through HTTP GET .
     * 
     * @param url
     *            the url
     * @return the string
     * @throws Exception
     *             the exception
     */
    public String get(String url) throws Exception {
        Log.d(TAG, "Receive request url: " + url);
        HttpGet get = new HttpGet(url);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                br = new BufferedReader(new InputStreamReader(
                        entity.getContent()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Http request error: " + e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                br.close();
            }
        }

        // try {
        // Thread.sleep(10000);
        // } catch (Exception e) {
        // // TODO: handle exception
        // }

        Log.d(TAG, "Response String: " + sb.toString());
        return sb.toString();
    }
}
