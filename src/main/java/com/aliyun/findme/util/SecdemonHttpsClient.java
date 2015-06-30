package com.aliyun.findme.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: xianliu.wy
 * Date: 14-1-2
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */
public class SecdemonHttpsClient extends SecdemonHttpClient {

    X509TrustManager tm = new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain,
                String authType)
                throws java.security.cert.CertificateException {
            // TODO Auto-generated method stub

        }
        
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain,
                String authType)
                throws java.security.cert.CertificateException {
            // TODO Auto-generated method stub

        }
    };
    protected HttpClient getHttpClient()throws Exception{
        if (tl.get() != null) {
            return tl.get();
        } else {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
            ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
            HttpClient client = new DefaultHttpClient();
            HttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            tl.set(httpClient);
            return httpClient;
        }
    }

}
