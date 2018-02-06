package mjoys.com.aopprograming;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 登录的点击事件
     * 常用方法判断网络连接
     * @param v
     */
    public void login_common(View v) {
        //CheckNetUtil网络处理的工具类
        if (CheckNetUtil.isNetworkAvailable(this)) {
            //如果有网，继续操作 比如处理登录的逻辑
            Intent intent = new Intent(this,ActivityLoginSuccess.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "请检查您的网络连接！", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 登录的点击事件
     * 使用AOP来判断网络连接
     * @param v
     */
    @CheckNet
    public void login_by_aop(View v) {
        Intent intent = new Intent(this,ActivityLoginSuccess.class);
        startActivity(intent);
    }

}
