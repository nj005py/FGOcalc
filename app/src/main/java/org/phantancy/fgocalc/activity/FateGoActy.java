package org.phantancy.fgocalc.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.view.CameraPreview;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PY on 2016/10/31.
 */
public class FateGoActy extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.afgo_fl_camera)
    FrameLayout afgoFlCamera;
    @BindView(R.id.afgo_iv_servant)
    ImageView afgoIvServant;
    @BindView(R.id.afgo_iv_stone)
    ImageView afgoIvStone;
    @BindView(R.id.afgo_iv_stone_shoot)
    ImageView afgoIvStoneShoot;
    @BindView(R.id.afgo_iv_stone_shoot2)
    ImageView afgoIvStoneShoot2;
    @BindView(R.id.afgo_iv_stone_shoot3)
    ImageView afgoIvStoneShoot3;
    private CameraPreview mPreview;
    private Camera mCamera;
    private static final int TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.acty_fate_go);
        ButterKnife.bind(this);
        ctx = this;
        checkPerssmission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //不使用地时候需要释放 资源
        if (mCamera != null) {
            mCamera.setPreviewCallback(null) ;
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null) ;
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void checkPerssmission() {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO);
        } else {
            init();
        }
    }

    private void init() {
        // Create an instance of Camera
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        afgoFlCamera.addView(mPreview);
        setCameraDisplayOrientation(this, getDefaultCameraId(), mCamera);
        setListener();
    }

    private void setListener() {
        afgoIvStone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.afgo_iv_stone:
                stoneAnime(afgoIvStoneShoot,R.anim.push_bottom_in);
                stoneAnime(afgoIvStoneShoot2,R.anim.push_bottom_in2);
                stoneAnime(afgoIvStoneShoot3,R.anim.push_bottom_in3);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    ToastUtils.displayShortToast(ctx, "您拒绝了权限");
                }
                break;

        }
    }

    private void stoneAnime(final View v,int a){
        v.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(ctx,a);
        v.setAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private int getDefaultCameraId() {
        int defaultId = -1;
        int mNumberOfCameras;

        // Find the total number of cameras available
        mNumberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the default camera
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < mNumberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                defaultId = i;
            }
        }
        if (-1 == defaultId) {
            if (mNumberOfCameras > 0) {
                // 如果没有后向摄像头
                defaultId = 0;
            } else {
                // 没有摄像头
                Toast.makeText(getApplicationContext(), "无",
                        Toast.LENGTH_LONG).show();
            }
        }
        return defaultId;
    }

    // 设置相机横竖屏
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

}
