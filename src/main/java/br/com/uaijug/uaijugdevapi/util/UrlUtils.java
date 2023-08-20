package br.com.uaijug.uaijugdevapi.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlUtils {

    public static URI getUriAssociateFondedView(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        /*return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();*/
    }
}
