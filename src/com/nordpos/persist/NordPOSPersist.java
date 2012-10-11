/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nordpos.persist;

import java.sql.Connection;
import net.sf.persist.Persist;
import org.apache.log4j.Logger;

/**
 *
 * @author svininykh-av
 */
public class NordPOSPersist {

    private static final Logger log = Logger.getLogger(NordPOSPersist.class);
    private static Persist persist;

    public static void main(String[] args) {
        Connection conn = null;
        Persist persist = null;
        log.info("Loading data base");
        conn = ConnectionInstance.getConIsntance();
        persist = new Persist(conn);

        Application application = persist.read(Application.class, "SELECT * FROM APPLICATIONS");
        System.out.println(application.getName());
        People loginUser = persist.read(People.class, "SELECT * FROM PEOPLE WHERE NAME = ?", "Nord");
        String login = loginUser.getName();
        System.out.println(login);

        String password = "nord";

        String sqlStr = "SELECT * FROM PEOPLE WHERE NAME = ? AND APPPASSWORD ";
        People validUser;
        if (password.isEmpty()) {
            validUser = persist.read(People.class, sqlStr.concat("IS NULL"), login);
        } else {
            validUser = persist.read(People.class, sqlStr.concat("= ?"), login, PasswordDecryptor.hashString(password));
        }


        System.out.println(validUser.getId());

        System.out.println(findUser("Nord", "nord").getVisible().toString());
    }

    public static People findUser(String login, String password) {
        persist = new Persist(ConnectionInstance.getConIsntance());
        String sqlStr = "SELECT * FROM PEOPLE WHERE NAME = ? AND APPPASSWORD ";
        People validUser;
        if (password.isEmpty()) {
            validUser = persist.read(People.class, sqlStr.concat("IS NULL"), login);
        } else {
            validUser = persist.read(People.class, sqlStr.concat("= ?"), login, PasswordDecryptor.hashString(password));
        }

        return validUser;
    }
}
