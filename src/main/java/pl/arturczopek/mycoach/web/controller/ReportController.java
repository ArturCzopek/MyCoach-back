package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.exception.InvalidDateException;
import pl.arturczopek.mycoach.model.database.Report;
import pl.arturczopek.mycoach.model.preview.ReportPreview;
import pl.arturczopek.mycoach.service.ReportService;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/previews")
    public List<ReportPreview> getPreviews() {
        return reportService.getReportPreviews();
    }

    @GetMapping("/{id}")
    public Report getReportDetails(@PathVariable long id) {
        return reportService.getReportById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added report")
    public void addReport(@RequestBody Report report) throws InvalidDateException {
        reportService.addReport(report);
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated report")
    public void updateWeights(@RequestBody Report report) throws InvalidDateException {
        reportService.updateReport(report);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed report")
    public void deleteWeights(@RequestBody Report report) {
        reportService.deleteReport(report);
    }
}