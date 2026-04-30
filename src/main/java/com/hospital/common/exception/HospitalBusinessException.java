package com.hospital.common.exception;

public class HospitalBusinessException extends RuntimeException {
    public HospitalBusinessException(String message) {
        super(message);
    }
    public HospitalBusinessException(String message,String code) {
        super(message);
    }
}
