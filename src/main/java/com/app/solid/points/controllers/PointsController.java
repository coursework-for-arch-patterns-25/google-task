package com.app.solid.points.controllers;

import com.app.solid.points.DTO.PointsPatchDTO;
import com.app.solid.points.models.Points;
import com.app.solid.points.repositories.PointsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/points")
public class PointsController {
    private final PointsRepository repository;

    private final HashMap<String, Integer> pointsCache = new HashMap<>();

    private String allPointsCache = "";

    PointsController(PointsRepository repository){
        this.repository = repository;
    }

    @GetMapping("/")
    ResponseEntity<?> getAll(@RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch){
        if (allPointsCache.equals(ifNoneMatch)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        List<Points> points = repository.findAll();

        allPointsCache = String.valueOf(points.hashCode());

        return ResponseEntity.status(HttpStatus.OK).eTag(allPointsCache).body(points);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getPointsByParticipantId(@RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch, @PathVariable String id){
        if (pointsCache.containsKey(ifNoneMatch)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        Optional<Points> pointsRecord =  repository.findByParticipantId(id);

        if (pointsRecord.isPresent()){
            var existingPoints = pointsRecord.get();
            int hashedRecord = existingPoints.hashCode();
            pointsCache.put(String.valueOf(hashedRecord), 1);
            return ResponseEntity.status(HttpStatus.OK).eTag(String.valueOf(hashedRecord)).body(pointsRecord.get());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PutMapping("/{id}")
    ResponseEntity<?> modifyPointsById(@RequestHeader(value = "If-Match", required = false) String ifMatch, @PathVariable String id, @RequestBody Points points){
        var pointsRecord = repository.findById(id);

        if (pointsRecord.isPresent()){
            var existingPoints = pointsRecord.get();

            int hashedRecord = existingPoints.hashCode();

            if (String.valueOf(hashedRecord).equals(ifMatch)){
                pointsCache.remove(String.valueOf(hashedRecord));
                allPointsCache = "";

                existingPoints.setScore(points.getScore());
                existingPoints.setParticipantId(points.getParticipantId());
                repository.save(existingPoints);
                return ResponseEntity.ok("Points modified successfully!");
            }

            else{
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
            }

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found!");
    }

    @PatchMapping("/{id}")
    ResponseEntity<String> modifyPointsByParticipantId(@PathVariable String id, @RequestBody PointsPatchDTO patch){

        var pointsRecord = repository.findByParticipantId(id);

        if (pointsRecord.isPresent()){
            var existingPoints = pointsRecord.get();
            int score = existingPoints.getScore();

            int hashedRecord = existingPoints.hashCode();
            pointsCache.remove(String.valueOf(hashedRecord));
            allPointsCache = "";

            existingPoints.setScore(score + patch.getScore());
            repository.save(existingPoints);
            return ResponseEntity.ok("Points modified successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found!");
    }
}
