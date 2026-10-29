package atlas.bank.strategy.ProfitCalculation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("savingsProfitStrategy")
public class SavingsProfitStrategy
        implements ProfitCalculationStrategy {

    private static final BigDecimal FIRST_TIER_LIMIT =
            new BigDecimal("50000");

    private static final BigDecimal FIRST_TIER_RATE =
            new BigDecimal("0.005");

    private static final BigDecimal SECOND_TIER_RATE =
            new BigDecimal("0.0075");

    @Override
    public BigDecimal calculateProfit(BigDecimal balance) {
        BigDecimal firstTierBalance =
                balance.min(FIRST_TIER_LIMIT);

        BigDecimal remainingBalance =
                balance.subtract(firstTierBalance);

        BigDecimal firstTierProfit =
                firstTierBalance.multiply(FIRST_TIER_RATE);

        BigDecimal secondTierProfit =
                remainingBalance.multiply(SECOND_TIER_RATE);

        return firstTierProfit.add(secondTierProfit)
                .setScale(2, RoundingMode.HALF_UP);
    }
}