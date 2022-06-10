package com.example.Runner8.ui.map.Guide;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.speech.tts.TextToSpeech.ERROR;

public class GuideActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private EditText editText;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn0;
    private FrameLayout mainLayout;
    private LinearLayout mainPage;

    GuideFragment guideFragment;
    Timer timer;
    Boolean startFlag = false;
    Boolean endFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mainPage = findViewById(R.id.mainPage);
        mainLayout = findViewById(R.id.mainLayout);
        editText = findViewById(R.id.editText);
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);

        guideFragment = new GuideFragment();

        tts = new TextToSpeech(this, status -> {
            if(status == ERROR){
                Log.i("ERROR", "check!!");
            }
            else{
                tts.setLanguage(Locale.KOREA);
                Log.i("NOT ERROR", "check!!");
            }
        });

        btn0.setOnClickListener(v -> {
            // dialogue.show(getSupportFragmentManager(),"Dialog");
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, new GuideFragment()).commit();
        });

        btn1.setOnClickListener(v -> {
            String str = strChangeLocation(guideFragment.getEdit_me());
            //startSpeak();
            //
            // timeSpeak();
            tts.speak("{내위치}" + 200+"미터" , TextToSpeech.QUEUE_ADD, null, null);

        });
        btn2.setOnClickListener(v -> {
            String str = strChangeLocation(guideFragment.getEdit_opp());
            timeSpeak();
        });
        btn3.setOnClickListener(v -> {
            String str = strChangeLocation(guideFragment.getEditmeOpp1());
            //locationSpeak(str);
        });
        btn4.setOnClickListener(v -> {
            String str = strChangeLocation(guideFragment.getEditmeOpp2());
            //locationSpeak(str);
        });
        btn5.setOnClickListener(v -> {
            String str = strChangeLocation(guideFragment.getEditoppMe2());
            //locationSpeak(str);
        });
        btn6.setOnClickListener(v -> {
            String str = strChangeLocation(guideFragment.getEditoppMe1());
            //locationSpeak(str);
        });
    }
    public void dist_tts(double dist){
        tts.setPitch(1.5f);
        tts.setSpeechRate(1.0f);
        tts.speak("당신은 지금 " + editText.getText().toString() + " 미터 걸었어" ,TextToSpeech.QUEUE_ADD,null,null);
    }

    public String strChangeLocation(String str){
        String result = str.replace("{내위치}",200+"미터");
       // result = result.replace("내위치", 3333 + "킬로미터");
        result = result.replace("{상대위치}",300+"미터");
        Log.i("change",result);
        return result;
    }
    // 위치가 조건으로 실행되는 경우
    public void locationSpeak(){
        Timer  timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            String str = "";
            int time = 0;
            double myLocationDist = 0.0;
            double oppLocationDist = 0.0;
            double endLocaionDist = 0.0;
            @Override
            public void run() {
                Log.i("timer", time + "");
                // 시작지점에서 Run 할때
                if (startFlag == true) {
                    startSpeak();
                    startFlag = false;
                    // 나와 상대가 30m 안쪽에 있을때
                }else if (Math.abs(myLocationDist - oppLocationDist) < 30) {
                    // 내가 더 앞서있을때
                    if (myLocationDist > oppLocationDist) {
                        if (endLocaionDist - myLocationDist > endLocaionDist - oppLocationDist) {
                            // 상대가 나를 앞서기 직전
                            str = strChangeLocation(guideFragment.getEditmeOpp1());
                            tts.speak(str, TextToSpeech.QUEUE_ADD, null, null);
                        } else {
                            // 상대가 나를 앞서 나갈때
                            str = strChangeLocation(guideFragment.getEditmeOpp2());
                            tts.speak(str, TextToSpeech.QUEUE_ADD, null, null);
                        }
                    }else{
                    // 상대가 더 앞서있을때
                        if (myLocationDist < oppLocationDist) {
                            if (endLocaionDist - myLocationDist < endLocaionDist - oppLocationDist) {
                                // 내가 상대를 앞서기 직전
                                str = strChangeLocation(guideFragment.getEditoppMe1());
                                tts.speak(str, TextToSpeech.QUEUE_ADD, null, null);
                            } else {
                                // 내가 상대를 앞서 나갈때
                                str = strChangeLocation(guideFragment.getEditoppMe2());
                                tts.speak(str, TextToSpeech.QUEUE_ADD, null, null);
                            }
                        }
                    }
                    //도착 지점일때
                }else if (endFlag == true) {
                    endSpeak();
                    endFlag = false;
                }
                if (time == 60)
                    timer.cancel();
                time++;
            }
        };
        timer.schedule(timerTask,0,1000);
    }
    // 시간이 조건으로 실행되는 경우
    public void timeSpeak(){
        Timer  timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            String str = "";
            int time = 0;
            @Override
            public void run() {
                Log.i("timer",time + "");
                if(time % 12 == 0 && time != 0 && time != 60) {
                    str = strChangeLocation(guideFragment.getEdit_me());
                    tts.speak(str, TextToSpeech.QUEUE_ADD, null, null);
                }else if(time % 20 == 0 && time != 0 && time != 60) {
                    str = strChangeLocation(guideFragment.getEdit_opp());
                    tts.speak(str, TextToSpeech.QUEUE_ADD, null, null);
                }else if(time == 60)
                    timer.cancel();
                time++;
            }
        };
        timer.schedule(timerTask,0,1000);
    }
    // 시작 조건으로 실행되는 경우
    public void startSpeak(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int time = 1;
            @Override
            public void run() {
                Log.i("timer",time + "");
                tts.setPitch(1.0f);
                tts.setSpeechRate(1.2f);
                tts.speak(time+"",TextToSpeech.QUEUE_ADD,null,null);
                if(time == 10)
                    timer.cancel();
                time++;
            }
        };
        timer.schedule(timerTask,0,1000);
    }
    // 도착 조건으로 실행되는 경우
    public void endSpeak(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int time = 1;
            @Override
            public void run() {
                Log.i("timer",time + "");
                String times = "2시간 30분";
                tts.setPitch(1.0f);
                tts.setSpeechRate(1.2f);
                // 도착했을때 (도착하기 직전에 거리마다 표시 )
                if(time == 5)
                {
                    tts.speak("도착지점 까지 100미터",TextToSpeech.QUEUE_ADD,null,null);
                    tts.speak("도착지점 까지 50미터",TextToSpeech.QUEUE_ADD,null,null);
                    tts.speak("도착지점 까지 20미터",TextToSpeech.QUEUE_ADD,null,null);
                    tts.speak("도착까지 걸린시간 "+ times + ", 수고하셨습니다",TextToSpeech.QUEUE_ADD,null,null);
                }
                if(time == 10)
                    timer.cancel();
                time++;
            }
        };
        timer.schedule(timerTask,0,1000);
    }
    public void speak(String str){
        tts.setPitch(1.0f);
        tts.setSpeechRate(1.0f);
        tts.speak(str ,TextToSpeech.QUEUE_ADD,null,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts!=null)
        {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}