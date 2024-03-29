package com.example.administrator.a9_3_2_parsexmlsaxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private TextView textViewGet;
    //private TextView textViewPost;
    private static int get_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button get_request =  (Button)findViewById(R.id.get_request);
        //Button post_request =  (Button)findViewById(R.id.post_request);
        get_request.setOnClickListener(this);
        //post_request.setOnClickListener(this);

        textViewGet = (TextView)findViewById(R.id.text_view_get);
        //textViewPost = (TextView)findViewById(R.id.text_view_post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_request:
                getRequestWithOkHttp();
                break;
            case R.id.post_request:
                postRequestWithOkHttp();
                break;
            default:
                break;
        }
    }

    private void getRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.1.101/get_data.xml")
                            .build();
                    // .url("http://127.0.0.1/get_data.xml")
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    MainActivity.get_count++;
                    parseXMLWithSAX(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postRequestWithOkHttp() {

    }

    private void parseXMLWithSAX(String data) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(data)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }

    private void showString(final String str, final TextView textView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(str);
            }
        });
    }
}
