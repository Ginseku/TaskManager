package com.collab.taskmanager.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProjectRequest {
    private String name;
    private String description;
    }