package it.lucianofilippucci.tcgarkhive.services;

import it.lucianofilippucci.tcgarkhive.API.V1.DTO.UserDTO;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.UserRolesEditRequest;
import it.lucianofilippucci.tcgarkhive.entity.RolesEntity;
import it.lucianofilippucci.tcgarkhive.entity.UserEntity;
import it.lucianofilippucci.tcgarkhive.helpers.enums.Actions;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.RoleNotExistsException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.UserNotExistsException;
import it.lucianofilippucci.tcgarkhive.repository.RolesRepository;
import it.lucianofilippucci.tcgarkhive.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.*;

@Service
public class AdministrationService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;

    public AdministrationService(UserRepository userRepository, RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }


    @Transactional
    public boolean EditUserRoles(UserRolesEditRequest req) {
        Optional<UserEntity> tmp = this.userRepository.findById(req.getUserId());
        if(tmp.isEmpty()) throw new UserNotExistsException("");
        UserEntity user = tmp.get();

        Optional<RolesEntity> tmp2 = this.rolesRepository.findById(req.getRoleId());
        if(tmp2.isEmpty()) throw new RoleNotExistsException("");
        RolesEntity role = tmp2.get();



        switch (req.getAction()) {
            case Actions.ADD: {
                user.getRoles().add(role);
                break;
            }
            case Actions.REMOVE: {
                user.getRoles().remove(role);
                break;
            }
        }

        UserEntity tester = this.userRepository.save(user);

        return true;
    }

    @Transactional
    public Set<UserDTO> fetchUserList(Pageable pageable) {
        Page<UserEntity> userPage = this.userRepository.findAll(pageable);
        return this.UserEntityToDTO(userPage.stream().toList());
    }



    private Set<UserDTO> UserEntityToDTO(List<UserEntity> users) {
        Set<UserDTO> userDTOS = new HashSet<>();
        for(UserEntity user : users) {
            UserDTO dto = new UserDTO();

            dto.setEmail(user.getEmail());
            dto.setUsername(user.getUsername());
            dto.setIsActive(user.isActive());
            dto.setUserRoles(user.getRoles());

            userDTOS.add(dto);
        }

        return userDTOS;
    }
}
