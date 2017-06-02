package com.askdog.model.entity;

import com.askdog.model.converter.TemplateContentConverter;
import com.askdog.model.entity.inner.template.Content;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Locale;

import static com.askdog.common.RegexPattern.REGEX_TEMPLATE_NAME;

@Entity
@Table(
        name = "mc_template",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"name", "language"}
        )
)
public class Template extends Base {

    private static final long serialVersionUID = 5371244614090734612L;

    @NotNull
    @Pattern(regexp = REGEX_TEMPLATE_NAME)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "content", nullable = false, length = 20000)
    @Convert(converter = TemplateContentConverter.class)
    private Content content;

    @NotNull
    @Column(name = "language", nullable = false)
    private Locale language;

    @NotNull
    @Column(name = "creation_time", nullable = false)
    private Date creationTime;

    @Column(name = "last_modify_time")
    private Date lastModifyTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
