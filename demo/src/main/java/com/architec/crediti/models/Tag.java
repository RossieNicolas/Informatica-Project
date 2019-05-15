package com.architec.crediti.models;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagId")
    private int tagId;

    @Column(name = "tagName", nullable = false)
    private String tagName;

    @Column(name = "tagDescription")
    private String tagDescription;

    @ManyToMany(mappedBy = "tags")
    Set<Assignment> assignedTags;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public Tag() {
    }

    public Tag(String tagName, String tagDescription) {
        this.tagName = tagName;
        this.tagDescription = tagDescription;
    }

    public Set<Assignment> getAssignedTags() {
        return assignedTags;
    }

    public void setAssignedTags(Set<Assignment> assignedTags) {
        this.assignedTags = assignedTags;
    }
}
