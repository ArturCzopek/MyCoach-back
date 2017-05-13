package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.exception.DuplicatedNameException;
import pl.arturczopek.mycoach.exception.InvalidDateException;
import pl.arturczopek.mycoach.exception.InvalidFlowException;
import pl.arturczopek.mycoach.exception.InvalidPropsException;
import pl.arturczopek.mycoach.model.add.NewCycle;
import pl.arturczopek.mycoach.model.add.NewSet;
import pl.arturczopek.mycoach.model.database.*;
import pl.arturczopek.mycoach.model.preview.CyclePreview;
import pl.arturczopek.mycoach.repository.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private DictionaryService dictionaryService;

    @Autowired
    public CycleService(CycleRepository cycleRepository, DateService dateService, ExerciseSessionRepository exerciseSessionRepository,
                        ExerciseRepository exerciseRepository, TrainingRepository trainingRepository, SeriesRepository seriesRepository,
                        SetRepository setRepository, DictionaryService dictionaryService) {
        this.cycleRepository = cycleRepository;
        this.dateService = dateService;
        this.exerciseSessionRepository = exerciseSessionRepository;
        this.exerciseRepository = exerciseRepository;
        this.trainingRepository = trainingRepository;
        this.seriesRepository = seriesRepository;
        this.setRepository = setRepository;
        this.dictionaryService = dictionaryService;
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
        Cycle cycle = cycleRepository.findOne(id);
        for (Set set : cycle.getSets()) {
            List<Training> nonSortedTrainings = set.getTrainings();
            List<Training> sortedTrainings = new ArrayList<>(nonSortedTrainings);

            sortedTrainings.sort((t1, t2) -> trainingsCompare(t1, t2));

            set.setTrainings(sortedTrainings);

            for (int i = 0; i < set.getExercises().size(); i++) {
                List<ExerciseSession> nonSortedSessions = set.getExercises().get(i).getExerciseSessions();
                List<ExerciseSession> sortedSessions = new ArrayList<>(nonSortedSessions);

                // we want to sort it by training date
                // unfortunately, we don't have in session reference to training
                sortedSessions.sort((s1, s2) -> {
                    int s1Index = nonSortedSessions.indexOf(s1);
                    int s2Index = nonSortedSessions.indexOf(s2);
                    Training t1 = nonSortedTrainings.get(s1Index);
                    Training t2 = nonSortedTrainings.get(s2Index);
                    return trainingsCompare(t1, t2);
                });

                set.getExercises().get(i).setExerciseSessions(sortedSessions);
            }
        }

        return cycle;
    }

    public boolean hashUserEveryCycleFinished() {
        return cycleRepository.countByIsFinishedFalse() == 0;
    }

    @Transactional
    public void addCycle(NewCycle newCycle) throws InvalidPropsException {

        if (!isNewCycleDateValid(newCycle)) {
            throw new InvalidDateException(dictionaryService.translate("page.trainings.cycle.error.invalidDates.message").getValue());
        }

        if (!areNewSetsNamesValid(newCycle.getSets())) {
            throw new DuplicatedNameException(dictionaryService.translate("page.trainings.cycle.error.invalidSetNames.message").getValue());
        }

        if (!canCycleBeActive()) {
            throw new InvalidFlowException(dictionaryService.translate("page.trainings.cycle.error.cannotActivate.message").getValue());
        }

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

    public void updateCycle(Cycle cycle) throws InvalidPropsException {

        if (!isCycleToUpdateDateValid(cycle)) {
            throw new InvalidDateException(dictionaryService.translate("page.trainings.cycle.error.coveringDates.message").getValue());
        }
        
        if (!isCycleToCloseDateValid(cycle)) {
            throw new InvalidDateException(dictionaryService.translate("page.trainings.cycle.error.earlyEndDate.message").getValue());
        }

        // if we want to active cycle we need to make sure if there is any active cycle
        if (!cycle.isFinished() && !canCycleBeActive()) {
            throw new InvalidFlowException(dictionaryService.translate("page.trainings.cycle.error.cannotActivate.message").getValue());
        }

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

    private int trainingsCompare(Training t1, Training t2) {
        if (t1.getTrainingDate().toLocalDate().isAfter(t2.getTrainingDate().toLocalDate())) {
            return 1;
        }

        return -1;
    }

    private boolean isCycleToCloseDateValid(Cycle cycle) {
        List<Set> sets = setRepository.findAllByCycleId(cycle.getCycleId());

        return sets.stream()
                .map(set -> trainingRepository.findFirstBySetIdOrderByTrainingDateDesc(set.getSetId()))
                .noneMatch(lastTraining -> lastTraining.getTrainingDate().toLocalDate().isAfter(cycle.getEndDate().toLocalDate()));
    }

    private boolean isNewCycleDateValid(NewCycle newCycle) {
        Cycle cycleFromDb = cycleRepository.findFirstByOrderByEndDateDesc();

        if (cycleFromDb == null) {
            return true;
        }

        if (newCycle.getStartDate().toLocalDate().isBefore(cycleFromDb.getEndDate().toLocalDate())) {
            return false;
        }

        LocalDate cycleStartDate = newCycle.getStartDate().toLocalDate();
        LocalDate cycleFromDbEndDate = cycleFromDb.getEndDate().toLocalDate();

        if (cycleStartDate.equals(cycleFromDbEndDate)) {
            return false;
        }

        return true;
    }

    private boolean canCycleBeActive() {
        Cycle currentActiveCycle = getActiveCycle();

        if (currentActiveCycle != null) {
            return false;
        }

        return true;
    }

    private boolean isCycleToUpdateDateValid(Cycle updateCycle) {

        Cycle currentCycle = cycleRepository.findOne(updateCycle.getCycleId());

        Cycle currentPreviousCycle = cycleRepository.findFirstByEndDateBeforeOrderByEndDateDesc(currentCycle.getStartDate());
        Cycle currentNextCycle = null;

        if (currentCycle.getEndDate() != null) {
            currentNextCycle = cycleRepository.findFirstByStartDateAfterOrderByStartDate(currentCycle.getEndDate());
        }

        // it's first cycle, we can set dates as we want
        if (currentPreviousCycle == null && currentNextCycle == null) {
            return true;
        }

        // new next/previous cycles should be the same, otherwise we would have covering dates or swap cycles, but for now it
        // is not handled
        Cycle newPreviousCycle = cycleRepository.findFirstByEndDateBeforeOrderByEndDateDesc(updateCycle.getStartDate());

        if ((currentPreviousCycle == null && newPreviousCycle != null) ||
            (currentPreviousCycle != null && newPreviousCycle == null)
                ) {
            return false;
        }

        // both are not null and has different ids
        if (currentPreviousCycle != null && currentPreviousCycle.getCycleId() != newPreviousCycle.getCycleId()) {
            return false;
        }

        Cycle newNextCycle = null;

        if (updateCycle.getEndDate() != null) {
            newNextCycle = cycleRepository.findFirstByStartDateAfterOrderByStartDate(updateCycle.getEndDate());
        }

        if (currentNextCycle != null && newNextCycle != null && currentNextCycle.getCycleId() != newNextCycle.getCycleId()) {
            return false;
        }

        return true;
    }

    private boolean areNewSetsNamesValid(List<NewSet> sets) {
        for (int i = 0; i < sets.size(); i++) {
            for (int j = i + 1; j < sets.size(); j++) {
                if (sets.get(j).getSetName().trim().equalsIgnoreCase(sets.get(i).getSetName().trim())) {
                    return false;
                }
            }
        }

        return true;
    }
}
