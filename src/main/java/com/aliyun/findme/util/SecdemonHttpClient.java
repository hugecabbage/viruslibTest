package com.aliyun.findme.util;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: xianliu.wy
 * Date: 13-12-30
 * Time: 下午2:39
 * To change this template use File | Settings | File Templates.
 */

public class SecdemonHttpClient {
    protected static ThreadLocal<HttpClient> tl = new ThreadLocal<HttpClient>();

    protected HttpClient getHttpClient() throws Exception{
        if (tl.get() != null) {
            return tl.get();
        } else {
            HttpClient client = new DefaultHttpClient();
            tl.set(client);
            return client;
        }
    }

    public String invoke(HttpPost httpPost)throws Exception {
        HttpClient httpClient = getHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        try{
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if(statusCode == 200){
                String responseString =  EntityUtils.toString(httpResponse.getEntity());
                return responseString;
            }

        }finally {
            httpPost.abort();
        }
        return null;
    }
    
    
    public String invoke(HttpGet httpGet)throws Exception {
        HttpClient httpClient = getHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        try{
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if(statusCode == 200){
                String responseString =  EntityUtils.toString(httpResponse.getEntity());
                return responseString;
            }

        }finally {
            httpGet.abort();
        }
        return null;
    }
}
