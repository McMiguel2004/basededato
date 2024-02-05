package com.example.restservice;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(this::convertToDTO).orElse(null);
    }

    public UserDTO addUser(UserDTO userDTO) {
        User newUser = convertToEntity(userDTO);
        return convertToDTO(userRepository.save(newUser));
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            BeanUtils.copyProperties(userDTO, existingUser, "id", "password");
            return convertToDTO(userRepository.save(existingUser));
        }
        return null;
    }

    public UserDTO partialUpdateUser(Integer id, Map<String, Object> updates) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            updates.forEach((key, value) -> {
                // Implementa la lógica de actualización parcial aquí
                switch (key) {
                    case "email":
                        existingUser.setEmail((String) value);
                        break;
                    case "password":
                        existingUser.setPassword((String) value);
                        break;
                    case "fullName":
                        existingUser.setFullName((String) value);
                        break;
                    // Agrega más casos según las propiedades que puedan actualizarse parcialmente
                }
            });
            return convertToDTO(userRepository.save(existingUser));
        }
        return null;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
