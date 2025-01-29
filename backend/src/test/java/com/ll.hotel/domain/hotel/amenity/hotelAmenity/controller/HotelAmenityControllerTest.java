package com.ll.hotel.domain.hotel.amenity.hotelAmenity.controller;

import com.ll.hotel.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Import(TestSecurityConfig.class)
public class HotelAmenityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("호텔 어메니티 추가")
    void addHotelAmenityTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        post("/admin/hotel-amenities")
                                .content("""
                                        {
                                            "description": "추가 테스트"
                                        }
                                        """.stripIndent())
                                .contentType(
                                        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
                                )
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(HotelAmenityController.class))
                .andExpect(handler().methodName("add"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201"))
                .andExpect(jsonPath("$.msg").value("항목이 추가되었습니다."));
    }

    @Test
    @DisplayName("호텔 어메니티 전체 조회")
    void getAllHotelAmenitiesTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        get("/admin/hotel-amenities")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(HotelAmenityController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("모든 항목이 조회되었습니다."));
    }

    @Test
    @DisplayName("호텔 어메니티 조회")
    void getHotelAmenityByIdTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        get("/admin/hotel-amenities/1")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(HotelAmenityController.class))
                .andExpect(handler().methodName("getById"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("항목이 조회되었습니다."));
    }

    @Test
    @DisplayName("호텔 어메니티 수정")
    void modifyHotelAmenityTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        put("/admin/hotel-amenities/1")
                                .content("""
                                        {
                                            "description": "수정됨"
                                        }
                                        """.stripIndent())
                                .contentType(
                                        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
                                )
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(HotelAmenityController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("항목이 수정되었습니다."));
    }

    @Test
    @DisplayName("호텔 어메니티 삭제")
    void deleteHotelAmenityTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        delete("/admin/hotel-amenities/1")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(HotelAmenityController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("항목이 삭제되었습니다."));
    }
}
