package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.arturczopek.mycoach.model.add.NewCycle;
import pl.arturczopek.mycoach.model.add.NewSet;
import pl.arturczopek.mycoach.model.database.Cycle;
import pl.arturczopek.mycoach.model.database.Exercise;
import pl.arturczopek.mycoach.model.database.Set;
import pl.arturczopek.mycoach.model.preview.CyclePreview;
import pl.arturczopek.mycoach.repository.CycleRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Service
public class CycleService {

    private CycleRepository cycleRepository;
    private DateService dateService;

    @Autowired
    public CycleService(CycleRepository cycleRepository, DateService dateService) {
        this.cycleRepository = cycleRepository;
        this.dateService = dateService;
    }

    public List<CyclePreview> getCyclePreviews() {
        List<Cycle> cycles = cycleRepository.findAllByOrderByEndDateDesc();

        return cycles
                .stream().map(CyclePreview::buildFromCycle)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Cycle getCycleById(long id) {
        return cycleRepository.findOne(id);
    }

    @Transactional
    public void addCycle(NewCycle newCycle) {
        Cycle cycle = new Cycle();

        if (newCycle.getStartDate() != null) {
            cycle.setStartDate(newCycle.getStartDate());
        } else {
            cycle.setStartDate(dateService.getCurrentDate());
        }

        cycle.setEndDate(null);

        List<Set> sets = new LinkedList<>();

        for (NewSet oneNewSet : newCycle.getSets()) {
            Set tmpSet = new Set();
            tmpSet.setSetName(oneNewSet.getSetName());

            List<Exercise> exercises = new LinkedList<>();

            for (String exerciseName : oneNewSet.getExercises()) {
                Exercise tmpExercise = new Exercise();
                tmpExercise.setExerciseName(exerciseName);
                tmpExercise.setSet(tmpSet);
                exercises.add(tmpExercise);
            }

            tmpSet.setExercises(exercises);
            tmpSet.setCycle(cycle);
            sets.add(tmpSet);
        }

        cycle.setSets(sets);
        cycleRepository.save(cycle);
    }

    public void endCycle(long id) {
        Cycle cycle = cycleRepository.findOne(id);

        if (cycle.getEndDate() == null) {
            cycle.setEndDate(dateService.getCurrentDate());
            cycleRepository.save(cycle);
        }
    }

//    public void updateCycle(CycleToUpdate cycleToUpdate) {
//        Cycle cycle = cycleRepository.findOne(cycleToUpdate.getCycleId());
//
//        if (cycleToUpdate.getStartDate() != null) {
//            cycle.setStartDate(cycleToUpdate.getStartDate());
//        }
//
//        if (cycleToUpdate.getEndDate() != null) {
//            cycle.setEndDate(cycleToUpdate.getEndDate());
//        }
//
//        cycleRepository.save(cycle);
//    }
}
