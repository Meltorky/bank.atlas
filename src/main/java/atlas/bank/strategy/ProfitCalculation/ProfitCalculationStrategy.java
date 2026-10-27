package atlas.bank.strategy.ProfitCalculation;

import java.math.BigDecimal;

public interface ProfitCalculationStrategy {

    BigDecimal calculateProfit(
            BigDecimal principal,
            BigDecimal rate
    );

    BigDecimal calculateProfit(
            BigDecimal principal,
            BigDecimal rate);
}