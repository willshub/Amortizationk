package main;

import main.domain.LoanInfo;
import main.util.AmortizationUtil;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wraphale
 * Date: 4/25/14
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Executor {

    public static void main(String [] args) {

        String[] userPrompts = {
                "Please enter the amount you would like to borrow: ",
                "Please enter the annual percentage rate used to repay the loan: ",
                "Please enter the term, in years, over which the loan is repaid: "
        };

        String line = "";
        double amount = 0;
        double apr = 0;
        int years = 0;

        for (int i = 0; i< userPrompts.length; ) {
            String userPrompt = userPrompts[i];
            try {
                line = AmortizationUtil.readLine(userPrompt);
            } catch (IOException e) {
                AmortizationUtil.print("An IOException was encountered. Terminating program.\n");
                return;
            }

            boolean isValidValue = true;
            try {
                switch (i) {
                    case 0:
                        amount = Double.parseDouble(line);
                        if (!AmortizationHelper.isValidBorrowAmount(amount)) {
                            isValidValue = false;
                            double range[] = AmortizationHelper.getBorrowAmountRange();
                            AmortizationUtil.print("Please enter a positive value between " + range[0] + " and " + range[1] + ". ");
                        }
                        break;
                    case 1:
                        apr = Double.parseDouble(line);
                        if (!AmortizationHelper.isValidAPRValue(apr)) {
                            isValidValue = false;
                            double range[] = AmortizationHelper.getAPRRange();
                            AmortizationUtil.print("Please enter a positive value between " + range[0] + " and " + range[1] + ". ");
                        }
                        break;
                    case 2:
                        years = Integer.parseInt(line);
                        if (!AmortizationHelper.isValidTerm(years)) {
                            isValidValue = false;
                            int range[] = AmortizationHelper.getTermRange();
                            AmortizationUtil.print("Please enter a positive integer value between " + range[0] + " and " + range[1] + ". ");
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                isValidValue = false;
            }
            if (isValidValue) {
                i++;
            } else {
                AmortizationUtil.print("An invalid value was entered.\n");
            }
        }
        LoanInfo loanInfo = new LoanInfo();
        loanInfo.setLoanAmount(amount);
        loanInfo.setInterestRate(apr);
        loanInfo.setNumberOfYears(years);

        try {
            AmortizationHelper as = new AmortizationHelper(loanInfo);
            as.outputAmortizationSchedule();
        } catch (IllegalArgumentException e) {
            AmortizationUtil.print("Unable to process the values entered. Terminating program.\n");
        }
    }
}
