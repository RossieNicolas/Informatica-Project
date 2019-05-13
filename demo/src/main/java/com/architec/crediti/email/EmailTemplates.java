package com.architec.crediti.email;

public class EmailTemplates {

    public static String testEmail(String assigner, String title, String classGroup, String email, String url) {
        String mail = "Test email" +
                "\nTest door " + assigner + " – "+ classGroup+"\n" +
                email+"\n" +
                "Testnaam: " + title + "\n" +
                url;

        return mail;
    }

    public static String createdAssignment(String assigner, String title, String email, String url, String group) {
        String mail = "Beste,\n"+
                "Nieuwe ZAP-opdracht van student(e) "+ assigner +".\n" +
                group + "\n" +
                email + " naam opdracht: "+ title +
                "\nTe vinden op: "+ url;

        return mail;
    }

    public static String archiveAssignment(String assigner, String title, String email, String url, String group) {
        String mail = "Beste,\n"+
                "Volgende ZAP-opdracht van student(e) "+ assigner +" werd gevalideerd.\n" +
                group + "\n" +
                email + " naam opdracht: "+ title +
                "\nTe vinden op: "+ url;

        return mail;
    }

}
