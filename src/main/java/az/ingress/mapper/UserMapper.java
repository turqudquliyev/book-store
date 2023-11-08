package az.ingress.mapper;

import az.ingress.dao.entity.RoleEntity;
import az.ingress.dao.entity.UserEntity;
import az.ingress.model.request.RegisterationRequest;
import az.ingress.model.response.UserResponse;

import java.util.Set;
import java.util.stream.Collectors;

import static az.ingress.mapper.RoleMapper.ROLE_MAPPER;

public enum UserMapper {
    USER_MAPPER;

    public UserEntity mapRegistrationRequestToEntity(RegisterationRequest request, RoleEntity role) {
        return UserEntity.builder()
                .username(request.getEmail())
                .password(request.getPassword())
                .authorities(Set.of(role))
                .build();
    }

    public UserResponse mapEntityToResponse(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .email(entity.getUsername())
                .roles(entity.getAuthorities().stream().map(ROLE_MAPPER::mapEntityToResponse).collect(Collectors.toSet()))
                .build();
    }
}