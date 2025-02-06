package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.validator.constraints.Length;

public record PutRoomRequest(

        @Length(min = 2, max = 30) String roomName,

        @Min(value = 0) Integer roomNumber,

        Integer basePrice,

        @Min(value = 1) Integer standardNumber,

        @Min(value = 1) Integer maxNumber,

        BedTypeNumber bedTypeNumber,

        String roomStatus,

        List<String> deleteImageUrls,

        List<String> imageExtensions,

        Set<String> roomOptions
) {
    public PutRoomRequest {
        deleteImageUrls = deleteImageUrls == null ? new ArrayList<>() : deleteImageUrls;
        imageExtensions = imageExtensions == null ? new ArrayList<>() : imageExtensions;
        roomOptions = roomOptions == null ? new HashSet<>() : roomOptions;
    }
}
