package com.dreamgames.backendengineeringcasestudy.controllertest;

import com.dreamgames.backendengineeringcasestudy.controller.PopBalloonEventController;
import com.dreamgames.backendengineeringcasestudy.dto.InvitePartnerRequest;
import com.dreamgames.backendengineeringcasestudy.dto.UpdateBalloonProgressRequest;
import com.dreamgames.backendengineeringcasestudy.dto.UpdateBalloonProgressResponse;
import com.dreamgames.backendengineeringcasestudy.service.PopBalloonEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PopBalloonEventController.class)
public class PopBalloonEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PopBalloonEventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testJoinEvent() throws Exception {
        mockMvc.perform(post("/pop-balloon-event/join?userId=1"))
                .andExpect(status().isOk());

        verify(eventService, times(1)).joinEvent(1);
    }

    @Test
    void testInvitePartner() throws Exception {
        InvitePartnerRequest request = new InvitePartnerRequest();
        request.setInviterUserId(1);
        request.setInviteeUserId(2);

        mockMvc.perform(post("/pop-balloon-event/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(eventService).invitePartner(1, 2);
    }

    @Test
    void testUpdateBalloon() throws Exception {
        UpdateBalloonProgressRequest req = new UpdateBalloonProgressRequest();
        req.setUserId(1);
        req.setHeliumToUse(100);

        UpdateBalloonProgressResponse resp = new UpdateBalloonProgressResponse();
        resp.setBalloonInflatedAmount(100);
        resp.setPopped(false);

        when(eventService.updateBalloonProgress(1, 100)).thenReturn(resp);

        mockMvc.perform(post("/pop-balloon-event/updateBalloon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(eventService).updateBalloonProgress(1, 100);
    }
}
