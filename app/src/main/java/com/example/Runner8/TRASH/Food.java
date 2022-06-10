package com.example.Runner8.TRASH;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.Runner8.ui.F_H.calorieDictionary.Adapter.Model.SearchModel;
import com.example.Runner8.ui.Parsing.parsing;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Food {

    private String search;
    private Activity activity;
    String[] tag_name = new String[]{"NUTR_CONT3", "NUTR_CONT2", "NUTR_CONT1"
            , "SERVING_SIZE", "NUTR_CONT9", "NUTR_CONT8", "FOOD_CD", "NUTR_CONT7",
            "NUTR_CONT6", "NUTR_CONT5", "NUTR_CONT4", "DESC_KOR", "NUM"};

    ArrayList<parsing> parsings = new ArrayList<>();
    parsing ps = new parsing();
    SearchModel searchModel;

    private static final Food foodInstance = new Food();

    public static synchronized Food getInstance(){
        return foodInstance;
    }


    List<String> food = new ArrayList<>();
    List<String> foodKcal = new ArrayList<>();
    List<String> containFood = new ArrayList<>();

    public void addFoodData(String name){
        food.add(name);
    }

    public List getFood(){
        return food;
    }

    public List getFoodKcacl(){
        return foodKcal;
    }

    public String getFood(int index){
        return food.get(index);
    }

    public String[] getTag_name() {
        return tag_name;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public List getContainFood(String string){

        this.search = string;

        int count=0;
        containFood.clear();

        for(String foodName : food){
            if((foodName.indexOf(string) == 0 ) && count < 10){
                containFood.add(foodName);
                count++;
            }
        }
        return containFood;
    }

    public ArrayList<parsing> getParsings() {
        return parsings;
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String) downloadUrl((String) urls[0]);
            } catch (Exception e) {
                Log.i("doInBackGround", "다운로드  실패");
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            try {
                Map<String, Object> Food = new HashMap<>();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));

                String NUM;            // 번호
                String FOOD_CD;        // 식품코드
                String DESC_KOR;       // 이름
                String SERVING_SIZE;   // 총 내용량
                String NUTR_CONT1;     // 열량(kcal)(1회 제공량)
                String NUTR_CONT2;     // 탄수화물(g)(1회 제공량)
                String NUTR_CONT3;     // 단백질(g)(1회 제공량)
                String NUTR_CONT4;     // 지방(g)(1회 제공량)
                String NUTR_CONT5;     // 당류(g)(1회 제공량)
                String NUTR_CONT6;     // 나트륨(mg)(1회 제공량)
                String NUTR_CONT7;     // 콜레스테롤(mg)(1회 제공량)
                String NUTR_CONT8;     // 포화지방산(mg)(1회 제공량)
                String NUTR_CONT9;    // 트랜스지방(mg)(1회 제공량)

                boolean bSet_NUM = false;            // 번호
                boolean bSet_FOOD_CD = false;        // 식품코드
                boolean bSet_DESC_KOR = false;       // 이름
                boolean bSet_SERVING_SIZE = false;   // 총 내용량
                boolean bSet_NUTR_CONT1 = false;     // 열량(kcal)(1회 제공량)
                boolean bSet_NUTR_CONT2 = false;     // 탄수화물(g)(1회 제공량)
                boolean bSet_NUTR_CONT3 = false;     // 단백질(g)(1회 제공량)
                boolean bSet_NUTR_CONT4 = false;     // 지방(g)(1회 제공량)
                boolean bSet_NUTR_CONT5 = false;     // 당류(g)(1회 제공량)
                boolean bSet_NUTR_CONT6 = false;     // 나트륨(mg)(1회 제공량)
                boolean bSet_NUTR_CONT7 = false;     // 콜레스테롤(mg)(1회 제공량)
                boolean bSet_NUTR_CONT8 = false;     // 포화지방산(mg)(1회 제공량)
                boolean bSet_NUTR_CONT9 = false;

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        searchModel = new SearchModel();
                        ;
                    } else if (eventType == XmlPullParser.START_TAG) {
                        String tagName = xpp.getName();

                        if(tagName.equals("bSet_NUTR_CONT3"))
                            bSet_NUTR_CONT3 = true;
                        else if(tagName.equals("bSet_NUTR_CONT2"))
                            bSet_NUTR_CONT2 = true;
                        else if(tagName.equals("bSet_NUTR_CONT1"))
                            bSet_NUTR_CONT1 = true;
                        else if(tagName.equals("bSet_SERVING_SIZE"))
                            bSet_SERVING_SIZE = true;
                        else if(tagName.equals("bSet_NUTR_CONT9"))
                            bSet_NUTR_CONT9 = true;
                        else if(tagName.equals("bSet_NUTR_CONT8"))
                            bSet_NUTR_CONT8 = true;
                        else if(tagName.equals("bSet_FOOD_CD"))
                            bSet_FOOD_CD = true;
                        else if(tagName.equals("bSet_NUTR_CONT7"))
                            bSet_NUTR_CONT7 = true;
                        else if(tagName.equals("bSet_NUTR_CONT6"))
                            bSet_NUTR_CONT6 = true;
                        else if(tagName.equals("bSet_NUTR_CONT5"))
                            bSet_NUTR_CONT5 = true;
                        else if(tagName.equals("bSet_NUTR_CONT4"))
                            bSet_NUTR_CONT4 = true;
                        else if(tagName.equals("bSet_DESC_KOR"))
                            bSet_DESC_KOR = true;
                        else if(tagName.equals("bSet_NUM"))
                            bSet_NUM = true;

                    } else if (eventType == XmlPullParser.TEXT) {

                        if(bSet_NUTR_CONT3)
                            searchModel.setNUTR_CONT3(xpp.getText());
                        else if(bSet_NUTR_CONT2)
                            searchModel.setNUTR_CONT2(xpp.getText());
                        else if(bSet_NUTR_CONT1)
                            searchModel.setNUTR_CONT1(xpp.getText());
                        else if(bSet_SERVING_SIZE)
                            searchModel.setSERVING_SIZE(xpp.getText());
                        else if(bSet_NUTR_CONT9)
                            searchModel.setNUTR_CONT9(xpp.getText());
                        else if(bSet_NUTR_CONT8)
                            searchModel.setNUTR_CONT8(xpp.getText());
                        else if(bSet_FOOD_CD)
                            searchModel.setFOOD_CD(xpp.getText());
                        else if(bSet_NUTR_CONT7)
                            searchModel.setNUTR_CONT7(xpp.getText());
                        else if(bSet_NUTR_CONT6)
                            searchModel.setNUTR_CONT6(xpp.getText());
                        else if(bSet_NUTR_CONT5)
                            searchModel.setNUTR_CONT5(xpp.getText());
                        else if(bSet_NUTR_CONT4)
                            searchModel.setNUTR_CONT4(xpp.getText());
                        else if(bSet_DESC_KOR)
                            searchModel.setDESC_KOR(xpp.getText());
                        else if(bSet_NUM)
                            searchModel.setNUM(xpp.getText());

                    } else if (eventType == XmlPullParser.END_TAG) {
                        String end_tag = xpp.getName();
                        if (end_tag.equals("row")) {

                        }
                    }
                    eventType = xpp.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.i("ParsingError", e.toString());
            }
        }

        private String downloadUrl(String myurl) throws IOException {

            HttpURLConnection conn = null;
            try {
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(
                        conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(
                        new InputStreamReader(buf, "utf-8"));
                String line = null;
                String page = "";
                while ((line = bufreader.readLine()) != null) {
                    page += line;
                }

                return page;

            } finally {
                conn.disconnect();
            }
        }
    }
}
