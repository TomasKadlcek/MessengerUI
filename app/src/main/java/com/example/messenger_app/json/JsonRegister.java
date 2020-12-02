package com.example.messenger_app.json;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JsonRegister extends JsonObjectRequest {
    public JsonRegister(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    // Override json parseerror to get message...
    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        String json;
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            try {
                json = new String(volleyError.networkResponse.data,
                        HttpHeaderParser.parseCharset(volleyError.networkResponse.headers));
            } catch (UnsupportedEncodingException e) {
                return new VolleyError(e.getMessage());
            }
            return new VolleyError(json);
        }
        return volleyError;
    }


}
