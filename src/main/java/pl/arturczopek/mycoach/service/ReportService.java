package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.database.Report;
import pl.arturczopek.mycoach.model.preview.ReportPreview;
import pl.arturczopek.mycoach.repository.ReportRepository;

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

    @Autowired
    public ReportService(ReportRepository reportRepository, DateService dateService) {
        this.reportRepository = reportRepository;
        this.dateService = dateService;
    }

    public List<ReportPreview> getReportPreviews() {
        List<Report> reports = reportRepository.findAllByOrderByEndDateDesc();

        return reports
                .stream().map(ReportPreview::buildFromReport)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Report getReportById(long id) {
        return reportRepository.findOne(id);
    }

    public void addReport(Report report) {
        Report reportToAdd = new Report();
        reportToAdd.setContent(report.getContent());
        reportToAdd.setStartDate(report.getStartDate());

        if(report.getEndDate() != null) {
            reportToAdd.setEndDate(report.getEndDate());
        } else {
            reportToAdd.setEndDate(dateService.getCurrentDate());
        }

        reportRepository.save(reportToAdd);

    }
}
