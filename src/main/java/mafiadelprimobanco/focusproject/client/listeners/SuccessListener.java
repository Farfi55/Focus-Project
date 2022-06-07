package mafiadelprimobanco.focusproject.client.listeners;

import mafiadelprimobanco.focusproject.client.DatabaseReference;

@FunctionalInterface
public interface SuccessListener {

    /**
     * Method that is called when an operation required to the server has been successfully completed.
     *
     * @param databaseReference contains the result of the operation.
     * */
    void onSuccess(DatabaseReference databaseReference);
}
