package ru.practicum.event.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Location;

/**
 * Репозиторий для работы с сущностью локации.
 * Использует JPA для выполнения запросов.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * Проверяет наличие локации по широте и долготе.
     *
     * @param lat Широта локации.
     * @param lon Долгота локации.
     * @return true, если локация с такими координатами существует, иначе false.
     */
    @Query("""
            SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END
            FROM Location l
            WHERE l.lat = :lat AND l.lon = :lon
            """)
    boolean existsByLatAndLon(@Param("lat") Double lat, @Param("lon") Double lon);
}
