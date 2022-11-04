package course.concurrency.m0_intro;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WelcomeClass {

    private final String message = "hello";


    public String getMessage() {
        Executors.newSingleThreadExecutor();
        return message;
    }
}
