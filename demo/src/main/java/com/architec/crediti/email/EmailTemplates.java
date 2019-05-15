package com.architec.crediti.email;

public class EmailTemplates {

    //voor coördinator
    public static String createdAssignment(String assigner, String title, String email, String url, String group) {
        String mail = "Beste,\n"+
                "Nieuwe ZAP-opdracht van student(e) "+ assigner +".\n" +
                group + "\n" +
                email + " naam opdracht: "+ title +
                "\nTe vinden op: "+ url;

        return mail;
    }

    public static String archivedAssignment(String assigner, String title, String email, String url, String group) {
        String mail = "Beste,\n"+
                "Volgende ZAP-opdracht van student(e) "+ assigner +" werd gearchiveerd.\n" +
                group + "\n" +
                email + " naam opdracht: "+ title +
                "\nTe vinden op: "+ url;

        return mail;
    }

    public static String validatedAssignment(String name) {
        String mail = "Beste,\n\n"+
                "De opdracht ("+ name +") die u via ons ZAP-platform ingaf werd gevalideerd en is nu zichtbaar voor studenten. " +
                "Van het moment dat een student geïnteresseerd is, neemt hij met u contact op voor het invullen en ondertekenen " +
                "van de contracten. Van zodra dat rond is (handtekening van uzelf, de verantwoordelijke van de hogeschool en " +
                "de student) kan de student van start gaan.\n\n"+
                "Na het uitvoeren van de opdracht verwacht de hogeschool nog een ondertekend bewijsdocument van u en " +
                "verwachten wij dat de student de nodige bewijsstukken " +
                "( teksten, audio, video, vergaderverslagen, oefeningen, fotomateriaal,…) kan aanleveren.\n\n"+
                "Bedankt voor de samenwerking.\n\n"+
                "AP-hogeschool";

        return mail;
    }

    // voor de externe

    public static String validatedAssignmentByExternal() {
        String mail = "Beste,\n\n"+
                "Uw registratie werd gevalideerd. Nu kunt u inloggen op de pagina.\n\n"+
                "De coördinator";

        return mail;
    }

    public static String notValidatedAssignmentByExternal() {
        String mail = "Beste,\n\n"+
                "Uw registratie werd geweigerd.\n\n"+
                "De coördinator";

        return mail;
    }

    // voor de student
    public static String validatedAssignmentStudent(String name) {
        String mail = "Beste,\n\n"+
                "De ZAP- opdracht ("+ name +") die je ingaf/waarvoor je intekende werd gevalideerd. Om te starten breng je de contracten in orde. " +
                "Pas na ondertekening door de opdrachtgever en de hogeschool mag en kan je starten.\n\n" +
                "Na het beëindigen van de opdracht, ondertekent de opdrachtgever het bewijsdocument, " +
                "vul je de zelfevaluatie in en verzamel je de nodige bewijsstukken voor je portfolio.\n\n" +
                "Succes.\n" +
                "\n" +
                "De coördinator";

        return mail;
    }

    public static String enrolledAssignmentStudent(String firstname, String lastname, String title, String email, String url, String name) {
        String mail = "Beste,\n\n"+
                "Volgende student, " + firstname + " " + lastname + ", heeft zich ingeschreven voor de opdracht ("+ name +") " +
                "die u via ons ZAP-platform ingaf. \n\n" +
                email + "naam opdracht: " + title +
                "\nTe vinden op: "+ url + "\n\n" +
                "AP-hogeschool";

        return mail;
    }

}
