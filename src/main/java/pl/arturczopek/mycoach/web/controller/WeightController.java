package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.arturczopek.mycoach.database.entity.Weight;
import pl.arturczopek.mycoach.database.entity.dto.WeightDatesPreview;
import pl.arturczopek.mycoach.web.service.WeightService;

import java.util.List;

/**
 * @Author arturczopek
 * @Date 10/9/16
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

    @RequestMapping(value = "/previews", method = RequestMethod.GET)
    public List<WeightDatesPreview> getPreviews() {
        return weightService.getWeightDatesList();
    }

    @RequestMapping(value = "/forDate/{year}/{month}", method = RequestMethod.GET)
    public List<Weight> getMonthWeightDetails(@PathVariable int year, @PathVariable int month) {
        return weightService.getWeightsByYearAndMonth(year, month);
    }
}
