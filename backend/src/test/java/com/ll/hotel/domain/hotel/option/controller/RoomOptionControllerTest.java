package com.ll.hotel.domain.hotel.option.controller;

import com.ll.hotel.domain.hotel.option.cotroller.RoomOptionController;
import com.ll.hotel.domain.hotel.option.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.repository.RoomOptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class RoomOptionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomOptionRepository roomOptionRepository;

    private Long testId;

    @BeforeEach
    void setUp() {
        RoomOption roomOption = roomOptionRepository.save(RoomOption
                .builder()
                .name("객실 옵션")
                .build()
        );
        testId = roomOption.getId();
    }

    @Test
    @DisplayName("객실 옵션 추가")
    void addRoomOptionTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        post("/admin/room-options")
                                .content("""
                                        {
                                            "name": "추가 테스트"
                                        }
                                        """.stripIndent())
                                .contentType(
                                        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
                                )
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(RoomOptionController.class))
                .andExpect(handler().methodName("add"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201"))
                .andExpect(jsonPath("$.msg").value("항목이 추가되었습니다."));
    }

    @Test
    @DisplayName("객실 옵션 전체 조회")
    void getAllRoomOptionsTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        get("/admin/room-options")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(RoomOptionController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("모든 항목이 조회되었습니다."));
    }

    @Test
    @DisplayName("객실 옵션 조회")
    void getRoomOptionByIdTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        get("/admin/room-options/{id}", testId)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(RoomOptionController.class))
                .andExpect(handler().methodName("getById"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("항목이 조회되었습니다."));
    }

    @Test
    @DisplayName("객실 옵션 수정")
    void modifyRoomOptionTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        put("/admin/room-options/{id}", testId)
                                .content("""
                                        {
                                            "name": "수정됨"
                                        }
                                        """.stripIndent())
                                .contentType(
                                        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
                                )
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(RoomOptionController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("항목이 수정되었습니다."));
    }

    @Test
    @DisplayName("객실 옵션 삭제")
    void deleteRoomOptionTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        delete("/admin/room-options/{id}", testId)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(RoomOptionController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("항목이 삭제되었습니다."));
    }
}
