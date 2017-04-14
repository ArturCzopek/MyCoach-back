package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    public WeightService(WeightRepository weightRepository, DateService dateService) {
        this.weightRepository = weightRepository;
        this.dateService = dateService;
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

    public void addWeight(NewWeight weightToAdd) {
        Weight weight = new Weight();
        weight.setValue(weightToAdd.getValue());

        if (weightToAdd.getMeasurementDate() != null) {
            weight.setMeasurementDate(weightToAdd.getMeasurementDate());
        } else {
            weight.setMeasurementDate(dateService.getCurrentDate());
        }

        weightRepository.save(weight);
    }

    public void updateWeights(List<Weight> weights) {
        weights.forEach(weight -> weightRepository.save(weight));
    }

    public void deleteWeights(List<Weight> weights) {
        weights.forEach(weight -> weightRepository.delete(weight.getWeightId()));
    }
}
