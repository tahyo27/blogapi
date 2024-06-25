package com.blog.practiceapi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostEditor {
    private final String title;
    private final String content;

    @Builder(builderClassName = "PostEditorBuilder")
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static class PostEditorBuilder {
        private String title;
        private String content;

        public PostEditorBuilder title(String title) {
            if(title != null) {
                this.title = title;
            }
            return this;
        }
        public PostEditorBuilder content(String content) {
            if (content != null) {
                this.content = content;
            }
            return this;
        }
        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }
    }

}
