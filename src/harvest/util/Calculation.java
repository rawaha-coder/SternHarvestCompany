package harvest.util;

public class Calculation {

    public static double companyCharge(double allQuantity , double defectiveQuantity, double price) {
        return (allQuantity - defectiveQuantity) * price;
    }

    public static double employeeCharge(double allQuantity ,
                                       double defectiveQuantity,
                                       double penaltyGeneral,
                                       double defectiveGeneral,
                                       double price,
                                       double totalCredit,
                                       double totalTransport)
    {
        return ((allQuantity - defectiveQuantity - penaltyGeneral - defectiveGeneral) * price) - (totalCredit + totalTransport);
    }

    public static double hoursCharge(double totalMinute,
                                        double price,
                                        double totalCredit,
                                        double totalTransport)
    {
        return ((totalMinute * price) - (totalCredit + totalTransport));
    }
}
