package com.architec.crediti.email;

public class EmailTemplates {

    private EmailTemplates() {}

    //voor coördinator
    public static String createdAssignment(String assigner, String title, String email, String url) {
        return "Beste,\n\n"+
                "Nieuwe opdracht van lector/externe/student(e) "+ assigner +" (" + email + ").\n" +
                "Naam opdracht: "+ title +
                ", te vinden op: "+ url + "\n\n"+
                "Bedankt voor de samenwerking" + "\n\n" +
                "AP-hogeschool";
    }

    public static String validatedAssignment(String name) {
        return  "Beste,\n\n"+
                "De opdracht, "+ name +", die u via ons platform ingaf werd gevalideerd en is nu zichtbaar voor studenten. " +
                "Van het moment dat een student geïnteresseerd is, neemt hij met u contact op voor het invullen en ondertekenen " +
                "van de contracten. Van zodra dat rond is (handtekening van uzelf, de verantwoordelijke van de hogeschool en " +
                "de student) kan de student van start gaan.\n\n"+
                "Na het uitvoeren van de opdracht verwacht de hogeschool nog een ondertekend bewijsdocument van u en " +
                "verwachten wij dat de student de nodige bewijsstukken " +
                "( teksten, audio, video, vergaderverslagen, oefeningen, fotomateriaal,…) kan aanleveren.\n\n"+
                "Bedankt voor de samenwerking.\n"+
                "AP-hogeschool";
    }

    public static String enrolledAssignment( String title, String assigner, String email) {
        return "Beste,\n\n"+
                "Nieuwe inschrijving van volgende opdracht: "+ title +".\n" +
                "Naam student(e): "+ assigner + "\n" +
                "E-mailadres: " + email + "\n\n"+
                "Bedankt voor de samenwerking.\n"+
                "AP-hogeschool";
    }

    // voor student
    public static String waitValidationEnrolledAssignmentStudent(String name) {
        return"Beste,\n\n"+
                "Je hebt je ingeschreven voor volgende opdracht: "+ name +"." +
                "\nOm te starten moet je wachten op een bevestiging van de opdrachtgever. " +
                "\n\n" +
                "AP-hogeschool";
    }

    public static String approvedEnrolledAssignmentStudent(String name) {
        return "Beste,\n\n"+
                "Je ingeschrijving voor volgende opdracht, "+ name +", werd goedgekeurd. Om te starten breng je de contracten in orde. " +
                "Pas na ondertekening door de opdrachtgever en de hogeschool mag en kan je starten.\n\n" +
                "Na het beëindigen van de opdracht, ondertekent de opdrachtgever het bewijsdocument, " +
                "vul je de zelfevaluatie in en verzamel je de nodige bewijsstukken voor je portfolio.\n\n" +
                "Succes.\n" +
                "\n" +
                "De coördinator";
    }

    public static String declinedEnrolledAssignmentStudent(String name) {
        return "Beste,\n\n"+
                "Je inschrijving voor opdracht, "+ name +", werd geweigerd." +
                "\n\n" +
                "DAP-hogeschool";
    }

    // Voor externe
    public static String validatedExternal() {
        return  "Beste,\n\n"+
                "Uw registratie werd goedgekeurd. U kan zich nu inloggen.\n\n"+
                "AP-hogeschool";
    }

    public static String notValidatedExternal() {
        return "Beste,\n\n"+
                "Uw registratie werd geweigerd.\n\n"+
                "AP-hogeschool";
    }

    public static String newExternalUser(String name, String company, String address, String phone, String email) {
        return  "Een externe opdrachtgever heeft zich zojuist geregistreerd.\n\n" +
                "Enkele gegevens van de opdrachtgever:\n\n" +
                "Volledige naam: " + name + "\n" +
                "Bedrijf: " + company + "\n" +
                "Adres: " + address + "\n" +
                "Telefoon: " + phone + "\n" +
                "E-mail: " + email + "\n\n";
    }

    public static String newExternal() {
        return  "Beste,\n\n"+
                "U heeft zich zojuist geregistreerd. Gelieve te wachten op de validatie van de coördinator.\n\n" +
                "AP-hogeschool";
    }
    public static String reminder(String opdrachtTitel) {
        return "Beste,\n\n"+
                "De deadline van opdracht : " + opdrachtTitel + " is morgen.\n\n" +
                "AP-hogeschool";
    }
    public static String userAlreadyExists() {
        return "Beste,\n\n"+
                "Een externe opdrachtgever probeerde zich zojuist te registreren op het platform met dit emailadres." +
                " Als u dit niet bent, of u herinnert uw wachtwoord niet meer, dan kan u een wachtwoordherstel aanvragen" +
                " via volgende link: http://vps092.ap.be/forgotPassword\n\n" +
                "AP-hogeschool";
    }

}
