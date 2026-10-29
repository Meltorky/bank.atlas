package atlas.bank.strategy.ProfitCalculation;

import java.math.BigDecimal;

public interface ProfitCalculationStrategy {

    BigDecimal calculateProfit(BigDecimal amount);
}