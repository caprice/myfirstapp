package com.mmm.mvideo.infrastructure.http.clienthelper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating MySSLSocket objects.
 * @author a37wczz
 */
public class MySSLSocketFactory implements LayeredSocketFactory, SocketFactory {

    /** The sslcontext. */
    private SSLContext sslcontext = null;

    /**
     * Creates a new MySSLSocket object.
     * 
     * @return the SSL context
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static SSLContext createEasySSLContext() throws IOException {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null,
                    new TrustManager[] { new MyX509TrustManager(null) }, null);
            return context;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Gets the sSL context.
     * 
     * @return the sSL context
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private SSLContext getSSLContext() throws IOException {
        if (this.sslcontext == null) {
            this.sslcontext = createEasySSLContext();
        }
        return this.sslcontext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.http.conn.scheme.SocketFactory#connectSocket(java.net.Socket,
     * java.lang.String, int, java.net.InetAddress, int,
     * org.apache.http.params.HttpParams)
     */
    public Socket connectSocket(Socket sock, String host, int port,
            InetAddress localAddress, int localPort, HttpParams params)
            throws IOException, UnknownHostException, ConnectTimeoutException {
        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        int soTimeout = HttpConnectionParams.getSoTimeout(params);

        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

        if ((localAddress != null) || (localPort > 0)) {
            // we need to bind explicitly
            if (localPort < 0) {
                localPort = 0; // indicates "any"
            }
            InetSocketAddress isa = new InetSocketAddress(localAddress,
                    localPort);
            sslsock.bind(isa);
        }

        sslsock.connect(remoteAddress, connTimeout);
        sslsock.setSoTimeout(soTimeout);
        return sslsock;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.http.conn.scheme.SocketFactory#createSocket()
     */
    public Socket createSocket() throws IOException {
        return getSSLContext().getSocketFactory().createSocket();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.http.conn.scheme.SocketFactory#isSecure(java.net.Socket)
     */
    public boolean isSecure(Socket socket) throws IllegalArgumentException {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.http.conn.scheme.LayeredSocketFactory#createSocket(java.net
     * .Socket, java.lang.String, int, boolean)
     */
    public Socket createSocket(Socket socket, String host, int port,
            boolean autoClose) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket, host,
                port, autoClose);
    }

    // -------------------------------------------------------------------
    // javadoc in org.apache.http.conn.scheme.SocketFactory says :
    // Both Object.equals() and Object.hashCode() must be overridden
    // for the correct operation of some connection managers
    // -------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass()
                .equals(MySSLSocketFactory.class));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return MySSLSocketFactory.class.hashCode();
    }

}
