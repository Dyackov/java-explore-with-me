package ru.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@ToString
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointHit endpointHit = (EndpointHit) o;
        return id == endpointHit.id && Objects.equals(app, endpointHit.app) && Objects.equals(uri, endpointHit.uri) && Objects.equals(ip, endpointHit.ip) && Objects.equals(timestamp, endpointHit.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, uri, ip, timestamp);
    }
}