package com.example.Runner8.SingleTon;

public class LoginSingleTon {

    boolean register_check = false;
    boolean login_finish_check = false;

    private static final LoginSingleTon LOGIN_SINGLE_TON_INSTANCE = new LoginSingleTon();

    public static synchronized LoginSingleTon getInstance(){
        return LOGIN_SINGLE_TON_INSTANCE;
    }

    public void setRegister_check(boolean register_check) {
        this.register_check = register_check;
    }

    public boolean isRegister_check() {
        return register_check;
    }

    public void setLogin_finish_check(boolean login_finish_check) {
        this.login_finish_check = login_finish_check;
    }

    public boolean isLogin_finish_check() {
        return login_finish_check;
    }
}
