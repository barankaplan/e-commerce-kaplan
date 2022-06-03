package backend.admin.user.service;


import backend.admin.user.repository.RoleRepository;
import backend.admin.user.repository.UserRepository;
import common.data.entity.Role;
import common.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int USERS_PER_PAGE = 4;

    public int getUsersPerPage() {
        return USERS_PER_PAGE;
    }

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listAll() {
        return userRepository.findAll();
    }

//    public Page<User> listByPage(int pageNumber){
//        Pageable pageable= PageRequest.of(pageNumber-1,USERS_PER_PAGE);
//        return userRepository.findAll(pageable);
//    }

    public Page<User> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);
        if (keyword != null) {
            return userRepository.findAll(keyword.toLowerCase(Locale.ROOT), pageable);
        }
        return userRepository.findAll(pageable);
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }


    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);

        }
        return userRepository.save(user);

    }

    public void encodePassword(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }

    public boolean isEmailUnique(Long id, String eMail) {
        Optional<User> userByEmail = userRepository.getUserByEmail(eMail);

        if (userByEmail.isEmpty()) {
            return true;
        }

        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {

            if (userByEmail != null) return false;
        } else {
            if (userByEmail.get().getId() != id) {
                return false;
            }
        }
        return true;
    }

    public User get(Long id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("Couldn't find any user with ID: " + id);
        }
    }

    public void delete(Long id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Couldn't find any user with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Long id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }
}
