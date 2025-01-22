package com.example.jiranbulletinboard.Domain.Vo.Title;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TitleDTO {
    private Integer titleId;
    private String titleName;
    private String titleCode;
    private Integer titleLevel;

    public TitleEntity toEntity() {
        return TitleEntity.builder()
                .titleId(this.titleId)
                .titleName(this.titleName)
                .titleCode(this.titleCode)
                .titleLevel(this.titleLevel)
                .build();
    }
}
