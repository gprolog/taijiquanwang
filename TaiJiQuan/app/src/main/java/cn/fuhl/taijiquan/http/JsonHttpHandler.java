package cn.fuhl.taijiquan.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cn.fuhl.taijiquan.R;
import cn.fuhl.taijiquan.helper.ToastHelper;

public class JsonHttpHandler extends AsyncHttpResponseHandler {
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private boolean isShowProgressDialog = false;
    private boolean isShowErrorMessage = true;
    private boolean isProgressDialogCancleable = true;
    private String mDialogMessage;

    public JsonHttpHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String responseString = null;
        try {
            responseString = new String(responseBody, "UTF-8");

            JSONObject response = null;
            response = new JSONObject(responseString);
            int status = response.getInt("status");
            String message = response.getString("msg");
            if (status == 1) {
                onDo(responseBody);
                try {
                    JSONObject data = response.getJSONObject("data");
                    onDo(data);
                    onDo(response.getString("data"));
                } catch (JSONException e) {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        onDo(data);
                        onDo(response.getString("data"));
                    } catch (JSONException e2) {
                        onDo(response.getString("data"));
                    }

                }
            } else {
                onFail(message);
                onFail(status, message);

            }
        } catch (Exception e) {
            e.printStackTrace();
            onFail("数据出错");
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if (mContext != null) {
            try {
                if (statusCode == 0) {
                    ToastHelper.ShowToast(R.string.error_http_fail_connet_server, mContext);
                    onFail(mContext.getString(R.string.error_http_fail_connet_server));
                } else if (statusCode >= 400) {
                    ToastHelper.ShowToast(R.string.error_http_request, mContext);
                    onFail(mContext.getString(R.string.error_http_request));
                } else if (statusCode >= 500) {
                    ToastHelper.ShowToast(R.string.error_http_server_error, mContext);
                    onFail(mContext.getString(R.string.error_http_server_error));
                } else {
                    ToastHelper.ShowToast(R.string.error_http_server_busy, mContext);
                    onFail(mContext.getString(R.string.error_http_server_busy));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                onFail("");
            }
        }
    }


    public void onDo(JSONObject response) {

    }

    public void onDo(JSONArray response) {

    }

    public void onDo(String response) {

    }
    public void onDo(byte[] response) {

    }
    public void onFail(String msg) {
        if (TextUtils.isEmpty(msg) == false && mContext != null) {
            if (isShowErrorMessage == true) {
                ToastHelper.ShowToast(msg, mContext);
            }
        }
    }

    public void onFail(int status, String msg) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (isShowProgressDialog) {
            mProgressDialog = new ProgressDialog(mContext);
            if (TextUtils.isEmpty(mDialogMessage)) {
                mProgressDialog.setMessage(mContext.getString(R.string.message_loading));
            } else {
                mProgressDialog.setMessage(mDialogMessage);
            }
            mProgressDialog.setCanceledOnTouchOutside(false);
            if (isProgressDialogCancleable == false) {
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }

    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (isShowProgressDialog && mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JsonHttpHandler setShowProgressDialog(String message) {
        isShowProgressDialog = true;
        this.mDialogMessage = message;
        return this;
    }

    public void setProgressDialogCancleable(boolean cancleable) {
        isProgressDialogCancleable = cancleable;
    }

    public void showProgressDialog() {
        isShowProgressDialog = true;
    }

    public void hideErrorMessage() {
        isShowErrorMessage = false;
    }

}
