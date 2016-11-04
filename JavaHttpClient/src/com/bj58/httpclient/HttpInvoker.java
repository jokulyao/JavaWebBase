package com.bj58.httpclient;

//import com.bj58.jwdf.framework.util.JsonHelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpInvoker {

//    public static final String GET_URL = "http://www.sina.com.cn";
    public static final String GET_URL = "http://10.252.24.180/api/list/ershoufang?&action=getHouseOnMapInfo&localname=bj&maptype=2&circlelat=40.220787&circlelon=116.257275&startlat=40.192079&startlon=116.199783&endlat=40.249484&endlon=116.314766&type=2&localId=1150&lastRecord=7417%2C5153%2C5152%2C5830%2C5829%2C5154%2C15509%2C5832%2C6058";

    public static final String POST_URL = "http://localhost:8080/welcome1";

    public static void readContentFromGet() {
        // ƴ��get�����URL�ִ���ʹ��URLEncoder.encode������Ͳ��ɼ��ַ����б���
//        String getURL = GET_URL + "?username=" + URLEncoder.encode("fat man", "utf-8");
        String getURL = GET_URL ;
        URL getUrl = null;
        try {
            getUrl = new URL(getURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // ����ƴ�յ�URL�������ӣ�URL.openConnection���������URL�����ͣ�
        // ���ز�ͬ��URLConnection����Ķ�������URL��һ��http�����ʵ�ʷ��ص���HttpURLConnection
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) getUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // �������ӣ�����ʵ����get requestҪ����һ���connection.getInputStream()�����вŻ���������
        // ������
        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ȡ������������ʹ��Reader��ȡ
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("=============================");
        System.out.println("Contents of get request");
        System.out.println("=============================");
        String lines;
        StringBuffer content = new StringBuffer("");
        try {
            while ((lines = reader.readLine()) != null) {
    //            System.out.println(lines);
                content.append(lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Map map = JsonHelper.parseObject(content.toString(), HashMap.class);
//        System.out.println(map.get("result").toString());
//        System.out.println(content.toString());
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // �Ͽ�����
        connection.disconnect();
        System.out.println("=============================");
        System.out.println("Contents of get request ends");
        System.out.println("=============================");
    }

    public static void readContentFromPost() throws IOException {
        // Post�����url����get��ͬ���ǲ���Ҫ������
        URL postUrl = new URL(POST_URL);
        // ������
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        // Output to the connection. Default is
        // false, set to true because post
        // method must write something to the
        // connection
        // �����Ƿ���connection�������Ϊ�����post���󣬲���Ҫ����
        // http�����ڣ������Ҫ��Ϊtrue
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // Set the post method. Default is GET
        connection.setRequestMethod("POST");
        // Post cannot use caches
        // Post ������ʹ�û���
        connection.setUseCaches(false);
        // This method takes effects to
        // every instances of this class.
        // URLConnection.setFollowRedirects��static���������������е�URLConnection����
        // connection.setFollowRedirects(true);

        // This methods only
        // takes effacts to this
        // instance.
        // URLConnection.setInstanceFollowRedirects�ǳ�Ա�������������ڵ�ǰ����
        connection.setInstanceFollowRedirects(true);
        // Set the content type to urlencoded,
        // because we will write
        // some URL-encoded content to the
        // connection. Settings above must be set before connect!
        // ���ñ������ӵ�Content-type������Ϊapplication/x-www-form-urlencoded��
        // ��˼��������urlencoded�������form�������������ǿ��Կ������Ƕ���������ʹ��URLEncoder.encode
        // ���б���
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // ���ӣ���postUrl.openConnection()���˵����ñ���Ҫ��connect֮ǰ��ɣ�
        // Ҫע�����connection.getOutputStream�������Ľ���connect��
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        // The URL-encoded contend
        // ���ģ�����������ʵ��get��URL��'?'��Ĳ����ַ���һ��
        String content = "firstname=" + URLEncoder.encode("һ�������", "utf-8");
        // DataOutputStream.writeBytes���ַ����е�16λ��unicode�ַ���8λ���ַ���ʽд��������
        out.writeBytes(content);

        out.flush();
        out.close(); // flush and close
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        System.out.println("=============================");
        System.out.println("Contents of post request");
        System.out.println("=============================");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("=============================");
        System.out.println("Contents of post request ends");
        System.out.println("=============================");
        reader.close();
        connection.disconnect();
    }

    /** */
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        try {
            readContentFromGet();
//            readContentFromPost();
//        } catch (IOException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

}
