package com.waes.palazares.scalableweb.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.waes.palazares.scalableweb.service.DifferenceService;

@WithMockUser(roles = "ADMIN")
@RunWith(SpringRunner.class)
@WebFluxTest(controllers = DifferenceController.class)
public class HomeControllerTest {
    @Autowired
    WebTestClient client;

    @MockBean
    private DifferenceService differenceService;

    @Test
    public void shouldRedirectToSwaggerUI() {
        client.get().uri("/").exchange()
                .expectStatus().is3xxRedirection();
    }
}