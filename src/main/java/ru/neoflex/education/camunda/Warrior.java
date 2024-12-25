package ru.neoflex.education.camunda;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warrior implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonAlias("name.findName")
    private String name;

    @JsonAlias("name.title")
    private String title;

    private Boolean isAlive;

    @JsonAlias("random.number")
    private Integer hp;
}