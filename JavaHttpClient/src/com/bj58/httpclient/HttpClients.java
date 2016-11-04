package com.bj58.httpclient;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
 
//import bsh.ParseException;
import com.google.gson.Gson;
 
/**
 * TODO
 * @Version 1.0
 */
public class HttpClients {
    /** UTF-8 */
    private static final String UTF_8 = "UTF-8";
    /** ��־��¼tag */
    private static final String TAG = "HttpClients";
 
    /** �û�host */
    private static String proxyHost = "";
    /** �û��˿� */
    private static int proxyPort = 80;
    /** �Ƿ�ʹ���û��˿� */
    private static boolean useProxy = false;
 
    /** ���ӳ�ʱ */
    private static final int TIMEOUT_CONNECTION = 60000;
    /** ��ȡ��ʱ */
    private static final int TIMEOUT_SOCKET = 60000;
    /** ����3�� */
    private static final int RETRY_TIME = 3;
 
    /**
     * @param url
     * @param requestData
     * @return
     */
    public String doHtmlPost(HttpClient httpClient,HttpPost httpPost )
    {
        String responseBody = null;
 
        int statusCode = -1;
 
        try {

            httpPost.setHeader("HTTPS-Tag", "https");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Header lastHeader = httpResponse.getLastHeader("Set-Cookie");
            if(null != lastHeader)
            {
                httpPost.setHeader("cookie", lastHeader.getValue());

            }
            statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("HTTP" + "  " + "HttpMethod failed: " + httpResponse.getStatusLine());
            }
            InputStream is = httpResponse.getEntity().getContent();
            responseBody = getStreamAsString(is, HTTP.UTF_8);
 
        } catch (Exception e) {
            // ���������쳣
            e.printStackTrace();
        } finally {
//          httpClient.getConnectionManager().shutdown();
//          httpClient = null;
        }
 
