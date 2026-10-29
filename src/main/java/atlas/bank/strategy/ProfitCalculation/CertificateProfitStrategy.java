package atlas.bank.strategy.ProfitCalculation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("certificateProfitStrategy")
public class CertificateProfitStrategy
        implements ProfitCalculationStrategy {

    private static final BigDecimal MONTHLY_RATE =
            new BigDecimal("0.01");

    @Override
    public BigDecimal calculateProfit(BigDecimal principal) {
        return principal.multiply(MONTHLY_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}