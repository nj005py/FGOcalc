package com.phantancy.fgocalc.activity;

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
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.util.BaseUtils;
import com.phantancy.fgocalc.util.ImageUtils;
import com.phantancy.fgocalc.util.SharedPreferencesUtils;
import com.phantancy.fgocalc.util.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by PY on 2016/10/31.
 */
public class MetaphysicsActy extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.am_cb_remember)
    CheckBox amCbRemember;
    @BindView(R.id.am_et_id)
    EditText amEtId;
    @BindView(R.id.am_btn_screen)
    Button amBtnScreen;
    @BindView(R.id.am_tv_percent)
    TextView amTvPercent;
    @BindView(R.id.am_iv_thing)
    ImageView amIvThing;
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
    @BindView(R.id.am_rb_believe)
    RadioButton amRbBelieve;
    @BindView(R.id.am_rb_unbelieve)
    RadioButton amRbUnbelieve;
    @BindView(R.id.am_rg_method)
    RadioGroup amRgMethod;
    private long fgoId;
    private double blackPercent = -1;
    private boolean ifRemember;
    private AnimationSet textAnimationSet;
    private static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    private static final int RESULT_PICK = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_metaphysics);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    private void init() {
        initBaseStatusBar(mContext);
        llStatusBar.setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorBlack));
        fgoId = (long) SharedPreferencesUtils.getParam(mContext, "fgoId", 0l);
        if (fgoId > 0) {
            amCbRemember.setChecked(true);
            amEtId.setText(fgoId + "");
        }
        setListener();
    }

    private void setListener() {
        amBtnScreen.setOnClickListener(this);
        amBtnTime.setOnClickListener(this);
        amRlCharacter.setOnClickListener(this);
        amCbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (notEmpty(etValue(amEtId))) {
                        fgoId = Long.parseLong(etValue(amEtId));
                        if (!verifyId(etValue(amEtId))) {
                            amCbRemember.setChecked(false);
                            ToastUtils.displayShortToast(mContext, "fgo数字Id有误");
                            return;
                        } else {
                            SharedPreferencesUtils.setParam(mContext, "fgoId", fgoId);
                        }
                    } else {
                        amCbRemember.setChecked(false);
                        ToastUtils.displayShortToast(mContext, "请输入fgo数字id");
                    }
                } else {
                    SharedPreferencesUtils.setParam(mContext, "fgoId", 0l);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.am_btn_screen:
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},TAKE_PHOTO);
                }else{
                    imgByPhoto();
                }
                break;
            case R.id.am_btn_time:
                if (notEmpty(etValue(amEtId))) {
                    if (!verifyId(etValue(amEtId))) {
                        ToastUtils.displayShortToast(mContext, "fgo数字Id有误");
                        return;
                    }
                    fgoId = Long.parseLong(etValue(amEtId));
                    if (blackPercent == -1) {
                        ToastUtils.displayShortToast(mContext, "请进行圣遗物检测");
                    } else {
                        if (blackPercent >= 0.85) {
                            amRlCharacter.setVisibility(View.VISIBLE);
                            amIvCharacter.setImageResource(R.mipmap.joan_alter_dislike);
                            amTvCharacter.setText("非气浓度都高成这样了还要抽卡？\n难道你是受虐狂吗？");
                            amTvCharacter.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_left_in));
                            amTvTime.setText("不换换圣遗物吗？");
                        }else{
                            if(blackPercent < 0.1){
                                amRlCharacter.setVisibility(View.VISIBLE);
                                amIvCharacter.setImageResource(R.mipmap.joan_alter_smile);
                                amTvCharacter.setText("欧气足的Master!\n快去迎接新Servant吧!");
                                amTvCharacter.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_left_in));
