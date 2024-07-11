package com.blog.practiceapi.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommentEditor {

    private final String content;

    @Builder(builderClassName = "CommentEditorBuilder")
    public CommentEditor(String content) {
        this.content = content;
    }

    public static class CommentEditorBuilder {
        private String content;

        public CommentEditorBuilder content(String content) {
            if(content != null) {
                this.content = content;
            }
            return this;
        }
        
        public CommentEditor build() {
            return new CommentEditor(this.content);
        }

    }
}
