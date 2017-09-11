package amazingme.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amazingme.activities.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import amazingme.activities.util.DialogHelper;
import amazingme.app.EnumeratedActivity;
import amazingme.controller.ActivityManager;

public class FirebaseHelper {

    public FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public FirebaseUser getFirebaseUser() {
        return getFirebaseAuthInstance().getCurrentUser();
    }

    public static void createNewUser(final String email,
                                     final String password,
                                     final Activity activity) {
        final FirebaseAuth mAuth = getFirebaseAuthInstance();
        // TODO: store names. also need to create another activity to store more parents and child info.
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        onCompleteRegistrationHandler(activity, task);
                    }
                });
    }

    public static void loginUser(final String email, final String password, final Activity activity) {
        final FirebaseAuth mAuth = getFirebaseAuthInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        onCompleteLoginHandler(activity, task);
                    }
                });
    }

    public void signOut() {
        getFirebaseAuthInstance().signOut();
    }




    private static void onCompleteRegistrationHandler(final Activity activity, final Task<AuthResult> task) {
        final Resources res = activity.getResources();

        final String registrationFailed = res.getString(R.string.dialog_registration_failed);
        final AlertDialog.Builder alertDialog = DialogHelper.getAlertDialog(activity, registrationFailed);

        if (task.isSuccessful()) {
            ActivityManager.getInstance().goTo(activity, EnumeratedActivity.MAIN_MENU);
        } else {
            final String exceptionMessage = getRegistrationExceptionMessage(task.getException(), res);

            alertDialog.setMessage(exceptionMessage);
            alertDialog.show();
        }
    }

    private static String getRegistrationExceptionMessage(final Exception exception, final Resources res) {
        final String registrationTag = res.getString(R.string.tag_registration);
        String exceptionMessage;

        try {
            throw exception;
        } catch (FirebaseAuthEmailException e) {
            exceptionMessage = res.getString(R.string.error_invalid_email);
        } catch (FirebaseAuthUserCollisionException e) {
            exceptionMessage = res.getString(R.string.error_user_exists);
        } catch (FirebaseAuthWeakPasswordException e) {
            exceptionMessage = res.getString(R.string.error_weak_password);
        } catch (Exception e) {
            Log.e(registrationTag, e.getMessage());
            exceptionMessage = res.getString(R.string.error_unknown);
        }
        return exceptionMessage;
    }

    private static void onCompleteLoginHandler(final Activity activity, final Task<AuthResult> task) {
        final Resources res = activity.getResources();

        final String loginFailed = res.getString(R.string.dialog_login_failed);
        final AlertDialog.Builder alertDialog = DialogHelper.getAlertDialog(activity, loginFailed);

        if (task.isSuccessful()) {
            ActivityManager.getInstance().goTo(activity, EnumeratedActivity.MAIN_MENU);
        } else {
            final String exceptionMessage = getLoginExceptionMessage(task.getException(), res);

            alertDialog.setMessage(exceptionMessage);
            alertDialog.show();
        }
    }

    private static String getLoginExceptionMessage(final Exception exception, final Resources res) {
        final String loginTag = res.getString(R.string.tag_login);
        String exceptionMessage;

        try {
            throw exception;
        } catch (FirebaseAuthInvalidCredentialsException
                | FirebaseAuthEmailException
                | FirebaseAuthInvalidUserException e) {
            exceptionMessage = res.getString(R.string.error_invalid_credentials);
        } catch (Exception e) {
            Log.e(loginTag, e.getMessage());
            exceptionMessage = res.getString(R.string.error_unknown);
        }

        return exceptionMessage;
    }

}
