package az.ingress.mapper;

import az.ingress.dao.entity.RoleEntity;
import az.ingress.model.response.RoleResponse;

public enum RoleMapper {
    ROLE_MAPPER;

    public RoleEntity buildRoleEntity(String role) {
        return RoleEntity.builder()
                .authority(role)
                .build();
    }

    public RoleResponse mapEntityToResponse(RoleEntity entity) {
        return RoleResponse.builder()
                .authority(entity.getAuthority())
                .build();
    }
}