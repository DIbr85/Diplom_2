package models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;


}
