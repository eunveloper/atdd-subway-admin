package nextstep.subway.ui;

import nextstep.subway.application.SectionService;
import nextstep.subway.application.StationService;
import nextstep.subway.dto.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class SectionController {

    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity createSection(@RequestBody SectionRequest sectionRequest,
                                        @PathVariable Long id) {
        LineResponse line = sectionService.saveSection(sectionRequest, id);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

}