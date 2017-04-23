package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.add.NewCycle;
import pl.arturczopek.mycoach.model.add.NewSet;
import pl.arturczopek.mycoach.model.database.*;
import pl.arturczopek.mycoach.model.preview.CyclePreview;
import pl.arturczopek.mycoach.repository.*;

import javax.transaction.Transactional;
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
    public void addCycle(NewCycle newCycle) {
        Cycle cycle = new Cycle();

        if (newCycle.getStartDate() != null) {
            cycle.setStartDate(newCycle.getStartDate());
        } else {
            cycle.setStartDate(dateService.getCurrentDate());
        }

        cycle.setEndDate(null);

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

        if (cycleToEdit.isFinished() && cycleToEdit.getEndDate() != null) {
            cycleToEdit.setEndDate(cycleToEdit.getEndDate());
        } else if (cycleToEdit.isFinished()) {
            cycleToEdit.setEndDate(dateService.getCurrentDate());
        } else {
            cycleToEdit.setEndDate(null);
        }

        cycleRepository.save(cycleToEdit);

    }
}
