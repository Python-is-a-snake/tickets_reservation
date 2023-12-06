package com.trs.tickets.service;

import com.trs.tickets.model.HallType;
import com.trs.tickets.model.PlaceType;
import com.trs.tickets.model.dto.PlaceDto;
import com.trs.tickets.model.entity.Place;
import com.trs.tickets.model.entity.Session;
import com.trs.tickets.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Place getPlaceById(Long id) {
        return placeRepository.findById(id).orElse(null);
    }

    public Place addPlace(Place place) {
        return placeRepository.save(place);
    }

    public Place updatePlace(Place place) {
        return placeRepository.save(place);
    }

    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }

    public List<Place> initPlaces(Session session, HallType hallType) {
        List<Place> places = new ArrayList<>();
        int maxRow;
        int maxNum;

        switch (hallType){
            case HALL_2D -> {
                maxRow = 8;
                maxNum = 12;
            }
            case HALL_3D -> {
                maxRow = 10;
                maxNum = 15;
            }
            case HALL_IMAX -> {
                maxRow = 15;
                maxNum = 19;
            }
            default -> {
                maxRow = 1;
                maxNum = 1;
            }
        }


        for (int row = 1; row <= maxRow; row++) {
            for (int num = 1; num <= maxNum; num++) {
                PlaceType placeType = PlaceType.NORMAL;

                if (row <= 2 && (num <= 2 || num >= 7)) {
                    placeType = PlaceType.CHEAP;
                } else if (row >= 7 && (num >= 5 || num <= 8)) {
                    placeType = PlaceType.VIP;
                }

                places.add(new Place(session, row, num, placeType));
            }
        }

        placeRepository.saveAll(places);
        placeRepository.flush();

        return places;
    }

}
