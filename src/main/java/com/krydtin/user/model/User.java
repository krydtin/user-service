package com.krydtin.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.krydtin.user.constant.MemberType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "[user]")
@JsonIgnoreProperties(value = {"password"}, allowGetters = false, allowSetters = true)
public class User implements Serializable {

    @Id
    @Size(min = 4, max = 50)
    @Column(unique = true)
    private String username;

    @NotNull
    @Size(min = 8, max = 60)
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    private String address;

    @NotNull
    @Size(min = 4)
    @Pattern(regexp = "[0-9]+", message = "Numeric only")
    @Column(nullable = false)
    private String phone;

    @NotNull
    @Min(value = 0)
    @Column(nullable = false)
    private Double salary;

    @Column(name = "reference_code")
    private String referenceCode;

    @Column(name = "member_type")
    private MemberType memberType;

}
