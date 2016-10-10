package pl.arturczopek.mycoach.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.database.entity.Cycle;
import pl.arturczopek.mycoach.database.entity.dto.CyclePreview;
import pl.arturczopek.mycoach.database.repository.CycleRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author arturczopek
 * @Date 10/10/16
 */

@Service
public class CycleService {

    private CycleRepository cycleRepository;

    @Autowired
    public CycleService(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    public List<CyclePreview> getCyclePreviews() {
        List<Cycle> cycles = cycleRepository.findAllByOrderByEndDateDesc();

        List<CyclePreview> previews = cycles
                .stream().map(CyclePreview::buildFromCycle)
                .collect(Collectors.toCollection(LinkedList::new));

        return previews;
    }

    public Cycle getCycleById(long id) {
        return cycleRepository.findOne(id);
    }
}
