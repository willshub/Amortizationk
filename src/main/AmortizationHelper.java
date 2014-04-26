package main;

import main.domain.LoanInfo;
import main.util.AmortizationUtil;

/**
 * Created with IntelliJ IDEA.
 * User: wraphale
 * Date: 4/25/14
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class AmortizationHelper {

    private long amountBorrowed = 0;		// in cents
    private double apr = 0d;
    private int initialTermMonths = 0;

    private final double monthlyInterestDivisor = 12d * 100d;
    private double monthlyInterest = 0d;
    private long monthlyPaymentAmount = 0;	// in cents

    private static final double[] borrowAmountRange = new double[] { 0.01d, 1000000000000d };
    private static final double[] aprRange = new double[] { 0.000001d, 100d };
    private static final int[] termRange = new int[] { 1, 1000000 };

    public AmortizationHelper(LoanInfo loanInfo) throws IllegalArgumentException {

        if ((!isValidBorrowAmount(loanInfo.getLoanAmount())) ||
                (!isValidAPRValue(loanInfo.getInterestRate())) ||
                (!isValidTerm(loanInfo.getNumberOfYears()))) {
            throw new IllegalArgumentException();
        }

        amountBorrowed = Math.round(loanInfo.getLoanAmount() * 100);
        apr = loanInfo.getInterestRate();
        initialTermMonths = loanInfo.getNumberOfYears() * 12;

        monthlyPaymentAmount = calculateMonthlyPayment();

        // the following shouldn't happen with the available valid ranges
        // for borrow amount, apr, and term; however, without range validation,
        // monthlyPaymentAmount as calculated by calculateMonthlyPayment()
        // may yield incorrect values with extreme input values
        if (monthlyPaymentAmount > amountBorrowed) {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isValidBorrowAmount(double amount) {
        double range[] = getBorrowAmountRange();
        return ((range[0] <= amount) && (amount <= range[1]));
    }

    public static boolean isValidAPRValue(double rate) {
        double range[] = getAPRRange();
        return ((range[0] <= rate) && (rate <= range[1]));
    }

    public static boolean isValidTerm(int years) {
        int range[] = getTermRange();
        return ((range[0] <= years) && (years <= range[1]));
    }

    public static final double[] getBorrowAmountRange() {
        return borrowAmountRange;
    }

    public static final double[] getAPRRange() {
        return aprRange;
    }

    public static final int[] getTermRange() {
        return termRange;
    }

    /**
     * Method to calculate the monthly payment
     * Formula - M = P * (J / (1 - (Math.pow(1/(1 + J), N))))
     * P = Principal
     * I = Interest
     * J = Monthly Interest in decimal form:  I / (12 * 100)
     * N = Number of months of loan
     * M = Monthly Payment Amount
     * @return
     */
    public long calculateMonthlyPayment() {

        // calculate J
        monthlyInterest = apr / monthlyInterestDivisor;

        // this is 1 / (1 + J)
        double tmp = Math.pow(1d + monthlyInterest, -1);

        // this is Math.pow(1/(1 + J), N)
        tmp = Math.pow(tmp, initialTermMonths);

        // this is 1 / (1 - (Math.pow(1/(1 + J), N))))
        tmp = Math.pow(1d - tmp, -1);

        // M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
        double rc = amountBorrowed * monthlyInterest * tmp;

        return Math.round(rc);
    }

    /**
     * Display the Amortization schedule to the user with columns - payment number, amount of the payment,
     * amount paid to interest, current balance, total payment amount and the interest paid fields
     * To create the amortization table, create a loop in your program and follow these steps:
     * 1.      Calculate H = P x J, this is your current monthly interest
     * 2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
     * 3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
     * 4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
     */
    public void outputAmortizationSchedule() {

        String formatString = "%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n";
        AmortizationUtil.printf(formatString,
                "PaymentNumber", "CurrentBalance", "PaymentAmount",
                "PaymentInterest", "TotalPayments", "TotalInterestPaid");

        long balance = amountBorrowed;
        int paymentNumber = 0;
        long totalPayments = 0;
        long totalInterestPaid = 0;

        // output is in dollars
        formatString = "%1$-20d%2$-20.2f%3$-20.2f%4$-20.2f%5$-20.2f%6$-20.2f\n";
        AmortizationUtil.printf(formatString, paymentNumber++,
                ((double) amountBorrowed) / 100d,
                0d, 0d,
                ((double) totalPayments) / 100d,
                ((double) totalInterestPaid) / 100d);

        final int maxNumberOfPayments = initialTermMonths + 1;
        while ((balance > 0) && (paymentNumber <= maxNumberOfPayments)) {
            // Calculate H = P x J, this is your current monthly interest
            long curMonthlyInterest = Math.round(((double) balance) * monthlyInterest);

            // the amount required to payoff the loan
            long curPayoffAmount = balance + curMonthlyInterest;

            // the amount to payoff the remaining balance may be less than the calculated monthlyPaymentAmount
            long curMonthlyPaymentAmount = Math.min(monthlyPaymentAmount, curPayoffAmount);

            // it's possible that the calculated monthlyPaymentAmount is 0,
            // or the monthly payment only covers the interest payment - i.e. no principal
            // so the last payment needs to payoff the loan
            if ((paymentNumber == maxNumberOfPayments) &&
                    ((curMonthlyPaymentAmount == 0) || (curMonthlyPaymentAmount == curMonthlyInterest))) {
                curMonthlyPaymentAmount = curPayoffAmount;
            }

            // Calculate C = M - H, this is your monthly payment minus your monthly interest,
            // so it is the amount of principal you pay for that month
            long curMonthlyPrincipalPaid = curMonthlyPaymentAmount - curMonthlyInterest;

            // Calculate Q = P - C, this is the new balance of your principal of your loan.
            long curBalance = balance - curMonthlyPrincipalPaid;

            totalPayments += curMonthlyPaymentAmount;
            totalInterestPaid += curMonthlyInterest;

            // output is in dollars
            AmortizationUtil.printf(formatString, paymentNumber++,
                    ((double) curBalance) / 100d,
                    ((double) curMonthlyPaymentAmount) / 100d,
                    ((double) curMonthlyInterest) / 100d,
                    ((double) totalPayments) / 100d,
                    ((double) totalInterestPaid) / 100d);

            // Set P equal to Q and go back to Step 1: You thus loop around until the value Q (and hence P) goes to zero.
            balance = curBalance;
        }
    }
}
