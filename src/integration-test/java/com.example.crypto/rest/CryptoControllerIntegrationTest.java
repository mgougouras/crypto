package com.example.crypto.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.crypto.application.getboundvalues.GetBoundValuesView;
import com.example.crypto.application.gethighestnormalized.GetHighestNormalizedView;
import com.example.crypto.application.getnormalizedrange.GetNormalizedRangeView;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
public class CryptoControllerIntegrationTest {

    private WebTestClient wtc;

    @Inject
    private ApplicationContext context;

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode();

    @Before
    public void setUp() {
        this.wtc = WebTestClient.bindToApplicationContext(this.context)
                .configureClient()
                .build();

        hoverflyRule.resetJournal();
        hoverflyRule.resetState();
    }

    @Test
    public void test_getEngagementMetrics() {

        wtc.get().uri(uriBuilder -> uriBuilder
                        .path("/cryptos/normalizedRange")
                        .queryParam("dateFrom", "2022-01-01")
                        .queryParam("dateTo", "2022-01-31")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(GetNormalizedRangeView[].class)
                .consumeWith(response -> {
                    final GetNormalizedRangeView[] views = response.getResponseBody();
                    assertNotNull(views);
                    assertEquals(5, views.length);

                    final GetNormalizedRangeView view1 = views[0];
                    assertNotNull(view1);
                    assertEquals("ETH", view1.getName());
                    assertEquals(Double.valueOf(0.6383810110763016), view1.getRange());

                    final GetNormalizedRangeView view2 = views[1];
                    assertNotNull(view2);
                    assertEquals("XRP", view2.getName());
                    assertEquals(Double.valueOf(0.5060541310541311), view2.getRange());

                    final GetNormalizedRangeView view3 = views[2];
                    assertNotNull(view3);
                    assertEquals("DOGE", view3.getName());
                    assertEquals(Double.valueOf(0.5046511627906975), view3.getRange());

                    final GetNormalizedRangeView view4 = views[3];
                    assertNotNull(view4);
                    assertEquals("LTC", view4.getName());
                    assertEquals(Double.valueOf(0.4651837524177949), view4.getRange());

                    final GetNormalizedRangeView view5 = views[4];
                    assertNotNull(view5);
                    assertEquals("BTC", view5.getName());
                    assertEquals(Double.valueOf(0.43412110435594536), view5.getRange());
                });
    }

    @Test
    public void test_getBoundValues() {

        wtc.get().uri(uriBuilder -> uriBuilder
                        .path("/cryptos/{symbol}/boundValues")
                        .queryParam("dateFrom", "2022-01-01")
                        .queryParam("dateTo", "2022-01-31")
                        .build("ETH"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(GetBoundValuesView.class)
                .consumeWith(response -> {
                    final GetBoundValuesView view = response.getResponseBody();
                    assertNotNull(view);

                    assertEquals(Double.valueOf(2336.52), view.getMinValue());
                    assertEquals(Double.valueOf(3828.11), view.getMaxValue());
                    assertEquals(Double.valueOf(3715.32), view.getOldestValue());
                    assertEquals(Double.valueOf(2672.5), view.getNewestValue());
                });
    }

    @Test
    public void test_getHighestNormalized() {

        wtc.get().uri(uriBuilder -> uriBuilder
                        .path("/cryptos/normalizedRange/highest")
                        .queryParam("day", "2022-01-02")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(GetHighestNormalizedView.class)
                .consumeWith(response -> {
                    final GetHighestNormalizedView view = response.getResponseBody();
                    assertNotNull(view);

                    assertEquals(Double.valueOf(0.021545908948832165), view.getRange());
                    assertEquals("ETH", view.getName());
                });
    }
}
