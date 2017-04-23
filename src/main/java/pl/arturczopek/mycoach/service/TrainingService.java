package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.add.NewSeries;
import pl.arturczopek.mycoach.model.add.NewTraining;
import pl.arturczopek.mycoach.model.database.*;
import pl.arturczopek.mycoach.model.request.dto.ExerciseForTrainingPreview;
import pl.arturczopek.mycoach.repository.*;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Service
public class TrainingService {

    private ExerciseSessionRepository exerciseSessionRepository;
    private ExerciseRepository exerciseRepository;
    private SeriesRepository seriesRepository;
    private SetRepository setRepository;
    private TrainingRepository trainingRepository;

    @Autowired
    public TrainingService(ExerciseSessionRepository exerciseSessionRepository, ExerciseRepository exerciseRepository, SeriesRepository seriesRepository, SetRepository setRepository, TrainingRepository trainingRepository) {
        this.exerciseSessionRepository = exerciseSessionRepository;
        this.exerciseRepository = exerciseRepository;
        this.seriesRepository = seriesRepository;
        this.setRepository = setRepository;
        this.trainingRepository = trainingRepository;
    }

    @Transactional
    public void addTraining(NewTraining newTraining) {
        Training training = new Training();
        training.setSetId(newTraining.getSetId());
        training.setTrainingDate(newTraining.getTrainingDate());

        trainingRepository.save(training);

        Set set = setRepository.findOne(newTraining.getSetId());

        for (int i = 0; i < newTraining.getExerciseSessions().size(); i++) {
            List<ExerciseSession> sessions = new LinkedList<>();
            ExerciseSession session = new ExerciseSession();
            session.setExerciseId(set.getExercises().get(i).getExerciseId());
            session.setEmpty(newTraining.getExerciseSessions().get(i).getSeries().isEmpty());

            exerciseSessionRepository.save(session);

            List<Series> seriesList = new LinkedList<>();

            List<NewSeries> newSeries = newTraining.getExerciseSessions().get(i).getSeries();

            for (int j = 0; j < newSeries.size(); j++) {

                Series series = new Series();
                series.setExerciseSessionId(session.getExerciseSessionId());
                series.setComment(newSeries.get(j).getComment());
                series.setRepeats(newSeries.get(j).getRepeats());
                series.setWeight(newSeries.get(j).getWeight());
                seriesList.add(series);
            }

            session.setSeries(seriesList);
            sessions.add(session);

            set.getExercises().get(i).getExerciseSessions().addAll(sessions);
        }

        setRepository.save(set);
    }

    @Transactional
    public List<ExerciseForTrainingPreview> findExercisesByTrainingId(long trainingId) {
        Training training = trainingRepository.findOne(trainingId);
        Set set = setRepository.findOneByTrainingsContains(training);

        List<ExerciseForTrainingPreview> exercisesWithSessionsForTraining = new LinkedList<>();

        for (int i = 0; i < set.getTrainings().size(); i++) {
            if (set.getTrainings().get(i).getTrainingId() == trainingId) {
                final int trainingIndex = i;
                set.getExercises().forEach((Exercise exercise) -> {
                    ExerciseForTrainingPreview exerciseForPreview = new ExerciseForTrainingPreview();

                    exerciseForPreview.setExerciseId(exercise.getExerciseId());
                    exerciseForPreview.setExerciseName(exercise.getExerciseName());
                    exerciseForPreview.setExerciseDescription(exercise.getExerciseDescription());
                    exerciseForPreview.setExerciseSessions(Collections.singletonList(exercise.getExerciseSessions().get(trainingIndex)));

                    exercisesWithSessionsForTraining.add(exerciseForPreview);
                });

                break;
            }
        }

        return exercisesWithSessionsForTraining;
    }

    public void deleteTraining(Training training) {

        Set set = setRepository.findOneByTrainingsContains(training);

        for (int i = 0; i < set.getTrainings().size(); i++) {
            if (set.getTrainings().get(i).getTrainingId() == training.getTrainingId()) {
                final int trainingIndex = i;

                set.getExercises().forEach((Exercise exercise) -> {

                    ExerciseSession session = exercise.getExerciseSessions().get(trainingIndex);
                    session.getSeries().forEach((Series series) -> seriesRepository.delete(series.getSeriesId()));
                    exerciseSessionRepository.delete(session.getExerciseSessionId());

                });
            }
        }

        trainingRepository.delete(training.getTrainingId());
    }

    public void updateTraining(Training training, List<Exercise> exercises) {
        Training trainingToUpdate = trainingRepository.findOne(training.getTrainingId());
        trainingToUpdate.setTrainingDate(training.getTrainingDate());
        trainingRepository.save(training);

        Set set = setRepository.findOneByTrainingsContains(training);

        int trainingIndex = findTrainingIndex(training, set);

        final int finalTrainingIndex = trainingIndex;

        for (Exercise exercise : exercises) {
            Exercise exerciseToUpdate = exerciseRepository.findOne(exercise.getExerciseId());
            ExerciseSession session = exercise.getExerciseSessions().get(0);

            java.util.Set<Long> idsForLeftSeries = new HashSet<>();

            ExerciseSession sessionToUpdate = exerciseToUpdate.getExerciseSessions().get(finalTrainingIndex);
            sessionToUpdate.setEmpty(exercise.getExerciseSessions().get(0).isEmpty());

            if (!sessionToUpdate.isEmpty()) {
                updateSeries(session, idsForLeftSeries, sessionToUpdate);
            }

            sessionToUpdate.getSeries().stream()
                    .filter(series -> !idsForLeftSeries.contains(series.getSeriesId()))
                    .forEach(series -> seriesRepository.delete(series.getSeriesId()));

            sessionToUpdate.setSeries(
                    sessionToUpdate.getSeries().stream()
                            .filter(series -> idsForLeftSeries.contains(series.getSeriesId()))
                            .collect(Collectors.toList())
            );


            exerciseRepository.save(exerciseToUpdate);
        }

    }

    private void updateSeries(ExerciseSession session, java.util.Set<Long> idsForLeftSeries, ExerciseSession sessionToUpdate) {
        for (int i = 0; i < session.getSeries().size(); i++) {
            Series series;

            if (session.getSeries().get(i).getSeriesId() > 0) {
                series = sessionToUpdate.getSeries().get(i);
            } else {
                series = new Series();
            }

            series.setWeight(session.getSeries().get(i).getWeight());
            series.setRepeats(session.getSeries().get(i).getRepeats());
            series.setComment(session.getSeries().get(i).getComment());
            series.setExerciseSessionId(sessionToUpdate.getExerciseSessionId());

            if (series.getSeriesId() == 0) {
                sessionToUpdate.getSeries().add(series);
            }

            seriesRepository.save(series);
            idsForLeftSeries.add(series.getSeriesId());
        }
    }

    private int findTrainingIndex(Training training, Set set) {
        int trainingIndex = 0;

        for (int i = 0; i < set.getTrainings().size(); i++) {
            if (set.getTrainings().get(i).getTrainingId() == training.getTrainingId()) {
                trainingIndex = i;
                break;
            }
        }
        return trainingIndex;
    }
}
