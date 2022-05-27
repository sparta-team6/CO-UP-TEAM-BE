package com.hanghae.coupteambe.api.domain.dto.project;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ReqProjectInfoDto {

    //프로젝트번호
    private UUID pjId;
    // 이미지주소
    private String thumbnail;
    //프로젝트제목
    private String title;
    //프로젝트개요
    private String summary;
}
