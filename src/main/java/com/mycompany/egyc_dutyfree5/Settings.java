package com.mycompany.egyc_dutyfree5;

public class Settings {
    private final double cigarPrice;
    private final double cigPrice;
    private final double exchangePrice;
    
   
    private final String cigStatus;
    private final String cigarStatus;
    private final String winStatus;
    private final String beerStatus;
    private final String perfumStatus;
    private final String devicesStatus;
    private final String giftsStatus;
    private final String foodStatus;

    public Settings(double cigarPrice, double cigPrice, double exchangePrice,
                    String cigStatus, String cigarStatus, String winStatus, String beerStatus,
                    String perfumStatus, String devicesStatus, String giftsStatus, String foodStatus) {
        this.cigarPrice = cigarPrice;
        this.cigPrice = cigPrice;
        this.exchangePrice = exchangePrice;
        this.cigStatus = cigStatus;
        this.cigarStatus = cigarStatus;
        this.winStatus = winStatus;
        this.beerStatus = beerStatus;
        this.perfumStatus = perfumStatus;
        this.devicesStatus = devicesStatus;
        this.giftsStatus = giftsStatus;
        this.foodStatus = foodStatus;
    }

    // Getters للأسعار
    public double getCigarPrice() { return cigarPrice; }
    public double getCigPrice() { return cigPrice; }
    public double getExchangePrice() { return exchangePrice; }

    // Getters لحالات الأصناف
    public String getCigStatus() { return cigStatus; }
    public String getCigarStatus() { return cigarStatus; }
    public String getWinStatus() { return winStatus; }
    public String getBeerStatus() { return beerStatus; }
    public String getPerfumStatus() { return perfumStatus; }
    public String getDevicesStatus() { return devicesStatus; }
    public String getGiftsStatus() { return giftsStatus; }
    public String getFoodStatus() { return foodStatus; }
}