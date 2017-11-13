package com.example.urmil.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class all_list extends AppCompatActivity {

    private ArrayList<Document> docList;
    private DocumentAdapter adapter;
    //String[] addr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alllist);
        docList = new ArrayList<Document>();
        new LoadAllUsers().execute("http://10.0.2.2/prj1/api/viewAllDocument");
        ListView listview = (ListView)findViewById(R.id.listView2);
        adapter = new DocumentAdapter(getApplicationContext(), R.layout.row, docList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), docList.get(position).getName(), Toast.LENGTH_LONG).show();
               // Log.i("POSITION::", "" + position);
               // new DownloadFileFromURL().execute(file_url);
            }
        });

    }

    public class LoadAllUsers extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {

            try {
                URL url=new URL(args[0]);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response=new StringBuffer();
                String inputLine;
                while ((inputLine=br.readLine())!=null){
                    response.append(inputLine);
                }
                br.close();
                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String result) {
            // dismiss the dialog after getting all users
            super.onPostExecute(result);
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if(result!=null){
                try {
                    JSONObject jsonRootObject = new JSONObject(result);
                    int i = 0;
                    while (i<jsonRootObject.getJSONArray("response").length()){
                        Document doc = new Document();
                        doc.setName(jsonRootObject.getJSONArray("response").getJSONObject(i).getString("document_name"));
                        doc.setImage(jsonRootObject.getJSONArray("response").getJSONObject(i).getString("document_thumbnail_url"));
                        doc.setTags(jsonRootObject.getJSONArray("response").getJSONObject(i).getString("document_tag"));
                        docList.add(i,doc);
                        i++;
                    }
                    if(i==0){
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
