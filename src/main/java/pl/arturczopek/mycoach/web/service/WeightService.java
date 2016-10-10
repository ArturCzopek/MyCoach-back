package pl.arturczopek.mycoach.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.database.entity.Weight;
import pl.arturczopek.mycoach.database.entity.dto.WeightDatesPreview;
import pl.arturczopek.mycoach.database.repository.WeightRepository;

import java.sql.Timestamp;
import java.util.*;

/**
 * @Author arturczopek
 * @Date 10/10/16
 */
@Service

public class WeightService {

    private WeightRepository weightRepository;

    @Autowired
    public WeightService(WeightRepository weightRepository) {
        this.weightRepository = weightRepository;
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
        String startMonthAsString = (startMonth < 10) ? "0" + Integer.toString(startMonth) : Integer.toString(startMonth);

        Timestamp startDate = Timestamp.valueOf(new StringBuilder()
                .append(year).append("-")
                .append(startMonthAsString).append("-01 00:00:00")
                .toString());

        Timestamp endDate;

        if (startMonth < 12) {
            int endMonth = month + 1;
            String endMonthAsString = (endMonth < 10) ? "0" + Integer.toString(endMonth) : Integer.toString(endMonth);

            endDate = Timestamp.valueOf(new StringBuilder()
                    .append(year).append("-")
                    .append(endMonthAsString).append("-01 00:00:00")
                    .toString());
        } else {
            endDate = Timestamp.valueOf(new StringBuilder()
                    .append(year + 1).append("-")
                    .append("01").append("-01 00:00:00")
                    .toString());
        }

        List<Weight> weights = weightRepository.findByMeasurementDateAfterAndMeasurementDateBefore(startDate, endDate);

        return weights;
    }
}
