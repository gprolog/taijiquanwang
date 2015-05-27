package cn.fuhl.taijiquan.http;

public class ApiUrl {

    public static final String DEV_BASE_URL = "http://hongliang.duapp.com/index.php/";
    public static final String ONLINE_BASE_URL = "http://hongliang.duapp.com/index.php/";

    public static String getAbsoluteUrl(String relativeUrl) {
        String url = DEV_BASE_URL + relativeUrl;
        return url;
    }

    public static final String LOGIN = "User/login";
    public static final String REGISTER = "User/register";
    public static final String COLLECTIONADD="UserCollection/add";
    public static final String GETCOLLECTIONLIST="UserCollection/getList";
    public static final String DEVCOLLECTION="UserCollection/cancel";
    public static final String GETCOMMENTADD="";

}
