package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookingSystemTest {

    @Mock
    TimeProvider timeProvider;

    @Mock
    RoomRepository roomRepository;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    BookingSystem bookingSystem;

    @Test
    void bookRoomShouldThrowExceptionIfStartTimeIsBeforeCurrentDate() {
        Mockito.when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> bookingSystem.bookRoom("1", LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));

        assertThat(exception).hasMessage("Kan inte boka tid i dåtid");
    }

    @Test
    void bookRoomShouldThrowExceptionIfEndTimeIsBeforeStartTime() {
        Mockito.when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> bookingSystem.bookRoom("1", LocalDateTime.now().plusDays(1), LocalDateTime.now()));

        assertThat(exception).hasMessage("Sluttid måste vara efter starttid");
    }

    @Test
    void bookRoomShouldThrowExceptionIfParmetersAreNull() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> bookingSystem.bookRoom(null, null, null));

        assertThat(exception).hasMessage("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    void bookRoomShouldThrowExceptionIfRoomIdIsDoesNotExist() {
        Mockito.when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());

        var exception = assertThrows(IllegalArgumentException.class,
                () -> bookingSystem.bookRoom("0", LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

        assertThat(exception).hasMessage("Rummet existerar inte");
    }

    @Test
    void bookRoomShouldReturnFalseIfBookingSameRoomOnSameDate() {
        Mockito.when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
        Mockito.when(roomRepository.findById("1")).thenReturn(Optional.of(new Room("1", "Room 1")));

        bookingSystem.bookRoom("1", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        var bookedRoom = bookingSystem.bookRoom("1", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        assertThat(bookedRoom).isFalse();
    }

    @Test
    void getAvailableRoomsShouldThrowExceptionIfParametersAreNull() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> bookingSystem.getAvailableRooms(null, null));

        assertThat(exception).hasMessage("Måste ange både start- och sluttid");
    }

    @Test
    void getAvailableRoomsShouldThrowExceptionIfEndTimeIsBeforeStartTime() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> bookingSystem.getAvailableRooms(LocalDateTime.now().plusDays(1), LocalDateTime.now()));

        assertThat(exception).hasMessage("Sluttid måste vara efter starttid");
    }

    @Test
    void getAvailableRoomsShouldReturnListOfAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        var room1 = new Room("1", "Room 1");
        var room2 = new Room("2", "Room 2");
        var room3 = new Room("3", "Room 3");
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);

        List<Room> expectedList = List.of(room1, room3);

        Mockito.when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now());
        Mockito.when(roomRepository.findById("2")).thenReturn(Optional.of(room2));
        bookingSystem.bookRoom("2", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Mockito.when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> availableRooms = bookingSystem.getAvailableRooms(LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        assertThat(availableRooms).isEqualTo(expectedList);
    }

    @Test
    void cancelBookingShouldThrowExceptionIfBookingIdIsNull() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> bookingSystem.cancelBooking(null));

        assertThat(exception).hasMessage("Boknings-id kan inte vara null");
    }

    @Test
    void cancelBookingShouldReturnFalseWhenBookingDoesNotExist() {
        var cancelled = bookingSystem.cancelBooking("2");

        assertThat(cancelled).isFalse();
    }

    @Test
    void cancelBookingShouldThrowExceptionIfBookingIsStartedOrFinished() {
        var room = new Room("1", "Room 1");
        var booking = new Booking("1", "1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        room.addBooking(booking);
        Mockito.when(roomRepository.findAll()).thenReturn(List.of(room));
        Mockito.when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now().plusHours(2));

        var exception = assertThrows(IllegalStateException.class,
                () -> bookingSystem.cancelBooking("1"));

        assertThat(exception).hasMessage("Kan inte avboka påbörjad eller avslutad bokning");
    }

    @Test
    void cancelBookingShouldReturnTrueWhenBookingIdExists() {
        var room = new Room("1", "Room 1");
        var booking = new Booking("1", "1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        room.addBooking(booking);
        Mockito.when(roomRepository.findAll()).thenReturn(List.of(room));
        Mockito.when(timeProvider.getCurrentTime()).thenReturn(LocalDateTime.now().minusHours(1));

        var cancelled = bookingSystem.cancelBooking("1");

        assertThat(cancelled).isTrue();
    }
}