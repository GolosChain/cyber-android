package io.golos.cyber4j.model;

import java.util.Date;

public class FirstRegistrationStepResult {
    private int code;
    private RegistrationStrategy strategy;
    private long nextSmsRetry;

    public FirstRegistrationStepResult(int code, RegistrationStrategy strategy, long nextSmsRetry) {
        this.code = code;
        this.strategy = strategy;
        this.nextSmsRetry = nextSmsRetry;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RegistrationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(RegistrationStrategy strategy) {
        this.strategy = strategy;
    }

    public long getNextSmsRetry() {
        return nextSmsRetry;
    }

    public void setNextSmsRetry(long nextSmsRetry) {
        this.nextSmsRetry = nextSmsRetry;
    }

    @Override
    public String toString() {
        return "FirstRegistrationStepResult{" +
                "code=" + code +
                ", strategy=" + strategy +
                ", nextSmsRetry=" + nextSmsRetry +
                '}';
    }
}
