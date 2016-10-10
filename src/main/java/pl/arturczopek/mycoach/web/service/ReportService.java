package pl.arturczopek.mycoach.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.database.entity.Report;
import pl.arturczopek.mycoach.database.entity.dto.ReportPreview;
import pl.arturczopek.mycoach.database.repository.ReportRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author arturczopek
 * @Date 10/10/16
 */
@Service
public class ReportService {

    private ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<ReportPreview> getReportPreviews() {
        List<Report> reports = reportRepository.findAll();
        List<ReportPreview> previews = reports
                .stream().map(ReportPreview::buildFromReport)
                .collect(Collectors.toCollection(LinkedList::new));

        return previews;
    }

    public Report getReportById(long id) {
        return reportRepository.findOne(id);
    }
}
