package com.hank.oma.entity;

public class CardResult {

    private String rapdu;
    private int status;
    private String message;

    public String getRapdu() {
        return rapdu;
    }

    public void setRapdu(String rapdu) {
        this.rapdu = rapdu;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CardResult(String rapdu, int status, String message) {
        super();
        this.rapdu = rapdu;
        this.status = status;
        this.message = message;
    }

    public CardResult(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CardResult [rapdu=" + rapdu + ", status=" + status + ", message=" + message + "]";
    }

}
