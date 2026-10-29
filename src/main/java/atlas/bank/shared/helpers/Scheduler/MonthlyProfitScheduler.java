package atlas.bank.shared.helpers.Scheduler;

import atlas.bank.services.MonthlyProfitServiceImp;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonthlyProfitScheduler {

    private final MonthlyProfitServiceImp monthlyProfitService;

    public MonthlyProfitScheduler(
            MonthlyProfitServiceImp monthlyProfitService) {
        this.monthlyProfitService = monthlyProfitService;
    }

    @Scheduled(
            cron = "0 0 23 L * *",
            zone = "Africa/Cairo"
    )
    public void runMonthlyProfits() {
        monthlyProfitService.processMonthlyProfits();
    }

    // This because if the App was Offline on the end of the month
    // So once the App become Online again we run the method
    @EventListener(ApplicationReadyEvent.class)
    public void runProfitsOnStartup() {
        monthlyProfitService.processMonthlyProfits();
    }
}
