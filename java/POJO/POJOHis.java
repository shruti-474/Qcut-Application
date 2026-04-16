package com.mountreachsolution.qcut.POJO;

public class POJOHis {
    String id;
    String shopName;
    String shopAddress;
    String shopEmail;
    String finalName;
    String finalEmail;
    String finalDate;
    String finalTime;
    String finalService;
    String totalAmount;
    String paymentMode;
    String paymentStatus;
    String Appoinmentstatus;


    public POJOHis(String id, String shopName, String shopAddress, String shopEmail, String finalName, String finalEmail, String finalDate, String finalTime, String finalService, String totalAmount, String paymentMode, String paymentStatus, String appoinmentstatus) {
        this.id = id;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopEmail = shopEmail;
        this.finalName = finalName;
        this.finalEmail = finalEmail;
        this.finalDate = finalDate;
        this.finalTime = finalTime;
        this.finalService = finalService;
        this.totalAmount = totalAmount;
        this.paymentMode = paymentMode;
        this.paymentStatus = paymentStatus;
        Appoinmentstatus = appoinmentstatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getFinalName() {
        return finalName;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public String getFinalEmail() {
        return finalEmail;
    }

    public void setFinalEmail(String finalEmail) {
        this.finalEmail = finalEmail;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public String getFinalService() {
        return finalService;
    }

    public void setFinalService(String finalService) {
        this.finalService = finalService;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAppoinmentstatus() {
        return Appoinmentstatus;
    }

    public void setAppoinmentstatus(String appoinmentstatus) {
        Appoinmentstatus = appoinmentstatus;
    }
}
