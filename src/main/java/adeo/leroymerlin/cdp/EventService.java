package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        List<Event> result = new ArrayList<>();

        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here
        if (events == null || events.isEmpty()) {
            return result;
        }

        events.forEach(event -> {
            Set<Band> bands = event.getBands();
            if (bands != null && !bands.isEmpty()) {
                bands.forEach(band -> {
                    Set<Member> members = band.getMembers();
                    if (members != null && !members.isEmpty()) {
                        members.forEach(member -> {
                            if (member.getName().toLowerCase().contains(query.toLowerCase())) {
                                result.add(event);
                            }
                        });
                    }
                });
            }
        });
        // Pour éliminer les évènements duppliqués
        return result.stream().distinct().collect(Collectors.toList());
    }
}
