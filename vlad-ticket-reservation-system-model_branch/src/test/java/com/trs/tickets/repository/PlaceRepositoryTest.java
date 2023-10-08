//package com.trs.tickets.repository;
//
//import com.trs.tickets.model.HallType;
//import com.trs.tickets.model.PlaceType;
//import com.trs.tickets.model.entity.Hall;
//import com.trs.tickets.model.entity.Place;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Arrays;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class PlaceRepositoryTest {
//    @Autowired
//    private PlaceRepository placeRepository;
//
//    @Autowired
//    private HallRepository hallRepository;
//
//    private Place place;
//
//    @BeforeEach
//    void setUp() {
//        place = new Place();
//    }
//
//    @Test
//    public void savePlaceShouldWork(){
//        Hall hall = hallRepository.save(new Hall());
//        hall.setPlaces(Arrays.asList());
//        hall.setType(HallType.HALL_2D);
//        //hall.setCinema();         ??? We have to create lots of objects
//        //place.setHall();
//        place.setPlaceCode("1");
//        place.setPlaceType(PlaceType.VIP);
//
//        Place placeSaved = placeRepository.save(place);
//        assertNotNull(placeSaved.getId());
//
//    }
//
//
//}
