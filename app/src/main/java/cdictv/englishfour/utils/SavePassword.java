package cdictv.englishfour.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;



public class SavePassword {
    Context context;
    public static boolean saveUserInfo(Context context,String phone,String password){
        SharedPreferences sp=context.getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("phone",phone);
        editor.putString("password",password);
        editor.commit();
        return true;
    }

    public static Map<String,String> getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
        String phone = sp.getString("phone", "");
        String password = sp.getString("password", "");
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("phone", phone);
        userMap.put("password", password);
        return userMap;
    }
    public SavePassword(Context context)
    {
        this.context = context;
    }
    /****设置状态  false为安装后第一次登录，true为已经登录过****/
    public void setState()
    {
        SharedPreferences sp = context.getSharedPreferences("save.himi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.commit();
    }
    /***获取状态***/
    public boolean getState()
    {
        SharedPreferences sp = context.getSharedPreferences("save.himi", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("isLogin",false);
        return b;
    }

}
