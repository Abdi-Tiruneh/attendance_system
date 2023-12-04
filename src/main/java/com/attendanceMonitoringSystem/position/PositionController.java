package com.attendanceMonitoringSystem.position;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/positions")
@Tag(name = "Position API.")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public ResponseEntity<List<Position>> getAllPositions() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable Long id) {
        return ResponseEntity.ok(positionService.getPositionById(id));
    }

    @PostMapping
    public ResponseEntity<Position> createPosition(@RequestBody @Valid Position position) {
        Position createdPosition = positionService.createPosition(position);
        return new ResponseEntity<>(createdPosition, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(@PathVariable Long id, @RequestBody Position newPosition) {
        Position updatedPosition = positionService.updatePosition(id, newPosition);
        return ResponseEntity.ok(updatedPosition);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}


