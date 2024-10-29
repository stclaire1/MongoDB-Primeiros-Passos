package com.iftm.startexample.service;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iftm.startexample.dto.UserDTO;
import com.iftm.startexample.models.User;
import com.iftm.startexample.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repo;

    public ResponseEntity<List<UserDTO>> findAll() {
        var dbUsers = repo.findAll();
        if(dbUsers.isEmpty())
            return ResponseEntity.notFound().build();

            var userDtos = dbUsers.stream().map(user -> {
                var userDTO = new UserDTO(user);
                return userDTO;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(userDtos);
    }
    
    public ResponseEntity<UserDTO> findById(ObjectId id) {
        if(id == null)
            return ResponseEntity.badRequest().build();
            var dbUser = repo.findById(id);
            if(dbUser.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(new UserDTO(dbUser.get()));
    }

    public ResponseEntity<UserDTO> save(User user) {
        // Validar usuario
        if(user.getName().isBlank() || user.getAge() <= 0)
            return ResponseEntity.badRequest().build();
        user.setId(ObjectId.get());

        return ResponseEntity.ok(new UserDTO(repo.save(user)));
    }

    public ResponseEntity<UserDTO> update(UserDTO user) {
        // Validar usuario
        if(user.getId() == null)
            return ResponseEntity.badRequest().build();

        var objectId = new ObjectId(user.getId());
        var dbUser = repo.findById(objectId);
        if(dbUser.isEmpty())
            return ResponseEntity.notFound().build();
        
        // Atualizar
        var dbUserObj = dbUser.get();
        dbUserObj.setName(user.getName());
        dbUserObj.setAge(user.getAge());
        
        return ResponseEntity.ok(new UserDTO(repo.save(dbUserObj)));
    }

    public ResponseEntity<?> delete(ObjectId id) {
        if(id != null)
            return ResponseEntity.badRequest().build();
        repo.deleteById(id);

        var dbUser = repo.findById(id);
        if(dbUser.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        return ResponseEntity.ok().build();
    }
}
