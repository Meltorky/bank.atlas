package atlas.bank.abstracts;

import org.springframework.transaction.annotation.Transactional;

public interface MonthlyProfitService {
    @Transactional
    void processMonthlyProfits();
}
