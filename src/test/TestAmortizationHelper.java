package test;

import main.AmortizationHelper;
import main.domain.LoanInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: wraphale
 * Date: 4/25/14
 * Time: 7:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestAmortizationHelper {

    LoanInfo loanInfo;

    @Before
    public void setUp() {
        loanInfo = new LoanInfo();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateMonthlyPayment_withInValidLoanAmount_ShouldReturnException() throws IllegalArgumentException{

        loanInfo.setLoanAmount(0);
        loanInfo.setInterestRate(4.75);
        loanInfo.setNumberOfYears(22);

        AmortizationHelper helper = new AmortizationHelper(loanInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateMonthlyPayment_withInValidInterestRate_ShouldReturnException() throws IllegalArgumentException{

        loanInfo.setLoanAmount(500008);
        loanInfo.setInterestRate(105);
        loanInfo.setNumberOfYears(22);

        AmortizationHelper helper = new AmortizationHelper(loanInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateMonthlyPayment_withInValidNumberOfYears_ShouldReturnException() throws IllegalArgumentException{

        loanInfo.setLoanAmount(500008);
        loanInfo.setInterestRate(4.75);
        loanInfo.setNumberOfYears(1000000002);

        AmortizationHelper helper = new AmortizationHelper(loanInfo);
    }

    @Test
    public void testCalculateMonthlyPayment_withValidValues_ShouldReturnMonthlyPayment() {

        loanInfo.setLoanAmount(500008);
        loanInfo.setInterestRate(4.75);
        loanInfo.setNumberOfYears(22);

        AmortizationHelper helper = new AmortizationHelper(loanInfo);
        double actual = helper.calculateMonthlyPayment();
        System.out.println("actual: " + actual);

        Assert.assertTrue(305629 == actual);
    }
}
