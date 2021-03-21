package com.limonnana.skate.web.rest;

import com.limonnana.skate.config.Constants;
import com.limonnana.skate.domain.ContributionForm;
import com.limonnana.skate.domain.Trick;
import com.limonnana.skate.domain.User;
import com.limonnana.skate.repository.TrickRepository;
import com.limonnana.skate.repository.UserRepository;
import com.limonnana.skate.security.AuthoritiesConstants;
import com.limonnana.skate.service.MailService;
import com.limonnana.skate.service.dto.PictureDTO;
import com.limonnana.skate.web.rest.errors.PhoneAlreadyUsedException;
import org.springframework.data.domain.Sort;
import java.util.Collections;
import com.limonnana.skate.service.UserService;
import com.limonnana.skate.service.dto.UserDTO;
import com.limonnana.skate.web.rest.errors.BadRequestAlertException;
import com.limonnana.skate.web.rest.errors.EmailAlreadyUsedException;
import com.limonnana.skate.web.rest.errors.LoginAlreadyUsedException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey"));

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final TrickRepository trickRepository;

    private final MailService mailService;

    public UserResource(TrickRepository trickRepository, UserService userService, UserRepository userRepository, MailService mailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.trickRepository = trickRepository;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        }
         else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName,  "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                .body(newUser);
        }
    }

    @PostMapping("/users/picture")
    public ResponseEntity<User> addProfilePicture(@Valid @RequestBody PictureDTO pictureDTO){

        User user = userRepository.findOneByLogin(pictureDTO.getLogin().toLowerCase()).get();

        if(user == null){
            throw new BadRequestAlertException(" user with that login doesn't exist ", "UserNULL", "UserNULL");
        }
        user.setPicture(pictureDTO.getPicture());
        user = userRepository.save(user);

        return ResponseUtil.wrapOrNotFound(Optional.of(user),
            HeaderUtil.createAlert(applicationName, "Picture has been updated", user.getLogin()));
    }


    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
       // Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getPhone());
       // if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
      //      throw new EmailAlreadyUsedException();
      //  }
        Optional<User>existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()));
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName,  "A user is deleted with identifier " + login, login)).build();
    }

    @PostMapping("/users/contribution")
    public ResponseEntity<User> createContribution(@Valid @RequestBody ContributionForm contributionForm) throws URISyntaxException {
        log.debug("REST request to save Contribution : {}", contributionForm);

        User user = new User();

        if(contributionForm.getUserId() != null){
            user = userRepository.findById(contributionForm.getUserId()).get();
        }else{

            String fullName = contributionForm.getUserFullName().trim();
            int location = fullName.indexOf(" ");
            String firstName = fullName.substring(0, location);
            String lastName = fullName.substring(location + 1);
            user.setLogin(contributionForm.getPhone());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(contributionForm.getPhone());
            user.setActivated(true);

            if (userRepository.findOneByLogin(user.getLogin().toLowerCase()).isPresent()) {
                throw new LoginAlreadyUsedException();

            } else if (userRepository.findOneByLogin(user.getPhone()).isPresent()){
                throw new PhoneAlreadyUsedException();
            }

            user = userService.registerUserFromContribution(user);
        }

        Trick trick = trickRepository.findById(contributionForm.getTrick().getId()).get();
        int ca = trick.getCurrentAmount().intValue();
        String amount = contributionForm.getAmount();
        int toAdd = Integer.parseInt(amount);
        int amountTotal =  ca + toAdd;
        trick.setCurrentAmount(amountTotal);
        trickRepository.save(trick);


        return ResponseEntity.created(new URI("/api/users/" + user.getLogin()))
            .headers(HeaderUtil.createAlert(applicationName,  "A user is created with identifier " + user.getLogin(), user.getLogin()))
            .body(user);
    }

    public User userDTOToUser(UserDTO userDTO){
        User user = new User();
        user.setLogin(userDTO.getPhone());
        user.setPhone(userDTO.getPhone());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
