package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.arturczopek.mycoach.database.entity.Report;
import pl.arturczopek.mycoach.database.entity.dto.ReportPreview;
import pl.arturczopek.mycoach.web.service.ReportService;

import java.util.List;

/**
 * @Author arturczopek
 * @Date 10/10/16
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

    @RequestMapping(value = "/previews", method = RequestMethod.GET)
    public List<ReportPreview> getPreviews() {
        return reportService.getReportPreviews();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Report getReportDetails(@PathVariable long id) {
        return reportService.getReportById(id);
    }
}
