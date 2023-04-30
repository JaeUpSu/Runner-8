# 🏃‍♂️ Runner-8

  > 달리기 경쟁 다이어트 App (안드로이드)

## ⚒️ Skills 
<div>
  <p>Android Studio</p>
  <p>FireStore [RDB]</p>
  <p>Java</p>
  <p>Google GPS</p>
  <p>Naver map</p>
  <p>No Sql</p>
<div/>
  
## 🦴 Main Algorithm
  
### 👋 Kalman Filter

      오픈된 GPS 는 개인의 신상때문에 완벽한 위치를 제공하지 않기 때문에
      이러한 오차를 줄이기 위해 논문중에 칼만 필터를 알아보고
      해당 내용에 등장하는 측정치와 예측치의 중간값인 추정치를 적용
      
### 👋 DBScan Clustering

      Map 에 Marker 를 표시하다보니 많은 Data 가 생길 시 Map 이 지저분해지는 현상이 있기 때문에
      Clustering 기법 중 DBScan Clustering 을 적용하여 해당 Marker 를 밀도 기반으로 클러스터링하여
      아이콘을 다르게 표현
      
### 👋 DMACA Kcal 

      거리만을 가지고는 정확한 Kcal 값을 계산할 수는 없어
      고도까지 고려하여 정밀도를 높이는 운동 거리를 계산하는 방법 적용
  
## 📰 Main Function


### ⭐ Running Simulation
      각 Gps 의 좌표값인 latitude 와 longtitude 를 일정 간격으로 기록하여
      DB 에 저장해서 해당 런닝한 거리와 데이터 일부를 저장하여 공유
      
      공유한 거리는 다른 유저가 Race 버튼을 누르면
      해당 거리에 기록을 남기는 top 5 등 여러 유저의 기록들이 
      마커로 Simulation 처럼 보여주며 해당 기술을 통해
      경쟁하는 느낌을 주어 더욱 재미 요소를 주도록 함
      
      해당 기술에는 그리기 / 달리기 / 따라가기 가 존재
      
        그리기는 코스 경로를 그려서 해당 코스에서 달릴 시 예상 데이터를 보여줌
        
        달리기는 Gps 위치를 실시간으로 업데이트하여 공유 가능한 코스를 만들고 측정 데이터를 표시
                TTS 를 적용하여 일정 기준으로 측정 데이터를 유저에게 알려줌
        
        따라가기는 공유된 코스들 중 하나를 골라 해당 유저와 경쟁을 하는 듯한 시뮬레이션
                  mode 는 sole / many / me 가 존재
        
    
### FunDB (Statics) 
### Kcal Direcotry
### Communication
### Notification

## 👨‍💻 References

- 칼만 필터 기반 사용자 운동거리 측정 알고리즘
http://koreascience.or.kr/article/JAKO201905653789235.pdf
- 고도를 고려한 정밀도 높은 운동거리 측정시스템
https://www.koreascience.or.kr/article/JAKO201211559436231.pdf
- 식품의약품안전처
식품영양성분 DB
