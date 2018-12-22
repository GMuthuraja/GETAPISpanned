package app.example.app.myjson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView)  findViewById(R.id.listView);

        fetchData data = new fetchData();
        data.execute(new String[]{"https://www.latrobe.edu.au/asdetect/api/appl/child"});
    }




    public class fetchData extends AsyncTask<String, Void, String>{



        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;

            try{
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "0c4533a4-b98c-4f34-9776-f3b90bf6e1dc");
                connection.connect();


                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));

                StringBuffer buffer =new StringBuffer();
                String lines;


                while((lines = reader.readLine()) != null){
                    buffer.append(lines+"\n");
                }
                result = buffer.toString();

            }catch (Exception e){

            }
            return result;
        }



        @Override
        protected void onPostExecute(String values){

            try {
                JSONObject obj = new JSONObject(values);
                JSONArray jArr = obj.getJSONArray("data");

                ArrayList<Spanned> arrayList = new ArrayList<Spanned>();
                Spanned spanObj;


                for(int i =0; i<jArr.length();i++){
                    spanObj = Html.fromHtml(
                            "First Name: "+jArr.getJSONObject(i).getString("firstName").toString()+"<br/><br/> LastName: "+
                            jArr.getJSONObject(i).getString("lastName").toString()+"<br/><br/> Gender: "+
                            jArr.getJSONObject(i).getString("gender").toString());
                    arrayList.add(spanObj);
                }

                ArrayAdapter<Spanned> adapter = new ArrayAdapter<Spanned>(MainActivity.this,android.R.layout.simple_list_item_1,arrayList);
                list.setAdapter(adapter);

            }catch (Exception e){

            }

        }
    }

}
