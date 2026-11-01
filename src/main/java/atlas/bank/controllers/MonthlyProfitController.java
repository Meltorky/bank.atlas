package atlas.bank.controllers;

import atlas.bank.abstracts.MonthlyProfitService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monthly-profits")
public class MonthlyProfitController {

    private final MonthlyProfitService monthlyProfitService;

    public MonthlyProfitController(MonthlyProfitService monthlyProfitService) {
        this.monthlyProfitService = monthlyProfitService;
    }

    @PostMapping("/process")
    public ResponseEntity<Void> processMonthlyProfits() {

        monthlyProfitService.processMonthlyProfits();

        return ResponseEntity.ok().build();
    }
}