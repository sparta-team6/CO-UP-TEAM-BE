package com.hanghae.coupteambe.api.domain.dto.kanban;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketInfoDto {

    // 소속 프로젝트 ID
    private UUID pjId;

    // 버킷 ID
    private UUID kbbId;

    // 버킷 제목
    private String title;

    // 배치 순서
    private int position;

}
