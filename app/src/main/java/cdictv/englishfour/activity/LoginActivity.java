package cdictv.englishfour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.litepal.LitePal;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.englishfour.R;
import cdictv.englishfour.javabean.Userinfo;
import cdictv.englishfour.utils.DBManager;
import cdictv.englishfour.utils.DataUtil;
import cdictv.englishfour.utils.SavePassword;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {
    //对控件进行findviewById
    @InjectView(R.id.et_name)
    EditText mEtName;
    @InjectView(R.id.et_password)
    EditText mEtPassword;
    @InjectView(R.id.tv_login)
    TextView mTvLogin;
    @InjectView(R.id.tv_register)
    TextView mTvRegister;
    private DBManager mDBManager;
   private String name;
   private String psd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        initdatabase();
    }

    private void initdatabase() {
        mDBManager=new DBManager(this);
        mDBManager.openDatabase();
        mDBManager.closeDatabase();
    }

    @OnClick({R.id.tv_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //登录点击事件
            case R.id.tv_login:
                login();
                break;
            //注册点击事件
            case R.id.tv_register:
               startActivity(new Intent(this,RegisterActivity.class));

                break;
        }
    }
    private void login() {
        name=mEtName.getText().toString().trim();
        psd=mEtPassword.getText().toString().trim();
        if (EmptyUtils.isEmpty(name)) {
            ToastUtils.showShort("请输入用户名或者电话");
            return;
        }
        if (EmptyUtils.isEmpty(psd)) {
            ToastUtils.showShort("请输入密码");
            return;
        }


        List<Userinfo> mData= selet();
        DataUtil dataUtil=new DataUtil(mData);
        if(mData.size()!=0){
            if (dataUtil.getUserData().getPassword().toString().trim().equals(psd)&&
                    (dataUtil.getUserData().getPhone().toString().trim().equals(name) ||dataUtil.getUserData().getName().toString().trim().equals(name))){
               String phone= dataUtil.getUserData().getPhone().toString().trim();
//                        保存密码与电话到sharePrefrences
                SavePassword.saveUserInfo(LoginActivity.this,phone , psd);
//                        将isLogin设置为true
                SavePassword sp = new SavePassword(LoginActivity.this);
                sp.setState();
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
                Log.i("=====","LoginActivity关闭2");
                finish();
            }else {
                ToastUtils.showShort("密码错误");
            }
        }else {
            ToastUtils.showShort("账号或密码错误");
        }
    }

    private List<Userinfo> selet() {
        List<Userinfo> data1;
        List<Userinfo> data2;
       data1 =LitePal.select("phone", "password", "name").where("password=" + "'" + psd + "'").where("phone=" + "'" + name + "'").find(Userinfo.class);
        data2=LitePal.select("phone","password","name").where("password="+"'"+psd+"'").where("name="+"'"+name+"'").find(Userinfo.class);
        if(data1!=null){
//             Toast.makeText(getApplicationContext(),"电话号码",Toast.LENGTH_LONG).show();
            return data1;
        }
        if (data2!=null){
//            Toast.makeText(getApplicationContext(),"姓名",Toast.LENGTH_LONG).show();
            return data2;
        }
        return null;
    }
}