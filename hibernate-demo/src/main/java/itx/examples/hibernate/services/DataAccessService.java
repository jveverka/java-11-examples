package itx.examples.hibernate.services;

import itx.examples.hibernate.entities.UserData;

import java.util.Collection;

public interface DataAccessService {

    void saveUserData(UserData userData);

    Collection<UserData> getUserData();

}
