package msi.myapplication;

/**
 * Created by MSI! on 07.10.2015.
 */

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ParseComServer implements ServerAuthenticate{
    private final static String APP_ID = "rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3";
    private final static String REST_API_KEY = "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW";

    public SharedPreferences prefs;
    Result loggedUser;

    public static List<Header> getAppParseComHeaders() {
        List<Header> ret = new ArrayList<Header>();
        ret.add(new BasicHeader("X-Parse-Application-Id", APP_ID));
        ret.add(new BasicHeader("X-Parse-REST-API-Key", REST_API_KEY));
        return ret;
    }

    public static class ParseComError implements Serializable {
        public int code;
        public String error;
    }

    @Override
    public String userSignUp(String name, String pass, String authType)
            throws Exception {

        String url = "https://api.parse.com/1/classes/_User/";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("X-Parse-Application-Id","rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3");
        httpPost.addHeader("X-Parse-REST-API-Key", "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW");
        //httpPost.addHeader("Content-Type", "application/json");

        String user = "{\"username\":\"" + name + "\",\"password\":\"" + pass + "\"}";
        HttpEntity entity = new StringEntity(user);
        httpPost.setEntity(entity);

        String authtoken = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() != 201) {
                ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
                throw new Exception("Error creating user["+error.code+"] - " + error.error);
            }

            loggedUser = new Gson().fromJson(responseString, Result.class);
            authtoken = loggedUser.sessionToken;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return authtoken;
    }

    @Override
    public String getUserObjectId()
    {
        return loggedUser.objectId;
    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.parse.com/1/classes/_User/";

        String query = null;
        query = String.format("{\"%s\":\"%s\"}", "username", URLEncoder.encode(user, "UTF-8"));
        //, URLEncoder.encode(user, "UTF-8")//, "password", pass);
        url += "?where=" + query;

        url = "https://api.parse.com/1/classes/_User/";

        HttpGet httpGet = new HttpGet(url);

        httpGet.addHeader("X-Parse-Application-Id","rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3");
        httpGet.addHeader("X-Parse-REST-API-Key", "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW");

        HttpParams params = new BasicHttpParams();
        params.setParameter("username", user);
        params.setParameter("password", pass);
        httpGet.setParams(params);
//        httpGet.getParams().setParameter("username", user).setParameter("password", pass);

        String authtoken = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);

            String responseString = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() != 200) {
                ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
                throw new Exception("Error signing-in ["+error.code+"] - " + error.error);
            }

            JSONObject jsonObject = new JSONObject(responseString);
            String[] js = jsonObject.toString().split("\\{");

            for(String j : js)
            {
                if(j.contains(user))
                {
                    String[] jm = j.split(",");
                }
            }

            Result loggedUser = new Gson().fromJson(responseString, Result.class);
            authtoken = loggedUser.sessionToken;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return authtoken;
    }

    @Override
    public boolean userSignUpWithoutToken(String name, String pass, String authType) throws Exception {
        String url = "https://api.parse.com/1/classes/_User/";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("X-Parse-Application-Id","rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3");
        httpPost.addHeader("X-Parse-REST-API-Key", "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW");
        //httpPost.addHeader("Content-Type", "application/json");

        String user = "{\"username\":\"" + name + "\",\"password\":\"" + pass + "\"}";
        HttpEntity entity = new StringEntity(user);
        httpPost.setEntity(entity);

        String authtoken = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() != 201) {
                ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
                throw new Exception("Error creating user["+error.code+"] - " + error.error);
            }
            else
                return true;

            //if(responseString.contains(name))


            //User createdUser = new Gson().fromJson(responseString, User.class);

            //authtoken = createdUser.sessionToken;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean userSignInWithoutToken(String user, String pass, String authType) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.parse.com/1/classes/_User/";

        String query = null;
        query = String.format("%s=%s", "username", URLEncoder.encode(user, "UTF-8"));
        //, URLEncoder.encode(user, "UTF-8")//, "password", pass);
        url += "?" + query;

        HttpGet httpGet = new HttpGet(url);

        httpGet.addHeader("X-Parse-Application-Id","rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3");
        httpGet.addHeader("X-Parse-REST-API-Key", "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW");

        HttpParams params = new BasicHttpParams();
        params.setParameter("username", user);
        params.setParameter("password", pass);
        httpGet.setParams(params);
//        httpGet.getParams().setParameter("username", user).setParameter("password", pass);

        String authtoken = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);

            String responseString = EntityUtils.toString(response.getEntity());

            JSONObject jsonObject = new JSONObject(responseString);
            String[] js = jsonObject.toString().split("\\{");

            for(String j : js)
            {
                if(j.contains(user))
                {
                    return true;
                }
            }

            if (response.getStatusLine().getStatusCode() != 200) {
                ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
                throw new Exception("Error signing-in ["+error.code+"] - " + error.error);
            }

            Result loggedUser = new Gson().fromJson(responseString, Result.class);
            authtoken = loggedUser.sessionToken;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private class Result implements Serializable {

        private String createdAt;
        private String objectId;
        private String sessionToken;

        private Result() {
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }
    }
}