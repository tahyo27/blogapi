package com.blog.practiceapi.common;

import com.blog.practiceapi.domain.Image;
import com.blog.practiceapi.domain.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class ImageNameParser {

    private final String originName;
    private final String uuidName;
    private final String gcsPath;
    private final String tempName;
    private final String extension;
    public ImageNameParser(String src) { //todo 주소처리 로컬이니까 변경 필요

        String replacedName = src.replace("/temp/image/", "");
        String[] parts = replacedName.split("_", 2);
        String origin = parts[1];
        String fileExtension = origin != null && origin.contains(".") //확장자 추출
                ? origin.substring(origin.lastIndexOf('.')) : "";
        String unique = parts[0] + fileExtension;
        String path = "https://storage.googleapis.com/imgtest_bucket/" + unique;

        this.extension = fileExtension;
        this.tempName = replacedName;
        this.originName = origin;
        this.uuidName = unique;
        this.gcsPath = path;

    }

    public Image convertImage(Post post) {
        return Image.builder()
                .post(post)
                .originName(originName)
                .uniqueName(uuidName)
                .imagePath(gcsPath)
                .build();
    }

}