        return responseBody;
    }
     
     
    /**
     * 
     * ������������
     * 
     * @param url
     *            URL
     * @param requestData
     *            requestData
     * @return INPUTSTREAM
     * @throws AppException
     */
    public static String doPost(String url, String requestData) throws Exception {
        String responseBody = null;
        HttpPost httpPost = null;
        HttpClient httpClient = null;
        int statusCode = -1;
        int time = 0;
        do {
            try {
                httpPost = new HttpPost(url);
                httpClient = getHttpClient();
                // ����HTTP POST�������������NameValuePair����
                List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("param", requestData));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                // ����HTTP POST�������
                httpPost.setEntity(entity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    System.out.println("HTTP" + "  " + "HttpMethod failed: " + httpResponse.getStatusLine());
                }
                InputStream is = httpResponse.getEntity().getContent();
                responseBody = getStreamAsString(is, HTTP.UTF_8);
                break;
            } catch (UnsupportedEncodingException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // �����������쳣��������Э�鲻�Ի��߷��ص�����������
                e.printStackTrace();
 
            } catch (ClientProtocolException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // �����������쳣��������Э�鲻�Ի��߷��ص�����������
                e.printStackTrace();
            } catch (IOException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // ���������쳣
                e.printStackTrace();
            } catch (Exception e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // ���������쳣
                e.printStackTrace();
            } finally {
                httpClient.getConnectionManager().shutdown();
                httpClient = null;
            }
        } while (time < RETRY_TIME);
        return responseBody;
    }
 
    /**
     * 
     * ��InputStream ת��ΪString
     * 
     * @param stream
     *            inputstream
     * @param charset
     *            �ַ���
     * @return
     * @throws IOException
     */
    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset), 8192);
            StringWriter writer = new StringWriter();
 
            char[] chars = new char[8192];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
 
            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
     
    /**
     * �õ�httpClient
     * 
     * @return
     */
    public HttpClient getHttpClient1() {
        final HttpParams httpParams = new BasicHttpParams();
 
        if (useProxy) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
            httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
 
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_SOCKET);
        HttpClientParams.setRedirecting(httpParams, true);
        final String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.14) Gecko/20110218 Firefox/3.6.14";
 
        HttpProtocolParams.setUserAgent(httpParams, userAgent);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpClientParams.setCookiePolicy(httpParams, CookiePolicy.RFC_2109);
         
        HttpProtocolParams.setUseExpectContinue(httpParams, false);
        HttpClient client = new DefaultHttpClient(httpParams);
 
        return client;
    }
 
    /**
     * 
     * �õ�httpClient
     * 
     * @return
     */
    private static HttpClient getHttpClient() {
        final HttpParams httpParams = new BasicHttpParams();
 
        if (useProxy) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
            httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
 
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_SOCKET);
        HttpClientParams.setRedirecting(httpParams, true);
        final String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.14) Gecko/20110218 Firefox/3.6.14";
 
        HttpProtocolParams.setUserAgent(httpParams, userAgent);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpClientParams.setCookiePolicy(httpParams, CookiePolicy.BROWSER_COMPATIBILITY);
        HttpProtocolParams.setUseExpectContinue(httpParams, false);
        HttpClient client = new DefaultHttpClient(httpParams);
 
        return client;
    }
 
    /**
     * ��ӡ��������
     * @param response
     * @throws ParseException
     * @throws IOException
     */
    public static void showResponse(String str) throws Exception {
        Gson gson = new Gson();    
        Map<String, Object> map = (Map<String, Object>) gson.fromJson(str, Object.class);
        String value = (String) map.get("data");       
        //String decodeValue =  Des3Request.decode(value);
        //System.out.println(decodeValue);
        //logger.debug(decodeValue);
    }
     
    /**
     * 
     * ������������
     * 
     * @param url
     *            URL
     * @param requestData
     *            requestData
     * @return INPUTSTREAM
     * @throws AppException
     */
    public static String doGet(String url) throws Exception {
        String responseBody = null;
        HttpGet httpGet = null;
        HttpClient httpClient = null;
        int statusCode = -1;
        int time = 0;
        do {
            try {
                httpGet = new HttpGet(url);
                httpClient = getHttpClient();
                httpGet.setHeader("HTTPS-Tag", "https");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    System.out.println("HTTP" + "  " + "HttpMethod failed: " + httpResponse.getStatusLine());
                }
                InputStream is = httpResponse.getEntity().getContent();
                responseBody = getStreamAsString(is, HTTP.UTF_8);
                break;
            } catch (UnsupportedEncodingException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // �����������쳣��������Э�鲻�Ի��߷��ص�����������
                e.printStackTrace();
 
            } catch (ClientProtocolException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // �����������쳣��������Э�鲻�Ի��߷��ص�����������
                e.printStackTrace();
            } catch (IOException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // ���������쳣
                e.printStackTrace();
            } catch (Exception e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // ���������쳣
                e.printStackTrace();
            } finally {
//                httpClient.getConnectionManager().shutdown();
                httpClient = null;
            }
        } while (time < RETRY_TIME);
        return responseBody;
    }

    public static void main(String[] args) {
//        String url = "http://10.252.24.180/api/list/zufang?v=1&action=getMetaInfo&curVer=7.4.4&filterParams=%7B%7D
//    						+ "&appId=1&params=%7B%7D&localname=bj&os=android&format=json";
    	String url = "http://10.252.24.180:8082/api/detail/hezu/27856016083626?v=1&commondata=%7B%22hasNext%22%3Atrue%2C%22nextObserverIndex%22%3A0%2C%22tracekey%22%3A%225ab09639511a1609b64d2fad2233151e%22%7D&sidDict=%7B%22PGTID%22%3A%22%22%2C%22GTID%22%3A%22199320032193756110048352673%22%7D&localname=bj&format=json&platform=android&version=7.4.4";
    	try{
            System.out.println(doGet(url));
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
