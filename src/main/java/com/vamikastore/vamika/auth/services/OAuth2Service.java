package com.vamikastore.vamika.auth.services;

import com.vamikastore.vamika.auth.entities.User;
import com.vamikastore.vamika.auth.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.vamikastore.vamika.auth.entities.User.*;

@Service
public class OAuth2Service {

    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    private AuthorityService authorityService;

    public User getUser(String userName) {
        return userDetailRepository.findByEmail(userName);
    }

    public User createUser(OAuth2User oAuth2User, String provider) {
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String email = oAuth2User.getAttribute("email");
        String phoneNumber = oAuth2User.getAttribute("phone_number");
        String createdOn = oAuth2User.getAttribute("created_on");
        String updatedOn = oAuth2User.getAttribute("updated_on");
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .provider(provider)
                .enabled(true)
                .authorities(authorityService.getUserAuthority())
                .build();
        return userDetailRepository.save(user);
    }
}
