package com.attendanceMonitoringSystem.position;

import java.util.List;

public interface PositionService {
    List<Position> getAllPositions();

    Position getPositionById(Long id);

    Position createPosition(Position position);

    Position updatePosition(Long id, Position position);

    void deletePosition(Long id);
}

