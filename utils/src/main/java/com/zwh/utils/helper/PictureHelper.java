package com.zwh.utils.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片的帮助类
 * 因为图片可能不是这一个地方使用所以这里创建一个帮助类便于复用
 */
public class PictureHelper {

    /**
     * 7.0图片路径
     */
    private static final String FILE_PROVIDER_AUTHORITY = "om.rrtx.mobile.xwallet.provider";
    /**
     * 上下文
     */
    private Activity mActivity;
    /**
     * 打开相机的请求码
     */
    private int REQUEST_CODE_CAPTURE_CAMERA = 0X1;
    /**
     * 打开相册的请求码
     */
    private int REQUEST_CODE_PICK_IMAGE = 0X3;
    /**
     * 裁剪的请求码
     */
    private int REQUEST_CODE_CUT = 0X2;

    private static int BITMAPSIZE = 2;

    /**
     * 回调
     */
    private SnapshotCallBack mPictureCallBack;
    /**
     * 图片的地址
     */
    private String mPath;
    /**
     * 请求码，用于区分相应的请求
     */
    private String mRequestCode;
    /**
     * 预想宽度
     */
    private int mAnticipationWidth;
    /**
     * 预想高度
     */
    private int mAnticipationHeight;


    public PictureHelper(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * 打开相应的相册
     *
     * @param imageUri        图片的名称
     * @param requestCode     请求码
     * @param width           预想宽度
     * @param height          预想高度
     * @param pictureCallBack 回调
     */
    public void openAlbum(@NonNull String imageUri, String requestCode, int width, int height, SnapshotCallBack pictureCallBack) {
        mRequestCode = requestCode;
        mPictureCallBack = pictureCallBack;
        //预想尺寸
        mAnticipationWidth = width;
        mAnticipationHeight = height;
        //图片要保存的路径
        mPath = getImageFilePath(imageUri);

        //把名称更换成路径
        Intent intent = new Intent(Intent.ACTION_PICK);
        //相片类型
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        mActivity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 打开相机
     *
     * @param imageUri        图片的名称
     * @param requestCode     请求码
     * @param width           预想宽度
     * @param height          预想高度
     * @param pictureCallBack 回调
     */
    public void openSnapshot(@NonNull String imageUri, @NonNull String requestCode, int width, int height, SnapshotCallBack pictureCallBack) {
        mRequestCode = requestCode;
        mPictureCallBack = pictureCallBack;
        //预想尺寸
        mAnticipationWidth = width;
        mAnticipationHeight = height;
        //把名称更换成路径
        mPath = getImageFilePath(imageUri);
        Log.e("路径", "openSnapshot: " + mPath);
        //调用相机
        if (!TextUtils.isEmpty(mPath)) {
            File imageFile = new File(mPath);
            //适配7.0的权限问题
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri mImageUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                /*7.0以上要通过FileProvider将File转化为Uri*/
                mImageUri = FileProvider.getUriForFile(mActivity, FILE_PROVIDER_AUTHORITY, imageFile);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                mImageUri = Uri.fromFile(imageFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            mActivity.startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
        }
    }

    /**
     * 默认的图片保存地址
     *
     * @param name 图片名称
     * @return 相应的图片路径
     */
    private String getImageFilePath(String name) {
        final File dir = mActivity.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + File.separator) + name);
    }

    /**
     * 回调页面
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("done", "onActivityResult: " + requestCode + "----–" + resultCode);
        if (REQUEST_CODE_CAPTURE_CAMERA == requestCode && Activity.RESULT_OK == resultCode) {
            //照相,这里有的时候data为空这里直接从文件中取
            Uri uri;
            //防止uri为空
            Log.e("test", "onActivityResult: " + mPath);
            File imageFile = new File(mPath);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                /*7.0以上要通过FileProvider将File转化为Uri*/
                uri = FileProvider.getUriForFile(mActivity, FILE_PROVIDER_AUTHORITY, imageFile);
            } else {
                /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                uri = Uri.fromFile(imageFile);
            }

            if (null != uri) {
                cropAndThumbnail(uri, mAnticipationWidth, mAnticipationHeight);
            }
        } else if (REQUEST_CODE_CUT == requestCode && Activity.RESULT_OK == resultCode) {
            if (!TextUtils.isEmpty(mPath)) {
                //将图片的长和宽缩小味原来的1/2
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);

//                    Bitmap bitmap = compressBitmap(mPath, BITMAPSIZE);

                    if (mPictureCallBack != null) {
                        mPictureCallBack.snapshotCallBack(mRequestCode, bitmap, mPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (REQUEST_CODE_PICK_IMAGE == requestCode && Activity.RESULT_OK == resultCode) {
            if (null != data) {
                Uri uri = data.getData();
                File imageFile = new File(mPath);
                // Android11以上 关键一步
                copyFieUriToInnerStorage(uri, imageFile);
                //裁剪
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    uri = FileProvider.getUriForFile(mActivity, FILE_PROVIDER_AUTHORITY, imageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    uri = Uri.fromFile(imageFile);
                }
                if (null != uri) {
                    try {
                        cropAndThumbnail(uri, mAnticipationWidth, mAnticipationHeight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private Bitmap compressBitmap(String srcPath, long sizeLimit) {
        //获取相应的Bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);


        if (bitmap == null) {
            return null;
        }

        Log.e("TAG", "compressBitmap: "+bitmap.getByteCount() );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int optionsNum = 100;
        while (optionsNum>5 && baos.toByteArray().length / 1024 > sizeLimit) {
            //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            //重置baos即清空baos
            baos.reset();
            //每次都减少5
            optionsNum -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, optionsNum, baos);
        }

        File file = new File(srcPath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 裁剪操作
     */
    private void cropAndThumbnail(Uri uri, int width, int height) {
        if (uri == null) {
            return;
        }

        int defaultSize = 400;

        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置类型
        intent.setDataAndType(uri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
        //裁剪宽度比例
        intent.putExtra("aspectX", width < defaultSize ? defaultSize - 1 : width - 1);
        //裁剪高度比例
        intent.putExtra("aspectY", Math.max(height, defaultSize));
        //裁剪后宽度
        intent.putExtra("outputX", Math.max(width, defaultSize));
        //裁剪后高度
        intent.putExtra("outputY", Math.max(height, defaultSize));
        //
        intent.putExtra("scale", true);
        //配置裁剪完成后输出的地址
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file://" + mPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //是否有人脸识别
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //添加私有的权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //打开剪裁Activity
        mActivity.startActivityForResult(intent, REQUEST_CODE_CUT);
    }

    /**
     * 拍照的回调
     */
    public interface SnapshotCallBack {
        /**
         * 拍照的回调
         *
         * @param mRequestCode 请求码
         * @param bitmap       回调相应的Bitmap
         * @param mPath        相应的图片保存路径
         */
        void snapshotCallBack(String mRequestCode, Bitmap bitmap, String mPath);
    }

    /**
     * 通过uri拷贝外部存储的文件到自己包名的目录下
     *
     * @param uri
     * @param destFile
     */
    private void copyFieUriToInnerStorage(Uri uri, File destFile) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = mActivity.getContentResolver().openInputStream(uri);
            if (destFile.exists()) {
                destFile.delete();
            }
            fileOutputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[4096];
            int redCount;
            while ((redCount = inputStream.read(buffer)) >= 0) {
                fileOutputStream.write(buffer, 0, redCount);
            }
        } catch (Exception e) {
            Log.e("PictureHelper", " copy file uri to inner storage e = " + e.toString());
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.getFD().sync();
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                Log.e("PictureHelper", " close stream e = " + e.toString());
            }
        }
    }
}
