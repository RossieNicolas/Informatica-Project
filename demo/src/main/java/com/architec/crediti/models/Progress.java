package com.architec.crediti.models;

public enum Progress {
    START(0, "start"), CONTRACT(25, "contract"), PORTFOLIO(50, "portfolio"), BEWIJS(75, "bewijsdocument"), END(100, "afgerond");
    private String info;
    private int status;

    Progress(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public int getStatus() {
        return status;
    }

    public String getNext() {
        switch (status) {
            case 0:
                return "contract";
            case 25:
                return "portfolio";
            case 50:
                return "bewijsdocument";
            default:
                return "afgerond";
        }
    }
}