//                                textAnimation(fmTvCharacter);
                            }
                            double result = fgoId * blackPercent;
                            String value = result + "";
                            Calendar ca = Calendar.getInstance();//可以对每个时间域单独修改
                            int hour = ca.get(Calendar.HOUR_OF_DAY);
                            int minute = ca.get(Calendar.MINUTE);
                            if (isNumeric(String.valueOf(value.charAt(0)))) {
                                hour += Integer.parseInt(String.valueOf(value.charAt(0)));
                            } else {
                                hour += Integer.parseInt(String.valueOf(value.charAt(1)));
                            }
                            if (hour > 24) {
                                hour = hour - 24;
                            }
                            if (isNumeric(String.valueOf(value.charAt(5)))) {
                                minute += Integer.parseInt(String.valueOf(value.charAt(5)));
                            } else {
                                minute += Integer.parseInt(String.valueOf(value.charAt(6)));
                            }
                            if (minute > 60) {
                                minute = minute - 60;
                            }
                            String min = minute + "";
                            if (minute >= 0 && minute <= 9) {
                                min = "0" + minute;
                            }
                            amTvTime.setText(hour + ":" + min);
                        }
                    }
                } else {
                    ToastUtils.displayShortToast(mContext, "请输入fgo数字id");
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
                    newcolor2bw();
//                    if (imageUri == null) {
//                        ToastUtils.displayShortToast(mContext, "图没了，别测了，也别抽卡了，本玄学救不了你");
//                    } else {
//                        color2bw(mContext, imageUri);
//                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imgByPhoto();
                }else{
                    ToastUtils.displayShortToast(mContext,"您拒绝了权限");
                }
                break;

        }
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private boolean verifyId(String str) {
        if (str.length() >= 9) {
            return true;
        } else {
            return false;
        }
    }

    //拍照获得照片
    public void imgByPhoto() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/screenTest.jpg";
        File outputImage = new File(path);
        try{
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(mContext,"com.phantancy.fgocalc.fileprovider",outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    //Uri转真实路径
    public String getRealFilePathFromUri(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                        try {
                            //4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
                            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                                cursor.close();
                            }
                        } catch (Exception e) {
                        }
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void newcolor2bw(){
        try{
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            Bitmap photo = ImageUtils.reduce(bitmap, 1200, 900, true);
            bitmap.recycle();
            amIvThing.setImageBitmap(photo);
            photo = ImageUtils.convertToBMW(photo, 1200, 900, 55);
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMinimumFractionDigits(2);
            blackPercent = ImageUtils.blackArea(photo);
            if (blackPercent == 0) {
                blackPercent = 0.01;
            }
            String black = nf.format(blackPercent);
            amTvPercent.setText("非气浓度: " + black);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            ToastUtils.displayShortToast(mContext, "图没了，别测了，也别抽卡了，本玄学救不了你");
        }
    }

    //二值化图片
    public void color2bw(Context context, Uri pUri) {
        try {
            //将保存在本地的图片取出并缩小后显示在界面上
            String picPath = getRealFilePathFromUri(context, pUri);
            //从文件夹读取
//            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +
//                    "/hcvbox_img/hcvboxCache.jpg");
            //根据Uri转的路径读取
            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
            Bitmap photo = ImageUtils.reduce(bitmap, 1200, 900, true);
            //从data里取bitmap的可能问题是获取图片不清晰，不如直接从文件获取
            //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
            bitmap.recycle();
            amIvThing.setImageBitmap(photo);
            photo = ImageUtils.convertToBMW(photo, 1200, 900, 55);
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMinimumFractionDigits(2);
            blackPercent = ImageUtils.blackArea(photo);
            if (blackPercent == 0) {
                blackPercent = 0.01;
            }
            String black = nf.format(blackPercent);
            amTvPercent.setText("非气浓度: " + black);
//            Uri photoUri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), photo, null,null));
        } catch (Exception e) {
            ToastUtils.displayShortToast(mContext, "图片异常，别测了，也别抽卡了，本玄学救不了你");
        }
    }

    public void textAnimation(TextView textScore){
        textScore.setVisibility(View.VISIBLE);
        TranslateAnimation tAnimation = new TranslateAnimation(0f,0f,0f,-80); //位移动画效果
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
