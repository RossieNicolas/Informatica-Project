package com.architec.crediti.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.REMOVE)
    Set<Assignment> assignedTags;

    @NotNull
    @Column(name = "inactive")
    private boolean inactive;

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

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public Tag() {
    }

    public Tag(String tagName, String tagDescription, boolean inactive) {
        this.tagName = tagName;
        this.tagDescription = tagDescription;
        this.inactive = inactive;
    }

    public Set<Assignment> getAssignedTags() {
        return assignedTags;
    }

    public void setAssignedTags(Set<Assignment> assignedTags) {
        this.assignedTags = assignedTags;
    }
}