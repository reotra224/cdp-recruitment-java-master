package adeo.leroymerlin.cdp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Transactional
    void deleteById(Long eventId);

    List<Event> findAllBy();
}
