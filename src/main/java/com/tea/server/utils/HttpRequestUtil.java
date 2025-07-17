package com.tea.server.utils;

import com.alibaba.fastjson.JSONObject;
import com.tea.server.entity.params.EleParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.AsyncRestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


@Slf4j
public class HttpRequestUtil {
    public static void GetURL(String url, String param) {

        String urlNameString = url + "?" + param;
        log.info("urlNameString:"+urlNameString);
            try {
                URL apiUrl = new URL(urlNameString);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                // 设置连接超时和读取超时（单位为毫秒）
                int timeout = 10000; // 10秒
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);

                // 发起请求
                connection.connect();

                int responseCode = connection.getResponseCode();
                log.info("responseCode:"+responseCode);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    reader.close();

            } catch (IOException e) {
                e.printStackTrace();
                log.info("xxxxxxxxxxxxxxxx:"+e.getMessage());
            }

    }
    /**
     * 向指定URL发送GET方法的请求
     *
     * @modificationHistory.
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        StringBuffer result = new StringBuffer("");
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            System.out.println("urlNameString:"+urlNameString);
            log.info("urlNameString:"+urlNameString);
            System.out.println("urlNameString:"+urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性

            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("11111111111111111222222222222222");
            // System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
            return "400";
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();

    }

    public static String doGet(String url) {
        StringBuffer result = new StringBuffer("");
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            // System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
            return "400";
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }
    public static String doPost(String url, EleParam eleParam) {
        StringBuffer result = new StringBuffer("");
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();


            // 设置通用的请求属性
            // connection.setRequestProperty("accept", "*/*");
            // connection.setRequestProperty("connection", "Keep-Alive");
            // connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 设置请求方法为POST
            connection.setRequestMethod("POST");

            // 设置请求内容类型
            connection.setRequestProperty("Content-Type", "application/json");

            // 设置允许输入输出
            connection.setDoOutput(true);

            // 请求体内容
            String requestBody = "{\"key1\":\"value1\",\"key2\":\"value2\"}";

            // 获取输出流并写入请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            // System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
            return "400";
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

    public static void sendPost(String uri, String param) {
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // 设置超时参数（单位为毫秒）
            conn.setConnectTimeout(10000); // 连接超时
            conn.setReadTimeout(10000); // 读取超时


            // 发送POST请求必须设置以下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 设置请求头，如Content-Type
            // conn.setRequestProperty("Content-Type", "application/json");

            // 参数
            String params = param;

            // 发送请求参数
            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes());
                os.flush();
            }catch (IOException e) {
                log.error("333333333333333333333"+e.getMessage());
            }

            // 获取响应码
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 处理响应...
            log.info("Response Code: " + responseCode);

            conn.disconnect();
        } catch (Exception e) {
            log.error("*********************************11111111111111111");
            e.printStackTrace();
            log.error("*********************************222222222222222222"+e.getMessage());
        }


    }





}
