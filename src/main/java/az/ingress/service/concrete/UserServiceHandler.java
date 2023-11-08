package az.ingress.service.concrete;

import az.ingress.aop.annotation.Log;
import az.ingress.aop.annotation.LogIgnore;
import az.ingress.dao.entity.UserEntity;
import az.ingress.dao.repository.UserRepository;
import az.ingress.exception.AlreadyExistsException;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.RegisterationRequest;
import az.ingress.model.response.UserResponse;
import az.ingress.service.abstraction.UserService;
import az.ingress.service.strategy.RegistrationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static az.ingress.mapper.RoleMapper.ROLE_MAPPER;
import static az.ingress.mapper.UserMapper.USER_MAPPER;
import static az.ingress.model.constant.ExceptionConstant.USER_ALREADY_EXISTS;
import static az.ingress.model.constant.ExceptionConstant.USER_NOT_FOUND;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class UserServiceHandler implements UserService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RegistrationStrategy registrationStrategy;

    @LogIgnore
    public UserResponse getUserByEmail(String email) {
        var user = fetchIfExist(email);
        return USER_MAPPER.mapEntityToResponse(user);
    }

    @SneakyThrows
    @Transactional
    public void registerUser(RegisterationRequest request) {
        if (checkIfExist(request.getEmail())) {
            throw new AlreadyExistsException(USER_ALREADY_EXISTS);
        }
        var user = getUserEntity(request);
        userRepository.save(user);
        registrationStrategy.register(request);
    }

    @LogIgnore
    private UserEntity fetchIfExist(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    @LogIgnore
    private boolean checkIfExist(String username) {
        return userRepository.existsByUsername(username);
    }

    private UserEntity getUserEntity(RegisterationRequest request) {
        var authority = ROLE_MAPPER.buildRoleEntity(request.getRole().name());
        var user = USER_MAPPER.mapRegistrationRequestToEntity(request, authority);
        authority.setUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }
}