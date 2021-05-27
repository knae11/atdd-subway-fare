package wooteco.subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.deletion.StationCannotDeleteException;
import wooteco.subway.exception.duplication.StationNameDuplicatedException;
import wooteco.subway.exception.notfound.StationNotFoundException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        validateDuplicatedName(stationRequest.getName());
        Station station = stationDao.insert(stationRequest.toStation());
        return StationResponse.of(station);
    }

    private void validateDuplicatedName(String name) {
        stationDao.findByName(name)
                .ifPresent(station -> { throw new StationNameDuplicatedException(); });
    }

    public Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(StationNotFoundException::new);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return StationResponse.listOf(stations);
    }

    @Transactional
    public void deleteStationById(Long id) {
        findStationById(id);
        validateDeletableStatus(id);
        stationDao.deleteById(id);
    }

    private void validateDeletableStatus(Long id) {
        if (stationDao.countRegisteredStations(id) > 0) {
            throw new StationCannotDeleteException();
        }
    }

    @Transactional
    public void updateStationById(Long id, StationRequest stationRequest) {
        findStationById(id);
        validateDuplicatedName(stationRequest.getName());
        stationDao.update(stationRequest.getName(), id);
    }
}
