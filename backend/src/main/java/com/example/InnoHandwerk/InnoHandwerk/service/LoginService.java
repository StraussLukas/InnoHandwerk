package com.example.InnoHandwerk.InnoHandwerk.service;

import com.example.InnoHandwerk.InnoHandwerk.entity.Login;
import com.example.InnoHandwerk.InnoHandwerk.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    LoginRepository repository;

    public String addLogin(Login login){
        return repository.save(login).getEmail();
    }

    public List<Login> getAllLogin() {
        return repository.findAll();
    }

    public Optional<Login> getLoginbyEmail(String email) {
        return repository.findById(email);
    }

    public String updateLogin(Login login) {
        return repository.save(login).getEmail();
    }

    public void deleteLoginbyEmail(String email) {
        repository.deleteById(email);
    }

}

