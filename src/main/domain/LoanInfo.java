package main.domain;

/**
 * Created with IntelliJ IDEA.
 * User: wraphale
 * Date: 4/25/14
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoanInfo {

    private double loanAmount;
    private double interestRate;
    private int numberOfYears;

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getNumberOfYears() {
        return numberOfYears;
    }

    public void setNumberOfYears(int numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

}
