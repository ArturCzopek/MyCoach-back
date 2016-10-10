package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.arturczopek.mycoach.database.entity.Cycle;
import pl.arturczopek.mycoach.database.entity.dto.CyclePreview;
import pl.arturczopek.mycoach.web.service.CycleService;

import java.util.List;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */
@Slf4j
@RestController
@RequestMapping("/cycle")
public class CycleController {

    private CycleService cycleService;

    @Autowired
    public CycleController(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    @RequestMapping(value = "/previews", method = RequestMethod.GET)
    public List<CyclePreview> getPreviews() {
        return cycleService.getCyclePreviews();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Cycle getCycleDetails(@PathVariable long id) {
        return cycleService.getCycleById(id);
    }
}