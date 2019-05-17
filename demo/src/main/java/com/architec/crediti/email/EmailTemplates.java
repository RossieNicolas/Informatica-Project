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

    public static String enrolledAssignment( String title, String assigner, String email, String group) {
        String mail = "Beste,\n\n"+
                "Nieuwe inschrijving van volgende ZAP-opdracht: "+ title +".\n" +
                "Naam student(e): "+ assigner + "\n" +
                "Groep: " + group + "\n" +
                "E-mailadres: " + email;

        return mail;
    }

    // voor student
    public static String enrolledAssignmentStudent(String name) {
        String mail = "Beste,\n\n"+
                "Je hebt je ingeschreven voor volgende ZAP- opdracht, "+ name +". Om te starten breng je de contracten in orde. " +
                "Pas na ondertekening door de opdrachtgever en de hogeschool mag en kan je starten.\n\n" +
                "Na het beëindigen van de opdracht, ondertekent de opdrachtgever het bewijsdocument, " +
                "vul je de zelfevaluatie in en verzamel je de nodige bewijsstukken voor je portfolio.\n\n" +
                "Succes.\n" +
                "\n" +
                "De coördinator";

        return mail;
    }

    // Voor externe
    public static String validatedExternal() {
        String mail = "Beste,\n\n"+
                "Uw registratie werd goedgekeurd. Nu kunt u inloggen op de pagina.\n\n"+
                "De coördinator";

        return mail;
    }

    public static String notValidatedExternal() {
        String mail = "Beste,\n\n"+
                "Uw registratie werd geweigerd.\n\n"+
                "De coördinator";

        return mail;
    }

    public static String newExternalUser(long userId, String name, String company, String address, String phone, String email) {
        String mail =
                "Een externe opdrachtgever heeft zich zojuist geregistreerd.\n\n" +
                "Enkele gegevens van de opdrachtgever:\n\n" +
                "Volledige naam: " + name + "\n" +
                "Bedrijf: " + company + "\n" +
                "Adres: " + address + "\n" +
                "Telefoon: " + phone + "\n" +
                "E-mail: " + email + "\n\n" +
                        //TODO: vervang localhost door vps092.be na testing
                "VALIDEER deze externe: http://localhost:8080/validateexternal/" + userId + "\n" +
                "WEIGER deze externe: http://localhost:8080/deleteexternal/" + userId + "\n\n" +
                "Lijst van alle ongevalideerde externe: http://localhost:8080/listUnvalidatedExternal";
        return mail;
    }
    public static String reminder(String opdrachtTitel ) {
        String mail = "Beste,\n\n"+
                "De deadline van opdracht : " + opdrachtTitel + " is morgen";

        return mail;


    }

}