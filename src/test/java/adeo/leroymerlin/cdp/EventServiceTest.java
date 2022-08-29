package adeo.leroymerlin.cdp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    EventService service;

    @Mock
    EventRepository repository;

    Event event1;
    Event event2;

    @BeforeEach
    public void setup() {
        service = new EventService(repository);

        // Init Event1
        event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setNbStars(3);

        // Init Event2
        event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");
        event2.setNbStars(5);
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
        assertNotEquals("Event 1", eventUpdated.getTitle());
    }
}
