//$_FILEHEADER_BEGIN ***************************
//版权声明: 贵阳朗玛信息技术股份有限公司版权所有
//Copyright (C) 2012 Longmaster Corporation. All Rights Reserved
//文件名称: AlbumUtils.java
//创建日期: 2013/03/20
//创 建 人: czc
//文件说明: 
//$_FILEHEADER_END *****************************
package com.qckj.dabei.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.qckj.dabei.BuildConfig;
import com.qckj.dabei.R;
import com.qckj.dabei.app.App;

import java.io.File;

public class AlbumUtils {
    public static final int NORMAL = 1;
    public static final int OPEN_CAMERA = 1;
    public static final int OPEN_ALBUM = 2;
    public static final int OPEN_CAMERA_EX = 4;
    public static final int OPEN_ALBUM_EX = 5;

    /**
     * 打开相册
     */
    public static void openAlbum(Activity a_Activity, int a_type) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(App.getInstance(), R.string.insert_sdcard, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");

        if (HealthData.isIntentExist(App.getInstance(), intent)) {
            if (a_type == NORMAL) {
                a_Activity.startActivityForResult(intent, OPEN_ALBUM);
            } else {
                a_Activity.startActivityForResult(intent, OPEN_ALBUM_EX);
            }
        } else {
            Toast.makeText(App.getInstance(), R.string.not_support, Toast.LENGTH_LONG).show();
        }
    }

    public static String getDirFromAlbumUri(Uri a_uri) {
        String path = null;
        Context context = App.getInstance();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                    && DocumentsContract.isDocumentUri(context, a_uri)) {
                if (isExternalStorageDocument(a_uri)) {
                    String docId = DocumentsContract.getDocumentId(a_uri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(a_uri)) {
                    String id = DocumentsContract.getDocumentId(a_uri);
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                } else if (isMediaDocument(a_uri)) {
                    String docId = DocumentsContract.getDocumentId(a_uri);
                    String[] split = docId.split(":");
                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, uri, selection, selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(a_uri.getScheme())) {
                // Return the remote address
                path = getDataColumn(context, a_uri, null, null);
            } // File
            else if ("file".equalsIgnoreCase(a_uri.getScheme())) {
                path = a_uri.getPath();
            } else {
                path = getDataColumn(context, a_uri, null, null);
            }
        } catch (Exception e) {
            path = a_uri.toString().replace("file://", "");
        }
        return path;
    }

    /**
     * 根据uri获取图片路径，注意uri只能是图片的uri
     *
     * @param context       上下文
     * @param uri           uri
     * @param selection     selection
     * @param selectionArgs selectionArgs
     * @return filePath
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 打开相机
     */
    public static void openCamera(Activity a_oActivity, int a_type, String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), fileName + ".png");
        if (file.exists())
            file.delete();
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(a_oActivity, BuildConfig.APPLICATION_ID + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        openCamera(a_oActivity, a_type, uri);
    }

    /**
     * 打开相机
     * Uri在Android N上的使用有修改,使用时需要注意.
     * 建议使用openCamera(Activity a_oActivity, int a_type)
     * 如需要自定义uri，请对Android N进行兼容
     */
    @Deprecated
    public static void openCamera(Activity a_oActivity, int a_type, Uri output) {
        if (!StorageUtils.checkSdcard()) {
            Toast.makeText(App.getInstance(), R.string.insert_sdcard, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        // 判断该设备是否支持打开相机的操作
        if (HealthData.isIntentExist(App.getInstance(), intent)) {
            if (a_type == NORMAL) {
                a_oActivity.startActivityForResult(intent, OPEN_CAMERA);
            } else {
                a_oActivity.startActivityForResult(intent, OPEN_CAMERA_EX);
            }
        } else {
            Toast.makeText(App.getInstance(), R.string.not_support, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 返回相册图片的路径
     *
     * @return 图片路径或null
     */
    public static String getAlbumImagePath(Context context, Uri uri) {
        String lImagePath = null;
        try {
            String[] lMediaStoreData = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor lCursor = context.getContentResolver().query(uri, lMediaStoreData, null, null, null);

            if (lCursor != null) {
                if (lCursor.moveToPosition(0)) {
                    int columnPathIndex = lCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    lImagePath = lCursor.getString(columnPathIndex);
                }
                lCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return lImagePath;
    }
}
