package com.api.parkingcontrol.controller;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.model.ParkingSpotModel;
import com.api.parkingcontrol.service.ParkingSpotServiceImp;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotServiceImp parkingSpotServiceImp;

    public ParkingSpotController(ParkingSpotServiceImp parkingSpotServiceImp) {
        this.parkingSpotServiceImp = parkingSpotServiceImp;
    }
    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){
        if (parkingSpotServiceImp.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car already in use");
        }

        if (parkingSpotServiceImp.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already in use");
        }

        if (parkingSpotServiceImp.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already in registered");
        }

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotServiceImp.save(parkingSpotModel));

    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotServiceImp.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable (value = "id") UUID id){

        Optional<ParkingSpotModel> parkingSpotModelOpenOption = parkingSpotServiceImp.findById(id);

        if (!parkingSpotModelOpenOption.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOpenOption.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable (value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOpenOption = parkingSpotServiceImp.findById(id);
        if (!parkingSpotModelOpenOption.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
        }

        parkingSpotServiceImp.deleteParkingSpot(parkingSpotModelOpenOption.get());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOpenOption.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto){

        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotServiceImp.findById(id);

        if (!parkingSpotModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
        }

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistationDate(parkingSpotModelOptional.get().getRegistationDate());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotServiceImp.updateParkingSpot(parkingSpotModel));

    }

}
