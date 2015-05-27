package cn.fuhl.taijiquan.http;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import cn.fuhl.taijiquan.bean.UserBean;
import cn.fuhl.taijiquan.helper.AccountHelper;

public class ApiRequest {
    /**
     * 基本参数变量
     *
     * @return
     */
    public static RequestParams getBaseParams() {
        RequestParams params = new RequestParams();
        return params;
    }

    /**
     * 登陆后参数变量
     *
     * @return
     */
    public static RequestParams getBaseLoginParams() {
        RequestParams params = getBaseParams();
        UserBean user = AccountHelper.getUser();
        if (user != null && TextUtils.isEmpty(user.getToken()) == false) {
            params.put("uid", user.getUid());
            params.put("token", user.getToken());
        }
        return params;
    }


    /**
     * 登录
     *
     * @param username   用户名
     * @param password   密码
     * @param handler    回调
     */
    public static RequestHandle login(Context context, String username, String password,JsonHttpHandler handler) {
        RequestParams params = getBaseParams();
        params.put("username", username);
        params.put("password", password);
        return AsyncHttp.post(context, ApiUrl.getAbsoluteUrl(ApiUrl.LOGIN), params, handler);
    }

    /**
     * 注册
     *
     * @param username   用户名
     * @param password   密码
     * @param handler    回调
     */
    public static RequestHandle register(Context context, String username,
                                         String password,JsonHttpHandler handler) {
        RequestParams params = getBaseParams();
        params.put("username", username);
        params.put("password", password);
        params.put("repassword", password);
        return AsyncHttp.post(context, ApiUrl.getAbsoluteUrl(ApiUrl.REGISTER), params, handler);
    }

    /**
     * 删除收藏
     *
     * @param post_id    用户id
     * @param handler    回调
     */
    public static RequestHandle delFavorite(Context context,int post_id,
                                              AsyncHttpResponseHandler handler) {
        RequestParams params = getBaseLoginParams();
        params.put("post_id", post_id);
        return AsyncHttp.get(context, ApiUrl.getAbsoluteUrl(ApiUrl.DEVCOLLECTION), params, handler);
    }

    /**
     * 添加收藏
     *
     * @param post_id    用户id
     * @param handler    回调
     */
    public static RequestHandle addFavorite(Context context,int post_id,
                                             AsyncHttpResponseHandler handler) {
        RequestParams params = getBaseLoginParams();
        params.put("post_id", post_id);
        return AsyncHttp.get(context, ApiUrl.getAbsoluteUrl(ApiUrl.COLLECTIONADD), params, handler);
    }

    /**
     * 获取收藏列表
     *
     * @param page       第几页
     * @param limit      每一页条数
     * @param handler    回调
     */
    public static RequestHandle getFavoriteList(Context context,int page,int limit,JsonHttpHandler handler) {
        RequestParams params = getBaseLoginParams();
        params.put("page", page+"");
        params.put("limit", limit+"");
        return AsyncHttp.get(context, ApiUrl.getAbsoluteUrl(ApiUrl.GETCOLLECTIONLIST),
                params, handler);
    }

    public static RequestHandle publicComment(Context context,int post_id,
                                              String comment_content,
                                              JsonHttpHandler handler){
        RequestParams params = new RequestParams();
        params.put("uid", AccountHelper.getUser().getUid());
        params.put("token", AccountHelper.getUser().getToken());
        params.put("post_id", post_id);
        params.put("comment_content", comment_content);
        return AsyncHttp.post(ApiUrl.getAbsoluteUrl(ApiUrl.GETCOMMENTADD),
                params, handler);
    }

}
