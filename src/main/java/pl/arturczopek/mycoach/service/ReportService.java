package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.exception.InvalidDateException;
import pl.arturczopek.mycoach.exception.WrongPermissionException;
import pl.arturczopek.mycoach.model.database.Report;
import pl.arturczopek.mycoach.model.preview.ReportPreview;
import pl.arturczopek.mycoach.repository.ReportRepository;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Service
public class ReportService {

    private ReportRepository reportRepository;
    private DateService dateService;
    private DictionaryService dictionaryService;

    @Autowired
    public ReportService(ReportRepository reportRepository, DateService dateService, DictionaryService dictionaryService) {
        this.reportRepository = reportRepository;
        this.dateService = dateService;
        this.dictionaryService = dictionaryService;
    }

    @Cacheable(value = "reportPreviews", key = "#userId")
    public List<ReportPreview> getReportPreviews(long userId) {
        List<Report> reports = reportRepository.findAllByUserIdOrderByEndDate(userId);

        return reports
                .stream().map(ReportPreview::buildFromReport)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Cacheable(value = "report", key = "#reportId")
    public Report getReportById(long reportId, long userId) throws WrongPermissionException {
        Report report = reportRepository.findOne(reportId);

        if (report.getUserId() != userId) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        return report;
    }

    @CacheEvict(value = "reportPreviews", key = "#userId")
    public void addReport(Report report, long userId) throws InvalidDateException {

        if (!areNewReportDatesValid(report.getStartDate(), report.getEndDate(), userId)) {
            throw new InvalidDateException(dictionaryService.translate("page.reports.error.invalidDate.message", userId).getValue());
        }

        Report reportToAdd = new Report();
        reportToAdd.setContent(report.getContent());
        reportToAdd.setStartDate(report.getStartDate());

        if (report.getEndDate() != null) {
            reportToAdd.setEndDate(report.getEndDate());
        } else {
            reportToAdd.setEndDate(dateService.getCurrentDate());
        }

        reportToAdd.setUserId(userId);
        reportRepository.save(reportToAdd);
    }

    @Caching(evict = {
            @CacheEvict(value = "reportPreviews", key = "#userId"),
            @CacheEvict(value = "report", key = "#report.reportId")
    })
    public void updateReport(Report report, long userId) throws InvalidDateException, WrongPermissionException {

        if (!areUpdateReportDatesValid(report.getStartDate(), report.getEndDate(), report.getReportId(), userId)) {
            throw new InvalidDateException(dictionaryService.translate("page.reports.error.invalidDate.message", userId).getValue());
        }

        reportRepository.save(report);
    }

    @Caching(evict = {
            @CacheEvict(value = "reportPreviews", key = "#userId"),
            @CacheEvict(value = "report", key = "#report.reportId")
    })
    public void deleteReport(Report report, long userId) throws WrongPermissionException {
        Report reportFromDb = this.getReportById(report.getReportId(), userId);
        reportRepository.delete(reportFromDb.getReportId());
    }

    private boolean areUpdateReportDatesValid(Date startDate, Date endDate, long reportId, long userId) throws WrongPermissionException {

        Report reportFromDb = reportRepository.findOne(reportId);

        if (reportFromDb.getUserId() != userId) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        // nothing has changed, everything is ok
        if (reportFromDb.getStartDate().toLocalDate().equals(startDate.toLocalDate())
                && reportFromDb.getEndDate().toLocalDate().equals(endDate.toLocalDate())) {
            return true;
        }

        Report nextReport = reportRepository.findFirstByStartDateAfterAndReportIdNotOrderByStartDate(startDate, reportId);

        // collision
        if (nextReport != null && !nextReport.getStartDate().toLocalDate().isAfter(endDate.toLocalDate())) {
            return false;
        }

        Report previousReport = reportRepository.findFirstByEndDateBeforeAndReportIdNotOrderByEndDateDesc(endDate, reportId);

        // collision
        if (previousReport != null && !previousReport.getEndDate().toLocalDate().isBefore(startDate.toLocalDate())) {
            return false;
        }

        return true;
    }

    private boolean areNewReportDatesValid(Date startDate, Date endDate, long userId) {
        List<Report> reports = reportRepository.findAllByUserId(userId);

        for (Report reportFromDb : reports) {

            Date correctEndDate = endDate;

            // if new report doesn't have date, it means that it is a new report
            if (correctEndDate == null) {
                correctEndDate = dateService.getCurrentDate();
            }

            // collision
            if (!reportFromDb.getEndDate().toLocalDate().isBefore(startDate.toLocalDate()) &&
                    !reportFromDb.getStartDate().toLocalDate().isAfter(correctEndDate.toLocalDate())) {
                return false;
            }
        }

        return true;
    }
}
