package org.phantancy.fgocalc.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.util.ImageUtils;
import org.phantancy.fgocalc.util.SharedPreferencesUtils;
import org.phantancy.fgocalc.util.ToastUtils;

import java.io.File;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PY on 2017/3/3.
 */
public class MetaphysicsFragment extends BaseFragment {

    @BindView(R.id.fm_btn_screen)
    Button fmBtnScreen;
    @BindView(R.id.fm_tv_percent)
    TextView fmTvPercent;
    @BindView(R.id.asl_rb_believe)
    RadioButton aslRbBelieve;
    @BindView(R.id.asl_rb_unbelieve)
    RadioButton aslRbUnbelieve;
    @BindView(R.id.asl_rg_method)
    RadioGroup aslRgMethod;
    @BindView(R.id.fm_cb_remember)
    CheckBox fmCbRemember;
    @BindView(R.id.fm_et_id)
    EditText fmEtId;
    @BindView(R.id.fm_btn_time)
    Button fmBtnTime;
    @BindView(R.id.fm_tv_time)
    TextView fmTvTime;
    @BindView(R.id.fm_iv_thing)
    ImageView fmIvThing;
    @BindView(R.id.fm_iv_character)
    ImageView fmIvCharacter;
    @BindView(R.id.fm_tv_character)
    TextView fmTvCharacter;
    @BindView(R.id.fm_rl_character)
    RelativeLayout fmRlCharacter;
    private Uri pUri;
    private long fgoId;
    private double blackPercent = -1;
    private boolean ifRemember;
    private AnimationSet textAnimationSet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_metaphysics, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = getActivity();
        init();
    }

    private void init() {
        fgoId = (long) SharedPreferencesUtils.getParam(ctx, "fgoId", 0l);
        if (fgoId > 0) {
            fmCbRemember.setChecked(true);
            fmEtId.setText(fgoId + "");
        }
        setListener();
    }

    private void setListener() {
        fmBtnScreen.setOnClickListener(this);
        fmBtnTime.setOnClickListener(this);
        fmRlCharacter.setOnClickListener(this);
        fmCbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (notEmpty(etValue(fmEtId))) {
                        fgoId = Long.parseLong(etValue(fmEtId));
                        if (!verifyId(etValue(fmEtId))) {
                            fmCbRemember.setChecked(false);
                            ToastUtils.displayShortToast(ctx, "fgo数字Id有误");
                            return;
                        } else {
                            SharedPreferencesUtils.setParam(ctx, "fgoId", fgoId);
                        }
                    } else {
                        fmCbRemember.setChecked(false);
                        ToastUtils.displayShortToast(ctx, "请输入fgo数字id");
                    }
                } else {
                    SharedPreferencesUtils.setParam(ctx, "fgoId", 0l);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fm_btn_screen:
                pUri = imgByPhoto();
                break;
            case R.id.fm_btn_time:
                if (notEmpty(etValue(fmEtId))) {
                    if (!verifyId(etValue(fmEtId))) {
                        ToastUtils.displayShortToast(ctx, "fgo数字Id有误");
                        return;
                    }
                    fgoId = Long.parseLong(etValue(fmEtId));
                    if (blackPercent == -1) {
                        ToastUtils.displayShortToast(ctx, "请进行圣遗物检测");
                    } else {
                        if (blackPercent >= 0.85) {
                            fmRlCharacter.setVisibility(View.VISIBLE);
                            fmIvCharacter.setImageResource(R.drawable.joan_alter_dislike);
                            fmTvCharacter.setText("非气浓度都高成这样了还要抽卡？\n难道你是受虐狂吗？");
                            fmTvCharacter.setAnimation(AnimationUtils.loadAnimation(ctx,R.anim.push_left_in));
                            fmTvTime.setText("不换换圣遗物吗？");
                        }else{
                            if(blackPercent < 0.1){
                                fmRlCharacter.setVisibility(View.VISIBLE);
                                fmIvCharacter.setImageResource(R.drawable.joan_alter_smile);
                                fmTvCharacter.setText("欧气足的Master!\n快去迎接新Servant吧!");
                                fmTvCharacter.setAnimation(AnimationUtils.loadAnimation(ctx,R.anim.push_left_in));
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
                            fmTvTime.setText(hour + ":" + min);
                        }
                    }
                } else {
                    ToastUtils.displayShortToast(ctx, "请输入fgo数字id");
                }
                break;
            case R.id.fm_rl_character:
                fmRlCharacter.setVisibility(View.GONE);
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case 1:
                    if (pUri == null) {
                        ToastUtils.displayShortToast(ctx, "图没了，别测了，也别抽卡了，本玄学救不了你");
                    } else {
                        color2bw(ctx, pUri);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        if (str.length() == 12) {
            return true;
        } else {
            return false;
        }
    }

    //拍照获得照片
    public Uri imgByPhoto() {
        /***
         * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
         * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
         * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
         */
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "";
        File path1 = new File(path);
        if (!path1.exists()) {
            path1.mkdirs();
        }
        String tempPath = path + "screenTest.jpg";
        File file = new File(tempPath);
        Uri photoUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, 1);
        return photoUri;
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
            fmIvThing.setImageBitmap(photo);
            photo = ImageUtils.convertToBMW(photo, 1200, 900, 55);
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMinimumFractionDigits(2);
            blackPercent = ImageUtils.blackArea(photo);
            if (blackPercent == 0) {
                blackPercent = 0.01;
            }
            String black = nf.format(blackPercent);
            fmTvPercent.setText("非气浓度: " + black);
//            Uri photoUri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), photo, null,null));
        } catch (Exception e) {
            ToastUtils.displayShortToast(ctx, "图片异常，别测了，也别抽卡了，本玄学救不了你");
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
