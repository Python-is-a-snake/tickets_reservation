package com.trs.tickets.service;

import com.mailboxvalidator.MBVResult;
import com.mailboxvalidator.SingleValidation;
import com.trs.tickets.configs.Role;
import com.trs.tickets.mappers.UserCreateDtoMapper;
import com.trs.tickets.mappers.UserMapper;
import com.trs.tickets.model.dto.UserCreateDto;
import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.model.entity.Ticket;
import com.trs.tickets.model.entity.User;
import com.trs.tickets.repository.TicketRepository;
import com.trs.tickets.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final UserCreateDtoMapper createMapper;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::convert).toList();
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::convert).orElse(null);
    }

    public List<UserDto> getNewUsersOfThisMonth(){
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        return userRepository.findAllByRegistrationDateBetween(startOfMonth, endOfMonth).stream().map(userMapper::convert).collect(Collectors.toList());
    }

    public UserDto addUser(UserCreateDto userCreateDto) {
        User user = createMapper.convert(userCreateDto);
        return userMapper.convert(userRepository.save(user));
    }

    //todo: fix constraint failure with session id
    public void deleteUser(Long id) {
        List<Ticket> ticketsByUserId = ticketRepository.findAllByUserId(id);
        ticketRepository.deleteAll(ticketsByUserId);
        ticketRepository.flush();
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading User with username: " + username);
        return userRepository.findByUsername(username).map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), List.of(user.getRole()))).orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }

    public UserDto getUserByUsername(String username) {
        log.info("Finding User with username: " + username);
        User user = userRepository.findByUsernameContainingIgnoreCase(username);
        return userMapper.convert(user);
    }

    public boolean usernameIsUnique(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public Page<UserDto> getAllUsersExcept(String username, Integer page, Integer size) {
//        List<UserDto> list = userRepository.findAll().stream().filter(user -> !user.getUsername().equals(username)).map(userMapper::convert).toList();

        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByUsernameNotIn(List.of(username), pageable).map(userMapper::convert);
    }

    public Page<UserDto> getUserByUsernameExcept(String username, String currentUserName, Integer page, Integer size) {
        log.info("Finding User with username: " + username + " except for " + currentUserName);
//        return userRepository.findByUsernameContainingIgnoreCase(username).stream().filter(user -> !user.getUsername().equals(currentUserName)).map(userMapper::convert).toList();

        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findByUsernameContainingIgnoreCaseAndUsernameNotIn(username, List.of(currentUserName),  pageable).map(userMapper::convert);
    }

    public void changeUserRole(Long id, Role role) {
        User user = userRepository.findById(id).get();
        user.setRole(role);
        userRepository.save(user);
    }

    public boolean isEmailCorrect(UserCreateDto userDto){
        try {
            SingleValidation mbv = new SingleValidation("ZJHY759VJQASZ5EISZEO");
            log.info("Validating email address with 'Mailbox Validation' :" + userDto.getUsername());
            MBVResult rec = mbv.ValidateEmail(userDto.getUsername());
            return rec.getStatus().equals("True");
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
