package cdictv.englishfour.utils;


import java.util.List;

import cdictv.englishfour.javabean.Userinfo;

/**
 * Created by Administrator on 2017/12/30 0030.
 */

public class DataUtil {
    private List<Userinfo> ud;

    public DataUtil(List<Userinfo> ud) {
        this.ud = ud;
    }

    public Userinfo getUserData(){
        Userinfo userData = null;

        for(int x=0;x<ud.size();x++){
            if(ud.get(x)!=null){
                userData=ud.get(x);
            }
        }
        return userData;
    }
}
