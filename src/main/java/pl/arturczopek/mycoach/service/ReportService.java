package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.exception.InvalidDateException;
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

    public List<ReportPreview> getReportPreviews() {
        List<Report> reports = reportRepository.findAllByOrderByEndDate();

        return reports
                .stream().map(ReportPreview::buildFromReport)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Report getReportById(long id) {
        return reportRepository.findOne(id);
    }

    public void addReport(Report report) throws InvalidDateException {

        if (!areNewReportDatesValid(report.getStartDate(), report.getEndDate())) {
            throw new InvalidDateException(dictionaryService.translate("page.reports.error.invalidDate.message").getValue());
        }

        Report reportToAdd = new Report();
        reportToAdd.setContent(report.getContent());
        reportToAdd.setStartDate(report.getStartDate());

        if (report.getEndDate() != null) {
            reportToAdd.setEndDate(report.getEndDate());
        } else {
            reportToAdd.setEndDate(dateService.getCurrentDate());
        }

        reportRepository.save(reportToAdd);

    }

    public void updateReport(Report report) throws InvalidDateException {

        if (!areUpdateReportDatesValid(report.getStartDate(), report.getEndDate(), report.getReportId())) {
            throw new InvalidDateException(dictionaryService.translate("page.reports.error.invalidDate.message").getValue());
        }

        reportRepository.save(report);
    }

    public void deleteReport(Report report) {
        reportRepository.delete(report.getReportId());
    }

    private boolean areUpdateReportDatesValid(Date startDate, Date endDate, long reportId) {

        Report reportFromDb = reportRepository.findOne(reportId);

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

    private boolean areNewReportDatesValid(Date startDate, Date endDate) {
        List<Report> reports = reportRepository.findAll();

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
