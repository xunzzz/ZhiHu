package com.zx.zhihu.network;

import com.squareup.okhttp.Request;

/**
 * Created by zhangxun on 2015/9/18.
 */
public abstract class ResultCallback {

//    Type mType;
//
//    public ResultCallback() {
//        mType = getSuperclassTypeParameter(getClass());
//    }
//
//    static Type getSuperclassTypeParameter(Class<?> subClass){
//        Type superclass = subClass.getGenericSuperclass();
//        if (superclass instanceof Class){
//            throw new RuntimeException("Missing type parameter");
//        }
//        ParameterizedType parameterizedType = (ParameterizedType) superclass;
//        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
//    }

    public void onBefore(Request request){

    }

    public abstract void onSuccess(String data);

    public abstract void onError(Request request, Exception e);

    public void onAfter(){

    }
}
