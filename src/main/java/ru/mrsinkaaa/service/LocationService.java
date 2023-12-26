package ru.mrsinkaaa.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import ru.mrsinkaaa.dto.LocationDTO;
import ru.mrsinkaaa.entity.Location;
import ru.mrsinkaaa.repository.LocationRepository;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationService {

    private static final LocationService INSTANCE = new LocationService();
    private final LocationRepository locationRepository = LocationRepository.getInstance();
    private final ModelMapper mapper = new ModelMapper();

    public LocationDTO findById(Integer id) {
        return locationRepository.findById(id)
                .map(this::toLocationDTO)
                .orElseThrow();
    }
    
    public List<LocationDTO> findByUserId(Integer id) {
        return locationRepository.findByUserId(id).stream()
                .map(this::toLocationDTO)
                .toList();
    }

    public Optional<LocationDTO> findByLocationIdAndUserId(Integer locationId, Integer userId) {
        return locationRepository.findByLocationIdAndUserId(locationId, userId)
               .map(this::toLocationDTO);
    }

    public List<LocationDTO> findAll() {
        return locationRepository.findAll().stream()
                .map(this::toLocationDTO)
                .toList();
    }

    public void save(LocationDTO locationDTO) {
        Location location = toLocation(locationDTO);
        locationRepository.save(location);
    }

    public void delete(LocationDTO locationDTO) {
        Location location = toLocation(locationDTO);
        locationRepository.delete(location);
    }

    private LocationDTO toLocationDTO(Location location) {
        return mapper.map(location, LocationDTO.class);
    }

    private Location toLocation(LocationDTO locationDTO) {
        return mapper.map(locationDTO, Location.class);
    }

    public static LocationService getInstance() {
        return INSTANCE;
    }
}
