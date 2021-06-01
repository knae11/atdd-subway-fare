package wooteco.subway.line.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleLineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public SimpleLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static List<SimpleLineResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(line -> new SimpleLineResponse(line.getId(), line.getName(), line.getColor(), StationResponse.listOf(line.getStations())))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
