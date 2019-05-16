package com.architec.crediti.models;

import javax.persistence.*;

@Entity
@Table(name = "documentation")
public class Documentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileId")
    private int documentId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "downloadlink")
    private String downloadLink;

    public Documentation() {
    }

    public Documentation(String fileTitle, String download) {
        this.title = fileTitle;
        this.downloadLink = download;
    }

    public int getId() {
        return documentId;
    }

    public void setId(int fileId) {
        this.documentId = fileId;
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

}