package com.qckj.dabei.app.http;

import android.os.Handler;
import android.os.Looper;
import com.qckj.dabei.util.IOUtils;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

/**
 * 异步请求
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public class AsyncHttpClient {

    public static Handler handler = new Handler(Looper.getMainLooper());
    private static Executor executorServer = Executors.newCachedThreadPool();
    private static String session_id;

    /**
     * 发起GET请求
     *
     * @param urlString        请求地址
     * @param iResponseHandler 请求回调
     */
    public static void get(final String urlString, final IResponseHandler iResponseHandler) {
        get(urlString, null, iResponseHandler);
    }

    /**
     * 发起get请求
     *
     * @param urlString        请求地址
     * @param params           参数
     * @param iResponseHandler 回调
     */
    public static void get(String urlString, Map<String, String> params, IResponseHandler iResponseHandler) {
        get(urlString, params, null, iResponseHandler);
    }

    /**
     * 发起get请求
     *
     * @param urlString        请求地址
     * @param params           参数
     * @param headers          请求头
     * @param iResponseHandler 请求回调
     */
    public static void get(final String urlString, final Map<String, String> params, final Map<String, String> headers, final IResponseHandler iResponseHandler) {
        executorServer.execute(() -> {
            HttpURLConnection httpURLConnection = null;
            try {
                String url;
                if (params != null && params.size() > 0) {
                    url = urlString + "?" + changeParams(params);
                } else {
                    url = urlString;
                }
                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(30000);
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.addRequestProperty("Accept-Encoding", "gzip");
                if (headers != null) {
                    addRequestProperty(httpURLConnection, headers);
                }
                httpURLConnection.connect();
                processResult(iResponseHandler, httpURLConnection);
            } catch (Exception exception) {
                onError(iResponseHandler, exception);
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
        });
    }

    /**
     * 延时发起请求
     *
     * @param urlString        请求地址
     * @param params           参数
     * @param delay            延时时间
     * @param iResponseHandler 请求回调
     */
    public static void postDelay(final String urlString, final Map<String, String> params, final Map<String, String> headers, int delay, final IResponseHandler iResponseHandler) {
        handler.postDelayed(() -> post(urlString, params, headers, iResponseHandler), delay);
    }

    /**
     * application/x-www-form-urlencoded 方式 发起POST请求
     *
     * @param urlString        请求地址
     * @param params           参数
     * @param headers          请求头
     * @param iResponseHandler 请求回调
     */
    public static void post(final String urlString, final Map<String, String> params, final Map<String, String> headers,
                            final IResponseHandler iResponseHandler) {
        executorServer.execute(() -> {
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = getPostConnection(urlString, "application/x-www-form-urlencoded; charset=UTF-8");
                if (headers != null && headers.size() > 0) {
                    addRequestProperty(httpURLConnection, headers);
                }
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                if (params != null)
                    outputStream.write(changeParams(params).getBytes("UTF-8"));
                outputStream.close();
                // 取得sessionid.
                String cookieval = httpURLConnection.getHeaderField("Set-Cookie");
                if (cookieval != null) {
                    session_id = cookieval.substring(0, cookieval.indexOf(";"));//获取sessionid
                }
                processResult(iResponseHandler, httpURLConnection);
            } catch (Exception exception) {
                onError(iResponseHandler, exception);
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
        });
    }

    public static void uploadFile(String url, File file, IResponseHandler iResponseHandler) {
        executorServer.execute(new Runnable() {
            @Override
            public void run() {
                String PREFIX = "--", LINE_END = "\r\n";
                String BOUNDARY = UUID.randomUUID().toString();
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setConnectTimeout(30000);
                    httpURLConnection.setReadTimeout(30000);
                    httpURLConnection.setDefaultUseCaches(false);
                    httpURLConnection.addRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
                    httpURLConnection.addRequestProperty("Accept-Encoding", "gzip");
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    String str = PREFIX + BOUNDARY + LINE_END +
                            "Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END +
                            "Content-Type: application/octet-stream; charset=UTF-8" + LINE_END + LINE_END;
                    dataOutputStream.write(str.getBytes("UTF-8"));
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = fileInputStream.read(bytes)) != -1) {
                        dataOutputStream.write(bytes, 0, len);
                    }
                    fileInputStream.close();
                    dataOutputStream.write(LINE_END.getBytes("UTF-8"));
                    byte[] endData = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes("UTF-8");
                    dataOutputStream.write(endData);
                    dataOutputStream.flush();
                    processResult(iResponseHandler, httpURLConnection);
                } catch (IOException e) {
                    onError(iResponseHandler, e);
                }

            }
        });
    }

    private static String changeParams(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!isFirst) {
                stringBuilder.append("&");
            } else {
                isFirst = false;
            }
            try {
                stringBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    private static void addRequestProperty(HttpURLConnection httpURLConnection, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpURLConnection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private static void processResult(final IResponseHandler iResponseHandler,
                                      HttpURLConnection httpURLConnection) throws IOException {
        int code = httpURLConnection.getResponseCode();
        InputStream inputStream = getInputStream(httpURLConnection, code);
        byte[] data = IOUtils.readInputStream(inputStream);
        inputStream.close();
        onResult(iResponseHandler, code, data);
    }

    private static void onResult(final IResponseHandler iResponseHandler, final int code,
                                 final byte[] data) {
        handler.post(() -> iResponseHandler.onResult(code, data));
    }

    private static void onError(final IResponseHandler iResponseHandler, final Exception exception) {
        handler.post(() -> iResponseHandler.onError(exception));
    }

    private static InputStream getInputStream(HttpURLConnection httpURLConnection, int code)
            throws IOException {
        InputStream inputStream;
        if (code == HttpURLConnection.HTTP_OK) {
            inputStream = httpURLConnection.getInputStream();
        } else {
            inputStream = httpURLConnection.getErrorStream();
        }
        String contentEncoding = httpURLConnection.getContentEncoding();
        if (contentEncoding != null && contentEncoding.contains("gzip"))
            inputStream = new GZIPInputStream(inputStream);
        return inputStream;
    }

    private static HttpURLConnection getPostConnection(final String url, String contentType)
            throws IOException {
        HttpURLConnection httpURLConnection;
        httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(30000);
        httpURLConnection.setDefaultUseCaches(false);
        httpURLConnection.addRequestProperty("Content-Type", contentType);
        httpURLConnection.addRequestProperty("Accept-Encoding", "gzip");
        if (session_id != null) {
            httpURLConnection.setRequestProperty("Cookie", session_id);//设置sessionid
        }
        return httpURLConnection;
    }

}
