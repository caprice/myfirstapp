package com.mmm.mvideo.infrastructure.http.clienthelper;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class MyX509TrustManager.
 * @author a37wczz
 */
public class MyX509TrustManager implements javax.net.ssl.X509TrustManager {

    /** The standard trust manager. */
    private MyX509TrustManager standardTrustManager = null;

    /**
     * Instantiates a new my x509 trust manager.
     * 
     * @param keystore
     *            the keystore
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     * @throws KeyStoreException
     *             the key store exception
     */
    public MyX509TrustManager(KeyStore keystore)
            throws NoSuchAlgorithmException, KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keystore);
        TrustManager[] trustmanagers = factory.getTrustManagers();
        if (trustmanagers.length == 0) {
            throw new NoSuchAlgorithmException("no trust manager found");
        }
        this.standardTrustManager = (MyX509TrustManager) trustmanagers[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.
     * X509Certificate[], java.lang.String)
     */
    public void checkClientTrusted(X509Certificate[] certificates,
            String authType) throws CertificateException {
        standardTrustManager.checkClientTrusted(certificates, authType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.
     * X509Certificate[], java.lang.String)
     */
    public void checkServerTrusted(X509Certificate[] certificates,
            String authType) throws CertificateException {
        if ((certificates != null) && (certificates.length == 1)) {
            certificates[0].checkValidity();
        } else {
            standardTrustManager.checkServerTrusted(certificates, authType);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    public X509Certificate[] getAcceptedIssuers() {
        return this.standardTrustManager.getAcceptedIssuers();
    }

}
