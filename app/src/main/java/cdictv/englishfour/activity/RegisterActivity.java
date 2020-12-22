package cdictv.englishfour.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cdictv.englishfour.R;
import cdictv.englishfour.javabean.Userinfo;
import cdictv.englishfour.utils.MyDialog;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    @InjectView(R.id.register_name)
    EditText mRegisterName;
    @InjectView(R.id.register_pwd)
    EditText mRegisterPwd;
    @InjectView(R.id.register_pwdt)
    EditText mRegisterPwdt;
    @InjectView(R.id.man)
    RadioButton mMan;
    @InjectView(R.id.secrecy)
    RadioButton mSecrecy;
    @InjectView(R.id.woman)
    RadioButton mWoman;
    @InjectView(R.id.register_phone)
    EditText mRegisterPhone;
    @InjectView(R.id.register_code)
    EditText mRegisterCode;
    @InjectView(R.id.register_school)
    EditText mRegisterSchool;
    @InjectView(R.id.register_sex)
    RadioGroup mRegisterSex;
    @InjectView(R.id.tv_get_code)
    TextView tv;
    private String code;
    private String gender;
    private String phone;

    private void setGender(String gender) {
        this.gender = gender;
    }

    private String getGender() {
        return gender;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        initlinter();

        mRegisterSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.man:
                        setGender("男");
                        break;
                    case R.id.woman:
                        setGender("女");
                        break;
                    case R.id.secrecy:
                        setGender("保密");
                        break;
                    default:
                        setGender("保密");
                        break;
                }
            }
        });
    }
    private void initlinter() {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });
    }
    /**
     * 注册
     */
    private void register() {
        String name = mRegisterName.getText().toString().trim();

        String password = mRegisterPwd.getText().toString().trim();
        String passwordTow = mRegisterPwdt.getText().toString().trim();
        String school = mRegisterSchool.getText().toString().trim();
        String codet = mRegisterCode.getText().toString().trim();
        if (school.equals("")) {
            school = "保密";
        }
        String psw_regex = "\\w.{5,15}";


        if (!name.equals("")) {
            if (!phone.equals("")) {
                if (!password.equals("") && !passwordTow.equals("")) {
                    if (EmptyUtils.isEmpty(phone)) {
                        Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if (password.matches(psw_regex)) {
                            if (!password.equals(passwordTow)) {
                                Toast.makeText(this, "两次密码不一致", Toast.LENGTH_LONG).show();

                            } else {
                                if (codet.equals(code)) {
                                    Userinfo userData = new Userinfo();
                                    List<Userinfo> mData = LitePal.select("phone").where("phone = " + "'" + phone + "'").find(Userinfo.class);
//                                    DataUtil dataUtil=new DataUtil(mData);
//                                    如果该phone存在于数据库中，则账号已存在
                                    if (mData.size() == 0) {
                                        userData.setName(name);
                                        if (getGender() == null) {
                                            userData.setGender("保密");
                                        } else {
                                            userData.setGender(getGender());
                                        }
                                        userData.setPassword(password);
                                        userData.setPhone(phone);
                                        userData.setSchool(school);

                                        boolean flag = userData.save();
                                        if (flag) {
                                            final MyDialog myDialog = new MyDialog(RegisterActivity.this);
                                            myDialog.setTitle("温馨提示");
                                            myDialog.setMessage("注册成功，是否返回登录界面？");
                                            myDialog.setYesOnclickListener("是", new MyDialog.onYesOnclickListener() {
                                                @Override
                                                public void onYesClick() {
                                                    RegisterActivity.this.finish();
                                                    myDialog.dismiss();
                                                }
                                            });
                                            myDialog.setNoOnclickListener("否", new MyDialog.onNoOnclickListener() {
                                                @Override
                                                public void onNoClick() {
                                                    myDialog.dismiss();
                                                }
                                            });
                                            myDialog.show();
                                        } else {
                                            Toast.makeText(this, "注册失败", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(this, "账号已存在", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(this, "验证码错误", Toast.LENGTH_LONG).show();
                                }
                            }
//
                        } else {
                            Toast.makeText(this, "密码只能是由6到16位的数字、字母或符号组成", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_LONG).show();
        }
    }


    private void getCode() {
        phone = mRegisterPhone.getText().toString().trim();
        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtils.showShort("请输入正确的电话号码");
            return;
        }

        final TextView tv = findViewById(R.id.tv_get_code);
        tv.setEnabled(false);

        code = "" + (int) ((Math.random()) * 1000000);
        OkGo.post("http://v.juhe.cn/sms/send")
                .params("mobile", phone)
                .params("tpl_id", "116752")
                .params("key", " 3647810c018a3b9c4c17b23c53aa8c4d")
                .params("tpl_value", "%23code%23%3D" + code)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jo = new JSONObject(s);
                            if ((jo.getInt("error_code") + "").equals("0")) {
                                Toast.makeText(RegisterActivity.this, "验证码发送成功！！！", Toast.LENGTH_SHORT).show();
                                    new Thread(){
                                        @Override
                                        public void run() {
                                            for (int i = 60; i >0; i--) {
                                                final int finalI = i;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        tv.setText(finalI +"秒后重发");
                                                        if(finalI==0){
                                                            tv.setEnabled(true);
                                                            return;
                                                        }

                                                    }
                                                });
                                                SystemClock.sleep(1000);
                                            }
                                        }
                                    }.start();






                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick({ R.id.tv_register, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_register:
                register();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }


}
