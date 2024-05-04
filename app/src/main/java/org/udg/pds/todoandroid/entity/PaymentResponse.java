package org.udg.pds.todoandroid.entity;

public class PaymentResponse {
    private String paymentMethod;
    public PaymentResponse() {
    }
    public PaymentResponse(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    // Getter y setter para paymentMethod
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
