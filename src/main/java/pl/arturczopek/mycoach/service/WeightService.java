package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.exception.InvalidDateException;
import pl.arturczopek.mycoach.exception.WrongPermissionException;
import pl.arturczopek.mycoach.model.add.NewWeight;
import pl.arturczopek.mycoach.model.database.Weight;
import pl.arturczopek.mycoach.model.preview.WeightsPreview;
import pl.arturczopek.mycoach.repository.WeightRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Cacheable(value = "weightPreviews", key = "#userId")
    public List<WeightsPreview> getWeightPreviews(long userId) {
        List<Weight> weights = weightRepository.findAllByUserIdOrderByMeasurementDateDesc(userId);

        Set<WeightsPreview> dates = new HashSet<>();

        Calendar cal = Calendar.getInstance();

        weights.forEach((Weight weight) -> {
            cal.setTime(weight.getMeasurementDate());

            WeightsPreview preview = new WeightsPreview();
            preview.setMonth(cal.get(Calendar.MONTH) + 1);
            preview.setYear(cal.get(Calendar.YEAR));
            dates.add(preview);
        });


        return dates.stream().sorted((p1, p2) -> {
            if (p1.getYear() == p2.getYear()) {
                return p1.getMonth() - p2.getMonth();
            } else {
                return p1.getYear() - p2.getYear();
            }
        }).collect(Collectors.toList());
    }

    @Cacheable(value = "weightGrouped", key = "#userId + ' ' + #year + ' ' + #month")
    public List<Weight> getWeightsByYearAndMonth(int year, int month, long userId) {
        LocalDate startDate = dateService.buildFirstMonthDay(year, month);
        LocalDate endDate = dateService.getLastDayOfTheMonth(startDate);

        return weightRepository.findByUserIdAndMeasurementDateBetweenOrderByMeasurementDate(userId, Date.valueOf(startDate), Date.valueOf(endDate));
    }

    @CacheEvict(value = {"weightPreviews", "weightGrouped"}, allEntries = true)
    public void addWeight(NewWeight weightToAdd, long userId) throws InvalidDateException {

        if (!isNewMeasurementDateValid(weightToAdd.getMeasurementDate())) {
            throw new InvalidDateException(dictionaryService.translate("page.weights.error.invalidDate.message", userId).getValue());
        }

        Weight weight = new Weight();
        weight.setValue(weightToAdd.getValue());

        if (weightToAdd.getMeasurementDate() != null) {
            weight.setMeasurementDate(weightToAdd.getMeasurementDate());
        } else {
            weight.setMeasurementDate(dateService.getCurrentDate());
        }

        weight.setUserId(userId);
        weightRepository.save(weight);
    }

    @CacheEvict(value = {"weightPreviews", "weightGrouped"}, allEntries = true)
    public void deleteWeights(List<Weight> weights, long userId) throws WrongPermissionException {
        for (Weight weight : weights) {
            Long weightId = weight.getWeightId();
            Weight weightFromDb = weightRepository.findOne(weightId);

            if (weightFromDb.getUserId() != userId) {
                throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
            }

            weightRepository.delete(weightFromDb.getWeightId());
        }
    }

    @CacheEvict(value = {"weightPreviews", "weightGrouped"}, allEntries = true)
    public void updateWeights(List<Weight> weights, long userId) throws InvalidDateException, WrongPermissionException {
        for (Weight weight : weights) {
            if (!isUpdateMeasurementDateValid(weight.getMeasurementDate(), weight.getWeightId())) {
                throw new InvalidDateException(dictionaryService.translate("page.weights.error.invalidDate.message", userId).getValue());
            }
        }

        for (Weight weight : weights) {
            Weight weightFromDb = weightRepository.findOne(weight.getWeightId());

            if (weightFromDb.getUserId() != userId) {
                throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
            }

            weightFromDb.setMeasurementDate(weight.getMeasurementDate());
            weightFromDb.setValue(weight.getValue());

            weightRepository.save(weightFromDb);
        }
    }

    private boolean isUpdateMeasurementDateValid(Date measurementDate, long weightId) {
        Weight duplicatedWeight = weightRepository.findOneByMeasurementDateAndWeightIdNot(measurementDate, weightId);

        return duplicatedWeight == null;
    }

    private boolean isNewMeasurementDateValid(Date measurementDate) {
        Weight duplicatedWeight = weightRepository.findOneByMeasurementDate(measurementDate);

        return duplicatedWeight == null;
    }
}
