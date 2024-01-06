package ru.mrsinkaaa.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import ru.mrsinkaaa.dto.LocationDTO;
import ru.mrsinkaaa.entity.Location;
import ru.mrsinkaaa.exceptions.location.LocationNotFoundException;
import ru.mrsinkaaa.repository.LocationRepository;

import java.util.List;
import java.util.Optional;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationService {

    private static final LocationService INSTANCE = new LocationService();
    private final LocationRepository locationRepository = LocationRepository.getInstance();
    private final ModelMapper mapper = new ModelMapper();

    public LocationDTO findById(Integer id) {
        return locationRepository.findById(id)
                .map(this::toLocationDTO)
                .orElseThrow(LocationNotFoundException::new);
    }

    public List<LocationDTO> findByUserId(Integer id) {
        return locationRepository.findByUserId(id).stream()
                .map(this::toLocationDTO)
                .toList();
    }

    public List<LocationDTO> findByLocationName(String name) {
        return locationRepository.findByLocationName(name)
                .stream()
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
        List<Location> locations = locationRepository.findByLocationName(locationDTO.getName());

        Optional<Location> savedLocation = locations.stream()
                .filter(location -> locationDTO.getUserId() == location.getUserId())
                .findFirst();

        if(savedLocation.isPresent()) {
            log.error("Location for user {} is already exists {}", savedLocation.get().getUserId(), savedLocation.get().getId());
        } else {
            Location location = toLocation(locationDTO);
            locationRepository.save(location);
            log.info("Location for user {} is saved {}", location.getUserId(), location.getId());
        }
    }

    public void delete(LocationDTO locationDTO) {
        Location location = toLocation(locationDTO);

        log.info("Location for user {} is deleted {}", location.getUserId(), location.getId());
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
