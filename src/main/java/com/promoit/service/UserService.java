
package com.promoit.service;

import com.promoit.dao.UserDAO;
import com.promoit.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public boolean register(String username, String password, String role) throws Exception {
        if ("ADMIN".equals(role) && userDAO.existsAdmin()) {
            throw new Exception("Admin already exists");
        }
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        userDAO.insert(new User(0, username, hashed, role));
        return true;
    }

    public User authenticate(String username, String password) throws Exception {
        User user = userDAO.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }
}
