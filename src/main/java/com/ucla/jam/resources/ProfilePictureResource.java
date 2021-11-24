package com.ucla.jam.resources;

import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProfilePictureResource {

    private static final Map<String, String> ACCEPTED_FILETYPES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/svg", "svg",
            "image/bmp", "bpm"
    );

    @Value("${server.static.content.path}")
    private String staticContentPath;

    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    public void uploadContent(@SessionFromHeader SessionInfo sessionInfo, @RequestPart MultipartFile image) {
        if (!ACCEPTED_FILETYPES.containsKey(image.getContentType())) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        try {
            UUID userId = sessionInfo.getUserId();
            String extension = ACCEPTED_FILETYPES.get(image.getContentType());
            for (File file : allFilesWithPathPrefix(userId.toString(), staticContentPath)) {
                file.delete();
            }

            String filename = Paths.get(staticContentPath, userId.toString()) + "." + extension;
            image.transferTo(new File(filename));
            // sometimes MultipartFile is stored in a temp file, sometimes in memory. I hope that the temp file is
            // deleted automatically, because I can't get Spring to put it in the temp file for testing purposes :/
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "upload/supported", produces = APPLICATION_JSON_VALUE)
    public Collection<String> supportedTypes() {
        return ACCEPTED_FILETYPES.values();
    }

    private File[] allFilesWithPathPrefix(String prefix, String dirPath) {
        return new File(dirPath).listFiles((ignored, name) -> name.startsWith(prefix));
    }

}
