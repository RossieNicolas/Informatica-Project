package com.architec.crediti.models;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileId")
    private int fileId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "downloadlink")
    private String downloadLink;

    @Column(name = "type_doc")
    private String docType;

    @Column(name = "assignmentId")
    private long assignmentId;

    @Column(name = "status")
    private Progress status;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public File() {
    }

    public File(String fileTitle, String download, User user, String typeDoc, long assignmentId) {
        this.title = fileTitle;
        this.downloadLink = download;
        this.user = user;
        this.docType = typeDoc;
        this.assignmentId = assignmentId;
        this.status = Progress.START;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getAssignmentId() {
        return assignmentId;
    }

    public String getDocType() {
        return docType;
    }

    public String getStatus() {
        return status.toString();
    }
}