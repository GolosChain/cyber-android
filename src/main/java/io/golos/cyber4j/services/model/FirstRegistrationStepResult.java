package io.golos.cyber4j.services.model;

import java.util.Date;

public class FirstRegistrationStepResult {
    private int code;
    private RegistrationStrategy strategy;
    private Date nextSmsRetry;

    public FirstRegistrationStepResult(int code, RegistrationStrategy strategy, Date nextSmsRetry) {
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

    public Date getNextSmsRetry() {
        return nextSmsRetry;
    }

    public void setNextSmsRetry(Date nextSmsRetry) {
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
