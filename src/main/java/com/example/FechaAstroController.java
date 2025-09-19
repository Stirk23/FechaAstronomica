package com.example;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/evento")
public class FechaAstroController {

    private final String WIKIPEDIA_API = "https://api.wikimedia.org/feed/v1/wikipedia/en/onthisday/events/";
    private final String NASA_API = "https://api.nasa.gov/planetary/apod?api_key=";
    private final String NASA_API_KEY = "bG6FsKiYDq1NXfXqeGAdmoLNxHQNwTk8xpk9kVar";

    @GetMapping()
    public ResponseEntity<List<WikipediaRequest>> getMethodName(@RequestParam String mes, @RequestParam String dia) {

        String url = WIKIPEDIA_API + mes + "/" + dia;
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "MyApp/1.0 (tu-email@example.com)");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<?, ?> raw = response.getBody();
        List<Map<String, Object>> eventos = (List<Map<String, Object>>) raw.get("events");

        List<WikipediaRequest> list = eventos.stream()
                .limit(5)
                .map(ev -> {
                    int year = (int) ev.get("year");
                    String text = (String) ev.get("text");

                    WikipediaRequest wiki = new WikipediaRequest();
                    wiki.setYear(year);
                    wiki.setText(text);

                    String date = year + "-01-01";
                    String nasaUrl = NASA_API + NASA_API_KEY + "&date=" + date;
                    NasaApod nasaResponse = restTemplate.getForObject(nasaUrl, NasaApod.class);
                    if (nasaResponse != null) {
                        wiki.setNasaApod(nasaResponse.getUrl());
                    }                    
                    return wiki;
                })
                .toList();

        return ResponseEntity.ok(list);
    }

}