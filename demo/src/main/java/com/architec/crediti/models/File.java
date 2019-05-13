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

    public File() {
    }

    public File(String fileTitle, String download) {
        this.title = fileTitle;
        this.downloadLink = download;
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
}