package com.gelistirmen.finance.data.remote.base;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class VolleyDao {
    private RequestQueue requestQueue;
    private String basicUrl;
    private String finalUrl;
    protected int method; // Request.Method.GET | .POST | ...;
    private VolleyDaoListener listener = null;
    protected DataHandler dataHandler = null;

    protected Map<String, String> headers = new HashMap<>();

    protected JSONObject bodyDataObject = null;
    protected JSONArray bodyDataArray = null;
    protected Map<String, String> urlData = new HashMap<>();
    private ArrayList<byte[]> multipartDataList = new ArrayList<>();
    private ArrayList<String> multipartDataKeyList = new ArrayList<>();
    private ArrayList<String> multipartDataFileNameList = new ArrayList<>();
    private ArrayList<String> multipartDataMimeTypeList = new ArrayList<>();
    private Map<String, String> multipartParams = null;
    protected boolean acceptEmptyResponse = false;

    private RequestType requestType;
    protected String contentType;

    private String errorCodeKey;
    private String errorMessageKey;
    protected int timeout = 30000; // request timeout in milliseconds
    protected int retryCount = 0; // request retry count

    public interface VolleyDaoListener {
        void daoDidSuccess(VolleyDao dao);

        void daoDidSuccess(VolleyDao dao, Object data);

        void daoDidFail(VolleyDao dao, Error error);
    }

    protected interface DataHandler {
        /**
         * @return JSONObject | JSONArray
         */
        Object handleJsonObjectData(JSONObject data);
    }

    /**
     * @param url Full URL of request
     * @param method com.android.volley.Request.Method.GET | POST | ...
     * @param listener
     * @param errorCodeKey Key word for error json parsing
     * @param errorMessageKey Key word for error json parsing
     */
    public VolleyDao(String url, int method, VolleyDaoListener listener, String errorCodeKey, String errorMessageKey) {
        this.basicUrl = url;
        this.method = method;
        this.listener = listener;
        this.errorCodeKey = errorCodeKey;
        this.errorMessageKey = errorMessageKey;
        this.requestQueue = MyApplication.instance.requestQueue;
        this.setRequestType(RequestType.JsonObjectRequest);
    }

    protected void setRequestType(RequestType requestType) {
        this.requestType = requestType;
        if (this.requestType == RequestType.JsonObjectRequest || this.requestType == RequestType.JsonArrayRequest)
            this.setContentType("application/json; charset=utf-8");
        else if (this.requestType == RequestType.StringRequest)
            this.setContentType("text/plain; charset=utf-8");
//        else if (this.requestType == RequestType.MultipartRequest)
//            this.setContentType("multipart/form-data");
    }

    protected void setContentType(String contentType) {
        this.contentType = contentType;
    }

    protected void setMultipartData(byte[] data, String key, String fileName, String mimeType) {
        this.multipartDataList.add(data);
        this.multipartDataKeyList.add(key);
        this.multipartDataFileNameList.add(fileName);
        this.multipartDataMimeTypeList.add(mimeType);
    }

    protected void addMultipartParameter(String key, String value) {
        if (this.multipartParams == null)
            this.multipartParams = new HashMap<>();
        this.multipartParams.put(key, value);
    }

    public void execute() {
        this.buildRequest();
        if (this.requestType == RequestType.JsonObjectRequest) {
            if (this.bodyDataArray != null)
                this.startJsonObjectRequestWithJsonArray();
            else
                this.startJsonObjectRequest();
        } else if (this.requestType == RequestType.JsonArrayRequest)
            this.startJsonArrayRequest();
        else if (this.requestType == RequestType.StringRequest)
            this.startStringRequest();
        else if (this.requestType == RequestType.MultipartRequest)
            this.startMultipartRequest();
        else if (this.requestType == RequestType.DownloadRequest)
            this.startDownloadRequest();

        Log.i("VOLLEY REQUEST URL >", finalUrl);
        Log.i("VOLLEY REQUEST TOKEN >", Cache.getAccessToken());
        if (this.method == Request.Method.POST || this.method == Request.Method.PUT) {
            if (this.bodyDataObject != null)
                Log.i("VOLLEY REQUEST DATA >", this.bodyDataObject.toString());
            else if (this.bodyDataArray != null)
                Log.i("VOLLEY REQUEST DATA >", this.bodyDataArray.toString());
        }
    }

    protected void buildRequest() {
        boolean isFirst = true;
        this.finalUrl = this.basicUrl;
        for (Map.Entry<String, String> param : this.urlData.entrySet()) {
            this.finalUrl += (isFirst ? "?" : "&") + param.getKey() + "=" + param.getValue();
            isFirst = false;
        }
    }

    private void startJsonObjectRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(this.method, this.finalUrl, this.bodyDataObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("VOLLEY REQUEST URL <", finalUrl);
                        Log.i("VOLLEY RESPONSE DATA <", new String(response.toString()));
                        Object data = dataHandler == null ? response : dataHandler.handleJsonObjectData(response);
                        Error error = null;
                        if (data instanceof JSONObject)
                            error = VolleyDao.this.handleDataError((JSONObject) data);
                        if (error == null)
                            VolleyDao.this.onAfterSuccessRequest(data);
                        else
                            VolleyDao.this.onAfterFailedRequest(error);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY REQUEST URL <", finalUrl);
                        Log.e("VOLLEY RESPONSE DATA <", new String(error.networkResponse.toString()));
                        handleVolleyError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return VolleyDao.this.headers;
            }
            @Override
            public String getBodyContentType() {
                return contentType;
            }
        };
        jsonObjectRequest.setShouldCache(false);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(this.timeout, this.retryCount, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(jsonObjectRequest);
    }

    private void startJsonObjectRequestWithJsonArray() {
        JsonObjectRequestWithJsonArray jsonObjectRequest = new JsonObjectRequestWithJsonArray(this.method, this.finalUrl, this.bodyDataArray,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("VOLLEY REQUEST URL <", finalUrl);
                        Log.i("VOLLEY RESPONSE DATA <", new String(response.toString()));
                        Object data = dataHandler == null ? response : dataHandler.handleJsonObjectData(response);
                        Error error = null;
                        if (data instanceof JSONObject)
                            error = VolleyDao.this.handleDataError((JSONObject) data);
                        if (error == null)
                            VolleyDao.this.onAfterSuccessRequest(data);
                        else
                            VolleyDao.this.onAfterFailedRequest(error);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY REQUEST URL <", finalUrl);
                        Log.e("VOLLEY RESPONSE DATA <", new String(error.networkResponse.toString()));
                        handleVolleyError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return VolleyDao.this.headers;
            }

            @Override
            public String getBodyContentType() {
                return contentType;
            }
        };
        jsonObjectRequest.setShouldCache(false);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(this.timeout, this.retryCount, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(jsonObjectRequest);
    }

    private void startJsonArrayRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(this.method, this.finalUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("VOLLEY REQUEST URL <", finalUrl);
                        Log.i("VOLLEY RESPONSE DATA <", new String(response.toString()));
                        VolleyDao.this.onAfterSuccessRequest(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY REQUEST URL <", finalUrl);
                        Log.e("VOLLEY RESPONSE DATA <", new String(error.networkResponse.toString()));
                        handleVolleyError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return VolleyDao.this.headers;
            }

            @Override
            public String getBodyContentType() {
                return contentType;
            }
        };
        jsonArrayRequest.setShouldCache(false);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(this.timeout, this.retryCount, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(jsonArrayRequest);
    }

    private void startStringRequest() {
        StringRequest stringRequest = new StringRequest(this.method, this.finalUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY REQUEST URL <", finalUrl);
                        Log.i("VOLLEY RESPONSE DATA <", new String(response));
                        Error error = VolleyDao.this.handleDataError(response);
                        if (error == null)
                            if (TextUtils.isEmpty(response))
                                VolleyDao.this.onAfterSuccessRequest();
                            else
                                VolleyDao.this.onAfterSuccessRequest(response);
                        else
                            VolleyDao.this.onAfterFailedRequest(error);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY REQUEST URL <", finalUrl);
                        Log.e("VOLLEY RESPONSE DATA <", new String(error.networkResponse.toString()));
                        handleVolleyError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return VolleyDao.this.headers;
            }
            @Override
            public String getBodyContentType() {
                return contentType;
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(this.timeout, this.retryCount, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(stringRequest);
    }

    private void startMultipartRequest() {
        VolleyMultipartRequest request = new VolleyMultipartRequest(this.finalUrl, this.headers, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.i("VOLLEY REQUEST URL <", finalUrl);
                        Log.i("VOLLEY RESPONSE DATA <", new String(response.data));
                        String dataString = new String(response.data);
                        Error error = null;
                        JSONObject jsonObject = null;
                        if (Method.isNotNullOrEmpty(dataString)) {
                            try {
                                jsonObject = new JSONObject(dataString);
                                error = VolleyDao.this.handleDataError(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (TextUtils.isEmpty(dataString) && acceptEmptyResponse)
                            VolleyDao.this.onAfterSuccessRequest();
                        if (error == null)
                            VolleyDao.this.onAfterSuccessRequest(jsonObject);
                        else
                            VolleyDao.this.onAfterFailedRequest(error);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY REQUEST URL <", finalUrl);
                        Log.e("VOLLEY RESPONSE DATA <", new String(error.networkResponse.toString()));
                        handleVolleyError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                if (multipartParams == null)
                    return new HashMap<>();
                else
                    return multipartParams;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
//                params.put(VolleyDao.this.multipartDataKey, new DataPart(VolleyDao.this.multipartDataFileName, VolleyDao.this.multipartData, VolleyDao.this.multipartDataMimeType));
//                params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));
                for (int i = 0; i < VolleyDao.this.multipartDataList.size(); i++)
                    params.put(VolleyDao.this.multipartDataKeyList.get(i), new DataPart(VolleyDao.this.multipartDataFileNameList.get(i), VolleyDao.this.multipartDataList.get(i), VolleyDao.this.multipartDataMimeTypeList.get(i)));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(this.timeout, this.retryCount, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.requestQueue.add(request);
    }

    private void startDownloadRequest() {
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(this.method, this.finalUrl, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("VOLLEY REQUEST URL <", finalUrl);
                Log.i("VOLLEY RESPONSE DATA <", new String(response));
                onAfterSuccessRequest(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        }, null);
        this.requestQueue.add(request);
    }

    private void handleVolleyError(VolleyError error) {
        Error dataError = null;
        if (error.networkResponse != null) {
            if (error.networkResponse.data != null) {
                String dataString = new String(error.networkResponse.data);
                if (Method.isNotNullOrEmpty(dataString)) {
                    try {
                        dataError = VolleyDao.this.handleDataError(new JSONObject(dataString));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dataError = null;
                    }
                    Log.e(this.getClass().toString() + " data", dataString);
                } else if (TextUtils.isEmpty(dataString) && acceptEmptyResponse)
                    VolleyDao.this.onAfterSuccessRequest();
            }
        }
        if (dataError == null) {
            if (error.getCause() instanceof JSONException && this.acceptEmptyResponse)
                VolleyDao.this.onAfterSuccessRequest();
            else {
                VolleyDao.this.onAfterFailedRequest(VolleyDao.this.getError(error));
                Log.e(this.getClass().toString(), error.toString());
            }
        } else {
            VolleyDao.this.onAfterFailedRequest(dataError);
            Log.e(this.getClass().toString(), error.toString());
        }
    }

    @Nullable
    protected Error handleDataError(JSONObject jsonObject) {
        return null;
//        try {
//            if (this.exists(jsonObject, "error_code")) {
//                String errorMessage = jsonObject.getString("message");
//                return new Error(ErrorType.Response_InternalServerError, errorMessage);
//            } else
//                return null;
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Nullable
    protected Error handleDataError(String data) {
        return null;
//        JSONObject jsonObject;
//        try {
//            jsonObject = new JSONObject(data);
//            String errorCode = jsonObject.getString("error_code");
//            if (!TextUtils.isEmpty(errorCode)) {
//                String errorMessage = jsonObject.getString("message");
//                return new Error(ErrorType.Response_InternalServerError, errorMessage);
//            } else
//                return null;
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Nullable
    protected Error handleNoResponseError() {
        return ErrorType.Unknown.getError();
    }

    protected void onAfterSuccessRequest() {
        if (this.listener != null) {
            this.listener.daoDidSuccess(this);
        }
    }

    protected void onAfterSuccessRequest(Object data) {
        if (this.listener != null) {
            this.listener.daoDidSuccess(this, data);
        }
    }

    protected void onAfterFailedRequest(Error error) {
        if (this.listener != null) {
            this.listener.daoDidFail(this, error);
        }
    }

    protected Error getError(VolleyError volleyError) {
        if (volleyError.networkResponse != null) {
            switch (volleyError.networkResponse.statusCode) {
                case 400:
                    return ErrorType.Response_BadRequest.getError();
                case 401:
                    return ErrorType.InvalidToken.getError();
                case 406:
                    return ErrorType.Response_NotAcceptable.getError();
                case 500:
                    return ErrorType.Response_InternalServerError.getError();
                default:
                    Error error = ErrorType.Unknown.getError();
                    error.message += " (" + Integer.toString(volleyError.networkResponse.statusCode) + ")";
                    return error;
            }
        } else {
            Error error = ErrorType.Unknown.getError();
            error.message = volleyError.getMessage(); // FIXME
            return error;
        }
    }

    protected Boolean exists(JSONObject jsonObject, String key) {
        if (! jsonObject.has(key)) {
            return false;
        } else {
            try {
                return (!jsonObject.getString(key).equals("null"));
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static class Error {
        public ErrorType type;
        public String message;

        public Error(ErrorType type, String message) {
            this.type = type;
            this.message = message;
        }
    }

    public enum RequestType {
        JsonObjectRequest,
        JsonArrayRequest,
        StringRequest,
        MultipartRequest,
        DownloadRequest
    }

    public enum ErrorType {
        Response_NoResponse,
        Response_BadRequest,
        Response_InternalServerError,
        Response_NotAcceptable,
        BadRequest,
        InvalidToken,
        Client_ParseError,
        ImageDownload,
        Unknown;

        public String getMessage() {
            switch (this) {
                case Response_NoResponse: return "No response.";
                case Response_BadRequest: return "Bad request.";
                case Response_InternalServerError: return "Unexpected error occurred.";
                case Response_NotAcceptable: return "Not acceptable.";
                case InvalidToken: return "Invalid access token.";
                case Client_ParseError: return "JSON parse error.";
                case ImageDownload: return "Image download error.";
                default: return "Unexpected error occurred.";
            }
        }

        public VolleyDao.Error getError() {
            return new VolleyDao.Error(this, this.getMessage());
        }

        public static ErrorType getWithCode(int code) {
            switch (code) {
                case 400: return ErrorType.Response_BadRequest;
                case 500: return ErrorType.Response_InternalServerError;
                default: return ErrorType.Unknown;
            }
        }
    }

    private class JsonObjectRequestWithJsonArray extends JsonRequest<JSONObject> {

        /**
         * Creates a new JsonObject request with JsonArray data.
         * @param method the HTTP method to use
         * @param url URL to fetch the JSON from
         * @param jsonArray A {@link JSONArray} to post with the request. Null is allowed and
         *   indicates no parameters will be posted along with request.
         * @param listener Listener to receive the JSON response
         * @param errorListener Error listener, or null to ignore errors.
         */
        public JsonObjectRequestWithJsonArray(int method, String url, JSONArray jsonArray,
                                              Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, (jsonArray == null) ? null : jsonArray.toString(), listener,
                    errorListener);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }

    private class InputStreamVolleyRequest extends Request<byte[]> {
        private final Response.Listener<byte[]> mListener;
        private Map<String, String> mParams;

        //create a static map for directly accessing headers
        public Map<String, String> responseHeaders ;

        public InputStreamVolleyRequest(int method, String mUrl, Response.Listener<byte[]> listener,
                                        Response.ErrorListener errorListener, HashMap<String, String> params) {
            // TODO Auto-generated constructor stub

            super(method, mUrl, errorListener);
            // this request would never use cache.
            setShouldCache(false);
            mListener = listener;
            mParams=params;
        }

        @Override
        protected Map<String, String> getParams()
                throws com.android.volley.AuthFailureError {
            return mParams;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return VolleyDao.this.headers;
        }

        @Override
        protected void deliverResponse(byte[] response) {
            mListener.onResponse(response);
        }

        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

            //Initialise local responseHeaders map with response headers received
            responseHeaders = response.headers;

            //Pass the response data here
            return Response.success( response.data, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

}
