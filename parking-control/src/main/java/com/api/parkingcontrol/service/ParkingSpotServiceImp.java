package com.api.parkingcontrol.service;

import com.api.parkingcontrol.model.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotServiceImp implements ParkinkSpotService {

    final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotServiceImp(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    //É interessante utilizar o transation em metodos de contrutivos e destrutivos  para garantir o rollback caso ocorra um erro na hora da tranzação
    @Override
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
        return parkingSpotRepository.save(parkingSpotModel);
    }

    public Page<ParkingSpotModel> findAll(Pageable pageable) {
        return parkingSpotRepository.findAll(pageable);
    }

    @Override
    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    @Override
    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    @Override
    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return existsByApartmentAndBlock(apartment, block);
    }

    @Override
    public Optional<ParkingSpotModel> findById(UUID id) {
        return parkingSpotRepository.findById(id);
    }

    @Transactional
    @Override
    public void deleteParkingSpot(ParkingSpotModel parkingSpotModel) {
        parkingSpotRepository.delete(parkingSpotModel);

    }

    @Transactional
    @Override
    public ParkingSpotModel updateParkingSpot(ParkingSpotModel parkingSpotModel) {
        return parkingSpotRepository.save(parkingSpotModel);
    }
}
