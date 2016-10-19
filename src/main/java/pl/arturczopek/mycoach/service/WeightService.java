package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.database.entity.Weight;
import pl.arturczopek.mycoach.dto.preview.WeightDatesPreview;
import pl.arturczopek.mycoach.database.repository.WeightRepository;

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

    public List<WeightDatesPreview> getWeightDatesList() {
        List<Weight> weights = weightRepository.findAllByOrderByMeasurementDateDesc();

        Set<WeightDatesPreview> dates = new HashSet<>();

        Calendar cal = Calendar.getInstance();

        for (Weight weight : weights) {
            cal.setTime(weight.getMeasurementDate());

            WeightDatesPreview preview = new WeightDatesPreview();
            preview.setMonth(cal.get(Calendar.MONTH) + 1);
            preview.setYear(cal.get(Calendar.YEAR));
            dates.add(preview);
        }

        return new LinkedList<>(dates);
    }

    public List<Weight> getWeightsByYearAndMonth(int year, int month) {

        int startMonth = month;
        Date startDate = dateService.buildFirstMonthDay(year, startMonth);

        int endMonth = startMonth + 1;
        Date endDate;

        if (startMonth < 12) {
            endDate = dateService.buildFirstMonthDay(year, endMonth);
        } else {
            endDate = dateService.buildFirstMonthDay(year, DateService.FIRST_MONTH);
        }

        return weightRepository.findByMeasurementDateAfterAndMeasurementDateBefore(startDate, endDate);
    }

    public void addWeight(Weight weightToAdd) {
        Weight weight = new Weight();
        weight.setWeight(weightToAdd.getWeight());

        if(weightToAdd.getMeasurementDate() != null) {
            weight.setMeasurementDate(weight.getMeasurementDate());
        } else {
            weight.setMeasurementDate(dateService.getCurrentDate());
        }

        weightRepository.save(weight);
    }
}
