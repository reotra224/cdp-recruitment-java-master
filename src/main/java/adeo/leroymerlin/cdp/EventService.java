package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    public void update(Long id, Event event) {
        Optional<Event> eventInDB = eventRepository.findById(id);
        if (eventInDB.isEmpty()) {
            throw new RuntimeException("Event with ID #" + id + " not found.");
        }
        if (!eventInDB.get().getId().equals(event.getId())) {
            throw new RuntimeException("Event in DB is not the same as the one to modify");
        }
        eventRepository.save(event);
    }

    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here

        return events;
    }
}
