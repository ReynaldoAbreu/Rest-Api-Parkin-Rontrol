package com.api.parkingcontrol.service;

import com.api.parkingcontrol.model.ParkingSpotModel;

import java.util.Optional;
import java.util.UUID;

public interface ParkinkSpotService {

    ParkingSpotModel save(ParkingSpotModel parkingSpotModel);

    boolean existsByLicensePlateCar(String licensePlateCar);

    boolean existsByParkingSpotNumber(String parkingSpotNumber);

    boolean existsByApartmentAndBlock(String apartment, String block);

    public Optional<ParkingSpotModel> findById(UUID id);

    public void deleteParkingSpot(ParkingSpotModel parkingSpotModel);

    public ParkingSpotModel updateParkingSpot(ParkingSpotModel parkingSpotModel);


}
