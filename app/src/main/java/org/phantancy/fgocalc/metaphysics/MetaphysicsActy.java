package org.phantancy.fgocalc.metaphysics;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.activity.BaseActivity;
import org.phantancy.fgocalc.util.ImageUtils;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.view.VerticalProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by PY on 2016/10/31.
 */
public class MetaphysicsActy extends BaseActivity implements
        View.OnClickListener,
        MetaphysicsContract.View{

    @BindView(R.id.am_rb_believe)
    RadioButton amRbBelieve;
    @BindView(R.id.am_rb_unbelieve)
    RadioButton amRbUnbelieve;
    @BindView(R.id.am_rg_method)
    RadioGroup amRgMethod;
    @BindView(R.id.am_btn_screen)
    Button amBtnScreen;
    @BindView(R.id.am_tv_percent)
    TextView amTvPercent;
    @BindView(R.id.am_iv_thing)
    ImageView amIvThing;
    @BindView(R.id.am_tv_europe)
    TextView amTvEurope;
    @BindView(R.id.am_pb_europe)
    VerticalProgressBar amPbEurope;
    @BindView(R.id.am_tv_africa)
    TextView amTvAfrica;
    @BindView(R.id.am_btn_time)
    Button amBtnTime;
    @BindView(R.id.am_tv_time)
    TextView amTvTime;
    @BindView(R.id.am_iv_character)
    ImageView amIvCharacter;
    @BindView(R.id.am_v_character)
    View amVCharacter;
    @BindView(R.id.am_tv_character)
    TextView amTvCharacter;
    @BindView(R.id.am_rl_character)
    RelativeLayout amRlCharacter;
    @BindView(R.id.am_pb_africa)
    VerticalProgressBar amPbAfrica;
    private AnimationSet textAnimationSet;
    private static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    private MetaphysicsContract.Presenter mPresenter;

    @Override
    public void setPresenter(MetaphysicsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_metaphysics);
        ButterKnife.bind(this);
        ctx = this;
        mPresenter = new MetaphysicsPresenter(ctx,this);
        init();
    }

    private void init() {
        //初始化状态栏
        initBaseStatusBar(ctx);
        setListener();
    }

    private void setListener() {
        amBtnScreen.setOnClickListener(this);
        amRlCharacter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.am_btn_screen:
                if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO);
                } else {
                    imgByPhoto();
                }
                break;
            case R.id.am_rl_character:
                amRlCharacter.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case TAKE_PHOTO:
                    mPresenter.pic2Result(imageUri);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //拍照获得照片
    public void imgByPhoto() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/screenTest.jpg";
        File outputImage = new File(path);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //7.0开始直接使用本地真实路径是不安全的
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(ctx, "org.phantancy.fgocalc.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imgByPhoto();
                } else {
                    ToastUtils.displayShortToast(ctx, "您拒绝了权限");
                }
                break;

        }
    }

    @Override
    public void setCharacter(String text, int imgRes) {
        amRlCharacter.setVisibility(View.VISIBLE);
        amIvCharacter.setImageResource(imgRes);
        amTvCharacter.setText(text);
        amTvCharacter.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.push_left_in));
    }

    @Override
    public void setResultProgress(String eu, String af, int euInt, int afInt) {
        amTvEurope.setText("欧气浓度: " + eu);
        amTvAfrica.setText("非气浓度: " + af);
        amPbEurope.setSecondaryProgress(euInt);
        amPbAfrica.setSecondaryProgress(afInt);
    }

    public void textAnimation(TextView textScore) {
        textScore.setVisibility(View.VISIBLE);
        TranslateAnimation tAnimation = new TranslateAnimation(0f, 0f, 0f, -80); //位移动画效果
        AlphaAnimation aAnimation = new AlphaAnimation(1, 0); //透明度动画效果
        ScaleAnimation sAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f, 0.5f, 0.5f);  //缩放动画效果
        textAnimationSet = new AnimationSet(true);
        textAnimationSet.addAnimation(tAnimation);
        textAnimationSet.addAnimation(aAnimation);
        textAnimationSet.addAnimation(sAnimation);
        textAnimationSet.setFillBefore(false);
        textAnimationSet.setFillAfter(false);
        textAnimationSet.setFillEnabled(true);
        textAnimationSet.setDuration(700);
        textScore.setAnimation(textAnimationSet);
    }
}
