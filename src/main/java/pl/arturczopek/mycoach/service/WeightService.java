package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.exception.InvalidDateException;
import pl.arturczopek.mycoach.model.add.NewWeight;
import pl.arturczopek.mycoach.model.database.Weight;
import pl.arturczopek.mycoach.model.preview.WeightsPreview;
import pl.arturczopek.mycoach.repository.WeightRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Service
public class WeightService {

    private WeightRepository weightRepository;
    private DateService dateService;
    private DictionaryService dictionaryService;

    @Autowired
    public WeightService(WeightRepository weightRepository, DateService dateService, DictionaryService dictionaryService) {
        this.weightRepository = weightRepository;
        this.dateService = dateService;
        this.dictionaryService = dictionaryService;
    }

    public List<WeightsPreview> getWeightPreviews() {
        List<Weight> weights = weightRepository.findAllByOrderByMeasurementDateDesc();

        Set<WeightsPreview> dates = new HashSet<>();

        Calendar cal = Calendar.getInstance();

        weights.forEach((Weight weight) -> {
            cal.setTime(weight.getMeasurementDate());

            WeightsPreview preview = new WeightsPreview();
            preview.setMonth(cal.get(Calendar.MONTH) + 1);
            preview.setYear(cal.get(Calendar.YEAR));
            dates.add(preview);
        });

        return new LinkedList<>(dates);
    }

    public List<Weight> getWeightsByYearAndMonth(int year, int month) {

        LocalDate startDate = dateService.buildFirstMonthDay(year, month);
        LocalDate endDate = dateService.getLastDayOfTheMonth(startDate);

        return weightRepository.findByMeasurementDateBetweenOrderByMeasurementDate(Date.valueOf(startDate), Date.valueOf(endDate));
    }

    public void addWeight(NewWeight weightToAdd) throws InvalidDateException {

        if (!isNewMeasurementDateValid(weightToAdd.getMeasurementDate())) {
            throw new InvalidDateException(dictionaryService.translate("page.weights.error.invalidDate.message").getValue());
        }

        Weight weight = new Weight();
        weight.setValue(weightToAdd.getValue());

        if (weightToAdd.getMeasurementDate() != null) {
            weight.setMeasurementDate(weightToAdd.getMeasurementDate());
        } else {
            weight.setMeasurementDate(dateService.getCurrentDate());
        }

        weightRepository.save(weight);
    }

    public void deleteWeights(List<Weight> weights) {
        weights.forEach((Weight weight) -> weightRepository.delete(weight.getWeightId()));
    }

    public void updateWeights(List<Weight> weights) throws InvalidDateException {
        for (Weight weight : weights) {
            if (!isUpdateMeasurementDateValid(weight.getMeasurementDate(), weight.getWeightId())) {
                throw new InvalidDateException(dictionaryService.translate("page.weights.error.invalidDate.message").getValue());
            }
        }

        for (Weight weight : weights) {
            weightRepository.save(weight);
        }
    }

    private boolean isUpdateMeasurementDateValid(Date measurementDate, long weightId) {
        Weight otherWeightInTheSameDay = weightRepository.findOneByMeasurementDateAndWeightIdNot(measurementDate, weightId);

        if (otherWeightInTheSameDay != null) {
            return false;
        }

        return true;
    }

    private boolean isNewMeasurementDateValid(Date measurementDate) {
        Weight otherWeightInTheSameDay = weightRepository.findOneByMeasurementDate(measurementDate);

        if (otherWeightInTheSameDay != null) {
            return false;
        }

        return true;
    }
}
