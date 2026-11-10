package com.mycompany.egyc_dutyfree5;

class SalesFor {

    private final int id, cigNum, winNum, beerNum, deviceNum, foodNum, prfuNum, gifNum, winTaxD, winTaxP;
    private final int cigPrice, winprice, beerPrice, devicePrice, foodprice, prfuprice, gifprice;
    private final String EditDate,addDate, branchName, users;

    public SalesFor(String users,String EditDate, String branchName, int id, String addDate, int cigNum, int cigPrice, int winNum, int winprice, int beerNum, int beerPrice, int foodNum, int foodprice, int prfuNum, int prfuprice, int deviceNum, int devicePrice, int gifNum, int gifprice, int winTaxD, int winTaxP) {

        this.EditDate= EditDate;
         this.users = users;
        this.branchName = branchName;
        this.id = id;
        this.addDate = addDate;
        this.cigNum = cigNum;
        this.cigPrice = cigPrice;
        this.winprice = winprice;
        this.winNum = winNum;
        this.beerNum = beerNum;
        this.beerPrice = beerPrice;
        this.foodNum = foodNum;
        this.foodprice = foodprice;
        this.prfuNum = prfuNum;
        this.prfuprice = prfuprice;
        this.deviceNum = deviceNum;
        this.devicePrice = devicePrice;
        this.gifNum = gifNum;
        this.gifprice = gifprice;
        this.winTaxD = winTaxD;
        this.winTaxP = winTaxP;
    }

    public String getEditDate() {
        return EditDate;
    }

    public String getusers() {
        return users;

    }

    public String getbranchName() {
        return branchName;

    }

    public int getId() {
        return id;
    }

    public String getaddDate() {
        return addDate;

    }

    public int getcigNum() {
        return cigNum;
    }

    public int getcigPrice() {
        return cigPrice;
    }

    public int getwinNum() {
        return winNum;
    }

    public int getwinprice() {
        return winprice;
    }

    public int getbeerNum() {
        return beerNum;
    }

    public int getbeerPrice() {
        return beerPrice;
    }

    public int getfoodNum() {
        return foodNum;
    }

    public int getfoodprice() {
        return foodprice;
    }

    public int getprfuNum() {
        return prfuNum;

    }

    public int getprfuprice() {
        return prfuprice;
    }

    public int getdeviceNum() {
        return deviceNum;
    }

    public int getdevicePrice() {
        return devicePrice;
    }

    public int getgifNum() {
        return gifNum;
    }

    public int getgifprice() {
        return gifprice;
    }

    public int getwinTaxD() {
        return winTaxD;
    }

    public int getwinTaxP() {
        return winTaxP;
    }

}
