package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.exception.InvalidDateException;
import pl.arturczopek.mycoach.exception.WrongPermissionException;
import pl.arturczopek.mycoach.model.add.NewWeight;
import pl.arturczopek.mycoach.model.database.User;
import pl.arturczopek.mycoach.model.database.Weight;
import pl.arturczopek.mycoach.model.preview.WeightsPreview;
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
    public List<WeightsPreview> getWeightPreviews(User user) {
        return weightService.getWeightPreviews(user.getUserId());
    }

    @GetMapping("/forDate/{year}/{month}")
    public List<Weight> getMonthWeightDetails(User user, @PathVariable int year, @PathVariable int month) {
        return weightService.getWeightsByYearAndMonth(year, month, user.getUserId());
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added weight measurement")
    public void addWeight(@RequestBody NewWeight weight, User user) throws InvalidDateException {
        weightService.addWeight(weight, user.getUserId());
    }

    @DeleteMapping("/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed weight measurements")
    public void deleteWeights(@RequestBody List<Weight> weights, User user) throws WrongPermissionException {
        weightService.deleteWeights(weights, user.getUserId());
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated weight measurements")
    public void updateWeights(@RequestBody List<Weight> weights, User user) throws InvalidDateException, WrongPermissionException {
        weightService.updateWeights(weights, user.getUserId());
    }
}
