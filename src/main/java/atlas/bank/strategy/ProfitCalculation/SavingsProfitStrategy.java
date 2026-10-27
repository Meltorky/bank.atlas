package atlas.bank.strategy.ProfitCalculation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SavingsProfitStrategy implements ProfitCalculationStrategy {

    @Override
    public BigDecimal calculateProfit(
            BigDecimal principal,
            BigDecimal rate) {

        return principal.multiply(rate);
    }
}