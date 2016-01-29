package msi.myapplication;

/**
 * Created by MSI! on 07.10.2015.
 */
public interface ServerAuthenticate {
    public String userSignUp(final String name, final String pass, String authType) throws Exception;
    public String userSignIn(final String user, final String pass, String authType) throws Exception;
    public String getUserObjectId();

    public boolean userSignUpWithoutToken(final String name, final String pass, String authType) throws Exception;
    public boolean userSignInWithoutToken(final String user, final String pass, String authType) throws Exception;

}
