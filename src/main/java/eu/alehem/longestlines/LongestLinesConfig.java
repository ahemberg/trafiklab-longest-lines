package eu.alehem.longestlines;

import eu.alehem.longestlines.service.TrafiklabService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LongestLinesConfig {

  @Value("${trafiklab.base-url}")
  private String trafiklabBaseUrl;

  @Value("${trafiklab.api-key}")
  private String trafiklabApiKey;

  @Bean
  TrafiklabService trafiklabService() {
    return new TrafiklabService(trafiklabBaseUrl, trafiklabApiKey);
  }

  @Bean
  public WebClient.Builder webClient() {
    return WebClient.builder();
  }
}
