package adeo.leroymerlin.cdp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    EventService service;

    @Mock
    EventRepository repository;

    Event event1;
    Event event2;
    Event event3;

    @BeforeEach
    public void setup() {
        service = new EventService(repository);

        Member m1 = new Member(1L, "TRAORE");
        Member m2 = new Member(2L, "KABA");
        Member m3 = new Member(3L, "KANDE");
        Member m4 = new Member(4L, "KOUYATE");
        Member m5 = new Member(5L, "CAMARA");

        Band b1 = new Band(1L, "Band1");
        b1.setMembers(Set.of(m1, m2));

        Band b2 = new Band(2L, "Band2");
        b2.setMembers(Set.of(m3));

        Band b3 = new Band(3L, "Band3");
        b3.setMembers(Set.of(m4, m5));

        // Init Event1
        event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setNbStars(3);
        event1.setBands(Set.of(b1));

        // Init Event2
        event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");
        event2.setNbStars(5);
        event2.setBands(Set.of(b2));

        event3 = new Event();
        event3.setId(3L);
        event3.setTitle("Event 3");
        event3.setNbStars(5);
        event3.setBands(Set.of(b3));
    }

    @Test
    void update_shouldHandleRuntimeException_whenItemNotFound() {
        Mockito.when(repository.findById(10L)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(RuntimeException.class, () -> service.update(10L, event1));
        assertEquals("Event with ID #10 not found.", exception.getMessage());
    }

    @Test
    void update_shouldHandleRuntimeException_whenItemToUpdateIsDifferentFromExistingOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(event1));

        Throwable exception = assertThrows(RuntimeException.class, () -> service.update(1L, event2));
        assertEquals("Event in DB is not the same as the one to modify", exception.getMessage());
    }

    @Test
    void update_shouldReturnEventToSendForUpdate_whenItemToUpdateIsUpdated() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(event1));

        event1.setTitle("My new event title");
        Mockito.when(repository.save(event1)).thenReturn(event1);

        Event eventUpdated = service.update(1L, event1);
        assertEquals(1L, eventUpdated.getId());
    }

    @Test
    void update_shouldReturnEventUpdatedWithNewData_whenItemToUpdateIsUpdated() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(event1));

        event1.setTitle("My new event title");
        Mockito.when(repository.save(event1)).thenReturn(event1);

        Event eventUpdated = service.update(1L, event1);

        Mockito.verify(repository).save(event1);
        assertNotEquals("Event 1", eventUpdated.getTitle());
    }

    @Test
    void delete_shouldDeleteItemFromDataBase_whenItemIsDeleted() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(event1)).thenReturn(Optional.empty());
        assertTrue(service.delete(1L));
    }

    @Test
    void getFilteredEvents_shouldReturnAllEventsThatMatchTheCriteria_whenCriteriaIsDefined() {
        List<Event> resultMustBe = List.of(event1, event2);
        Mockito.when(repository.findAllBy()).thenReturn(List.of(event1, event2, event3));

        List<Event> filterResult = service.getFilteredEvents("ka");
        assertTrue(resultMustBe.size() == filterResult.size()
                && resultMustBe.containsAll(filterResult)
                && filterResult.containsAll(resultMustBe)
        );
    }

    @Test
    void getFilteredEvents_shouldReturnEmptyList_whenCriteriaIsNull() {
        List<Event> filterResult = service.getFilteredEvents(null);
        assertTrue(filterResult.isEmpty());
    }

    @Test
    void getFilteredEvents_shouldReturnEmptyList_whenCriteriaIsEmpty() {
        List<Event> filterResult = service.getFilteredEvents("");
        assertTrue(filterResult.isEmpty());
    }

}
