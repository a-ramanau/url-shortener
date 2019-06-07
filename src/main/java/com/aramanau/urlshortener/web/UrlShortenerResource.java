package com.aramanau.urlshortener.web;

import com.aramanau.urlshortener.repository.UrlRepository;
import com.aramanau.urlshortener.util.Base62Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class UrlShortenerResource {

    private final UrlRepository urlRepository;

    public UrlShortenerResource(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<String> shorten(@RequestParam String longUrl) {

        Long id = urlRepository
            .findIdByUrl(longUrl)
            .orElseGet(() -> urlRepository.save(longUrl));

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .replacePath("/{id}")
            .replaceQuery("")
            .buildAndExpand(Base62Utils.encode(id))
            .toUri();

        return ResponseEntity
            .created(location).body(location.toString());
    }

    @GetMapping("/api/restore")
    public ResponseEntity<String> restore(@RequestParam String shortUrl) {

        String base62Id = URI
            .create(shortUrl)
            .getPath()
            .substring(1);

        Long id = Base62Utils.decode(base62Id);

        return urlRepository
            .findUrlById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable(name = "id") String base62Id) {

        Long id = Base62Utils.decode(base62Id);

        return urlRepository
            .findUrlById(id)
            .map(url -> ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", url)
                .<Void>build()
            )
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
