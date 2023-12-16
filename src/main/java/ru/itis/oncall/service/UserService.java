package ru.itis.oncall.service;

import ru.itis.oncall.db.entity.User;
import ru.itis.oncall.db.repository.UserRepository;
import ru.itis.oncall.dto.SignUpDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void save(SignUpDTO signUpDTO) {
        userRepository.persist(dtoToUser(signUpDTO));
    }

    @Transactional
    public void save(List<SignUpDTO> signUpDTOs) {
        var users = signUpDTOs.stream()
                .map(this::dtoToUser)
                .collect(Collectors.toList());
        userRepository.persist(users);
    }

    private User dtoToUser(SignUpDTO signUpDTO) {
        return User.builder()
                .firstName(signUpDTO.getFirstName())
                .lastName(signUpDTO.getLastName())
                .email(signUpDTO.getEmail())
                .password(signUpDTO.getPassword())
                .build();
    }
}
