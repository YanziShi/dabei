package com.qckj.dabei.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import com.qckj.dabei.app.SystemConfig;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import static android.net.sip.SipErrorCode.TIME_OUT;
import static android.provider.Telephony.Mms.Part.CHARSET;

/**
 * Created by yangzhizhong on 2019/3/22.
 */
public class CommonUtils {

    /**
     * weixin登录与支付分享
     */
    public static final String WX_APP_ID = "wxf8cc09cfbaf41350";
    public static final String WX_APP_SECRET = "c47fa9626104f06d8c3d8d08ec2d6bec";
    // QQ
    public static final String QQ_APP_ID = "101507083";
    public static final String QQ_APP_SECRET = "2da9daeb773bdb04590db94c67d53976";

    //Weibo

    public static final String WB_APP_ID = "545392839";
    public static final String WB_APP_SECRET = "0228fb404dc7e67c09caf92aad4ba266";

    //EventBus
    public static final String CHANGE_ADDRESS_TAG = "CHANGE_ADDRESS_TAG";
    public static final String CHANGE_USERINFO_TAG = "CHANGE_USERINFO_TAG";

    /**
     * 将dip转换为px
     *
     * @param dip dip
     * @return
     */
    public static int dipToPx(Context context, float dip) {
        return (int) (context.getResources().getDisplayMetrics().density * dip + 0.5f);
    }

    public static String uploadFile(String postUrl,File file) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型

        String requestUrl = SystemConfig.getServerUrl(postUrl);

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", "UTF-8");  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                String sb = PREFIX +
                        BOUNDARY +
                        LINE_END +
                        "Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END +
                        "Content-Type: application/octet-stream; charset=UTF-8" + LINE_END +
                        LINE_END;
                dos.write(sb.getBytes("UTF-8"));
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes("UTF-8"));
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes("UTF-8");
                dos.write(end_data);
                dos.flush();
                InputStream input = conn.getInputStream();
                String contentEncoding = conn.getContentEncoding();
                if (contentEncoding != null && contentEncoding.contains("gzip"))
                    input = new GZIPInputStream(input);
                byte[] data = IOUtils.readInputStream(input);
                result = new String(data, "UTF-8");
                Log.e("===", "result : " + result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保证生产bitmap的时候不oom
     *
     * @param path
     * @return
     */

    public static Bitmap lessenUriImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //此时返回 bm 为空
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        options.inJustDecodeBounds = false;
        int be = (int) (options.outHeight / (float) 1320);
        if (be <= 0) {
            be = 1;
        }
        //重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回 false 了
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    public static void compressBitmapToFile(Bitmap bmp, File file){
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 1;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public String buildPath(List<String> paths) {
        String mFilePath = "";
        if(paths==null) return mFilePath;
        for (String path : paths) {
            if (TextUtils.isEmpty(mFilePath)) {
                mFilePath = path;
            } else {
                mFilePath = mFilePath + "," + path;
            }
        }
        return mFilePath;
    }

}
