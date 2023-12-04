package com.attendanceMonitoringSystem.position;

import com.attendanceMonitoringSystem.exceptions.customExceptions.ForbiddenException;
import com.attendanceMonitoringSystem.exceptions.customExceptions.ResourceNotFoundException;
import com.attendanceMonitoringSystem.userManager.user.Users;
import com.attendanceMonitoringSystem.utils.CurrentlyLoggedInUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final CurrentlyLoggedInUser inUser;

    @Override
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @Override
    public Position getPositionById(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found"));
    }

    @Override
    public Position createPosition(Position position) {
        Users loggedInUser = inUser.getUser();
        if (!loggedInUser.getRole().getRoleName().equalsIgnoreCase("ADMIN"))
            throw new ForbiddenException("Access Denied: Only administrators are authorized to create Positions.");

        return positionRepository.save(position);
    }

    @Override
    public Position updatePosition(Long id, Position newPosition) {
        Position position = getPositionById(id);
        if (newPosition.getName() != null)
            position.setName(newPosition.getName());

        return positionRepository.save(position);
    }

    @Override
    public void deletePosition(Long id) {
        getPositionById(id);
        positionRepository.deleteById(id);
    }
}
