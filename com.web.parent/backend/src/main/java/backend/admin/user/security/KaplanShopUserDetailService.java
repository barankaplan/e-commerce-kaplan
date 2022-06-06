package backend.admin.user.security;

import backend.admin.user.repository.UserRepository;
import common.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class KaplanShopUserDetailService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.getUserByEmail(email);
        if (userByEmail.isPresent()) {
            return new KaplanShopDetails(userByEmail.get());
        }
        throw new UsernameNotFoundException("Couldn't find user with email: " + email);
    }
}
