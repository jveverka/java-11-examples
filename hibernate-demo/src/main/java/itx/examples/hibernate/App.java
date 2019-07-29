package itx.examples.hibernate;

import itx.examples.hibernate.entities.UserData;
import itx.examples.hibernate.services.DataAccessService;
import itx.examples.hibernate.services.DataAccessServiceImpl;

public class App {

    public static void main(String[] args) {
        DataAccessService dataAccessService = new DataAccessServiceImpl();
        UserData userData = new UserData("Juraj", "Veverka", "juraj.veverka@inter-net.sk");
        dataAccessService.saveUserData(userData);
        dataAccessService.getUserData();
    }

}
