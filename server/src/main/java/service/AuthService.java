package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.AuthDAO;
import model.UserData;

public class AuthService {
    private AuthDAO auths = new MemoryAuthDAO();
    public AuthService(){}

}
