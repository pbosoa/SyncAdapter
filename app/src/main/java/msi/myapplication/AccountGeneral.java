package msi.myapplication;

/**
 * Created by MSI! on 07.10.2015.
 */
public class AccountGeneral {

    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "com.auth";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "auth";

    /**
     * User data fields
     */
    public static final String USERDATA_USER_OBJ_ID = "userObjectId";   //Parse.com object id

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access";

    public static final ServerAuthenticate sServerAuthenticate = new ParseComServer();
    //public static final ServerAuthenticate sServerAuthenticate = new ParseComServer();
}