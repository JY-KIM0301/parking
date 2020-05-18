package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MenuActivity extends AppCompatActivity {
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tv=findViewById(R.id.tv);
    }
    public void clickBtn(View v) {
        Toast.makeText(getApplicationContext(), "주차장 정보를 불러왔어요.", Toast.LENGTH_LONG);

        AssetManager assetManager= getAssets();  //assets폴더의 파일을 가져오기 위해 창고관리자(AssetManager) 얻어오기
        try {
            InputStream is= assetManager.open("jsons/location.json");
            InputStreamReader isr= new InputStreamReader(is);
            BufferedReader reader= new BufferedReader(isr);

            StringBuffer buffer= new StringBuffer();
            String line= reader.readLine();
            while (line!=null){
                buffer.append(line+"\n");
                line=reader.readLine();
            }

            String jsonData= buffer.toString();
            JSONArray jsonArray= new JSONArray(jsonData);

            String s="";
            for(int i=0; i<jsonArray.length();i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                String TEMP_CONT01 = jo.getString("TEMP_CONT01");
                String REFINE_LOTNO_ADDR = jo.getString("REFINE_LOTNO_ADDR");
                String CONTCT_NO = jo.getString("CONTCT_NO");

                s += "\n" +"주차장 이름: " + TEMP_CONT01 + "\n" + "주소: " + REFINE_LOTNO_ADDR + "\n" + "전화번호: " + CONTCT_NO + "\n" ;
            }
            tv.setText(s);

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

