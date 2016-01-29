package msi.myapplication;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static msi.myapplication.ParseComServer.getAppParseComHeaders;


public class ParseServerData {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public List<Project> getProject(
            String auth
    ) throws Exception {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.parse.com/1/classes/Project/";

        HttpGet httpGet = new HttpGet(url);
        for (Header header : getAppParseComHeaders()) {
            httpGet.addHeader(header);
        }
        //httpGet.addHeader("X-Parse-Session-Token", auth); // taken from https://parse.com/questions/how-long-before-the-sessiontoken-expires

        try {
            HttpResponse response = httpClient.execute(httpGet);

            String responseString = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                ParseComServer.ParseComError error = new Gson()
                        .fromJson(responseString, ParseComServer.ParseComError.class);
                throw new Exception("Error retrieving ["+error.code+"] - " + error.error);
            }

            Projects projects = new Gson().fromJson(responseString, Projects.class);
            return projects.results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<Project>();
    }

    public void putProject(String authtoken, String userId, Project projectToAdd) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.parse.com/1/classes/Project/";

        HttpPost httpPost = new HttpPost(url);

        for (Header header : getAppParseComHeaders()) {
            httpPost.addHeader(header);
        }
 /*       httpPost.addHeader("X-Parse-Session-Token", authtoken); // taken from https://parse.com/questions/how-long-before-the-sessiontoken-expires
        httpPost.addHeader("Content-Type", "application/json");
*/
        httpPost.addHeader("X-Parse-Application-Id","rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3");
        httpPost.addHeader("X-Parse-REST-API-Key", "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW");

        JSONObject project = new JSONObject();
        project.put("author", projectToAdd.author);
        project.put("createdAt", projectToAdd.createdAt);
        project.put("objectId", projectToAdd.objectId);
        project.put("updatedAt", projectToAdd.updatedAt);
        project.put("description", projectToAdd.description);
        project.put("link", projectToAdd.link);
        project.put("title", projectToAdd.title);

        // Creating ACL JSON object for the current user
        JSONObject acl = new JSONObject();
        JSONObject aclEveryone = new JSONObject();
        JSONObject aclMe = new JSONObject();
        aclMe.put("read", true);
        aclMe.put("write", true);
        //acl.put(userId, aclMe);
        acl.put("*", aclEveryone);
        project.put("ACL", acl);

        String request = project.toString();
        httpPost.setEntity(new StringEntity(request,"UTF-8"));

        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 201) {
                ParseComServer.ParseComError error = new Gson().fromJson(responseString, ParseComServer.ParseComError.class);
                throw new Exception("Error posting ProjectContract.ProjectEntrys ["+error.code+"] - " + error.error);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Projects implements Serializable {
        List<Project> results;
    }

}
