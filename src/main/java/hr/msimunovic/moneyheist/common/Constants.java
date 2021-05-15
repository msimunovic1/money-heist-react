package hr.msimunovic.moneyheist.common;

public class Constants {

    // Http Constants
    public static final String HTTP_HEADER_LOCATION = "Location";
    public static final String HTTP_HEADER_CONTENT_LOCATION = "Content-Location";

    // Error Messages
    public static final String MSG_MEMBER_NOT_FOUND = "Member not found";
    public static final String MSG_HEIST_NOT_FOUND = "Heist not found";
    public static final String MSG_MEMBER_EXISTS = "Member exists.";
    public static final String MSG_HEIST_EXISTS = "Heist exists.";
    public static final String MSG_MEMBER_STATUS_NOT_MATCH_TO_HEIST = "Member status must be AVAILABLE or RETIRED.";
    public static final String MSG_HEIST_STATUS_MUST_BE_PLANNING = "Heist status must be PLANNING.";
    public static final String MSG_HEIST_STATUS_MUST_NOT_BE_PLANNING = "Heist status must not be PLANNING.";
    public static final String MSG_HEIST_STATUS_MUST_BE_READY = "Heist status must be READY.";

    public static final String DEFAULT_SKILL_LEVEL = "*";

    // Email Subjects and Messages
    public static final String MAIL_MEMBER_ADDED_TO_HEIST_SUBJECT = "Heist member";
    public static final String MAIL_MEMBER_ADDED_TO_HEIST_TEXT = "You are added to new Heist.";
}
