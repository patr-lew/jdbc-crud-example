package pl.lewandowski.model;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Planet {
    private Long id;
    private String name;
    private String biggestMoon;
    private Double solarDay;
    private long distanceSun;

}
