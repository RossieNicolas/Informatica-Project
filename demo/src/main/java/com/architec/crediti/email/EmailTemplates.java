package com.architec.crediti.email;

public class EmailTemplates {

    private EmailTemplates() {}

    //voor coördinator
    public static String createdAssignment(String assigner, String title, String email, String url) {
        return "Beste,\n"+
                "Nieuwe ZAP-opdracht van student(e) "+ assigner +".\n" +
                email + " naam opdracht: "+ title +
                "\nTe vinden op: "+ url;
    }

    public static String archivedAssignment(String assigner, String title, String email, String url) {
        return"Beste,\n"+
                "Volgende ZAP-opdracht van student(e) "+ assigner +" werd gearchiveerd.\n" +
                email + " naam opdracht: "+ title +
                "\nTe vinden op: "+ url;
    }

    public static String validatedAssignment(String name) {
        return  "Beste,\n\n"+
                "De opdracht ("+ name +") die u via ons ZAP-platform ingaf werd gevalideerd en is nu zichtbaar voor studenten. " +
                "Van het moment dat een student geïnteresseerd is, neemt hij met u contact op voor het invullen en ondertekenen " +
                "van de contracten. Van zodra dat rond is (handtekening van uzelf, de verantwoordelijke van de hogeschool en " +
                "de student) kan de student van start gaan.\n\n"+
                "Na het uitvoeren van de opdracht verwacht de hogeschool nog een ondertekend bewijsdocument van u en " +
                "verwachten wij dat de student de nodige bewijsstukken " +
                "( teksten, audio, video, vergaderverslagen, oefeningen, fotomateriaal,…) kan aanleveren.\n\n"+
                "Bedankt voor de samenwerking.\n\n"+
                "AP-hogeschool";
    }

    public static String enrolledAssignment( String title, String assigner, String email) {
        return "Beste,\n\n"+
                "Nieuwe inschrijving van volgende ZAP-opdracht: "+ title +".\n" +
                "Naam student(e): "+ assigner + "\n" +
                "E-mailadres: " + email;
    }

    // voor student
    public static String waitValidationEnrolledAssignmentStudent(String name) {
        return"Beste,\n\n"+
                "Je hebt je ingeschreven voor volgende ZAP- opdracht: "+ name +". " +
                "\nOm te starten moet je wachten op een bevestiging van de opdrachtgever. " +
                "\n\n" +
                "De coördinator";
    }

    public static String approvedEnrolledAssignmentStudent(String name) {
        return "Beste,\n\n"+
                "Je ingeschrijving voor volgende ZAP- opdracht, "+ name +" werd goedgekeurd. Om te starten breng je de contracten in orde. " +
                "Pas na ondertekening door de opdrachtgever en de hogeschool mag en kan je starten.\n\n" +
                "Na het beëindigen van de opdracht, ondertekent de opdrachtgever het bewijsdocument, " +
                "vul je de zelfevaluatie in en verzamel je de nodige bewijsstukken voor je portfolio.\n\n" +
                "Succes.\n" +
                "\n" +
                "De coördinator";
    }

    public static String declinedEnrolledAssignmentStudent(String name) {
        return "Beste,\n\n"+
                "Je inschrijving voor ZAP- opdracht, "+ name +", werd geweigerd. " +
                "\n\n" +
                "De coördinator";
    }

    public static String unvalidatedEnrolledAssignmentStudent(String name) {
        return "Beste,\n\n"+
                "Je hebt je ingeschreven voor volgende ZAP- opdracht, "+ name +". Om te starten breng je de contracten in orde. " +
                "Pas na ondertekening door de opdrachtgever en de hogeschool mag en kan je starten.\n\n" +
                "Na het beëindigen van de opdracht, ondertekent de opdrachtgever het bewijsdocument, " +
                "vul je de zelfevaluatie in en verzamel je de nodige bewijsstukken voor je portfolio.\n\n" +
                "Succes.\n" +
                "\n" +
                "De coördinator";

    }

    // Voor externe
    public static String validatedExternal() {
        return  "Beste,\n\n"+
                "Uw registratie werd goedgekeurd. Nu kunt u inloggen op de pagina.\n\n"+
                "De coördinator";
    }

    public static String notValidatedExternal() {
        return "Beste,\n\n"+
                "Uw registratie werd geweigerd.\n\n"+
                "De coördinator";
    }

    public static String newExternalUser(long userId, String name, String company, String address, String phone, String email) {
        return  "Een externe opdrachtgever heeft zich zojuist geregistreerd.\n\n" +
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
    }
    public static String reminder(String opdrachtTitel ) {
        return "Beste,\n\n"+
                "De deadline van opdracht : " + opdrachtTitel + " is morgen";
    }

}
