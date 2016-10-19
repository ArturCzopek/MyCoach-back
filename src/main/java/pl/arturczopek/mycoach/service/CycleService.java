package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.arturczopek.mycoach.database.entity.Cycle;
import pl.arturczopek.mycoach.database.entity.Exercise;
import pl.arturczopek.mycoach.database.entity.Set;
import pl.arturczopek.mycoach.database.repository.CycleRepository;
import pl.arturczopek.mycoach.dto.add.CycleToAdd;
import pl.arturczopek.mycoach.dto.add.SetToAdd;
import pl.arturczopek.mycoach.dto.preview.CyclePreview;
import pl.arturczopek.mycoach.dto.update.CycleToUpdate;

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
    public void addCycle(CycleToAdd cycleToAdd) {
        Cycle cycle = new Cycle();

        if (cycleToAdd.getStartDate() != null) {
            cycle.setStartDate(cycleToAdd.getStartDate());
        } else {
            cycle.setStartDate(dateService.getCurrentDate());
        }

        cycle.setEndDate(null);

        List<Set> sets = new LinkedList<>();

        for (SetToAdd oneSetToAdd : cycleToAdd.getSets()) {
            Set tmpSet = new Set();
            tmpSet.setSetName(oneSetToAdd.getSetName());

            List<Exercise> exercises = new LinkedList<>();

            for (String exerciseName : oneSetToAdd.getExercises()) {
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

    public void updateCycle(CycleToUpdate cycleToUpdate) {
        Cycle cycle = cycleRepository.findOne(cycleToUpdate.getCycleId());

        if(cycleToUpdate.getStartDate() != null) {
            cycle.setStartDate(cycleToUpdate.getStartDate());
        }

        if(cycleToUpdate.getEndDate() != null) {
            cycle.setEndDate(cycleToUpdate.getEndDate());
        }

        cycleRepository.save(cycle);
    }
}
