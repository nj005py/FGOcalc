package org.phantancy.fgocalc.dialog;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.util.ToolCase;

/**
 * Created by HATTER on 2017/8/8.
 */

public class FeedbackDialog extends Dialog implements View.OnClickListener{
    private Context ctx;
    private TextView tvEmail,tvBbs,tvB,tvGithub,tvTieba,tvQQ;

    public FeedbackDialog(@NonNull Context context) {
        super(context,R.style.dialog);
        setContentView(R.layout.diag_feedback);
        ctx = context;
        tvEmail = (TextView)findViewById(R.id.df_tv_email);
        tvBbs = (TextView)findViewById(R.id.df_tv_bbs);
        tvB = (TextView)findViewById(R.id.df_tv_b);
        tvGithub = findViewById(R.id.df_tv_github);
        tvTieba = findViewById(R.id.df_tv_tieba);
        tvQQ = findViewById(R.id.df_tv_qq);
        tvEmail.setOnClickListener(this);
        tvBbs.setOnClickListener(this);
        tvB.setOnClickListener(this);
        tvGithub.setOnClickListener(this);
        tvTieba.setOnClickListener(this);
        tvQQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url;
        switch (v.getId()) {
            case R.id.df_tv_email:
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:phantancy@hotmail.com"));// only email apps should handle this
                i.putExtra(Intent.EXTRA_SUBJECT, "fgo计算器反馈");
                i.putExtra(Intent.EXTRA_TEXT, "我想反馈");
                try {
                    ctx.startActivity(Intent.createChooser(i, "邮件反馈"));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(ctx, "没找到Email相关应用.", Toast.LENGTH_SHORT).show();
                }
                dismiss();
                break;
            case R.id.df_tv_bbs:
                url = Uri.parse("http://bbs.ngacn.cc/read.php?tid=13304364");
                intent.setData(url);
                ctx.startActivity(intent);
                dismiss();
                break;
            case R.id.df_tv_b:
                url = Uri.parse("https://space.bilibili.com/532252/#/");
                intent.setData(url);
                ctx.startActivity(intent);
                dismiss();
                break;
            case R.id.df_tv_github:
                url = Uri.parse("https://github.com/nj005py/FGOcalc/issues");
                intent.setData(url);
                ctx.startActivity(intent);
                dismiss();
                break;
            case R.id.df_tv_tieba:
                url = Uri.parse("https://tieba.baidu.com/p/5551126341");
                intent.setData(url);
                ctx.startActivity(intent);
                dismiss();
                break;
            case R.id.df_tv_qq:
                ToolCase.copy2Clipboard(ctx,"422969398","群号已复制剪贴板");
                break;
        }
    }
}
