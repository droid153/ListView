package com.example.matrix.listview;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MyActivity extends Activity {

    public static final String SERVER_URL_LS = "http://monocle.livingsocial.com/v2/deals?coords=38.89,-77.02&api-key=8551A250FEB245E5836CDB902C163A6C";
    public ArrayList<Deals> dealsList = new ArrayList<Deals>();;
    ListView mListview;
    TextView mTvMerchant;
    TextView mTvShortTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mListview = (ListView) findViewById(R.id.listView1);
        mTvMerchant = (TextView) findViewById(R.id.deal_merchant);
        mTvShortTitle = (TextView) findViewById(R.id.deal_shortTitle);

//        adapter = new DealsAdapter(getApplicationContext(), R.layout.layout_livingsocial, dealsList);
//        mListview.setAdapter(adapter);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(),dealsList.get(position).getMerchantName(), Toast.LENGTH_SHORT).show();
            }
        });

        DownloadData dd = new DownloadData();
        dd.execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadData extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObject;

        @Override
        protected JSONObject doInBackground(Void... arg0) {

            try {
                DefaultHttpClient defaultClient = new DefaultHttpClient();
                HttpGet httpGetRequest = new HttpGet(SERVER_URL_LS);
                HttpResponse httpResponse = defaultClient.execute(httpGetRequest);

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF_8"));
                String json = reader.readLine();

                jsonObject = new JSONObject(json);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return jsonObject;
        }

        protected void onPostExecute(JSONObject result) {

            livingSocialParser(jsonObject);

            DealsAdapter adapter = new DealsAdapter(MyActivity.this, dealsList);
            mListview.setAdapter(adapter);

        }    //onPostExecute

        // Implemented with a Deal class object; i.e. each deal has an object associated with it.
        // Not like the groupon api above //
        private void livingSocialParser(JSONObject result) {
            JSONArray dealsArray;
            JSONObject oneElemInDealsArray;
            JSONArray dealTypesArray;
            JSONObject dealTypeInside;
            try {
                dealsArray = result.getJSONArray("deals");

                for (int i = 0; i < dealsArray.length(); i++) {
                    oneElemInDealsArray = dealsArray.getJSONObject(i);

                    Deals thisDeal = new Deals();

                    thisDeal.setMerchantName(oneElemInDealsArray.getString("merchant_name"));   //Log.d("MERCHANT", oneElemInDealsArray.getString("merchant_name"));
                    thisDeal.setShortTitle(oneElemInDealsArray.getString("short_title"));       //Log.d("SHORT_TITLE",oneElemInDealsArray.getString("short_title"));

                    dealsList.add(thisDeal);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        private void grouponParser(JSONObject result) {
//            JSONArray dealsArray;
//            JSONObject oneElemInDealsArray;
//            JSONArray dealTypesArray;
//            JSONObject dealTypeInside;
//            try {
//                dealsArray = result.getJSONArray("deals");
//
//                for (int i = 0; i < dealsArray.length(); i++) {
//                    oneElemInDealsArray = dealsArray.getJSONObject(i);
//
//                    // Parsing individual node in "deals" array //
//                    dealTypesArray = oneElemInDealsArray.getJSONArray("dealTypes");
//
//                    // Parsing a node array in "deals" array //
//                    for (int j = 0; j < dealTypesArray.length(); j++) {
//                        dealTypeInside = dealTypesArray.getJSONObject(j);
//                        Log.d("Dealtype Desc: ", dealTypeInside.getString("description"));
//                    }
//
//                    // Parsing individual node in "deals" array //
//                    String test = oneElemInDealsArray.getString("shortAnnouncementTitle");
//                    mDesc.add(i, test);
//
//                    Log.d("Announcement Title: ", test);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

    }

//    class MyAdapter extends ArrayAdapter<String> {
//        Context mContext;
//        String[] mTitleArray;
//        ArrayList<String> mDescArray;
//        int[] mImages;
//
//        public MyAdapter(Context context, String[] titles, int[] image, ArrayList<String> description) {
//            super(context, R.layout.layout_image_title_desc, R.id.title, description);
//            this.mContext = context;
//            this.mTitleArray = titles;
//            this.mDescArray = description;
//            this.mImages = image;
//        }
//
//        // Optimize further to avoid findViewById() call. Its expensive too. So use view holder class //
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            View row = convertView;
//            ViewHolder holder = null;
//
//            if (row == null) {
//                LayoutInflater rowInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                row = rowInflator.inflate(R.layout.layout_image_title_desc, parent, false);
//
//                holder = new ViewHolder(row);
//                row.setTag(holder);
//            } else {
//                holder = (ViewHolder) row.getTag();
//            }
//
//            holder.titleText.setText(mTitle[position]);
//            holder.descText.setText(mDesc.get(position));
//            holder.imageView.setImageResource(mImages[position]);
//
//            return row;
//        }

//        class ViewHolder {
//            TextView titleText;
//            TextView descText;
//            ImageView imageView;
//
//            public ViewHolder(View row) {
//                titleText = (TextView) row.findViewById(R.id.title);
//                descText = (TextView) row.findViewById(R.id.description);
//                imageView = (ImageView) row.findViewById(R.id.image);
//            }
//        }
//    }

    class DealsAdapter extends ArrayAdapter<Deals>{

        Context mContext;
        ArrayList<Deals> dealsList;

        public DealsAdapter(Context context, ArrayList<Deals> dealObject){

            super(context, R.layout.layout_livingsocial, dealObject);

            mContext = context;
            dealsList = dealObject;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            DealsViewHolder holder = null;

            if (row == null) {

                LayoutInflater rowInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row  = rowInflator.inflate(R.layout.layout_livingsocial, parent, false);

                holder = new DealsViewHolder();
                row.setTag(holder);
            }
            else{
                holder = (DealsViewHolder) row.getTag();
            }

            ///////////// ????????????????? //////////////

            Log.d("HOLDER","" + position);

            holder.textMerchant.setText(dealsList.get(position).getMerchantName());
            holder.textShortTitle.setText(dealsList.get(position).getShortTitle());

            return row;
        }

        public class DealsViewHolder{

            TextView textMerchant;
            TextView textShortTitle;

            public DealsViewHolder() {
                textMerchant = (TextView) findViewById(R.id.deal_merchant); Log.d("CONSTRUCTOR",textMerchant.toString());
                textShortTitle = (TextView) findViewById(R.id.deal_shortTitle);
            }


        }
    }

}
