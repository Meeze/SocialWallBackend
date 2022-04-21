package de.udg.socialwall.service;

import com.google.gson.Gson;
import de.udg.socialwall.PostType;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
abstract class AbstractApiService {

    protected final Gson gson = new Gson();

    protected abstract RestTemplate getRestTemplate();

    @SneakyThrows
    protected <t> ResponseEntity<t> request(String url, Class<t> tClass) {
        ResponseEntity<t> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<>(null), tClass);
        return responseEntity;
    }

}
