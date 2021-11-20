package com.ucla.jam.music.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.List;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class MasterResourceResponse {
    String id;
    String title;
    List<Style> styles;
}
