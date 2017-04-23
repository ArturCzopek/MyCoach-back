package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.add.NewCycle;
import pl.arturczopek.mycoach.model.add.NewSet;
import pl.arturczopek.mycoach.model.database.*;
import pl.arturczopek.mycoach.model.preview.CyclePreview;
import pl.arturczopek.mycoach.repository.*;

import javax.management.InvalidAttributeValueException;
import javax.transaction.Transactional;
import java.time.LocalDate;
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
    private ExerciseSessionRepository exerciseSessionRepository;
    private ExerciseRepository exerciseRepository;
    private TrainingRepository trainingRepository;
    private SeriesRepository seriesRepository;
    private SetRepository setRepository;

    @Autowired
    public CycleService(CycleRepository cycleRepository, DateService dateService, ExerciseSessionRepository exerciseSessionRepository, ExerciseRepository exerciseRepository, TrainingRepository trainingRepository, SeriesRepository seriesRepository, SetRepository setRepository) {
        this.cycleRepository = cycleRepository;
        this.dateService = dateService;
        this.exerciseSessionRepository = exerciseSessionRepository;
        this.exerciseRepository = exerciseRepository;
        this.trainingRepository = trainingRepository;
        this.seriesRepository = seriesRepository;
        this.setRepository = setRepository;
    }

    public Cycle getActiveCycle() {
        return cycleRepository.findOneByIsFinishedFalse();
    }

    public List<CyclePreview> getCyclePreviews() {
        List<Cycle> cycles = cycleRepository.findAllByOrderByIsFinishedDescEndDateAsc();

        return cycles
                .stream().map(CyclePreview::buildFromCycle)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Cycle getCycleById(long id) {
        return cycleRepository.findOne(id);
    }

    public boolean hashUserEveryCycleFinished() {
        return cycleRepository.countByIsFinishedFalse() == 0;
    }

    @Transactional
    public void addCycle(NewCycle newCycle) throws InvalidAttributeValueException {
        Cycle cycle = new Cycle();

        if (newCycle.getStartDate() != null) {
            cycle.setStartDate(newCycle.getStartDate());
        } else {
            cycle.setStartDate(dateService.getCurrentDate());
        }

        cycle.setEndDate(null);

        if (!isNewCycleDateValid(cycle)) {
            throw new InvalidAttributeValueException("Start date must be later than last end date");
        }

        if (!areNewSetsNamesValid(newCycle.getSets())) {
            throw new DuplicateKeyException("Sets must have different names");
        }

        cycleRepository.save(cycle);

        List<Set> sets = new LinkedList<>();

        for (NewSet setToAdd : newCycle.getSets()) {
            Set newSet = new Set();
            newSet.setSetName(setToAdd.getSetName());
            newSet.setCycleId(cycle.getCycleId());
            sets.add(newSet);
        }

        cycle.setSets(sets);
        cycleRepository.save(cycle);
    }

    private boolean areNewSetsNamesValid(List<NewSet> sets) {
        for (int i = 0; i < sets.size(); i++) {
            for(int j = i + 1; j < sets.size(); j++) {
                if (sets.get(j).getSetName().equalsIgnoreCase(sets.get(i).getSetName())) {
                    return false;
                }
            }
        }

        return true;
    }

    public void deleteCycle(Cycle cycle) {

        cycle.getSets().forEach((Set set) -> {
            set.getTrainings().forEach((Training training) -> trainingRepository.delete(training.getTrainingId()));

            set.getExercises().forEach((Exercise exercise) -> {
                exercise.getExerciseSessions().forEach((ExerciseSession session) -> {
                    session.getSeries().forEach((Series series) -> seriesRepository.delete(series.getSeriesId()));
                    exerciseSessionRepository.delete(session.getExerciseSessionId());
                });
                exerciseRepository.delete(exercise.getExerciseId());
            });

            setRepository.delete(set.getSetId());
        });

        cycleRepository.delete(cycle.getCycleId());
    }

    public void updateCycle(Cycle cycle) {
        Cycle cycleToEdit = cycleRepository.findOne(cycle.getCycleId());
        cycleToEdit.setStartDate(cycle.getStartDate());

        cycleToEdit.setFinished(cycle.isFinished());

        if (cycle.isFinished() && cycle.getEndDate() != null) {
            cycleToEdit.setEndDate(cycle.getEndDate());
        } else if (cycle.isFinished()) {
            cycleToEdit.setEndDate(dateService.getCurrentDate());
        } else {
            cycleToEdit.setEndDate(null);
        }

        cycleRepository.save(cycleToEdit);
    }

    private boolean isNewCycleDateValid(Cycle cycle) {
        Cycle cycleFromDb = cycleRepository.findFirstByOrderByEndDateDesc();

        if (cycle.getStartDate().before(cycleFromDb.getEndDate())) {
            return false;
        }

        LocalDate cycleStartDate = cycle.getStartDate().toLocalDate();
        LocalDate cycleFromDbEndDate = cycleFromDb.getEndDate().toLocalDate();

        if (cycleStartDate.equals(cycleFromDbEndDate)) {
            return false;
        }

        return true;
    }
}
