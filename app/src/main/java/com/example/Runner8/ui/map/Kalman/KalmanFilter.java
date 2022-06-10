package com.example.Runner8.ui.map.Kalman;

import android.util.Log;

public class KalmanFilter {

    private static final int THREAD_PRIORITY = 5;

    private static final double DEG_TO_METER = 81225.0;
    private static final double METER_TO_DEG = 1.0 / DEG_TO_METER;

    private static final double TIME_STEP = 1.0;
    private static final double COORDINATE_NOISE = 4.0 * METER_TO_DEG;
    private static final double ALTITUDE_NOISE = 100.0;

    private long timeStamp; // millis
    private double latitude; // degree
    private double longitude; // degree
    private double variance = -1; // P matrix. Initial estimate of error
    private double variance_ver = -1;
    private double al_change_rate = 1;
    private double altitude;                                // 추정치
    private double prev_altitude;                           // 이전 고도 값
    private double pred_altitude;                           // 예측치

    private double accuracy;
    private double speed;

    private double noise;

    private boolean mPredicted = false;

    Tracker1D mLatitudeTracker, mLongitudeTracker, mAltitudeTracker;

    // Init method (use this after constructor, and before process)
    // if you are using last known data from gps)
    public void setState(double latitude, double longitude, long timeStamp, double accuracy, double altitude, double ver_Accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeStamp = timeStamp;
        this.variance = accuracy * DEG_TO_METER;
        this.variance_ver = ver_Accuracy * ver_Accuracy;
        this.altitude = altitude;
        this.prev_altitude = altitude;
        this.pred_altitude = altitude;

        mLatitudeTracker = new Tracker1D(TIME_STEP, COORDINATE_NOISE);
        mLatitudeTracker.setState(latitude, 0.0, accuracy);
        mLatitudeTracker.predict(0.0);

        mLongitudeTracker = new Tracker1D(TIME_STEP, COORDINATE_NOISE);
        mLongitudeTracker.setState(longitude, 0.0, accuracy);
        mLongitudeTracker.predict(0.0);

        mAltitudeTracker = new Tracker1D(TIME_STEP, ALTITUDE_NOISE);
        mAltitudeTracker.setState(altitude, 0.0, ver_Accuracy);
        mAltitudeTracker.predict(0.0);

    }

    /**
     * Kalman filter processing for latitude and longitude
     * <p>
     * newLatitude - new measurement of latitude
     * newLongitude - new measurement of longitude
     * accuracy - measurement of 1 standard deviation error in meters
     * newTimeStamp - time of measurement in millis
     */

    // Add Param
    public void process(double newSpeed, double newLatitude, double newLongitude,
                        long newTimeStamp, double newAccuracy, double newAltitude, double new_ver_Accuracy) {
        // Uncomment this, if you are receiving accuracy from your gps
        // if (newAccuracy < Constants.MIN_ACCURACY) {
        //      newAccuracy = Constants.MIN_ACCURACY;
        // }

        // Log.i("newLongitude", newLongitude + "") ;
        // Log.i("newLatitude", newLatitude + "") ;

        double position, noise;

        if (variance < 0) {
            // if variance < 0, object is unitialised, so initialise with current values
            setState(newLatitude, newLongitude, newTimeStamp, newAccuracy, newAltitude, new_ver_Accuracy);
        } else {
            // else apply Kalman filter
            long duration = newTimeStamp - this.timeStamp;

            // Log.i("duration", duration + "");
            if (duration > 0) {
                // time has moved on, so the uncertainty in the current position increases
                variance += duration * newSpeed * newSpeed / 1000;

                pred_altitude = newAltitude / prev_altitude * altitude;

                timeStamp = newTimeStamp;

                // Log.i("variance", variance + "") ;
            }

            Log.i("altitude", newAltitude + "");
            Log.i("latitude", newLatitude + "") ;
            Log.i("longitude", newLongitude + "") ;

            if(!mPredicted)
                mLatitudeTracker.predict(0.0);

            noise = newAccuracy * METER_TO_DEG;
            mLatitudeTracker.update(newLatitude, noise);

            if(!mPredicted)
                mLongitudeTracker.predict(0.0);

            noise = newAccuracy * Math.cos(Math.toRadians(latitude)) * METER_TO_DEG;

            mLongitudeTracker.update(newLongitude, noise);

            if(!mPredicted)
                mAltitudeTracker.predict(0.0);

            noise = new_ver_Accuracy;
            mAltitudeTracker.update(newAltitude, noise);

            mPredicted = false;


            mLatitudeTracker.predict(0.0);
            latitude = mLatitudeTracker.getPosition();
            accuracy = mLatitudeTracker.getAccuracy();

            speed = mLongitudeTracker.getVelocity();
            Log.i("pred_SPEED", speed + "");

            mLongitudeTracker.predict(0.0);
            longitude = mLongitudeTracker.getPosition();


            speed = mLongitudeTracker.getVelocity();

            mAltitudeTracker.predict(0.0);
            altitude = mAltitudeTracker.getPosition();


            mPredicted = true;

            Log.i("SPEED", newSpeed + "");
            Log.i("pred_SPEED", speed + "");

            Log.i("pred_altitude", altitude + "");
            Log.i("pred_latitude", latitude + "") ;
            Log.i("pred_longitude", longitude + "") ;



            /*

            // Kalman gain matrix 'k' = Covariance * Inverse(Covariance + MeasurementVariance)
            // because 'k' is dimensionless,
            // it doesn't matter that variance has different units to latitude and longitude

            // 위치 칼만 구하기
            double k = variance / (variance + newAccuracy * newAccuracy);

            // 고도 칼만 구하기 (accuracy 수정 필요)
            double k_ver = variance_ver /  (variance_ver + new_ver_Accuracy * new_ver_Accuracy);

            latitude += k * (newLatitude - latitude);
            longitude += k * (newLongitude - longitude);

            altitude = pred_altitude + k_ver * (newAltitude - pred_altitude);

            // 오차공분산 구하기
            variance = (1 - k) * variance;
            variance_ver = (1 - k_ver) * variance_ver;

            Log.i("k_ver", k_ver + "");
            Log.i("variance_ver", variance_ver + "");
            // Log.i("latitude", latitude + "") ;
            // Log.i("longitude", longitude + "") ;

             */

        }
    }

    public double getVariance_ver() {
        return variance_ver;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getVariance() {
        return variance;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double getAltitude() {
        return altitude;
    }
    public double getAccuracy(){
        return accuracy;
    }

    public Double diff_result(double alt1, double alt2){
        return alt1 - alt2;
    }
    public String time_result_alt(double dist, double diff){
        Double move_dist = Math.sqrt(dist*dist + diff*diff);
        Integer time = 1 * (int)(move_dist / 100) - (int)(move_dist / 1600);
        String result = time+ " 분";

        return result;
    }
}