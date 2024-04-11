package com.example.Util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

// 这个工具类用于统一归类属性，方便统一修改，比如在Redis中的前缀等等
public class Const {
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";
    public static final String VERIFY_EMAIL_DATA = "verify:email";
    public static final String RESET_PASSWORD_ACCOUNT="reset";
    public static final String EMAIL_QUEUE_NAME = "mail";
    public static final String EMAIL_EXCHANGE_NAME = "mailExchange";
    public static final String EMAIL_ROUTING_KEY = "product.addEmail";
    public static final String PROCESS_QUEUE_NAME = "process";
    public static final String PROCESS_EXCHANGE_NAME = "processExchange";
    public static final String PROCESS_ROUTING_KEY = "product.process";
    public static final String DEAD_LETTER_EXCHANGE_NAME = "deadEmailExchange";
    public static final String DEAD_LETTER_QUEUE_NAME = "deadEmailQueue";
    public static final String DEAD_LETTER_QUEUE_ROUTING_KEY = "deadEmail.key";
    public static final Integer X_MAX_LENGTH = 10;
    public static final Integer X_TTL = 10000;

    public static class SendEmailLogStatus {
        public static final int CONSUME_PROCESS = 0;
        public static final int CONSUME_SUCCESS = 1;
        public static final int CONSUME_FAILURE = 2;
        public static final int CONSUMED = 3;
    }

    public static class ResetPasswordStatus{
        public static final String UN_ASK_ACCOUNT = "UN_ASK_ACCOUNT";
        public static final String WAIT_FOR_SEND = "WAIT_FOR_SEND";
        public static final String WAIT_FOR_IDENTIFY = "WAIT_FOR_IDENTIFY";
        public static final String WAIT_FOR_RESET = "WAIT_FOR_RESET";
        public static final String RESET_COMPLETE= "RESET_COMPLETE";
    }

    @Getter
    public enum Level {
        LEVEL_1(0,"新手上路",4999),
        LEVEL_2(5000,"小试牛刀",9999),
        LEVEL_3(10000,"崭露头角",14999),
        LEVEL_4(15000,"轻车熟路",19999),
        LEVEL_5(20000,"秋名车神",24999),
        MAX_LEVEL(25000,"羽化登仙",30000);

        private final int minExp;
        private final String name;
        private final int maxExp;

        Level(int minExp, String name, int maxExp) {
            this.minExp = minExp;
            this.name = name;
            this.maxExp = maxExp;
        }
    }

    public static final Map<Integer,String> categoryMap = Map.of(
            0,"PC游戏", 1,"模拟器游戏", 2,"至高之神", 3,"手机游戏",4,"游戏插件"
    );

}
