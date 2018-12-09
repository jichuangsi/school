package com.jichuangsi.school.eaxmservice.Utils;

import java.util.List;

public final class CommonUtils {

    private CommonUtils(){};

    public final static Boolean decideList(List list){
        if(list == null||!(list.size()>0)){
            return true;
        }
        return false;
    }
}
