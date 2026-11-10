//تم عمل هذا الكلاس لياخذ القيم من كلاس(item )
//والهدف منه لجعل دالة حساب القيم ترجع أكثر من قيمة 
package com.mycompany.egyc_dutyfree5;

public class TaxesResult {
    private final double importTax;
    private final double tableTax;
    private final double addValueTax;
    private final double developTax;
    private final double total;

    public TaxesResult(double importTax, double tableTax, double addValueTax , double developTax, double total) {
        this.importTax = importTax;
        this.tableTax = tableTax;
        this.addValueTax = addValueTax;
        this.developTax = developTax;
        this.total = total;
        
    }

    public double getImportTax() {
        return importTax;
    }

    public double getTableTax() {
        return tableTax;
    }

    public double getAddValueTax() {
        return addValueTax;
    }
    public double getDevelopTax() {
        return developTax;
    }
     public double getTotal() {
        return total;
    }
}
