package com.example.urmil.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class search_list extends Activity {

    private ArrayList<Document> docList;
    private DocumentAdapter adapter;
    private EditText t;
    private String Name;
    private ListView listview;
    private Spinner sp;
    private String fList[] = {"-Select Filter-",
            "Name","Tags","Author"
    };
    private ArrayAdapter<String> add;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
        t = (EditText) findViewById(R.id.editText);
        sp = (Spinner) findViewById(R.id.spinner);
        add = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,fList);
        add.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp.setAdapter(add);
        docList = new ArrayList<Document>();
        listview = (ListView) findViewById(R.id.listView2);

    }

    public void senddatatoserver(View v) throws JSONException {
        //function in the activity that corresponds to the layout button
        if(sp.getSelectedItem().toString()=="-Select Filter-"){
            Toast.makeText(getApplicationContext(),"Select Appropriate filter",Toast.LENGTH_SHORT).show();
            return;
        }
        else if( t.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(),"Search cannot be blank",Toast.LENGTH_SHORT).show();
            return;
        }
        docList.clear();
        Name = t.getText().toString();
        String x = "{\"document_name\":\""+Name+"\",\"filter\":\"" + sp.getSelectedItemId() + "\"}";
        //Log.i("String::",x);
        new SendJsonDataToServer().execute(x);
    }

    //add background inline class here
    class SendJsonDataToServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String JsonResponse = "";
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            URL url = null;

            try{
                String a = "http://172.37.6.26:80/prj1/api/searchDocument";
                url = new URL(a);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.close();
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer response=new StringBuffer();
                String inputLine;
                while ((inputLine=br.readLine())!=null){
                    response.append(inputLine);
                }
                br.close();
                JsonResponse = response.toString();}
            catch(Exception e){
                e.printStackTrace();
            }
            return JsonResponse;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if (result != null) {
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
                    adapter = new DocumentAdapter(getApplicationContext(), R.layout.row, docList);
                    listview.setAdapter(adapter);

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
