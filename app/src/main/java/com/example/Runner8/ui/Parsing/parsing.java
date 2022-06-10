package com.example.Runner8.ui.Parsing;

import java.util.HashMap;
import java.util.Map;

public class parsing {

    Map<String, String> tagName = new HashMap<>();
    Map<String, Boolean> bSetName = new HashMap<>();

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

    boolean bSet_NUM;            // 번호
    boolean bSet_FOOD_CD;        // 식품코드
    boolean bSet_DESC_KOR;       // 이름
    boolean bSet_SERVING_SIZE;   // 총 내용량
    boolean bSet_NUTR_CONT1;     // 열량(kcal)(1회 제공량)
    boolean bSet_NUTR_CONT2;     // 탄수화물(g)(1회 제공량)
    boolean bSet_NUTR_CONT3;     // 단백질(g)(1회 제공량)
    boolean bSet_NUTR_CONT4;     // 지방(g)(1회 제공량)
    boolean bSet_NUTR_CONT5;     // 당류(g)(1회 제공량)
    boolean bSet_NUTR_CONT6;     // 나트륨(mg)(1회 제공량)
    boolean bSet_NUTR_CONT7;     // 콜레스테롤(mg)(1회 제공량)
    boolean bSet_NUTR_CONT8;     // 포화지방산(mg)(1회 제공량)
    boolean bSet_NUTR_CONT9;

    public parsing(){


        tagName.put("NUM", "");
        tagName.put("FOOD_CD", "");
        tagName.put("DESC_KOR", "");
        tagName.put("SERVING_SIZE", "");
        tagName.put("NUTR_CONT1", "");
        tagName.put("NUTR_CONT2", "");
        tagName.put("NUTR_CONT3", "");
        tagName.put("NUTR_CONT4", "");
        tagName.put("NUTR_CONT5", "");
        tagName.put("NUTR_CONT6", "");
        tagName.put("NUTR_CONT7", "");
        tagName.put("NUTR_CONT8", "");
        tagName.put("NUTR_CONT9", "");

        bSetName.put("bSet_NUM", false);
        bSetName.put("bSet_FOOD_CD", false);
        bSetName.put("bSet_DESC_KOR", false);
        bSetName.put("bSet_SERVING_SIZE", false);
        bSetName.put("bSet_NUTR_CONT1", false);
        bSetName.put("bSet_NUTR_CONT2", false);
        bSetName.put("bSet_NUTR_CONT3", false);
        bSetName.put("bSet_NUTR_CONT4", false);
        bSetName.put("bSet_NUTR_CONT5", false);
        bSetName.put("bSet_NUTR_CONT6", false);
        bSetName.put("bSet_NUTR_CONT7", false);
        bSetName.put("bSet_NUTR_CONT8", false);
        bSetName.put("bSet_NUTR_CONT9", false);

    }

    public Boolean getbSetParsingData(String tagName){
        return bSetName.get(tagName);
    }

    public void bSetParsingData(String tagName, Boolean check){
        bSetName.put(tagName,check);
    }

    public String getParsingData(String tn){
        return tagName.get(tn);
    }

    public void setParsingData(String Name, String tn){ tagName.put(Name,tn); }

    public String changeName(String tagName){
        if(tagName.equals("NUM")) return "num";
        else if(tagName.equals("FOOD_CD")) return "food_code";
        else if(tagName.equals("DESC_KOR")) return "name";
        else if(tagName.equals("SERVING_SIZE")) return "content";
        else if(tagName.equals("NUTR_CONT1")) return "Eng";
        else if(tagName.equals("NUTR_CONT2")) return "Car";
        else if(tagName.equals("NUTR_CONT3")) return "Pro";
        else if(tagName.equals("NUTR_CONT4")) return "Fat";
        else if(tagName.equals("NUTR_CONT5")) return "Tsg";
        else if(tagName.equals("NUTR_CONT6")) return "Na";
        else if(tagName.equals("NUTR_CONT7")) return "CHOLE";
        else if(tagName.equals("NUTR_CONT8")) return "SFA";
        else if(tagName.equals("NUTR_CONT9")) return "TFA";
        else return "";
    }

    /*

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        ProgressDialog asyncDialog = new ProgressDialog(getContext());
        MarkerDialogue markerDialogue;

        public void setMarkerDialogue(MarkerDialogue markerDialogue) {
            this.markerDialogue = markerDialogue;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String) downloadUrl((String) urls[0]);
            } catch (Exception e) {
                Log.i("doInBackGround", "다운로드  실패");
                return "다운로드 실패";
            }
        }

        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("확인 중...");
            asyncDialog.show();
        }

        protected void onPostExecute(String result) {
            Double totalDistance =0.0;
            Double totalTime =0.0;
            long hour = 0;
            long minute = 0;
            try {
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(result);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray features = jsonObject.get("features").getAsJsonArray();

                for(JsonElement data : features) {
                    JsonObject jsonObject1 = data.getAsJsonObject();
                    JsonObject properties = jsonObject1.get("properties").getAsJsonObject();
                    totalDistance = properties.get("totalDistance").getAsDouble();
                    totalTime = properties.get("totalTime").getAsDouble();
                    Log.i("totalDistance", totalDistance + "");
                    Log.i("totalTime", totalTime + "");

                }
            } catch (Exception e) {
                Log.i("ParsingError", e.toString());
            } finally {
                asyncDialog.dismiss();
            }
            if(totalDistance >= 1000) {
                dist = totalDistance/1000 + " km";
            }else{
                dist = totalDistance + " m";
            }
            if(totalTime >= 3600) {
                hour = Math.round(totalTime/3600);
                totalTime -= (3600 * hour);
                if (totalTime >= 60){
                    minute = Math.round(totalTime/60);
                    time = hour + " 시간 " + minute + " 분";
                }
                time = hour + " 시간";
            }else if (totalTime >= 60){
                minute = Math.round(totalTime/60);
                time = minute + " 분";
            }else{time = "0 분";}
            Log.i("Distance", dist);
            Log.i("Time", time);
            Log.i("Result", result);
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
                Log.i("page", page);

                return page;

            } finally {
                conn.disconnect();
            }
        }
    }

     */

}
