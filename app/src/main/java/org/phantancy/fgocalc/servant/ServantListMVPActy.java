package org.phantancy.fgocalc.servant;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.base.BaseActy;
import org.phantancy.fgocalc.common.ActivityCollector;
import org.phantancy.fgocalc.util.ActivityUtils;
import org.phantancy.fgocalc.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;


/**
 * Created by PY on 2016/10/31.
 */
public class ServantListMVPActy extends BaseActy{

    @BindView(R.id.aslm_fl_main)
    FrameLayout aslmFlMain;
    private long exitTime = 0;//用于记录退出时间
    private ServantListPresenter mPresenter;
    private final int REQUEST_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_servant_list_mvp);
        ButterKnife.bind(this);
        ServantListFragment slFrag = (ServantListFragment)getSupportFragmentManager().findFragmentById(R.id.aslm_fl_main);
        if (slFrag == null) {
            slFrag = ServantListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),slFrag,R.id.aslm_fl_main);
        }
        mPresenter = new ServantListPresenter(slFrag,ctx);
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType. E_UM_NORMAL);
        MobclickAgent.setSessionContinueMillis(1000);
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else{
                    ToastUtils.displayShortToast(ctx,"您拒绝了权限");
                }
                break;
        }
    }

    //重载返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(ctx, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
