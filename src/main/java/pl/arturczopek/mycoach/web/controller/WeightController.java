package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.model.database.Weight;
import pl.arturczopek.mycoach.model.preview.WeightDatesPreview;
import pl.arturczopek.mycoach.service.WeightService;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Slf4j
@RestController
@RequestMapping("/weight")
public class WeightController {

    private WeightService weightService;

    @Autowired
    public WeightController(WeightService weightService) {
        this.weightService = weightService;
    }

    @GetMapping("/previews")
    public List<WeightDatesPreview> getPreviews() {
        return weightService.getWeightDatesList();
    }

    @GetMapping("/forDate/{year}/{month}")
    public List<Weight> getMonthWeightDetails(@PathVariable int year, @PathVariable int month) {
        return weightService.getWeightsByYearAndMonth(year, month);
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano pomiar wagi")
    public void addWeight(@RequestBody Weight weight) {
        weightService.addWeight(weight);
    }
}
