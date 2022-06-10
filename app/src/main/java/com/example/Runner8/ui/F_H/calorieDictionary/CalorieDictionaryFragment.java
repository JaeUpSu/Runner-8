package com.example.Runner8.ui.F_H.calorieDictionary;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.TRASH.Food;
import com.example.Runner8.TRASH.FoodItem;
import com.example.Runner8.ui.Loading.LoadingDialogue;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;
import com.example.Runner8.ui.F_H.calorie.Activity.FoodSearchActivity;
import com.example.Runner8.ui.F_H.calorie.SingleTon.CalorieSingleTon;
import com.example.Runner8.ui.F_H.calorieDictionary.Adapter.Model.SearchModel;
import com.example.Runner8.ui.F_H.calorieDictionary.Adapter.SearchAdapter;
import com.example.Runner8.ui.F_H.calorieDictionary.Adapter.SearchAdapter2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CalorieDictionaryFragment extends Fragment {

    private CalorieDictionaryViewModel calorieDictionaryViewModel;

    private List<java.lang.String> list;          // 데이터를 넣은 리스트변수
    private RecyclerView recyclerView;
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private Button button;
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<FoodItem> foodItems = new ArrayList<>();

    private ArrayList<SearchModel> searchModels = new ArrayList<>();
    private ArrayList<SearchModel> search_view_models = new ArrayList<>();
    private ArrayList<SearchModel> search_results = new ArrayList<>();
    private ArrayList<java.lang.String> remove_duplication = new ArrayList<>();

    private NavController navController;

    ProgressBar search_progress;

    FloatingActionButton cart;

    public static CalorieDictionaryFragment newInstance() {
        return new CalorieDictionaryFragment();
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    // View
    AppCompatButton search_button;

    // Class
    SearchModel searchModel;

    // Adapter
    SearchAdapter2 searchAdapter2;

    // Valuable
    java.lang.String search;
    int count = 0;


    public CalorieDictionaryFragment(){}

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        Log.i("FragmentLifeCycle", "CalorieDictionaryFragment onAttach!!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("FragmentLifeCycle", "CalorieDictionaryFragment onDetach!!");
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("FragmentLifeCycle", "CalorieDictionaryFragment onCreate!!");
    }

    // LayoutInflater : 레이아웃 XML 파일을 해당 View 객체로 인스턴스화 합니다.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("FragmentLifeCycle", "CalorieDictionaryFragment onCreateView!!");

        calorieDictionaryViewModel =
                new ViewModelProvider(this).get(CalorieDictionaryViewModel.class);

        // inflate : xml 에 씌여져 있는 view 의 정의를 실제 view 객체로 만드는 역할.
        View root = inflater.inflate(R.layout.fragment_calorie_dictionary, container, false);

        editSearch = root.findViewById(R.id.editSearch);
        recyclerView = root.findViewById(R.id.food_search_list);
        search_button = root.findViewById(R.id.search_button);

        navController = Navigation.findNavController(getActivity(), R.id.nav_food_search_fragment);

        user = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<>();

        // InitialListToFoodDB();

        // 리스트에 연동될 아답터를 생성한다.
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchAdapter2 = new SearchAdapter2(getActivity(), search_results);

        // 리스트뷰에 아답터를 연결한다.

        recyclerView.setAdapter(searchAdapter2);

        //
        // 키보드 앤터키를 찾기로 설정
        editSearch.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER))
                return true;
            else {
                return false;
            }
        });

        search_button.setOnClickListener(v -> {
            search = editSearch.getText().toString();
            search();
        });

        searchAdapter2.setListener((holder, view, position) -> {

            searchModel = search_results.get(position);
            FoodData foodData = new FoodData();

            if(searchModel.getNUTR_CONT4().equals("    ")) foodData.setFat(0.0);
            else foodData.setFat(Double.valueOf(searchModel.getNUTR_CONT4()));

            if(searchModel.getNUTR_CONT2().equals("    ")) foodData.setCar(0.0);
            else foodData.setCar(Double.valueOf(searchModel.getNUTR_CONT2()));

            if(searchModel.getNUTR_CONT3().equals("    ")) foodData.setPro(0.0);
            else foodData.setPro(Double.valueOf(searchModel.getNUTR_CONT3()));

            foodData.setContent(searchModel.getSERVING_SIZE());
            Log.i("content", Double.valueOf(searchModel.getSERVING_SIZE()) + "");

            if(searchModel.getNUTR_CONT1().equals("    ")) foodData.setKcal(0.0);
            else foodData.setKcal(Double.valueOf(searchModel.getNUTR_CONT1()));

            foodData.setName(searchModel.getDESC_KOR());

            CalorieSingleTon.getInstance().setFoodData(foodData);

            navController.navigate(R.id.nav_food_detail);

        });

        return root;
    }

    // 검색을 수행하는 메소드
    public void search() {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        java.lang.String encodeStr = URLEncoder.encode(search);
        java.lang.String serviceUrl =
                "https://openapi.foodsafetykorea.go.kr/api/a11e1bdb6e4b4f77bc7b/I2790/xml/1/200/DESC_KOR=" + encodeStr;

        DownloadWebpageTask downloadWebpageTask = new DownloadWebpageTask();
        downloadWebpageTask.setSearchModel(searchModel);
        downloadWebpageTask.setSearchModels(searchModels);
        downloadWebpageTask.execute(serviceUrl, search);

    }

    // FoodList Update (FireStore)
    public void InitialListToFoodDB() {

        db.collection("Calorie").document("Food")
                .collection("Dictionary")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int i = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Food.getInstance().addFoodData(document.getId());
                            i++;
                        }
                        Log.i("documentSize", java.lang.String.valueOf(task.getResult().size()));
                    } else
                        Log.d("QueryError", "Error getting documents: ", task.getException());
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("FragmentLifeCycle", "CalorieDictionaryFragment onActivityResult!!");
    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentActivity activity = getActivity();
        if (activity != null){
            ((FoodSearchActivity) activity).getSupportActionBar().setTitle("칼로리 사전");
        }


        Log.i("FragmentLifeCycle", "FoodDetailFragment onResume!!");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i("FragmentLifeCycle", "CalorieDictionaryFragment onPause!!");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i("FragmentLifeCycle", "CalorieDictionaryFragment onStop!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("FragmentLifeCycle", "CalorieDictionaryFragment onDestroy!!");
    }

    private class DownloadWebpageTask extends AsyncTask<java.lang.String, Void, java.lang.String> {
        private LoadingDialogue progressDialog = null;

        ArrayList<SearchModel> searchModels;
        SearchModel searchModel;
        java.lang.String searchText;
        boolean equal_check = false;

        public void setSearchModel(SearchModel searchModel) {
            this.searchModel = searchModel;
        }

        public void setSearchModels(ArrayList<SearchModel> searchModels) {
            this.searchModels = searchModels;
        }

        @Override
        protected java.lang.String doInBackground(java.lang.String... params) {
            try {
                searchText = params[1];
                searchModels.clear();
                return (java.lang.String) downloadUrl((java.lang.String) params[0]);
            } catch (Exception e) {
                Log.i("doInBackGround", "다운로드  실패");
                return "다운로드 실패";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog  = new LoadingDialogue();
            progressDialog.show(getActivity().getSupportFragmentManager(), "loading");

        }

        protected void onProgressUpdate(Integer ... values){

        }

        protected void onPostExecute(java.lang.String result) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));

                java.lang.String NUM;            // 번호
                java.lang.String FOOD_CD;        // 식품코드
                java.lang.String DESC_KOR;       // 이름
                java.lang.String SERVING_SIZE;   // 총 내용량
                java.lang.String NUTR_CONT1;     // 열량(kcal)(1회 제공량)
                java.lang.String NUTR_CONT2;     // 탄수화물(g)(1회 제공량)
                java.lang.String NUTR_CONT3;     // 단백질(g)(1회 제공량)
                java.lang.String NUTR_CONT4;     // 지방(g)(1회 제공량)
                java.lang.String NUTR_CONT5;     // 당류(g)(1회 제공량)
                java.lang.String NUTR_CONT6;     // 나트륨(mg)(1회 제공량)
                java.lang.String NUTR_CONT7;     // 콜레스테롤(mg)(1회 제공량)
                java.lang.String NUTR_CONT8;     // 포화지방산(mg)(1회 제공량)
                java.lang.String NUTR_CONT9;    // 트랜스지방(mg)(1회 제공량)

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
                        ;
                    }
                    else if (eventType == XmlPullParser.START_TAG) {
                        java.lang.String tagName = xpp.getName();
                        if(tagName.equals("row")){
                            searchModel = new SearchModel();
                        }
                        if (tagName.equals("NUTR_CONT3")) {
                            bSet_NUTR_CONT3 = true;
                        }
                        if (tagName.equals("NUTR_CONT2")) {
                            bSet_NUTR_CONT2 = true;
                        }
                        if (tagName.equals("NUTR_CONT1"))
                            bSet_NUTR_CONT1 = true;
                        if (tagName.equals("SERVING_SIZE"))
                            bSet_SERVING_SIZE = true;
                        if (tagName.equals("NUTR_CONT9"))
                            bSet_NUTR_CONT9 = true;
                        if (tagName.equals("NUTR_CONT8"))
                            bSet_NUTR_CONT8 = true;
                        if (tagName.equals("FOOD_CD"))
                            bSet_FOOD_CD = true;
                        if (tagName.equals("NUTR_CONT7"))
                            bSet_NUTR_CONT7 = true;
                        if (tagName.equals("NUTR_CONT6"))
                            bSet_NUTR_CONT6 = true;
                        if (tagName.equals("NUTR_CONT5"))
                            bSet_NUTR_CONT5 = true;
                        if (tagName.equals("NUTR_CONT4"))
                            bSet_NUTR_CONT4 = true;
                        if (tagName.equals("DESC_KOR"))
                            bSet_DESC_KOR = true;
                        if (tagName.equals("NUM"))
                            bSet_NUM = true;
                    }
                    else if (eventType == XmlPullParser.TEXT) {
                        if (bSet_NUTR_CONT3) {
                            searchModel.setNUTR_CONT3(xpp.getText());
                            bSet_NUTR_CONT3 = false;
                        }
                        if (bSet_NUTR_CONT2) {
                            searchModel.setNUTR_CONT2(xpp.getText());
                            bSet_NUTR_CONT2 = false;
                        }
                        if (bSet_NUTR_CONT1) {
                            searchModel.setNUTR_CONT1(xpp.getText());
                            bSet_NUTR_CONT1 = false;
                        }
                        if (bSet_SERVING_SIZE) {
                            searchModel.setSERVING_SIZE(xpp.getText());
                            bSet_SERVING_SIZE = false;
                        }
                        if (bSet_NUTR_CONT9) {
                            searchModel.setNUTR_CONT9(xpp.getText());
                            bSet_NUTR_CONT9 = false;
                        }
                        if (bSet_NUTR_CONT8) {
                            searchModel.setNUTR_CONT8(xpp.getText());
                            bSet_NUTR_CONT8 = false;
                        }
                        if (bSet_FOOD_CD) {
                            searchModel.setFOOD_CD(xpp.getText());
                            bSet_FOOD_CD = false;
                        }
                        if (bSet_NUTR_CONT7) {
                            searchModel.setNUTR_CONT7(xpp.getText());
                            bSet_NUTR_CONT7 = false;
                        }
                        if (bSet_NUTR_CONT6) {
                            searchModel.setNUTR_CONT6(xpp.getText());
                            bSet_NUTR_CONT6 = false;
                        }
                        if (bSet_NUTR_CONT5) {
                            searchModel.setNUTR_CONT5(xpp.getText());
                            bSet_NUTR_CONT5 = false;
                        }
                        if (bSet_NUTR_CONT4) {
                            searchModel.setNUTR_CONT4(xpp.getText());
                            bSet_NUTR_CONT4 = false;
                        }
                        if (bSet_DESC_KOR) {
                            searchModel.setDESC_KOR(xpp.getText());
                            bSet_DESC_KOR = false;
                        }
                        if (bSet_NUM) {
                            searchModel.setNUM(xpp.getText());
                            bSet_NUM = false;
                        }
                    }
                    else if (eventType == XmlPullParser.END_TAG) {
                        java.lang.String end_tag = xpp.getName();
                        if (end_tag.equals("row")) {
                            searchModels.add(searchModel);
                        }
                    }

                    eventType = xpp.next();
                }
                count = 0;
                search_view_models.clear();
                search_results.clear();
                remove_duplication.clear();

                for(SearchModel sm : searchModels){
                    Log.i("sm", java.lang.String.valueOf(sm.getDESC_KOR().indexOf(searchText)));

                    if ((sm.getDESC_KOR().indexOf(searchText) == 0) && count < 20) {
                        Log.i("sm", sm.getDESC_KOR());
                        search_view_models.add(sm);
                        count++;
                    }
                }

                for(SearchModel data : search_view_models){
                    Log.i("data", data.getDESC_KOR());
                    if(!remove_duplication.contains(data.getDESC_KOR())) {
                        remove_duplication.add(data.getDESC_KOR());
                        search_results.add(data);
                    }
                }

                searchAdapter2.notifyDataSetChanged();
                progressDialog.dismiss();

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.i("ParsingError", e.toString());
            }
        }

        private java.lang.String downloadUrl(java.lang.String myurl) throws IOException {

            HttpURLConnection conn = null;
            try {
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(
                        conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(
                        new InputStreamReader(buf, "utf-8"));
                java.lang.String line = null;
                java.lang.String page = "";
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
}

