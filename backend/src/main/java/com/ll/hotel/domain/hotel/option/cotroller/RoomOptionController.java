package com.ll.hotel.domain.hotel.option.cotroller;

import com.ll.hotel.domain.hotel.option.dto.request.OptionRequest;
import com.ll.hotel.domain.hotel.option.dto.response.OptionResponse;
import com.ll.hotel.domain.hotel.option.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.service.RoomOptionService;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/room-options")
@RequiredArgsConstructor
public class RoomOptionController {
    private final RoomOptionService roomOptionService;

    @PostMapping
    public RsData<OptionResponse> add(@RequestBody @Valid OptionRequest optionRequest) {

        RoomOption roomOption = roomOptionService.add(optionRequest);

        return new RsData<>(
                "201",
                "항목이 추가되었습니다.",
                OptionResponse.from(roomOption)
        );
    }

    @GetMapping
    public RsData<List<OptionResponse>> getAll() {

        List<OptionResponse> roomAmenityList = roomOptionService.findAll()
                .stream()
                .map(OptionResponse::from).toList();

        return new RsData<>(
                "200",
                "모든 항목이 조회되었습니다.",
                roomAmenityList
        );
    }

    @GetMapping("/{id}")
    public RsData<OptionResponse> getById(@PathVariable("id") Long id) {

        RoomOption roomOption = roomOptionService.findById(id);

        return new RsData<>(
                "200",
                "항목이 조회되었습니다.",
                OptionResponse.from(roomOption)
        );
    }

    @PutMapping("/{id}")
    public RsData<OptionResponse> modify(@PathVariable("id") Long id,
                                        @RequestBody OptionRequest optionRequest) {
        RoomOption roomOption = roomOptionService.findById(id);
        roomOptionService.modify(roomOption, optionRequest);

        roomOptionService.flush();

        return new RsData<>(
                "200",
                "항목이 수정되었습니다.",
                OptionResponse.from(roomOption)
        );
    }

    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable("id") Long id) {
        RoomOption roomOption = roomOptionService.findById(id);
        roomOptionService.delete(roomOption);
        return new RsData<>(
                "200",
                "항목이 삭제되었습니다."
        );
    }
}
